package ykk.xc.com.wms.basics

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import butterknife.OnClick
import kotlinx.android.synthetic.main.ab_stock_group_dialog.*
import okhttp3.*
import ykk.xc.com.wms.R
import ykk.xc.com.wms.bean.Stock
import ykk.xc.com.wms.bean.StockArea
import ykk.xc.com.wms.bean.StockPosition
import ykk.xc.com.wms.bean.StorageRack
import ykk.xc.com.wms.comm.BaseDialogActivity
import ykk.xc.com.wms.comm.BaseFragment
import ykk.xc.com.wms.comm.Comm
import ykk.xc.com.wms.util.JsonUtil
import ykk.xc.com.wms.util.LogUtil
import ykk.xc.com.wms.util.zxing.android.CaptureActivity
import java.io.IOException
import java.io.Serializable
import java.lang.ref.WeakReference
import java.util.concurrent.TimeUnit

/**
 * 选择仓库组dialog
 */
class Stock_GroupDialogActivity : BaseDialogActivity() {

    companion object {
        private val SEL_STOCK = 10000
        private val SEL_STOCKAREA = 20000
        private val SEL_STORAGE_RACK = 30000
        private val SEL_STOCKPOS = 40000

        private val SETFOCUS = 1
        private val SAOMA = 2
        private val WRITE_CODE = 3

        private val SUCC1 = 200
        private val UNSUCC1 = 500
        private val SEL_STOCKAUTOBACK = "SEL_STOCKAUTOBACK"
    }
    private val context = this
    private var okHttpClient: OkHttpClient? = null
    private var stock:Stock? = null
    private var stockArea:StockArea? = null
    private var storageRack:StorageRack? = null
    private var stockPos:StockPosition? = null
    private var isTextChange: Boolean = false // 是否进入TextChange事件
    private var smqFlag = '1' // 使用扫码枪扫码（1：仓库扫码，2：库区扫码，3：货架扫码，4：库位扫码）
    private var stockEnable = true // 仓库是否可选择

