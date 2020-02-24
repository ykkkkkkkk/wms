package ykk.xc.com.wms.produce.adapter

import android.app.Activity
import android.view.View
import android.widget.TextView
import ykk.xc.com.wms.R
import ykk.xc.com.wms.bean.prod.ProdOrder
import ykk.xc.com.wms.util.basehelper.BaseArrayRecyclerAdapter
import ykk.xc.com.wms.util.basehelper.BaseRecyclerAdapter
import java.text.DecimalFormat

class Prod_InStock_Sel_ProdOrder_Adapter(private val context: Activity, private val datas: List<ProdOrder>) : BaseArrayRecyclerAdapter<ProdOrder>(datas) {
    private val df = DecimalFormat("#.####")
    private var callBack: MyCallBack? = null

    override fun bindView(viewtype: Int): Int {
        return R.layout.prod_sel_order_item
    }

    override fun onBindHoder(holder: BaseRecyclerAdapter.RecyclerHolder, entity: ProdOrder, pos: Int) {
        // 初始化id
        val tv_row = holder.obtainView<TextView>(R.id.tv_row)
        val tv_date = holder.obtainView<TextView>(R.id.tv_date)
        val tv_fbillNo = holder.obtainView<TextView>(R.id.tv_fbillNo)
        val tv_mtlName = holder.obtainView<TextView>(R.id.tv_mtlName)
        val tv_numUnit = holder.obtainView<TextView>(R.id.tv_numUnit)
        val tv_deptName = holder.obtainView<TextView>(R.id.tv_deptName)

        // 赋值
        tv_row!!.setText((pos + 1).toString())
        tv_date!!.setText(entity.fconfirmDate)
        tv_fbillNo!!.setText(entity.prodNo)
        tv_mtlName!!.setText(entity.icItem.fname)
        tv_numUnit!!.setText(df.format(entity.useableQty) + "" + entity.unitName)
        tv_deptName!!.setText(entity.department.departmentName)

        val view = tv_row!!.getParent() as View
        if (entity.isCheck == 1) {
            view.setBackgroundResource(R.drawable.back_style_check1_true)
        } else {
            view.setBackgroundResource(R.drawable.back_style_check1_false)
        }
    }

    fun setCallBack(callBack: MyCallBack) {
        this.callBack = callBack
    }

    interface MyCallBack {
        fun onClick(entity: ProdOrder, position: Int)
    }


}
