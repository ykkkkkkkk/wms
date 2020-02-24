package ykk.xc.com.wms.warehouse

import android.app.AlertDialog
import android.content.DialogInterface
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.KeyEvent
import android.view.View
import butterknife.OnClick
import kotlinx.android.synthetic.main.ware_icinvbackup_main.*
import ykk.xc.com.wms.R
import ykk.xc.com.wms.comm.BaseActivity
import ykk.xc.com.wms.util.adapter.BaseFragmentAdapter
import java.text.DecimalFormat
import java.util.*

/**
 * 日期：2019-10-16 09:14
 * 描述：盘点物料查询
 * 作者：ykk
 */
class ICInvBackup_Sel_MaterialMainDialog : BaseActivity() {

    private val context = this
    private val TAG = "ICInvBackup_SelMaterialMainDialog"
    private var curRadio: View? = null
    private val fragment1 = ICInvBackup_Sel_Material_Fragment1()
    private val fragment2 = ICInvBackup_Sel_Material_Fragment2()
    private var pageId = 0 // 页面id

    override fun setLayoutResID(): Int {
        return R.layout.ware_icinvbackup_sel_material_main_dialog;
    }

    override fun initData() {
        curRadio = viewRadio1
        val listFragment = ArrayList<Fragment>()
//        Bundle bundle2 = new Bundle();
//        bundle2.putSerializable("customer", customer);
//        fragment1.setArguments(bundle2); // 传参数
//        fragment2.setArguments(bundle2); // 传参数
//        Pur_ScInFragment1 fragment1 = new Pur_ScInFragment1();
//        Sal_OutFragment2 fragment2 = new Sal_OutFragment2();
//        Sal_OutFragment3 fragment3 = new Sal_OutFragment3();

        listFragment.add(fragment1)
        listFragment.add(fragment2)
//        listFragment.add(fragment3);
//        viewPager.setScanScroll(false); // 禁止左右滑动
        //ViewPager设置适配器
        viewPager.setAdapter(BaseFragmentAdapter(supportFragmentManager, listFragment))
        //ViewPager显示第一个Fragment
        viewPager!!.setCurrentItem(0)

        //ViewPager页面切换监听
        viewPager!!.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> tabChange(viewRadio1!!, "销售出库--销售订单", 0)
                    1 -> tabChange(viewRadio2!!, "销售出库--箱码", 1)
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

    @OnClick(R.id.btn_close, R.id.lin_tab1, R.id.lin_tab2, R.id.btn_search, R.id.btn_confirm)
    fun onViewClicked(view: View) {
        // setCurrentItem第二个参数控制页面切换动画
        //  true:打开/false:关闭
        //  viewPager.setCurrentItem(0, false);

        when (view.id) {
            R.id.btn_close -> { // 关闭
                when(pageId) {
                    0 -> fragment1.finish()
                    1 -> fragment2.finish()
                }
            }
            R.id.btn_search -> { // 查询
                when(pageId) {
                    0 -> fragment1.findFun()
                    1 -> fragment2.findFun()
                }
            }
            R.id.btn_confirm -> {// 确认
                when(pageId) {
                    0 -> fragment1.confirm()
                    1 -> fragment2.confirm()
                }

            }

            R.id.lin_tab1 -> {
                tabChange(viewRadio1!!, "销售出库--销售订单", 0)
            }
            R.id.lin_tab2 -> {
                tabChange(viewRadio2!!, "销售出库--箱码", 1)
            }
        }
    }

    /**
     * 选中之后改变样式
     */
    private fun tabSelected(v: View) {
        curRadio!!.setBackgroundResource(R.drawable.check_off2)
        v.setBackgroundResource(R.drawable.check_on)
        curRadio = v
    }

    private fun tabChange(view: View, str: String, page: Int) {
        pageId = page
        tabSelected(view)
//        tv_title.text = str
        viewPager!!.setCurrentItem(page, false)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            context.finish()
        }
        return false
    }
}