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
import kotlinx.android.synthetic.main.ware_tool_move_fragment2.*
import okhttp3.*
import ykk.xc.com.wms.R
import ykk.xc.com.wms.basics.Container_DialogActivity
import ykk.xc.com.wms.basics.Emp_DialogActivity
import ykk.xc.com.wms.basics.Stock_GroupDialogActivity
import ykk.xc.com.wms.bean.*
import ykk.xc.com.wms.bean.k3Bean.Emp
import ykk.xc.com.wms.comm.BaseFragment
import ykk.xc.com.wms.comm.Comm
import ykk.xc.com.wms.util.JsonUtil
import ykk.xc.com.wms.util.LogUtil
import ykk.xc.com.wms.util.zxing.android.CaptureActivity
import java.io.IOException
import java.lang.ref.WeakReference
import java.util.concurrent.TimeUnit

/**
 * 日期：2019-10-16 09:50
 * 描述：其他出库
 * 作者：ykk
 */
class ToolMove_Fragment2 : BaseFragment() {

    companion object {
        private val SEL_STOCK = 11
        private val SEL_CONTAINER = 12
        private val RESULT_PURPOSE = 13
        private val SEL_EMP = 13
        private val SUCC1 = 200
        private val UNSUCC1 = 500
        private val SAVE = 202
        private val UNSAVE = 502

        private val SETFOCUS = 1
        private val SAOMA = 2
        private val WRITE_CODE = 3
    }
    private val context = this
    private var okHttpClient: OkHttpClient? = null
    private var user: User? = null
    private var stock:Stock? = null
    private var stockArea:StockArea? = null
    private var storageRack:StorageRack? = null
    private var stockPos:StockPosition? = null
    private var mContext: Activity? = null
    private var parent: ToolMove_MainActivity? = null
    private var isTextChange: Boolean = false // 是否进入TextChange事件
    private var record = PlantMould_UseRecord()
    private var smqType = 1 // 扫描类型1：移入容器扫描，2：移出位置扫描

