package ykk.xc.com.wms.warehouse

import android.graphics.drawable.BitmapDrawable
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import butterknife.OnClick
import kotlinx.android.synthetic.main.ware_outin_stock_search_main.*
import ykk.xc.com.wms.R
import ykk.xc.com.wms.comm.BaseActivity
import ykk.xc.com.wms.util.adapter.BaseFragmentAdapter
import java.text.DecimalFormat
import java.util.*

/**
 * 日期：2019-10-16 09:14
 * 描述：其它出入库查询
 * 作者：ykk
 */
class OutInStock_Search_MainActivity : BaseActivity() {

    val context = this
    private val TAG = "OutInStock_Search_MainActivity"
    private var curRadio: View? = null
    private var curRadioName: TextView? = null
    private val df = DecimalFormat("#.####")
    val listFragment = ArrayList<Fragment>()
    private var pageId = 0 // 上个页面传来的id
    private var billType = "QTRK" // 上个页面传来的
    var fragment1:OutInStock_Search_Fragment1_OtherInStock? = null
    var fragment2:OutInStock_Search_Fragment2_OtherOutStock? = null
    var fragment3:OutInStock_Search_Fragment3_PurInStock? = null
    var fragment4:OutInStock_Search_Fragment4_SalOutStock? = null
    var fragment5:OutInStock_Search_Fragment5_ProdInStock? = null
    var fragment6:OutInStock_Search_Fragment6_PurReceiveInStock? = null
    var fragment7:OutInStock_Search_Fragment7_PurReceiveQC? = null
    var fragment8:OutInStock_Search_Fragment8_ProdTransfer? = null
    var fragment9:OutInStock_Search_Fragment9_ProdInStockTransfer? = null
    var fragment10:OutInStock_Search_Fragment10_XSJH? = null
    var fragment11:OutInStock_Search_Fragment11_CKZJ? = null
    var fragment12:OutInStock_Search_Fragment12_CGFH? = null
    var fragment13:OutInStock_Search_Fragment13_XSZX? = null
    var fragment14:OutInStock_Search_Fragment14_XSCK_BTOR? = null
    var fragment15:OutInStock_Search_Fragment15_PurInStock_BTOR? = null
    var fragment16:OutInStock_Search_Fragment16_ZYDB? = null

    override fun setLayoutResID(): Int {
        return R.layout.ware_outin_stock_search_main;
    }

