package ykk.xc.com.wms.produce

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.KeyEvent
import android.view.View
import android.widget.TextView
import butterknife.OnClick
import kotlinx.android.synthetic.main.prod_instock_transfer_fragment1.*
import kotlinx.android.synthetic.main.prod_instock_transfer_fragment2.*
import kotlinx.android.synthetic.main.prod_instock_transfer_main.*
import ykk.xc.com.wms.R
import ykk.xc.com.wms.comm.BaseActivity
import ykk.xc.com.wms.comm.Comm
import ykk.xc.com.wms.util.adapter.BaseFragmentAdapter
import ykk.xc.com.wms.util.blueTooth.Constant
import ykk.xc.com.wms.util.blueTooth.DeviceListActivity
import ykk.xc.com.wms.warehouse.OutInStock_Search_MainActivity
import java.io.IOException
import java.io.InputStream
import java.util.*

/**
 * 生产入库调拨
 */
class Prod_InStock_Transfer_MainActivity : BaseActivity() {

    private val REFRESH = 10
    private val BLUETOOTH_REC = 11
    private val RESET_TEXT = 12

    private val context = this
    private val TAG = "Prod_InStock_Transfer_MainActivity"
    private var curRadio: View? = null
    private var curRadioName: TextView? = null
    var isChange: Boolean = false // 返回的时候是否需要判断数据是否保存了

    private val _bluetooth = BluetoothAdapter.getDefaultAdapter()    //获取本地蓝牙适配器，即蓝牙设备
    internal var _device: BluetoothDevice? = null     //蓝牙设备
    internal var _socket: BluetoothSocket? = null      //蓝牙通信socket
    private var inputStream: InputStream? = null    //输入流，用来接收蓝牙数据
    private val MY_UUID = "00001101-0000-1000-8000-00805F9B34FB"   //SPP服务UUID号
    internal var bRun = true
    internal var bThread = false
    var smsg = StringBuffer()    //显示称重数据缓存

    val fragment1 = Prod_InStock_Transfer_Fragment1()
    val fragment2 = Prod_InStock_Transfer_Fragment2()
    val fragment3 = Prod_InStock_Transfer_Fragment3()
    val fragment4 = Prod_InStock_Transfer_Fragment4()
    var isMainSave = false // 主表信息是否保存
    var pageId = 0

    override fun setLayoutResID(): Int {
        return R.layout.prod_instock_transfer_main;
    }

