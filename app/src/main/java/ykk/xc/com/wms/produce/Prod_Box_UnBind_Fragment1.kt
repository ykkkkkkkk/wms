package ykk.xc.com.wms.produce

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
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
import kotlinx.android.synthetic.main.prod_box_unbind_fragment1.*
import okhttp3.*
import ykk.xc.com.wms.R
import ykk.xc.com.wms.bean.BoxBarCode
import ykk.xc.com.wms.bean.MaterialBinningRecord
import ykk.xc.com.wms.bean.MaterialBinningRecord_Barcode
import ykk.xc.com.wms.bean.User
import ykk.xc.com.wms.comm.BaseFragment
import ykk.xc.com.wms.comm.Comm
import ykk.xc.com.wms.produce.adapter.Prod_Box_UnBind_Fragment1_Adapter
import ykk.xc.com.wms.util.JsonUtil
import ykk.xc.com.wms.util.LogUtil
import ykk.xc.com.wms.util.zxing.android.CaptureActivity
import java.io.IOException
import java.lang.ref.WeakReference
import java.text.DecimalFormat
import java.util.concurrent.TimeUnit

/**
 * 日期：2019-10-16 09:50
 * 描述：开箱取货
 * 作者：ykk
 */
class Prod_Box_UnBind_Fragment1 : BaseFragment() {

    companion object {
        private val SUCC1 = 200
        private val UNSUCC1 = 500
        private val MODIFY = 201
        private val UNMODIFY = 501
        private val MODIFY_QTY = 202
        private val UNMODIFY_QTY = 502
        private val DELETE = 203
        private val UNDELETE = 503

        private val SETFOCUS = 1
        private val SAOMA = 2
        private val RESULT_NUM = 3
        private val WRITE_CODE = 4
        private val RESULT_WEIGHT = 5
        private val RESULT_VOLUME = 6
    }
    private val context = this
    private var okHttpClient: OkHttpClient? = null
    private var checkDatas = ArrayList<MaterialBinningRecord>()
    private var mAdapter: Prod_Box_UnBind_Fragment1_Adapter? = null
    private var mContext: Activity? = null
    private var user: User? = null
    private var boxBarCode:BoxBarCode? = null
    private var boxBarCode2:BoxBarCode? = null
    private val df = DecimalFormat("#.######")
    private var parent: Prod_Box_UnBind_MainActivity? = null
    private var isTextChange: Boolean = false // 是否进入TextChange事件
    private var timesTamp:String? = null // 时间戳
    private var smqFlag = '1' // 扫描类型1：箱子扫描，2：物料扫描
    private var curPos:Int = -1 // 当前行
    private var mbrBarcode:MaterialBinningRecord_Barcode?  = null

