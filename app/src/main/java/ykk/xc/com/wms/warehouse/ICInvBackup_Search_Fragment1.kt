package ykk.xc.com.wms.warehouse

import android.app.Activity
import android.os.Handler
import android.os.Message
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.OnClick
import kotlinx.android.synthetic.main.ware_icinvbackup_search_fragment1.*
import okhttp3.*
import ykk.xc.com.wms.R
import ykk.xc.com.wms.bean.User
import ykk.xc.com.wms.bean.k3Bean.ICInvBackup
import ykk.xc.com.wms.comm.BaseFragment
import ykk.xc.com.wms.comm.Comm
import ykk.xc.com.wms.util.JsonUtil
import ykk.xc.com.wms.util.basehelper.BaseRecyclerAdapter
import ykk.xc.com.wms.util.xrecyclerview.XRecyclerView
import ykk.xc.com.wms.warehouse.adapter.ICInvBackup_Search_Fragment1_Adapter
import java.io.IOException
import java.lang.ref.WeakReference
import java.util.*

/**
 * 日期：2019-10-16 09:50
 * 描述：WMS 盘点（有盘点方案）
 * 作者：ykk
 */
class ICInvBackup_Search_Fragment1 : BaseFragment(), XRecyclerView.LoadingListener {

    companion object {
        private val SUCC1 = 200
        private val UNSUCC1 = 501
    }

    private var mAdapter: ICInvBackup_Search_Fragment1_Adapter? = null
    private val listDatas = ArrayList<ICInvBackup>()
    private val okHttpClient = OkHttpClient()
    private var limit = 1
    private var isRefresh: Boolean = false
    private var isLoadMore: Boolean = false
    private var isNextPage: Boolean = false
    private var mContext: Activity? = null
    private var parent: ICInvBackup_Search_MainActivity? = null
    private var curPos: Int = 0 // 当前行
    private var user: User? = null

    // 消息处理
    private val mHandler = MyHandler(this)

    private class MyHandler(activity: ICInvBackup_Search_Fragment1) : Handler() {
        private val mActivity: WeakReference<ICInvBackup_Search_Fragment1>

        init {
            mActivity = WeakReference(activity)
        }

        override fun handleMessage(msg: Message) {
            val m = mActivity.get()
            if (m != null) {
                m.hideLoadDialog()
                when (msg.what) {
                    SUCC1 // 成功
                    -> {
                        val list = JsonUtil.strToList2(msg.obj as String, ICInvBackup::class.java)
                        m.listDatas.addAll(list!!)
                        m.mAdapter!!.notifyDataSetChanged()

                        if (m.isRefresh) {
                            m.xRecyclerView!!.refreshComplete(true)
                        } else if (m.isLoadMore) {
                            m.xRecyclerView!!.loadMoreComplete(true)
                        }

                        m.xRecyclerView!!.isLoadingMoreEnabled = m.isNextPage
                    }
                    UNSUCC1 -> {// 数据加载失败！
                        m.mAdapter!!.notifyDataSetChanged()
                        m.toasts("抱歉，没有加载到数据！")
                    }
                }
            }
        }
    }

    override fun setLayoutResID(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.ware_icinvbackup_search_fragment1, container, false)
    }

    override fun initView() {
        mContext = getActivity()
        parent = mContext as ICInvBackup_Search_MainActivity

        xRecyclerView!!.addItemDecoration(DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL))
        xRecyclerView!!.layoutManager = LinearLayoutManager(context)
        mAdapter = ICInvBackup_Search_Fragment1_Adapter(mContext!!, listDatas)
        xRecyclerView!!.adapter = mAdapter
        xRecyclerView!!.setLoadingListener(this)

        xRecyclerView!!.isPullRefreshEnabled = false // 上啦刷新禁用
        xRecyclerView.setLoadingMoreEnabled(false); // 不显示下拉刷新的view

        mAdapter!!.onItemClickListener = BaseRecyclerAdapter.OnItemClickListener { adapter, holder, view, pos ->
            //            val m = listDatas[pos - 1]
//            val intent = Intent()
//            intent.putExtra("obj", m)
//            context.setResult(Activity.RESULT_OK, intent)
//            context.finish()
        }
    }

    override fun initData() {
        getUserInfo()
        tv_begDate.text = Comm.getSysDate(7)
        tv_endDate.text = Comm.getSysDate(7)

        initLoadDatas()
    }

    // 监听事件
    @OnClick(R.id.tv_begDate, R.id.tv_endDate)
    fun onViewClicked(view: View) {
        when (view.id) {
            R.id.tv_begDate -> {
                Comm.showDateDialog(mContext, view, 0)
            }
            R.id.tv_endDate -> {
                Comm.showDateDialog(mContext, view, 0)
            }
        }
    }

    fun findFun() {
        initLoadDatas()
    }

    private fun initLoadDatas() {
        limit = 1
        listDatas.clear()
        run_okhttpDatas()
    }

    /**
     * 通过okhttp加载数据
     */
    private fun run_okhttpDatas() {
        showLoadDialog("加载中...", false)
        val mUrl = getURL("icInvBackup/findListByPage")
        val formBody = FormBody.Builder()
                .add("finterId", "0")
                .add("userId", user!!.id.toString())
                .add("begDate", getValues(tv_begDate))
                .add("endDate", getValues(tv_endDate) + " 23:59:59")
                .add("limit", limit.toString())
                .add("pageSize", "30")
                .build()

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