package ykk.xc.com.wms.basics.adapter

import android.app.Activity
import android.widget.TextView

import ykk.xc.com.wms.R
import ykk.xc.com.wms.bean.k3Bean.ReturnReason
import ykk.xc.com.wms.util.basehelper.BaseArrayRecyclerAdapter
import ykk.xc.com.wms.util.basehelper.BaseRecyclerAdapter

class ReturnReason_DialogAdapter(private val context: Activity, private val datas: List<ReturnReason>) : BaseArrayRecyclerAdapter<ReturnReason>(datas) {
    private var callBack: MyCallBack? = null

    override fun bindView(viewtype: Int): Int {
        return R.layout.ab_returnreason_dialog_item
    }

    override fun onBindHoder(holder: BaseRecyclerAdapter.RecyclerHolder, entity: ReturnReason, pos: Int) {
        // 初始化id
        val tv_row = holder.obtainView<TextView>(R.id.tv_row)
        val tv_fname = holder.obtainView<TextView>(R.id.tv_fname)
        // 赋值
        tv_row!!.setText((pos + 1).toString())
        tv_fname!!.setText(entity.fname)
    }

    fun setCallBack(callBack: MyCallBack) {
        this.callBack = callBack
    }

    interface MyCallBack {
        fun onClick(entity: ReturnReason, position: Int)
    }

}
