package ykk.xc.com.wms.entrance


import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.os.Process.killProcess
import android.support.v4.content.FileProvider
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.ProgressBar
import android.widget.TextView
import butterknife.OnClick
import kotlinx.android.synthetic.main.aa_main_item0.*
import okhttp3.*
import ykk.xc.com.wms.R
import ykk.xc.com.wms.bean.AppInfo
import ykk.xc.com.wms.bean.MissionBill
import ykk.xc.com.wms.bean.User
import ykk.xc.com.wms.comm.BaseFragment
import ykk.xc.com.wms.comm.Comm
import ykk.xc.com.wms.produce.Prod_InStock_Transfer_MainActivity
import ykk.xc.com.wms.produce.Prod_Transfer_MainActivity
import ykk.xc.com.wms.purchase.Pur_InStock_RED_MainActivity
import ykk.xc.com.wms.purchase.Pur_Receive_InStock_MainActivity
import ykk.xc.com.wms.purchase.Pur_Receive_QC_MainActivity
import ykk.xc.com.wms.purchase.adapter.MissionBill_List_Adapter
import ykk.xc.com.wms.sales.*
import ykk.xc.com.wms.util.IDownloadContract
import ykk.xc.com.wms.util.IDownloadPresenter
import ykk.xc.com.wms.util.JsonUtil
import ykk.xc.com.wms.util.basehelper.BaseRecyclerAdapter
import ykk.xc.com.wms.util.xrecyclerview.XRecyclerView
import ykk.xc.com.wms.warehouse.OtherInStock2_MainActivity
import ykk.xc.com.wms.warehouse.OtherOutStock2_MainActivity
import java.io.File
import java.io.IOException
import java.lang.ref.WeakReference
import java.util.*
import kotlin.collections.ArrayList

/**
 * 任务列表
 */
class MainTabFragment0 : BaseFragment(), IDownloadContract.View, XRecyclerView.LoadingListener {

    companion object {
        private val SUCC1 = 200
        private val UNSUCC1 = 500
        private val CLOSE = 201
        private val UNCLOSE = 501
        private val UPDATE = 202
        private val UNUPDATE = 502

        private val UPDATE_PLAN = 1
    }
    private val context = this
    private var mContext: Activity? = null
    private var parent: MainTabFragmentActivity? = null
    private val okHttpClient = OkHttpClient()
    private var mPresenter: IDownloadPresenter? = null
    private var isCheckUpdate = false // 是否已经检查过更新
    private val listDatas = ArrayList<MissionBill>()
    private var mAdapter: MissionBill_List_Adapter? = null
    private var user: User? = null
    private var limit = 1
    private var isRefresh: Boolean = false
    private var isLoadMore: Boolean = false
    private var isNextPage: Boolean = false
    private var missionType = 0
    var isInit = false
    var isLoadData = false

    // 消息处理
    private val mHandler = MyHandler(this)

    /**
     * 显示下载的进度
     */
    private var downloadDialog: Dialog? = null
    private var progressBar: ProgressBar? = null
    private var tvDownPlan: TextView? = null
    private var progress: Int = 0

    private class MyHandler(activity: MainTabFragment0) : Handler() {
        private val mActivity: WeakReference<MainTabFragment0>

        init {
            mActivity = WeakReference(activity)
        }

