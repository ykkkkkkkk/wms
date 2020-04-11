package ykk.xc.com.wms.warehouse.adapter

import android.app.Activity
import android.text.Html
import android.view.View
import android.widget.TextView
import ykk.xc.com.wms.R
import ykk.xc.com.wms.bean.k3Bean.ICItem
import ykk.xc.com.wms.util.basehelper.BaseArrayRecyclerAdapter
import ykk.xc.com.wms.util.basehelper.BaseRecyclerAdapter

class ICInvBackup_Sel_Material_Fragment2Adapter(context: Activity, datas: List<ICItem>) : BaseArrayRecyclerAdapter<ICItem>(datas) {
    private var callBack: MyCallBack? = null

    override fun bindView(viewtype: Int): Int {
        return R.layout.ware_icinvbackup_sel_material_fragment2_item
    }

    override fun onBindHoder(holder: BaseRecyclerAdapter.RecyclerHolder, entity: ICItem, pos: Int) {
        // 初始化id
        // 初始化id
        val tv_row = holder.obtainView<TextView>(R.id.tv_row)
        val tv_fnumber = holder.obtainView<TextView>(R.id.tv_fnumber)
        val tv_fname = holder.obtainView<TextView>(R.id.tv_fname)
        val tv_fmodel = holder.obtainView<TextView>(R.id.tv_fmodel)
        // 赋值
        tv_row!!.setText((pos + 1).toString())
        tv_fname!!.setText(entity.fname)
        tv_fnumber.text = Html.fromHtml("代码:&nbsp;<font color='#6a5acd'>"+entity.fnumber+"</font>")
        tv_fmodel.text = Html.fromHtml("规格:&nbsp;<font color='#6a5acd'>"+entity.fmodel+"</font>")

        val view = tv_row!!.getParent() as View
        if (entity.isCheck) {
            view.setBackgroundResource(R.drawable.back_style_check1_true)
        } else {
            view.setBackgroundResource(R.drawable.back_style_check1_false)
        }
    }

    fun setCallBack(callBack: MyCallBack) {
        this.callBack = callBack
    }

    interface MyCallBack {
        fun onClick(entity: ICItem, position: Int)
    }

}
