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
import kotlinx.android.synthetic.main.ware_icinvbackup_fragment1.*
import okhttp3.*
import ykk.xc.com.wms.R
import ykk.xc.com.wms.basics.Stock_DialogActivity
import ykk.xc.com.wms.bean.Stock
import ykk.xc.com.wms.bean.User
import ykk.xc.com.wms.bean.k3Bean.ICInvBackup
import ykk.xc.com.wms.bean.k3Bean.ICStockCheckProcess
import ykk.xc.com.wms.comm.BaseFragment
import ykk.xc.com.wms.comm.Comm
import ykk.xc.com.wms.util.BigdecimalUtil
import ykk.xc.com.wms.util.JsonUtil
import ykk.xc.com.wms.util.LogUtil
import ykk.xc.com.wms.util.zxing.android.CaptureActivity
import ykk.xc.com.wms.warehouse.adapter.ICInvBackup_Fragment1_Adapter
import java.io.IOException
import java.lang.ref.WeakReference
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * 日期：2019-10-16 09:50
 * 描述：WMS 盘点（有盘点方案）
 * 作者：ykk
 */
class ICInvBackup_Fragment1 : BaseFragment() {

    private val SEL_PROJECT = 10
    private val SEL_STOCK = 11
    private val SEL_MTL = 12

    private val context = this
    private val SUCC1 = 200
    private val UNSUCC1 = 500
    private val SUCC2 = 201
    private val UNSUCC2 = 501
    private val SUBMIT = 202
    private val UNSUBMIT = 502
    private val SAVE = 203
    private val UNSAVE = 503
    private val SETFOCUS = 1
    private val RESULT_NUM = 2
    private val SAOMA = 3
    private val WRITE_CODE = 4

    private var mAdapter: ICInvBackup_Fragment1_Adapter? = null
    private val checkDatas = ArrayList<ICInvBackup>()
    private var okHttpClient: OkHttpClient? = null
    private var user: User? = null
    private var mContext: Activity? = null
    private var parent: ICInvBackup_MainActivity? = null
    private var barcode: String? = null // 对应的条码号
    private var isTextChange: Boolean = false // 是否进入TextChange事件
    private var icStockCheckProcess: ICStockCheckProcess? = null
    private var curPos:Int = 0 // 当前行

    // 消息处理
    private val mHandler = MyHandler(this)

    private class MyHandler(activity: ICInvBackup_Fragment1) : Handler() {
        private val mActivity: WeakReference<ICInvBackup_Fragment1>

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
                        val icInvBackup = JsonUtil.strToObject(msgObj, ICInvBackup::class.java)
                        var listIcInvBackup = ArrayList<ICInvBackup>()
                        listIcInvBackup.add(icInvBackup)

