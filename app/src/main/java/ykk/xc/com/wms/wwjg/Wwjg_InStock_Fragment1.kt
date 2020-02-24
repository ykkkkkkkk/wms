package ykk.xc.com.wms.wwjg

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.OnClick
import kotlinx.android.synthetic.main.wwjg_in_stock_fragment1.*
import kotlinx.android.synthetic.main.wwjg_in_stock_main.viewPager
import okhttp3.*
import org.greenrobot.eventbus.EventBus
import ykk.xc.com.wms.R
import ykk.xc.com.wms.basics.*
import ykk.xc.com.wms.bean.*
import ykk.xc.com.wms.bean.k3Bean.Emp
import ykk.xc.com.wms.comm.BaseFragment
import ykk.xc.com.wms.comm.Comm
import ykk.xc.com.wms.util.JsonUtil
import ykk.xc.com.wms.util.LogUtil
import java.io.IOException
import java.lang.ref.WeakReference
import java.util.concurrent.TimeUnit

/**
 * 日期：2019-10-16 09:50
 * 描述：委外加工入库
 * 作者：ykk
 */
class Wwjg_InStock_Fragment1 : BaseFragment() {

    companion object {
        private val SEL_DEPT = 10
        private val SEL_EMP1 = 12
        private val SEL_EMP2 = 13
        private val SEL_EMP3 = 14
        private val SEL_EMP4 = 15
        private val SEL_STOCK = 16
        private val SAVE = 202
        private val UNSAVE = 502
    }

    private val context = this
    private var okHttpClient: OkHttpClient? = null
    private var user: User? = null
    private var mContext: Activity? = null
    private var parent: Wwjg_InStock_MainActivity? = null
    private var timesTamp:String? = null // 时间戳
    var icStockBill = ICStockBill() // 保存的对象

    // 消息处理
    private val mHandler = MyHandler(this)

