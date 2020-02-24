package ykk.xc.com.wms.purchase

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.KeyEvent
import android.view.View
import butterknife.OnClick
import kotlinx.android.synthetic.main.missionbill_list.*
import okhttp3.*
import ykk.xc.com.wms.R
import ykk.xc.com.wms.bean.MissionBill
import ykk.xc.com.wms.bean.User
import ykk.xc.com.wms.comm.BaseActivity
import ykk.xc.com.wms.comm.Comm
import ykk.xc.com.wms.produce.Prod_Transfer_MainActivity
import ykk.xc.com.wms.purchase.adapter.MissionBill_List_Adapter
import ykk.xc.com.wms.util.JsonUtil
import ykk.xc.com.wms.util.basehelper.BaseRecyclerAdapter
import ykk.xc.com.wms.util.xrecyclerview.XRecyclerView
import java.io.IOException
import java.lang.ref.WeakReference
import java.util.*

/**
 * 选择任务单dialog
 */
class MissionBillListActivity : BaseActivity(), XRecyclerView.LoadingListener {

    companion object {
        private val REFRESH = 10

        private val SUCC1 = 200
        private val UNSUCC1 = 501
    }
    private val context = this
    private val listDatas = ArrayList<MissionBill>()
    private var mAdapter: MissionBill_List_Adapter? = null
    private var user: User? = null
    private val okHttpClient = OkHttpClient()
    private var limit = 1
    private var isRefresh: Boolean = false
    private var isLoadMore: Boolean = false
    private var isNextPage: Boolean = false

    // 消息处理
    private val mHandler = MyHandler(this)

    private class MyHandler(activity: MissionBillListActivity) : Handler() {
        private val mActivity: WeakReference<MissionBillListActivity>

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
                        val list = JsonUtil.strToList2(msg.obj as String, MissionBill::class.java)
                        m.listDatas.addAll(list!!)
                        m.mAdapter!!.notifyDataSetChanged()

                        if (m.isRefresh) {
                            m.xRecyclerView!!.refreshComplete(true)
                        } else if (m.isLoadMore) {
                            m.xRecyclerView!!.loadMoreComplete(true)
                        }

                        m.xRecyclerView!!.isLoadingMoreEnabled = m.isNextPage
                    }
                    UNSUCC1 // 数据加载失败！
                    -> {
                        m.mAdapter!!.notifyDataSetChanged()
                        m.toasts("抱歉，没有加载到数据！")
                    }
                }
            }
        }
    }

    override fun setLayoutResID(): Int {
        return R.layout.missionbill_list
    }

    override fun initView() {
        xRecyclerView!!.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        xRecyclerView!!.layoutManager = LinearLayoutManager(context)
        mAdapter = MissionBill_List_Adapter(context, listDatas)
        xRecyclerView!!.adapter = mAdapter
        xRecyclerView!!.setLoadingListener(context)

        xRecyclerView!!.isPullRefreshEnabled = false // 上啦刷新禁用
        xRecyclerView.setLoadingMoreEnabled(false); // 不显示下拉刷新的view

        mAdapter!!.onItemClickListener = BaseRecyclerAdapter.OnItemClickListener { adapter, holder, view, pos ->

            val bundle = Bundle()
            bundle.putSerializable("missionBill", listDatas[pos - 1])
            when(listDatas[pos-1].missionType) {
                1 -> showForResult(Pur_Receive_InStock_MainActivity::class.java, REFRESH, bundle)
                31 -> showForResult(Pur_Receive_QC_MainActivity::class.java, REFRESH, bundle)
                41 -> showForResult(Prod_Transfer_MainActivity::class.java, REFRESH, bundle)
            }
        }
    }

    override fun initData() {
        getUserInfo()
        val bundle = context.intent.extras
        if (bundle != null) {
        }

//        initLoadDatas()
    }

    override fun onResume() {
        super.onResume()
        initLoadDatas()
    }

    // 监听事件
    @OnClick(R.id.btn_close, R.id.btn_refresh, R.id.tv_date, R.id.btn_confirm)
    fun onViewClicked(view: View) {
        when (view.id) {
            R.id.btn_close -> {
                closeHandler(mHandler)
                context.finish()
            }
            R.id.btn_refresh -> {
                initLoadDatas()
            }
            R.id.tv_date -> {
                Comm.showDateDialog(context, tv_date, 0)
            }
            R.id.btn_confirm -> { // 确定
            }
        }
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
        val formBody = FormBody.Builder()
                .add("billNo", getValues(et_purNo).trim ())
//                .add("missionType", "1") // 任务类型 1代表外购收料任务，21代表销售发货任务
                .add("missionStatus", "B,D") // 任务状态 A：创建、B：审核、C：业务关闭、D：进行中，E：手工关闭
                .add("receiveUserId", user!!.id.toString())
                .add("limit", limit.toString())
                .add("pageSize", "30")
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

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            closeHandler(mHandler)
            context.finish()
        }
        return false
    }

    /**
     * 得到用户对象
     */
    private fun getUserInfo() {
        if (user == null) user = showUserByXml()
    }

    override fun onDestroy() {
        closeHandler(mHandler)
        super.onDestroy()
    }

}
