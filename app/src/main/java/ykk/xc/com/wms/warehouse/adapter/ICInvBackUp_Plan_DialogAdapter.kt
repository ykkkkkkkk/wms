package ykk.xc.com.wms.warehouse.adapter

import android.app.Activity
import android.widget.TextView
import ykk.xc.com.wms.R
import ykk.xc.com.wms.bean.ICInvBackUp_Plan
import ykk.xc.com.wms.util.basehelper.BaseArrayRecyclerAdapter
import ykk.xc.com.wms.util.basehelper.BaseRecyclerAdapter

class ICInvBackUp_Plan_DialogAdapter(private val context: Activity, private val datas: List<ICInvBackUp_Plan>) : BaseArrayRecyclerAdapter<ICInvBackUp_Plan>(datas) {
    private var callBack: MyCallBack? = null

    override fun bindView(viewtype: Int): Int {
        return R.layout.ware_icinvbackup_plan_dialog_item
    }

    override fun onBindHoder(holder: BaseRecyclerAdapter.RecyclerHolder, entity: ICInvBackUp_Plan, pos: Int) {
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
        fun onClick(entity: ICInvBackUp_Plan, position: Int)
    }

}
