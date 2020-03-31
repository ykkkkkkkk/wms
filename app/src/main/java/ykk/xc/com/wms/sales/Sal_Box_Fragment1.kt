package ykk.xc.com.wms.sales

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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.OnClick
import kotlinx.android.synthetic.main.sal_box_fragment1.*
import okhttp3.*
import ykk.xc.com.wms.R
import ykk.xc.com.wms.basics.BatchAndNumInputDialog
import ykk.xc.com.wms.basics.Box_DialogActivity
import ykk.xc.com.wms.basics.ExpressCompany_DialogActivity
import ykk.xc.com.wms.bean.*
import ykk.xc.com.wms.bean.k3Bean.ExpressCompany
import ykk.xc.com.wms.bean.k3Bean.ICItem
import ykk.xc.com.wms.comm.BaseFragment
import ykk.xc.com.wms.comm.Comm
import ykk.xc.com.wms.sales.adapter.Sal_Box_Fragment1_Adapter
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
 * 描述：销售装箱
 * 作者：ykk
 */
class Sal_Box_Fragment1 : BaseFragment() {

    companion object {
        private val SEL_BOX = 60
        private val SEL_MTL = 61
        private val SEL_EXPRESS = 62
        private val SUCC1 = 200
        private val UNSUCC1 = 500
        private val SAVE = 201
        private val UNSAVE = 501
        private val DELETE = 202
        private val UNDELETE = 502
        private val BOX_STATUS = 203
        private val UNBOX_STATUS = 503
        private val FIND_SOURCE = 204
        private val UNFIND_SOURCE = 504
        private val UPLOAD = 205
        private val UNUPLOAD = 505

        private val SETFOCUS = 1
        private val SAOMA = 2
        private val RESULT_NUM = 3
        private val WRITE_CODE = 4
        private val RESULT_WEIGHT = 5
        private val RESULT_VOLUME = 6
        private val RESULT_BATCH_NUM = 7
    }
    private val context = this
    private var okHttpClient: OkHttpClient? = null
    private var checkDatas = ArrayList<MaterialBinningRecord>()
    private var mAdapter: Sal_Box_Fragment1_Adapter? = null
    private var mContext: Activity? = null
    private var user: User? = null
    private var boxBarCode:BoxBarCode? = null
    private var boxBarCode2:BoxBarCode? = null
    private val df = DecimalFormat("#.######")
    private var parent: Sal_Box_MainActivity? = null
    private var isTextChange: Boolean = false // 是否进入TextChange事件
    private var timesTamp:String? = null // 时间戳
    private var smqFlag = '3' // 扫描类型1：箱子扫描，2：上级箱子扫描，3：物料扫描
    private var curPos:Int = -1 // 当前行
    private var modifyBoxStatus = 1 // 开箱还是封箱
    private var autoSave = false // 点击封箱自动保存
    private var autoSaveAferBarcode:String? = null
    private var curBoxStatus = 0   // 记录当前扫描箱码的状态
    private var listIcItem :List<ICItem>? = null  // 记录当前物料扫描返回的对象
    private var icstockBillId = 0   // 销售装箱传来的值
    private var isExpand = false    // 是否全屏查看
    private var expressCompany:ExpressCompany? = null // 快递公司
    private var missionBillId = 0   // 上个页面任务单id
    private var expressNo = StringBuffer()

