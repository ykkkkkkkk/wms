package ykk.xc.com.wms.warehouse

import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.KeyEvent
import android.view.View
import android.widget.TextView
import butterknife.OnClick
import kotlinx.android.synthetic.main.ware_bill_confirm_list_main.*
import ykk.xc.com.wms.R
import ykk.xc.com.wms.comm.BaseActivity
import ykk.xc.com.wms.comm.Comm
import ykk.xc.com.wms.util.adapter.BaseFragmentAdapter
import java.text.DecimalFormat
import java.util.*

/**
 * 日期：2019-10-16 09:14
 * 描述：待上传列表查询
 * 作者：ykk
 */
class Ware_BillConfirmList_MainActivity : BaseActivity() {

    val context = this
    private val TAG = "Ware_BillConfirmList_MainActivity"
    val listFragment = ArrayList<Fragment>()
    var fragment1:Ware_BillConfirmList_Fragment1? = null

    override fun setLayoutResID(): Int {
        return R.layout.ware_bill_confirm_list_main
    }

    override fun initData() {
        bundle()
//        val listFragment = ArrayList<Fragment>()
//        Bundle bundle2 = new Bundle();
//        bundle2.putSerializable("customer", customer);
//        fragment1.setArguments(bundle2); // 传参数
//        fragment2.setArguments(bundle2); // 传参数

        fragment1 = Ware_BillConfirmList_Fragment1()
        listFragment.add(fragment1!!)

        viewPager.setScanScroll(false); // 禁止左右滑动
        //ViewPager设置适配器
        viewPager.setAdapter(BaseFragmentAdapter(supportFragmentManager, listFragment))
        //ViewPager显示第一个Fragment
        viewPager!!.setCurrentItem(0)
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
        }
    }

    @OnClick(R.id.btn_close, R.id.tv_dateSel, R.id.btn_clear, R.id.btn_search, R.id.btn_batchUpload)
    fun onViewClicked(view: View) {
        when (view.id) {
            R.id.btn_close -> {// 关闭
                context.finish()
            }
            R.id.tv_dateSel -> { // 选择日期
                Comm.showDateDialog(context, view,0)
            }
            R.id.btn_clear -> {
                tv_dateSel.text = ""
                fragment1!!.findFun()
            }
            R.id.btn_search -> { // 查询
                fragment1!!.findFun()
            }
            R.id.btn_batchUpload -> { // 批量上传
                fragment1!!.batchUpload()
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            context.finish()
        }
        return false
    }
}