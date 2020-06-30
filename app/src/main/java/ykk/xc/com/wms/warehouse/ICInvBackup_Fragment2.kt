package ykk.xc.com.wms.warehouse

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.view.*
import butterknife.OnClick
import kotlinx.android.synthetic.main.ware_icinvbackup_fragment2.*
import okhttp3.*
import ykk.xc.com.wms.R
import ykk.xc.com.wms.basics.Container_DialogActivity
import ykk.xc.com.wms.basics.Mtl_MoreDialogActivity
import ykk.xc.com.wms.basics.Stock_GroupDialogActivity
import ykk.xc.com.wms.bean.*
import ykk.xc.com.wms.bean.k3Bean.ICInvBackup
import ykk.xc.com.wms.bean.k3Bean.ICItem
import ykk.xc.com.wms.bean.k3Bean.ICStockCheckProcess
import ykk.xc.com.wms.comm.BaseFragment
import ykk.xc.com.wms.comm.Comm
import ykk.xc.com.wms.util.BigdecimalUtil
import ykk.xc.com.wms.util.JsonUtil
import ykk.xc.com.wms.util.LogUtil
import ykk.xc.com.wms.util.basehelper.BaseRecyclerAdapter
import ykk.xc.com.wms.util.zxing.android.CaptureActivity
import ykk.xc.com.wms.warehouse.adapter.ICInvBackup_Fragment2_Adapter
import java.io.IOException
import java.lang.ref.WeakReference
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * 日期：2019-10-16 09:50
 * 描述：WMS 盘点（无盘点方案）
 * 作者：ykk
 */
class ICInvBackup_Fragment2 : BaseFragment() {

    companion object {
        private val SEL_STOCK = 10
        private val SEL_MTL = 11
        private val SEL_CONTAINER = 12

        private val SUCC1 = 200
        private val UNSUCC1 = 500
        private val SUCC2 = 201
        private val UNSUCC2 = 501
        private val SUBMIT = 202
        private val UNSUBMIT = 502
        private val SAVE = 203
        private val UNSAVE = 503
        private val FIND_AVGQTY = 204
        private val UNFIND_AVGQTY = 504
        private val SETFOCUS = 1
        private val RESULT_NUM = 2
        private val RESULT_BATCH = 3
        private val RESULT_WEIGHT = 4
        private val RESULT_MINPACK = 5
        private val SAOMA = 6
        private val WRITE_CODE = 7
    }

    private val context = this
    private var mAdapter: ICInvBackup_Fragment2_Adapter? = null
    private val checkDatas = ArrayList<ICItem>()
    private var okHttpClient: OkHttpClient? = null
    private var user: User? = null
    private var mContext: Activity? = null
    private var parent: ICInvBackup_MainActivity? = null
    private var stock:Stock? = null
    private var stockArea: StockArea? = null
    private var storageRack: StorageRack? = null
    private var stockPos: StockPosition? = null
    private var container: Container? = null

    private var isTextChange: Boolean = false // 是否进入TextChange事件
    private var icStockCheckProcess: ICStockCheckProcess? = null
    private var curPos:Int = 0 // 当前行
    private var smqFlag = '1' // 使用扫码枪扫码（1：仓库位置扫码，2：容器扫码，3：物料扫码）
    var mapMtlQty = HashMap<Int, Double>()

    // 消息处理
    private val mHandler = MyHandler(this)

