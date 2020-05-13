package ykk.xc.com.wms.produce

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import butterknife.OnClick
import kotlinx.android.synthetic.main.prod_instock_transfer_fragment2.*
import okhttp3.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import ykk.xc.com.wms.R
import ykk.xc.com.wms.basics.MoreBatchInputDialog
import ykk.xc.com.wms.basics.Mtl_DialogActivity
import ykk.xc.com.wms.basics.Stock_GroupDialogActivity
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
import java.io.Serializable
import java.lang.ref.WeakReference
import java.text.DecimalFormat
import java.util.concurrent.TimeUnit

/**
 * 日期：2019-10-16 09:50
 * 描述：生产入库调拨---添加明细
 * 作者：ykk
 */
class Prod_InStock_Transfer_Fragment2 : BaseFragment() {

    companion object {
        private val SEL_POSITION = 61
        private val SEL_MTL = 62
        private val SEL_UNIT = 63
        private val SUCC1 = 200
        private val UNSUCC1 = 500
        private val SUCC2 = 201
        private val UNSUCC2 = 501
        private val SAVE = 202
        private val UNSAVE = 502

        private val SETFOCUS = 1
        private val SAOMA = 2
        private val RESULT_PRICE = 3
        private val RESULT_NUM = 4
        private val RESULT_BATCH = 5
        private val RESULT_REMAREK = 6
        private val WRITE_CODE = 7
        private val RESULT_PUR_ORDER = 8
        private val RESULT_WEIGHT = 9
        private val SM_RESULT_NUM = 10
    }
    private val context = this
    private var okHttpClient: OkHttpClient? = null
    private var user: User? = null
    private var stock:Stock? = null
    private var stockArea:StockArea? = null
    private var storageRack:StorageRack? = null
    private var stockPos:StockPosition? = null
    private var stock2:Stock? = null
    private var stockArea2:StockArea? = null
    private var storageRack2:StorageRack? = null
    private var stockPos2:StockPosition? = null
    private var mContext: Activity? = null
    private val df = DecimalFormat("#.######")
    private var parent: Prod_InStock_Transfer_MainActivity? = null
    private var isTextChange: Boolean = false // 是否进入TextChange事件
    private var timesTamp:String? = null // 时间戳
    var icStockBillEntry = ICStockBillEntry()
    private var smICStockBillEntry:ICStockBillEntry? = null // 扫码返回的对象
    private var autoICStockBillEntry:ICStockBillEntry? = null // 用于自动保存记录的对象
    private var smICStockBillEntry_Barcodes = ArrayList<ICStockBillEntry_Barcode>() // 扫码返回的对象
    private var smqFlag = '1' // 扫描类型1：位置扫描，2：物料扫描
    private var isWeightTextChanged = true // 是否改变称重数就执行改变事件
    private var isReferenceTextChanged = true // 是否改变参考数就执行改变事件