        override fun handleMessage(msg: Message) {
            val m = mActivity.get()
            if (m != null) {
                m.hideLoadDialog()

                when (msg.what) {
                    UPDATE -> { // 更新版本  成功
                        m.isCheckUpdate = true
                        val appInfo = JsonUtil.strToObject(msg.obj as String, AppInfo::class.java)
                        if (m.getAppVersionCode(m.mContext) != appInfo!!.appVersion) {
                            m.showNoticeDialog(appInfo.appRemark)
                        }
                    }
                    UNUPDATE -> { // 更新版本  失败！
                    }
                    UPDATE_PLAN -> { // 更新进度
                        m.progressBar!!.progress = m.progress
                        m.tvDownPlan!!.text = String.format(Locale.CHINESE, "%d%%", m.progress)
                    }
                    SUCC1 -> { // 成功
                        val list = JsonUtil.strToList2(msg.obj as String, MissionBill::class.java)
                        m.listDatas.addAll(list!!)
                        m.mAdapter!!.notifyDataSetChanged()

                        if (m.isRefresh) {
                            m.xRecyclerView.refreshComplete(true)
                        } else if (m.isLoadMore) {
                            m.xRecyclerView.loadMoreComplete(true)
                        }

                        m.xRecyclerView?.isLoadingMoreEnabled = m.isNextPage
                    }
                    UNSUCC1 -> { // 数据加载失败！
                        m.mAdapter!!.notifyDataSetChanged()
                        m.toasts("抱歉，没有加载到数据！")
                    }
                    CLOSE -> { // 关闭    成功
                        m.initLoadDatas()
                    }
                    UNCLOSE -> { // 关闭    失败！
                        Comm.showWarnDialog(m.mContext,"很抱歉，服务器繁忙！")
                    }
                }
            }
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mContext = context as Activity?
    }

