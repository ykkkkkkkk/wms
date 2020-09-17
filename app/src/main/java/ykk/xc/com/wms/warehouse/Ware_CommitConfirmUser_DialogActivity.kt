package ykk.xc.com.wms.warehouse

import android.app.Activity
import android.content.Intent
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.KeyEvent
import android.view.View
import butterknife.OnClick
import kotlinx.android.synthetic.main.ware_commit_confirm_user_dialog.*
import okhttp3.*
import ykk.xc.com.wms.R
import ykk.xc.com.wms.basics.User_DialogActivity
import ykk.xc.com.wms.bean.User
import ykk.xc.com.wms.comm.BaseDialogActivity
import ykk.xc.com.wms.comm.Comm
import ykk.xc.com.wms.util.JsonUtil
import java.io.IOException
import java.lang.ref.WeakReference

/**
 * 选择确认人
 */
class Ware_CommitConfirmUser_DialogActivity : BaseDialogActivity() {

    private val context = this
    private val okHttpClient = OkHttpClient()
    private var user: User? = null
    private val SEL_USER = 60
    private var confirmUserId = 0
    private var icstockBillId = 0

    // 消息处理
    private val mHandler = MyHandler(this)

    private class MyHandler(activity: Ware_CommitConfirmUser_DialogActivity) : Handler() {
        private val mActivity: WeakReference<Ware_CommitConfirmUser_DialogActivity>

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
                    SUCC1 // 成功
                    -> {
                        m.toasts("提交成功")
                        m.finish()
                    }
                    UNSUCC1 // 数据加载失败！
                    -> {
                        errMsg = JsonUtil.strToString(msgObj)
                        if (m.isNULLS(errMsg).length == 0) errMsg = "服务器繁忙，请稍后再试！"
                        Comm.showWarnDialog(m.context, errMsg)
                    }
                }
            }
        }

    }

    override fun setLayoutResID(): Int {
        return R.layout.ware_commit_confirm_user_dialog
    }

    override fun initView() {
        getUserInfo()
    }

    override fun initData() {
        val bundle = context.intent.extras
        if (bundle != null) {
            icstockBillId = bundle.getInt("icstockBillId")
        }
    }


    // 监听事件
    @OnClick(R.id.btn_close, R.id.tv_userSel, R.id.btn_confirm)
    fun onViewClicked(view: View) {
        when (view.id) {
            R.id.btn_close -> {
                closeHandler(mHandler)
                context.finish()
            }
            R.id.tv_userSel -> { // 选择用户
                showForResult(User_DialogActivity::class.java, SEL_USER, null)
            }
            R.id.btn_confirm // 确认
            -> {
                run_save()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                SEL_USER -> {// 员工	返回
                    val userT = data!!.getSerializableExtra("obj") as User
                    if (userT.id == user!!.id) {
                        Comm.showWarnDialog(context, "确认人不能是自己！")
                        return
                    }
                    confirmUserId = userT.id
                    tv_userSel.text = userT.username
                }
            }
        }
    }

    /**
     * 保存
     */
    private fun run_save() {
        if(confirmUserId == 0) {
            Comm.showWarnDialog(context,"请选择确认人！")
            return
        }
        showLoadDialog("加载中...", false)
        val mUrl = getURL("billConfirmList/add")
        val formBody = FormBody.Builder()
                .add("commitUserId", user!!.id.toString())
                .add("confirmUserId", confirmUserId.toString())
                .add("icstockBillId", icstockBillId.toString()) // 出入库id
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
                    val msg = mHandler.obtainMessage(UNSUCC1, result)
                    mHandler.sendMessage(msg)
                    return
                }

                val msg = mHandler.obtainMessage(SUCC1, result)
                Log.e("run_save --> onResponse", result)
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

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            closeHandler(mHandler)
            context.finish()
        }
        return false
    }

    override fun onDestroy() {
        closeHandler(mHandler)
        super.onDestroy()
    }

    companion object {
        private val SUCC1 = 200
        private val UNSUCC1 = 501
    }
}
