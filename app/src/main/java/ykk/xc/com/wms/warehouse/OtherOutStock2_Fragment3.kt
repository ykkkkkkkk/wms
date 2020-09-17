package ykk.xc.com.wms.warehouse

import android.app.Activity
import android.content.Intent
import android.os.Handler
import android.os.Message
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.OnClick
import kotlinx.android.synthetic.main.ware_other_out_stock2_fragment3.*
import kotlinx.android.synthetic.main.ware_other_out_stock2_main.*
import okhttp3.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import ykk.xc.com.wms.R
import ykk.xc.com.wms.bean.EventBusEntity
import ykk.xc.com.wms.bean.ICStockBill
import ykk.xc.com.wms.bean.ICStockBillEntry
import ykk.xc.com.wms.bean.User
import ykk.xc.com.wms.comm.BaseFragment
import ykk.xc.com.wms.comm.Comm
import ykk.xc.com.wms.util.BigdecimalUtil
import ykk.xc.com.wms.util.JsonUtil
import ykk.xc.com.wms.util.LogUtil
import ykk.xc.com.wms.util.basehelper.BaseRecyclerAdapter
import ykk.xc.com.wms.warehouse.adapter.OtherOutStock2Fragment3Adapter
import java.io.IOException
import java.lang.ref.WeakReference
import java.text.DecimalFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * 日期：2019-10-16 09:50
 * 描述：其他出库
 * 作者：ykk
 */
class OtherOutStock2_Fragment3 : BaseFragment() {

    companion object {
        private val SUCC1 = 200
        private val UNSUCC1 = 500
        private val DELETE = 201
        private val UNDELETE = 501
        private val UPLOAD = 202
        private val UNUPLOAD = 502
    }

    private val context = this
    private var parent: OtherOutStock2_MainActivity? = null

    val checkDatas = ArrayList<ICStockBillEntry>()
    private var okHttpClient: OkHttpClient? = null
    private var mAdapter: OtherOutStock2Fragment3Adapter? = null
    private var user: User? = null
    private var mContext: Activity? = null
    private var curPos:Int = -1 // 当前行
    private var timesTamp:String? = null // 时间戳
    private val df = DecimalFormat("#.######")

    // 消息处理
    private val mHandler = MyHandler(this)

    private class MyHandler(activity: OtherOutStock2_Fragment3) : Handler() {
        private val mActivity: WeakReference<OtherOutStock2_Fragment3>

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
                    SUCC1 -> { // 查询分录 进入
                        m.checkDatas.clear()
                        val list = JsonUtil.strToList(msgObj, ICStockBillEntry::class.java)
                        m.checkDatas.addAll(list)

                        var sumNum = 0.0
                        var sumMoney = 0.0
                        list.forEach() {
                            sumNum += it.fqty
                            val mul = BigdecimalUtil.mul(it.fqty, it.fprice)
                            sumMoney += mul
                        }
                        m.tv_sumNum.text = m.df.format(sumNum)
                        m.tv_sumMoney.text = m.df.format(sumMoney)

                        m.mAdapter!!.notifyDataSetChanged()
                    }
                    UNSUCC1 -> { // 查询分录  失败
                        m.tv_sumNum.text = "0"
                        m.tv_sumMoney.text = "0"
                    }
                    DELETE -> { // 删除分录 进入
                        m.run_findEntryList()
                    }
                    UNDELETE -> { // 删除分录  失败
                        Comm.showWarnDialog(m.mContext,"服务器繁忙，请稍后再试！")
                    }
                    UPLOAD -> { // 上传单据 进入
                        m.toasts("上传成功")
                        m.parent!!.finish()
                        // 滑动第一个页面
//                        m.parent!!.viewPager!!.setCurrentItem(0, false)
//                        m.parent!!.fragment1.reset() // 重置
                    }
                    UNUPLOAD -> { // 上传单据  失败
                        errMsg = JsonUtil.strToString(msgObj)
                        if (m.isNULLS(errMsg).length == 0) errMsg = "服务器繁忙，请稍后再试！"
                        Comm.showWarnDialog(m.mContext, errMsg)
                    }
                }
            }
        }
    }

    @Subscribe
    fun onEventBus(entity: EventBusEntity) {
        when (entity.caseId) {
            12,21 -> { // 接收第一个页面（12）发来的指令，接收第二个页面（21）发来的指令
                run_findEntryList()
            }
        }
    }

    override fun setLayoutResID(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.ware_other_out_stock2_fragment3, container, false)
    }

    override fun initView() {
        mContext = getActivity()
        parent = mContext as OtherOutStock2_MainActivity

        recyclerView.addItemDecoration(DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL))
        recyclerView.layoutManager = LinearLayoutManager(mContext)
        mAdapter = OtherOutStock2Fragment3Adapter(mContext!!, checkDatas)
        recyclerView.adapter = mAdapter
        // 设值listview空间失去焦点
        recyclerView.isFocusable = false

        // 行事件
        mAdapter!!.setCallBack(object : OtherOutStock2Fragment3Adapter.MyCallBack {
//            override fun onModify(entity: ICStockBillEntry, position: Int) {
//                EventBus.getDefault().post(EventBusEntity(3, entity))
//                // 滑动第二个页面
//                parent!!.viewPager!!.setCurrentItem(1, false)
//            }
            override fun onDelete(entity: ICStockBillEntry, position: Int) {
                curPos = position
                run_removeEntry(entity.id)
            }
        })

        mAdapter!!.onItemClickListener = BaseRecyclerAdapter.OnItemClickListener { adapter, holder, view, pos ->
            EventBus.getDefault().post(EventBusEntity(31, checkDatas[pos]))
            // 滑动第二个页面
            parent!!.viewPager!!.setCurrentItem(1, false)
        }