    // 消息处理
    private val mHandler = MyHandler(this)
    private class MyHandler(activity: ToolMove_Fragment2) : Handler() {
        private val mActivity: WeakReference<ToolMove_Fragment2>

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
                            1 -> { // 容器
                                val plantMould = JsonUtil.strToObject(msgObj, PlantMould::class.java)
                                m.getStockGroup(plantMould)
                            }
                            2 -> { // 库位
                                m.resetStockGroup()
                                m.getStockGroup2(msgObj)
                            }
                        }
                    }
                    UNSUCC1 -> { // 扫码失败
                        errMsg = JsonUtil.strToString(msgObj)
                        if (m.isNULLS(errMsg).length == 0) errMsg = "很抱歉，没有找到数据！"
                        Comm.showWarnDialog(m.mContext, errMsg)
                    }
                    SAVE -> { // 保存成功 进入
                        m.reset()
                        m.toasts("保存成功✔")
                    }
                    UNSAVE -> { // 保存失败
                        errMsg = JsonUtil.strToString(msgObj)
                        if (m.isNULLS(errMsg).length == 0) errMsg = "保存失败！"
                        Comm.showWarnDialog(m.mContext, errMsg)
                    }
                    SETFOCUS -> { // 当弹出其他窗口会抢夺焦点，需要跳转下，才能正常得到值
                        m.setFocusable(m.et_getFocus)
                        when(m.smqType) {
                            1 -> m.setFocusable(m.et_positionCode)
                            2 -> m.setFocusable(m.et_positionCode2)
                            3 -> m.setFocusable(m.et_positionCode3)
                        }
                    }
                    SAOMA -> { // 扫码之后
                        // 执行查询方法
                        m.run_smDatas()
                    }
                }
            }
        }
    }

    override fun setLayoutResID(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.ware_tool_move_fragment2, container, false)
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
        hideSoftInputMode(mContext, et_positionCode)
        hideSoftInputMode(mContext, et_positionCode2)

        record.sourceType = '2' // 来源数据类型	1：容器，2：工装模具
        record.useType = 'L' // 使用类型	L：领用，H：归还
        record.useMan = user!!.empName

        tv_useBegDate.text = Comm.getSysDate(7)
        tv_useEndDate.text = Comm.getSysDate(7)
        tv_useMan.text = user!!.username
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            mHandler.sendEmptyMessageDelayed(SETFOCUS, 200)
        }
    }

    @OnClick( R.id.btn_positionScan, R.id.btn_positionSel, R.id.btn_positionScan2, R.id.btn_positionSel2, R.id.btn_positionScan3,
              R.id.radio_use, R.id.radio_return, R.id.tv_useBegDate, R.id.tv_useEndDate, R.id.tv_useMan, R.id.tv_purpose,
              R.id.btn_clone, R.id.btn_save )
    fun onViewClicked(view: View) {
        when (view.id) {
            R.id.btn_positionSel -> { // 选择容器
                smqType = 1
                showForResult(Container_DialogActivity::class.java, SEL_CONTAINER, null)
            }
            R.id.btn_positionSel2 -> { // 选择位置
                smqType = 2
                val bundle = Bundle()
                bundle.putSerializable("stock", stock)
                bundle.putSerializable("stockArea", stockArea)
                bundle.putSerializable("storageRack", storageRack)
                bundle.putSerializable("stockPos", stockPos)
                showForResult(Stock_GroupDialogActivity::class.java, SEL_STOCK, bundle)
            }
            R.id.btn_positionScan -> { // 调用摄像头扫描（容器）
                smqType = 1
                showForResult(CaptureActivity::class.java, BaseFragment.CAMERA_SCAN, null)
            }
            R.id.btn_positionScan2 -> { // 调用摄像头扫描（位置）
                smqType = 2
                showForResult(CaptureActivity::class.java, BaseFragment.CAMERA_SCAN, null)
            }
            R.id.btn_positionScan3 -> { // 调用摄像头扫描（工单号）
                smqType = 3
                showForResult(CaptureActivity::class.java, BaseFragment.CAMERA_SCAN, null)
            }
            R.id.radio_use -> {// 领用
                record.useType = 'L'
            }
            R.id.radio_return -> {// 归还
                record.useType = 'H'
            }
            R.id.tv_useMan -> { // 使用人
                showForResult(Emp_DialogActivity::class.java, SEL_EMP, null)
            }
            R.id.tv_useBegDate -> { // 使用日期
                Comm.showDateDialog(mContext, view, 0)
            }
            R.id.tv_useEndDate -> { // 结束日期
                Comm.showDateDialog(mContext, view, 0)
            }
            R.id.tv_purpose -> { // 用途
                showInputDialog("备注", record.purpose, "none", RESULT_PURPOSE)
            }
            R.id.btn_save -> { // 保存
                if(record.parentId == 0) {
                    Comm.showWarnDialog(mContext,"请扫描物料条码！")
                    return
                }
                if(record.useStockId == 0) {
                    Comm.showWarnDialog(mContext,"请扫描位置条码或选择！")
                    return
                }
                record.useBegDate = getValues(tv_useBegDate)
                record.useEndDate = getValues(tv_useEndDate)
                val strJson = JsonUtil.objectToString(record)
                run_save(strJson);
            }
            R.id.btn_clone -> { // 重置
                reset()
            }
        }
    }

    override fun setListener() {
        val click = View.OnClickListener { v ->
            setFocusable(et_getFocus)
            when (v.id) {
                R.id.et_positionCode -> setFocusable(et_positionCode)
                R.id.et_positionCode2 -> setFocusable(et_positionCode2)
            }
        }
        et_positionCode!!.setOnClickListener(click)
        et_positionCode2!!.setOnClickListener(click)

        // 移出位置---数据变化
        et_positionCode!!.addTextChangedListener(object : TextWatcher {
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
        et_positionCode!!.setOnLongClickListener {
            smqType = 1
            showInputDialog("输入位置条码", getValues(et_positionCode), "none", WRITE_CODE)
            true
        }

        // 移入位置---数据变化
        et_positionCode2!!.addTextChangedListener(object : TextWatcher {
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
        // 移入位置---长按输入条码
        et_positionCode2!!.setOnLongClickListener {
            smqType = 2
            showInputDialog("输入位置条码", getValues(et_positionCode2), "none", WRITE_CODE)
            true
        }

        // 用途---长按输入
        et_positionCode3!!.setOnLongClickListener {
            smqType = 3
            showInputDialog("输入用途", getValues(et_positionCode2), "none", WRITE_CODE)
            true
        }
    }

    /**
     * 0：表示点击重置，1：表示保存后重置
     */
    private fun reset() {
        tv_stockName.visibility = View.VISIBLE
        tv_stockAreaName.visibility = View.VISIBLE
        tv_storageRackName.visibility = View.VISIBLE
        tv_stockPosName.visibility = View.VISIBLE
        tv_containerName.visibility = View.VISIBLE
        tv_stockName2.visibility = View.VISIBLE
        tv_stockAreaName2.visibility = View.VISIBLE
        tv_storageRackName2.visibility = View.VISIBLE
        tv_stockPosName2.visibility = View.VISIBLE

        tv_mouldName.text = "模具："
        tv_stockName.text = "仓库："
        tv_stockAreaName.text = "库区："
        tv_storageRackName.text = "货架："
        tv_stockPosName.text = "库位："
        tv_containerName.text = "容器："
        tv_stockName2.text = "仓库："
        tv_stockAreaName2.text = "库区："
        tv_storageRackName2.text = "货架："
        tv_stockPosName2.text = "库位："
        radio_return.isChecked = false
        radio_use.isChecked = true
        tv_useBegDate.text = Comm.getSysDate(7)
        tv_useEndDate.text = Comm.getSysDate(7)
        tv_useMan.text = user!!.empName
        et_positionCode.setText("")
        et_positionCode2.setText("")
        et_positionCode3.setText("")
        tv_purpose.text = ""

        stock = null
        stockArea = null
        storageRack = null
        stockPos = null
        record.useType = 'L'
        record.sourceNo = ""
        record.purpose = ""
        record.useStockId = 0
        record.useStockAreaId = 0
        record.useStorageRackId = 0
        record.useStockPosId = 0
        record.useContainerId = 0
        record.returnStockId = 0
        record.returnStockAreaId = 0
        record.returnStorageRackId = 0
        record.returnStockPosId = 0
        record.returnContainerId = 0
//        parent!!.isChange = false
        smqType = 1
        mHandler.sendEmptyMessageDelayed(SETFOCUS, 200)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            SEL_CONTAINER -> { //查询容器	返回
//                if (resultCode == Activity.RESULT_OK) {
//                    val m = data!!.getSerializableExtra("obj") as Container
//                    getStockGroup(m)
//                }
            }
            SEL_STOCK -> {// 仓库	返回
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
                    getStockGroup2(null)
                }
            }
            BaseFragment.CAMERA_SCAN -> {// 扫一扫成功  返回
                if (resultCode == Activity.RESULT_OK) {
                    val bundle = data!!.extras
                    if (bundle != null) {
                        val code = bundle.getString(BaseFragment.DECODED_CONTENT_KEY, "")
                        when (smqType) {
                            1 -> setTexts(et_positionCode, code)
                            2 -> setTexts(et_positionCode2, code)
                            3 -> setTexts(et_positionCode3, code)
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
                            1 -> setTexts(et_positionCode, value.toUpperCase())
                            2 -> setTexts(et_positionCode2, value.toUpperCase())
                            3 -> setTexts(et_positionCode3, value)
                        }
                    }
                }
            }
            SEL_EMP -> {// 查询使用人	返回
                if (resultCode == Activity.RESULT_OK) {
                    val emp = data!!.getSerializableExtra("obj") as Emp
                    tv_useMan.text = emp!!.fname
                    record.useMan = emp!!.fname
                }
            }
            RESULT_PURPOSE -> { // 用途	返回
                if (resultCode == Activity.RESULT_OK) {
                    val bundle = data!!.getExtras()
                    if (bundle != null) {
                        val value = bundle.getString("resultValue", "")
                        tv_purpose.text = value
                        record.purpose = value
                    }
                }
            }
        }
    }

    /**
     * 得到扫码或选择数据
     */
    private fun getStockGroup(m : PlantMould) {
        tv_stockName.text = "仓库："
        tv_stockAreaName.text = "库区："
        tv_storageRackName.text = "货架："
        tv_stockPosName.text = "库位："
        tv_containerName.text = "容器："
        tv_stockName.visibility = View.GONE
        tv_stockAreaName.visibility = View.GONE
        tv_storageRackName.visibility = View.GONE
        tv_stockPosName.visibility = View.GONE
        tv_containerName.visibility = View.GONE

        record.parentId = m.id

        // 重置数据
        record.returnStockId = 0
        record.returnStockAreaId = 0
        record.returnStorageRackId = 0
        record.returnStockPosId = 0
        record.returnContainerId = 0

        if(m.stock != null ) {
            tv_stockName.text = Html.fromHtml("仓库：<font color='#6a5acd'>"+m.stock.stockName+"</font>")
            tv_stockName.visibility = View.VISIBLE
            record.returnStockId = m.stock.id
        }
        if(m.stockArea != null ) {
            tv_stockAreaName.text = Html.fromHtml("库区：<font color='#6a5acd'>"+m.stockArea.fname+"</font>")
            tv_stockAreaName.visibility = View.VISIBLE
            record.returnStockAreaId = m.stockArea.id
        }
        if(m.storageRack != null ) {
            tv_storageRackName.text = Html.fromHtml("货架：<font color='#6a5acd'>"+m.storageRack.fnumber+"</font>")
            tv_storageRackName.visibility = View.VISIBLE
            record.returnStorageRackId = m.storageRack.id
        }
        if(m.stockPos != null ) {
            tv_stockPosName.text = Html.fromHtml("库位：<font color='#6a5acd'>"+m.stockPos.stockPositionName+"</font>")
            tv_stockPosName.visibility = View.VISIBLE
            record.returnStockPosId = m.stockPos.id
        }
        if(m.container != null ) {
            tv_containerName.text = Html.fromHtml("容器：<font color='#6a5acd'>"+m.container.fnumber+"</font>")
            tv_containerName.visibility = View.VISIBLE
            record.returnContainerId = m.container.id
        }
        tv_mouldName.text = Html.fromHtml("模具：<font color='#FF4400'>"+m.fname+"</font>")

        if(stock != null) {
            // 自动跳到物料焦点
            smqType = 2
            mHandler.sendEmptyMessage(SETFOCUS)
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
    fun getStockGroup2(msgObj : String?) {
        tv_stockName2.text = "仓库："
        tv_stockAreaName2.text = "库区："
        tv_storageRackName2.text = "货架："
        tv_stockPosName2.text = "库位："
        tv_stockAreaName2.visibility = View.GONE
        tv_storageRackName2.visibility = View.GONE
        tv_stockPosName2.visibility = View.GONE
        // 重置数据
        record.useStockId = 0
        record.useStockAreaId = 0
        record.useStorageRackId = 0
        record.useStockPosId = 0
//        record.useContainerId = 0

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
            tv_stockName2.text = Html.fromHtml("仓库：<font color='#6a5acd'>"+stock!!.stockName+"</font>")
            record.useStockId = stock!!.id
        }
        if(stockArea != null ) {
            tv_stockAreaName2.visibility = View.VISIBLE
            tv_stockAreaName2.text = Html.fromHtml("库区：<font color='#6a5acd'>"+stockArea!!.fname+"</font>")
            record.useStockAreaId = stockArea!!.id
        }
        if(storageRack != null ) {
            tv_storageRackName2.visibility = View.VISIBLE
            tv_storageRackName2.text = Html.fromHtml("货架：<font color='#6a5acd'>"+storageRack!!.fnumber+"</font>")
            record.useStorageRackId = storageRack!!.id
        }
        if(stockPos != null ) {
            tv_stockPosName2.visibility = View.VISIBLE
            tv_stockPosName2.text = Html.fromHtml("库位：<font color='#6a5acd'>"+stockPos!!.stockPositionName+"</font>")
            record.useStockPosId = stockPos!!.id
        }
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
                mUrl = getURL("plantMould/findBarcode")
                barcode = getValues(et_positionCode)
            }
            2 -> {
                mUrl = getURL("stockPosition/findBarcodeGroup")
                barcode = getValues(et_positionCode2)
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
    private fun run_save(strJson : String) {
        showLoadDialog("保存中...", false)
        var mUrl = getURL("plantMould_UseRecord/add")
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