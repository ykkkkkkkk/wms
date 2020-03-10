package ykk.xc.com.wms.warehouse

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.OnClick
import kotlinx.android.synthetic.main.ware_sc_other_in_stock_fragment2.*
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
 * 描述：其他入库---添加明细
 * 作者：ykk
 */
class OtherInStock_Fragment2 : BaseFragment() {

    private val SEL_STOCK = 11
    private val SEL_STOCKPOS = 12
    private val SEL_MTL = 13
    private val SEL_UNIT = 14
    private val context = this
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


    private var okHttpClient: OkHttpClient? = null
    private var user: User? = null
    private var stock:Stock? = null
    private var stockPos:StockPosition? = null
    private var mContext: Activity? = null
    private val df = DecimalFormat("#.######")
    private var parent: OtherInStock_MainActivity? = null
    private var isTextChange: Boolean = false // 是否进入TextChange事件
    private var timesTamp:String? = null // 时间戳
    private var icStockBillEntry = ICStockBillEntry()
    var isAddEntry = false // 是否添加了分录信息

    // 消息处理
    private val mHandler = MyHandler(this)
    private class MyHandler(activity: OtherInStock_Fragment2) : Handler() {
        private val mActivity: WeakReference<OtherInStock_Fragment2>

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
                    m.SUCC1 -> { // 扫码成功 进入
                        val icItem = JsonUtil.strToObject(msgObj, ICItem::class.java)
                        m.getMtlAfter(icItem)
                    }
                    m.UNSUCC1 -> { // 扫码失败
                        errMsg = JsonUtil.strToString(msgObj)
                        if (m.isNULLS(errMsg).length == 0) errMsg = "很抱歉，没有找到数据！"
                        Comm.showWarnDialog(m.mContext, errMsg)
                    }
                    m.SUCC2 -> { // 查询库存 进入
                        val list = JsonUtil.strToList(msgObj, ICInventory::class.java)
                        m.tv_stockQty.text = m.df.format(list[0].getfQty())
                    }
                    m.UNSUCC2 -> { // 查询库存  失败
                        m.tv_stockQty.text = "0"
                    }
                    m.SUCC3 -> { // 查询仓库库位 进入
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
                    m.UNSUCC3 -> { // 查询仓库库位    失败
                        Comm.showWarnDialog(m.mContext,"请检查仓库是否设置为禁用！")
                    }
                    m.SAVE -> { // 保存成功 进入
                        m.isAddEntry = true
                        m.reset()
                        m.toasts("保存成功✔")
                    }
                    m.UNSAVE -> { // 保存失败
                        errMsg = JsonUtil.strToString(msgObj)
                        if (m.isNULLS(errMsg).length == 0) errMsg = "保存失败！"
                        Comm.showWarnDialog(m.mContext, errMsg)
                    }
                    m.SETFOCUS -> { // 当弹出其他窗口会抢夺焦点，需要跳转下，才能正常得到值
                        m.setFocusable(m.et_getFocus)
                        m.setFocusable(m.et_code)
                    }
                    m.SAOMA -> { // 扫码之后
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
            1 -> { // 接收第一个页面发来的指令
                reset()
            }
            3 -> { // 接收第三个页面发来的指令
                var icEntry = entity.obj as ICStockBillEntry
                btn_save.text = "保存"
                getICStockBillEntry(icEntry)
            }
        }
    }

    override fun setLayoutResID(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.ware_sc_other_in_stock_fragment2, container, false)
    }

