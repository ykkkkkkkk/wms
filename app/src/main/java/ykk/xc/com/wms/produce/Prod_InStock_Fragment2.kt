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
import kotlinx.android.synthetic.main.prod_in_fragment1.tv_deptSel
import kotlinx.android.synthetic.main.prod_in_stock_fragment2.*
import okhttp3.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import ykk.xc.com.wms.R
import ykk.xc.com.wms.basics.BatchAndNumInputDialog
import ykk.xc.com.wms.basics.MoreBatchInputDialog
import ykk.xc.com.wms.basics.Mtl_DialogActivity
import ykk.xc.com.wms.basics.Stock_GroupDialogActivity
import ykk.xc.com.wms.bean.*
import ykk.xc.com.wms.bean.k3Bean.ICInventory
import ykk.xc.com.wms.bean.k3Bean.ICItem
import ykk.xc.com.wms.bean.k3Bean.MeasureUnit
import ykk.xc.com.wms.bean.prod.ProdOrder
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
 * 描述：生产入库---添加明细
 * 作者：ykk
 */
class Prod_InStock_Fragment2 : BaseFragment() {

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
        private val RESULT_FKFPERIOD = 10
        private val SM_RESULT_NUM = 11
    }
    private val context = this
    private var okHttpClient: OkHttpClient? = null
    private var user: User? = null
    private var stock:Stock? = null
    private var stockArea:StockArea? = null
    private var storageRack:StorageRack? = null
    private var stockPos:StockPosition? = null
    private var mContext: Activity? = null
    private val df = DecimalFormat("#.######")
    private var parent: Prod_InStock_MainActivity? = null
    private var isTextChange: Boolean = false // 是否进入TextChange事件
    private var timesTamp:String? = null // 时间戳
    var icStockBillEntry = ICStockBillEntry()
    private var smICStockBillEntry:ICStockBillEntry? = null // 扫码返回的对象
    private var autoICStockBillEntry:ICStockBillEntry? = null // 用于自动保存记录的对象
    private var smICStockBillEntry_Barcodes = ArrayList<ICStockBillEntry_Barcode>() // 扫码返回的对象
    private var smqFlag = '2' // 扫描类型1：位置扫描，2：物料扫描，3：箱码扫描
    private var isWeightTextChanged = true // 是否改变称重数就执行改变事件
    private var isReferenceTextChanged = true // 是否改变参考数就执行改变事件
    private var addFlag = false // 是否扫描就新增一行
    private var clickSaveButtom = false // 是否点击保存按钮


    // 消息处理
    private val mHandler = MyHandler(this)
    private class MyHandler(activity: Prod_InStock_Fragment2) : Handler() {
        private val mActivity: WeakReference<Prod_InStock_Fragment2>

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
                                val list = JsonUtil.strToList(msgObj, BarCodeTable::class.java)
                                if(list.size == 1) {
                                    m.addFlag = true
                                    m.setICStockEntry_ProdOrder(list[0])

                                } else if(list.size == 2) {
                                    m.addFlag = false
                                    val icEntry = JsonUtil.stringToObject(list[1].relationObj, ICStockBillEntry::class.java)
                                    if(m.getValues(m.tv_mtlName).length > 0 && m.smICStockBillEntry != null && m.smICStockBillEntry!!.id != icEntry.id) {
                                        // 上次扫的和这次的不同，就自动保存
                                        if(!m.checkSave()) return
                                        m.icStockBillEntry.icstockBillId = m.parent!!.fragment1.icStockBill.id
//                                    m.icStockBillEntry.fkfDate = m.getValues(m.tv_fkfDate)

                                        m.autoICStockBillEntry = icEntry // 加到自动保存对象
                                        m.run_save(null,1,0)
//                                    Comm.showWarnDialog(m.mContext,"请先保存当前数据！")
                                        return
                                    }
                                    m.getMaterial(icEntry)
                                }
                            }
                            '3'-> { // 箱码
                                val boxBarCode = JsonUtil.strToObject(msgObj, BoxBarCode::class.java)
                                if(boxBarCode.listMbr[0].dept.productStock == null) {
                                    Comm.showWarnDialog(m.mContext,"请在金蝶中部门（"+boxBarCode.listMbr[0].dept.departmentName+"）设置的成品仓库，然后在PC端同步！")
                                    return
                                }
                                m.tv_boxName.text = boxBarCode.box.boxName
                                m.setICStockEntry_BoxBarCode(boxBarCode)
                            }
                        }
                    }
                    UNSUCC1 -> { // 扫码失败
                        when(m.smqFlag) {
                            '1' -> { // 仓库位置扫描
                                m.tv_positionName.text = ""
                            }
                            '2' -> { // 物料扫描
                                m.tv_icItemName.text = ""
                            }
                            '3' -> { // 箱码扫描
                                m.tv_boxName.text = ""
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
                        if(m.addFlag) {
                            m.addFlag = false
                            val barcode = m.getValues(m.et_code)
                            m.et_code.setText("")
                            m.setTexts(m.et_code, barcode)
                        } else {
                            // 保存了分录，供应商就不能修改
                            EventBus.getDefault().post(EventBusEntity(21)) // 发送指令到fragment3，告其刷新
                            m.reset(1)
//                        m.toasts("保存成功✔")
                            // 如果有自动保存的对象，保存后就显示下一个
                            if (m.autoICStockBillEntry != null) {
                                m.toasts("自动保存成功✔")
                                m.getMaterial(m.autoICStockBillEntry!!)
                                m.autoICStockBillEntry = null

                            } else {
                                m.toasts("保存成功✔")
                            }
                        }
                    }
                    UNSAVE -> { // 保存失败
                        errMsg = JsonUtil.strToString(msgObj)
                        if (m.isNULLS(errMsg).length == 0) errMsg = "保存失败！"
                        if(m.clickSaveButtom && errMsg.equals("UpdateConfirmDialog")) { // 提示是否同时修改物料默认仓库
                            m.clickSaveButtom = false
                            val build = AlertDialog.Builder(m.mContext)
                            build.setIcon(R.drawable.caution)
                            build.setTitle("系统提示")
                            build.setMessage("物料默认仓库不一致，是否修改？")
                            build.setPositiveButton("是") { dialog, which -> m.run_save(null, 1,1) }
                            build.setNegativeButton("否") {dialog, which -> m.run_save(null, 0,0) }
                            build.setCancelable(false)
                            build.show()
                        } else {
                            Comm.showWarnDialog(m.mContext, errMsg)
                        }
                    }
                    SETFOCUS -> { // 当弹出其他窗口会抢夺焦点，需要跳转下，才能正常得到值
                        m.setFocusable(m.et_getFocus)
                        when(m.smqFlag) {
                            '1'-> m.setFocusable(m.et_positionCode)
                            '2'-> m.setFocusable(m.et_code)
                            '3'-> m.setFocusable(m.et_boxCode)
                        }
                    }
                    SAOMA -> { // 扫码之后
                        when(m.smqFlag) {
                            '2' -> {
//                                if(m.getValues(m.tv_mtlName).length > 0) {
//                                    Comm.showWarnDialog(m.mContext,"请先保存当前数据！")
//                                    m.isTextChange = false
//                                    return
//                                }
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
        return inflater.inflate(R.layout.prod_in_stock_fragment2, container, false)
    }

    override fun initView() {
        mContext = getActivity()
        parent = mContext as Prod_InStock_MainActivity
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
        hideSoftInputMode(mContext, et_boxCode)
        tv_fkfDate.text = Comm.getSysDate(7) // 初始化---生产/采购日期

        icStockBillEntry.weightUnitType = 2
        parent!!.fragment1.icStockBill.fselTranType = 85
        icStockBillEntry.fsourceTranType = 85
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            // 显示部门对应的仓库
//            if(parent!!.fragment1.icStockBill.fdeptId > 0) {
//                stock = parent!!.fragment1.icStockBill.department.productStock
//                getStockGroup(null)
//            }
            mHandler.sendEmptyMessageDelayed(SETFOCUS, 200)
        }
    }

    @OnClick(R.id.btn_scan, R.id.btn_mtlSel, R.id.btn_positionScan, R.id.btn_positionSel, R.id.btn_scanBox, R.id.tv_num, R.id.tv_weight, R.id.tv_batchNo,
            R.id.tv_unitSel, R.id.tv_fkfDate, R.id.tv_fkfPeriod, R.id.tv_remark, R.id.btn_save, R.id.btn_clone, R.id.tv_weightUnitType,
            R.id.tv_connBlueTooth2, R.id.tv_positionName, R.id.tv_icItemName, R.id.tv_boxName)
    fun onViewClicked(view: View) {
        when (view.id) {
            R.id.btn_positionSel -> { // 选择仓库
                smqFlag = '1'
                val bundle = Bundle()
                bundle.putSerializable("stock", stock)
                bundle.putSerializable("stockArea", stockArea)
                bundle.putSerializable("storageRack", storageRack)
                bundle.putSerializable("stockPos", stockPos)
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
            R.id.btn_scanBox -> {
                smqFlag = '3'
                showForResult(CaptureActivity::class.java, BaseFragment.CAMERA_SCAN, null)
            }
            R.id.tv_positionName -> { // 位置点击
                smqFlag = '1'
                mHandler.sendEmptyMessageDelayed(SETFOCUS, 200)
            }
            R.id.tv_icItemName -> { // 物料点击
                smqFlag = '2'
                mHandler.sendEmptyMessageDelayed(SETFOCUS, 200)
            }
            R.id.tv_boxName -> { // 箱码点击
                smqFlag = '3'
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
            R.id.tv_fkfDate -> { // 生产/采购日期
                Comm.showDateDialog(mContext, view, 0)
            }
            R.id.tv_fkfPeriod -> { // 保质期
                showInputDialog("保质期", icStockBillEntry.fkfPeriod.toString(), "0", RESULT_FKFPERIOD)
            }
            R.id.tv_remark -> { // 备注
                showInputDialog("备注", icStockBillEntry.remark, "none", RESULT_REMAREK)
            }
            R.id.btn_save -> { // 保存
                if(!checkSave()) return
                icStockBillEntry.icstockBillId = parent!!.fragment1.icStockBill.id
//                icStockBillEntry.fkfDate = getValues(tv_fkfDate)
                clickSaveButtom = true
                run_save(null,1,0)
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
            Comm.showWarnDialog(mContext, "请选择位置！")
            return false
        }
//        if (icStockBillEntry.fprice == 0.0) {
//            Comm.showWarnDialog(mContext, "请输入单价！")
//            return false;
//        }
        if(icStockBillEntry.icItem.batchManager.equals("Y") && icStockBillEntry.icstockBillEntry_Barcodes.size == 0) {
            Comm.showWarnDialog(mContext, "请输入批次！")
            return false
        }
        if (icStockBillEntry.fqty == 0.0) {
            Comm.showWarnDialog(mContext, "请输入数量！")
            return false
        }
        if (icStockBillEntry.weight == 0.0 && (icStockBillEntry.icItem.calByWeight.equals("M") || icStockBillEntry.icItem.calByWeight.equals("Y")) && !icStockBillEntry.icItem.snManager.equals("Y")) {
            Comm.showWarnDialog(mContext, "请输入称重数量或连接蓝牙自动称重！")
            return false
        }
        if(icStockBillEntry.icItem.isQualityPeriodManager.equals("Y") && icStockBillEntry.fkfPeriod == 0) {
            Comm.showWarnDialog(mContext, "请输入保质期！")
            return false
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

        // 箱码---数据变化
        et_boxCode!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (s.length == 0) return
                if (!isTextChange) {
                    isTextChange = true
                    smqFlag = '3'
                    mHandler.sendEmptyMessageDelayed(SAOMA, 300)
                }
            }
        })
        // 物料---长按输入条码
        et_boxCode!!.setOnLongClickListener {
            smqFlag = '3'
            showInputDialog("输入条码号", getValues(et_boxCode), "none", WRITE_CODE)
            true
        }
        // 物料---焦点改变
        et_boxCode.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if(hasFocus) {
                lin_focusBox.setBackgroundResource(R.drawable.back_style_red_focus)
            } else {
                if (lin_focusBox != null) {
                    lin_focusBox!!.setBackgroundResource(R.drawable.back_style_gray4)
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
//        if(parent!!.fragment1.icStockBill.fselTranType == 0 && flag == 0 ) {
        if(flag == 0 ) {
            lin_getMtl.visibility = View.VISIBLE
            tv_positionName.text = ""
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
        setEnables(tv_batchNo, R.drawable.back_style_blue, true)
        setEnables(tv_num, R.drawable.back_style_blue, true)
        setEnables(tv_weight, R.drawable.back_style_blue, true)
        setEnables(tv_fkfPeriod, R.drawable.back_style_blue, true)
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
        et_boxCode.setText("")
        tv_boxName.text = ""

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

        timesTamp = user!!.getId().toString() + "-" + Comm.randomUUID()
        parent!!.isChange = false
        smqFlag = '2'
        mHandler.sendEmptyMessageDelayed(SETFOCUS, 200)

        // 仓库重置为不可选择
        lin_focusPosition.setBackgroundResource(R.drawable.back_style_gray3)
        btn_positionScan.visibility = View.GONE
        et_positionCode.visibility = View.GONE
        et_positionCode.isEnabled = false
        btn_positionSel.visibility = View.GONE
    }

    /**
     *  扫码之后    物料启用批次
     */
    fun setBatchCode(fqty : Double, batchCode : String) {
        if(smICStockBillEntry_Barcodes != null && smICStockBillEntry_Barcodes.size > 0) {
            smICStockBillEntry_Barcodes.forEach {
                if(it.batchCode == batchCode) {
                    it.fqty = fqty
                }
            }
        } else {
            val entryBarcode = ICStockBillEntry_Barcode()
            entryBarcode.parentId = smICStockBillEntry!!.id
            entryBarcode.barcode = getValues(et_code)
            entryBarcode.batchCode = if (isNULLS(smICStockBillEntry!!.smBatchCode).length == 0) batchCode else smICStockBillEntry!!.smBatchCode
            entryBarcode.snCode = ""
            entryBarcode.fqty = fqty
            entryBarcode.isUniqueness = 'N'
            entryBarcode.againUse = 0
            entryBarcode.createUserName = user!!.username
            entryBarcode.billType = parent!!.fragment1.icStockBill.billType

            smICStockBillEntry_Barcodes.add(entryBarcode)
        }
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
    fun unStartBatchOrSnCode(fqty : Double) {
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

    fun getMaterial(icEntry : ICStockBillEntry) {
        smICStockBillEntry = icEntry

        btn_save.text = "保存"
        // 判断条码是否存在（启用批次，序列号）
//        if (icStockBillEntry.icstockBillEntry_Barcodes.size > 0 && (icEntry.icItem.batchManager.equals("Y") || icEntry.icItem.snManager.equals("Y"))) {
        if (icStockBillEntry.icstockBillEntry_Barcodes.size > 0 && icEntry.icItem.snManager.equals("Y")) {
            icStockBillEntry.icstockBillEntry_Barcodes.forEach {
                if (getValues(et_code) == it.barcode) {
                    Comm.showWarnDialog(mContext,"条码已使用！")
                    return
                }
            }
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
        if(icEntry.icItem.batchManager.equals("Y")) { // 启用批次号
//            val showInfo:String = "<font color='#666666'>批次号：</font>" + icEntry.smBatchCode
//            showInputDialog("数量", showInfo, "", "0.0", SM_RESULT_NUM)
            val bundle = Bundle()
            bundle.putString("mtlName", icEntry.mtlName)
            bundle.putString("batchCode", if(isNULLS(icEntry.smBatchCode).length == 0) getValues(tv_batchNo) else icEntry.smBatchCode)
            bundle.putDouble("fqty", 0.0)
            showForResult(BatchAndNumInputDialog::class.java, SM_RESULT_NUM, bundle)

        } else if(icEntry.icItem.snManager.equals("Y")) { // 启用序列号
            setSnCode()

        } else { // 未启用
            unStartBatchOrSnCode(1.0)
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
//        icStockBillEntry.fbatchNo = icEntry.fbatchNo
//        icStockBillEntry.fqty = icEntry.fqty
        icStockBillEntry.fsourceQty = icEntry.fsourceQty
        icStockBillEntry.fprice = icEntry.fprice
        icStockBillEntry.weight = icEntry.weight
        icStockBillEntry.referenceNum = icEntry.referenceNum
        icStockBillEntry.funitId = icEntry.funitId
        icStockBillEntry.mtlNumber = icEntry.mtlNumber
        icStockBillEntry.mtlName = icEntry.mtlName
        icStockBillEntry.fmode = icEntry.fmode
        icStockBillEntry.inStockName = icEntry.inStockName
        icStockBillEntry.inStockPosName = icEntry.inStockPosName
        icStockBillEntry.unitName = icEntry.unitName
//        icStockBillEntry.fkfDate = icEntry.fkfDate
//        icStockBillEntry.fkfPeriod = icEntry.fkfPeriod
        icStockBillEntry.remark = icEntry.remark

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
        if(icEntry.icItem.batchManager.equals("Y") || icEntry.icItem.snManager.equals("Y")) {
            setEnables(tv_num, R.drawable.back_style_gray3, false)
        } else {
            setEnables(tv_num, R.drawable.back_style_blue, true)
        }
//        tv_num.text = if(icEntry.fqty > 0) df.format(icEntry.fqty) else ""
        tv_sourceQty.text = if(icEntry.fsourceQty > 0) df.format(icEntry.fsourceQty) else ""
        tv_referenceNum.text = if(icEntry.referenceNum > 0) df.format(icEntry.referenceNum) else ""
        tv_weight.text = if(icEntry.weight > 0) df.format(icEntry.weight) else ""
        tv_unitSel.text = icEntry.unitName
        if(getValues(tv_fkfDate).length == 0) {
            tv_fkfDate.text = Comm.getSysDate(7)
        } else {
            tv_fkfDate.text = icEntry.fkfDate
        }
        tv_fkfPeriod.text = if(icEntry.fkfPeriod > 0) df.format(icEntry.fkfPeriod) else ""
        if(icEntry.icItem.isQualityPeriodManager.equals("Y")) {
            setEnables(tv_fkfPeriod, R.drawable.back_style_blue, true)
        } else {
            setEnables(tv_fkfPeriod, R.drawable.back_style_gray3, false)
        }
        tv_remark.text = icEntry.remark

        // 如果物料启用了称重
        if(icEntry.icItem.calByWeight.equals("M") || icEntry.icItem.calByWeight.equals("N") || icEntry.icItem.snManager.equals("Y")) {
            setEnables(tv_weight, R.drawable.back_style_gray3, false)
        } else {
            setEnables(tv_weight, R.drawable.back_style_blue, true)
        }

//        val mul = BigdecimalUtil.mul(icEntry.fprice, icEntry.fqty)
//        tv_sumMoney.text = df.format(mul)
        // 查询即时库存
        run_findInventoryQty()
        // 显示仓库
        if(icEntry.stockId_wms > 0) {
            stock = icEntry.stock
            stockArea = icEntry.stockArea
            storageRack = icEntry.storageRack
            stockPos = icEntry.stockPos
        }
        getStockGroup(null)
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

        // 如果仓库为空，就显示可以选择
        if(icEntry.fdcStockId == 0) {
            lin_focusPosition.setBackgroundResource(R.drawable.back_style_blue)
            btn_positionScan.visibility = View.VISIBLE
            et_positionCode.visibility = View.VISIBLE
            et_positionCode.isEnabled = true
            btn_positionSel.visibility = View.VISIBLE
        }
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
//                        val value = bundle.getString("resultValue", "")
//                        val num = parseDouble(value)
                        val batchCode = bundle.getString("batchCode")
                        val fqty = bundle.getDouble("fqty")
                        setBatchCode(fqty, batchCode)
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
            RESULT_FKFPERIOD -> { // 保质期    返回
                if (resultCode == Activity.RESULT_OK) {
                    val bundle = data!!.getExtras()
                    if (bundle != null) {
                        val value = bundle.getString("resultValue", "")
                        if(parseInt(value) <= 0) {
                            Comm.showWarnDialog(mContext,"保质期必须大于0！")
                            return
                        }
                        tv_fkfPeriod.text = value
                        icStockBillEntry.fkfPeriod = parseInt(value)
                    }
                }
            }
            RESULT_PUR_ORDER -> { // 选择单据   返回
                if (resultCode == Activity.RESULT_OK) {
                    if(icStockBillEntry.fsourceTranType == 85) {
                        val list = data!!.getSerializableExtra("obj") as List<ProdOrder>
//                        setICStockEntry_ProdOrder(list)
//                    } else if(icStockBillEntry.fsourceTranType == 72){
//                        val list = data!!.getSerializableExtra("obj") as List<POInStockEntry>
//                        setICStockEntry_POInStock(list)
                    }
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
                            '3' -> setTexts(et_boxCode, code)
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
                            '3' -> setTexts(et_boxCode, value.toUpperCase())
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
        stock = null
        stockArea = null
        storageRack = null
        stockPos = null
    }

    /**
     * 得到仓库组
     */
    fun getStockGroup(msgObj : String?) {
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
                    tv_positionName.text = stock!!.stockName
                }
                2 -> {
                    stockArea = JsonUtil.strToObject(msgObj, StockArea::class.java)
                    tv_positionName.text = stockArea!!.fname
                    if(stockArea!!.stock != null) stock = stockArea!!.stock
                }
                3 -> {
                    storageRack = JsonUtil.strToObject(msgObj, StorageRack::class.java)
                    tv_positionName.text = storageRack!!.fnumber
                    if(storageRack!!.stock != null) stock = storageRack!!.stock
                    if(storageRack!!.stockArea != null)  stockArea = storageRack!!.stockArea
                }
                4 -> {
                    stockPos = JsonUtil.strToObject(msgObj, StockPosition::class.java)
                    tv_positionName.text = stockPos!!.stockPositionName
                    if(stockPos!!.stock != null) stock = stockPos!!.stock
                    if(stockPos!!.stockArea != null)  stockArea = stockPos!!.stockArea
                    if(stockPos!!.storageRack != null)  storageRack = stockPos!!.storageRack
                }
            }
        }

        if(stock != null ) {
            tv_positionName.text = stock!!.stockName
            icStockBillEntry.fdcStockId = stock!!.fitemId
            icStockBillEntry.stockId_wms = stock!!.id
            icStockBillEntry.inStockName = stock!!.stockName
        }
        if(stockArea != null ) {
            tv_positionName.text = stockArea!!.fname
            icStockBillEntry.stockAreaId_wms = stockArea!!.id
        }
        if(storageRack != null ) {
            tv_positionName.text = storageRack!!.fnumber
            icStockBillEntry.storageRackId_wms = storageRack!!.id
        }
        if(stockPos != null ) {
            tv_positionName.text = stockPos!!.stockPositionName
            icStockBillEntry.fdcSPId = stockPos!!.fitemId
            icStockBillEntry.stockPosId_wms = stockPos!!.id
            icStockBillEntry.inStockPosName = stockPos!!.stockPositionName
        }

        if(stock != null) {
            // 自动跳到物料焦点
            smqFlag = '2'
            mHandler.sendEmptyMessage(SETFOCUS)
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

    private fun setICStockEntry_ProdOrder(bt : BarCodeTable) {
        var it = JsonUtil.stringToObject(bt.relationObj, ProdOrder::class.java)
        // 不同车间
        if(parent!!.fragment1.icStockBill.fdeptId > 0 && parent!!.fragment1.icStockBill.fdeptId != it.department.fitemID) {
            addFlag = false
            Comm.showWarnDialog(mContext,"请扫描相同（生产车间）的生产任务单条码！")
            return
        }
        if(it.department.productStock == null) {
            addFlag = false
            Comm.showWarnDialog(mContext,"请在金蝶中部门（"+it.department.departmentName+"）设置的成品仓库，然后在PC端同步！")
            return
        }
        if(parent!!.fragment1.icStockBill.fdeptId == 0) {
            val dept = it.department
            parent!!.fragment1.tv_deptSel.text = dept.departmentName
            parent!!.fragment1.icStockBill.fdeptId = dept.fitemID
            parent!!.fragment1.icStockBill.deptName = dept.departmentName
            parent!!.fragment1.icStockBill.department = dept
            // 显示生产成品仓
            stock = dept.productStock
            getStockGroup(null)
            // 发送指令到fragment1，告其保存
            parent!!.fragment1.saveNeedHint = false
            parent!!.fragment1.run_save()
        }

        parent!!.fragment1.icStockBill.fselTranType = 85
        icStockBillEntry.icstockBillId = parent!!.fragment1.icStockBill.id
        icStockBillEntry.fitemId = it.icItem.fitemid
//            entry.fentryId = it.fentryid
        icStockBillEntry.fdcStockId = icStockBillEntry.fdcStockId
        icStockBillEntry.fdcSPId = icStockBillEntry.fdcSPId
        icStockBillEntry.fqty = 0.0
        icStockBillEntry.fprice = 0.0
        icStockBillEntry.funitId = it.unitId
        icStockBillEntry.fsourceInterId = it.prodId
        icStockBillEntry.fsourceEntryId = it.prodId
//        icStockBillEntry.fsourceQty = it.fqty
        icStockBillEntry.fsourceQty = it.useableQty
        icStockBillEntry.qcPassQty = it.fauxInHighLimitQty
        icStockBillEntry.fsourceTranType = 85
        icStockBillEntry.fsourceBillNo = it.prodNo
        icStockBillEntry.fdetailId = it.prodId

        icStockBillEntry.mtlNumber = it.icItem.fnumber
        icStockBillEntry.mtlName = it.icItem.fname
        icStockBillEntry.fmode = it.icItem.fmodel
        icStockBillEntry.inStockName = icStockBillEntry.inStockName
        icStockBillEntry.inStockPosName = ""
        icStockBillEntry.unitName = it.unitName
        icStockBillEntry.remark = ""
        icStockBillEntry.icItem = it.icItem

        // 设置表头的部门


        run_save(null,0,0)
    }

    private fun setICStockEntry_BoxBarCode(boxBarCode : BoxBarCode) {
        // 不同车间
        if(parent!!.fragment1.icStockBill.fdeptId > 0 && parent!!.fragment1.icStockBill.fdeptId != boxBarCode.listMbr[0].fdeptId) {
            addFlag = false
            Comm.showWarnDialog(mContext,"请扫描相同生产车间的条码！")
            return
        }
        if(parent!!.fragment1.icStockBill.fdeptId == 0) {
            val dept = boxBarCode.listMbr[0].dept
            parent!!.fragment1.tv_deptSel.text = dept.departmentName
            parent!!.fragment1.icStockBill.fdeptId = dept.fitemID
            parent!!.fragment1.icStockBill.deptName = dept.departmentName
            parent!!.fragment1.icStockBill.department = dept
            // 显示生产成品仓
            stock = dept.productStock
            getStockGroup(null)
            // 发送指令到fragment1，告其保存
            parent!!.fragment1.saveNeedHint = false
            parent!!.fragment1.run_save()
        }

        parent!!.fragment1.icStockBill.fselTranType = 85
        var listEntry = ArrayList<ICStockBillEntry>()
        boxBarCode.listMbr.forEach {
            val entry = ICStockBillEntry()
            entry.icstockBillId = parent!!.fragment1.icStockBill.id
            entry.fitemId = it.fitemId
//            entry.fentryId = it.fentryid
            entry.fdcStockId = icStockBillEntry.fdcStockId
            entry.fdcSPId = icStockBillEntry.fdcSPId
            entry.stockId_wms = icStockBillEntry.stockId_wms
            entry.stockAreaId_wms = icStockBillEntry.stockAreaId_wms
            entry.storageRackId_wms = icStockBillEntry.storageRackId_wms
            entry.stockPosId_wms = icStockBillEntry.stockPosId_wms
            entry.fqty = it.fqty
            entry.fprice = 0.0
            entry.funitId = it.icItem.unit.funitId
            entry.fsourceInterId = it.fsourceInterId
            entry.fsourceEntryId = it.fsourceEntryId
            entry.fsourceQty = it.fsourceQty
            entry.qcPassQty = it.fsourceHightLimitQty
            entry.fsourceTranType = 85
            entry.fsourceBillNo = it.fsourceNo
            entry.fdetailId = it.fsourceInterId

            entry.mtlNumber = it.icItem.fnumber
            entry.mtlName = it.icItem.fname
            entry.fmode = it.icItem.fmodel
            entry.inStockName = icStockBillEntry.inStockName
            entry.inStockPosName = ""
            entry.unitName = it.icItem.unit.unitName
            entry.remark = ""
            entry.boxBarCodeId = boxBarCode.id

            listEntry.add(entry)
        }
        run_save(listEntry,0,0)
    }

    /**
     * 扫码查询对应的方法
     */
    private fun run_smDatas() {
        isTextChange = false
        showLoadDialog("加载中...", false)
        var mUrl:String? = null
        var barcode:String? = null
        var icstockBillId:String? = null
        var mbrType = "" // 1:生产装箱，2：销售装箱
        when(smqFlag) {
            '1' -> {
                mUrl = getURL("stockPosition/findBarcodeGroup")
                barcode = getValues(et_positionCode)
                icstockBillId = ""
            }
            '2' -> {
                mUrl = getURL("prodOrder/findBarcodeByInStock")
                barcode = getValues(et_code)
                icstockBillId = parent!!.fragment1.icStockBill.id.toString()
            }
            '3' -> {
                mUrl = getURL("boxBarCode/findBarcodeByIcStockBill")
                barcode = getValues(et_boxCode)
                icstockBillId = ""
                mbrType = "1"
            }
        }
        val formBody = FormBody.Builder()
                .add("barcode", barcode)
                .add("icstockBillId", icstockBillId)
                .add("mbrType", mbrType)
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
    private fun run_save(list: List<ICStockBillEntry>?, checkMtlStockEqual: Int, isUpdateMtlStock: Int) {
        showLoadDialog("保存中...", false)
        var mUrl:String? = null
        var mJson:String? = null
        var isInStock:String? = null // 装箱是否入库
        var updateBarCodeTableBatchCode:String? = null // 保存条码时，是否更新BarCodeTable 的批次号
        if(list != null) {
            mUrl = getURL("stockBill_WMS/saveEntryList")
            mJson = JsonUtil.objectToString(list)
            isInStock = "1"
            updateBarCodeTableBatchCode = ""
        } else {
            mUrl = getURL("stockBill_WMS/saveEntry")
            mJson = JsonUtil.objectToString(icStockBillEntry)
            isInStock = ""
            updateBarCodeTableBatchCode = "1"
        }
        val formBody = FormBody.Builder()
                .add("strJson", mJson)
                .add("isInStock", isInStock)
                .add("updateBarCodeTableBatchCode", updateBarCodeTableBatchCode)
                .add("checkMtlStockEqual", checkMtlStockEqual.toString()) // 是否检查物料的默认仓库是否和当前一致
                .add("isUpdateMtlStock", isUpdateMtlStock.toString())
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