    // 消息处理
    private val mHandler = MyHandler(this)
    private class MyHandler(activity: Prod_Box_UnBind_Fragment1) : Handler() {
        private val mActivity: WeakReference<Prod_Box_UnBind_Fragment1>

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
                            '1'-> { // 箱码
                                val list = JsonUtil.strToList(msgObj, BoxBarCode::class.java)
                                if(list.size == 2) { // 最上级箱子
                                    m.boxBarCode2 = list[1]
                                    m.tv_boxName0.text = m.boxBarCode2!!.box.boxName
                                    m.tv_boxSize0.text = m.boxBarCode2!!.box.boxSize
                                    m.btn_save.visibility = View.VISIBLE
                                } else {
                                    m.boxBarCode2 = null
                                    m.tv_boxName0.text = ""
                                    m.tv_boxSize0.text = ""
                                    m.btn_save.visibility = View.GONE
                                }
                                m.checkDatas.clear()
                                m.getBoxBarcode(list[0])
                            }
                            '2'-> { // 物料
                                m.mbrBarcode = JsonUtil.strToObject(msgObj, MaterialBinningRecord_Barcode::class.java)
                                m.checkDatas.forEach {
                                    if (m.mbrBarcode!!.parentId == it.id) {
                                        m.run_modifyQtyByUnBind(it.id, m.boxBarCode!!.id, m.mbrBarcode!!.fqty)
                                        return
                                    }
                                }
                            }
                        }
                    }
                    UNSUCC1 -> { // 扫码失败
                        errMsg = JsonUtil.strToString(msgObj)
                        if (m.isNULLS(errMsg).length == 0) errMsg = "很抱歉，没有找到数据！"
                        Comm.showWarnDialog(m.mContext, errMsg)
                    }
                    MODIFY -> { // 整箱取出成功 进入
                        m.reset()
                        m.toasts("操作成功")
                    }
                    UNMODIFY -> { // 整箱取出失败
                        errMsg = JsonUtil.strToString(msgObj)
                        if (m.isNULLS(errMsg).length == 0) errMsg = "操作失败！"
                        Comm.showWarnDialog(m.mContext, errMsg)
                    }
                    MODIFY_QTY -> { // 取出数量成功 进入
                        val barcode = m.getValues(m.et_boxCode)
                        m.reset()
                        m.toasts("操作成功")
                        m.setTexts(m.et_boxCode, barcode)
                    }
                    UNMODIFY_QTY -> { // 取出数量失败
                        errMsg = JsonUtil.strToString(msgObj)
                        if (m.isNULLS(errMsg).length == 0) errMsg = "操作失败！"
                        Comm.showWarnDialog(m.mContext, errMsg)
                    }
                    DELETE -> { // 删除行 进入
                        m.checkDatas.removeAt(m.curPos)
                        m.mAdapter!!.notifyDataSetChanged()
                    }
                    UNDELETE -> { // 删除行  失败
                        Comm.showWarnDialog(m.mContext,"删除出错！")
                    }
                    SETFOCUS -> { // 当弹出其他窗口会抢夺焦点，需要跳转下，才能正常得到值
                        m.setFocusable(m.et_getFocus)
                        when(m.smqFlag) {
                            '1'-> m.setFocusable(m.et_boxCode)
                            '2'-> m.setFocusable(m.et_mtlCode)
                        }
                    }
                    SAOMA -> { // 扫码之后
                        when(m.smqFlag) {
//                            '1' -> {
//                                if(m.boxBarCode != null && m.boxBarCode!!.status != 2 && m.checkDatas.size > 0) {
//                                    Comm.showWarnDialog(m.mContext,"请先保存当前数据！")
//                                    m.isTextChange = false
//                                    return
//                                }
//                            }
                            '2' -> {
                                if(m.boxBarCode == null) {
                                    Comm.showWarnDialog(m.mContext,"请先扫描箱码！")
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

    override fun setLayoutResID(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.prod_box_unbind_fragment1, container, false)
    }

    override fun initView() {
        mContext = getActivity()
        parent = mContext as Prod_Box_UnBind_MainActivity

        recyclerView.addItemDecoration(DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL))
        recyclerView.layoutManager = LinearLayoutManager(mContext)
        mAdapter = Prod_Box_UnBind_Fragment1_Adapter(mContext!!, checkDatas)
        recyclerView.adapter = mAdapter
        // 设值listview空间失去焦点
        recyclerView.isFocusable = false

        // 行事件
        mAdapter!!.setCallBack(object : Prod_Box_UnBind_Fragment1_Adapter.MyCallBack {
            override fun onClickNum(entity: MaterialBinningRecord, position: Int) {
                curPos = position
                showInputDialog("数量", entity.fqty.toString(), "0.0", RESULT_NUM)
            }
            override fun onDelete(entity: MaterialBinningRecord, position: Int) {
                curPos = position
                val build = AlertDialog.Builder(mContext)
                build.setIcon(R.drawable.caution)
                build.setTitle("系统提示")
                build.setMessage("是否删除选中数据？")
                build.setPositiveButton("是") { dialog, which -> run_removeRow(entity) }
                build.setNegativeButton("否", null)
                build.setCancelable(false)
                build.show()

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
        timesTamp = user!!.getId().toString() + "-" + Comm.randomUUID()
        hideSoftInputMode(mContext, et_boxCode)
        hideSoftInputMode(mContext, et_mtlCode)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            mHandler.sendEmptyMessageDelayed(SETFOCUS, 200)
        }
    }

    @OnClick( R.id.btn_scan, R.id.btn_scanMtl, R.id.btn_save, R.id.btn_clone,
            R.id.tv_realWeight, R.id.tv_realVolume)
    fun onViewClicked(view: View) {
        when (view.id) {
            R.id.btn_scan -> { // 调用摄像头扫描（箱码）
                smqFlag = '1'
                showForResult(CaptureActivity::class.java, BaseFragment.CAMERA_SCAN, null)
            }
            R.id.btn_scanMtl -> { // 调用摄像头扫描（物料）
                smqFlag = '2'
                showForResult(CaptureActivity::class.java, BaseFragment.CAMERA_SCAN, null)
            }
            R.id.tv_realWeight -> { // 重量
                showInputDialog("重量", getValues(tv_realWeight), "0.0", RESULT_WEIGHT)
            }
            R.id.tv_realVolume -> { // 实际体积
                showInputDialog("体积", getValues(tv_realVolume), "none", RESULT_VOLUME)
            }
            R.id.btn_save -> { // 保存
                if(!checkSave()) return
                // 保存的时候把实际体重，体积记录，新生成箱子用
                run_modifyByUnBind(boxBarCode!!.id)
            }
            R.id.btn_clone -> { // 重置
                reset()
            }
        }
    }

    /**
     * 检查数据
     */
    fun checkSave() : Boolean {
        if(boxBarCode == null) {
            Comm.showWarnDialog(mContext,"请扫描箱码！")
            return false
        }
        if(checkDatas.size == 0) {
            Comm.showWarnDialog(mContext,"请扫描装了物料的箱子！")
            return false
        }
        return true;
    }

    override fun setListener() {
        val click = View.OnClickListener { v ->
            setFocusable(et_getFocus)
            when (v.id) {
                R.id.et_boxCode -> setFocusable(et_boxCode)
                R.id.et_mtlCode -> setFocusable(et_mtlCode)
            }
        }
        et_boxCode!!.setOnClickListener(click)
        et_mtlCode!!.setOnClickListener(click)

        // 箱码---数据变化
        et_boxCode!!.addTextChangedListener(object : TextWatcher {
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
        // 箱码---长按输入条码
        et_boxCode!!.setOnLongClickListener {
            smqFlag = '1'
            showInputDialog("输入条码", getValues(et_boxCode), "none", WRITE_CODE)
            true
        }
        // 箱码---焦点改变
        et_boxCode.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if(hasFocus) {
                lin_focusBox.setBackgroundResource(R.drawable.back_style_red_focus)
            } else {
                if (lin_focusBox != null) {
                    lin_focusBox!!.setBackgroundResource(R.drawable.back_style_gray4)
                }
            }
        }

        // 物料---数据变化
        et_mtlCode!!.addTextChangedListener(object : TextWatcher {
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
        et_mtlCode!!.setOnLongClickListener {
            smqFlag = '2'
            showInputDialog("输入条码号", getValues(et_mtlCode), "none", WRITE_CODE)
            true
        }
        // 物料---焦点改变
        et_mtlCode.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if(hasFocus) {
                lin_focusMtl.setBackgroundResource(R.drawable.back_style_red_focus)
            } else {
                if (lin_focusMtl != null) {
                    lin_focusMtl!!.setBackgroundResource(R.drawable.back_style_gray4)
                }
            }
        }
    }

    /**
     * 0：表示点击重置，1：表示保存后重置
     */
    private fun reset() {
        et_boxCode.setText("")
        et_mtlCode.setText("")
        tv_boxName0.text = ""
        tv_boxSize0.text = ""
        tv_boxName.text = ""
        tv_boxSize.text = ""
        tv_realWeight.text = ""
        tv_realVolume.text = ""

        boxBarCode = null
        boxBarCode2 = null
        mbrBarcode = null
        checkDatas.clear()
        mAdapter!!.notifyDataSetChanged()
        timesTamp = user!!.getId().toString() + "-" + Comm.randomUUID()
        parent!!.isChange = false

        smqFlag = '1'
        mHandler.sendEmptyMessageDelayed(SETFOCUS, 200)
    }

    fun getBoxBarcode(m : BoxBarCode) {
        boxBarCode = m

        tv_boxName.text = m.box.boxName
        tv_boxSize.text = m.box.boxSize
        if(m.listMbr != null && m.listMbr.size > 0) {
            checkDatas.addAll(m.listMbr)
            checkDatas.forEachIndexed { index, it ->
                it.rowNo = (index+1) // 自动算出行号
                it.createUserName = user!!.username
            }
            mAdapter!!.notifyDataSetChanged()
        }
        smqFlag = '2'
        mHandler.sendEmptyMessageDelayed(SETFOCUS,200)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RESULT_NUM -> { // 数量	返回
                if (resultCode == Activity.RESULT_OK) {
                    val bundle = data!!.getExtras()
                    if (bundle != null) {
                        val value = bundle.getString("resultValue", "")
                        var num = parseDouble(value)
                        run_modifyQtyByUnBind(checkDatas[curPos].id, checkDatas[curPos].boxBarCodeId, num)

                        mAdapter!!.notifyDataSetChanged()
                    }
                }
            }
            BaseFragment.CAMERA_SCAN -> {// 扫一扫成功  返回
                if (resultCode == Activity.RESULT_OK) {
                    val bundle = data!!.extras
                    if (bundle != null) {
                        val code = bundle.getString(BaseFragment.DECODED_CONTENT_KEY, "")
                        when(smqFlag) {
                            '1' -> setTexts(et_boxCode, code)
                            '2' -> setTexts(et_mtlCode, code)
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
                            '1' -> setTexts(et_boxCode, value.toUpperCase())
                            '2' -> setTexts(et_mtlCode, value.toUpperCase())
                        }
                    }
                }
            }
            RESULT_WEIGHT -> {// 重量  返回
                if (resultCode == Activity.RESULT_OK) {
                    val bundle = data!!.extras
                    if (bundle != null) {
                        val value = bundle.getString("resultValue", "")
                        tv_realWeight.text  = df.format(parseDouble(value))
                    }
                }
            }
            RESULT_VOLUME -> {// 体积  返回
                if (resultCode == Activity.RESULT_OK) {
                    val bundle = data!!.extras
                    if (bundle != null) {
                        val value = bundle.getString("resultValue", "")
                        if(value.length > 46) {
                            Comm.showWarnDialog(mContext, "输入的体积超出了字符串长度（46个字符）！")
                            return
                        }
                        tv_realVolume.text = value
                    }
                }
            }
        }
        mHandler.sendEmptyMessageDelayed(SETFOCUS, 200)
    }

    /**
     * 扫码查询对应的方法
     */
    private fun run_smDatas() {
        isTextChange = false
        showLoadDialog("加载中...", false)
        var mUrl:String? = null
        var barcode:String? = null
        var type:String? = null
        var boxBarCodeId:String? = null
        when(smqFlag) {
            '1' -> { // 箱码
                mUrl = getURL("boxBarCode/findBarcodeByUnBind")
                barcode = getValues(et_boxCode)
                type = "1" // 生产装箱
                boxBarCodeId = ""
            }
            '2' -> { // 物料
                mUrl = getURL("materialBinningRecord/findByBarcode")
                barcode = getValues(et_mtlCode)
                type = ""
                boxBarCodeId = if(boxBarCode != null) boxBarCode!!.id.toString() else ""
            }
        }
        val formBody = FormBody.Builder()
                .add("barcode", barcode)
                .add("type", type)
                .add("mbrType", "1")
                .add("boxBarCodeId", boxBarCodeId)
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
     * 修改解绑数量
     */
    private fun run_modifyQtyByUnBind(id:Int, boxBarCodeId:Int, fqty:Double) {
        showLoadDialog("保存中...", false)
        var mUrl = getURL("materialBinningRecord/modifyQtyByUnBind")
        val formBody = FormBody.Builder()
                .add("id", id.toString())
                .add("fqty", fqty.toString())
                .add("boxBarCodeId", boxBarCodeId.toString())
                .add("mbrBarcodeId", if(mbrBarcode != null) mbrBarcode!!.id.toString() else "")
                .build()

        val request = Request.Builder()
                .addHeader("cookie", getSession())
                .url(mUrl)
                .post(formBody)
                .build()

        val call = okHttpClient!!.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                mHandler.sendEmptyMessage(UNMODIFY_QTY)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()
                val result = body.string()
                if (!JsonUtil.isSuccess(result)) {
                    val msg = mHandler.obtainMessage(UNMODIFY_QTY, result)
                    mHandler.sendMessage(msg)
                    return
                }
                val msg = mHandler.obtainMessage(MODIFY_QTY, result)
                LogUtil.e("run_modifyQtyByUnBind --> onResponse", result)
                mHandler.sendMessage(msg)
            }
        })
    }

    /**
     * 修改整箱取出
     */
    private fun run_modifyByUnBind(boxBarCodeId:Int) {
        showLoadDialog("执行中...", false)
        var mUrl = getURL("materialBinningRecord/modifyByUnBind")
        val formBody = FormBody.Builder()
                .add("type", "1") // 生产装箱
                .add("boxBarCodeId", boxBarCodeId.toString())
                .build()

        val request = Request.Builder()
                .addHeader("cookie", getSession())
                .url(mUrl)
                .post(formBody)
                .build()

        val call = okHttpClient!!.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                mHandler.sendEmptyMessage(UNMODIFY)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()
                val result = body.string()
                if (!JsonUtil.isSuccess(result)) {
                    val msg = mHandler.obtainMessage(UNMODIFY, result)
                    mHandler.sendMessage(msg)
                    return
                }
                val msg = mHandler.obtainMessage(MODIFY, result)
                LogUtil.e("run_modifyByUnBind --> onResponse", result)
                mHandler.sendMessage(msg)
            }
        })
    }

    /**
     * 删除行
     */
    private fun run_removeRow(mbr : MaterialBinningRecord) {
        showLoadDialog("保存中...", false)
        var mUrl = getURL("materialBinningRecord/removeRow")
        var mJson = JsonUtil.objectToString(checkDatas)
        val formBody = FormBody.Builder()
                .add("id", mbr.id.toString())
                .add("boxBarCodeId", mbr.boxBarCodeId.toString())
                .build()

        val request = Request.Builder()
                .addHeader("cookie", getSession())
                .url(mUrl)
                .post(formBody)
                .build()

        val call = okHttpClient!!.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                mHandler.sendEmptyMessage(UNDELETE)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()
                val result = body.string()
                if (!JsonUtil.isSuccess(result)) {
                    val msg = mHandler.obtainMessage(UNDELETE, result)
                    mHandler.sendMessage(msg)
                    return
                }
                val msg = mHandler.obtainMessage(DELETE, result)
                LogUtil.e("run_removeRow --> onResponse", result)
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