    private class MyHandler(activity: Wwjg_InStock_Fragment1) : Handler() {
        private val mActivity: WeakReference<Wwjg_InStock_Fragment1>

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
                    SAVE -> {// 保存成功 进入
                        val strId_pdaNo = JsonUtil.strToString(msgObj)
                        if(m.icStockBill.id == 0) {
                            val arr = strId_pdaNo.split(":") // id和pdaNo数据拼接（1:IC201912121）
                            m.icStockBill.id = m.parseInt(arr[0])
                            m.icStockBill.pdaNo = arr[1]
                            m.tv_pdaNo.text = arr[1]
                        }
                        m.parent!!.isMainSave = true
                        m.parent!!.viewPager.setScanScroll(true); // 放开左右滑动
                        m.toasts("保存成功✔")
                        // 滑动第二个页面
                        m.parent!!.viewPager!!.setCurrentItem(1, false)
                        m.parent!!.isChange = true
                    }
                    UNSAVE -> { // 保存失败
                        errMsg = JsonUtil.strToString(msgObj)
                        if (m.isNULLS(errMsg).length == 0) errMsg = "保存失败！"
                        Comm.showWarnDialog(m.mContext, errMsg)
                    }
                }
            }
        }
    }

    override fun setLayoutResID(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.wwjg_in_stock_fragment1, container, false)
    }

    override fun initView() {
        mContext = getActivity()
        parent = mContext as Wwjg_InStock_MainActivity

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
        tv_inDateSel.text = Comm.getSysDate(7)
        tv_operationManName.text = user!!.empName

        icStockBill.billType = "WWJGRK"
        icStockBill.ftranType = 5
        icStockBill.frob = 1
        icStockBill.fbillerId = user!!.empId
        icStockBill.createUserId = user!!.id
        icStockBill.createUserName = user!!.username
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
        }
    }

    @OnClick(R.id.tv_inDateSel, R.id.tv_deptSel, R.id.tv_stockSel, R.id.tv_emp2Sel, R.id.tv_emp4Sel,
             R.id.btn_save, R.id.btn_clone)
    fun onViewClicked(view: View) {
        var bundle: Bundle? = null
        when (view.id) {
            R.id.tv_inDateSel -> { // 选择日期
                Comm.showDateDialog(mContext, tv_inDateSel, 0)
            }
            R.id.tv_deptSel -> { // 选择部门
                showForResult(Dept_DialogActivity::class.java, SEL_DEPT, null)
            }
            R.id.tv_stockSel -> { // 选择仓库
                val bundle = Bundle()
                bundle.putString("accountType", "SC")
                bundle.putInt("unDisable", 1) // 只显示未禁用的数据
                showForResult(Stock_DialogActivity::class.java, SEL_STOCK, bundle)
            }
//            R.id.tv_emp1Sel -> { // 选择业务员
//                bundle = Bundle()
//                bundle.putString("accountType", "SC")
//                showForResult(Emp_DialogActivity::class.java, SEL_EMP1, bundle)
//            }
            R.id.tv_emp2Sel -> { // 选择保管者
                bundle = Bundle()
                bundle.putString("accountType", "SC")
                showForResult(Emp_DialogActivity::class.java, SEL_EMP2, bundle)
            }
//            R.id.tv_emp3Sel -> { // 选择负责人
//                bundle = Bundle()
//                bundle.putString("accountType", "SC")
//                showForResult(Emp_DialogActivity::class.java, SEL_EMP3, bundle)
//            }
            R.id.tv_emp4Sel -> { // 选择验收人
                bundle = Bundle()
                bundle.putString("accountType", "SC")
                showForResult(Emp_DialogActivity::class.java, SEL_EMP4, bundle)
            }
            R.id.btn_save -> { // 保存
                if(!checkSave()) return
                icStockBill.fdate = getValues(tv_inDateSel)
                run_save();
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
     * 保存检查数据判断
     */
    fun checkSave() : Boolean {
        if (icStockBill.fdeptId == 0) {
            Comm.showWarnDialog(mContext, "请选择部门！")
            return false;
        }
        if (icStockBill.stock == null) {
            Comm.showWarnDialog(mContext, "请选择收料仓库！")
            return false;
        }
        if(icStockBill.fsmanagerId == 0) {
            Comm.showWarnDialog(mContext, "请选择保管人！")
            return false
        }
        if(icStockBill.ffmanagerId == 0) {
            Comm.showWarnDialog(mContext, "请选择验收人！")
            return false
        }
        return true;
    }

    override fun setListener() {

    }

    fun reset() {
        setEnables(tv_deptSel, R.drawable.back_style_blue2, true)
        parent!!.isMainSave = false
        parent!!.viewPager.setScanScroll(false) // 禁止滑动
        tv_pdaNo.text = ""
        tv_inDateSel.text = Comm.getSysDate(7)
        tv_deptSel.text = ""
        tv_stockSel.text = ""
        tv_emp2Sel.text = ""
        tv_emp4Sel.text = ""
        icStockBill.id = 0
        icStockBill.fselTranType = 0
        icStockBill.pdaNo = ""
        icStockBill.fsupplyId = 0
        icStockBill.fdeptId = 0
        icStockBill.fempId = 0
        icStockBill.fsmanagerId = 0
        icStockBill.fmanagerId = 0
        icStockBill.ffmanagerId = 0
        icStockBill.suppName = ""
        icStockBill.deptName = ""
        icStockBill.yewuMan = ""
        icStockBill.baoguanMan = ""
        icStockBill.fuzheMan = ""
        icStockBill.yanshouMan = ""
        icStockBill.stock = null

        timesTamp = user!!.getId().toString() + "-" + Comm.randomUUID()
        parent!!.isChange = false
        EventBus.getDefault().post(EventBusEntity(11)) // 发送指令到fragment2，告其清空
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            SEL_DEPT -> {//查询部门	返回
                if (resultCode == Activity.RESULT_OK) {
                    val dept = data!!.getSerializableExtra("obj") as Department
                    tv_deptSel.text = dept!!.departmentName
                    icStockBill.fdeptId = dept.fitemID
                    icStockBill.deptName = dept.departmentName
                }
            }
            SEL_STOCK -> {// 仓库	返回
                if (resultCode == Activity.RESULT_OK) {
                    val stock = data!!.getSerializableExtra("obj") as Stock
                    tv_stockSel.text = stock!!.fname
                    icStockBill.stock = stock
                }
            }
            SEL_EMP2 -> {//查询保管人	返回
                if (resultCode == Activity.RESULT_OK) {
                    val emp = data!!.getSerializableExtra("obj") as Emp
                    tv_emp2Sel.text = emp!!.fname
                    icStockBill.fsmanagerId = emp.fitemId
                    icStockBill.baoguanMan = emp.fname
                }
            }
            SEL_EMP4 -> {//查询验收人	返回
                if (resultCode == Activity.RESULT_OK) {
                    val emp = data!!.getSerializableExtra("obj") as Emp
                    tv_emp4Sel.text = emp!!.fname
                    icStockBill.ffmanagerId = emp.fitemId
                    icStockBill.yanshouMan = emp.fname
                }
            }

        }
    }

    /**
     * 保存
     */
    private fun run_save() {
        showLoadDialog("保存中...", false)
        val mUrl = getURL("stockBill_WMS/save")

        val mJson = JsonUtil.objectToString(icStockBill)
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