package ykk.xc.com.wms.warehouse

import android.graphics.Color
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

    private val context = this
    private val TAG = "Other_OutInStockSearchActivity"
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
//        Pur_ScInFragment1 fragment1 = new Pur_ScInFragment1();
//        Sal_OutFragment2 fragment2 = new Sal_OutFragment2();
//        Sal_OutFragment3 fragment3 = new Sal_OutFragment3();
//        when(pageId) {
//            0 -> {
//                fragment1 = OutInStock_Search_Fragment1_OtherInStock()
//                listFragment.add(fragment1!!)
//            }
//            1 -> {
//                fragment2 = OutInStock_Search_Fragment2_OtherOutStock()
//                listFragment.add(fragment2!!)
//            }
//            2 -> {
//                fragment3 = OutInStock_Search_Fragment3_PurInStock()
//                listFragment.add(fragment3!!)
//            }
//            3 -> {
//                fragment4 = OutInStock_Search_Fragment4_SalOutStock()
//                listFragment.add(fragment4!!)
//            }
//            4 -> {
//                fragment5 = OutInStock_Search_Fragment5_ProdInStock()
//                listFragment.add(fragment5!!)
//            }
//            5 -> {
//                fragment6 = OutInStock_Search_Fragment6_PurReceiveInStock()
//                listFragment.add(fragment6!!)
//            }
//            6 -> {
//                fragment7 = OutInStock_Search_Fragment7_PurReceiveQC()
//                listFragment.add(fragment7!!)
//            }
//
//            fragment1 = OutInStock_Search_Fragment1_OtherInStock()
//                    listFragment.add(fragment1!!)
//        }
        fragment1 = OutInStock_Search_Fragment1_OtherInStock()
        fragment2 = OutInStock_Search_Fragment2_OtherOutStock()
        fragment3 = OutInStock_Search_Fragment3_PurInStock()
        fragment4 = OutInStock_Search_Fragment4_SalOutStock()
        fragment5 = OutInStock_Search_Fragment5_ProdInStock()
        fragment6 = OutInStock_Search_Fragment6_PurReceiveInStock()
        fragment7 = OutInStock_Search_Fragment7_PurReceiveQC()
        fragment8 = OutInStock_Search_Fragment8_ProdTransfer()

        listFragment.add(fragment1!!)
        listFragment.add(fragment2!!)
        listFragment.add(fragment3!!)
        listFragment.add(fragment4!!)
        listFragment.add(fragment5!!)
        listFragment.add(fragment6!!)
        listFragment.add(fragment7!!)
        listFragment.add(fragment8!!)

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
                }
            }
        }
    }

    /**
     * 创建PopupWindow 【 单据类型选择 】
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
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            context.finish()
        }
        return false
    }
}