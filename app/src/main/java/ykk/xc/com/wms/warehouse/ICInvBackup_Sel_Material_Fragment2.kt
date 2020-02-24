package ykk.xc.com.wms.warehouse

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.OnClick
import kotlinx.android.synthetic.main.ware_icinvbackup_sel_material_fragment2.*
import okhttp3.*
import ykk.xc.com.wms.R
import ykk.xc.com.wms.basics.StockPos_DialogActivity
import ykk.xc.com.wms.basics.Stock_DialogActivity
import ykk.xc.com.wms.bean.Stock
import ykk.xc.com.wms.bean.StockPosition
import ykk.xc.com.wms.bean.User
import ykk.xc.com.wms.bean.k3Bean.ICInvBackup
import ykk.xc.com.wms.bean.k3Bean.ICItem
import ykk.xc.com.wms.bean.k3Bean.MeasureUnit
import ykk.xc.com.wms.comm.BaseFragment
import ykk.xc.com.wms.comm.Comm
import ykk.xc.com.wms.util.BigdecimalUtil
import ykk.xc.com.wms.util.JsonUtil
import ykk.xc.com.wms.util.basehelper.BaseRecyclerAdapter
import ykk.xc.com.wms.util.xrecyclerview.XRecyclerView
import ykk.xc.com.wms.warehouse.adapter.ICInvBackup_Sel_Material_Fragment2Adapter
import java.io.IOException
import java.io.Serializable
import java.lang.ref.WeakReference
import java.util.*

/**
 * 日期：2019-10-16 09:50
 * 描述：
 * 作者：ykk
 */
class ICInvBackup_Sel_Material_Fragment2 : BaseFragment(), XRecyclerView.LoadingListener {
    companion object {
        private val SUCC1 = 200
        private val UNSUCC1 = 501

        private val SEL_STOCK = 11
    }

    private val context = this
    private val listDatas = ArrayList<ICItem>()
    private var mAdapter: ICInvBackup_Sel_Material_Fragment2Adapter? = null
    private val okHttpClient = OkHttpClient()
    private var limit = 1
    private var isRefresh: Boolean = false
    private var isLoadMore: Boolean = false
    private var isNextPage: Boolean = false
    private var mContext: Activity? = null
    private var user: User? = null
    private var parent: ICInvBackup_Sel_MaterialMainDialog? = null

    // 消息处理
    private val mHandler = MyHandler(this)

    private class MyHandler(activity: ICInvBackup_Sel_Material_Fragment2) : Handler() {
        private val mActivity: WeakReference<ICInvBackup_Sel_Material_Fragment2>

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
                        val list = JsonUtil.strToList2(msg.obj as String, ICItem::class.java)
                        m.listDatas.addAll(list!!)
                        m.mAdapter!!.notifyDataSetChanged()

                        if (m.isRefresh) {
                            m.xRecyclerView!!.refreshComplete(true)
                        } else if (m.isLoadMore) {
                            m.xRecyclerView!!.loadMoreComplete(true)
                        }

                        m.xRecyclerView!!.isLoadingMoreEnabled = m.isNextPage
                    }
                    UNSUCC1 -> { // 数据加载失败！

                        m.mAdapter!!.notifyDataSetChanged()
                        m.toasts("抱歉，没有加载到数据！")
                    }
                }
            }
        }

    }

    override fun setLayoutResID(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.ware_icinvbackup_sel_material_fragment2, container, false)
    }

    override fun initView() {
        mContext = getActivity()
        parent = mContext as ICInvBackup_Sel_MaterialMainDialog

        xRecyclerView!!.addItemDecoration(DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL))
        xRecyclerView!!.layoutManager = LinearLayoutManager(mContext)
        mAdapter = ICInvBackup_Sel_Material_Fragment2Adapter(mContext!!, listDatas)
        xRecyclerView!!.adapter = mAdapter
        xRecyclerView!!.setLoadingListener(context)

        xRecyclerView!!.isPullRefreshEnabled = false // 上啦刷新禁用
        xRecyclerView.setLoadingMoreEnabled(false); // 不显示下拉刷新的view

        mAdapter!!.onItemClickListener = BaseRecyclerAdapter.OnItemClickListener { adapter, holder, view, pos ->
            val m = listDatas[pos - 1]
            val isCheck = m.isCheck
            if (isCheck) {
                m.isCheck = false
            } else {
                m.isCheck = true
            }
            mAdapter!!.notifyDataSetChanged()
        }
    }

    override fun initData() {
        getUserInfo()
        initLoadDatas()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
//            initLoadDatas()
        }
    }


    // 监听事件
//    @OnClick(R.id.tv_stockSel)
//    fun onViewClicked(view: View) {
//        when (view.id) {
//            R.id.tv_stockSel -> { // 选择仓库
//                val bundle = Bundle()
//                bundle.putString("accountType", "SC")
//                bundle.putInt("unDisable", 1) // 只显示未禁用的数据
//                showForResult(Stock_DialogActivity::class.java, SEL_STOCK, bundle)
//            }
//        }
//    }
    fun finish() {
        mContext!!.finish()
    }

    /**
     *  查询方法
     */
    fun findFun() {
        initLoadDatas()
    }

    /**
     * 确认方法
     */
    fun confirm() {
        val size = listDatas.size
        if (size == 0) {
            Comm.showWarnDialog(mContext, "请查询数据！")
            return
        }
        var listMtl = ArrayList<ICItem>()
        for (i in 0 until size) {
            val mtl = listDatas[i]
            if (mtl.isCheck) {
                listMtl.add(mtl)
            }
        }
        if (listMtl.size == 0) {
            Comm.showWarnDialog(mContext, "请至少选择一行数据！")
            return
        }
        val list = ArrayList<ICInvBackup>()
        listMtl.forEach {
            var ic = ICInvBackup()
            ic.finterId = 0 // 方案id
            ic.stockId = 0 // 仓库id
            ic.mtlId = it.fitemid // 物料id
            ic.fauxQty = 0.0 // 账存数
            ic.fauxQtyAct = 0.0 // 实存数
            ic.fauxCheckQty = 0.0 // 盘点数
            ic.realQty = 1.0 // 当时盘点的输入的数
            ic.createUserId = user!!.id // 创建人
            ic.toK3 = 0 // 是否提交到K3  1: 未提交	3:已提交
            ic.stockName = "" // 仓库名称
            ic.mtlNumber = it.fnumber // 物料代码
            ic.mtlName = it.fname // 物料名称
            ic.unitName = it.unit.unitName // 单位名称
            ic.fmodel = it.fmodel // 物料规格
            ic.fbatchNo = "" // 物料批次

            list.add(ic)
        }
        val intent = Intent()
        intent.putExtra("obj", list as Serializable)
        mContext!!.setResult(Activity.RESULT_OK, intent)
        mContext!!.finish()
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
        var mUrl = getURL("material/findIcItemList")
        val formBody = FormBody.Builder()
                .add("fNumberAndName", getValues(et_search).trim())
                .add("limit", limit.toString())
                .add("pageSize", "50")
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
//            SEL_STOCK -> {// 仓库	返回
//                if (resultCode == Activity.RESULT_OK) {
//                    stock = data!!.getSerializableExtra("obj") as Stock
//                    tv_stockSel.text = stock!!.fname
//                }
//            }
        }
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