    override fun initView() {
        mContext = getActivity()
        parent = mContext as OtherInStock_MainActivity
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
//            // 表头是否点击了重置，点击了就清空这个页面
//            if(parent!!.fragment1.isReset) {
//                parent!!.fragment1.isReset = false
//                reset()
//            }
        }
    }

    @OnClick(R.id.btn_scan, R.id.btn_mtlSel, R.id.tv_stockSel, R.id.tv_stockPosSel, R.id.tv_price, R.id.tv_num, R.id.tv_batchNo, R.id.tv_unitSel, R.id.tv_remark, R.id.btn_save, R.id.btn_clone)
    fun onViewClicked(view: View) {
        when (view.id) {
            R.id.tv_stockSel -> { // 选择仓库
                val bundle = Bundle()
                bundle.putString("accountType", "SC")
                bundle.putInt("unDisable", 1) // 只显示未禁用的数据
                showForResult(Stock_DialogActivity::class.java, SEL_STOCK, bundle)
            }
            R.id.tv_stockPosSel -> { // 选择库位
                val bundle = Bundle()
                bundle.putInt("fspGroupId", stock!!.fspGroupId)
                bundle.putString("accountType", "SC")
                showForResult(StockPos_DialogActivity::class.java, SEL_STOCKPOS, bundle)
            }
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
                run_save();
            }
            R.id.btn_clone -> { // 重置
                if (checkSaveHint()) {
                    val build = AlertDialog.Builder(mContext)
                    build.setIcon(R.drawable.caution)
                    build.setTitle("系统提示")
                    build.setMessage("您有未保存的数据，继续重置吗？")
                    build.setPositiveButton("是") { dialog, which -> reset() }
                    build.setNegativeButton("否", null)
                    build.setCancelable(false)
                    build.show()

                } else {
                    reset()
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
            Comm.showWarnDialog(mContext, "请选择仓库！")
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

    private fun reset() {
        btn_save.text = "添加"
        setEnables(tv_stockPosSel, R.drawable.back_style_gray3, false)
        tv_mtlName.text = ""
        tv_mtlNumber.text = ""
        tv_fmodel.text = ""
        tv_sumMoney.text = ""
        tv_stockQty.text = ""
        tv_stockSel.text = ""
        tv_stockPosSel.text = ""
        tv_price.text = ""
        tv_batchNo.text = ""
        tv_num.text = ""
        tv_unitSel.text = ""
        tv_remark.text = ""

        icStockBillEntry.id = 0
        icStockBillEntry.icstockBillId = parent!!.fragment1.icStockBill.id
        icStockBillEntry.fitemId = 0
        icStockBillEntry.fdcStockId = 0
        icStockBillEntry.fdcSPId = 0
        icStockBillEntry.fbatchNo = ""
        icStockBillEntry.fqty = 0.0
        icStockBillEntry.fprice = 0.0
        icStockBillEntry.funitId = 0
        icStockBillEntry.mtlName = ""
        icStockBillEntry.mtlNumber = ""
        icStockBillEntry.fmode = ""
        icStockBillEntry.inStockName = ""
        icStockBillEntry.inStockPosName = ""
        icStockBillEntry.unitName = ""
        icStockBillEntry.remark = ""
        stock = null
        stockPos = null
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
        tv_stockSel.text = icEntry.inStockName
        tv_stockPosSel.text = icEntry.inStockPosName
        tv_price.text = df.format(icEntry.fprice)
        tv_batchNo.text = icEntry.fbatchNo
        tv_num.text = df.format(icEntry.fqty)
        tv_unitSel.text = icEntry.unitName
        tv_remark.text = icEntry.remark

        if(icEntry.fdcSPId > 0) {
            setEnables(tv_stockPosSel, R.drawable.back_style_blue, true)
        } else {
            setEnables(tv_stockPosSel, R.drawable.back_style_gray3, false)
        }
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
                    tv_stockSel.text = stock!!.fname
                    // 如果启用了库位
                    if(stock!!.fisStockMgr == 1) {
                        setEnables(tv_stockPosSel, R.drawable.back_style_blue, true)
                        var bundle = Bundle()
                        bundle.putInt("fspGroupId", stock!!.fspGroupId);
                        showForResult(StockPos_DialogActivity::class.java, SEL_STOCKPOS, bundle)

                    } else {
                        setEnables(tv_stockPosSel, R.drawable.back_style_gray3, false)
                    }
                    stockPos = null
                    tv_stockPosSel.text = ""

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
                    tv_stockPosSel.text = stockPos!!.fname
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
    private fun run_save() {
        showLoadDialog("保存中...", false)
        val mUrl = getURL("stockBill_WMS/saveEntry")
        val mJson = JsonUtil.objectToString(icStockBillEntry)
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