    override fun initData() {
        bundle()
        curRadio = viewRadio1
        curRadioName = tv_radioName1
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
        listFragment.add(fragment3)
        listFragment.add(fragment4)
        viewPager.setScanScroll(false); // 禁止左右滑动
        //ViewPager设置适配器
        viewPager.setAdapter(BaseFragmentAdapter(supportFragmentManager, listFragment))
        //设置ViewPage缓存界面数，默认为1
        viewPager.offscreenPageLimit = 4
        //ViewPager显示第一个Fragment
        viewPager!!.setCurrentItem(0)

        //ViewPager页面切换监听
        viewPager!!.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> tabChange(viewRadio1!!, tv_radioName1, "表头", 0)
                    1 -> tabChange(viewRadio2!!, tv_radioName2, "添加分录", 1)
                    2 -> tabChange(viewRadio3!!, tv_radioName3, "表体", 2)
                    3 -> tabChange(viewRadio4!!, tv_radioName4, "条码", 3)
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

    @OnClick(R.id.btn_close, R.id.lin_tab1, R.id.lin_tab2, R.id.lin_tab3, R.id.lin_tab4, R.id.btn_search)
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
                            closeBlueTooth(true)
                            context.finish()
                        }
                    })
                    build.setNegativeButton("否", null)
                    build.setCancelable(false)
                    build.show()

                } else {
                    closeBlueTooth(true)
                    context.finish()
                }
            }
            R.id.btn_search -> { // 查询
                val bundle = Bundle()
                bundle.putInt("pageId", 5)
                bundle.putString("billType", "CGSHRK")
                showForResult(OutInStock_Search_MainActivity::class.java, REFRESH, bundle)
            }
            R.id.lin_tab1 -> {
                tabChange(viewRadio1!!, tv_radioName1, "表头", 0)
            }
            R.id.lin_tab2 -> {
                if(isMainSave) {
                    tabChange(viewRadio2!!, tv_radioName2, "添加分录", 1)
                } else {
                    Comm.showWarnDialog(context,"请先完善（表头）信息！")
                }
            }
            R.id.lin_tab3 -> {
                if(isMainSave) {
                    tabChange(viewRadio3!!, tv_radioName3, "表体", 2)
                } else {
                    Comm.showWarnDialog(context,"请先完善（表头）信息！")
                }
            }
            R.id.lin_tab4 -> {
                if(isMainSave) {
                    tabChange(viewRadio4!!, tv_radioName4, "条码", 3)
                } else {
                    Comm.showWarnDialog(context,"请先完善（表头）信息！")
                }
            }
        }
    }

    /**
     * 选中之后改变样式
     */
    private fun tabSelected(v: View, tv: TextView) {
        curRadio!!.setBackgroundResource(R.drawable.check_off2)
        v.setBackgroundResource(R.drawable.check_on)
        curRadio = v
        curRadioName!!.setTextColor(Color.parseColor("#000000"))
        tv.setTextColor(Color.parseColor("#FF4400"))
        curRadioName = tv
    }

    private fun tabChange(view: View, tv: TextView, str: String, page: Int) {
        pageId = page
        tabSelected(view, tv)
//        tv_title.text = str
        viewPager!!.setCurrentItem(page, false)
    }

    /**
     * 打开已配对蓝牙
     */
    fun openBluetooth() {
        if (_socket == null) {
            // 打开蓝牙配对页面
//            startActivityForResult(Intent(this, BluetoothDeviceListDialog::class.java), Constant.BLUETOOTH_REQUEST_CODE)
            startActivityForResult(Intent(this, DeviceListActivity::class.java), Constant.BLUETOOTH_REQUEST_CODE)

        } else {
            closeBlueTooth(false)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REFRESH -> {// 刷新
                if (resultCode == RESULT_OK) {
                    viewPager!!.setCurrentItem(0,false)
                    fragment1.reset()
                }
            }
            Constant.BLUETOOTH_REQUEST_CODE -> { // 蓝牙连接
                /*获取蓝牙mac地址*/
//                val macAddress = data!!.getStringExtra(BluetoothDeviceListDialog.EXTRA_DEVICE_ADDRESS)
                val macAddress = data!!.getStringExtra(DeviceListActivity.EXTRA_DEVICE_ADDRESS)
                openReceiveThread(macAddress)
            }
        }
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        // 按了删除键，回退键
        //        if(!isKeyboard && (event.getKeyCode() == KeyEvent.KEYCODE_FORWARD_DEL || event.getKeyCode() == KeyEvent.KEYCODE_DEL)) {
        // 240 为PDA两侧面扫码键，241 为PDA中间扫码键
        return if (!(event.keyCode == 240 || event.keyCode == 241)) {
            false
        } else super.dispatchKeyEvent(event)
    }

    @SuppressLint("HandlerLeak")
    private val mHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                BLUETOOTH_REC -> { // 得到电子秤返回的数据
                    when(pageId) {
                        0 -> context!!.fragment1.tv_roughWeight.text = smsg.toString()
                        1 -> {
//                            // 物料启用了称重，才写数据
                            val icItem = context!!.fragment2.icStockBillEntry.icItem
                            if(icItem.calByWeight.equals("M") || icItem.calByWeight.equals("Y")) {
                                context!!.fragment2.tv_weight.text = smsg.toString()
                            } else {
//                                Comm.showWarnDialog(context,"请选择物料称重！")
                            }
                        }
                    }
                    this.sendEmptyMessageDelayed(RESET_TEXT,300)
                }
                RESET_TEXT -> {
                    // 重置这个变量
                    smsg.setLength(0)
                }
            }
        }
    }

    // 接收电子秤数据  线程
    private var readThread: Thread = object : Thread() {
        override fun run() {
            var num = 0
            val buffer = ByteArray(1024)
            val buffer_new = ByteArray(1024)
            var i = 0
            var n = 0
            bRun = true
            //接收线程
            while (true) {
                try {
                    while (inputStream!!.available() == 0) {
                        while (bRun == false) {
                        }
                    }
                    while (true) {
                        if (!bThread) return

                        num = inputStream!!.read(buffer)         //读入数据
                        n = 0

//                        val s0 = String(buffer, 0, num)
//                        fmsg += s0    //保存收到数据
                        i = 0
                        while (i < num) {
                            if (buffer[i].toInt() == 0x0d && buffer[i + 1].toInt() == 0x0a) {
                                buffer_new[n] = 0x0a
                                i++
                            } else {
                                buffer_new[n] = buffer[i]
                            }
                            n++
                            i++
                        }
                        val s = String(buffer_new, 0, n)
//                        smsg += s   //写入接收缓存
                        smsg.append(s)
                        if (inputStream!!.available() == 0) break  //短时间没有数据才跳出进行显示
                    }
                    //发送显示消息，进行显示刷新
                    mHandler.sendEmptyMessage(BLUETOOTH_REC)
                } catch (e: IOException) {
                }

            }
        }
    }

    /**
     *  打开接收线程
     */
    fun openReceiveThread(address : String) {
        // 得到蓝牙设备句柄
        _device = _bluetooth!!.getRemoteDevice(address)

        // 用服务号得到socket
        try {
            _socket = _device!!.createRfcommSocketToServiceRecord(UUID.fromString(MY_UUID))
        } catch (e: IOException) {
            toasts("连接失败！")
            tv_connState.setText(getString(R.string.str_conn_state_disconnect))
            tv_connState.setTextColor(Color.parseColor("#666666")) // 未连接-灰色
            tv_connBlueTooth.text = "连接蓝牙"
            tv_connBlueTooth2.text = "连接蓝牙"
        }

        try {
            tv_connState.setText(getString(R.string.str_conn_state_connected))
            tv_connState.setTextColor(Color.parseColor("#008800")) // 已连接-绿色
            tv_connBlueTooth.text = "断开连接"
            tv_connBlueTooth2.text = "断开连接"
            _socket!!.connect()
        } catch (e: IOException) {
            try {
                toasts("连接失败！")
                tv_connState.setText(getString(R.string.str_conn_state_disconnect))
                tv_connState.setTextColor(Color.parseColor("#666666")) // 未连接-灰色
                tv_connBlueTooth.text = "连接蓝牙"
                tv_connBlueTooth2.text = "连接蓝牙"
                _socket!!.close()
                _socket = null

            } catch (ee: IOException) {
                toasts("连接失败！")
                tv_connState.setText(getString(R.string.str_conn_state_disconnect))
                tv_connState.setTextColor(Color.parseColor("#666666")) // 未连接-灰色
                tv_connBlueTooth.text = "连接蓝牙"
                tv_connBlueTooth2.text = "连接蓝牙"
            }

            return
        }


        //打开接收线程
        try {
            inputStream = _socket!!.getInputStream()   //得到蓝牙数据输入流
        } catch (e: IOException) {
            toasts("接收数据失败！")
            tv_connState.setText(getString(R.string.str_conn_state_disconnect))
            tv_connState.setTextColor(Color.parseColor("#666666")) // 未连接-灰色
            tv_connBlueTooth.text = "连接蓝牙"
            tv_connBlueTooth2.text = "连接蓝牙"
            return
        }

        if (bThread == false) {
            readThread.start()
            bThread = true
        } else {
            bRun = true
        }
    }

    /**
     *  关闭蓝牙服务
     */
    fun closeBlueTooth(isClose : Boolean) {
        //---安全关闭蓝牙连接再退出，避免报异常----//
        if (_socket != null) {
            //关闭连接socket
            try {
                bRun = false
                Thread.sleep(1000)

                inputStream!!.close()
                _socket!!.close()
                _socket = null
            } catch (e: IOException) {
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            tv_connState.setText(getString(R.string.str_conn_state_disconnect))
            tv_connState.setTextColor(Color.parseColor("#666666")) // 未连接-灰色
            tv_connBlueTooth.text = "连接蓝牙"
            tv_connBlueTooth2.text = "连接蓝牙"
        }
        if(isClose) {
            closeHandler(mHandler)
            context.finish()
        }
    }

    //关闭程序掉用处理部分
    public override fun onDestroy() {
        super.onDestroy()
        if (_socket != null)
        //关闭连接socket
            try {
                _socket!!.close()
            } catch (e: IOException) {
            }

    }
}