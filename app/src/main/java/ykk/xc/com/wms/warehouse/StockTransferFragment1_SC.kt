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
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.OnClick
import kotlinx.android.synthetic.main.ware_sc_stock_transfer_fragment1_sc.*
import okhttp3.*
import ykk.xc.com.wms.R
import ykk.xc.com.wms.basics.StockPos_DialogActivity
import ykk.xc.com.wms.basics.Stock_DialogActivity
import ykk.xc.com.wms.bean.Stock
import ykk.xc.com.wms.bean.StockPosition
import ykk.xc.com.wms.bean.StockTransferRecord
import ykk.xc.com.wms.bean.User
import ykk.xc.com.wms.bean.k3Bean.ICInventory
import ykk.xc.com.wms.comm.BaseFragment
import ykk.xc.com.wms.comm.Comm
import ykk.xc.com.wms.util.BigdecimalUtil
import ykk.xc.com.wms.util.JsonUtil
import ykk.xc.com.wms.util.LogUtil
import ykk.xc.com.wms.util.zxing.android.CaptureActivity
import ykk.xc.com.wms.warehouse.adapter.StockTransferFragment1_SC_Adapter
import java.io.IOException
import java.lang.ref.WeakReference
import java.text.DecimalFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * 日期：2019-10-16 09:50
 * 描述：调拨
 * 作者：ykk
 */
class StockTransferFragment1_SC : BaseFragment() {

    private val SEL_OUTSTOCK = 10
    private val SEL_INSTOCK = 11
    private val SEL_OUTSTOCKPOS = 12
    private val SEL_INSTOCKPOS = 13
    private val SEL_MTL = 14
    private val context = this
    private val SUCC1 = 200
    private val UNSUCC1 = 500
    private val SUCC2 = 201
    private val UNSUCC2 = 501
    private val SAVE = 202
    private val UNSAVE = 502
    private val SETFOCUS = 1
    private val RESULT_NUM = 2
    private val SAOMA = 3
    private val WRITE_CODE = 4

    private var mAdapter: StockTransferFragment1_SC_Adapter? = null
    private val checkDatas = ArrayList<ICInventory>()
    private var okHttpClient: OkHttpClient? = null
    private var user: User? = null
    private var outStock:Stock? = null
    private var inStock: Stock? = null
    private var outStockPos:StockPosition? = null
    private var inStockPos:StockPosition? = null
    private var mContext: Activity? = null
    private val df = DecimalFormat("#.######")
    private var parent: StockTransferMainActivity? = null
    private var isTextChange: Boolean = false // 是否进入TextChange事件
    private var curPos:Int = 0 // 当前行
    private var smqFlag:Char = '3' // 使用扫码枪扫码（1：调出库位扫码，2：调入库位扫码，3：物料扫码）
    private var timesTamp:String? = null // 时间戳


    // 消息处理
    private val mHandler = MyHandler(this)

