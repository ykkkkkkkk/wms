package ykk.xc.com.wms.purchase

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import butterknife.OnClick
import kotlinx.android.synthetic.main.pur_in_stock_fragment1.tv_suppSel
import kotlinx.android.synthetic.main.pur_in_stock_fragment2.*
import okhttp3.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import ykk.xc.com.wms.R
import ykk.xc.com.wms.basics.Mtl_DialogActivity
import ykk.xc.com.wms.basics.StockPos_DialogActivity
import ykk.xc.com.wms.basics.Stock_DialogActivity
import ykk.xc.com.wms.bean.*
import ykk.xc.com.wms.bean.k3Bean.ICInventory
import ykk.xc.com.wms.bean.k3Bean.ICItem
import ykk.xc.com.wms.bean.k3Bean.MeasureUnit
import ykk.xc.com.wms.bean.pur.POInStockEntry
import ykk.xc.com.wms.bean.pur.POOrderEntry
import ykk.xc.com.wms.comm.BaseFragment
import ykk.xc.com.wms.comm.Comm
import ykk.xc.com.wms.util.BigdecimalUtil
import ykk.xc.com.wms.util.JsonUtil
import ykk.xc.com.wms.util.LogUtil
import ykk.xc.com.wms.util.zxing.android.CaptureActivity
import java.io.IOException
import java.lang.ref.WeakReference
import java.text.DecimalFormat
import java.util.concurrent.TimeUnit

/**
 * 日期：2019-10-16 09:50
 * 描述：采购入库---添加明细
 * 作者：ykk
 */
class Pur_InStock_Fragment2 : BaseFragment() {

    companion object {
        private val SEL_STOCK = 11
        private val SEL_STOCKPOS = 12
        private val SEL_MTL = 13
        private val SEL_UNIT = 14
        private val SUCC1 = 200
        private val UNSUCC1 = 500
        private val SUCC2 = 201
        private val UNSUCC2 = 501
        private val SUCC3 = 202
        private val UNSUCC3 = 502
        private val SAVE = 203
        private val UNSAVE = 503

        private val SETFOCUS = 1
        private val SAOMA = 2
        private val RESULT_PRICE = 3
        private val RESULT_NUM = 4
        private val RESULT_BATCH = 5
        private val RESULT_REMAREK = 6
        private val WRITE_CODE = 7
        private val RESULT_PUR_ORDER = 8
    }
    private val context = this
    private var okHttpClient: OkHttpClient? = null
    private var user: User? = null
    private var stock:Stock? = null
    private var stockPos:StockPosition? = null
    private var mContext: Activity? = null
    private val df = DecimalFormat("#.######")
    private var parent: Pur_InStock_MainActivity? = null
    private var isTextChange: Boolean = false // 是否进入TextChange事件
    private var timesTamp:String? = null // 时间戳
    private var icStockBillEntry = ICStockBillEntry()

    // 消息处理
    private val mHandler = MyHandler(this)
    private class MyHandler(activity: Pur_InStock_Fragment2) : Handler() {
        private val mActivity: WeakReference<Pur_InStock_Fragment2>

        init {
            mActivity = WeakReference(activity)
        }

