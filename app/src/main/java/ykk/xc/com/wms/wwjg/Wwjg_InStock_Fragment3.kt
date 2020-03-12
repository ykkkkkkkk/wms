package ykk.xc.com.wms.wwjg

import android.app.Activity
import android.content.Intent
import android.os.Handler
import android.os.Message
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.wwjg_in_stock_fragment3.*
import kotlinx.android.synthetic.main.wwjg_in_stock_main.*
import okhttp3.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import ykk.xc.com.wms.R
import ykk.xc.com.wms.bean.EventBusEntity
import ykk.xc.com.wms.bean.ICStockBillEntry
import ykk.xc.com.wms.bean.User
import ykk.xc.com.wms.comm.BaseFragment
import ykk.xc.com.wms.comm.Comm
import ykk.xc.com.wms.produce.adapter.Prod_InStock_Fragment3_Adapter
import ykk.xc.com.wms.util.BigdecimalUtil
import ykk.xc.com.wms.util.JsonUtil
import ykk.xc.com.wms.util.LogUtil
import ykk.xc.com.wms.util.basehelper.BaseRecyclerAdapter
import java.io.IOException
import java.lang.ref.WeakReference
import java.text.DecimalFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * 日期：2019-10-16 09:50
 * 描述：委外加工入库
 * 作者：ykk
 */
class Wwjg_InStock_Fragment3 : BaseFragment() {

    companion object {
        private val SUCC1 = 200
        private val UNSUCC1 = 500
        private val DELETE = 201
        private val UNDELETE = 501
    }
    private val context = this
    private var parent: Wwjg_InStock_MainActivity? = null

    val checkDatas = ArrayList<ICStockBillEntry>()
    private var okHttpClient: OkHttpClient? = null
    private var mAdapter: Prod_InStock_Fragment3_Adapter? = null
    private var user: User? = null
    private var mContext: Activity? = null
    private var curPos:Int = -1 // 当前行
    private var timesTamp:String? = null // 时间戳
    private val df = DecimalFormat("#.######")

    // 消息处理
    private val mHandler = MyHandler(this)

    private class MyHandler(activity: Wwjg_InStock_Fragment3) : Handler() {
        private val mActivity: WeakReference<Wwjg_InStock_Fragment3>

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
                        m.checkDatas.removeAt(m.curPos)

                        m.mAdapter!!.notifyDataSetChanged()
                    }
                    UNDELETE -> { // 删除分录  失败
                        Comm.showWarnDialog(m.mContext,"服务器繁忙，请稍后再试！")
                    }
                }
            }
        }
    }

    @Subscribe
    fun onEventBus(entity: EventBusEntity) {
        when (entity.caseId) {
            21 -> { // 接收第二个页面发来的指令
                run_findEntryList()
            }
        }
    }

    override fun setLayoutResID(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.wwjg_in_stock_fragment3, container, false)
    }

    override fun initView() {
        mContext = getActivity()
        parent = mContext as Wwjg_InStock_MainActivity

        recyclerView.addItemDecoration(DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL))
        recyclerView.layoutManager = LinearLayoutManager(mContext)
        mAdapter = Prod_InStock_Fragment3_Adapter(mContext!!, checkDatas)
        recyclerView.adapter = mAdapter
        // 设值listview空间失去焦点
        recyclerView.isFocusable = false

        // 行事件
        mAdapter!!.setCallBack(object : Prod_InStock_Fragment3_Adapter.MyCallBack {
//            override fun onModify(entity: ICStockBillEntry, position: Int) {
//                EventBus.getDefault().post(EventBusEntity(31, entity))
//                // 滑动第二个页面
//                parent!!.viewPager!!.setCurrentItem(1, false)
//            }
            override fun onDelete(entity: ICStockBillEntry, position: Int) {
                curPos = position
                run_removeEntry(entity.id)
            }
        })

        mAdapter!!.onItemClickListener = BaseRecyclerAdapter.OnItemClickListener { adapter, holder, view, pos ->
            if(curPos > -1) {
                checkDatas[curPos].isShowButton = false
            }
            curPos = pos
            checkDatas[pos].isShowButton = true
            mAdapter!!.notifyDataSetChanged()
        }
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

//    @OnClick(R.id.tv_deptSel, R.id.tv_suppSel, R.id.tv_stockSel, R.id.btn_scan_stockPos, R.id.btn_stockPosSel, R.id.btn_scan, R.id.btn_mtlSel, R.id.btn_save, R.id.btn_clone)
//    fun onViewClicked(view: View) {
//        var bundle: Bundle? = null
//        when (view.id) {
//            R.id.tv_deptSel -> { // 选择部门
//                showForResult(Dept_DialogActivity::class.java, SEL_DEPT, null)
//            }
//            R.id.tv_suppSel -> { // 选择供应商
//                showForResult(Supplier_DialogActivity::class.java, SEL_SUPP, null)
//            }
//            R.id.tv_stockSel -> { // 选择收货仓库
//                showForResult(Stock_DialogActivity::class.java, SEL_STOCK, null)
//            }
//            R.id.btn_stockPosSel -> { // 选择调出库位
//                if(!checkSelStockPos()) return
//                var bundle = Bundle()
//                bundle.putInt("fspGroupId", stock!!.fspGroupId);
//                showForResult(StockPos_DialogActivity::class.java, SEL_STOCKPOS, bundle)
//            }
//            R.id.btn_mtlSel -> { // 选择物料
//                if(!checkSelStockPos()) return
//                bundle = Bundle()
//                bundle.putInt("fStockId", stock!!.fitemId)
//                bundle.putInt("fStockPlaceID", if(stockPos != null) stockPos!!.fspId else 0)
//                showForResult(StockTransfer_Material_DialogActivity::class.java, SEL_MTL, bundle)
//            }
//            R.id.btn_scan_stockPos -> { // 调用摄像头扫描（调出库位）
//                if(!checkSelStockPos()) return
//                showForResult(CaptureActivity::class.java, BaseFragment.CAMERA_SCAN, null)
//            }
//            R.id.btn_scan -> { // 调用摄像头扫描（物料）
//                if(!checkSelStockPos()) return
//                showForResult(CaptureActivity::class.java, BaseFragment.CAMERA_SCAN, null)
//            }
//            R.id.btn_save -> { // 保存
//                if(checkDatas.size == 0) {
//                    Comm.showWarnDialog(mContext,"请选择物料或者扫码条码！")
//                    return
//                }
//                run_save();
//            }
//            R.id.btn_clone -> { // 重置
//                if (parent!!.isChange) {
//                    val build = AlertDialog.Builder(mContext)
//                    build.setIcon(R.drawable.caution)
//                    build.setTitle("系统提示")
//                    build.setMessage("您有未保存的数据，继续重置吗？")
//                    build.setPositiveButton("是") { dialog, which -> reset(true) }
//                    build.setNegativeButton("否", null)
//                    build.setCancelable(false)
//                    build.show()
//
//                } else {
//                    reset(true)
//                }
//            }
//        }
//    }

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