    // 消息处理
    private val mHandler = MyHandler(this)
    private class MyHandler(activity: Stock_GroupDialogActivity) : Handler() {
        private val mActivity: WeakReference<Stock_GroupDialogActivity>

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
                    SUCC1 -> { // 扫码成功 进入
                        when(m.smqFlag) {
                            '1' -> { // 仓库扫码
                                val stock = JsonUtil.strToObject(msgObj, Stock::class.java)
                                m.getStock(stock,false)
                            }
                            '2' -> { // 库区扫码
                                val stockArea = JsonUtil.strToObject(msgObj, StockArea::class.java)
                                m.getStockArea(stockArea,false)
                            }
                            '3' -> { // 货架扫码
                                val storageRack = JsonUtil.strToObject(msgObj, StorageRack::class.java)
                                m.getStorageRack(storageRack,false)
                            }
                            '4' -> { // 库位扫码
                                val stockPos = JsonUtil.strToObject(msgObj, StockPosition::class.java)
                                var stock:Stock? = null
                                var stockArea:StockArea? = null
                                var storageRack:StorageRack? = null
                                if(stockPos.stock != null) {
                                    stock = stockPos.stock
                                }
                                if(stockPos.stockArea != null) {
                                    stockArea = stockPos.stockArea
                                }
                                if(stockPos.storageRack != null) {
                                    storageRack = stockPos.storageRack
                                }
                                m.getStockGroup(stock, stockArea, storageRack, stockPos)
                            }
                        }
                    }
                    UNSUCC1 -> { // 扫码失败
                        errMsg = JsonUtil.strToString(msgObj)
                        if (m.isNULLS(errMsg).length == 0) errMsg = "很抱歉，没有找到数据！"
                        Comm.showWarnDialog(m.context, errMsg)
                    }
                    SETFOCUS -> { // 当弹出其他窗口会抢夺焦点，需要跳转下，才能正常得到值
                        m.setFocusable(m.et_getFocus)
                        when(m.smqFlag) {
                            '1' -> m.setFocusable(m.et_stockCode)
                            '2' -> m.setFocusable(m.et_stockAreaCode)
                            '3' -> m.setFocusable(m.et_storageRackCode)
                            '4' -> m.setFocusable(m.et_stockPosCode)
                        }
                    }
                    SAOMA -> { // 扫码之后
                        // 执行查询方法
                        m.run_smDatas()
                    }
                }
            }
        }
    }

    override fun setLayoutResID(): Int {
        return R.layout.ab_stock_group_dialog
    }

    override fun initView() {
        if (okHttpClient == null) {
            okHttpClient = OkHttpClient.Builder()
                    //                .connectTimeout(10, TimeUnit.SECONDS) // 设置连接超时时间（默认为10秒）
                    .writeTimeout(30, TimeUnit.SECONDS) // 设置写的超时时间
                    .readTimeout(30, TimeUnit.SECONDS) //设置读取超时时间
                    .build()
        }
        hideSoftInputMode(et_stockCode)
        hideSoftInputMode(et_stockAreaCode)
        hideSoftInputMode(et_storageRackCode)
        hideSoftInputMode(et_stockPosCode)

        val spf = spf(getResStr(R.string.saveOther))
        if(spf.contains(SEL_STOCKAUTOBACK)) {
            cb_stockAutoConfirm.isChecked = true
        }
    }

    override fun initData() {
        val bundle = context.intent.extras
        if (bundle != null) {
            if(bundle.containsKey("stock") && bundle.getSerializable("stock") != null) {
                stock = bundle.getSerializable("stock") as Stock
                getStock(stock!!,false)
            }
            if(bundle.containsKey("stockArea") && bundle.getSerializable("stockArea") != null) {
                stockArea = bundle.getSerializable("stockArea") as StockArea
                getStockArea(stockArea!!,false)
            }
            if(bundle.containsKey("storageRack") && bundle.getSerializable("storageRack") != null) {
                storageRack = bundle.getSerializable("storageRack") as StorageRack
                getStorageRack(storageRack!!,false)
            }
            if(bundle.containsKey("stockPos") && bundle.getSerializable("stockPos") != null) {
                stockPos = bundle.getSerializable("stockPos") as StockPosition
                getStockPos(stockPos!!,false)
            }
            stockEnable = bundle.getBoolean("stockEnable", true)
            if(!stockEnable && stock != null) { // 仓库不能选择
                setEnables(lin_focusStock, R.drawable.back_style_gray3, true)
                btn_stockScan.isEnabled = false
                btn_stockSel.isEnabled = false
                et_stockCode.isEnabled = false
                tv_stockName.isEnabled = false
                // 是否启用了库区
                if(stock!!.useStockArea.equals("Y")) {
                    smqFlag = '2'
                } else if(stock!!.useStorageRack.equals("Y")) { // 是否启用了货架
                    smqFlag = '3'
                } else if(stock!!.fisStockMgr == 1) { // 是否启用了库位
                    smqFlag = '4'
                }
            }
        }
        if(stock == null) {
            showForResult(Stock_DialogActivity::class.java, SEL_STOCK, null)
        }
        mHandler.sendEmptyMessageDelayed(SETFOCUS, 200)
    }

    // 监听事件
    @OnClick( R.id.btn_close, R.id.btn_stockSel, R.id.btn_stockAreaSel, R.id.btn_storageRackSel, R.id.btn_stockPosSel,
              R.id.tv_stockName, R.id.tv_stockAreaName, R.id.tv_storageRackName, R.id.tv_stockPosName,
              R.id.btn_stockScan,R.id.btn_stockAreaScan,R.id.btn_storageRackScan,R.id.btn_stockPosScan,R.id.btn_confirm )
    fun onViewClicked(view: View) {
        when (view.id) {
            R.id.btn_close -> {
                context.finish()
            }
            R.id.btn_stockSel -> { // 仓库
                showForResult(Stock_DialogActivity::class.java, SEL_STOCK, null)
            }
            R.id.btn_stockAreaSel -> { // 库区
                val bundle = Bundle()
                bundle.putInt("stockId", if(stock != null)stock!!.id else 0)
                showForResult(StockArea_DialogActivity::class.java, SEL_STOCKAREA, bundle)
            }
            R.id.btn_storageRackSel -> { // 货架
                val bundle = Bundle()
                bundle.putInt("stockId", if(stock != null)stock!!.id else 0)
                bundle.putInt("stockAreaId", if(stockArea != null)stockArea!!.id else 0)
                showForResult(StorageRack_DialogActivity::class.java, SEL_STORAGE_RACK, bundle)
            }
            R.id.btn_stockPosSel -> { // 库位
                val bundle = Bundle()
                bundle.putInt("stockId", if(stock != null)stock!!.id else 0)
                bundle.putInt("stockAreaId", if(stockArea != null)stockArea!!.id else 0)
                bundle.putInt("storageRackId", if(storageRack != null)storageRack!!.id else 0)
                showForResult(StockPos_DialogActivity::class.java, SEL_STOCKPOS, bundle)
            }
            R.id.btn_stockScan -> { // 调用摄像头扫描（仓库）
                smqFlag = '1'
                showForResult(CaptureActivity::class.java, BaseFragment.CAMERA_SCAN, null)
            }
            R.id.btn_stockAreaScan -> { // 调用摄像头扫描（库区）
                smqFlag = '2'
                showForResult(CaptureActivity::class.java, BaseFragment.CAMERA_SCAN, null)
            }
            R.id.btn_storageRackScan -> { // 调用摄像头扫描（货架）
                smqFlag = '3'
                showForResult(CaptureActivity::class.java, BaseFragment.CAMERA_SCAN, null)
            }
            R.id.btn_stockPosScan -> { // 调用摄像头扫描（库位）
                smqFlag = '4'
                showForResult(CaptureActivity::class.java, BaseFragment.CAMERA_SCAN, null)
            }
            R.id.tv_stockName -> { // 仓库点击
                smqFlag = '1'
                mHandler.sendEmptyMessageDelayed(SETFOCUS,200)
            }
            R.id.tv_stockAreaName -> { // 库区点击
                smqFlag = '2'
                mHandler.sendEmptyMessageDelayed(SETFOCUS,200)
            }
            R.id.tv_storageRackName -> { // 货架点击
                smqFlag = '3'
                mHandler.sendEmptyMessageDelayed(SETFOCUS,200)
            }
            R.id.tv_stockPosName -> { // 库位点击
                smqFlag = '4'
                mHandler.sendEmptyMessageDelayed(SETFOCUS,200)
            }
            R.id.btn_confirm -> { // 确认
                if(stock == null) { // 是否启用了库区
                    Comm.showWarnDialog(context, "请选择仓库！")
                    return
                }
                if(stock!!.useStockArea.equals("Y") && stockArea == null) { // 是否启用了库区
                    Comm.showWarnDialog(context, "请选择库区！")
                    return
                }
                if(stock!!.useStorageRack.equals("Y") && storageRack == null) { // 是否启用了货架
                    Comm.showWarnDialog(context, "请选择货架！")
                    return
                }
                if(stock!!.fisStockMgr == 1 && stockPos == null) { // 是否启用了库位
                    Comm.showWarnDialog(context, "请选择库位！")
                    return
                }
                // 传送对象
                sendObj()
            }
        }
    }

    override fun setListener() {
        val click = View.OnClickListener { v ->
            setFocusable(et_getFocus)
            when (v.id) {
                R.id.et_stockCode -> setFocusable(et_stockCode)
                R.id.et_stockAreaCode -> setFocusable(et_stockAreaCode)
                R.id.et_storageRackCode -> setFocusable(et_storageRackCode)
                R.id.et_stockPosCode -> setFocusable(et_stockPosCode)
            }
        }
        et_stockCode!!.setOnClickListener(click)
        et_stockAreaCode!!.setOnClickListener(click)
        et_storageRackCode!!.setOnClickListener(click)
        et_stockPosCode!!.setOnClickListener(click)

        // 仓库---数据变化
        et_stockCode!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (s.length == 0) return
                if (!isTextChange) {
                    isTextChange = true
                    smqFlag = '1'
                    mHandler.sendEmptyMessageDelayed(SAOMA, 300)
                }
            }
        })
        // 仓库---长按输入条码
        et_stockCode!!.setOnLongClickListener {
            smqFlag = '1'
            showInputDialog("输入条码", "", "none", WRITE_CODE)
            true
        }
        // 仓库---焦点改变
        et_stockCode.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if(hasFocus) {
                lin_focusStock.setBackgroundResource(R.drawable.back_style_red_focus)
            } else {
                if (lin_focusStock != null) {
                    lin_focusStock!!.setBackgroundResource(R.drawable.back_style_gray4)
                }
            }
        }

        // 库区---数据变化
        et_stockAreaCode!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (s.length == 0) return
                if (!isTextChange) {
                    isTextChange = true
                    smqFlag = '2'
                    mHandler.sendEmptyMessageDelayed(SAOMA, 300)
                }
            }
        })
        // 库区---长按输入条码
        et_stockAreaCode!!.setOnLongClickListener {
            smqFlag = '2'
            showInputDialog("输入条码", "", "none", WRITE_CODE)
            true
        }
        // 库区---焦点改变
        et_stockAreaCode.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if(hasFocus) {
                lin_focusStockArea.setBackgroundResource(R.drawable.back_style_red_focus)
            } else {
                if (lin_focusStockArea != null) {
                    lin_focusStockArea!!.setBackgroundResource(R.drawable.back_style_gray4)
                }
            }
        }

        // 货架---数据变化
        et_storageRackCode!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (s.length == 0) return
                if (!isTextChange) {
                    isTextChange = true
                    smqFlag = '3'
                    mHandler.sendEmptyMessageDelayed(SAOMA, 300)
                }
            }
        })
        // 货架---长按输入条码
        et_storageRackCode!!.setOnLongClickListener {
            smqFlag = '3'
            showInputDialog("输入条码", "", "none", WRITE_CODE)
            true
        }
        // 货架---焦点改变
        et_storageRackCode.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if(hasFocus) {
                lin_focusStorageRack.setBackgroundResource(R.drawable.back_style_red_focus)
            } else {
                if (lin_focusStorageRack != null) {
                    lin_focusStorageRack!!.setBackgroundResource(R.drawable.back_style_gray4)
                }
            }
        }

        // 库位---数据变化
        et_stockPosCode!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (s.length == 0) return
                if (!isTextChange) {
                    isTextChange = true
                    smqFlag = '4'
                    mHandler.sendEmptyMessageDelayed(SAOMA, 300)
                }
            }
        })
        // 库位---长按输入条码
        et_stockPosCode!!.setOnLongClickListener {
            smqFlag = '4'
            showInputDialog("输入条码", "", "none", WRITE_CODE)
            true
        }
        // 库位---焦点改变
        et_stockPosCode.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if(hasFocus) {
                lin_focusStockPos.setBackgroundResource(R.drawable.back_style_red_focus)
            } else {
                if (lin_focusStockPos != null) {
                    lin_focusStockPos!!.setBackgroundResource(R.drawable.back_style_gray4)
                }
            }
        }

        // 自动返回
        cb_stockAutoConfirm.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) {
                spf(getResStr(R.string.saveOther)).edit().putBoolean(SEL_STOCKAUTOBACK, true).commit()
            } else {
                spf(getResStr(R.string.saveOther)).edit().remove(SEL_STOCKAUTOBACK).commit()
            }
        }
    }

    private fun sendObj() {
        // 传送对象
        val intent = Intent()
        intent.putExtra("stock", stock as Serializable)
        if(stockArea != null) {
            intent.putExtra("stockArea", stockArea as Serializable)
        }
        if(storageRack != null) {
            intent.putExtra("storageRack", storageRack as Serializable)
        }
        if(stockPos != null) {
            intent.putExtra("stockPos", stockPos as Serializable)
        }
        context.setResult(Activity.RESULT_OK, intent)
        context.finish()
    }

    private fun stockStartParam() {
        // 是否启用了库区
        if(stock!!.useStockArea.equals("Y")) {
            setEnables(lin_focusStockArea, R.drawable.back_style_blue, true)
            btn_stockAreaScan.isEnabled = true
            btn_stockAreaSel.isEnabled = true
            et_stockAreaCode.isEnabled = true
            tv_stockAreaName.isEnabled = true
        } else {
            setEnables(lin_focusStockArea, R.drawable.back_style_gray3, false)
            btn_stockAreaScan.isEnabled = false
            btn_stockAreaSel.isEnabled = false
            et_stockAreaCode.isEnabled = false
            tv_stockAreaName.isEnabled = false
        }

        // 是否启用了货架
        if(stock!!.useStorageRack.equals("Y")) {
            setEnables(lin_focusStorageRack, R.drawable.back_style_blue, true)
            btn_storageRackScan.isEnabled = true
            btn_storageRackSel.isEnabled = true
            et_storageRackCode.isEnabled = true
            tv_storageRackName.isEnabled = true
        } else {
            setEnables(lin_focusStorageRack, R.drawable.back_style_gray3, false)
            btn_storageRackScan.isEnabled = false
            btn_storageRackSel.isEnabled = false
            et_storageRackCode.isEnabled = false
            tv_storageRackName.isEnabled = false
        }

        // 是否启用了库位
        if(stock!!.fisStockMgr == 1) {
            setEnables(lin_focusStockPos, R.drawable.back_style_blue, true)
            btn_stockPosScan.isEnabled = true
            btn_stockPosSel.isEnabled = true
            et_stockPosCode.isEnabled = true
            tv_stockPosName.isEnabled = true
        } else {
            setEnables(lin_focusStockPos, R.drawable.back_style_gray3, false)
            btn_stockPosScan.isEnabled = false
            btn_stockPosSel.isEnabled = false
            et_stockPosCode.isEnabled = false
            tv_stockPosName.isEnabled = false
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            SEL_STOCK -> {// 仓库	返回
                if (resultCode == Activity.RESULT_OK) {
                    stock = data!!.getSerializableExtra("obj") as Stock
                    getStock(stock!!,true)
                }
            }
            SEL_STOCKAREA -> { // 库区	返回
                if (resultCode == Activity.RESULT_OK) {
                    stockArea = data!!.getSerializableExtra("obj") as StockArea
                    getStockArea(stockArea!!,true)
                }
            }
            SEL_STORAGE_RACK -> { // 货架	返回
                if (resultCode == Activity.RESULT_OK) {
                    storageRack = data!!.getSerializableExtra("obj") as StorageRack
                    getStorageRack(storageRack!!,true)
                }
            }
            SEL_STOCKPOS -> { // 库位	返回
                if (resultCode == Activity.RESULT_OK) {
                    stockPos = data!!.getSerializableExtra("obj") as StockPosition
                    getStockPos(stockPos!!, true)
                }
            }
            BaseFragment.CAMERA_SCAN -> {// 扫一扫成功  返回
                if (resultCode == Activity.RESULT_OK) {
                    val bundle = data!!.extras
                    if (bundle != null) {
                        val code = bundle.getString(BaseFragment.DECODED_CONTENT_KEY, "")
                        when(smqFlag) {
                            '1' -> setTexts(et_stockCode, code)
                            '2' -> setTexts(et_stockAreaCode, code)
                            '3' -> setTexts(et_storageRackCode, code)
                            '4' -> setTexts(et_stockPosCode, code)
                        }
                    }
                }
            }
            WRITE_CODE -> {// 输入条码  返回
                if (resultCode == Activity.RESULT_OK) {
                    val bundle = data!!.extras
                    if (bundle != null) {
                        val value = bundle.getString("resultValue", "")
                        when(smqFlag) {
                            '1' -> setTexts(et_stockCode, value.toUpperCase())
                            '2' -> setTexts(et_stockAreaCode, value.toUpperCase())
                            '3' -> setTexts(et_storageRackCode, value.toUpperCase())
                            '4' -> setTexts(et_stockPosCode, value.toUpperCase())
                        }
                    }
                }
            }
        }
        mHandler.sendEmptyMessageDelayed(SETFOCUS, 200)
    }

    /**
     * 得到仓库
     */
    fun getStock(stock :Stock, autoNext :Boolean) {
        context.stock = stock
        tv_stockName.text = stock!!.stockName

        stockStartParam()
        stockArea = null
        storageRack = null
        stockPos = null
        tv_stockAreaName.text = ""
        tv_storageRackName.text = ""
        tv_stockPosName.text = ""

        var isBool = true
        // 只有选择的时候，自动打开下一级
        if(autoNext && btn_stockAreaSel.isEnabled) {
            isBool = false
            val bundle = Bundle()
            bundle.putInt("stockId", if(stock != null)stock!!.id else 0)
            showForResult(StockArea_DialogActivity::class.java, SEL_STOCKAREA, bundle)

        } else if(autoNext && btn_storageRackSel.isEnabled) {
            isBool = false
            val bundle = Bundle()
            bundle.putInt("stockId", if(stock != null)stock!!.id else 0)
            bundle.putInt("stockAreaId", if(stockArea != null)stockArea!!.id else 0)
            showForResult(StorageRack_DialogActivity::class.java, SEL_STORAGE_RACK, bundle)

        } else if(autoNext && btn_stockPosSel.isEnabled) {
            isBool = false
            val bundle = Bundle()
            bundle.putInt("stockId", if(stock != null)stock!!.id else 0)
            bundle.putInt("stockAreaId", if(stockArea != null)stockArea!!.id else 0)
            bundle.putInt("storageRackId", if(storageRack != null)storageRack!!.id else 0)
            showForResult(StockPos_DialogActivity::class.java, SEL_STOCKPOS, bundle)
        }

        // 自动返回
        if(isBool && autoNext && cb_stockAutoConfirm.isChecked) {
            sendObj()
        }
    }

    /**
     * 得到库区
     */
    fun getStockArea(stockArea : StockArea, autoNext :Boolean) {
        context.stockArea = stockArea
        tv_stockAreaName.text = stockArea!!.fname

        storageRack = null
        stockPos = null
        tv_storageRackName.text = ""
        tv_stockPosName.text = ""

        var isBool = true
        // 只有选择的时候，自动打开下一级
        if(autoNext && btn_storageRackSel.isEnabled) {
            isBool = false
            val bundle = Bundle()
            bundle.putInt("stockId", if(stock != null)stock!!.id else 0)
            bundle.putInt("stockAreaId", if(stockArea != null)stockArea!!.id else 0)
            showForResult(StorageRack_DialogActivity::class.java, SEL_STORAGE_RACK, bundle)

        } else if(autoNext && btn_stockPosSel.isEnabled) {
            isBool = false
            val bundle = Bundle()
            bundle.putInt("stockId", if(stock != null)stock!!.id else 0)
            bundle.putInt("stockAreaId", if(stockArea != null)stockArea!!.id else 0)
            bundle.putInt("storageRackId", if(storageRack != null)storageRack!!.id else 0)
            showForResult(StockPos_DialogActivity::class.java, SEL_STOCKPOS, bundle)
        }

        // 自动返回
        if(isBool && autoNext && cb_stockAutoConfirm.isChecked) {
            sendObj()
        }
    }

    /**
     * 得到货架
     */
    fun getStorageRack(storageRack : StorageRack, autoNext :Boolean) {
        context.storageRack = storageRack
        tv_storageRackName.text = storageRack!!.fnumber

        stockPos = null
        tv_stockPosName.text = ""

        var isBool = true
        // 只有选择的时候，自动打开下一级
        if(autoNext && autoNext && btn_stockPosSel.isEnabled) {
            isBool = false
            val bundle = Bundle()
            bundle.putInt("stockId", if(stock != null)stock!!.id else 0)
            bundle.putInt("stockAreaId", if(stockArea != null)stockArea!!.id else 0)
            bundle.putInt("storageRackId", if(storageRack != null)storageRack!!.id else 0)
            showForResult(StockPos_DialogActivity::class.java, SEL_STOCKPOS, bundle)
        }

        // 自动返回
        if(isBool && autoNext && cb_stockAutoConfirm.isChecked) {
            sendObj()
        }
    }

    /**
     * 得到库位
     */
    fun getStockPos(stockPos : StockPosition, autoNext :Boolean) {
        context.stockPos = stockPos
        tv_stockPosName.text = stockPos!!.stockPositionName

        // 自动返回
        if(autoNext && cb_stockAutoConfirm.isChecked) {
            sendObj()
        }
    }

    /**
     * 根据库位扫描得到（仓库，库区，货架，库位）
     */
    fun getStockGroup(stock: Stock?, stockArea: StockArea?, storageRack: StorageRack?, stockPos : StockPosition?) {
        if(context.stock == null && stock != null) {
            getStock(stock,false)
        }
        if(context.stockArea == null && stockArea != null) {
            getStockArea(stockArea,false)
        }
        if(context.storageRack == null && storageRack != null) {
            getStorageRack(storageRack,false)
        }
        if(context.stockPos == null && stockPos != null) {
            getStockPos(stockPos, false)
        }
    }

    /**
     * 扫码查询对应的方法
     */
    private fun run_smDatas() {
        isTextChange = false
        showLoadDialog("加载中...", false)
        var mUrl:String? = null
        var barcode:String? = null
        when(smqFlag) {
            '1' -> {
                mUrl = getURL("stock/findBarcode")
                barcode = getValues(et_stockCode)
            }
            '2' -> {
                mUrl = getURL("stockArea/findBarcode")
                barcode = getValues(et_stockAreaCode)
            }
            '3' -> {
                mUrl = getURL("storageRack/findBarcode")
                barcode = getValues(et_storageRackCode)
            }
            '4' -> {
                mUrl = getURL("stockPosition/findBarcode")
                barcode = getValues(et_stockPosCode)
            }
        }
        val formBody = FormBody.Builder()
                .add("barcode", barcode)
                .add("stockId", if(stock != null) stock!!.id.toString() else "" )
                .add("stockAreaId", if(stockArea != null) stockArea!!.id.toString() else "" )
                .add("storageRackId", if(storageRack != null) storageRack!!.id.toString() else "" )
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
                LogUtil.e("run_smDatas --> onResponse", result)
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

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            context.finish()
        }
        return false
    }

    override fun onDestroy() {
        closeHandler(mHandler)
        super.onDestroy()
    }

}