//        // 长按查看条码
//        mAdapter!!.onItemLongClickListener = BaseRecyclerAdapter.OnItemLongClickListener{ adapter, holder, view, pos ->
//            EventBus.getDefault().post(EventBusEntity(32, checkDatas[pos], (pos+1)))
//            // 滑动第四个页面
//            parent!!.viewPager!!.setCurrentItem(3, false)
//            true
//        }
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
        EventBus.getDefault().register(this)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
        }
    }

    @OnClick(R.id.btn_upload)
    fun onViewClicked(view: View) {
        when (view.id) {
            R.id.btn_upload -> { // 上传
                val size = checkDatas.size
                if(size == 0) {
                    Comm.showWarnDialog(mContext,"没有分录信息，不能上传！")
                    return
                }
                checkDatas.forEachIndexed { index, it ->
                    if(it.stockId_wms == 0) {
                        Comm.showWarnDialog(mContext,"第（"+(index+1)+"）行，请选择仓库信息！")
                        return
                    }
                    if(it.fqty == 0.0) {
                        Comm.showWarnDialog(mContext,"第（"+(index+1)+"）行，请扫码或输入（出库数）！")
                        return
                    }
                    if(it.fqty != it.fsourceQty) {
                        Comm.showWarnDialog(mContext,"第（"+(index+1)+"）行，出库数和源单数不一致！")
                        return
                    }
                }

                val list = ArrayList<ICStockBill>()
                list.add(parent!!.fragment1.icStockBill)
                val strJson = JsonUtil.objectToString(list)
                run_uploadToK3(strJson)
            }
        }
    }

    override fun setListener() {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
//            SEL_MTL //查询物料	返回
//            -> if (resultCode == Activity.RESULT_OK) {
//                val list = data!!.getSerializableExtra("obj") as List<ICInventory>
//
//                getMtlAfter(list)
//            }

        }
    }

    /**
     * 历史查询
     */
    private fun run_findEntryList() {
        showLoadDialog("加载中...", false)
        val mUrl = getURL("stockBill_WMS/findEntryList")
        val formBody = FormBody.Builder()
                .add("icstockBillId", parent!!.fragment1.icStockBill.id.toString())
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
                LogUtil.e("run_findEntryList --> onResponse", result)
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
     * 删除
     */
    private fun run_removeEntry(id : Int) {
        showLoadDialog("加载中...", false)
        val mUrl = getURL("stockBill_WMS/removeEntry")
        val formBody = FormBody.Builder()
                .add("id", id.toString())
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
                LogUtil.e("run_findEntryList --> onResponse", result)
                if (!JsonUtil.isSuccess(result)) {
                    val msg = mHandler.obtainMessage(UNDELETE, result)
                    mHandler.sendMessage(msg)
                    return
                }
                val msg = mHandler.obtainMessage(DELETE, result)
                mHandler.sendMessage(msg)
            }
        })
    }

    /**
     * 上传单据
     */
    private fun run_uploadToK3(strJson : String) {
        showLoadDialog("加载中...", false)
        val mUrl = getURL("stockBill_WMS/uploadToK3")
        val formBody = FormBody.Builder()
                .add("strJson", strJson)
                .add("timesTamp", timesTamp)
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
                LogUtil.e("run_uploadToK3 --> onResponse", result)
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
        EventBus.getDefault().unregister(this)
        super.onDestroyView()
    }
}