                        m.getMtlAfter(listIcInvBackup)
                    }
                    m.UNSUCC1 -> { // 扫码失败
                        m.mAdapter!!.notifyDataSetChanged()
                        errMsg = JsonUtil.strToString(msgObj)
                        if (m.isNULLS(errMsg).length == 0) errMsg = "很抱歉，没能找到数据！"
                        Comm.showWarnDialog(m.mContext, errMsg)
                    }
                    m.SUCC2 -> { // 历史查询 进入
                        m.checkDatas.clear()
                        val icInvBackup = JsonUtil.strToList(msgObj, ICInvBackup::class.java)
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
                        m.reset()
                        m.toasts("保存成功✔")
                    }
                    m.UNSAVE -> { // 保存失败
                        errMsg = JsonUtil.strToString(msgObj)
                        if (m.isNULLS(errMsg).length == 0) errMsg = "保存失败！"
                        Comm.showWarnDialog(m.mContext, errMsg)
                    }
                    m.SUBMIT // 提交成功 返回
                    -> {
                        m.reset()
                        m.toasts("提交成功✔")
                    }
                    m.UNSUBMIT // 提交失败 返回
                    -> {
                        errMsg = JsonUtil.strToString(msgObj!!)
                        if (m.isNULLS(errMsg).length == 0) errMsg = "服务器忙，请稍候再试！"
                        Comm.showWarnDialog(m.mContext, errMsg)
                    }
                    m.SETFOCUS // 当弹出其他窗口会抢夺焦点，需要跳转下，才能正常得到值
                    -> {
                        m.setFocusable(m.et_getFocus)
                        m.setFocusable(m.et_code)
                    }
                    m.SAOMA // 扫码之后
                    -> {
                        if(!m.checkProject()) {
                            m.isTextChange = false
                            return
                        }
                        m.barcode = m.getValues(m.et_code)
                        m.setTexts(m.et_code, m.barcode!!)
                        // 执行查询方法
                        m.run_okhttpDatas()
                    }
                }
            }
        }
    }

    override fun setLayoutResID(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.ware_icinvbackup_fragment1, container, false)
    }

    override fun initView() {
        mContext = getActivity()
        parent = mContext as ICInvBackup_MainActivity

        recyclerView.addItemDecoration(DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL))
        recyclerView.layoutManager = LinearLayoutManager(mContext)
        mAdapter = ICInvBackup_Fragment1_Adapter(mContext, checkDatas)
        recyclerView.adapter = mAdapter
        // 设值listview空间失去焦点
        recyclerView.isFocusable = false

        // 行事件
        mAdapter!!.setCallBack(object : ICInvBackup_Fragment1_Adapter.MyCallBack {
            override fun onClick_num(entity: ICInvBackup, position: Int) {
                curPos = position
                showInputDialog("数量", entity.realQty.toString(), "0.0", RESULT_NUM)
            }

            override fun onDelete(entity: ICInvBackup, position: Int) {
                checkDatas.removeAt(position)
                mAdapter!!.notifyDataSetChanged()
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
        hideSoftInputMode(mContext, et_code)

    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            mHandler.sendEmptyMessageDelayed(SETFOCUS, 200)
        }
    }

    @OnClick(R.id.tv_icStockCheckProcess, R.id.tv_stockSel, R.id.btn_mtlSel, R.id.btn_scan, R.id.btn_save, R.id.btn_clone, R.id.btn_submit)
    fun onViewClicked(view: View) {
        var bundle: Bundle? = null
        when (view.id) {
            R.id.tv_icStockCheckProcess -> { // 选择方案
                bundle = Bundle()
                showForResult(ICStockCheckProcess_DialogActivity::class.java, SEL_PROJECT, bundle)
            }
            R.id.tv_stockSel -> { // 选择仓库
                bundle = Bundle()
                bundle.putString("accountType", "SC")
                bundle.putInt("unDisable", 1) // 只显示未禁用的数据
                showForResult(Stock_DialogActivity::class.java, SEL_STOCK, bundle)
            }
            R.id.btn_mtlSel -> { // 选择物料
                if(!checkProject()) return
                bundle = Bundle()
                bundle.putInt("finterId", icStockCheckProcess!!.fid)
                showForResult(ICInvBackup_Sel_MaterialMainDialog::class.java, SEL_MTL, bundle)
            }
            R.id.btn_scan -> { // 调用摄像头扫描（快递单）
                if(!checkProject()) return
                showForResult(CaptureActivity::class.java, BaseFragment.CAMERA_SCAN, null)
            }
            R.id.btn_save -> { // 保存
                if(!checkProject()) return
                if(checkDatas.size == 0) {
                    Comm.showWarnDialog(mContext,"请选择物料或者扫码条码！")
                    return
                }
                run_save()
            }
            R.id.btn_submit -> { // 提交到k3
                if(!checkProject()) return
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

    /**
     * 检查方案是否有值
     */
    fun checkProject() : Boolean {
        val project :String = getValues(tv_icStockCheckProcess)
        if(project.length == 0 || icStockCheckProcess == null) {
            Comm.showWarnDialog(mContext,"请选择方案！")
            return false
        }
        if(icStockCheckProcess!!.stock == null) {
            Comm.showWarnDialog(mContext, "请选择仓库！")
            return false
        }
        return true
    }

    override fun setListener() {
        val click = View.OnClickListener { v ->
            setFocusable(et_getFocus)
            when (v.id) {
                R.id.et_code -> setFocusable(et_code)
            }
        }
        et_code!!.setOnClickListener(click)

        // 物料
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

        // 长按输入条码
        et_code!!.setOnLongClickListener {
            showInputDialog("输入条码号", "", "none", WRITE_CODE)
            true
        }

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

    /**
     * 查询历史记录
     */
    fun findFun() {
        if(!checkProject()) return
        if(parent!!.isChange) {
            Comm.showWarnDialog(mContext,"请先保存当前数据！")
            return;
        }
        run_findListByParamWms()
    }

    private fun reset() {
        setEnables(et_code, R.color.transparent, true)
        et_code!!.setText("")
        barcode = null
        parent!!.isChange = false
        checkDatas.clear()
        mHandler.sendEmptyMessageDelayed(SETFOCUS, 200)

        mAdapter!!.notifyDataSetChanged()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            SEL_PROJECT -> {// 查询盘点方案	返回
                if (resultCode == Activity.RESULT_OK) {
                    icStockCheckProcess = data!!.getSerializableExtra("obj") as ICStockCheckProcess
                    tv_icStockCheckProcess.text = icStockCheckProcess!!.fprocessId
                    // 当选择不同的方案时，要清空列表
                    if (getValues(tv_icStockCheckProcess).length > 0 && !getValues(tv_icStockCheckProcess).equals(icStockCheckProcess!!.fprocessId)) {
                        checkDatas.clear()
                        mAdapter!!.notifyDataSetChanged()
                    }

                    if (icStockCheckProcess!!.stock == null) {
                        lin_stock.visibility = View.VISIBLE
                        tv_stockSel.text = ""
                        tv_stockName.text = "仓库:"

                    } else {
                        lin_stock.visibility = View.GONE
                        tv_stockSel.text = icStockCheckProcess!!.stock.fname
                        tv_stockName.text = icStockCheckProcess!!.stock.fname
                    }
                }
            }
            SEL_STOCK -> {// 仓库	返回
                if (resultCode == Activity.RESULT_OK) {
                    val stock = data!!.getSerializableExtra("obj") as Stock
                    tv_stockSel.text = stock!!.fname
                    icStockCheckProcess!!.stock = stock

                    lin_stock.visibility = View.GONE
                    tv_stockSel.text = stock.fname
                    tv_stockName.text = stock.fname
                }
            }
            SEL_MTL -> {//查询物料	返回
                if (resultCode == Activity.RESULT_OK) {
                    val list = data!!.getSerializableExtra("obj") as List<ICInvBackup>
                    if (list[0].finterId == 0) { // 纯物料
                        list.forEach {
                            it.finterId = icStockCheckProcess!!.fid
                            it.stockId = icStockCheckProcess!!.stock.fitemId
                            it.stockName = icStockCheckProcess!!.stock.fname
                        }
                    }
                    getMtlAfter(list)
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
            WRITE_CODE -> {// 输入条码返回
                if (resultCode == Activity.RESULT_OK) {
                    val bundle = data!!.extras
                    if (bundle != null) {
                        val value = bundle.getString("resultValue", "")
                        et_code!!.setText(value.toUpperCase())
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
    private fun getMtlAfter(list: List<ICInvBackup>) {
        parent!!.isChange = true
        // 循环判断业务
        for (icInvBackup in list) {
            // 填充数据
            val size = checkDatas.size
            var addRow = true
            var curPosition = 0
            for (i in 0 until size) {
                val sr = checkDatas.get(i)
                // 有相同的，就不新增了
                if (sr.mtlId == icInvBackup.mtlId) {
                    curPosition = i
                    addRow = false
                    break
                }
            }
            if (addRow) {
                icInvBackup.realQty = 1.0
                icInvBackup.createUserId = user!!.id
                checkDatas.add(icInvBackup)

            } else {
                // 已有相同物料行，就叠加数量
                val fqty = checkDatas[curPosition].realQty
                val addVal = BigdecimalUtil.add(fqty, 1.0);
                checkDatas[curPosition].realQty = addVal
            }
        }

        mAdapter!!.notifyDataSetChanged()
    }

    /**
     * 扫码查询对应的方法
     */
    private fun run_okhttpDatas() {
        isTextChange = false
        showLoadDialog("加载中...", false)
        val mUrl = getURL("icInvBackup/findBarcode")
        val formBody = FormBody.Builder()
                .add("finterId", if (icStockCheckProcess != null) icStockCheckProcess!!.fid.toString() else "") // 方案id
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
    private fun run_save() {
        showLoadDialog("保存中...", false)

        val mUrl = getURL("icInvBackup/save")
        val mJson = JsonUtil.objectToString(checkDatas)
        val formBody = FormBody.Builder()
                .add("strJson", mJson)
                .add("callType", "1") // 执行的存储过程是哪个（1：callTmpICInvBackup_To_ICInvBackup，2：callTmpICInvBackup_To_ICInvBackup2）
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