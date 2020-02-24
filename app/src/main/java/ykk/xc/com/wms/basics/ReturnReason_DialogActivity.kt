package ykk.xc.com.wms.basics

import android.app.Activity
import android.content.Intent
import android.os.Handler
import android.os.Message
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.KeyEvent
import android.view.View
import butterknife.OnClick
import kotlinx.android.synthetic.main.ab_returnreason_dialog.*
import okhttp3.*
import ykk.xc.com.wms.R
import ykk.xc.com.wms.basics.adapter.ReturnReason_DialogAdapter
import ykk.xc.com.wms.bean.k3Bean.ReturnReason
import ykk.xc.com.wms.comm.BaseDialogActivity
import ykk.xc.com.wms.util.JsonUtil
import ykk.xc.com.wms.util.basehelper.BaseRecyclerAdapter
import ykk.xc.com.wms.util.xrecyclerview.XRecyclerView
import java.io.IOException
import java.lang.ref.WeakReference
import java.util.*

/**
 * 选择退货理由dialog
 */
class ReturnReason_DialogActivity : BaseDialogActivity(), XRecyclerView.LoadingListener {

    private val context = this
    private val listDatas = ArrayList<ReturnReason>()
    private var mAdapter: ReturnReason_DialogAdapter? = null
    private val okHttpClient = OkHttpClient()
    private var limit = 1
    private var isRefresh: Boolean = false
    private var isLoadMore: Boolean = false
    private val isNextPage: Boolean = false
    private var flag: String? = null // DS：电商账号数据，NX：内销账号数据，SC：生产账号数据

    // 消息处理
    private val mHandler = MyHandler(this)

    private class MyHandler(activity: ReturnReason_DialogActivity) : Handler() {
        private val mActivity: WeakReference<ReturnReason_DialogActivity>

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
                        val list = JsonUtil.strToList(msg.obj as String, ReturnReason::class.java)
                        m.listDatas.addAll(list!!)
                        m.mAdapter!!.notifyDataSetChanged()
                    }
                    UNSUCC1 // 数据加载失败！
                    -> {
                        m.mAdapter!!.notifyDataSetChanged()
                        m.toasts("抱歉，没有加载到数据！")
                    }
                }//                        if (m.isRefresh) {
                //                            m.xRecyclerView.refreshComplete(true);
                //                        } else if (m.isLoadMore) {
                //                            m.xRecyclerView.loadMoreComplete(true);
                //                        }
                //
                //                        m.xRecyclerView.setLoadingMoreEnabled(m.isNextPage);
            }
        }

    }

    override fun setLayoutResID(): Int {
        return R.layout.ab_returnreason_dialog
    }

    override fun initView() {
        xRecyclerView!!.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        xRecyclerView!!.layoutManager = LinearLayoutManager(context)
        mAdapter = ReturnReason_DialogAdapter(context, listDatas)
        xRecyclerView!!.adapter = mAdapter
        xRecyclerView!!.setLoadingListener(context)

        xRecyclerView!!.isPullRefreshEnabled = false // 上啦刷新禁用
        xRecyclerView!!.isLoadingMoreEnabled = false // 不显示下拉刷新的view

        mAdapter!!.onItemClickListener = BaseRecyclerAdapter.OnItemClickListener { adapter, holder, view, pos ->
            val m = listDatas[pos - 1]
            val intent = Intent()
            intent.putExtra("obj", m)
            context.setResult(Activity.RESULT_OK, intent)
            context.finish()
        }
    }

    override fun initData() {
        val bundle = context.intent.extras
        if (bundle != null) {
            flag = bundle.getString("flag")
        }

        initLoadDatas()
    }


    // 监听事件
    @OnClick(R.id.btn_close, R.id.btn_search)
    fun onViewClicked(view: View) {
        when (view.id) {
            R.id.btn_close -> {
                closeHandler(mHandler)
                context.finish()
            }
            R.id.btn_search -> initLoadDatas()
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
        showLoadDialog("加载中...", false)

        var mUrl = getURL("returnReason/findReturnReasonList_DS")
        // 判断是哪个账号的数据
        if (flag != null && flag == "DS") { // 电商账号数据
            mUrl = getURL("returnReason/findReturnReasonList_DS")
        } else if (flag != null && flag == "NX") { // 内销账号数据
            mUrl = getURL("returnReason/findReturnReasonList_NX")
        } else if (flag != null && flag == "SC") { // 生产账号数据
            mUrl = getURL("returnReason/findReturnReasonList_SC")
        }
        val formBody = FormBody.Builder()
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
                //                isNextPage = JsonUtil.isNextPage(result, limit);

                val msg = mHandler.obtainMessage(SUCC1, result)
                Log.e("Dept_DialogActivity --> onResponse", result)
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

    override fun onDestroy() {
        closeHandler(mHandler)
        super.onDestroy()
    }

    companion object {
        private val SUCC1 = 200
        private val UNSUCC1 = 501
    }
}