    private class MyHandler(activity: ICInvBackup_Fragment2) : Handler() {
        private val mActivity: WeakReference<ICInvBackup_Fragment2>

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
                            '1' -> { // 仓库位置扫描
                                m.resetStockGroup()
                                m.getStockGroup(msgObj)
                            }
                            '2' -> { // 容器扫描
                                val container = JsonUtil.strToObject(msgObj, Container::class.java)
                                m.getContainer(container)
                            }
                            '3' -> { // 物料扫描
                                val icItem = JsonUtil.strToObject(msgObj, ICItem::class.java)
                                var listIcitem = ArrayList<ICItem>()
                                listIcitem.add(icItem)
                                m.tv_mtlName.text = icItem!!.fnumber

                                m.getMtlAfter(listIcitem, 0)
                            }
                        }
                    }
                    UNSUCC1 -> { // 扫码失败
                        m.mAdapter!!.notifyDataSetChanged()
                        when(m.smqFlag) {
                            '1' -> { // 仓库位置扫描
//                                m.tv_positionName.text = ""
                            }
                            '2' -> { // 容器扫描
//                                m.container = null
//                                m.tv_containerName.text = ""
                            }
                            '3' -> { // 物料扫描
//                                m.tv_mtlName.text = ""
                            }
                        }
                        errMsg = JsonUtil.strToString(msgObj)
                        if (m.isNULLS(errMsg).length == 0) errMsg = "很抱歉，没能找到数据！"
                        Comm.showWarnDialog(m.mContext, errMsg)
                    }
                    SUCC2 -> { // 历史查询 进入
                        m.checkDatas.clear()
//                        val icInvBackup = JsonUtil.strToList(msgObj, ICInvBackup::class.java)
//                        m.checkDatas.addAll(icInvBackup)
//                        m.mAdapter!!.notifyDataSetChanged()
                    }
                    UNSUCC2 -> { // 历史查询  失败
                        m.mAdapter!!.notifyDataSetChanged()
                        errMsg = JsonUtil.strToString(msgObj)
                        if (m.isNULLS(errMsg).length == 0) errMsg = "很抱歉，没能找到数据！"
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
                    FIND_AVGQTY -> { // 查询库存    成功
                        val listAvbQty = JsonUtil.strToList(msgObj, InventoryNow::class.java)
                        m.mapMtlQty.clear()
                        listAvbQty.forEach {
                            m.mapMtlQty.put(it.icItemId, it.avbQty)
                        }
                    }
                    UNFIND_AVGQTY -> { // 查询库存  失败
                        m.mapMtlQty.clear()
                    }
                    SUBMIT -> { // 提交成功 返回
                        m.reset()
                        m.toasts("提交成功✔")
                    }
                    UNSUBMIT // 提交失败 返回
                    -> {
                        errMsg = JsonUtil.strToString(msgObj!!)
                        if (m.isNULLS(errMsg).length == 0) errMsg = "服务器忙，请稍候再试！"
                        Comm.showWarnDialog(m.mContext, errMsg)
                    }
                    SETFOCUS -> { // 当弹出其他窗口会抢夺焦点，需要跳转下，才能正常得到值
                        m.setFocusable(m.et_getFocus)
                        when(m.smqFlag) {
                            '1' -> m.setFocusable(m.et_positionCode)
                            '2' -> m.setFocusable(m.et_containerCode)
                            '3' -> m.setFocusable(m.et_code)
                        }
                    }
                    SAOMA -> { // 扫码之后
                        if(!m.checkSaoMa()) {
                            m.isTextChange = false
                            return
                        }
                        // 执行查询方法
                        m.run_okhttpDatas()
                    }
                }
            }
        }
    }

    override fun setLayoutResID(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.ware_icinvbackup_fragment2, container, false)
    }

    override fun initView() {
        mContext = getActivity()
        parent = mContext as ICInvBackup_MainActivity

        recyclerView.addItemDecoration(DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL))
        recyclerView.layoutManager = LinearLayoutManager(mContext)
        mAdapter = ICInvBackup_Fragment2_Adapter(mContext!!, checkDatas)
        recyclerView.adapter = mAdapter
        // 设值listview空间失去焦点
        recyclerView.isFocusable = false

        // 行事件
        mAdapter!!.setCallBack(object : ICInvBackup_Fragment2_Adapter.MyCallBack {
            override fun onClick_weight(entity: ICItem, position: Int) {
                curPos = position
                showInputDialog("重量", entity.weight.toString(), "0.0", RESULT_WEIGHT)
            }

            override fun onClick_num(entity: ICItem, position: Int) {
                curPos = position
                showInputDialog("数量", entity.realQty.toString(), "0.0", RESULT_NUM)
            }

            override fun onClick_minPackQty(entity: ICItem, position: Int) {
                curPos = position
                showInputDialog("最小包装数", entity.minPackQty.toString(), "0.0", RESULT_MINPACK)
            }

            override fun onClick_batch(entity: ICItem, position: Int) {
                curPos = position
                showInputDialog("批次号", Comm.isNULLS(entity.batchCode), "none", RESULT_BATCH)
            }

            override fun onDelete(entity: ICItem, position: Int) {
                checkDatas.removeAt(position)
                mAdapter!!.notifyDataSetChanged()
            }
        })

        mAdapter!!.onItemClickListener = BaseRecyclerAdapter.OnItemClickListener { adapter, holder, view, pos ->
            checkSaveSon(smqFlag, pos)
        }
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
        hideSoftInputMode(mContext, et_code)
        hideSoftInputMode(mContext, et_containerCode)

    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            mHandler.sendEmptyMessageDelayed(SETFOCUS, 200)
        }
    }

    @OnClick(R.id.btn_positionSel, R.id.btn_positionScan, R.id.btn_containerSel, R.id.btn_containerScan, R.id.btn_mtlSel, R.id.btn_scan,
            R.id.tv_positionName, R.id.tv_containerName, R.id.tv_mtlName,
            R.id.btn_save, R.id.btn_clone, R.id.btn_submit)
    fun onViewClicked(view: View) {
        var bundle: Bundle? = null
        when (view.id) {
            R.id.btn_positionSel -> { // 选择仓库位置
                smqFlag = '1'
                bundle = Bundle()
                bundle.putSerializable("stock", stock)
                bundle.putSerializable("stockArea", stockArea)
                bundle.putSerializable("storageRack", storageRack)
                bundle.putSerializable("stockPos", stockPos)
                showForResult(Stock_GroupDialogActivity::class.java, SEL_STOCK, bundle)
            }
            R.id.btn_containerSel -> { // 选择容器
                smqFlag = '2'
                bundle = Bundle()
//                bundle.putInt("finterId", icStockCheckProcess!!.fid)
                showForResult(Container_DialogActivity::class.java, SEL_CONTAINER, bundle)
            }
            R.id.btn_mtlSel -> { // 选择物料
                smqFlag = '3'
                bundle = Bundle()
                if(!checkSaoMa()) return
//                bundle.putInt("finterId", icStockCheckProcess!!.fid)
                showForResult(Mtl_MoreDialogActivity::class.java, SEL_MTL, bundle)
            }
            R.id.btn_positionScan -> { // 调用摄像头扫描（仓库位置）
//                if(!checkProject()) return
                smqFlag = '1'
                showForResult(CaptureActivity::class.java, BaseFragment.CAMERA_SCAN, null)
            }
            R.id.btn_containerScan -> { // 调用摄像头扫描（容器）
//                if(!checkProject()) return
                smqFlag = '2'
                showForResult(CaptureActivity::class.java, BaseFragment.CAMERA_SCAN, null)
            }
            R.id.btn_scan -> { // 调用摄像头扫描（物料）
                smqFlag = '3'
                if(!checkSaoMa()) return
                showForResult(CaptureActivity::class.java, BaseFragment.CAMERA_SCAN, null)
            }
            R.id.tv_positionName -> { // 位置点击
                smqFlag = '1'
                mHandler.sendEmptyMessageDelayed(SETFOCUS, 200)
            }
            R.id.tv_containerName -> { // 容器点击
                smqFlag = '2'
                mHandler.sendEmptyMessageDelayed(SETFOCUS, 200)
            }
            R.id.tv_mtlName -> { // 物料点击
                smqFlag = '3'
                mHandler.sendEmptyMessageDelayed(SETFOCUS, 200)
            }
            R.id.btn_save -> { // 保存
                val list = checkSave();
                if(list == null) return
                if(checkDatas.size == 0) {
                    Comm.showWarnDialog(mContext,"请选择物料或者扫码条码！")
                    return
                }
                val strJson = JsonUtil.objectToString(list)
                run_save(strJson)
            }
            R.id.btn_submit -> { // 提交到k3
//                if(!checkProject()) return
                run_submitTok3();
            }
            R.id.btn_clone -> { // 重置
                if (parent!!.isChange) {
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

    fun checkSaoMa() : Boolean{
        when(smqFlag) {
            '3' -> { // 物料扫描
                if(stock == null) {
                    Comm.showWarnDialog(mContext,"请扫描或选择位置！")
                    return false
                }
            }
        }
        return true
    }

    /**
     * 检查方案是否有值
     */
    fun checkSave() : List<ICInvBackup>? {
        val list = ArrayList<ICInvBackup>()
        checkDatas.forEachIndexed { index, icItem ->
            if(icItem.stock == null) {
                Comm.showWarnDialog(mContext,"第（"+(index+1)+"）行，请扫描或者选择位置！")
                // 位置自动获取焦点，直接扫描就能匹配数据
                checkSaveSon('1', index)
                recyclerView.post(Runnable { recyclerView.smoothScrollToPosition(index) })
                return null
            }
            if(icItem.useContainer.equals("Y") && icItem.container == null) {
                Comm.showWarnDialog(mContext,"第（"+(index+1)+"）行，请扫描或者选择容器！")
                // 容器自动获取焦点，直接扫描就能匹配数据
                checkSaveSon('2', index)
                recyclerView.post(Runnable { recyclerView.smoothScrollToPosition(index) })
                return null
            }
//            if(icItem.realQty == 0.0) {
//                Comm.showWarnDialog(mContext,"第（"+(index+1)+"）行，请输入盘点数！")
//                return null
//            }
            if(icItem.batchManager.equals("Y") && Comm.isNULLS(icItem.batchCode).length == 0) {
                Comm.showWarnDialog(mContext,"第（"+(index+1)+"）行，请长按数字框输入批次！")
                return null
            }
            val m = ICInvBackup()
            m.finterId = 0
            m.stockId = icItem.stock.fitemId
            m.mtlId = icItem.fitemid
            m.fauxQty = (if(mapMtlQty.containsKey(icItem.fitemid)) mapMtlQty.getValue(icItem.fitemid) else 0.0)
            m.fauxQtyAct = 0.0
            m.fauxCheckQty = 0.0
            m.realQty = icItem.realQty
            m.createUserId = user!!.id
            m.toK3 = 1
            m.stockPosId = if(icItem.stockPos != null) icItem.stockPos.fitemId else 0

            m.stockId_wms = icItem.stock.id
            m.stockAreaId_wms = if(icItem.stockArea != null) icItem.stockArea.id else 0
            m.storageRackId_wms = if(icItem.storageRack != null) icItem.storageRack.id else 0
            m.stockPosId_wms = if(icItem.stockPos != null) icItem.stockPos.id else 0
            m.containerId = if(icItem.container != null) icItem.container.id else 0
            m.weight = icItem.weight
            m.weightUnitType = 2 // // 重量单位类型(1：千克，2：克，3：磅)
            m.minPackQty = icItem.minPackQty
            m.repeatStatus = 0 // 复盘状态
            m.repeatQty = 0.0 // 复盘数

            m.stockName = icItem.stock.stockName
            m.mtlNumber = icItem.fnumber
            m.mtlName = icItem.fname
            m.unitName = icItem.unit.unitName
            m.fmodel = icItem.fmodel
            m.fbatchNo = icItem.batchCode
            m.accountName = "WMS"

            list.add(m)
        }
        return list
    }

    /**
     * 自动填充焦点，和选中行
     */
    fun checkSaveSon(flag : Char, index : Int) {
        // 容器自动获取焦点，直接扫描就能匹配数据
        smqFlag = flag
        curPos = index
        mHandler.sendEmptyMessageDelayed(SETFOCUS, 200)
        // 自动选中行
        checkDatas.forEach {
            it.isCheck = false
        }
        checkDatas[index].isCheck = true
        mAdapter!!.notifyDataSetChanged()
    }

    override fun setListener() {
        val click = View.OnClickListener { v ->
            setFocusable(et_getFocus)
            when (v.id) {
                R.id.et_positionCode -> setFocusable(et_positionCode)
                R.id.et_code -> setFocusable(et_code)
                R.id.et_containerCode -> setFocusable(et_containerCode)
            }
        }
        et_positionCode!!.setOnClickListener(click)
        et_code!!.setOnClickListener(click)
        et_containerCode!!.setOnClickListener(click)

        // 位置---数据变化
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
        // 位置---长按输入条码
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

        // 容器---数据变化
        et_containerCode!!.addTextChangedListener(object : TextWatcher {
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
        // 容器---长按输入条码
        et_containerCode!!.setOnLongClickListener {
            smqFlag = '2'
            showInputDialog("输入条码号", "", "none", WRITE_CODE)
            true
        }
        // 容器---焦点改变
        et_containerCode.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if(hasFocus) {
                lin_focusContainer.setBackgroundResource(R.drawable.back_style_red_focus)

            } else {
                if (lin_focusContainer != null) {
                    lin_focusContainer!!.setBackgroundResource(R.drawable.back_style_gray4)
                }
            }
        }

        // 物料---数据变化
        et_code!!.addTextChangedListener(object : TextWatcher {
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
        et_code!!.setOnLongClickListener {
            smqFlag = '3'
            showInputDialog("输入条码号", "", "none", WRITE_CODE)
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

        // 位置长按显示仓库详细
        tv_positionName.setOnLongClickListener{
            relative_stockGroup.visibility = View.VISIBLE
            mHandler.postDelayed(Runnable {
                relative_stockGroup.visibility = View.GONE
            },5000)
            true
        }
    }

    /**
     * 查询历史记录
     */
    fun findFun() {
        if(parent!!.isChange) {
            Comm.showWarnDialog(mContext,"请先保存当前数据！")
            return;
        }
        run_findListByParamWms()
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
        tv_stockName.text = "仓库："
        tv_stockAreaName.text = "库区："
        tv_storageRackName.text = "货架："
        tv_stockPosName.text = "库位："
        tv_stockAreaName.visibility = View.INVISIBLE
        tv_storageRackName.visibility = View.INVISIBLE
        tv_stockPosName.visibility = View.INVISIBLE

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
            tv_stockName.text = Html.fromHtml("仓库：<font color='#6a5acd'>"+stock!!.stockName+"</font>")
        }
        if(stockArea != null ) {
            tv_positionName.text = stockArea!!.fname
            tv_stockAreaName.visibility = View.VISIBLE
            tv_stockAreaName.text = Html.fromHtml("库区：<font color='#6a5acd'>"+stockArea!!.fname+"</font>")
        }
        if(storageRack != null ) {
            tv_positionName.text = storageRack!!.fnumber
            tv_storageRackName.visibility = View.VISIBLE
            tv_storageRackName.text = Html.fromHtml("货架：<font color='#6a5acd'>"+storageRack!!.fnumber+"</font>")
        }
        if(stockPos != null ) {
            tv_positionName.text = stockPos!!.stockPositionName
            tv_stockPosName.visibility = View.VISIBLE
            tv_stockPosName.text = Html.fromHtml("库位：<font color='#6a5acd'>"+stockPos!!.stockPositionName+"</font>")
        }

        // 人为替换仓库信息
        if(checkDatas.size > 0) {
            checkDatas[curPos].stock = null
            checkDatas[curPos].stockArea = null
            checkDatas[curPos].storageRack = null
            checkDatas[curPos].stockPos = null

            if(stock != null) {
                checkDatas[curPos].stock = stock
            }
            if(stockArea != null) {
                checkDatas[curPos].stockArea = stockArea
            }
            if(storageRack != null) {
                checkDatas[curPos].storageRack = storageRack
            }
            if(stockPos != null) {
                checkDatas[curPos].stockPos = stockPos
            }
            mAdapter!!.notifyDataSetChanged()
        }

        // 自动跳到物料焦点
        smqFlag = '3'
        mHandler.sendEmptyMessage(SETFOCUS)
        // 查询库存数
        run_findAvgQtyList()
    }

    /**
     * 得到容器
     */
    fun getContainer(m : Container) {
        container = m
        tv_containerName.text = m!!.fnumber
        if(checkDatas.size > 0 && checkDatas[curPos].useContainer.equals("Y")) {
            checkDatas[curPos].container = m
            mAdapter!!.notifyDataSetChanged()
        }
    }

    private fun reset() {
        setEnables(et_code, R.color.transparent, true)
        if(stock != null) {
            smqFlag = '3'
        } else {
            smqFlag = '1'
        }
        et_code!!.setText("")
        tv_mtlName.text = ""
        parent!!.isChange = false
        checkDatas.clear()
        mHandler.sendEmptyMessageDelayed(SETFOCUS, 200)

        mAdapter!!.notifyDataSetChanged()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
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
                    getStockGroup(null)
                }
            }
            SEL_CONTAINER -> {//查询容器	返回
                if (resultCode == Activity.RESULT_OK) {
                    val container = data!!.getSerializableExtra("obj") as Container
                    getContainer(container)
                }
            }
            SEL_MTL -> {//查询物料	返回
                if (resultCode == Activity.RESULT_OK) {
                    val list = data!!.getSerializableExtra("obj") as List<ICItem>
                    getMtlAfter(list,1)
                }
            }
            BaseFragment.CAMERA_SCAN -> {// 扫一扫成功  返回
                if (resultCode == Activity.RESULT_OK) {
                    val bundle = data!!.extras
                    if (bundle != null) {
                        val code = bundle.getString(BaseFragment.DECODED_CONTENT_KEY, "")
                        when(smqFlag) {
                            '1' -> setTexts(et_positionCode, code)
                            '2' -> setTexts(et_containerCode, code)
                            '3' -> setTexts(et_code, code)
                        }
                    }
                }
            }
            WRITE_CODE -> {// 输入条码返回
                if (resultCode == Activity.RESULT_OK) {
                    val bundle = data!!.extras
                    if (bundle != null) {
                        val value = bundle.getString("resultValue", "")
                        when(smqFlag) {
                            '1' -> setTexts(et_positionCode, value.toUpperCase())
                            '2' -> setTexts(et_containerCode, value.toUpperCase())
                            '3' -> setTexts(et_code, value.toUpperCase())
                        }
                    }
                }
            }
            RESULT_WEIGHT -> { // 重量
                if (resultCode == Activity.RESULT_OK) {
                    val bundle = data!!.getExtras()
                    if (bundle != null) {
                        val value = bundle.getString("resultValue", "")
                        val num = parseDouble(value)
                        checkDatas[curPos].weight = num
                        curPos = -1
                        mAdapter!!.notifyDataSetChanged()
                    }
                }
            }
            RESULT_NUM -> { // 数量
                if (resultCode == Activity.RESULT_OK) {
                    val bundle = data!!.getExtras()
                    if (bundle != null) {
                        val value = bundle.getString("resultValue", "")
                        val num = parseDouble(value)
                        checkDatas[curPos].realQty = num
                        curPos = -1
                        mAdapter!!.notifyDataSetChanged()
                    }
                }
            }
            RESULT_MINPACK -> { // 最小包装数
                if (resultCode == Activity.RESULT_OK) {
                    val bundle = data!!.getExtras()
                    if (bundle != null) {
                        val value = bundle.getString("resultValue", "")
                        val num = parseDouble(value)
                        checkDatas[curPos].minPackQty = num
                        curPos = -1
                        mAdapter!!.notifyDataSetChanged()
                    }
                }
            }
            RESULT_BATCH -> { // 批次号
                if (resultCode == Activity.RESULT_OK) {
                    val bundle = data!!.getExtras()
                    if (bundle != null) {
                        val value = bundle.getString("resultValue", "")
                        checkDatas[curPos].batchCode = value
                        curPos = -1
                        mAdapter!!.notifyDataSetChanged()
                    }
                }
            }

        }
        mHandler.sendEmptyMessageDelayed(SETFOCUS, 300)
    }

    /**
     * 得到扫码或选择数据
     */
    private fun getMtlAfter(list: List<ICItem>, flag: Int) {
        parent!!.isChange = true
        // 循环判断业务
        for (icItem in list) {
            // 填充数据
            val size = checkDatas.size
            var addRow = true
            var curPosition = 0
            for (i in 0 until size) {
                val sr = checkDatas.get(i)
                // 有相同的，就不新增了
                if (sr.fitemid == icItem.fitemid) {
                    curPosition = i
                    addRow = false
                    break
                }
            }
            if (addRow) {
                // 全部清空
                icItem.stock = null
                icItem.stockArea = null
                icItem.storageRack = null
                icItem.stockPos = null
                icItem.container = null

                if(stock != null) {
                    icItem.stock = stock
                }
                if(stockArea != null) {
                    icItem.stockArea = stockArea
                }
                if(storageRack != null) {
                    icItem.storageRack = storageRack
                }
                if(stockPos != null) {
                    icItem.stockPos = stockPos
                }
                if(icItem.useContainer.equals("Y") && container != null) {
                    icItem.container = container
                }
                if(icItem.batchManager.equals("Y")) { // 启用了批次号，就给个默认值
                    icItem.batchCode = Comm.getSysDate(3)
                }
                icItem.isCheck = false
                icItem.realQty = 1.0
                checkDatas.add(icItem)

            } else {
                // 已有相同物料行，就叠加数量
                val fqty = checkDatas[curPosition].realQty
                val addVal = BigdecimalUtil.add(fqty, 1.0);
                checkDatas[curPosition].realQty = addVal
            }
        }

        if(flag == 1) {
            checkDatas.forEach {
                it.isCheck = false
            }
            mAdapter!!.notifyDataSetChanged()
            recyclerView.post(Runnable { recyclerView.smoothScrollToPosition(checkDatas.size - 1) })
        }
        mAdapter!!.notifyDataSetChanged()

        run_findAvgQtyList()
    }

    /**
     * 扫码查询对应的方法
     */
    private fun run_okhttpDatas() {
        isTextChange = false
        showLoadDialog("加载中...", false)
        var mUrl:String? = null
        var barcode:String? = null
        when(smqFlag) {
            '1' -> {
                mUrl = getURL("stockPosition/findBarcodeGroup")
                barcode = getValues(et_positionCode)
            }
            '2' -> {
                mUrl = getURL("container/findBarcode")
                barcode = getValues(et_containerCode)
            }
            '3' -> {
                mUrl = getURL("icItem/findBarcode")
                barcode = getValues(et_code)
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
                LogUtil.e("run_okhttpDatas --> onResponse", result)
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

        val mUrl = getURL("icInvBackup/save")
        val mJson = JsonUtil.objectToString(checkDatas)
        val formBody = FormBody.Builder()
                .add("strJson", strJson)
                .add("callType", "2") // 执行的存储过程是哪个（1：callTmpICInvBackup_To_ICInvBackup，2：callTmpICInvBackup_To_ICInvBackup2）
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
     * 提交
     */
    private fun run_submitTok3() {
        showLoadDialog("提交中...", false)

        getUserInfo()
        val mUrl = getURL("icInvBackup/submitTok3")
        val mJson = JsonUtil.objectToString(checkDatas)
        val formBody = FormBody.Builder()
                .add("strJson", mJson)
                .add("userId", user!!.id.toString())
                .build()

        val request = Request.Builder()
                .addHeader("cookie", getSession())
                .url(mUrl)
                .post(formBody)
                .build()

        val call = okHttpClient!!.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                mHandler.sendEmptyMessage(UNSUBMIT)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()
                val result = body.string()
                if (!JsonUtil.isSuccess(result)) {
                    val msg = mHandler.obtainMessage(UNSUBMIT, result)
                    mHandler.sendMessage(msg)
                    return
                }
                val msg = mHandler.obtainMessage(SUBMIT, result)
                LogUtil.e("run_submitTok3 --> onResponse", result)
                mHandler.sendMessage(msg)
            }
        })
    }

    /**
     * 历史查询
     */
    private fun run_findListByParamWms() {
        isTextChange = false
        showLoadDialog("加载中...", false)
        val mUrl = getURL("icInvBackup/findListByParamWms")
        val formBody = FormBody.Builder()
                .add("finterId", if (icStockCheckProcess != null) icStockCheckProcess!!.fid.toString() else "") // 方案id
                .add("toK3", "1")
                .add("userId", user!!.id.toString())
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
     * 可用库存查询
     */
    private fun run_findAvgQtyList() {
        showLoadDialog("加载中...", false)
        val mUrl = getURL("inventoryNow/findAvbQty")
        val strStockGroup = stock!!.fitemId.toString() +","+ (if(stockArea != null) stockArea!!.id else 0) +","+
                (if(storageRack != null) storageRack!!.id else 0) +","+ (if(stockPos != null) stockPos!!.id else 0)
        var strMtlId = StringBuffer()
        checkDatas.forEach {
            strMtlId.append((if(strMtlId.length > 0) "," else "" )+it.fitemid)
        }

        val formBody = FormBody.Builder()
                .add("strMtlId", strMtlId.toString())
                .add("strStockGroup", strStockGroup)
                .build()

        val request = Request.Builder()
                .addHeader("cookie", getSession())
                .url(mUrl)
                .post(formBody)
                .build()

        val call = okHttpClient!!.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                mHandler.sendEmptyMessage(UNFIND_AVGQTY)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()
                val result = body.string()
                LogUtil.e("run_findListByParamWms --> onResponse", result)
                if (!JsonUtil.isSuccess(result)) {
                    val msg = mHandler.obtainMessage(UNFIND_AVGQTY, result)
                    mHandler.sendMessage(msg)
                    return
                }
                val msg = mHandler.obtainMessage(FIND_AVGQTY, result)
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