    // 消息处理
    private val mHandler = MyHandler(this)
    private class MyHandler(activity: Prod_InStock_Transfer_Fragment2) : Handler() {
        private val mActivity: WeakReference<Prod_InStock_Transfer_Fragment2>

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
                        when(m.smqFlag) {
                            '1'-> { // 仓库位置
                                m.resetStockGroup()
                                m.getStockGroup(msgObj)
                            }
                            '2'-> { // 物料
                                val result = JsonUtil.strToString(msgObj)
                                if(msgObj!!.indexOf("ykk_string") > -1 && result.equals("refresh")) { // 这是扫了箱码返回的成功状态
                                    m.toasts("保存成功✔")
                                    EventBus.getDefault().post(EventBusEntity(21)) // 发送指令到fragment3，告其刷新

                                } else {

                                    val icEntry = JsonUtil.strToObject(msgObj, ICStockBillEntry::class.java)
                                    if (m.getValues(m.tv_mtlName).length > 0 && m.smICStockBillEntry != null && m.smICStockBillEntry!!.id != icEntry.id) {
                                        if (!m.checkSave()) return
                                        m.icStockBillEntry.icstockBillId = m.parent!!.fragment1.icStockBill.id

                                        m.autoICStockBillEntry = icEntry // 加到自动保存对象
                                        m.run_save(null)
//                                    Comm.showWarnDialog(m.mContext,"请先保存当前数据！")
                                        return
                                    }
                                    m.getMaterial(icEntry)
                                }
                            }
                        }
                    }
                    UNSUCC1 -> { // 扫码失败
                        when(m.smqFlag) {
                            '1' -> { // 仓库位置扫描
                                m.tv_outPositionName.text = ""
                            }
                            '2' -> { // 物料扫描
                                m.tv_icItemName.text = ""
                            }
                        }
                        errMsg = JsonUtil.strToString(msgObj)
                        if (m.isNULLS(errMsg).length == 0) errMsg = "很抱歉，没有找到数据！"
                        Comm.showWarnDialog(m.mContext, errMsg)
                    }
                    SUCC2 -> { // 查询库存 进入
                        val list = JsonUtil.strToList(msgObj, ICInventory::class.java)
                        m.tv_stockQty.text = Html.fromHtml("即时库存：<font color='#6a5acd'>"+m.df.format(list[0].getfQty())+"</font>")
                    }
                    UNSUCC2 -> { // 查询库存  失败
                        m.tv_stockQty.text = "0"
                    }
                    SAVE -> { // 保存成功 进入
                        // 保存了分录，供应商就不能修改
//                        m.setEnables(m.parent!!.fragment1.tv_suppSel, R.drawable.back_style_gray2a,false)
                        m.parent!!.fragment1.icstockBillEntryList = null
                        EventBus.getDefault().post(EventBusEntity(21)) // 发送指令到fragment3，告其刷新
                        m.reset(1)
//                        m.toasts("保存成功✔")
                        // 如果有自动保存的对象，保存后就显示下一个
                        if(m.autoICStockBillEntry != null) {
                            m.toasts("自动保存成功✔")
                            m.getMaterial(m.autoICStockBillEntry!!)
                            m.autoICStockBillEntry = null

                        } else {
                            m.toasts("保存成功✔")
                        }
                    }
                    UNSAVE -> { // 保存失败
                        errMsg = JsonUtil.strToString(msgObj)
                        if (m.isNULLS(errMsg).length == 0) errMsg = "保存失败！"
                        Comm.showWarnDialog(m.mContext, errMsg)
                    }
                    SETFOCUS -> { // 当弹出其他窗口会抢夺焦点，需要跳转下，才能正常得到值
                        m.setFocusable(m.et_getFocus)
                        when(m.smqFlag) {
                            '1'-> m.setFocusable(m.et_positionCode)
                            '2'-> m.setFocusable(m.et_code)
                        }
                    }
                    SAOMA -> { // 扫码之后
                        when(m.smqFlag) {
                            '2' -> {
//                                if(m.getValues(m.tv_mtlName).length > 0) {
                                if(m.getValues(m.et_code)[0] == '9' && m.getValues(m.tv_mtlName).length > 0 ) { // 如果扫描的是箱码，就提示先保存
                                    Comm.showWarnDialog(m.mContext,"请先保存当前数据！")
                                    m.isTextChange = false
                                    return
                                }
                                if(m.getValues(m.et_code)[0] == '9' && m.stock == null ) { // 如果扫描的是箱码，未选择位置，就提示
                                    Comm.showWarnDialog(m.mContext,"请先扫描或选择位置！")
                                    m.isTextChange = false
                                    return
                                }
                            }
                        }
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
                smICStockBillEntry_Barcodes.clear()
                smICStockBillEntry_Barcodes.addAll(icEntry.icstockBillEntry_Barcodes)
                getICStockBillEntry(icEntry)
            }
        }
    }

    override fun setLayoutResID(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.prod_instock_transfer_fragment2, container, false)
    }

    override fun initView() {
        mContext = getActivity()
        parent = mContext as Prod_InStock_Transfer_MainActivity
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
        hideSoftInputMode(mContext, et_positionCode)
        hideSoftInputMode(mContext, et_code)

        icStockBillEntry.weightUnitType = 2
        parent!!.fragment1.icStockBill.fselTranType = 0
        icStockBillEntry.fsourceTranType = 0
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            if(parent!!.fragment1.icstockBillEntryList != null) {
                // 执行保存功能
                setICStockEntry(parent!!.fragment1.icstockBillEntryList)
            }
            mHandler.sendEmptyMessageDelayed(SETFOCUS, 200)
        }
    }

    @OnClick(R.id.btn_scan, R.id.btn_mtlSel, R.id.btn_positionScan, R.id.btn_positionSel, R.id.tv_num, R.id.tv_weight, R.id.tv_batchNo,
             R.id.tv_unitSel, R.id.tv_remark, R.id.btn_save, R.id.btn_clone, R.id.tv_weightUnitType,
             R.id.tv_connBlueTooth2, R.id.tv_outPositionName, R.id.tv_icItemName)
    fun onViewClicked(view: View) {
        when (view.id) {
            R.id.btn_positionSel -> { // 选择仓库
                smqFlag = '1'
                val bundle = Bundle()
                showForResult(context, Stock_GroupDialogActivity::class.java, SEL_POSITION, bundle)
            }
            R.id.btn_mtlSel -> { // 选择物料
                smqFlag = '2'
                val bundle = Bundle()
                showForResult(Mtl_DialogActivity::class.java, SEL_MTL, bundle)
            }
            R.id.btn_positionScan -> { // 调用摄像头扫描（位置）
                smqFlag = '1'
                showForResult(CaptureActivity::class.java, BaseFragment.CAMERA_SCAN, null)
            }
            R.id.btn_scan -> { // 调用摄像头扫描（物料）
                smqFlag = '2'
                showForResult(CaptureActivity::class.java, BaseFragment.CAMERA_SCAN, null)
            }
            R.id.tv_outPositionName -> { // 调出位置点击
                smqFlag = '1'
                mHandler.sendEmptyMessageDelayed(SETFOCUS, 200)
            }
            R.id.tv_icItemName -> { // 物料点击
                smqFlag = '2'
                mHandler.sendEmptyMessageDelayed(SETFOCUS, 200)
            }
            R.id.tv_price -> { // 单价
//                showInputDialog("单价", icStockBillEntry.fprice.toString(), "0.0", RESULT_PRICE)
            }
            R.id.tv_num -> { // 数量
                showInputDialog("数量", icStockBillEntry.fqty.toString(), "0.0", RESULT_NUM)
            }
            R.id.tv_weight -> { // 称重数量
                showInputDialog("称重数量", icStockBillEntry.weight.toString(), "0.0", RESULT_WEIGHT)
            }
            R.id.tv_batchNo -> { // 批次号
                val bundle = Bundle()
                bundle.putInt("icstockBillEntryId", icStockBillEntry.id)
                bundle.putSerializable("icstockBillEntry_Barcodes", icStockBillEntry.icstockBillEntry_Barcodes as Serializable)
                bundle.putString("userName", user!!.username)
                bundle.putString("barcode", getValues(et_code))
                bundle.putInt("againUse", 1)
                showForResult(MoreBatchInputDialog::class.java, RESULT_BATCH, bundle)
            }
            R.id.tv_weightUnitType -> { // 称重单位选择
                pop_unitType(view)
                popWindow2!!.showAsDropDown(view)
            }
            R.id.tv_connBlueTooth2 -> { // 蓝牙连接
                parent!!.openBluetooth()
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
        if(icStockBillEntry.id == 0) {
            Comm.showWarnDialog(mContext, "请扫码物料条码，或点击表体列表！")
            return false
        }
        if (icStockBillEntry.fdcStockId == 0 || stock == null) {
            Comm.showWarnDialog(mContext, "请选择调入位置！")
            return false
        }
//        if (icStockBillEntry.fprice == 0.0) {
//            Comm.showWarnDialog(mContext, "请输入单价！")
//            return false;
//        }
//        if(icStockBillEntry.icItem.batchManager.equals("Y") && icStockBillEntry.icstockBillEntry_Barcodes.size == 0) {
//            Comm.showWarnDialog(mContext, "请输入批次！")
//            return false
//        }
        if (icStockBillEntry.fqty == 0.0) {
            Comm.showWarnDialog(mContext, "请输入数量！")
            return false
        }
        if (icStockBillEntry.fqty > icStockBillEntry.fsourceQty) {
            Comm.showWarnDialog(mContext, "调拨数量不能大于可调拨数！")
            return false
        }
        if (icStockBillEntry.weight == 0.0 && (icStockBillEntry.icItem.calByWeight.equals("M") || icStockBillEntry.icItem.calByWeight.equals("Y")) && icStockBillEntry.boxBarCodeId == 0 && !icStockBillEntry.icItem.snManager.equals("Y")) {
            Comm.showWarnDialog(mContext, "请输入称重数量或连接蓝牙自动称重！")
            return false
        }
//        if(icStockBillEntry.icItem.isQualityPeriodManager.equals("Y") && icStockBillEntry.fkfPeriod == 0) {
//            Comm.showWarnDialog(mContext, "请输入保质期！")
//            return false
//        }
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
                R.id.et_positionCode -> setFocusable(et_positionCode)
                R.id.et_code -> setFocusable(et_code)
//                R.id.et_containerCode -> setFocusable(et_containerCode)
            }
        }
        et_positionCode!!.setOnClickListener(click)
        et_code!!.setOnClickListener(click)
//        et_containerCode!!.setOnClickListener(click)

        // 仓库---数据变化
        et_positionCode!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (s.length == 0) return
                if (!isTextChange) {
                    isTextChange = true
                    smqFlag = '1'
                    mHandler.sendEmptyMessageDelayed(SAOMA, 300)
                }
            }
        })
        // 仓库---长按输入条码
        et_positionCode!!.setOnLongClickListener {
            smqFlag = '1'
            showInputDialog("输入条码", "", "none", WRITE_CODE)
            true
        }
        // 仓库---焦点改变
        et_positionCode.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if(hasFocus) {
                lin_focusPosition.setBackgroundResource(R.drawable.back_style_red_focus)
            } else {
                if (lin_focusPosition != null) {
                    lin_focusPosition!!.setBackgroundResource(R.drawable.back_style_gray4)
                }
            }
        }

