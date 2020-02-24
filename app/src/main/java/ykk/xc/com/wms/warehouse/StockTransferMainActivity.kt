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
 * 描述：调拨Main
 * 作者：ykk
 */
class StockTransferMainActivity : BaseActivity() {

    private val context = this
    private val TAG = "Sc_StockTransferMainActivity"
    private var curRadio: View? = null
    var isChange: Boolean = false // 返回的时候是否需要判断数据是否保存了
    private val listMaps = ArrayList<Map<String, Any>>()
    private val df = DecimalFormat("#.####")
    private val fragment1 = StockTransferFragment1_DS()
    private val fragment2 = StockTransferFragment1_SC()

    override fun setLayoutResID(): Int {
        return R.layout.ware_sc_stock_transfer_main;
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
        listFragment.add(fragment2);
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
                    0 -> tabChange(viewRadio1!!, "电商调拨", 0)
                    1 -> tabChange(viewRadio2!!, "生产调拨", 1)
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

    @OnClick(R.id.btn_close, R.id.lin_tab1, R.id.lin_tab2, R.id.btn_search)
    fun onViewClicked(view: View) {
        // setCurrentItem第二个参数控制页面切换动画
        //  true:打开/false:关闭
        //  viewPager.setCurrentItem(0, false);

        when (view.id) {
            R.id.btn_close // 关闭
            -> {
                if (isChange) {
                    val build = AlertDialog.Builder(context)
                    build.setIcon(R.drawable.caution)
                    build.setTitle("系统提示")
                    build.setMessage("您有未保存的数据，继续关闭吗？")
                    build.setPositiveButton("是", object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface, which: Int) {
                            context.finish()
                        }
                    })
                    build.setNegativeButton("否", null)
                    build.setCancelable(false)
                    build.show()

                } else {
                    context.finish()
                }
            }
            R.id.btn_search // 查询
            -> {
//                fragment1.findFun()
            }

            R.id.lin_tab1 -> {
                tabChange(viewRadio1!!, "电商调拨", 0)
            }
            R.id.lin_tab2 -> {
                tabChange(viewRadio1!!, "生产调拨", 1)
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
        tabSelected(view)
        tv_title.text = str
        viewPager!!.setCurrentItem(page, false)
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        // 按了删除键，回退键
        //        if(!isKeyboard && (event.getKeyCode() == KeyEvent.KEYCODE_FORWARD_DEL || event.getKeyCode() == KeyEvent.KEYCODE_DEL)) {
        // 240 为PDA两侧面扫码键，241 为PDA中间扫码键
        return if (!(event.keyCode == 240 || event.keyCode == 241)) {
            false
        } else super.dispatchKeyEvent(event)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            context.finish()
        }
        return false
    }
}