    override fun initData() {
        bundle()
//        val listFragment = ArrayList<Fragment>()
//        Bundle bundle2 = new Bundle();
//        bundle2.putSerializable("customer", customer);
//        fragment1.setArguments(bundle2); // 传参数
//        fragment2.setArguments(bundle2); // 传参数

        fragment1 = OutInStock_Search_Fragment1_OtherInStock()
        fragment2 = OutInStock_Search_Fragment2_OtherOutStock()
        fragment3 = OutInStock_Search_Fragment3_PurInStock()
        fragment4 = OutInStock_Search_Fragment4_SalOutStock()
        fragment5 = OutInStock_Search_Fragment5_ProdInStock()
        fragment6 = OutInStock_Search_Fragment6_PurReceiveInStock()
        fragment7 = OutInStock_Search_Fragment7_PurReceiveQC()
        fragment8 = OutInStock_Search_Fragment8_ProdTransfer()
        fragment9 = OutInStock_Search_Fragment9_ProdInStockTransfer()
        fragment10 = OutInStock_Search_Fragment10_XSJH()
        fragment11 = OutInStock_Search_Fragment11_CKZJ()
        fragment12 = OutInStock_Search_Fragment12_CGFH()
        fragment13 = OutInStock_Search_Fragment13_XSZX()
        fragment14 = OutInStock_Search_Fragment14_XSCK_BTOR()
        fragment15 = OutInStock_Search_Fragment15_PurInStock_BTOR()
        fragment16 = OutInStock_Search_Fragment16_ZYDB()

        listFragment.add(fragment1!!)
        listFragment.add(fragment2!!)
        listFragment.add(fragment3!!)
        listFragment.add(fragment4!!)
        listFragment.add(fragment5!!)
        listFragment.add(fragment6!!)
        listFragment.add(fragment7!!)
        listFragment.add(fragment8!!)
        listFragment.add(fragment9!!)
        listFragment.add(fragment10!!)
        listFragment.add(fragment11!!)
        listFragment.add(fragment12!!)
        listFragment.add(fragment13!!)
        listFragment.add(fragment14!!)
        listFragment.add(fragment15!!)
        listFragment.add(fragment16!!)

//        if(billType.equals("CGSHRK")) { // 采购
//            fragment6 = OutInStock_Search_Fragment6_PurReceiveInStock()
//            fragment7 = OutInStock_Search_Fragment7_PurReceiveQC()
//            listFragment.add(fragment6!!)
//            listFragment.add(fragment7!!)
//
//        } else if(billType.equals("SCRK")) { // 生产
//            fragment5 = OutInStock_Search_Fragment5_ProdInStock()
//            fragment8 = OutInStock_Search_Fragment8_ProdTransfer()
//            fragment9 = OutInStock_Search_Fragment9_ProdInStockTransfer()
//            listFragment.add(fragment5!!)
//            listFragment.add(fragment8!!)
//            listFragment.add(fragment9!!)
//
//        } else if(billType.equals("CGFH")) { // 销售
//            fragment10 = OutInStock_Search_Fragment10_XSJH()
//            fragment11 = OutInStock_Search_Fragment11_CKZJ()
//            fragment12 = OutInStock_Search_Fragment12_CGFH()
//            fragment13 = OutInStock_Search_Fragment13_XSZX()
//            listFragment.add(fragment10!!)
//            listFragment.add(fragment11!!)
//            listFragment.add(fragment12!!)
//            listFragment.add(fragment13!!)
//        }

        viewPager.setScanScroll(false); // 禁止左右滑动
        //ViewPager设置适配器
        viewPager.setAdapter(BaseFragmentAdapter(supportFragmentManager, listFragment))
        //ViewPager显示第一个Fragment
        viewPager!!.setCurrentItem(pageId)
//        viewPager!!.offscreenPageLimit = 7

        //ViewPager页面切换监听
        viewPager!!.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                when (position) {
//                    0 -> tabChange(viewRadio1!!, tv_radioName1, "表头", 0)
//                    1 -> tabChange(viewRadio2!!, tv_radioName2, "添加分录", 1)
                }
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })

    }

    private fun bundle() {
        val bundle = context.intent.extras
        if (bundle != null) {
            pageId = bundle.getInt("pageId")
            when(pageId) {
                0 -> tv_billType.text = "其他入库单列表"
                1 -> tv_billType.text = "其他出库单列表"
                2 -> tv_billType.text = "采购入库单列表"
                3 -> tv_billType.text = "销售出库单列表"
                4 -> tv_billType.text = "产品入库单列表"
                5 -> tv_billType.text = "外购入库单列表"
                6 -> tv_billType.text = "仓库收料单列表"
                7 -> tv_billType.text = "生产调拨单列表"
                8 -> tv_billType.text = "生产入库调拨单列表"
                9 -> tv_billType.text = "销售拣货列表"
                10 -> tv_billType.text = "出库质检列表"
                11 -> tv_billType.text = "仓管复核列表"
                12 -> tv_billType.text = "销售装箱列表"
                13 -> tv_billType.text = "销售退货列表"
                14 -> tv_billType.text = "采购退货列表"
                15 -> tv_billType.text = "自由调拨列表"
            }
            billType = bundle.getString("billType")
        }
    }

    @OnClick(R.id.btn_close, R.id.tv_billType, R.id.btn_search, R.id.btn_batchUpload)
    fun onViewClicked(view: View) {
        when (view.id) {
            R.id.btn_close -> {// 关闭
                context.finish()
            }
            R.id.tv_billType -> { // 单据类型选择
                pop_billType(view)
                popWindow!!.showAsDropDown(view)
            }
            R.id.btn_search -> { // 查询
                when(pageId) {
                    0 ->  fragment1!!.findFun()
                    1 ->  fragment2!!.findFun()
                    2 ->  fragment3!!.findFun()
                    3 ->  fragment4!!.findFun()
                    4 ->  fragment5!!.findFun()
                    5 ->  fragment6!!.findFun()
                    6 ->  fragment7!!.findFun()
                    7 ->  fragment8!!.findFun()
                    8 ->  fragment9!!.findFun()
                    9 ->  fragment10!!.findFun()
                    10 ->  fragment11!!.findFun()
                    11 ->  fragment12!!.findFun()
                    12 ->  fragment13!!.findFun()
                    13 ->  fragment14!!.findFun()
                    14 ->  fragment15!!.findFun()
                    15 ->  fragment16!!.findFun()
                }
            }
            R.id.btn_batchUpload -> { // 批量上传
                when(pageId) {
                    0 -> fragment1!!.batchUpload()
                    1 -> fragment2!!.batchUpload()
                    2 -> fragment3!!.batchUpload()
                    3 -> fragment4!!.batchUpload()
                    4 -> fragment5!!.batchUpload()
                    5 -> fragment6!!.batchUpload()
                    6 -> fragment7!!.batchUpload()
                    7 -> fragment8!!.batchUpload()
                    8 -> fragment9!!.batchUpload()
                    9 -> fragment10!!.batchUpload()
                    10 -> fragment11!!.batchUpload()
                    11 -> fragment12!!.batchUpload()
                    12 -> fragment13!!.batchUpload()
                    13 -> fragment14!!.batchUpload()
                    14 -> fragment15!!.batchUpload()
                    15 -> fragment16!!.batchUpload()
                }
            }
        }
    }

    /**
     * 创建PopupWindow 【单据类型选择 】
     */
    private var popWindow: PopupWindow? = null
    private fun pop_billType(v: View) {
        if (null != popWindow) {//不为空就隐藏
            popWindow!!.dismiss()
            return
        }
        // 获取自定义布局文件popupwindow_left.xml的视图
        val popV = layoutInflater.inflate(R.layout.ware_outin_stock_search_popwindow, null)
        // 创建PopupWindow实例,200,LayoutParams.MATCH_PARENT分别是宽度和高度
        popWindow = PopupWindow(popV, v.width, ViewGroup.LayoutParams.WRAP_CONTENT, true)
        // 设置动画效果
        // popWindow.setAnimationStyle(R.style.AnimationFade);
        popWindow!!.setBackgroundDrawable(BitmapDrawable())
        popWindow!!.isOutsideTouchable = true
        popWindow!!.isFocusable = true

        popV.findViewById<View>(R.id.tv1).visibility = View.GONE
        popV.findViewById<View>(R.id.tv2).visibility = View.GONE
        popV.findViewById<View>(R.id.tv3).visibility = View.GONE
        popV.findViewById<View>(R.id.tv4).visibility = View.GONE
        popV.findViewById<View>(R.id.tv5).visibility = View.GONE
        popV.findViewById<View>(R.id.tv6).visibility = View.GONE
        popV.findViewById<View>(R.id.tv7).visibility = View.GONE
        popV.findViewById<View>(R.id.tv8).visibility = View.GONE
        popV.findViewById<View>(R.id.tv9).visibility = View.GONE
        popV.findViewById<View>(R.id.tv10).visibility = View.GONE
        popV.findViewById<View>(R.id.tv11).visibility = View.GONE
        popV.findViewById<View>(R.id.tv12).visibility = View.GONE
        popV.findViewById<View>(R.id.tv13).visibility = View.GONE
        popV.findViewById<View>(R.id.tv14).visibility = View.GONE
        popV.findViewById<View>(R.id.tv15).visibility = View.GONE
        popV.findViewById<View>(R.id.tv16).visibility = View.GONE

        if (billType.equals("QTRK")) { // 仓库模块
            popV.findViewById<View>(R.id.tv1).visibility = View.VISIBLE
            popV.findViewById<View>(R.id.tv2).visibility = View.VISIBLE
            popV.findViewById<View>(R.id.tv16).visibility = View.VISIBLE

        } else if(billType.equals("CGSHRK")) { // 采购
            popV.findViewById<View>(R.id.tv6).visibility = View.VISIBLE
            popV.findViewById<View>(R.id.tv7).visibility = View.VISIBLE
            popV.findViewById<View>(R.id.tv15).visibility = View.VISIBLE

        } else if(billType.equals("SCRK")) { // 生产
            popV.findViewById<View>(R.id.tv5).visibility = View.VISIBLE
            popV.findViewById<View>(R.id.tv8).visibility = View.VISIBLE
            popV.findViewById<View>(R.id.tv9).visibility = View.VISIBLE

        } else if(billType.equals("CGFH")) { // 销售
            popV.findViewById<View>(R.id.tv10).visibility = View.VISIBLE
            popV.findViewById<View>(R.id.tv11).visibility = View.VISIBLE
            popV.findViewById<View>(R.id.tv12).visibility = View.VISIBLE
            popV.findViewById<View>(R.id.tv13).visibility = View.VISIBLE
            popV.findViewById<View>(R.id.tv14).visibility = View.VISIBLE
        }

        // 点击其他地方消失
        val click = View.OnClickListener { v ->
            when (v.id) {
                R.id.tv1 -> {
                    tv_billType.text = "其他入库单列表"
                    pageId = 0
                }
                R.id.tv2 -> {
                    tv_billType.text = "其他出库单列表"
                    pageId = 1
                }
                R.id.tv3 -> {
                    tv_billType.text = "采购入库单列表"
                    pageId = 2
                }
                R.id.tv4 -> {
                    tv_billType.text = "销售出库单列表"
                    pageId = 3
                }
                R.id.tv5 -> {
                    tv_billType.text = "产品入库单列表"
                    pageId = 4
                }
                R.id.tv6 -> {
                    tv_billType.text = "外购入库单列表"
                    pageId = 5
                }
                R.id.tv7 -> {
                    tv_billType.text = "仓库收料单列表"
                    pageId = 6
                }
                R.id.tv8 -> {
                    tv_billType.text = "生产调拨单列表"
                    pageId = 7
                }
                R.id.tv9 -> {
                    tv_billType.text = "生产入库调拨单列表"
                    pageId = 8
                }
                R.id.tv10 -> {
                    tv_billType.text = "销售拣货列表"
                    pageId = 9
                }
                R.id.tv11 -> {
                    tv_billType.text = "出库质检列表"
                    pageId = 10
                }
                R.id.tv12 -> {
                    tv_billType.text = "仓库复核列表"
                    pageId = 11
                }
                R.id.tv13 -> {
                    tv_billType.text = "销售装箱列表"
                    pageId = 12
                }
                R.id.tv14 -> {
                    tv_billType.text = "销售退货列表"
                    pageId = 13
                }
                R.id.tv15 -> {
                    tv_billType.text = "采购退货列表"
                    pageId = 14
                }
                R.id.tv16 -> {
                    tv_billType.text = "自由调拨列表"
                    pageId = 15
                }
            }
            viewPager!!.setCurrentItem(pageId)
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
        popV.findViewById<View>(R.id.tv13).setOnClickListener(click)
        popV.findViewById<View>(R.id.tv14).setOnClickListener(click)
        popV.findViewById<View>(R.id.tv15).setOnClickListener(click)
        popV.findViewById<View>(R.id.tv16).setOnClickListener(click)

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            context.finish()
        }
        return false
    }
}