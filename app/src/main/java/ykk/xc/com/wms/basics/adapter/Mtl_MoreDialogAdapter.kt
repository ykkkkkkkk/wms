package ykk.xc.com.wms.basics.adapter

import android.app.Activity
import android.text.Html
import android.view.View
import android.widget.TextView
import ykk.xc.com.wms.R
import ykk.xc.com.wms.bean.k3Bean.ICItem
import ykk.xc.com.wms.util.basehelper.BaseArrayRecyclerAdapter
import ykk.xc.com.wms.util.basehelper.BaseRecyclerAdapter

class Mtl_MoreDialogAdapter(private val context: Activity, private val datas: List<ICItem>) : BaseArrayRecyclerAdapter<ICItem>(datas) {
    private var callBack: MyCallBack? = null

    override fun bindView(viewtype: Int): Int {
        return R.layout.ab_mtl_list_more_item
    }

    override fun onBindHoder(holder: BaseRecyclerAdapter.RecyclerHolder, entity: ICItem, pos: Int) {
        // 初始化id
        val tv_row = holder.obtainView<TextView>(R.id.tv_row)
        val tv_fnumber = holder.obtainView<TextView>(R.id.tv_fnumber)
        val tv_fname = holder.obtainView<TextView>(R.id.tv_fname)
        val tv_fmodel = holder.obtainView<TextView>(R.id.tv_fmodel)
        val view_check = holder.obtainView<View>(R.id.view_check)
        // 赋值
        tv_row!!.setText((pos + 1).toString())
        tv_fname!!.setText(entity.fname)
        tv_fnumber.text = Html.fromHtml("代码:&nbsp;<font color='#6a5acd'>"+entity.fnumber+"</font>")
        tv_fmodel.text = Html.fromHtml("规格:&nbsp;<font color='#6a5acd'>"+entity.fmodel+"</font>")

        if (entity.isCheck) {
            view_check.setBackgroundResource(R.drawable.check_true)
        } else {
            view_check.setBackgroundResource(R.drawable.check_false)
        }
    }

    fun setCallBack(callBack: MyCallBack) {
        this.callBack = callBack
    }

    interface MyCallBack {
        fun onClick(entity: ICItem, position: Int)
    }

}