//        // 容器---数据变化
//        et_containerCode!!.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
//            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
//            override fun afterTextChanged(s: Editable) {
//                if (s.length == 0) return
//                if (!isTextChange) {
//                    isTextChange = true
//                    smqFlag = '2'
//                    mHandler.sendEmptyMessageDelayed(ICInvBackup_Fragment2.SAOMA, 300)
//                }
//            }
//        })
//        // 容器---长按输入条码
//        et_containerCode!!.setOnLongClickListener {
//            smqFlag = '2'
//            showInputDialog("输入条码号", "", "none", ICInvBackup_Fragment2.WRITE_CODE)
//            true
//        }
//        // 容器---焦点改变
//        et_containerCode.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
//            if(hasFocus) {
//                lin_focusContainer.setBackgroundResource(R.drawable.back_style_red_focus)
//
//            } else {
//                if (lin_focusContainer != null) {
//                    lin_focusContainer!!.setBackgroundResource(R.drawable.back_style_gray4)
//                }
//            }
//        }

        // 物料---数据变化
        et_code!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (s.length == 0) return
                if (!isTextChange) {
                    isTextChange = true
                    smqFlag = '2'
                    mHandler.sendEmptyMessageDelayed(SAOMA, 300)
                }
            }
        })
        // 物料---长按输入条码
        et_code!!.setOnLongClickListener {
            smqFlag = '2'
            showInputDialog("输入条码号", getValues(et_code), "none", WRITE_CODE)
            true
        }
        // 物料---焦点改变
        et_code.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if(hasFocus) {
                lin_focusMtl.setBackgroundResource(R.drawable.back_style_red_focus)
            } else {
                if (lin_focusMtl != null) {
                    lin_focusMtl!!.setBackgroundResource(R.drawable.back_style_gray4)
                }
            }
        }

        // 称重数量发生改变
        tv_weight.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if(isWeightTextChanged && parseDouble(s.toString()) > 0 ) {
                    mHandler.postDelayed(Runnable {
                        val weight = parseDouble(s.toString())
                        val icItem = icStockBillEntry.icItem
                        val unitWeight = if (icItem.unitWeight <= 0) 1.0 else icItem.unitWeight
                        var fqtydecimal = icItem.fqtydecimal
                        if(fqtydecimal == null) {
                            fqtydecimal = 0
                        } else if(fqtydecimal >= 6) {
                            fqtydecimal = 6
                        }
                        val referenceNum = BigdecimalUtil.div(weight, unitWeight, fqtydecimal.toInt())
                        tv_referenceNum.text = df.format(referenceNum)
                        icStockBillEntry.weight = weight
                        icStockBillEntry.referenceNum = referenceNum
                    },150)
                }
            }
        })

        // 参考数量发生改变
        tv_referenceNum.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if(isReferenceTextChanged && parseDouble(s.toString()) > 0 ) {
                    mHandler.postDelayed(Runnable {
                        val roundQty = BigdecimalUtil.round(icStockBillEntry.referenceNum, 0)
                        icStockBillEntry.fqty = roundQty
                        tv_num.text = df.format(roundQty)
                    },150)
                }
            }
        })
    }

    /**
     * 0：表示点击重置，1：表示保存后重置
     */
    private fun reset(flag : Int) {
        if(parent!!.fragment1.icStockBill.fselTranType == 0 && flag == 0 ) {
            lin_getMtl.visibility = View.VISIBLE
            tv_inPositionName.text = ""
            tv_outPositionName.text = ""
            tv_stockName.text = "仓库："
            tv_stockAreaName.text = "库区："
            tv_storageRackName.text = "货架："
            tv_stockPosName.text = "库位："
            icStockBillEntry.fsourceTranType = 0
            icStockBillEntry.fdcStockId = 0
            icStockBillEntry.fdcSPId = 0
            icStockBillEntry.inStockName = ""
            icStockBillEntry.inStockPosName = ""
            stock = null
            stockArea = null
            storageRack = null
            stockPos = null
        }
        lin_focusMtl.setBackgroundResource(R.drawable.back_style_gray4)
        btn_scan.isEnabled = true
        et_code.isEnabled = true
        tv_icItemName.isEnabled = true
        setEnables(tv_batchNo, R.drawable.back_style_blue, true)
        setEnables(tv_num, R.drawable.back_style_blue, true)
        setEnables(tv_weight, R.drawable.back_style_blue, true)
        btn_save.text = "添加"
        tv_mtlName.text = ""
        tv_mtlNumber.text = "物料代码："
        tv_fmodel.text = "规格型号："
        tv_stockQty.text = "即时库存："
        tv_batchNo.text = ""
        tv_num.text = ""
        tv_sourceQty.text = ""
        tv_referenceNum.text = ""
        tv_weight.text = ""
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
        icStockBillEntry.referenceNum = 0.0
        icStockBillEntry.weight = 0.0
        icStockBillEntry.funitId = 0
        icStockBillEntry.mtlName = ""
        icStockBillEntry.mtlNumber = ""
        icStockBillEntry.fmode = ""
//        icStockBillEntry.inStockName = ""
//        icStockBillEntry.inStockPosName = ""
        icStockBillEntry.unitName = ""
        icStockBillEntry.remark = ""

        icStockBillEntry.icItem = null
        icStockBillEntry.icstockBillEntry_Barcodes.clear()
        smICStockBillEntry = null
        smICStockBillEntry_Barcodes.clear()
//        stock = null
//        stockPos = null
        timesTamp = user!!.getId().toString() + "-" + Comm.randomUUID()
        parent!!.isChange = false
        mHandler.sendEmptyMessageDelayed(SETFOCUS, 200)
    }

    /**
     *  扫码之后    物料启用批次
     */
    fun setBatchCode(fqty : Double) {
        val entryBarcode = ICStockBillEntry_Barcode()
        entryBarcode.parentId = smICStockBillEntry!!.id
        entryBarcode.barcode = getValues(et_code)
        entryBarcode.batchCode = smICStockBillEntry!!.smBatchCode
        entryBarcode.snCode = ""
        entryBarcode.fqty = fqty
        entryBarcode.isUniqueness = 'Y'
        entryBarcode.againUse = 0
        entryBarcode.createUserName = user!!.username
        entryBarcode.billType = parent!!.fragment1.icStockBill.billType

        smICStockBillEntry_Barcodes.add(entryBarcode)
        getICStockBillEntry(smICStockBillEntry!!)
    }

    /**
     *  扫码之后    物料启用序列号
     */
    fun setSnCode() {
        val entryBarcode = ICStockBillEntry_Barcode()
        entryBarcode.parentId = smICStockBillEntry!!.id
        entryBarcode.barcode = getValues(et_code)
        entryBarcode.batchCode = ""
        entryBarcode.snCode = smICStockBillEntry!!.smSnCode
        entryBarcode.fqty = 1.0
        entryBarcode.isUniqueness = 'Y'
        entryBarcode.againUse = 0
        entryBarcode.createUserName = user!!.username
        entryBarcode.billType = parent!!.fragment1.icStockBill.billType

        smICStockBillEntry_Barcodes.add(entryBarcode)
        getICStockBillEntry(smICStockBillEntry!!)
    }

    /**
     *  扫码之后    物料未启用
     */
    fun unSetBatchOrSnCode(fqty : Double) {
        val entryBarcode = ICStockBillEntry_Barcode()
        entryBarcode.parentId = smICStockBillEntry!!.id
        entryBarcode.barcode = getValues(et_code)
        entryBarcode.batchCode = ""
        entryBarcode.snCode = ""
        entryBarcode.fqty = fqty
        entryBarcode.isUniqueness = 'N'
        entryBarcode.againUse = 0
        entryBarcode.createUserName = user!!.username
        entryBarcode.billType = parent!!.fragment1.icStockBill.billType

        smICStockBillEntry_Barcodes.add(entryBarcode)
        getICStockBillEntry(smICStockBillEntry!!)
    }

    fun getMaterial(icEntry: ICStockBillEntry) {
        smICStockBillEntry = icEntry

        btn_save.text = "保存"
        // 判断条码是否存在（启用批次，序列号）
        if (icStockBillEntry.icstockBillEntry_Barcodes.size > 0 && (icEntry.icItem.batchManager.equals("Y") || icEntry.icItem.snManager.equals("Y"))) {
            icStockBillEntry.icstockBillEntry_Barcodes.forEach {
                if (getValues(et_code) == it.barcode) {
                    Comm.showWarnDialog(mContext,"条码已使用！")
                    return
                }
            }
        }
        if(icEntry.icItem.batchManager.equals("Y")) { // 启用批次号
            val showInfo:String = "<font color='#666666'>批次号：</font>" + icEntry.smBatchCode+"<font color='#666666'>可用库存：</font>"+"<font color='#FF2200'>"+df.format(icEntry.inventoryNowQty)+"</font>"
            showInputDialog("数量", showInfo, "", "0.0", SM_RESULT_NUM)

        } else if(icEntry.icItem.snManager.equals("Y")) { // 启用序列号
            setSnCode()

        } else { // 未启用
            unSetBatchOrSnCode(1.0)
        }
        if(icEntry.icstockBillEntry_Barcodes.size > 0) {
            if (smICStockBillEntry_Barcodes.size > 0) {
                var isBool = true
                icEntry.icstockBillEntry_Barcodes.forEach {
                    isBool = false
                    for (it2 in smICStockBillEntry_Barcodes) {
                        if(it.barcode == it2.barcode) {
                            isBool = false
                            break
                        }
                    }
                    if(isBool) {
                        smICStockBillEntry_Barcodes.add(it)
                    }
                }
            } else {
                smICStockBillEntry_Barcodes.addAll(icEntry.icstockBillEntry_Barcodes)
            }
        } else {
            smICStockBillEntry_Barcodes.addAll(icEntry.icstockBillEntry_Barcodes)
        }
    }

    fun getICStockBillEntry(icEntry: ICStockBillEntry) {
        isWeightTextChanged = false
        isReferenceTextChanged = false

        icStockBillEntry.id = icEntry.id
        icStockBillEntry.icstockBillId = icEntry.icstockBillId
        icStockBillEntry.finterId = icEntry.finterId
        icStockBillEntry.fitemId = icEntry.fitemId
        icStockBillEntry.fentryId = icEntry.fentryId
        icStockBillEntry.fdcStockId = icEntry.fdcStockId
        icStockBillEntry.fdcSPId = icEntry.fdcSPId
        icStockBillEntry.stockId_wms = icEntry.stockId_wms
        icStockBillEntry.stockAreaId_wms = icEntry.stockAreaId_wms
        icStockBillEntry.storageRackId_wms = icEntry.storageRackId_wms
        icStockBillEntry.stockPosId_wms = icEntry.stockPosId_wms
        icStockBillEntry.fscStockId = icEntry.fscStockId
        icStockBillEntry.fscSPId = icEntry.fscSPId
        icStockBillEntry.stockId2_wms = icEntry.stockId2_wms
        icStockBillEntry.stockAreaId2_wms = icEntry.stockAreaId2_wms
        icStockBillEntry.storageRackId2_wms = icEntry.storageRackId2_wms
        icStockBillEntry.stockPosId2_wms = icEntry.stockPosId2_wms
//        icStockBillEntry.fbatchNo = icEntry.fbatchNo
//        icStockBillEntry.fqty = icEntry.fqty
        icStockBillEntry.fsourceQty = icEntry.fsourceQty
        icStockBillEntry.qcPassQty = icEntry.qcPassQty
        icStockBillEntry.fprice = icEntry.fprice
        icStockBillEntry.weight = icEntry.weight
        icStockBillEntry.referenceNum = icEntry.referenceNum
        icStockBillEntry.funitId = icEntry.funitId
        icStockBillEntry.mtlNumber = icEntry.mtlNumber
        icStockBillEntry.mtlName = icEntry.mtlName
        icStockBillEntry.fmode = icEntry.fmode
        icStockBillEntry.inStockName = icEntry.inStockName
        icStockBillEntry.inStockPosName = icEntry.inStockPosName
        icStockBillEntry.outStockName = icEntry.outStockName
        icStockBillEntry.outStockPosName = icEntry.outStockPosName
        icStockBillEntry.unitName = icEntry.unitName
        icStockBillEntry.fkfDate = icEntry.fkfDate
        icStockBillEntry.fkfPeriod = icEntry.fkfPeriod
        icStockBillEntry.remark = icEntry.remark
        icStockBillEntry.inventoryNowQty = icEntry.inventoryNowQty
        icStockBillEntry.boxBarCodeId = icEntry.boxBarCodeId

        icStockBillEntry.icItem = icEntry.icItem

        tv_mtlName.text = icEntry.mtlName
        tv_mtlNumber.text = Html.fromHtml("物料代码：<font color='#6a5acd'>"+icEntry.mtlNumber+"</font>")
        tv_fmodel.text = Html.fromHtml("规格型号：<font color='#6a5acd'>"+icEntry.fmode+"</font>")
//        tv_price.text = df.format(icEntry.fprice)
//        tv_batchNo.text = icEntry.fbatchNo
//        if(icEntry.icItem.batchManager.equals("Y")) {
//            setEnables(tv_batchNo, R.drawable.back_style_blue, true)
//        } else {
            setEnables(tv_batchNo, R.drawable.back_style_gray3, false)
//        }
        if(icEntry.icItem.batchManager.equals("Y") || icEntry.icItem.snManager.equals("Y") || icEntry.boxBarCodeId > 0) {
            setEnables(tv_num, R.drawable.back_style_gray3, false)
        } else {
            setEnables(tv_num, R.drawable.back_style_blue, true)
        }
        // 如果是装箱的物料，把扫描物料框禁止
        if(icEntry.boxBarCodeId > 0) {
            lin_focusMtl.setBackgroundResource(R.drawable.back_style_gray3)
            btn_scan.isEnabled = false
            et_code.isEnabled = false
            tv_icItemName.isEnabled = false
        } else {
            lin_focusMtl.setBackgroundResource(R.drawable.back_style_gray4)
            btn_scan.isEnabled = true
            et_code.isEnabled = true
            tv_icItemName.isEnabled = true
        }
//        tv_num.text = if(icEntry.fqty > 0) df.format(icEntry.fqty) else ""
        tv_sourceQty.text = if(icEntry.fsourceQty > 0) df.format(icEntry.fsourceQty) else ""
        tv_referenceNum.text = if(icEntry.referenceNum > 0) df.format(icEntry.referenceNum) else ""
        tv_weight.text = if(icEntry.weight > 0) df.format(icEntry.weight) else ""
        tv_unitSel.text = icEntry.unitName
        tv_remark.text = icEntry.remark

        // 如果物料启用了称重
        if(icEntry.icItem.calByWeight.equals("M") || icEntry.icItem.calByWeight.equals("N") || icEntry.boxBarCodeId > 0 || icEntry.icItem.snManager.equals("Y")) {
            setEnables(tv_weight, R.drawable.back_style_gray3, false)
        } else {
            setEnables(tv_weight, R.drawable.back_style_blue, true)
        }

//        val mul = BigdecimalUtil.mul(icEntry.fprice, icEntry.fqty)
//        tv_sumMoney.text = df.format(mul)
        // 查询即时库存
        run_findInventoryQty()
        // 显示调入仓库
        if(icEntry.stockId_wms > 0) {
            stock = icEntry.stock
            stockArea = icEntry.stockArea
            storageRack = icEntry.storageRack
            stockPos = icEntry.stockPos
        }
        getStockGroup(null)
        // 显示调出仓库
        if(icEntry.stockId2_wms > 0) {
            stock2 = icEntry.stock2
            stockArea2 = icEntry.stockArea2
            storageRack2 = icEntry.storageRack2
            stockPos2 = icEntry.stockPos2
        }
        getStockGroup2()
        // 物料未启用
        if(icEntry.icstockBillEntry_Barcodes.size > 0 && icEntry.icItem.batchManager.equals("N") && icEntry.icItem.snManager.equals("N")) {
            showBatch_Qty(null, icEntry.fqty)
        } else {
            // 显示多批次
//        showBatch_Qty(icEntry.icstockBillEntry_Barcodes, icEntry.fqty)
            showBatch_Qty(smICStockBillEntry_Barcodes, icEntry.fqty)
        }
        mHandler.postDelayed(Runnable {
            isWeightTextChanged = true
            isReferenceTextChanged = true
        },300)
    }

    /**
     * 创建PopupWindow 【 来源类型选择 】
     */
    private var popWindow2: PopupWindow? = null
    private fun pop_unitType(v: View) {
        if (null != popWindow2) {//不为空就隐藏
            popWindow2!!.dismiss()
            return
        }
        // 获取自定义布局文件popupwindow_left.xml的视图
        val popV = layoutInflater.inflate(R.layout.weight_unitname_popwindow, null)
        // 创建PopupWindow实例,200,LayoutParams.MATCH_PARENT分别是宽度和高度
        popWindow2 = PopupWindow(popV, v.width, ViewGroup.LayoutParams.WRAP_CONTENT, true)
        // 设置动画效果
        // popWindow.setAnimationStyle(R.style.AnimationFade);
        popWindow2!!.setBackgroundDrawable(BitmapDrawable())
        popWindow2!!.isOutsideTouchable = true
        popWindow2!!.isFocusable = true

        // 点击其他地方消失
        val click = View.OnClickListener { v ->
            when (v.id) {
                R.id.tv1 -> { // 千克（kg）
                    tv_weightUnitType.text = "千克（kg）"
                    icStockBillEntry.weightUnitType = 1
                }
                R.id.tv2 -> { // 克（g）
                    tv_weightUnitType.text = "克（g）"
                    icStockBillEntry.weightUnitType = 2
                }
                R.id.tv3 -> { // 磅（lb）
                    tv_weightUnitType.text = "磅（lb）"
                    icStockBillEntry.weightUnitType = 3
                }
            }
            popWindow2!!.dismiss()
        }
        popV.findViewById<View>(R.id.tv1).setOnClickListener(click)
        popV.findViewById<View>(R.id.tv2).setOnClickListener(click)
        popV.findViewById<View>(R.id.tv3).setOnClickListener(click)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            SEL_POSITION -> {// 仓库	返回
                if (resultCode == Activity.RESULT_OK) {
                    resetStockGroup()
                    stock = data!!.getSerializableExtra("stock") as Stock
                    if(data!!.getSerializableExtra("stockArea") != null) {
                        stockArea = data!!.getSerializableExtra("stockArea") as StockArea
                    }
                    if(data!!.getSerializableExtra("storageRack") != null) {
                        storageRack = data!!.getSerializableExtra("storageRack") as StorageRack
                    }
                    if(data!!.getSerializableExtra("stockPos") != null) {
                        stockPos = data!!.getSerializableExtra("stockPos") as StockPosition
                    }
                    getStockGroup(null)
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
//                        tv_price.text = df.format(price)
//                        icStockBillEntry.fprice = price
//                        if(icStockBillEntry.fqty > 0) {
//                            val mul = BigdecimalUtil.mul(price, icStockBillEntry.fqty)
//                            tv_sumMoney.text = df.format(mul)
//                        }
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
//                        if(icStockBillEntry.fprice > 0) {
//                            val mul = BigdecimalUtil.mul(num, icStockBillEntry.fprice)
//                            tv_sumMoney.text = df.format(mul)
//                        }
                    }
                }
            }
            SM_RESULT_NUM -> { // 扫码数量	    返回
                if (resultCode == Activity.RESULT_OK) {
                    val bundle = data!!.getExtras()
                    if (bundle != null) {
                        val value = bundle.getString("resultValue", "")
                        val num = parseDouble(value)
                        if(num > icStockBillEntry.inventoryNowQty) {
                            Comm.showWarnDialog(mContext,"当前输入的数量不能大于可用库存数量！")
                            return
                        }
                        setBatchCode(num)
                    }
                }
            }
            RESULT_WEIGHT -> { // 称重    	返回
                if (resultCode == Activity.RESULT_OK) {
                    val bundle = data!!.getExtras()
                    if (bundle != null) {
                        val value = bundle.getString("resultValue", "")
                        val num = parseDouble(value)
                        tv_weight.text = df.format(num)
                        icStockBillEntry.weight = num
                    }
                }
            }
            RESULT_BATCH -> { // 批次号	返回
                if (resultCode == Activity.RESULT_OK) {
                    val bundle = data!!.getExtras()
                    if (bundle != null) {
                        val list = bundle.getSerializable("icstockBillEntry_Barcodes") as List<ICStockBillEntry_Barcode>
                        smICStockBillEntry_Barcodes.clear()
                        smICStockBillEntry_Barcodes.addAll(list)
                        showBatch_Qty(smICStockBillEntry_Barcodes, 0.0)
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
//                    if(icStockBillEntry.fsourceTranType == 71) {
//                        val list = data!!.getSerializableExtra("obj") as List<POOrderEntry>
//                        setICStockEntry_POOrder(list)
//                    } else if(icStockBillEntry.fsourceTranType == 72){
//                        val list = data!!.getSerializableExtra("obj") as List<POInStockEntry>
//                        setICStockEntry_PPBomTransfer(list)
//                    }
                }
            }
            BaseFragment.CAMERA_SCAN -> {// 扫一扫成功  返回
                if (resultCode == Activity.RESULT_OK) {
                    val bundle = data!!.extras
                    if (bundle != null) {
                        val code = bundle.getString(BaseFragment.DECODED_CONTENT_KEY, "")
                        when(smqFlag) {
                            '1' -> setTexts(et_positionCode, code)
                            '2' -> setTexts(et_code, code)
                        }
                    }
                }
            }
            WRITE_CODE -> {// 输入条码  返回
                if (resultCode == Activity.RESULT_OK) {
                    val bundle = data!!.extras
                    if (bundle != null) {
                        val value = bundle.getString("resultValue", "")
                        when(smqFlag) {
                            '1' -> setTexts(et_positionCode, value.toUpperCase())
                            '2' -> setTexts(et_code, value.toUpperCase())
                        }
                    }
                }
            }
        }
        mHandler.sendEmptyMessageDelayed(SETFOCUS, 200)
    }

    /**
     *  显示批次号和数量
     */
    fun showBatch_Qty(list : List<ICStockBillEntry_Barcode>?, fqty : Double) {
        if(list != null && list.size > 0) {
            val strBatch = StringBuffer()
            var sumQty = 0.0
            val listBatch = ArrayList<String>()

            list.forEach{
                if(Comm.isNULLS(it.batchCode).length > 0 && !listBatch.contains(it.batchCode)) {
                    listBatch.add(it.batchCode)
                }
                sumQty += it.fqty
            }
            listBatch.forEach {
                strBatch.append(it + "，")
            }
            // 删除最后一个，
            if (strBatch.length > 0) {
                strBatch.delete(strBatch.length - 1, strBatch.length)
            }
            tv_batchNo.text = strBatch.toString()
            tv_num.text = df.format(sumQty)

            icStockBillEntry.fqty = sumQty
            icStockBillEntry.icstockBillEntry_Barcodes.clear()
            icStockBillEntry.icstockBillEntry_Barcodes.addAll(list)
        } else {
            icStockBillEntry.fbatchNo = ""
            icStockBillEntry.fqty = fqty
            tv_batchNo.text = ""
            tv_num.text = if(fqty > 0) df.format(fqty) else ""
        }
    }

    fun resetStockGroup() {
        stock2 = null
        stockArea2 = null
        storageRack2 = null
        stockPos2 = null
    }

    /**
     * 得到调入仓库组
     */
    fun getStockGroup(msgObj : String?) {
        tv_stockName.text = "仓库："
        tv_stockAreaName.text = "库区："
        tv_storageRackName.text = "货架："
        tv_stockPosName.text = "库位："
        tv_stockAreaName.visibility = View.INVISIBLE
        tv_storageRackName.visibility = View.INVISIBLE
        tv_stockPosName.visibility = View.INVISIBLE
        // 重置数据
        icStockBillEntry.fdcStockId = 0
        icStockBillEntry.stockId_wms = 0
        icStockBillEntry.inStockName = ""
        icStockBillEntry.stockAreaId_wms = 0
        icStockBillEntry.storageRackId_wms = 0
        icStockBillEntry.fdcSPId = 0
        icStockBillEntry.stockPosId_wms = 0
        icStockBillEntry.inStockPosName = ""

        if(msgObj != null) {
            stock = null
            stockArea = null
            storageRack = null
            stockPos = null

            var caseId:Int = 0
            if(msgObj.indexOf("Stock_CaseId=1") > -1) {
                caseId = 1
            } else if(msgObj.indexOf("StockArea_CaseId=2") > -1) {
                caseId = 2
            } else if(msgObj.indexOf("StorageRack_CaseId=3") > -1) {
                caseId = 3
            } else if(msgObj.indexOf("StockPosition_CaseId=4") > -1) {
                caseId = 4
            }

            when(caseId) {
                1 -> {
                    stock = JsonUtil.strToObject(msgObj, Stock::class.java)
                    tv_inPositionName.text = stock!!.stockName
                }
                2 -> {
                    stockArea = JsonUtil.strToObject(msgObj, StockArea::class.java)
                    tv_inPositionName.text = stockArea!!.fname
                    if(stockArea!!.stock != null) stock = stockArea!!.stock
                }
                3 -> {
                    storageRack = JsonUtil.strToObject(msgObj, StorageRack::class.java)
                    tv_inPositionName.text = storageRack!!.fnumber
                    if(storageRack!!.stock != null) stock = storageRack!!.stock
                    if(storageRack!!.stockArea != null)  stockArea = storageRack!!.stockArea
                }
                4 -> {
                    stockPos = JsonUtil.strToObject(msgObj, StockPosition::class.java)
                    tv_inPositionName.text = stockPos!!.stockPositionName
                    if(stockPos!!.stock != null) stock = stockPos!!.stock
                    if(stockPos!!.stockArea != null)  stockArea = stockPos!!.stockArea
                    if(stockPos!!.storageRack != null)  storageRack = stockPos!!.storageRack
                }
            }
        }

        if(stock != null ) {
            tv_inPositionName.text = stock!!.stockName
            tv_stockName.text = Html.fromHtml("仓库：<font color='#6a5acd'>"+stock!!.stockName+"</font>")
            icStockBillEntry.fdcStockId = stock!!.fitemId
            icStockBillEntry.stockId_wms = stock!!.id
            icStockBillEntry.inStockName = stock!!.stockName
        }
        if(stockArea != null ) {
            tv_inPositionName.text = stockArea!!.fname
            tv_stockAreaName.visibility = View.VISIBLE
            tv_stockAreaName.text = Html.fromHtml("库区：<font color='#6a5acd'>"+stockArea!!.fname+"</font>")
            icStockBillEntry.stockAreaId_wms = stockArea!!.id
        }
        if(storageRack != null ) {
            tv_inPositionName.text = storageRack!!.fnumber
            tv_storageRackName.visibility = View.VISIBLE
            tv_storageRackName.text = Html.fromHtml("货架：<font color='#6a5acd'>"+storageRack!!.fnumber+"</font>")
            icStockBillEntry.storageRackId_wms = storageRack!!.id
        }
        if(stockPos != null ) {
            tv_inPositionName.text = stockPos!!.stockPositionName
            tv_stockPosName.visibility = View.VISIBLE
            tv_stockPosName.text = Html.fromHtml("库位：<font color='#6a5acd'>"+stockPos!!.stockPositionName+"</font>")
            icStockBillEntry.fdcSPId = stockPos!!.fitemId
            icStockBillEntry.stockPosId_wms = stockPos!!.id
            icStockBillEntry.inStockPosName = stockPos!!.stockPositionName
        }

        if(stock != null && icStockBillEntry.boxBarCodeId == 0) {
            // 自动跳到物料焦点
            smqFlag = '2'
            mHandler.sendEmptyMessage(SETFOCUS)
        }
    }

    /**
     * 得到调出仓库组
     */
    fun getStockGroup2() {
        if(stock2 != null ) {
            tv_outPositionName.text = stock2!!.stockName
        }
        if(stockArea2 != null ) {
            tv_outPositionName.text = stockArea2!!.fname
        }
        if(storageRack2 != null ) {
            tv_outPositionName.text = storageRack2!!.fnumber
        }
        if(stockPos2 != null ) {
            tv_outPositionName.text = stockPos2!!.stockPositionName
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

    private fun setICStockEntry(list : List<ICStockBillEntry>?) {
        parent!!.fragment1.icStockBill.fselTranType = 0
        var listEntry = ArrayList<ICStockBillEntry>()
        list!!.forEach {
            val entry = ICStockBillEntry()
            entry.icstockBillId = parent!!.fragment1.icStockBill.id
            entry.fitemId = it.fitemId
//            entry.fentryId = it.fentryid
            entry.fdcStockId = 0
            entry.fdcSPId = 0
            entry.stockId_wms = 0
            entry.stockAreaId_wms = 0
            entry.storageRackId_wms = 0
            entry.stockPosId_wms = 0

            entry.fscStockId = it.fdcStockId
            entry.fscSPId = 0
            entry.stockId2_wms = it.stockId_wms
            entry.stockAreaId2_wms = it.stockAreaId_wms
            entry.storageRackId2_wms = it.storageRackId_wms
            entry.stockPosId2_wms = it.stockPosId_wms
            // 是箱子里的物料，就给值
//            if(it.boxBarCodeId > 0) {
//                entry.fqty = it.fqty
//            }
//            entry.fqty = it.useableQty
//            entry.fprice = it.fprice
            entry.funitId = it.funitId
            entry.fsourceInterId = it.icstockBillId
            entry.fsourceEntryId = it.id
            entry.fsourceQty = it.fqty
            entry.fsourceTranType = 0
            entry.fsourceBillNo = it.icstockBill.pdaNo
            entry.fdetailId = it.id

            entry.mtlNumber = it.mtlNumber
            entry.mtlName = it.mtlName
            entry.fmode = it.fmode
            entry.inStockName = ""
            entry.inStockPosName = ""
            entry.outStockName = it.inStockName
            entry.outStockPosName = ""
            entry.unitName = it.unitName
            entry.fkfDate = null
            entry.remark = ""
            entry.boxBarCodeId = it.boxBarCodeId
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
        var mUrl:String? = null
        var barcode:String? = null
        var icstockBillId = ""
        var moreStock = "" // 多仓库查询
        var billType:String = "" // 单据类型
        var checkInventoryNow = "" // 是否检查库存
        var strStockGroup = "" // 保存仓库组id，用，号隔开(stockK3Id,stockId,stockAreaId,storageRackId,stockPosId)
        var sourceBarcode = "" // 条码来源
        var isSmBoxBarcode = "" // 是否可以扫描箱码
        var isProdInStockDB = "" // 更新箱码的是否生产入库调拨状态
        when(smqFlag) {
            '1' -> {
                mUrl = getURL("stockPosition/findBarcodeGroup")
                barcode = getValues(et_positionCode)
            }
            '2' -> {
                mUrl = getURL("stockBill_WMS/findBarcode_EntryItem")
                barcode = getValues(et_code)
                icstockBillId = parent!!.fragment1.icStockBill.id.toString()
                moreStock = "1"
                billType = parent!!.fragment1.icStockBill.billType
                checkInventoryNow = "1"
                strStockGroup = icStockBillEntry.fdcStockId.toString() +","+ icStockBillEntry.stockId_wms +","+ icStockBillEntry.stockAreaId_wms +","+
                        icStockBillEntry.storageRackId_wms +","+ icStockBillEntry.stockPosId_wms
                sourceBarcode = "1"
                isSmBoxBarcode = "1"
                isProdInStockDB = "1"
            }
        }
        val formBody = FormBody.Builder()
                .add("barcode", barcode)
                .add("icstockBillId", icstockBillId)
                .add("moreStock", moreStock)
                .add("billType", billType)
                .add("checkInventoryNow", checkInventoryNow)
                .add("strStockGroup", strStockGroup)
                .add("sourceBarcode", sourceBarcode)
                .add("isSmBoxBarcode", isSmBoxBarcode)
                .add("isProdInStockDB", isProdInStockDB)
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