        override fun handleMessage(msg: Message) {
            val m = mActivity.get()
            if (m != null) {
                m.hideLoadDialog()

                var errMsg: String? = null
                var msgObj: String? = null
                if (msg.obj is String) {
                    msgObj = msg.obj as String
                }
                when (msg.what) {
                    SUCC1 -> { // 扫码成功 进入
                        val icItem = JsonUtil.strToObject(msgObj, ICItem::class.java)
                        m.getMtlAfter(icItem)
                    }
                    UNSUCC1 -> { // 扫码失败
                        errMsg = JsonUtil.strToString(msgObj)
                        if (m.isNULLS(errMsg).length == 0) errMsg = "很抱歉，没有找到数据！"
                        Comm.showWarnDialog(m.mContext, errMsg)
                    }
                    SUCC2 -> { // 查询库存 进入
                        val list = JsonUtil.strToList(msgObj, ICInventory::class.java)
                        m.tv_stockQty.text = m.df.format(list[0].getfQty())
                    }
                    UNSUCC2 -> { // 查询库存  失败
                        m.tv_stockQty.text = "0"
                    }
                    SUCC3 -> { // 查询仓库库位 进入
                        var list = JsonUtil.strToList<List<String>>(msgObj)
                        if(list.size == 1) { // 一个对象，仓库
                            m.stock = null
                            m.stock = JsonUtil.stringToObject(list[0].toString(), Stock::class.java)

                        } else if(list.size == 2) { // 两个对象，仓库和库位
                            m.stock = null
                            m.stockPos = null
                            m.stock = JsonUtil.stringToObject(list[0].toString(), Stock::class.java)
                            m.stockPos = JsonUtil.stringToObject(list[1].toString(), StockPosition::class.java)
                        }
                    }
                    UNSUCC3 -> { // 查询仓库库位    失败
                        Comm.showWarnDialog(m.mContext,"请检查仓库是否设置为禁用！")
                    }
                    SAVE -> { // 保存成功 进入
                        // 保存了分录，供应商就不能修改
                        m.setEnables(m.parent!!.fragment1.tv_suppSel, R.drawable.back_style_gray2a,false)
                        EventBus.getDefault().post(EventBusEntity(21)) // 发送指令到fragment3，告其刷新
                        m.reset(1)
                        m.toasts("保存成功✔")
                    }
                    UNSAVE -> { // 保存失败
                        errMsg = JsonUtil.strToString(msgObj)
                        if (m.isNULLS(errMsg).length == 0) errMsg = "保存失败！"
                        Comm.showWarnDialog(m.mContext, errMsg)
                    }
                    SETFOCUS -> { // 当弹出其他窗口会抢夺焦点，需要跳转下，才能正常得到值
                        m.setFocusable(m.et_getFocus)
                        m.setFocusable(m.et_code)
                    }
                    SAOMA -> { // 扫码之后
                        // 执行查询方法
                        m.run_smDatas()
                    }
                }
            }
        }
    }

    @Subscribe
    fun onEventBus(entity: EventBusEntity) {
        when (entity.caseId) {
            11 -> { // 接收第一个页面发来的指令
                reset(0)
            }
            31 -> { // 接收第三个页面发来的指令
                var icEntry = entity.obj as ICStockBillEntry
                btn_save.text = "保存"
                getICStockBillEntry(icEntry)
            }
        }
    }