    // 消息处理
    private val mHandler = MyHandler(this)
    private class MyHandler(activity: Sal_Box_Fragment1) : Handler() {
        private val mActivity: WeakReference<Sal_Box_Fragment1>

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
                                val boxBarCode = JsonUtil.strToObject(msgObj, BoxBarCode::class.java)
                                // 扫描重复的箱子
                                if(m.boxBarCode != null && boxBarCode.id == m.boxBarCode!!.id) {
                                    return
                                }
                                // 如果第一次的箱子是封箱，那么第二次的箱子也是封箱，否则提示
                                if(m.boxBarCode != null && boxBarCode.status != 2) {
                                    Comm.showWarnDialog(m.mContext,"请扫描已封箱的箱子！")
                                    return
                                }
                                // 如果有扫描物料列表在前，箱码在后，那么扫描箱码必须是空箱子
//                                if(m.boxBarCode == null && m.checkDatas.size > 0 && boxBarCode.listMbr.size > 0) {
//                                    Comm.showWarnDialog(m.mContext,"请扫描未使用的箱子！")
//                                    return
//                                }
                                if(m.autoSaveAferBarcode != null) {
                                    m.reset(0)
                                }
                                m.getBoxBarcode(boxBarCode)
                            }
                            '2'-> { // 上级箱码
                                m.boxBarCode2 = JsonUtil.strToObject(msgObj, BoxBarCode::class.java)
                                m.getBoxBarcode2(m.boxBarCode2!!)
                            }
                            '3'-> { // 物料
                                m.listIcItem = JsonUtil.strToList(msgObj, ICItem::class.java)
                                // 启用批次号就弹出输入批次
                                if(m.listIcItem!!.size == 1 && m.listIcItem!![0].batchManager.equals("Y")) {
                                    var fqty = 0.0
                                    for ((index, it) in m.checkDatas.withIndex()) {
                                        if(m.listIcItem!![0].fitemid == it.fitemId) {
                                            fqty = it.fsourceQty
                                            break
                                        }
                                    }
                                    val bundle = Bundle()
                                    bundle.putString("mtlName", m.listIcItem!![0].fname)
                                    bundle.putString("batchCode", m.listIcItem!![0].batchCode)
                                    bundle.putDouble("fqty", fqty)
                                    m.showForResult(BatchAndNumInputDialog::class.java, RESULT_BATCH_NUM, bundle)

                                } else {
                                    m.getMaterial(m.listIcItem!!)
                                }
                            }
                        }
                    }
                    UNSUCC1 -> { // 扫码失败
                        errMsg = JsonUtil.strToString(msgObj)
                        if (m.isNULLS(errMsg).length == 0) errMsg = "很抱歉，没有找到数据！"
                        Comm.showWarnDialog(m.mContext, errMsg)
                    }
                    SAVE -> { // 保存成功 进入
                        m.missionBillId = 0
                        m.autoSaveAferBarcode = JsonUtil.strToString(msgObj)
                        if(m.autoSave) {
                            m.run_modifyStatus(m.modifyBoxStatus.toString())
                        } else {
//                            m.reset(1)
                            m.smqFlag = '1'
                            m.boxBarCode = null
                            m.setTexts(m.et_boxCode, m.autoSaveAferBarcode)
                        }
                        m.toasts("保存成功")
                    }
                    UNSAVE -> { // 保存失败
                        errMsg = JsonUtil.strToString(msgObj)
                        if (m.isNULLS(errMsg).length == 0) errMsg = "保存失败！"
                        Comm.showWarnDialog(m.mContext, errMsg)
                    }
                    DELETE -> { // 删除行 进入
                        m.checkDatas.removeAt(m.curPos)
                        m.mAdapter!!.notifyDataSetChanged()
                    }
                    UNDELETE -> { // 删除行  失败
                        Comm.showWarnDialog(m.mContext,"删除出错！")
                    }
                    BOX_STATUS -> { // 开箱和封箱 进入
                        if(m.boxBarCode != null) {
                            m.boxBarCode!!.status = m.modifyBoxStatus // 更新箱子状态
                            m.checkDatas.forEach {
                                it.boxBarCode.status = m.modifyBoxStatus
                            }
                            m.getBoxBarcode_status(m.modifyBoxStatus, true)
                        }
                        if(m.autoSave) {
                            m.smqFlag = '1'
                            m.boxBarCode = null
                            m.setTexts(m.et_boxCode, m.autoSaveAferBarcode)
                        }
                    }
                    UNBOX_STATUS -> { // 开箱和封箱  失败
                        Comm.showWarnDialog(m.mContext,"操作出错！")
                    }
                    FIND_SOURCE ->{ // 查询源单 返回
                        val list = JsonUtil.strToList(msgObj, ICStockBillEntry::class.java)
                        list.forEach {
                            m.addRow(it)
                        }
                        m.mAdapter!!.notifyDataSetChanged()
                    }
                    UNFIND_SOURCE ->{ // 查询源单失败！ 返回
                        m.toasts("该页面有错误！2秒后自动关闭...")
                        m.mHandler.postDelayed(Runnable {
                            m.mContext!!.finish()
                        },2000)
                    }
                    UPLOAD -> { // 上传单据 进入
                        m.lin_button.visibility = View.GONE
                        m.toasts("上传成功")
//                        m.parent!!.finish()
                    }
                    UNUPLOAD -> { // 上传单据  失败
                        errMsg = JsonUtil.strToString(msgObj)
                        if (m.isNULLS(errMsg).length == 0) errMsg = "服务器繁忙，请稍后再试！"
                        Comm.showWarnDialog(m.mContext, errMsg)
                    }
                    SETFOCUS -> { // 当弹出其他窗口会抢夺焦点，需要跳转下，才能正常得到值
                        m.setFocusable(m.et_getFocus)
                        when(m.smqFlag) {
                            '1'-> m.setFocusable(m.et_boxCode)
                            '2'-> m.setFocusable(m.et_boxCode2)
                            '3'-> m.setFocusable(m.et_mtlCode)
                        }
                    }
                    SAOMA -> { // 扫码之后
                        when(m.smqFlag) {
                            '1' -> {
                                if(m.boxBarCode != null && m.boxBarCode!!.status != 2 && m.checkDatas.size > 0) {
                                    Comm.showWarnDialog(m.mContext,"请先保存当前数据！")
                                    m.isTextChange = false
                                    return
                                }
                            }
                            '2' -> {
                                if(m.boxBarCode == null) {
                                    Comm.showWarnDialog(m.mContext,"请先扫描箱码！")
                                    m.isTextChange = false
                                    return
                                }
                                if(m.boxBarCode != null && m.boxBarCode!!.listMbr == null) {
                                    Comm.showWarnDialog(m.mContext,"请扫描物料条码！")
                                    m.isTextChange = false
                                    return
                                }
                            }
                            '4'-> {
                                val value = m.getValues(m.et_expressCode)
                                if(m.cb_expressNo.isChecked) {
                                    m.expressNo.append( (if(m.expressNo.length > 0) "，" else "") + value)
                                } else {
                                    m.expressNo.setLength(0)
                                    m.expressNo.append(value)
                                }
                                m.setTexts(m.et_expressCode, m.expressNo.toString())
                                m.isTextChange = false
                                return
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
        return inflater.inflate(R.layout.sal_box_fragment1, container, false)
    }

    override fun initView() {
        mContext = getActivity()
        parent = mContext as Sal_Box_MainActivity

        recyclerView.addItemDecoration(DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL))
        recyclerView.layoutManager = LinearLayoutManager(mContext)
        mAdapter = Sal_Box_Fragment1_Adapter(mContext!!, checkDatas)
        recyclerView.adapter = mAdapter
        // 设值listview空间失去焦点
        recyclerView.isFocusable = false

        // 行事件
        mAdapter!!.setCallBack(object : Sal_Box_Fragment1_Adapter.MyCallBack {
            override fun onClickNum(entity: MaterialBinningRecord, position: Int) {
                // 已出入库，不能修改数据
                if((entity.boxBarCode != null && entity.boxBarCode.status == 2) || (boxBarCode != null && entity.boxBarCodeId != boxBarCode!!.id) ) return

                curPos = position
                showInputDialog("数量", entity.fqty.toString(), "0.0", RESULT_NUM)
            }
            override fun onDelete(entity: MaterialBinningRecord, position: Int) {
                // 已出入库，不能修改数据
                if((entity.boxBarCode != null && entity.boxBarCode.status == 2) || (boxBarCode != null && entity.boxBarCodeId != boxBarCode!!.id) ) return

                curPos = position
                // 还没保存的行，就直接删除
                if(entity.id == 0) {
                    checkDatas.removeAt(position)
                    checkDatas.forEachIndexed { index, it ->
                        it.rowNo = (index+1) // 自动算出行号
                    }
                    mAdapter!!.notifyDataSetChanged()

                } else {
                    val build = AlertDialog.Builder(mContext)
                    build.setIcon(R.drawable.caution)
                    build.setTitle("系统提示")
                    build.setMessage("是否删除选中数据？")
                    build.setPositiveButton("是") { dialog, which -> run_removeRow(entity) }
                    build.setNegativeButton("否", null)
                    build.setCancelable(false)
                    build.show()
                }

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
        hideSoftInputMode(mContext, et_boxCode2)
        hideSoftInputMode(mContext, et_mtlCode)
        hideSoftInputMode(mContext, et_expressCode)
        bundle()
    }

    private fun bundle() {
        val bundle = mContext!!.intent.extras
        if(bundle != null) {
            // 任务单点击过来的
            if(bundle.containsKey("missionBill")) {
                val missionBill = bundle.getSerializable("missionBill") as MissionBill
                missionBillId = missionBill.id
                icstockBillId = missionBill.icstockBillId
                run_findICStockBillEntryList()

            } else if(bundle.containsKey("boxBarcode")) { // 查询过来的
                val boxBarcode = bundle.getString("boxBarcode")
                mHandler.postDelayed(Runnable {
                    setTexts(et_boxCode, boxBarcode)
                },200)
            }
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            mHandler.sendEmptyMessageDelayed(SETFOCUS, 200)
        }
    }

    @OnClick(R.id.tv_box, R.id.btn_scan, R.id.btn_scan2, R.id.btn_scanMtl, R.id.btn_save, R.id.btn_clone, R.id.btn_expand,
             R.id.tv_realWeight, R.id.tv_realVolume, R.id.btn_openBox, R.id.btn_closeBox, R.id.btn_upload, R.id.tv_expressCompany)
    fun onViewClicked(view: View) {
        when (view.id) {
            R.id.tv_box -> {
                showForResult(Box_DialogActivity::class.java, SEL_BOX, null)
            }
            R.id.btn_scan -> { // 调用摄像头扫描（箱码）
                smqFlag = '1'
                showForResult(CaptureActivity::class.java, BaseFragment.CAMERA_SCAN, null)
            }
            R.id.btn_scan2 -> { // 调用摄像头扫描（上级箱码）
                smqFlag = '2'
                showForResult(CaptureActivity::class.java, BaseFragment.CAMERA_SCAN, null)
            }
            R.id.btn_scanMtl -> { // 调用摄像头扫描（物料）
                smqFlag = '3'
                showForResult(CaptureActivity::class.java, BaseFragment.CAMERA_SCAN, null)
            }
            R.id.btn_expand -> {
                var visibilityId = 0
                if(isExpand) {
                    isExpand = false
                    visibilityId = View.VISIBLE
                } else {
                    isExpand = true
                    visibilityId = View.GONE
                }
                lin_box.visibility = View.GONE
                lin_curBox.visibility = visibilityId
                lin_curBox2.visibility = visibilityId
                lin_parentBox.visibility = visibilityId
                lin_parentBox2.visibility = visibilityId
                lin_expressCompany.visibility = visibilityId
                lin_express.visibility = visibilityId
                lin_weight.visibility = visibilityId
            }
            R.id.tv_expressCompany -> { // 快递公司
                showForResult(ExpressCompany_DialogActivity::class.java, SEL_EXPRESS, null)
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
                checkDatas.forEach {
                    it.expressCompanyId = expressCompany!!.fitemId
                    it.expressCompanyName = expressCompany!!.fname
                    it.expressNo = getValues(et_expressCode).trim()
                }
                run_save()
            }
            R.id.btn_openBox -> { // 开箱
                modifyBoxStatus = 1
                run_modifyStatus(modifyBoxStatus.toString())
            }
            R.id.btn_closeBox -> { // 封箱
                if(checkDatas.size == 0) {
                    Comm.showWarnDialog(mContext,"还没有装入物料，不能封箱！")
                    return
                }
                if(!checkSave()) return

                if(checkDatas[0].boxBarCodeId > 0) {
                    modifyBoxStatus = 2
                    autoSave = true
                }
                run_save()
            }
            R.id.btn_upload -> { // 上传
                run_uploadToK3_XSZX()
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
        if(checkDatas.size == 0) {
            Comm.showWarnDialog(mContext,"请扫描物料条码！")
            return false
        }
        checkDatas.forEachIndexed { index, it ->
            if(it.fqty == 0.0) {
                Comm.showWarnDialog(mContext, "第（"+(index+1)+"）行，请扫描或输入数量！")
                return false
            }
            if(it.fqty < it.fsourceQty) {
                Comm.showWarnDialog(mContext, "第（"+(index+1)+"）行，数量未装完！")
                return false
            }
            if(it.fqty > it.fsourceQty) {
                Comm.showWarnDialog(mContext, "第（"+(index+1)+"）行，装箱数不能大于发货数！")
                return false
            }
        }
        if(checkDatas[0].boxId == 0) {
            lin_box.visibility = View.VISIBLE
            Comm.showWarnDialog(mContext, "请选择包装箱！")
            return false
        }
        if(boxBarCode != null && boxBarCode2 != null && boxBarCode!!.id == boxBarCode2!!.id) {
            Comm.showWarnDialog(mContext, "上级箱码、箱码不能相同！")
            return false
        }
        if(getValues(tv_expressCompany).length == 0) {
            Comm.showWarnDialog(mContext,"请输入货运单位！")
            return false
        }
        if(getValues(et_expressCode).trim().length == 0) {
            Comm.showWarnDialog(mContext,"请扫描或长按快递单号，进行输入！")
            return false
        }
        return true
    }

    /**
     * 选择了物料没有点击保存，点击了重置，需要提示
     */
    fun checkSaveHint() : Boolean {
        if(checkDatas.size > 0) {
            return true
        }
        return false
    }

    fun print() {
        if (boxBarCode == null) {
            Comm.showWarnDialog(mContext, "请先扫描箱码！")
            return
        }
        if (checkDatas == null || checkDatas.size == 0) {
            Comm.showWarnDialog(mContext, "箱子里还没有物料不能打印！")
            return
        }
        checkDatas.forEach {
            if(it.id == 0) {
                Comm.showWarnDialog(mContext,"请先保存当前数据！")
                return
            }
        }
        parent!!.setFragment1Print(checkDatas)
    }

    override fun setListener() {
        val click = View.OnClickListener { v ->
            setFocusable(et_getFocus)
            when (v.id) {
                R.id.et_boxCode -> setFocusable(et_boxCode)
                R.id.et_boxCode2 -> setFocusable(et_boxCode2)
                R.id.et_mtlCode -> setFocusable(et_mtlCode)
            }
        }
        et_boxCode!!.setOnClickListener(click)
        et_boxCode2!!.setOnClickListener(click)
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

        // 上级箱码---数据变化
        et_boxCode2!!.addTextChangedListener(object : TextWatcher {
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
        // 上级箱码---长按输入条码
        et_boxCode2!!.setOnLongClickListener {
            smqFlag = '2'
            showInputDialog("输入条码号", getValues(et_boxCode2), "none", WRITE_CODE)
            true
        }
        // 上级箱码---焦点改变
        et_boxCode2.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if(hasFocus) {
                lin_focusBox2.setBackgroundResource(R.drawable.back_style_red_focus)
            } else {
                if (lin_focusBox2 != null) {
                    lin_focusBox2!!.setBackgroundResource(R.drawable.back_style_gray4)
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
                    smqFlag = '3'
                    mHandler.sendEmptyMessageDelayed(SAOMA, 300)
                }
            }
        })
        // 物料---长按输入条码
        et_mtlCode!!.setOnLongClickListener {
            smqFlag = '3'
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

        // 快递单号---数据变化
        et_expressCode!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (s.length == 0) return
                if (!isTextChange) {
                    isTextChange = true
                    smqFlag = '4'
                    mHandler.sendEmptyMessageDelayed(SAOMA, 300)
                }
            }
        })
        // 快递单号---焦点改变
        et_expressCode.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if(hasFocus) {
                et_expressCode.setBackgroundResource(R.drawable.back_style_red_focus)
            } else {
                if (et_expressCode != null) {
                    et_expressCode!!.setBackgroundResource(R.drawable.back_style_blue)
                }
            }
        }
        // 快递单号---长按输入条码
        et_expressCode!!.setOnLongClickListener {
            smqFlag = '4'
            showInputDialog("输入快递单", "", "none", WRITE_CODE)
            true
        }
    }

    /**
     * 0：表示点击重置，1：表示保存后重置
     */
    private fun reset(flag : Int) {
        tv_box.text = ""
        lin_box.visibility = View.GONE
        if(autoSaveAferBarcode == null) {
            et_boxCode.setText("")
        }
        et_boxCode2.setText("")
        et_mtlCode.setText("")
        tv_boxName.text = ""
        tv_boxSize.text = ""
        tv_boxName2.text = ""
        tv_boxSize2.text = ""
        tv_realWeight.text = ""
        tv_realVolume.text = ""
        tv_custName.text = ""

        modifyBoxStatus = 1
        boxBarCode = null
        boxBarCode2 = null
        checkDatas.clear()
        mAdapter!!.notifyDataSetChanged()
        timesTamp = user!!.getId().toString() + "-" + Comm.randomUUID()
        parent!!.isChange = false
        curBoxStatus = 0
        if(flag == 0) {
            autoSave = false
            autoSaveAferBarcode = null
            getBoxBarcode_status(0, false)
        }

        smqFlag = '3'
        mHandler.sendEmptyMessageDelayed(SETFOCUS, 200)
    }

    fun getBoxBarcode(m : BoxBarCode) {
        boxBarCode = m

        lin_box.visibility = View.GONE
        tv_boxName.text = m.box.boxName
        tv_boxSize.text = m.box.boxSize
        // 先扫描物料，再扫描箱码
        if(checkDatas.size > 0 && checkDatas[0].boxBarCodeId == 0) {
            checkDatas.forEach {
                it.boxId = m.boxId
                it.boxBarCodeId = m.id
                it.boxBarCode = m
            }
        }
        if(m.listMbr != null && m.listMbr.size > 0) {
            tv_custName.text = m.listMbr[0].cust.fname
            checkDatas.addAll(m.listMbr)
            checkDatas.forEachIndexed { index, it ->
                it.rowNo = (index+1) // 自动算出行号
                it.createUserName = user!!.username
            }
            tv_expressCompany.text = m.listMbr[0].expressCompanyName
            et_expressCode.setText(m.listMbr[0].expressNo)
            expressNo.setLength(0)
            expressNo.append(m.listMbr[0].expressNo) // 每次都记录当前值
            mAdapter!!.notifyDataSetChanged()
        }
        if(m.status != 2) {
            smqFlag = '3'
            mHandler.sendEmptyMessageDelayed(SETFOCUS,200)
        }
        tv_realWeight.text = df.format(m.realWeight)
        tv_realVolume.text = m.realVolume
        getBoxBarcode_status(m.status, false)
    }

    private fun getBoxBarcode_status(status : Int, isFocus : Boolean) {
        if(status == 2) { // 封箱
            et_boxCode2.isEnabled = false
            et_mtlCode.isEnabled = false
            btn_scan2.isEnabled = false
            btn_scanMtl.isEnabled = false
            tv_boxStatus.text = Html.fromHtml("<font color='#FF2200'>已封箱</font>")
            btn_openBox.visibility = View.VISIBLE
            btn_closeBox.visibility = View.GONE
            btn_save.visibility = View.GONE
            btn_upload.visibility = View.VISIBLE
            cb_autoCreateCode.isEnabled = false
            cb_autoCreateCode.isChecked = false
            lin_focusBox2.setBackgroundResource(R.drawable.back_style_gray3)
            lin_focusMtl.setBackgroundResource(R.drawable.back_style_gray3)

            // 已经封箱的箱子超过了2个，就可以装到大箱子里
            if(boxBarCode != null && curBoxStatus == 2) {
                var boxBarCodeId = 0
                for ((index, it) in checkDatas.withIndex()) {
                    if (boxBarCodeId > 0 && boxBarCodeId != it.boxBarCodeId) {
                        lin_focusBox2.setBackgroundResource(R.drawable.back_style_gray4)
                        btn_scan2.isEnabled = true
                        et_boxCode2.isEnabled = true
                        btn_openBox.visibility = View.GONE
                        btn_closeBox.visibility = View.GONE
                        btn_save.visibility = View.VISIBLE
                        btn_upload.visibility = View.GONE
                        cb_autoCreateCode.isEnabled = true
                        cb_autoCreateCode.isChecked = false

                        break
                    }
                    boxBarCodeId = it.boxBarCodeId
                }
            }
            smqFlag = '1'
            mHandler.sendEmptyMessageDelayed(SETFOCUS,200)

        } else {
            et_boxCode2.isEnabled = true
            et_mtlCode.isEnabled = true
            btn_scan2.isEnabled = true
            btn_scanMtl.isEnabled = true
            btn_openBox.visibility = View.GONE
            btn_closeBox.visibility = View.VISIBLE
            btn_save.visibility = View.VISIBLE
            btn_upload.visibility = View.GONE
            cb_autoCreateCode.isEnabled = true
            cb_autoCreateCode.isChecked = false
            lin_focusBox2.setBackgroundResource(R.drawable.back_style_gray4)
            lin_focusMtl.setBackgroundResource(R.drawable.back_style_gray4)
            if(checkDatas.size > 0) {
                tv_boxStatus.text = Html.fromHtml("<font color='#009900'>已开箱</font>")
            } else {
                tv_boxStatus.text = "未开箱"
            }
            if(isFocus) {
                smqFlag = '3'
                mHandler.sendEmptyMessageDelayed(SETFOCUS,200)
            }
        }
        curBoxStatus = status
    }

    fun getBoxBarcode2(m : BoxBarCode) {
        tv_boxName2.text = m.box.boxName
        tv_boxSize2.text = m.box.boxSize
//        checkDatas.forEach {
//            it.autoCreateParentBoxBarCodeId = m.id
//            it.parentBoxBarCodeId = m.id
//            it.parentBarcode = m.barCode
//        }
        cb_autoCreateCode.isEnabled = false
        cb_autoCreateCode.isChecked = false
        smqFlag = '3'
        mHandler.sendEmptyMessageDelayed(SETFOCUS,200)
    }

    /**
     *  扫码之后    物料启用批次
     */
    fun setBatchCode(pos : Int, batchCode : String, fqty : Double) {
        val entryBarcode = MaterialBinningRecord_Barcode()
        entryBarcode.parentId = 0
        entryBarcode.barcode = getValues(et_mtlCode)
        entryBarcode.batchCode = batchCode
        entryBarcode.snCode = ""
        entryBarcode.fqty = fqty
        entryBarcode.isUniqueness = 'N'
        entryBarcode.createUserName = user!!.username

        checkDatas[pos].mbrBarcodes.add(entryBarcode)
//        val addVal = BigdecimalUtil.add(checkDatas[pos].fqty, fqty)
//        checkDatas[pos].fqty = addVal
        checkDatas[pos].fqty = fqty
    }

    /**
     *  扫码之后    物料启用序列号
     */
    fun setSnCode(pos : Int, snCode : String, fqty : Double) {
        if(checkDatas[pos].fqty >= checkDatas[pos].fsourceQty) {
            Comm.showWarnDialog(mContext,"第（"+(pos+1)+"）行数量已装完！")
            return
        }
        val entryBarcode = MaterialBinningRecord_Barcode()
        entryBarcode.parentId = 0
        entryBarcode.barcode = getValues(et_mtlCode)
        entryBarcode.batchCode = ""
        entryBarcode.snCode = snCode
        entryBarcode.fqty = fqty
        entryBarcode.isUniqueness = 'Y'
        entryBarcode.createUserName = user!!.username

        checkDatas[pos].mbrBarcodes.add(entryBarcode)
        val addVal = BigdecimalUtil.add(checkDatas[pos].fqty, 1.0)
        checkDatas[pos].fqty = addVal
    }

    /**
     *  扫码之后    物料未启用
     */
    fun unSetBatchOrSnCode(pos : Int, fqty : Double) {
        if(checkDatas[pos].fqty >= checkDatas[pos].fsourceQty) {
            Comm.showWarnDialog(mContext,"第（"+(pos+1)+"）行数量已装完！")
            return
        }
        val entryBarcode = MaterialBinningRecord_Barcode()
        entryBarcode.parentId = 0
        entryBarcode.barcode = getValues(et_mtlCode)
        entryBarcode.batchCode = ""
        entryBarcode.snCode = ""
        entryBarcode.fqty = fqty
        entryBarcode.isUniqueness = 'N'
        entryBarcode.createUserName = user!!.username

        checkDatas[pos].mbrBarcodes.add(entryBarcode)
        val addVal = BigdecimalUtil.add(checkDatas[pos].fqty, fqty)
        checkDatas[pos].fqty = addVal
    }

    /**
     *  物料扫码后处理
     */
    fun getMaterial(listICItem: List<ICItem>) {
        checkDatas.forEachIndexed { index, it ->
//              if(it.icItem.batchManager.equals("Y") || it.icItem.snManager.equals("Y")) {
            if(it.icItem.snManager.equals("Y")) {
                for (it2 in it.mbrBarcodes) {
                    if (getValues(et_mtlCode) == it2.barcode) {
                        Comm.showWarnDialog(mContext, "条码已使用！")
                        return
                    }
                }
            }
        }
        val mapICItem = HashMap<Int, ICItem>()
        listICItem.forEach {
            if(!mapICItem.containsKey(it.fitemid)) {
                mapICItem.put(it.fitemid, it)
            } else {
                val icItem = mapICItem.get(it.fitemid)
                val addVal = BigdecimalUtil.add(icItem!!.realQty, it.realQty)
                icItem.realQty = addVal
                mapICItem.put(it.fitemid, icItem)
            }
        }
        var isBool = false
        // 判断相同的物料
        for ((index,it) in checkDatas.withIndex()) {
            if (mapICItem.containsKey(it.fitemId)) {
                isBool = true
                if (it.icItem.batchManager.equals("Y")) { // 启用批次号
                    setBatchCode(index, mapICItem.get(it.fitemId)!!.batchCode, mapICItem.get(it.fitemId)!!.realQty)

                } else if (it.icItem.snManager.equals("Y")) { // 启用序列号
                    val fqty = if(mapICItem.get(it.fitemId)!!.realQty > 1) mapICItem.get(it.fitemId)!!.realQty else 1.0
                    setSnCode(index, mapICItem.get(it.fitemId)!!.snCode, fqty)

                } else { // 未启用
                    var fqty = if (it.useableQty > 0) mapICItem.get(it.fitemId)!!.realQty else 1.0
                    unSetBatchOrSnCode(index, fqty)
                }
                break
            }
        }
        if(!isBool) { // 不存在，就提示
            Comm.showWarnDialog(mContext,"扫描的条码不匹配！")
            return
//                addRow(bt!!.batchCode, bt!!.snCode, bt!!.barcodeQty, prodOrder)
        }
        mAdapter!!.notifyDataSetChanged()
    }

    /**
     * 新增行
     */
    private fun addRow(icStockBillEntry: ICStockBillEntry) {
        val mbr = MaterialBinningRecord()
        mbr.type = '2'
        mbr.packType = 'A'
        mbr.boxId = if(boxBarCode != null) boxBarCode!!.box.id else 0
        mbr.boxBarCodeId = if(boxBarCode != null) boxBarCode!!.id else 0
        mbr.parentBoxBarCodeId = if(boxBarCode2 != null) boxBarCode2!!.id else 0
        mbr.fitemId = icStockBillEntry.fitemId
        mbr.fsourceInterId = icStockBillEntry.fsourceInterId
        mbr.fsourceEntryId = icStockBillEntry.fsourceEntryId
        mbr.fsourceNo = icStockBillEntry.fsourceBillNo
        mbr.fsourceQty = icStockBillEntry.fqty
        mbr.fsourceHightLimitQty = icStockBillEntry.fqty
        mbr.createUserId = user!!.id
        mbr.createUserName = user!!.username
        mbr.icItem = icStockBillEntry.icItem
        mbr.boxBarCode = boxBarCode
        mbr.fdeptId = icStockBillEntry.icstockBill.fdeptId
        mbr.fcustId = icStockBillEntry.icstockBill.fcustId
        mbr.icstockBillId = icstockBillId

        mbr.useableQty = icStockBillEntry.fqty
        if(checkDatas.size == 0) {
            mbr.rowNo = 1
        } else {
            mbr.rowNo = checkDatas[checkDatas.size-1].rowNo + 1
        }
        checkDatas.add(mbr)
        val pos = checkDatas.size-1

//        if (icStockBillEntry.icItem.batchManager.equals("Y")) { // 启用批次号
//            setBatchCode(pos, batchCode, barcodeQty)
//
//        } else if (icStockBillEntry.icItem.snManager.equals("Y")) { // 启用序列号
//            setSnCode(pos, snCode)
//
//        } else { // 未启用
//            var fqty = if (barcodeQty > 0) barcodeQty else 1.0
//            unSetBatchOrSnCode(pos, fqty)
//        }
        tv_custName.text = icStockBillEntry.icstockBill.cust.fname
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            SEL_BOX -> {
                if (resultCode == Activity.RESULT_OK) {
                    val box = data!!.getSerializableExtra("obj") as Box
                    tv_box.setText(box.getBoxName())
                    checkDatas.forEach {
                        it.boxId = box.id
                    }
                }
            }
            SEL_MTL -> { //查询物料	返回
                if (resultCode == Activity.RESULT_OK) {
                    val icItem = data!!.getSerializableExtra("obj") as ICItem
                    getMtlAfter(icItem)
                }
            }
            RESULT_NUM -> { // 数量	返回
                if (resultCode == Activity.RESULT_OK) {
                    val bundle = data!!.getExtras()
                    if (bundle != null) {
                        val value = bundle.getString("resultValue", "")
                        val num = parseDouble(value)
                        if(num <= 0) {
                            Comm.showWarnDialog(mContext,"数量不能小于等于0！")
                            return
                        }
                        checkDatas[curPos].fqty = num
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
                            '2' -> setTexts(et_boxCode2, code)
                            '3' -> setTexts(et_mtlCode, code)
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
                            '2' -> setTexts(et_boxCode2, value.toUpperCase())
                            '3' -> setTexts(et_mtlCode, value.toUpperCase())
                            '4' -> setTexts(et_expressCode, value)
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
            RESULT_BATCH_NUM -> {// 批次和数量  返回
                if (resultCode == Activity.RESULT_OK) {
                    val bundle = data!!.extras
                    if (bundle != null) {
                        val batchCode = bundle.getString("batchCode")
                        val fqty = bundle.getDouble("fqty")
//                        bt!!.batchCode = batchCode
                        listIcItem!![0].realQty = fqty
                        getMaterial(listIcItem!!)
                    }
                }
            }
            SEL_EXPRESS -> {//查询快递公司	返回
                if (resultCode == Activity.RESULT_OK) {
                    expressCompany = data!!.getSerializableExtra("obj") as ExpressCompany
                    tv_expressCompany.text = expressCompany!!.fname
                }
            }
        }
        mHandler.sendEmptyMessageDelayed(SETFOCUS, 200)
    }

    /**
     * 得到扫码或选择数据
     */
    private fun getMtlAfter(icItem : ICItem) {
        parent!!.isChange = true
//        // 满足条件就查询库存
//        if(icItem.inventoryQty <= 0 && icStockBillEntry.fdcStockId > 0 && icStockBillEntry.fitemId > 0) {
//            run_findInventoryQty()
//        }
    }

    /**
     * 扫码查询对应的方法
     */
    private fun run_smDatas() {
        isTextChange = false
        showLoadDialog("加载中...", false)
        var mUrl:String? = null
        var barcode:String? = null
        var type = ""
        when(smqFlag) {
            '1' -> { // 箱码
                mUrl = getURL("boxBarCode/findBarcode")
                barcode = getValues(et_boxCode)
                type = "2" // 销售装箱
            }
            '2' -> { // 上级箱码
                mUrl = getURL("boxBarCode/findBarcode_parent")
                barcode = getValues(et_boxCode2)
                type = "2" // 销售装箱
            }
            '3' -> { // 物料
                mUrl = getURL("icItem/findBarcodeBySalBox")
                barcode = getValues(et_mtlCode)
            }
        }
        val formBody = FormBody.Builder()
                .add("barcode", barcode)
                .add("type", type)
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
        var mUrl = getURL("materialBinningRecord/save")
        var mJson = JsonUtil.objectToString(checkDatas)
        val formBody = FormBody.Builder()
                .add("strJson", mJson)
                .add("realWeight", getValues(tv_realWeight))
                .add("realVolume", getValues(tv_realVolume))
                .add("type", "2") // 销售装箱
                .add("createParentBoxCode", if(cb_autoCreateCode.isChecked && boxBarCode != null) "2" else "") // 销售装箱
                .add("missionBillId", missionBillId.toString()) // 任务单id是保存了之后，修改任务单状态
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
     * 开箱或者封箱
     */
    private fun run_modifyStatus(status : String) {
        showLoadDialog("保存中...", false)
        var mUrl = getURL("boxBarCode/modifyStatus")
        val formBody = FormBody.Builder()
                .add("id", boxBarCode!!.id.toString())
                .add("status", status)
                .build()

        val request = Request.Builder()
                .addHeader("cookie", getSession())
                .url(mUrl)
                .post(formBody)
                .build()

        val call = okHttpClient!!.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                mHandler.sendEmptyMessage(UNBOX_STATUS)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()
                val result = body.string()
                if (!JsonUtil.isSuccess(result)) {
                    val msg = mHandler.obtainMessage(UNBOX_STATUS, result)
                    mHandler.sendMessage(msg)
                    return
                }
                val msg = mHandler.obtainMessage(BOX_STATUS, result)
                LogUtil.e("run_modifyStatus --> onResponse", result)
                mHandler.sendMessage(msg)
            }
        })
    }

    /**
     * 查询复核的数据
     */
    private fun run_findICStockBillEntryList() {
        showLoadDialog("保存中...", false)
        var mUrl = getURL("stockBill_WMS/findEntryListAndICStockBill")
        val formBody = FormBody.Builder()
                .add("icstockBillId", icstockBillId!!.toString())
                .build()

        val request = Request.Builder()
                .addHeader("cookie", getSession())
                .url(mUrl)
                .post(formBody)
                .build()

        val call = okHttpClient!!.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                mHandler.sendEmptyMessage(UNFIND_SOURCE)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()
                val result = body.string()
                if (!JsonUtil.isSuccess(result)) {
                    val msg = mHandler.obtainMessage(UNFIND_SOURCE, result)
                    mHandler.sendMessage(msg)
                    return
                }
                val msg = mHandler.obtainMessage(FIND_SOURCE, result)
                LogUtil.e("run_modifyStatus --> onResponse", result)
                mHandler.sendMessage(msg)
            }
        })
    }

    /**
     * 上传
     */
    private fun run_uploadToK3_XSZX() {
        val strBoxBarCode = JsonUtil.objectToString(boxBarCode)
        showLoadDialog("上传中...", false)
        val mUrl = getURL("stockBill_WMS/uploadToK3_XSZX")
        val formBody = FormBody.Builder()
                .add("strBoxBarCode", strBoxBarCode)
                .add("expressCompanyId", checkDatas[0].expressCompanyId.toString()) // 快递公司
                .add("expressNo", getValues(et_expressCode)) // 快递单号
                .add("realWeight", getValues(tv_realWeight)) // 重量
                .add("realVolume", getValues(tv_realVolume)) // 体积
                .build()

        val request = Request.Builder()
                .addHeader("cookie", getSession())
                .url(mUrl)
                .post(formBody)
                .build()

        val call = okHttpClient!!.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                mHandler.sendEmptyMessage(UNUPLOAD)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()
                val result = body.string()
                LogUtil.e("run_uploadToK3_XSZX --> onResponse", result)
                if (!JsonUtil.isSuccess(result)) {
                    val msg = mHandler.obtainMessage(UNUPLOAD, result)
                    mHandler.sendMessage(msg)
                    return
                }
                val msg = mHandler.obtainMessage(UPLOAD, result)
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