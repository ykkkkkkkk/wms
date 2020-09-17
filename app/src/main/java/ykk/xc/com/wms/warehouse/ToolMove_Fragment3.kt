package ykk.xc.com.wms.warehouse

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.OnClick
import kotlinx.android.synthetic.main.ware_tool_move_fragment3.*
import okhttp3.*
import ykk.xc.com.wms.R
import ykk.xc.com.wms.basics.Stock_GroupDialogActivity
import ykk.xc.com.wms.bean.*
import ykk.xc.com.wms.bean.k3Bean.ICItem
import ykk.xc.com.wms.comm.BaseFragment
import ykk.xc.com.wms.comm.Comm
import ykk.xc.com.wms.util.JsonUtil
import ykk.xc.com.wms.util.LogUtil
import ykk.xc.com.wms.util.zxing.android.CaptureActivity
import java.io.IOException
import java.lang.ref.WeakReference
import java.text.DecimalFormat
import java.util.concurrent.TimeUnit

/**
 * 日期：2019-10-16 09:50
 * 描述：物料移库
 * 作者：ykk
 */
class ToolMove_Fragment3 : BaseFragment() {

    companion object {
        private val SEL_STOCK = 60
        private val SEL_STOCK2 = 61
        private val SEL_MTL = 62
        private val SUCC1 = 200
        private val UNSUCC1 = 500
        private val SAVE = 202
        private val UNSAVE = 502

        private val SETFOCUS = 1
        private val SAOMA = 2
        private val WRITE_CODE = 3
        private val RESULT_NUM = 4
    }
    private val context = this
    private var okHttpClient: OkHttpClient? = null
    private var user: User? = null
    private var stock:Stock? = null
    private var stockArea:StockArea? = null
    private var storageRack:StorageRack? = null
    private var stockPos:StockPosition? = null
    private var icItem:ICItem? = null
    private var stock2:Stock? = null
    private var stockArea2:StockArea? = null
    private var storageRack2:StorageRack? = null
    private var stockPos2:StockPosition? = null
    private var mContext: Activity? = null
    private var parent: ToolMove_MainActivity? = null
    private var isTextChange: Boolean = false // 是否进入TextChange事件
    private var smqType = 1 // 扫描类型1：移出位置扫描，2：移出物料扫描，3：移入位置扫描
    private val df = DecimalFormat("#.######")
    private var stockQty = 0.0  // 库存数