    override fun setLayoutResID(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.pur_in_stock_fragment2, container, false)
    }

    override fun initView() {
        mContext = getActivity()
        parent = mContext as Pur_InStock_MainActivity
        EventBus.getDefault().register(this) // 注册EventBus

    }

    override fun initData() {
        if (okHttpClient == null) {
            okHttpClient = OkHttpClient.Builder()
                    //                .connectTimeout(10, TimeUnit.SECONDS) // 设置连接超时时间（默认为10秒）
                    .writeTimeout(30, TimeUnit.SECONDS) // 设置写的超时时间
                    .readTimeout(30, TimeUnit.SECONDS) //设置读取超时时间
                    .build()
        }

        getUserInfo()
        timesTamp = user!!.getId().toString() + "-" + Comm.randomUUID()
        hideSoftInputMode(mContext, et_code)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            mHandler.sendEmptyMessageDelayed(SETFOCUS, 200)
            // 得到上个页面的仓库对象
//            stock = parent!!.fragment1.icStockBill.stock
//            icStockBillEntry.fdcStockId = stock!!.fitemId
//            icStockBillEntry.inStockName = stock!!.fname
//            tv_stockSel.text = stock!!.fname
//            // 如果启用了库位
//            if(stock!!.fisStockMgr == 1) {
//                setEnables(tv_stockPosSel, R.drawable.back_style_blue, true)
//            } else {
//                setEnables(tv_stockPosSel, R.drawable.back_style_gray3, false)
//            }
            icStockBillEntry.fdcSPId = 0
            icStockBillEntry.inStockPosName = ""
        }
    }

    @OnClick(R.id.tv_sourceTypeSel, R.id.btn_sourceNoSel, R.id.btn_scan, R.id.btn_mtlSel, R.id.tv_price, R.id.tv_num, R.id.tv_batchNo, R.id.tv_unitSel, R.id.tv_remark, R.id.btn_save, R.id.btn_clone)
    fun onViewClicked(view: View) {
        when (view.id) {
            R.id.tv_sourceTypeSel -> { // 选择来源类型
                pop_sourceType(view)
                popWindow!!.showAsDropDown(view)
            }
            R.id.btn_sourceNoSel -> { // 选择来源单据
                val strFdetailId = StringBuffer()
                parent!!.fragment3.checkDatas.forEach {
                    strFdetailId.append(it.fdetailId.toString()+",")
                }
                if(strFdetailId.length > 0) { // 刪除最后一个，
                    strFdetailId.delete(strFdetailId.length-1, strFdetailId.length)
                }
                val bundle = Bundle()
                bundle.putInt("suppId", parent!!.fragment1.icStockBill.fsupplyId)
                bundle.putString("suppName", parent!!.fragment1.icStockBill.suppName)
                bundle.putString("strFdetailId", strFdetailId.toString())
                when(icStockBillEntry.fsourceTranType) {
                    71 -> showForResult(Pur_InStock_Sel_POOOrderActivity::class.java, RESULT_PUR_ORDER, bundle)
                    72 -> showForResult(Pur_InStock_Sel_POInStockActivity::class.java, RESULT_PUR_ORDER, bundle)
                }
            }
//            R.id.tv_stockSel -> { // 选择仓库
//                val bundle = Bundle()
//                bundle.putString("accountType", "SC")
//                bundle.putInt("unDisable", 1) // 只显示未禁用的数据
//                showForResult(Stock_DialogActivity::class.java, SEL_STOCK, bundle)
//            }
//            R.id.tv_stockPosSel -> { // 选择库位
//                val bundle = Bundle()
//                bundle.putInt("fspGroupId", stock!!.fspGroupId)
//                bundle.putString("accountType", "SC")
//                showForResult(StockPos_DialogActivity::class.java, SEL_STOCKPOS, bundle)
//            }
            R.id.btn_mtlSel -> { // 选择物料
                val bundle = Bundle()
                bundle.putString("accountType", "SC")
                showForResult(Mtl_DialogActivity::class.java, SEL_MTL, bundle)
            }
            R.id.btn_scan -> { // 调用摄像头扫描（物料）
                showForResult(CaptureActivity::class.java, BaseFragment.CAMERA_SCAN, null)
            }
            R.id.tv_price -> { // 单价
                showInputDialog("单价", icStockBillEntry.fprice.toString(), "0.0", RESULT_PRICE)
            }
            R.id.tv_num -> { // 数量
                showInputDialog("数量", icStockBillEntry.fqty.toString(), "0.0", RESULT_NUM)
            }
            R.id.tv_batchNo -> { // 批次号
                showInputDialog("批次号", icStockBillEntry.fbatchNo, "none", RESULT_BATCH)
            }
            R.id.tv_unitSel -> { // 单位
//                val bundle = Bundle()
//                bundle.putString("accountType", "SC")
//                showForResult(Unit_DialogActivity::class.java, SEL_UNIT, bundle)
            }
            R.id.tv_remark -> { // 备注
                showInputDialog("备注", icStockBillEntry.remark, "none", RESULT_REMAREK)
            }
            R.id.btn_save -> { // 保存
                if(!checkSave()) return
                icStockBillEntry.icstockBillId = parent!!.fragment1.icStockBill.id
                run_save(null);
            }
            R.id.btn_clone -> { // 重置
                if (checkSaveHint()) {
                    val build = AlertDialog.Builder(mContext)
                    build.setIcon(R.drawable.caution)
                    build.setTitle("系统提示")
                    build.setMessage("您有未保存的数据，继续重置吗？")
                    build.setPositiveButton("是") { dialog, which -> reset(0) }
                    build.setNegativeButton("否", null)
                    build.setCancelable(false)
                    build.show()

                } else {
                    reset(0)
                }
            }
        }
    }

    /**
     * 检查数据
     */
    fun checkSave() : Boolean {
        if(icStockBillEntry.fitemId == 0) {
            Comm.showWarnDialog(mContext, "请扫码物料或选择物料！")
            return false
        }
        if (icStockBillEntry.fdcStockId == 0 || stock == null) {
            Comm.showWarnDialog(mContext, "请选择位置！")
            return false;
        }
        if (stock!!.fisStockMgr == 1 && icStockBillEntry.fdcStockId == 0) {
            Comm.showWarnDialog(mContext, "请选择库位！")
            return false;
        }
//        if (icStockBillEntry.fprice == 0.0) {
//            Comm.showWarnDialog(mContext, "请输入单价！")
//            return false;
//        }
        if (icStockBillEntry.fqty == 0.0) {
            Comm.showWarnDialog(mContext, "请输入数量！")
            return false;
        }
        return true;
    }

    /**
     * 选择了物料没有点击保存，点击了重置，需要提示
     */
    fun checkSaveHint() : Boolean {
        if(icStockBillEntry.fitemId > 0) {
            return true
        }
        return false
    }

    override fun setListener() {
        val click = View.OnClickListener { v ->
            setFocusable(et_getFocus)
            when (v.id) {
                R.id.et_code -> setFocusable(et_code)
            }
        }
        et_code!!.setOnClickListener(click)

        // 物料---数据变化
        et_code!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (s.length == 0) return
                if (!isTextChange) {
                    isTextChange = true
                    mHandler.sendEmptyMessageDelayed(SAOMA, 300)
                }
            }
        })
        // 物料---长按输入条码
        et_code!!.setOnLongClickListener {
            showInputDialog("输入条码", "", "none", WRITE_CODE)
            true
        }
    }

    /**
     * 0：表示点击重置，1：表示保存后重置
     */
    private fun reset(flag : Int) {
        if(parent!!.fragment1.icStockBill.fselTranType == 0 && flag == 0 ) {
            setEnables(tv_sourceTypeSel, R.drawable.back_style_blue2, true)
            tv_sourceTypeSel.text = "无源单"
            btn_sourceNoSel.visibility = View.GONE
            lin_getMtl.visibility = View.VISIBLE
//            tv_stockSel.text = ""
//            tv_stockPosSel.text = ""
//            setEnables(tv_stockPosSel, R.drawable.back_style_gray3, false)
            icStockBillEntry.fsourceTranType = 0
            icStockBillEntry.fdcStockId = 0
            icStockBillEntry.fdcSPId = 0
            icStockBillEntry.inStockName = ""
            icStockBillEntry.inStockPosName = ""
            stock = null
            stockPos = null
        }
        btn_save.text = "添加"
        tv_mtlName.text = ""
        tv_mtlNumber.text = ""
        tv_fmodel.text = ""
        tv_sumMoney.text = ""
        tv_stockQty.text = ""
        tv_price.text = ""
        tv_batchNo.text = ""
        tv_num.text = ""
        tv_unitSel.text = ""
        tv_remark.text = ""

        icStockBillEntry.id = 0
        icStockBillEntry.icstockBillId = parent!!.fragment1.icStockBill.id
        icStockBillEntry.fitemId = 0
//        icStockBillEntry.fdcStockId = 0
//        icStockBillEntry.fdcSPId = 0
        icStockBillEntry.fbatchNo = ""
        icStockBillEntry.fqty = 0.0
        icStockBillEntry.fprice = 0.0
        icStockBillEntry.funitId = 0
        icStockBillEntry.mtlName = ""
        icStockBillEntry.mtlNumber = ""
        icStockBillEntry.fmode = ""
//        icStockBillEntry.inStockName = ""
//        icStockBillEntry.inStockPosName = ""
        icStockBillEntry.unitName = ""
        icStockBillEntry.remark = ""
//        stock = null
//        stockPos = null
        timesTamp = user!!.getId().toString() + "-" + Comm.randomUUID()
        parent!!.isChange = false
        mHandler.sendEmptyMessageDelayed(SETFOCUS, 200)
    }

    fun getICStockBillEntry(icEntry: ICStockBillEntry) {
        icStockBillEntry.id = icEntry.id
        icStockBillEntry.icstockBillId = icEntry.icstockBillId
        icStockBillEntry.finterId = icEntry.finterId
        icStockBillEntry.fitemId = icEntry.fitemId
        icStockBillEntry.fentryId = icEntry.fentryId
        icStockBillEntry.fdcStockId = icEntry.fdcStockId
        icStockBillEntry.fdcSPId = icEntry.fdcSPId
        icStockBillEntry.fbatchNo = icEntry.fbatchNo
        icStockBillEntry.fqty = icEntry.fqty
        icStockBillEntry.fprice = icEntry.fprice
        icStockBillEntry.funitId = icEntry.funitId
        icStockBillEntry.mtlNumber = icEntry.mtlNumber
        icStockBillEntry.mtlName = icEntry.mtlName
        icStockBillEntry.fmode = icEntry.fmode
        icStockBillEntry.inStockName = icEntry.inStockName
        icStockBillEntry.inStockPosName = icEntry.inStockPosName
        icStockBillEntry.unitName = icEntry.unitName
        icStockBillEntry.remark = icEntry.remark

        tv_mtlName.text = icEntry.mtlName
        tv_mtlNumber.text = icEntry.mtlNumber
        tv_fmodel.text = icEntry.fmode
//        tv_stockSel.text = icEntry.inStockName
//        tv_stockPosSel.text = icEntry.inStockPosName
        tv_price.text = df.format(icEntry.fprice)
        tv_batchNo.text = icEntry.fbatchNo
        tv_num.text = df.format(icEntry.fqty)
        tv_unitSel.text = icEntry.unitName
        tv_remark.text = icEntry.remark

//        if(icEntry.fdcSPId > 0) {
//            setEnables(tv_stockPosSel, R.drawable.back_style_blue, true)
//        } else {
//            setEnables(tv_stockPosSel, R.drawable.back_style_gray3, false)
//        }
        val mul = BigdecimalUtil.mul(icEntry.fprice, icEntry.fqty)
        tv_sumMoney.text = df.format(mul)
        // 查询即时库存
        run_findInventoryQty()
        // 查询仓库和库位对象
        run_findStockAndStockPos()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            SEL_STOCK -> {// 仓库	返回
                if (resultCode == Activity.RESULT_OK) {
                    stock = data!!.getSerializableExtra("obj") as Stock
//                    tv_stockSel.text = stock!!.fname
                    // 如果启用了库位
                    if(stock!!.fisStockMgr == 1) {
//                        setEnables(tv_stockPosSel, R.drawable.back_style_blue, true)
                        var bundle = Bundle()
                        bundle.putInt("fspGroupId", stock!!.fspGroupId);
                        showForResult(StockPos_DialogActivity::class.java, SEL_STOCKPOS, bundle)

                    } else {
//                        setEnables(tv_stockPosSel, R.drawable.back_style_gray3, false)
                    }
                    stockPos = null
//                    tv_stockPosSel.text = ""

                    icStockBillEntry.fdcStockId = stock!!.fitemId
                    icStockBillEntry.inStockName = stock!!.fname
                    icStockBillEntry.fdcSPId = 0
                    icStockBillEntry.inStockPosName = ""
                    // 满足条件就查询库存
                    if(icStockBillEntry.fdcStockId > 0 && icStockBillEntry.fitemId > 0) {
                        run_findInventoryQty()
                    }
                }
            }
            SEL_STOCKPOS -> {// 库位	返回
                if (resultCode == Activity.RESULT_OK) {
                    stockPos = data!!.getSerializableExtra("obj") as StockPosition
//                    tv_stockPosSel.text = stockPos!!.fname
//                    icStockBillEntry.fdcSPId = stockPos!!.fspId
                    icStockBillEntry.inStockPosName = stockPos!!.fname
                    // 满足条件就查询库存
                    if(icStockBillEntry.fdcStockId > 0 && icStockBillEntry.fitemId > 0) {
                        run_findInventoryQty()
                    }
                }
            }
            SEL_MTL -> { //查询物料	返回
                if (resultCode == Activity.RESULT_OK) {
                    val icItem = data!!.getSerializableExtra("obj") as ICItem
                    getMtlAfter(icItem)
                }
            }
            SEL_UNIT -> { //查询单位	返回
                if (resultCode == Activity.RESULT_OK) {
                    val unit = data!!.getSerializableExtra("obj") as MeasureUnit
                    tv_unitSel.text = unit.getfName()
                    icStockBillEntry.funitId = unit.fitemID
                }
            }
            RESULT_PRICE -> { // 单价	返回
                if (resultCode == Activity.RESULT_OK) {
                    val bundle = data!!.getExtras()
                    if (bundle != null) {
                        val value = bundle.getString("resultValue", "")
                        val price = parseDouble(value)
                        tv_price.text = df.format(price)
                        icStockBillEntry.fprice = price
                        if(icStockBillEntry.fqty > 0) {
                            val mul = BigdecimalUtil.mul(price, icStockBillEntry.fqty)
                            tv_sumMoney.text = df.format(mul)
                        }
                    }
                }
            }
            RESULT_NUM -> { // 数量	返回
                if (resultCode == Activity.RESULT_OK) {
                    val bundle = data!!.getExtras()
                    if (bundle != null) {
                        val value = bundle.getString("resultValue", "")
                        val num = parseDouble(value)
                        tv_num.text = df.format(num)
                        icStockBillEntry.fqty = num
                        if(icStockBillEntry.fprice > 0) {
                            val mul = BigdecimalUtil.mul(num, icStockBillEntry.fprice)
                            tv_sumMoney.text = df.format(mul)
                        }
                    }
                }
            }
            RESULT_BATCH -> { // 批次号	返回
                if (resultCode == Activity.RESULT_OK) {
                    val bundle = data!!.getExtras()
                    if (bundle != null) {
                        val value = bundle.getString("resultValue", "")
                        tv_batchNo.text = value
                        icStockBillEntry.fbatchNo = value
                    }
                }
            }
            RESULT_REMAREK -> { // 备注	返回
                if (resultCode == Activity.RESULT_OK) {
                    val bundle = data!!.getExtras()
                    if (bundle != null) {
                        val value = bundle.getString("resultValue", "")
                        tv_remark.text = value
                        icStockBillEntry.remark = value
                    }
                }
            }
            RESULT_PUR_ORDER -> { // 选择单据   返回
                if (resultCode == Activity.RESULT_OK) {
                    if(icStockBillEntry.fsourceTranType == 71) {
                        val list = data!!.getSerializableExtra("obj") as List<POOrderEntry>
                        setICStockEntry_POOrder(list)
                    } else if(icStockBillEntry.fsourceTranType == 72){
                        val list = data!!.getSerializableExtra("obj") as List<POInStockEntry>
                        setICStockEntry_POInStock(list)
                    }
                    setEnables(tv_sourceTypeSel, R.drawable.back_style_gray2a, false)
                }
            }
            BaseFragment.CAMERA_SCAN -> {// 扫一扫成功  返回
                if (resultCode == Activity.RESULT_OK) {
                    val bundle = data!!.extras
                    if (bundle != null) {
                        val code = bundle.getString(BaseFragment.DECODED_CONTENT_KEY, "")
                        setTexts(et_code, code)
                    }
                }
            }
            WRITE_CODE -> {// 输入条码  返回
                if (resultCode == Activity.RESULT_OK) {
                    val bundle = data!!.extras
                    if (bundle != null) {
                        val value = bundle.getString("resultValue", "")
                        et_code!!.setText(value.toUpperCase())
                    }
                }
            }
        }
    }

    /**
     * 得到扫码或选择数据
     */
    private fun getMtlAfter(icItem : ICItem) {
        parent!!.isChange = true
        tv_mtlName.text = icItem.fname
        tv_mtlNumber.text = icItem.fnumber
        tv_fmodel.text = icItem.fmodel
        tv_unitSel.text = icItem.unit.unitName
        tv_stockQty.text = df.format(icItem.inventoryQty)

        icStockBillEntry.fitemId = icItem.fitemid
        icStockBillEntry.mtlNumber = icItem.fnumber
        icStockBillEntry.fmode = icItem.fmodel
        icStockBillEntry.mtlName = icItem.fname
        icStockBillEntry.funitId = icItem.unit.funitId
        icStockBillEntry.unitName = icItem.unit.unitName
        // 满足条件就查询库存
        if(icItem.inventoryQty <= 0 && icStockBillEntry.fdcStockId > 0 && icStockBillEntry.fitemId > 0) {
            run_findInventoryQty()
        }
    }

    private fun setICStockEntry_POOrder(list : List<POOrderEntry>) {
        parent!!.fragment1.icStockBill.fselTranType = 71
        var listEntry = ArrayList<ICStockBillEntry>()
        list.forEach {
            val entry = ICStockBillEntry()
            entry.icstockBillId = parent!!.fragment1.icStockBill.id
            entry.fitemId = it.icItem.fitemid
//            entry.fentryId = it.fentryid
            entry.fdcStockId = icStockBillEntry.fdcStockId
            entry.fdcSPId = icStockBillEntry.fdcSPId
            entry.fqty = it.useableQty
            entry.fprice = it.fprice
            entry.funitId = it.funitid
            entry.fsourceInterId = it.finterid
            entry.fsourceEntryId = it.fentryid
            entry.fsourceTranType = 71
            entry.fsourceBillNo = it.fbillno
            entry.fdetailId = it.fdetailid

            entry.mtlNumber = it.icItem.fnumber
            entry.mtlName = it.icItem.fname
            entry.fmode = it.icItem.fmodel
            entry.inStockName = icStockBillEntry.inStockName
            entry.inStockPosName = ""
            entry.unitName = it.unitName
            entry.remark = ""
            listEntry.add(entry)
        }
        run_save(listEntry)
    }

    private fun setICStockEntry_POInStock(list : List<POInStockEntry>) {
        parent!!.fragment1.icStockBill.fselTranType = 72
        var listEntry = ArrayList<ICStockBillEntry>()
        list.forEach {
            val entry = ICStockBillEntry()
            entry.icstockBillId = parent!!.fragment1.icStockBill.id
            entry.fitemId = it.icItem.fitemid
//            entry.fentryId = it.fentryid
            entry.fdcStockId = icStockBillEntry.fdcStockId
            entry.fdcSPId = icStockBillEntry.fdcSPId
            entry.fqty = it.useableQty
            entry.fprice = it.fprice
            entry.funitId = it.funitid
            entry.fsourceInterId = it.finterid
            entry.fsourceEntryId = it.fentryid
            entry.fsourceTranType = 72
            entry.fsourceBillNo = it.fbillno
            entry.fdetailId = it.fdetailid

            entry.mtlNumber = it.icItem.fnumber
            entry.mtlName = it.icItem.fname
            entry.fmode = it.icItem.fmodel
            entry.inStockName = icStockBillEntry.inStockName
            entry.inStockPosName = ""
            entry.unitName = it.unitName
            entry.remark = ""
            listEntry.add(entry)
        }
        run_save(listEntry)
    }

    /**
     * 扫码查询对应的方法
     */
    private fun run_smDatas() {
        isTextChange = false
        showLoadDialog("加载中...", false)
        var mUrl:String? = getURL("stockBill_WMS/findBarcode")
        val formBody = FormBody.Builder()
                .add("barcode", getValues(et_code))
                .add("fStockID", if(stock != null) stock!!.fitemId.toString() else "" )
//                .add("fStockPlaceID", if(stockPos != null) stockPos!!.fspId.toString() else "" )
                .build()

        val request = Request.Builder()
                .addHeader("cookie", getSession())
                .url(mUrl)
                .post(formBody)
                .build()

        val call = okHttpClient!!.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                mHandler.sendEmptyMessage(UNSUCC1)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()
                val result = body.string()
                LogUtil.e("run_smDatas --> onResponse", result)
                if (!JsonUtil.isSuccess(result)) {
                    val msg = mHandler.obtainMessage(UNSUCC1, result)
                    mHandler.sendMessage(msg)
                    return
                }
                val msg = mHandler.obtainMessage(SUCC1, result)
                mHandler.sendMessage(msg)
            }
        })
    }

    /**
     * 保存
     */
    private fun run_save(list: List<ICStockBillEntry>?) {
        showLoadDialog("保存中...", false)
        var mUrl:String? = null
        var mJson:String? = null
        if(list != null) {
            mUrl = getURL("stockBill_WMS/saveEntryList")
            mJson = JsonUtil.objectToString(list)
        } else {
            mUrl = getURL("stockBill_WMS/saveEntry")
            mJson = JsonUtil.objectToString(icStockBillEntry)
        }
        val formBody = FormBody.Builder()
                .add("strJson", mJson)
                .build()

        val request = Request.Builder()
                .addHeader("cookie", getSession())
                .url(mUrl)
                .post(formBody)
                .build()

        val call = okHttpClient!!.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                mHandler.sendEmptyMessage(UNSAVE)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()
                val result = body.string()
                if (!JsonUtil.isSuccess(result)) {
                    val msg = mHandler.obtainMessage(UNSAVE, result)
                    mHandler.sendMessage(msg)
                    return
                }
                val msg = mHandler.obtainMessage(SAVE, result)
                LogUtil.e("run_save --> onResponse", result)
                mHandler.sendMessage(msg)
            }
        })
    }

    /**
     * 查询库存
     */
    private fun run_findInventoryQty() {
        isTextChange = false
        showLoadDialog("加载中...", false)
        val mUrl = getURL("icInventory/findInventoryQty")
        val formBody = FormBody.Builder()
                .add("fStockID", icStockBillEntry.fdcStockId.toString())
                .add("fStockPlaceID",  icStockBillEntry.fdcSPId.toString())
                .add("mtlId", icStockBillEntry.fitemId.toString())
                .build()

        val request = Request.Builder()
                .addHeader("cookie", getSession())
                .url(mUrl)
                .post(formBody)
                .build()

        val call = okHttpClient!!.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                mHandler.sendEmptyMessage(UNSUCC2)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()
                val result = body.string()
                LogUtil.e("run_findListByParamWms --> onResponse", result)
                if (!JsonUtil.isSuccess(result)) {
                    val msg = mHandler.obtainMessage(UNSUCC2, result)
                    mHandler.sendMessage(msg)
                    return
                }
                val msg = mHandler.obtainMessage(SUCC2, result)
                mHandler.sendMessage(msg)
            }
        })
    }

    /**
     * 查询仓库和库位对象
     */
    private fun run_findStockAndStockPos() {
        val mUrl = getURL("stock/findStockAndStockPos")
        val formBody = FormBody.Builder()
                .add("fitemId", icStockBillEntry.fdcStockId.toString()) // 仓库id
                .add("fspId",  icStockBillEntry.fdcSPId.toString()) // 库位id
                .build()

        val request = Request.Builder()
                .addHeader("cookie", getSession())
                .url(mUrl)
                .post(formBody)
                .build()

        val call = okHttpClient!!.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                mHandler.sendEmptyMessage(UNSUCC3)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()
                val result = body.string()
                LogUtil.e("run_findStockAndStockPos --> onResponse", result)
                if (!JsonUtil.isSuccess(result)) {
                    val msg = mHandler.obtainMessage(UNSUCC3, result)
                    mHandler.sendMessage(msg)
                    return
                }
                val msg = mHandler.obtainMessage(SUCC3, result)
                mHandler.sendMessage(msg)
            }
        })
    }

    /**
     * 创建PopupWindow 【 来源类型选择 】
     */
    private var popWindow: PopupWindow? = null
    private fun pop_sourceType(v: View) {
        if (null != popWindow) {//不为空就隐藏
            popWindow!!.dismiss()
            return
        }
        // 获取自定义布局文件popupwindow_left.xml的视图
        val popV = layoutInflater.inflate(R.layout.pur_in_stock_sourcetype_popwindow, null)
        // 创建PopupWindow实例,200,LayoutParams.MATCH_PARENT分别是宽度和高度
        popWindow = PopupWindow(popV, v.width, ViewGroup.LayoutParams.WRAP_CONTENT, true)
        // 设置动画效果
        // popWindow.setAnimationStyle(R.style.AnimationFade);
        popWindow!!.setBackgroundDrawable(BitmapDrawable())
        popWindow!!.isOutsideTouchable = true
        popWindow!!.isFocusable = true

        // 点击其他地方消失
        val click = View.OnClickListener { v ->
            when (v.id) {
                R.id.btn1 -> { // 无源单
                    tv_sourceTypeSel.text = "无源单"
                    parent!!.fragment1.icStockBill.fselTranType = 0
                    icStockBillEntry.fsourceTranType = 0
                    btn_sourceNoSel.visibility = View.GONE
                    lin_getMtl.visibility = View.VISIBLE
                }
                R.id.btn2 -> {// 采购订单
                    tv_sourceTypeSel.text = "采购订单"
                    parent!!.fragment1.icStockBill.fselTranType = 71
                    icStockBillEntry.fsourceTranType = 71
                    btn_sourceNoSel.visibility = View.VISIBLE
                    lin_getMtl.visibility = View.INVISIBLE
                }
                R.id.btn3 -> {// 收料通知单
                    tv_sourceTypeSel.text = "收料通知单"
                    parent!!.fragment1.icStockBill.fselTranType = 72
                    icStockBillEntry.fsourceTranType = 72
                    btn_sourceNoSel.visibility = View.VISIBLE
                    lin_getMtl.visibility = View.INVISIBLE
                }
            }
            popWindow!!.dismiss()
        }
        popV.findViewById<View>(R.id.btn1).setOnClickListener(click)
        popV.findViewById<View>(R.id.btn2).setOnClickListener(click)
        popV.findViewById<View>(R.id.btn3).setOnClickListener(click)
    }

    /**
     * 得到用户对象
     */
    private fun getUserInfo() {
        if (user == null) user = showUserByXml()
    }

    override fun onDestroyView() {
        closeHandler(mHandler)
        mBinder!!.unbind()
        EventBus.getDefault().unregister(this);
        super.onDestroyView()
    }
}