    private class MyHandler(activity: StockTransferFragment1_SC) : Handler() {
        private val mActivity: WeakReference<StockTransferFragment1_SC>

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
                        when(m.smqFlag) {
                            '1' -> {
                                m.outStockPos = JsonUtil.strToObject(msgObj, StockPosition::class.java)
                                m.tv_outStockPosName.text = m.outStockPos!!.fname
                            }
                            '2' -> {
                                m.inStockPos = JsonUtil.strToObject(msgObj, StockPosition::class.java)
                                m.tv_inStockPosName.text = m.inStockPos!!.fname
                            }
                            '3' -> {
                                val icInventory = JsonUtil.strToObject(msgObj, ICInventory::class.java)
                                var listIcInvBackup = ArrayList<ICInventory>()
                                listIcInvBackup.add(icInventory)

                                m.getMtlAfter(listIcInvBackup)
                            }
                        }
                    }
                    m.UNSUCC1 -> { // 扫码失败
                        m.mAdapter!!.notifyDataSetChanged()
                        errMsg = JsonUtil.strToString(msgObj)
                        if (m.isNULLS(errMsg).length == 0) errMsg = "很抱歉，没能找到数据！"
                        Comm.showWarnDialog(m.mContext, errMsg)
                    }
                    m.SUCC2 -> { // 历史查询 进入
                        m.checkDatas.clear()
                        val icInvBackup = JsonUtil.strToList(msgObj, ICInventory::class.java)
                        m.checkDatas.addAll(icInvBackup)
                        m.mAdapter!!.notifyDataSetChanged()
                    }
                    m.UNSUCC2 -> { // 历史查询  失败
                        m.mAdapter!!.notifyDataSetChanged()
                        errMsg = JsonUtil.strToString(msgObj)
                        if (m.isNULLS(errMsg).length == 0) errMsg = "很抱歉，没能找到数据！"
                        Comm.showWarnDialog(m.mContext, errMsg)
                    }
                    m.SAVE // 保存成功 进入
                    -> {
                        m.reset(false)
                        m.toasts("保存成功✔")
                    }
                    m.UNSAVE -> { // 保存失败
                        errMsg = JsonUtil.strToString(msgObj)
                        if (m.isNULLS(errMsg).length == 0) errMsg = "保存失败！"
                        Comm.showWarnDialog(m.mContext, errMsg)
                    }
                    m.SETFOCUS -> { // 当弹出其他窗口会抢夺焦点，需要跳转下，才能正常得到值
                        m.setFocusable(m.et_getFocus)
                        when (m.smqFlag) {
                            '1' -> m.setFocusable(m.et_outStockPos)
                            '2' -> m.setFocusable(m.et_inStockPos)
                            '3' -> m.setFocusable(m.et_code)
                        }
                    }
                    m.SAOMA -> { // 扫码之后
                        if(!m.checkSelStockPos()){
                            m.isTextChange = false
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
        return inflater.inflate(R.layout.ware_sc_stock_transfer_fragment1_ds, container, false)
    }

    override fun initView() {
        mContext = getActivity()
        parent = mContext as StockTransferMainActivity

        recyclerView.addItemDecoration(DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL))
        recyclerView.layoutManager = LinearLayoutManager(mContext)
        mAdapter = StockTransferFragment1_SC_Adapter(mContext!!, checkDatas)
        recyclerView.adapter = mAdapter
        // 设值listview空间失去焦点
        recyclerView.isFocusable = false

        // 行事件
        mAdapter!!.setCallBack(object : StockTransferFragment1_SC_Adapter.MyCallBack {
            override fun onClick_num(entity: ICInventory, position: Int) {
                curPos = position
                showInputDialog("数量", entity.realQty.toString(), "0.0", RESULT_NUM)
            }
        })
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
        hideSoftInputMode(mContext, et_outStockPos)
        hideSoftInputMode(mContext, et_inStockPos)
        hideSoftInputMode(mContext, et_code)
        timesTamp = user!!.getId().toString() + "-" + Comm.randomUUID()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            mHandler.sendEmptyMessageDelayed(SETFOCUS, 200)
        }
    }

    @OnClick(R.id.tv_outStockSel, R.id.tv_inStockSel, R.id.btn_scan_outStockPos, R.id.btn_scan_inStockPos, R.id.btn_outStockPosSel, R.id.btn_inStockPosSel, R.id.btn_scan, R.id.btn_mtlSel, R.id.btn_save, R.id.btn_clone)
    fun onViewClicked(view: View) {
        var bundle: Bundle? = null
        when (view.id) {
            R.id.tv_outStockSel -> { // 选择调出仓库
                bundle = Bundle()
                bundle.putString("accountType", "SC");
                bundle.putInt("unDisable", 1) // 只显示未禁用的数据
                showForResult(Stock_DialogActivity::class.java, SEL_OUTSTOCK, bundle)
            }
            R.id.tv_inStockSel -> { // 选择调入仓库
                bundle = Bundle()
                bundle.putString("accountType", "SC");
                bundle.putInt("unDisable", 1) // 只显示未禁用的数据
                showForResult(Stock_DialogActivity::class.java, SEL_INSTOCK, bundle)
            }
            R.id.btn_outStockPosSel -> { // 选择调出库位
                smqFlag = '1'
                if(!checkSelStockPos()) return;
                bundle = Bundle()
                bundle.putString("accountType", "SC")
                bundle.putInt("fspGroupId", outStock!!.fspGroupId);
                showForResult(StockPos_DialogActivity::class.java, SEL_OUTSTOCKPOS, bundle)
            }
            R.id.btn_inStockPosSel -> { // 选择调入库位
                smqFlag = '2'
                if(!checkSelStockPos()) return;
                var bundle = Bundle()
                bundle.putInt("fspGroupId", inStock!!.fspGroupId);
                bundle.putString("accountType", "SC");
                showForResult(StockPos_DialogActivity::class.java, SEL_INSTOCKPOS, bundle)
            }
            R.id.btn_mtlSel -> { // 选择物料
                smqFlag = '3'
                if(!checkSelStockPos()) return;
                bundle = Bundle()
                bundle.putInt("fStockID", outStock!!.fitemId)
//                bundle.putInt("fStockPlaceID", if(outStockPos != null) outStockPos!!.fspId else 0)
                bundle.putString("accountType", "SC");
                showForResult(StockTransfer_Material_DialogActivity::class.java, SEL_MTL, bundle)
            }
            R.id.btn_scan_outStockPos -> { // 调用摄像头扫描（调出库位）
                smqFlag = '1'
                if(!checkSelStockPos()) return;
                showForResult(CaptureActivity::class.java, BaseFragment.CAMERA_SCAN, null)
            }
            R.id.btn_scan_inStockPos -> { // 调用摄像头扫描（调入库位）
                smqFlag = '2'
                if(!checkSelStockPos()) return;
                showForResult(CaptureActivity::class.java, BaseFragment.CAMERA_SCAN, null)
            }
            R.id.btn_scan -> { // 调用摄像头扫描（物料）
                smqFlag = '3'
                if(!checkSelStockPos()) return;
                showForResult(CaptureActivity::class.java, BaseFragment.CAMERA_SCAN, null)
            }
            R.id.btn_save -> { // 保存
                if(checkDatas.size == 0) {
                    Comm.showWarnDialog(mContext,"请选择物料或者扫码条码！")
                    return
                }
                run_save();
            }
            R.id.btn_clone -> { // 重置
                if (parent!!.isChange) {
                    val build = AlertDialog.Builder(mContext)
                    build.setIcon(R.drawable.caution)
                    build.setTitle("系统提示")
                    build.setMessage("您有未保存的数据，继续重置吗？")
                    build.setPositiveButton("是") { dialog, which -> reset(true) }
                    build.setNegativeButton("否", null)
                    build.setCancelable(false)
                    build.show()

                } else {
                    reset(true)
                }
            }
        }
    }

    /**
     * 选择库位判断
     */
    fun checkSelStockPos() : Boolean {
        when (smqFlag) {
            '1' -> {
                if (outStock == null) {
                    Comm.showWarnDialog(mContext, "请选择调出仓库！")
                    return false;
                }
                if (outStock!!.fisStockMgr == 0) {
                    Comm.showWarnDialog(mContext, "调出仓库没有启用库位！")
                    return false;
                }
            }
            '2' -> {
                if (inStock == null) {
                    Comm.showWarnDialog(mContext, "请选择调入仓库！")
                    return false;
                }
                if (inStock!!.fisStockMgr == 0) {
                    Comm.showWarnDialog(mContext, "调入仓库没有启用库位！")
                    return false;
                }
            }
            '3' -> {
                if (outStock == null) {
                    Comm.showWarnDialog(mContext, "请选择调出仓库！")
                    return false;
                }
                if (getValues(tv_outStockPosName).length == 0 && outStock!!.fisStockMgr == 1) {
                    Comm.showWarnDialog(mContext, "请选择调出库位！")
                    return false;
                }
                if (inStock == null) {
                    Comm.showWarnDialog(mContext, "请选择调入仓库！")
                    return false;
                }
                if (getValues(tv_inStockPosName).length == 0 && inStock!!.fisStockMgr == 1) {
                    Comm.showWarnDialog(mContext, "请选择调入库位！")
                    return false;
                }
            }
        }
        return true;
    }

    override fun setListener() {
        val click = View.OnClickListener { v ->
            setFocusable(et_getFocus)
            when (v.id) {
                R.id.et_outStockPos -> setFocusable(et_outStockPos)
                R.id.et_inStockPos -> setFocusable(et_inStockPos)
                R.id.et_code -> setFocusable(et_code)
            }
        }
        et_outStockPos!!.setOnClickListener(click)
        et_inStockPos!!.setOnClickListener(click)
        et_code!!.setOnClickListener(click)

        // 调出库位---数据变化
        et_outStockPos!!.addTextChangedListener(object : TextWatcher {
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

        // 调出库位---长按输入条码
        et_outStockPos!!.setOnLongClickListener {
            smqFlag = '1'
            showInputDialog("输入条码", "", "none", WRITE_CODE)
            true
        }

        // 调出库位---焦点变化
        et_outStockPos!!.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                lin_focusOutStockPos!!.setBackgroundResource(R.drawable.back_style_red_focus)
            } else {
                if (lin_focusOutStockPos != null) {
                    lin_focusOutStockPos!!.setBackgroundResource(R.drawable.back_style_gray4)
                }
            }
        }

        // 调入库位---数据变化
        et_inStockPos!!.addTextChangedListener(object : TextWatcher {
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
        // 调入库位---长按输入条码
        et_inStockPos!!.setOnLongClickListener {
            smqFlag = '2'
            showInputDialog("输入条码", "", "none", WRITE_CODE)
            true
        }
        // 调入库位---焦点变化
        et_inStockPos!!.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                lin_focusInStockPos!!.setBackgroundResource(R.drawable.back_style_red_focus)
            } else {
                if (lin_focusInStockPos != null) {
                    lin_focusInStockPos!!.setBackgroundResource(R.drawable.back_style_gray4)
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
            showInputDialog("输入条码", "", "none", WRITE_CODE)
            true
        }
        // 物料---焦点变化
        et_code!!.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                lin_focusMtl!!.setBackgroundResource(R.drawable.back_style_red_focus)
            } else {
                if (lin_focusMtl != null) {
                    lin_focusMtl!!.setBackgroundResource(R.drawable.back_style_gray4)
                }
            }
        }
    }

    private fun reset(isAll : Boolean) {
        if(isAll) {
            et_outStockPos!!.setText("")
            et_inStockPos!!.setText("")
            et_code!!.setText("")
            tv_outStockSel.text = ""
            tv_inStockSel.text = ""
            tv_outStockPosName.text = ""
            tv_inStockPosName.text = ""
            outStock = null;
            inStock = null;
            outStockPos = null;
            inStockPos = null;
            smqFlag = '3'
        }
        tv_countNum.text = ""
        timesTamp = user!!.getId().toString() + "-" + Comm.randomUUID()
        parent!!.isChange = false
        checkDatas.clear()
        mHandler.sendEmptyMessageDelayed(SETFOCUS, 200)

        mAdapter!!.notifyDataSetChanged()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            SEL_OUTSTOCK -> {// 调出仓库	返回
                if (resultCode == Activity.RESULT_OK) {
                    outStock = data!!.getSerializableExtra("obj") as Stock
                    // 判断调出仓库和调入仓库是否一样
                    if(inStock != null && inStock!!.fnumber == outStock!!.fnumber) {
                        Comm.showWarnDialog(mContext,"调出仓库和调入仓库不能相同！")
                        return
                    }
                    tv_outStockSel.text = outStock!!.fname
                    // 如果启用了库位
                    if(outStock!!.fisStockMgr == 1) {
                        lin_outStockPos.visibility = View.VISIBLE
                        var bundle = Bundle()
                        bundle.putInt("fspGroupId", outStock!!.fspGroupId);
                        bundle.putString("accountType", "SC")
                        showForResult(StockPos_DialogActivity::class.java, SEL_OUTSTOCKPOS, bundle)
                    } else {
                        lin_outStockPos.visibility = View.GONE
                    }
                    outStockPos = null
                    tv_outStockPosName.text = ""
                }
            }
            SEL_INSTOCK -> {// 调入仓库	返回
                if (resultCode == Activity.RESULT_OK) {
                    inStock = data!!.getSerializableExtra("obj") as Stock
                    // 判断调出仓库和调入仓库是否一样
                    if(outStock != null && inStock!!.fnumber == outStock!!.fnumber) {
                        Comm.showWarnDialog(mContext,"调出仓库和调入仓库不能相同！")
                        return
                    }
                    tv_inStockSel.text = inStock!!.fname
                    // 如果启用了库位
                    if(inStock!!.fisStockMgr == 1) {
                        lin_inStockPos.visibility = View.VISIBLE
                        var bundle = Bundle()
                        bundle.putInt("fspGroupId", inStock!!.fspGroupId);
                        bundle.putString("accountType", "SC")
                        showForResult(StockPos_DialogActivity::class.java, SEL_INSTOCKPOS, bundle)
                    } else {
                        lin_inStockPos.visibility = View.GONE
                    }
                    inStockPos = null
                    tv_inStockPosName.text = ""
                }
            }
            SEL_OUTSTOCKPOS -> {// 调出仓库库位	返回
                if (resultCode == Activity.RESULT_OK) {
                    outStockPos = data!!.getSerializableExtra("obj") as StockPosition
                    tv_outStockPosName.text = outStockPos!!.fname
                }
            }
            SEL_INSTOCKPOS -> {// 调入仓库库位	返回
                if (resultCode == Activity.RESULT_OK) {
                    inStockPos = data!!.getSerializableExtra("obj") as StockPosition
                    tv_inStockPosName.text = inStockPos!!.fname
                }
            }
            SEL_MTL //查询物料	返回
            -> if (resultCode == Activity.RESULT_OK) {
                val list = data!!.getSerializableExtra("obj") as List<ICInventory>

                getMtlAfter(list)
            }

            BaseFragment.CAMERA_SCAN // 扫一扫成功  返回
            -> if (resultCode == Activity.RESULT_OK) {
                val bundle = data!!.extras
                if (bundle != null) {
                    val code = bundle.getString(BaseFragment.DECODED_CONTENT_KEY, "")
                    when (smqFlag) {
                        '1' -> setTexts(et_outStockPos, code)
                        '2' -> setTexts(et_inStockPos, code)
                        '3' -> setTexts(et_code, code)
                    }
                }
            }
            WRITE_CODE // 输入条码返回
            -> if (resultCode == Activity.RESULT_OK) {
                val bundle = data!!.extras
                if (bundle != null) {
                    val value = bundle.getString("resultValue", "")
                    et_code!!.setText(value.toUpperCase())
                }
            }
            RESULT_NUM // 数量
            -> if (resultCode == Activity.RESULT_OK) {
                val bundle = data!!.getExtras()
                if (bundle != null) {
                    val value = bundle.getString("resultValue", "")
                    val num = parseDouble(value)
                    checkDatas[curPos].realQty = num
                    countNum()
                    mAdapter!!.notifyDataSetChanged()
                }
            }

        }
        mHandler.sendEmptyMessageDelayed(SETFOCUS, 300)
    }

    /**
     * 得到扫码或选择数据
     */
    private fun getMtlAfter(list: List<ICInventory>) {
        parent!!.isChange = true
        val size = checkDatas.size
        var addRow = true
        var curPosition = 0
        // 循环判断业务
        for (icInventory in list) {
            // 填充数据
            addRow = true
            curPosition = 0
            for (i in 0 until size) {
                val sr = checkDatas.get(i)
                // 有相同的，就不新增了
                if (sr.getfItemID() == icInventory.getfItemID()) {
                    curPosition = i
                    addRow = false
                    break
                }
            }
            if (addRow) {
                icInventory.realQty = 1.0
                icInventory.createUserId = user!!.id
                checkDatas.add(icInventory)

            } else {
                // 已有相同物料行，就叠加数量
                val fqty = checkDatas[curPosition].realQty
                val addVal = BigdecimalUtil.add(fqty, 1.0);
                checkDatas[curPosition].realQty = addVal
            }
        }
        countNum()
        mAdapter!!.notifyDataSetChanged()
    }

    /**
     *  合计
     */
    fun countNum() {
        var count:Double = 0.0
        checkDatas.forEach() {
            val addVal = BigdecimalUtil.add(count, it.realQty)
            count = addVal;
        }
        tv_countNum.text = df.format(count)
    }

    /**
     * 扫码查询对应的方法
     */
    private fun run_smDatas() {
        isTextChange = false
        showLoadDialog("加载中...", false)
        var mUrl:String? = null
        var barcode:String? = null
        when(smqFlag) {
            '1' -> {
                mUrl = getURL("stockPosition/findBarcode")
                barcode = getValues(et_outStockPos)
            }
            '2' -> {
                mUrl = getURL("stockPosition/findBarcode")
                barcode = getValues(et_inStockPos)
            }
            '3' -> {
                mUrl = getURL("icInventory/findBarcode")
                barcode = getValues(et_code)
            }
        }
        val formBody = FormBody.Builder()
                .add("fStockID", outStock!!.fitemId.toString())
//                .add("fStockPlaceID", if(outStockPos != null) outStockPos!!.fspId.toString() else "")
                .add("FSPGroupId", outStock!!.fspGroupId.toString())
                .add("accountType", "SC")
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
    private fun run_save() {
        var saveList = ArrayList<StockTransferRecord>()
        // for循环
        checkDatas.forEachIndexed { index, icInventory ->
            if(icInventory.getfQty() < icInventory.realQty) {
                Comm.showWarnDialog(mContext,"第（"+(index+1)+"）行，调拨数不能大于库存数！")
                return
            }
            val save = StockTransferRecord()
            save.outStockId = outStock!!.fitemId
            save.outStockNumber = outStock!!.fnumber
            save.outStockName = outStock!!.fname
            save.inStockId = inStock!!.fitemId
            save.inStockNumber = inStock!!.fnumber
            save.inStockName = inStock!!.fname
            if(outStockPos != null) {
//                save.outStockPosId = outStockPos!!.fspId
                save.outStockPosNumber = outStockPos!!.fnumber
                save.outStockPosName = outStockPos!!.fname
            }
            if(inStockPos != null) {
//                save.inStockPosId = inStockPos!!.fspId
                save.inStockPosNumber = inStockPos!!.fnumber
                save.inStockPosName = inStockPos!!.fname
            }
            save.icItemId = icInventory.getfItemID()
            save.icItemNumber = icInventory!!.mtlNumber
            save.icItemName = icInventory!!.mtlName
            save.timesTamp = timesTamp
            save.allotQty = icInventory.realQty
            save.createUserId = user!!.id
            save.createUserName = user!!.username
            save.accountName = "生产账号"
            saveList.add(save)
        }
        showLoadDialog("保存中...", false)
        val mUrl = getURL("stockTransferRecord/addList_SC")
        val mJson = JsonUtil.objectToString(saveList)
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
     * 历史查询
     */
    private fun run_findListByParamWms() {
        isTextChange = false
        showLoadDialog("加载中...", false)
        val mUrl = getURL("ICInventory/findListByParamWms")
        val formBody = FormBody.Builder()
//                .add("finterId", if (icStockCheckProcess != null) icStockCheckProcess!!.fid.toString() else "") // 方案id
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