    // 消息处理
    private val mHandler = MyHandler(this)
    private class MyHandler(activity: ToolMove_Fragment3) : Handler() {
        private val mActivity: WeakReference<ToolMove_Fragment3>

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
                        when(m.smqType) {
                            1 -> { // 移出位置
                                m.resetStockGroup2()
                                m.getStockGroup2(msgObj)
                            }
                            2 -> { // 物料
                                val icitem = JsonUtil.strToObject(msgObj, ICItem::class.java)

                                val bundle = Bundle()
                                bundle.putSerializable("stock", m.stock2)
                                bundle.putSerializable("stockArea", m.stockArea2)
                                bundle.putSerializable("storageRack", m.storageRack2)
                                bundle.putSerializable("stockPos", m.stockPos2)
                                bundle.putInt("mtlId", icitem.fitemid)
                                bundle.putString("batchCode", icitem.batchCode)
                                m.showForResult(InventoryNowByStock_DialogActivity::class.java, SEL_MTL, bundle)
//                                m.getMtlAfter(icitem)
                            }
                            3 -> { // 移入位置
                                m.resetStockGroup()
                                m.getStockGroup(msgObj)
                            }
                        }
                    }
                    UNSUCC1 -> { // 扫码失败
                        errMsg = JsonUtil.strToString(msgObj)
                        if (m.isNULLS(errMsg).length == 0) errMsg = "很抱歉，没有找到数据！"
                        Comm.showWarnDialog(m.mContext, errMsg)
                    }
                    SAVE -> { // 保存成功 进入
                        m.reset(1)
                        m.toasts("操作成功✔")
                    }
                    UNSAVE -> { // 保存失败
                        errMsg = JsonUtil.strToString(msgObj)
                        if (m.isNULLS(errMsg).length == 0) errMsg = "操作失败！"
                        Comm.showWarnDialog(m.mContext, errMsg)
                    }
                    SETFOCUS -> { // 当弹出其他窗口会抢夺焦点，需要跳转下，才能正常得到值
                        m.setFocusable(m.et_getFocus)
                        when(m.smqType) {
                            1 -> m.setFocusable(m.et_positionCode2)
                            2 -> m.setFocusable(m.et_code)
                            3 -> m.setFocusable(m.et_positionCode)

                        }
                    }
                    SAOMA -> { // 扫码之后
                        if(m.smqType == 2 && m.stock2 == null) {
                            m.isTextChange = false
                            Comm.showWarnDialog(m.mContext,"请扫描移出位置条码或选择！")
                            return
                        }
                        // 执行查询方法
                        m.run_smDatas()
                    }
                }
            }
        }
    }

    override fun setLayoutResID(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.ware_tool_move_fragment3, container, false)
    }

    override fun initView() {
        mContext = getActivity()
        parent = mContext as ToolMove_MainActivity

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
        hideSoftInputMode(mContext, et_positionCode2)
        hideSoftInputMode(mContext, et_code)
        hideSoftInputMode(mContext, et_positionCode)

    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            mHandler.sendEmptyMessageDelayed(SETFOCUS, 200)
        }
    }

    @OnClick( R.id.btn_positionScan, R.id.btn_scan, R.id.btn_positionScan2,
              R.id.btn_positionSel, R.id.btn_mtlSel, R.id.btn_positionSel2,
              R.id.btn_clone, R.id.btn_save, R.id.tv_num )
    fun onViewClicked(view: View) {
        when (view.id) {
            R.id.btn_positionSel2 -> { // 选择移出位置
                smqType = 1
                val bundle = Bundle()
                bundle.putSerializable("stock", stock2)
                bundle.putSerializable("stockArea", stockArea2)
                bundle.putSerializable("storageRack", storageRack2)
                bundle.putSerializable("stockPos", stockPos2)
                showForResult(Stock_GroupDialogActivity::class.java, SEL_STOCK2, bundle)
            }
            R.id.btn_mtlSel -> { // 选择物料
                if(stock2 == null) {
                    Comm.showWarnDialog(mContext,"请扫描移出位置条码或选择！")
                    return
                }
                smqType = 2
                val bundle = Bundle()
                bundle.putSerializable("stock", stock2)
                bundle.putSerializable("stockArea", stockArea2)
                bundle.putSerializable("storageRack", storageRack2)
                bundle.putSerializable("stockPos", stockPos2)
//                bundle.putString("batchCode", if(icItem != null) icItem!!.batchCode else "")
//                showForResult(Mtl_DialogActivity::class.java, SEL_MTL, bundle)``
                showForResult(InventoryNowByStock_DialogActivity::class.java, SEL_MTL, bundle)
            }
            R.id.btn_positionSel -> { // 选择移入位置
                smqType = 3
                val bundle = Bundle()
                bundle.putSerializable("stock", stock)
                bundle.putSerializable("stockArea", stockArea)
                bundle.putSerializable("storageRack", storageRack)
                bundle.putSerializable("stockPos", stockPos)
                showForResult(Stock_GroupDialogActivity::class.java, SEL_STOCK, bundle)
            }
            R.id.btn_positionScan2 -> { // 调用摄像头扫描（移出位置）
                smqType = 1
                showForResult(CaptureActivity::class.java, BaseFragment.CAMERA_SCAN, null)
            }
            R.id.btn_scan -> { // 调用摄像头扫描（物料）
                if(stock2 == null) {
                    Comm.showWarnDialog(mContext,"请扫描移出位置条码或选择！")
                    return
                }
                smqType = 2
                showForResult(CaptureActivity::class.java, BaseFragment.CAMERA_SCAN, null)
            }
            R.id.btn_positionScan -> { // 调用摄像头扫描（移入位置）
                smqType = 3
                showForResult(CaptureActivity::class.java, BaseFragment.CAMERA_SCAN, null)
            }
            R.id.tv_num -> { // 数量
                showInputDialog("数量", getValues(tv_num), "0.0", RESULT_NUM)
            }
            R.id.btn_save -> { // 保存
                if(stock2 == null) {
                    Comm.showWarnDialog(mContext,"请扫描移出位置条码或选择！")
                    return
                }
                if(icItem == null) {
                    Comm.showWarnDialog(mContext,"请扫描物料条码或选择！")
                    return
                }
                if(stock == null) {
                    Comm.showWarnDialog(mContext,"请扫描移入位置条码或选择！")
                    return
                }
                if(stockQty == 0.0) {
                    Comm.showWarnDialog(mContext,"库存数为0！")
                    return
                }
                if(stockQty > 0 && parseDouble(getValues(tv_num)) > stockQty) {
                    Comm.showWarnDialog(mContext,"请扫描移出数量不能大于库存数！")
                    return
                }
                val strJson = JsonUtil.objectToString(setICStockEntry())
                run_save(strJson)
            }
            R.id.btn_clone -> { // 重置
                reset(0)
            }
        }
    }

    override fun setListener() {
        val click = View.OnClickListener { v ->
            setFocusable(et_getFocus)
            when (v.id) {
                R.id.et_positionCode2 -> setFocusable(et_positionCode2)
                R.id.et_code -> setFocusable(et_code)
                R.id.et_positionCode -> setFocusable(et_positionCode)
            }
        }
        et_positionCode2!!.setOnClickListener(click)
        et_code!!.setOnClickListener(click)
        et_positionCode!!.setOnClickListener(click)

        // 移出位置---数据变化
        et_positionCode2!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (s.length == 0) return
                if (!isTextChange) {
                    isTextChange = true
                    smqType = 1
                    mHandler.sendEmptyMessageDelayed(SAOMA, 300)
                }
            }
        })
        // 移出位置---长按输入条码
        et_positionCode2!!.setOnLongClickListener {
            smqType = 1
            showInputDialog("输入容器条码", getValues(et_positionCode2), "none", WRITE_CODE)
            true
        }

        // 物料---数据变化
        et_code!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (s.length == 0) return
                if (!isTextChange) {
                    isTextChange = true
                    smqType = 2
                    mHandler.sendEmptyMessageDelayed(SAOMA, 300)
                }
            }
        })
        // 物料---长按输入条码
        et_code!!.setOnLongClickListener {
            smqType = 2
            showInputDialog("输入容器条码", getValues(et_code), "none", WRITE_CODE)
            true
        }

        // 移入位置---数据变化
        et_positionCode!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (s.length == 0) return
                if (!isTextChange) {
                    isTextChange = true
                    smqType = 3
                    mHandler.sendEmptyMessageDelayed(SAOMA, 300)
                }
            }
        })
        // 移入位置---长按输入条码
        et_positionCode!!.setOnLongClickListener {
            smqType = 3
            showInputDialog("输入位置条码", getValues(et_positionCode), "none", WRITE_CODE)
            true
        }
    }

    /**
     * 0：表示点击重置，1：表示保存后重置
     */
    private fun reset(flag :Int) {
        if(flag == 0) {
            tv_stockName.visibility = View.VISIBLE
            tv_stockAreaName.visibility = View.VISIBLE
            tv_storageRackName.visibility = View.VISIBLE
            tv_stockPosName.visibility = View.VISIBLE

            tv_stockName2.visibility = View.VISIBLE
            tv_stockAreaName2.visibility = View.VISIBLE
            tv_storageRackName2.visibility = View.VISIBLE
            tv_stockPosName2.visibility = View.VISIBLE

            tv_stockName.text = "仓库："
            tv_stockAreaName.text = "库区："
            tv_storageRackName.text = "货架："
            tv_stockPosName.text = "库位："

            tv_stockName2.text = "仓库："
            tv_stockAreaName2.text = "库区："
            tv_storageRackName2.text = "货架："
            tv_stockPosName2.text = "库位："

            stock = null
            stockArea = null
            storageRack = null
            stockPos = null

            stock2 = null
            stockArea2 = null
            storageRack2 = null
            stockPos2 = null

            smqType = 1
        }
        tv_mtlName.text = "名称："
        tv_mtlNumber.text = "代码："
        tv_fmodel.text = "规格："
        tv_stockQty.text = "库存数：0"
        tv_num.text = ""
        icItem = null

        stockQty = 0.0
        mHandler.sendEmptyMessageDelayed(SETFOCUS, 200)
    }

    /**
     * 设置保存的数据
     */
    private fun setICStockEntry() : ICStockBillEntry {
        val icstockBill = ICStockBill()
        icstockBill.billType = "WLYK" // 物料移库
        icstockBill.ftranType = 1
        icstockBill.frob = 1
        icstockBill.fselTranType = 0
        icstockBill.fcustId = 0
        icstockBill.fdeptId = 0
        icstockBill.fempId = user!!.empId
        icstockBill.yewuMan = user!!.empName
        icstockBill.fsmanagerId = user!!.empId
        icstockBill.baoguanMan = user!!.empName
        icstockBill.fmanagerId = user!!.empId
        icstockBill.fuzheMan = user!!.empName
        icstockBill.ffmanagerId = user!!.empId
        icstockBill.yanshouMan = user!!.empName
        icstockBill.fbillerId = user!!.erpUserId
        icstockBill.createUserId = user!!.id
        icstockBill.createUserName = user!!.username

        val entry = ICStockBillEntry()
        entry.icstockBill = icstockBill
        entry.icstockBillId = 0
        entry.fitemId = icItem!!.fitemid
        entry.fbatchNo = icItem!!.batchCode
//            entry.fentryId = it.fentryid
        entry.fdcStockId = stock!!.fitemId
        entry.stockId_wms = stock!!.id
        if(stockArea != null) entry.stockAreaId_wms = stockArea!!.id
        if(storageRack != null) entry.storageRackId_wms = storageRack!!.id
        if(stockPos != null) {
            entry.fdcSPId = stockPos!!.fitemId
            entry.stockPosId_wms = stockPos!!.id
        }
        entry.stock = stock
        entry.stockArea = stockArea
        entry.storageRack = storageRack
        entry.stockPos = stockPos

        entry.fscStockId = stock2!!.fitemId
        entry.stockId2_wms = stock2!!.id
        if(stockArea2 != null) entry.stockAreaId2_wms = stockArea2!!.id
        if(storageRack2 != null) entry.storageRackId2_wms = storageRack2!!.id
        if(stockPos2 != null) {
            entry.fscSPId = stockPos2!!.fitemId
            entry.stockPosId2_wms = stockPos2!!.id
        }
        entry.stock2 = stock2
        entry.stockArea2 = stockArea2
        entry.storageRack2 = storageRack2
        entry.stockPos2 = stockPos2

        entry.fqty = parseDouble(getValues(tv_num))
        entry.allotQty = parseDouble(getValues(tv_num))
        entry.fprice = 0.0
        entry.funitId = icItem!!.unit.funitId
        entry.fsourceInterId = 0
        entry.fsourceEntryId = 0
        entry.fsourceQty = stockQty
        entry.fsourceTranType = 0
        entry.fsourceBillNo = ""
        entry.fdetailId = 0

        entry.icItem = icItem
//      entry.unit = it.icItem.unit

        entry.remark = ""

        return entry
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            SEL_STOCK -> { // 移入位置	返回
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
                    val inventoryNow = data!!.getSerializableExtra("obj") as InventoryNow
                    getMtlAfter(inventoryNow)
                }
            }
            SEL_STOCK2 -> {// 移出位置	返回
                if (resultCode == Activity.RESULT_OK) {
                    resetStockGroup2()
                    stock2 = data!!.getSerializableExtra("stock") as Stock
                    if(data!!.getSerializableExtra("stockArea") != null) {
                        stockArea2 = data!!.getSerializableExtra("stockArea") as StockArea
                    }
                    if(data!!.getSerializableExtra("storageRack") != null) {
                        storageRack2 = data!!.getSerializableExtra("storageRack") as StorageRack
                    }
                    if(data!!.getSerializableExtra("stockPos") != null) {
                        stockPos2 = data!!.getSerializableExtra("stockPos") as StockPosition
                    }
                    getStockGroup2(null)
                }
            }
            BaseFragment.CAMERA_SCAN -> {// 扫一扫成功  返回
                if (resultCode == Activity.RESULT_OK) {
                    val bundle = data!!.extras
                    if (bundle != null) {
                        val code = bundle.getString(BaseFragment.DECODED_CONTENT_KEY, "")
                        when (smqType) {
                            1 -> setTexts(et_positionCode2, code)
                            2 -> setTexts(et_code, code)
                            3 -> setTexts(et_positionCode, code)
                        }
                    }
                }
            }
            WRITE_CODE -> {// 输入条码  返回
                if (resultCode == Activity.RESULT_OK) {
                    val bundle = data!!.extras
                    if (bundle != null) {
                        val value = bundle.getString("resultValue", "")
                        when (smqType) {
                            1 -> setTexts(et_positionCode2, value.toUpperCase())
                            2 -> setTexts(et_code, value.toUpperCase())
                            3 -> setTexts(et_positionCode, value.toUpperCase())
                        }
                    }
                }
            }
            RESULT_NUM -> { // 返回数量
                val bundle = data!!.getExtras()
                if (bundle != null) {
                    val value = bundle.getString("resultValue", "")
                    val num = parseDouble(value)
                    if(num <= 0) {
                        Comm.showWarnDialog(mContext,"移出数量必须大于0！")
                        return
                    }
                    if(num > stockQty) {
                        Comm.showWarnDialog(mContext,"移出数量不能大于库存数！")
                        return
                    }
                    tv_num.text = df.format(num)
                }
            }
        }
        mHandler.sendEmptyMessageDelayed(SETFOCUS, 200)
    }

    fun resetStockGroup() {
        stock = null
        stockArea = null
        storageRack = null
        stockPos = null
    }

    fun resetStockGroup2() {
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
        tv_stockAreaName.visibility = View.GONE
        tv_storageRackName.visibility = View.GONE
        tv_stockPosName.visibility = View.GONE

        if(msgObj != null) {
            resetStockGroup()

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
                }
                2 -> {
                    stockArea = JsonUtil.strToObject(msgObj, StockArea::class.java)
                    if(stockArea!!.stock != null) stock = stockArea!!.stock
                }
                3 -> {
                    storageRack = JsonUtil.strToObject(msgObj, StorageRack::class.java)
                    if(storageRack!!.stock != null) stock = storageRack!!.stock
                    if(storageRack!!.stockArea != null)  stockArea = storageRack!!.stockArea
                }
                4 -> {
                    stockPos = JsonUtil.strToObject(msgObj, StockPosition::class.java)
                    if(stockPos!!.stock != null) stock = stockPos!!.stock
                    if(stockPos!!.stockArea != null)  stockArea = stockPos!!.stockArea
                    if(stockPos!!.storageRack != null)  storageRack = stockPos!!.storageRack
                }
            }
        }

        if(stock != null ) {
            tv_stockName.text = Html.fromHtml("仓库：<font color='#6a5acd'>"+stock!!.stockName+"</font>")
        }
        if(stockArea != null ) {
            tv_stockAreaName.visibility = View.VISIBLE
            tv_stockAreaName.text = Html.fromHtml("库区：<font color='#6a5acd'>"+stockArea!!.fname+"</font>")
        }
        if(storageRack != null ) {
            tv_storageRackName.visibility = View.VISIBLE
            tv_storageRackName.text = Html.fromHtml("货架：<font color='#6a5acd'>"+storageRack!!.fnumber+"</font>")
        }
        if(stockPos != null ) {
            tv_stockPosName.visibility = View.VISIBLE
            tv_stockPosName.text = Html.fromHtml("库位：<font color='#6a5acd'>"+stockPos!!.stockPositionName+"</font>")
        }

    }

    /**
     * 得到调出仓库组
     */
    fun getStockGroup2(msgObj : String?) {
        tv_stockName2.text = "仓库："
        tv_stockAreaName2.text = "库区："
        tv_storageRackName2.text = "货架："
        tv_stockPosName2.text = "库位："
        tv_stockAreaName2.visibility = View.GONE
        tv_storageRackName2.visibility = View.GONE
        tv_stockPosName2.visibility = View.GONE

        if(msgObj != null) {
            resetStockGroup2()

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
                    stock2 = JsonUtil.strToObject(msgObj, Stock::class.java)
                }
                2 -> {
                    stockArea2 = JsonUtil.strToObject(msgObj, StockArea::class.java)
                    if(stockArea2!!.stock != null) stock2 = stockArea2!!.stock
                }
                3 -> {
                    storageRack2 = JsonUtil.strToObject(msgObj, StorageRack::class.java)
                    if(storageRack2!!.stock != null) stock2 = storageRack2!!.stock
                    if(storageRack2!!.stockArea != null)  stockArea2 = storageRack2!!.stockArea
                }
                4 -> {
                    stockPos2 = JsonUtil.strToObject(msgObj, StockPosition::class.java)
                    if(stockPos2!!.stock != null) stock2 = stockPos2!!.stock
                    if(stockPos2!!.stockArea != null)  stockArea2 = stockPos2!!.stockArea
                    if(stockPos2!!.storageRack != null)  storageRack2 = stockPos2!!.storageRack
                }
            }
        }

        if(stock2 != null ) {
            tv_stockName2.text = Html.fromHtml("仓库：<font color='#6a5acd'>"+stock2!!.stockName+"</font>")
        }
        if(stockArea2 != null ) {
            tv_stockAreaName2.visibility = View.VISIBLE
            tv_stockAreaName2.text = Html.fromHtml("库区：<font color='#6a5acd'>"+stockArea2!!.fname+"</font>")
        }
        if(storageRack2 != null ) {
            tv_storageRackName2.visibility = View.VISIBLE
            tv_storageRackName2.text = Html.fromHtml("货架：<font color='#6a5acd'>"+storageRack2!!.fnumber+"</font>")
        }
        if(stockPos2 != null ) {
            tv_stockPosName2.visibility = View.VISIBLE
            tv_stockPosName2.text = Html.fromHtml("库位：<font color='#6a5acd'>"+stockPos2!!.stockPositionName+"</font>")
        }
        if(stock2 != null) {
            // 自动跳到物料焦点
            smqType = 2
            mHandler.sendEmptyMessage(SETFOCUS)
        }
    }

    private fun getMtlAfter(m :InventoryNow) {
        tv_mtlName.text = Html.fromHtml("名称：<font color='#FF4400'>"+m.icItem.fname+"</font>")
        tv_mtlNumber.text = Html.fromHtml("代码：<font color='#6a5acd'>"+m.icItem.fnumber+"</font>")
        tv_fmodel.text = Html.fromHtml("规格：<font color='#6a5acd'>"+m.icItem.fmodel+"</font>")
        tv_stockQty.text = Html.fromHtml((if(isNULLS(m.batchCode).length > 0) "批次：<font color='#6a5acd'>"+isNULLS(m.batchCode)+"</font>&nbsp;&nbsp;&nbsp;&nbsp;" else "") +"库存数：<font color='#FF2200'>"+df.format(m.avbQty)+"</font>")
        tv_num.text = df.format(m.avbQty)
        stockQty = m.avbQty

        icItem = m.icItem
        icItem!!.unit = m.unit
        icItem!!.batchCode = m.batchCode
        icItem!!.snCode = m.snCode
    }

    /**
     * 扫码查询对应的方法
     */
    private fun run_smDatas() {
        isTextChange = false
        showLoadDialog("加载中...", false)
        var mUrl:String? = null
        var barcode:String? = null
        when(smqType) {
            1 -> {
                mUrl = getURL("stockPosition/findBarcodeGroup")
                barcode = getValues(et_positionCode2)
            }
            2 -> {
                mUrl = getURL("icItem/findBarcode2")
                barcode = getValues(et_code)
            }
            3 -> {
                mUrl = getURL("stockPosition/findBarcodeGroup")
                barcode = getValues(et_positionCode)
            }
        }
        val formBody = FormBody.Builder()
                .add("barcode", barcode)
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
    private fun run_save(strJson :String) {
        showLoadDialog("保存中...", false)
        var mUrl = getURL("stockBill_WMS/addByMtlMoveSotck")
        val formBody = FormBody.Builder()
                .add("strJson", strJson)
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
     * 得到用户对象
     */
    private fun getUserInfo() {
        if (user == null) user = showUserByXml()
    }

    override fun onDestroyView() {
        closeHandler(mHandler)
        mBinder!!.unbind()
        super.onDestroyView()
    }
}