    //SDK API<23时，onAttach(Context)不执行，需要使用onAttach(Activity)。Fragment自身的Bug，v4的没有此问题
    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            mContext = activity
        }
    }

    override fun onDetach() {
        super.onDetach()
        mContext = null
    }

    override fun setLayoutResID(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.aa_main_item0, container, false)
    }

    override fun initView() {
        mContext = getActivity()
        parent = mContext as MainTabFragmentActivity

        xRecyclerView!!.addItemDecoration(DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL))
        xRecyclerView!!.layoutManager = LinearLayoutManager(mContext)
        mAdapter = MissionBill_List_Adapter(mContext!!, listDatas)
        xRecyclerView!!.adapter = mAdapter
        xRecyclerView!!.setLoadingListener(context)

        xRecyclerView!!.isPullRefreshEnabled = false // 上啦刷新禁用
        xRecyclerView.setLoadingMoreEnabled(false); // 不显示下拉刷新的view

        mAdapter!!.onItemClickListener = BaseRecyclerAdapter.OnItemClickListener { adapter, holder, view, pos ->
            if(missionType == 51) {
                val check = listDatas[pos-1].isCheck
                if(check) {
                    listDatas[pos-1].isCheck = false
                } else {
                    listDatas[pos-1].isCheck = true
                }
                mAdapter!!.notifyDataSetChanged()

            } else {
                val bundle = Bundle()
                bundle.putSerializable("missionBill", listDatas[pos - 1])
                when (listDatas[pos - 1].missionType) {
                    1 -> show(Pur_Receive_InStock_MainActivity::class.java, bundle)
//                21 -> show(Pur_Receive_InStock_MainActivity::class.java, bundle)
                    31 -> show(Pur_Receive_QC_MainActivity::class.java, bundle)
                    32 -> show(Pur_InStock_RED_MainActivity::class.java, bundle)
                    41 -> show(Prod_Transfer_MainActivity::class.java, bundle)
                    42 -> show(Prod_InStock_Transfer_MainActivity::class.java, bundle)
                    51 -> {
                        val list = ArrayList<MissionBill>()
                        list.add(listDatas[pos-1])
                        bundle.putSerializable("missionBills", list)
                        show(Sal_PickGoods_MainActivity::class.java, bundle)
                    }
                    52 -> show(Sal_QcPass_MainActivity::class.java, bundle)
                    53 -> show(Sal_ReCheck_MainActivity::class.java, bundle)
                    54 -> show(Sal_Box_MainActivity::class.java, bundle)
                    55 -> show(Sal_OutStock_RED_MainActivity::class.java, bundle)
                    61 -> show(OtherInStock2_MainActivity::class.java, bundle)
                    62 -> show(OtherOutStock2_MainActivity::class.java, bundle)
                }
            }
        }
        // 长按查看条码
        mAdapter!!.onItemLongClickListener = BaseRecyclerAdapter.OnItemLongClickListener{ adapter, holder, view, pos ->
            val build = AlertDialog.Builder(mContext)
            build.setIcon(R.drawable.caution)
            build.setTitle("系统提示")
            build.setMessage("是否关闭任务？")
            build.setPositiveButton("是") { dialog, which -> run_close(listDatas[pos-1].id) }
            build.setNegativeButton("否", null)
            build.setCancelable(false)
            build.show()

            true
        }
    }

    override fun initData() {

        getUserInfo()

        mPresenter = IDownloadPresenter(context)
        if (!isCheckUpdate) {
            // 执行更新版本请求
            run_findAppInfo()
        }
        isInit = true
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if(isVisibleToUser && isInit && !isLoadData) {
            initLoadDatas()
        }
    }

    override fun onResume() {
        super.onResume()
        if(userVisibleHint == true) {
            initLoadDatas()
        }
    }

    @OnClick(R.id.tv_missionType, R.id.tv_date, R.id.btn_confirm)
    fun onViewClicked(view: View) {
        when (view.id) {
            R.id.tv_missionType -> { // 单据类型选择
                pop_missionType(view)
                popWindow!!.showAsDropDown(view)
            }
            R.id.tv_date -> {
                Comm.showDateDialog(mContext, tv_date, 0)
            }
            R.id.btn_confirm -> { // 确定
                // 进入拣货页面
                var isCheck = false
                var list = ArrayList<MissionBill>()
                listDatas.forEach {
                    if(it.isCheck) {
                        list.add(it)
                    }
                }
                if(list.size == 0) {
                    Comm.showWarnDialog(mContext,"请选中一行进行拣货！")
                    return
                }
                val bundle = Bundle()
                bundle.putSerializable("missionBills", list)
                show(Sal_PickGoods_MainActivity::class.java, bundle)
            }
        }
    }

    /**
     * 创建PopupWindow 【 单据类型选择 】
     */
    private var popWindow: PopupWindow? = null
    private fun pop_missionType(v: View) {
        if (null != popWindow) {//不为空就隐藏
            popWindow!!.dismiss()
            return
        }
        // 获取自定义布局文件popupwindow_left.xml的视图
        val popV = layoutInflater.inflate(R.layout.missiointype_popwindow, null)
        // 创建PopupWindow实例,200,LayoutParams.MATCH_PARENT分别是宽度和高度
        popWindow = PopupWindow(popV, v.width, ViewGroup.LayoutParams.WRAP_CONTENT, true)
        // 设置动画效果
        // popWindow.setAnimationStyle(R.style.AnimationFade)
        popWindow!!.setBackgroundDrawable(BitmapDrawable())
        popWindow!!.isOutsideTouchable = true
        popWindow!!.isFocusable = true

        // 点击其他地方消失
        val click = View.OnClickListener { v ->
            when (v.id) {
                R.id.tv1 -> {
                    tv_missionType.text = "外购收料任务"
                    missionType = 1
                }
                R.id.tv2 -> {
                    tv_missionType.text = "销售发货任务"
                    missionType = 21
                }
                R.id.tv3 -> {
                    tv_missionType.text = "仓库外购收货任务"
                    missionType = 31
                }
                R.id.tv4 -> {
                    tv_missionType.text = "采购退货任务"
                    missionType = 32
                }
                R.id.tv5 -> {
                    tv_missionType.text = "投料调拨任务"
                    missionType = 41
                }
                R.id.tv6 -> {
                    tv_missionType.text = "生产入库调拨任务"
                    missionType = 42
                }
                R.id.tv7 -> {
                    tv_missionType.text = "销售拣货任务"
                    missionType = 51
                }
                R.id.tv8 -> {
                    tv_missionType.text = "出库质检任务"
                    missionType = 52
                }
                R.id.tv9 -> {
                    tv_missionType.text = "仓管复核任务"
                    missionType = 53
                }
                R.id.tv10 -> {
                    tv_missionType.text = "销售装箱任务"
                    missionType = 54
                }
                R.id.tv11 -> {
                    tv_missionType.text = "其他入库任务"
                    missionType = 61
                }
                R.id.tv12 -> {
                    tv_missionType.text = "其他出库任务"
                    missionType = 62
                }
            }
            if(missionType == 51) { // 拣货任务可以多个任务单一起拣货
                btn_confirm.visibility = View.VISIBLE
            } else {
                btn_confirm.visibility = View.GONE
            }
            initLoadDatas()
            popWindow!!.dismiss()
        }
        popV.findViewById<View>(R.id.tv1).setOnClickListener(click)
        popV.findViewById<View>(R.id.tv2).setOnClickListener(click)
        popV.findViewById<View>(R.id.tv3).setOnClickListener(click)
        popV.findViewById<View>(R.id.tv4).setOnClickListener(click)
        popV.findViewById<View>(R.id.tv5).setOnClickListener(click)
        popV.findViewById<View>(R.id.tv6).setOnClickListener(click)
        popV.findViewById<View>(R.id.tv7).setOnClickListener(click)
        popV.findViewById<View>(R.id.tv8).setOnClickListener(click)
        popV.findViewById<View>(R.id.tv9).setOnClickListener(click)
        popV.findViewById<View>(R.id.tv10).setOnClickListener(click)
        popV.findViewById<View>(R.id.tv11).setOnClickListener(click)
        popV.findViewById<View>(R.id.tv12).setOnClickListener(click)
    }

    fun initLoadDatas() {
        isLoadData = true
        limit = 1
        listDatas.clear()
        run_okhttpDatas()
    }

    /**
     * 通过okhttp加载数据
     */
    private fun run_okhttpDatas() {
        val formBody = FormBody.Builder()
                .add("billNo", getValues(et_purNo).trim ())
                .add("missionType", if(missionType > 0) missionType.toString() else "") // 任务类型 1代表外购收料任务，21代表销售发货任务
                .add("missionStatus", "B,D") // 任务状态 A：创建、B：审核、C：业务关闭、D：进行中，E：手工关闭
                .add("receiveUserId", user!!.id.toString())
                .add("limit", limit.toString())
                .add("pageSize", "30")
                .add("columnName", "checkTime") // 根据审核时间倒序
                .add("sortWay", "DESC")
                .build()
        showLoadDialog("加载中...", false)
        val mUrl = getURL("missionBill/findListByParam")

        val request = Request.Builder()
                .addHeader("cookie", session)
                .url(mUrl)
                .post(formBody)
                .build()

        val call = okHttpClient.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                mHandler.sendEmptyMessage(UNSUCC1)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()
                val result = body.string()
                if (!JsonUtil.isSuccess(result)) {
                    mHandler.sendEmptyMessage(UNSUCC1)
                    return
                }
                isNextPage = JsonUtil.isNextPage(result, limit)

                val msg = mHandler.obtainMessage(SUCC1, result)
                Log.e("run_okhttpDatas --> onResponse", result)
                mHandler.sendMessage(msg)
            }
        })
    }

    /**
     * 关闭任务
     */
    private fun run_close(id :Int) {
        val formBody = FormBody.Builder()
                .add("closeTime", "1") // 关闭日期
                .add("missionStatus", "E") // 任务状态 A：创建、B：审核、C：业务关闭、D：进行中，E：手工关闭
                .add("closerName", user!!.username)
                .add("id", id.toString())
                .build()
        showLoadDialog("关闭中...", false)
        val mUrl = getURL("missionBill/modifyStatus")

        val request = Request.Builder()
                .addHeader("cookie", session)
                .url(mUrl)
                .post(formBody)
                .build()

        val call = okHttpClient.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                mHandler.sendEmptyMessage(UNCLOSE)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()
                val result = body.string()
                if (!JsonUtil.isSuccess(result)) {
                    mHandler.sendEmptyMessage(UNCLOSE)
                    return
                }
                val msg = mHandler.obtainMessage(CLOSE, result)
                Log.e("run_okhttpDatas --> onResponse", result)
                mHandler.sendMessage(msg)
            }
        })
    }

    override fun onRefresh() {
        isRefresh = true
        isLoadMore = false
        initLoadDatas()
    }

    override fun onLoadMore() {
        isRefresh = false
        isLoadMore = true
        limit += 1
        run_okhttpDatas()
    }

    /**
     * 获取服务端的App信息
     */
    private fun run_findAppInfo() {
        val mUrl = getURL("appInfo/findAppInfo")
        val formBody = FormBody.Builder()
                .build()

        val request = Request.Builder()
                .addHeader("cookie", session)
                .url(mUrl)
                .post(formBody)
                .build()

        // step 3：创建 Call 对象
        val call = okHttpClient.newCall(request)

        //step 4: 开始异步请求
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                mHandler.sendEmptyMessage(UNUPDATE)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()
                val result = body.string()
                Log.e("run_findAppInfo --> onResponse", result)
                if (!JsonUtil.isSuccess(result)) {
                    mHandler.sendEmptyMessage(UNUPDATE)
                    return
                }
                val msg = mHandler.obtainMessage(UPDATE, result)
                mHandler.sendMessage(msg)
            }
        })
    }

    private fun showDownloadDialog() {
        val builder = AlertDialog.Builder(mContext)

        builder.setTitle("软件更新")
        val inflater = LayoutInflater.from(mContext)
        val v = inflater.inflate(R.layout.progress, null)
        progressBar = v.findViewById<View>(R.id.progress) as ProgressBar
        tvDownPlan = v.findViewById<View>(R.id.tv_downPlan) as TextView
        builder.setView(v)
        // 开发员用的，长按进度条，就关闭下载框
        tvDownPlan!!.setOnLongClickListener {
            downloadDialog!!.dismiss()
            true
        }
        // 如果用户点击取消就销毁掉这个系统
        //        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
        //            @Override
        //            public void delClick(DialogInterface dialog, int which) {
        ////                mContext.finish();
        //                dialog.dismiss();
        //            }
        //        });
        downloadDialog = builder.create()
        downloadDialog!!.show()
        downloadDialog!!.setCancelable(false)
        downloadDialog!!.setCanceledOnTouchOutside(false)
    }

    /**
     * 提示下载框
     */
    private fun showNoticeDialog(remark: String) {
        val alertDialog = AlertDialog.Builder(mContext)
                .setTitle("更新版本").setMessage(remark)
                .setPositiveButton("下载") { dialog, which ->
                    // 得到ip和端口
                    val spfConfig = spf(getResStr(R.string.saveConfig))
                    val ip = spfConfig.getString("ip", "192.168.3.198")
                    val port = spfConfig.getString("port", "8080")
                    val url = "http://$ip:$port/apks/wms.apk"

                    showDownloadDialog()
                    mPresenter!!.downApk(mContext, url)
                    dialog.dismiss()
                }
                //                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                //                    public void delClick(DialogInterface dialog, int which) {
                //                        dialog.dismiss();
                //                    }
                //                })
                .create()// 创建
        alertDialog.setCancelable(false)
        alertDialog.setCanceledOnTouchOutside(false)
        alertDialog.show()// 显示
    }

    /**
     * 得到本机的版本信息
     */
    private fun getAppVersionCode(context: Context?): Int {
        val pack: PackageManager
        val info: PackageInfo
        // String versionName = "";
        try {
            pack = context!!.packageManager
            info = pack.getPackageInfo(context.packageName, 0)
            return info.versionCode
            // versionName = info.versionName;
        } catch (e: Exception) {
            Log.e("getAppVersionName(Context context)：", e.toString())
        }

        return 0
    }

    override fun showUpdate(version: String) {}

    override fun showProgress(progress: Int) {
        context.progress = progress
        mHandler.sendEmptyMessage(UPDATE_PLAN)
    }

    override fun showFail(msg: String) {
        toasts(msg)
    }

    override fun showComplete(file: File) {
        if (downloadDialog != null) downloadDialog!!.dismiss()

        try {
            val intent = Intent(Intent.ACTION_VIEW)

            //7.0以上需要添加临时读取权限
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val authority = mContext!!.applicationContext.packageName + ".fileProvider"
                val fileUri = FileProvider.getUriForFile(mContext!!, authority, file)
                intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                intent.setDataAndType(fileUri, "application/vnd.android.package-archive")

            } else {
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive")
            }
            startActivity(intent)

            //弹出安装窗口把原程序关闭。
            //避免安装完毕点击打开时没反应
            killProcess(android.os.Process.myPid())

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 得到用户对象
     */
    private fun getUserInfo() {
        if (user == null) user = showUserByXml()
    }

}
