package ykk.xc.com.wms.purchase.adapter

import android.app.Activity
import android.view.View
import android.widget.TextView
import ykk.xc.com.wms.R
import ykk.xc.com.wms.bean.pur.POOrderEntry
import ykk.xc.com.wms.util.basehelper.BaseArrayRecyclerAdapter
import ykk.xc.com.wms.util.basehelper.BaseRecyclerAdapter
import java.text.DecimalFormat

class Pur_InStock_Sel_POOrderAdapter(private val context: Activity, private val datas: List<POOrderEntry>) : BaseArrayRecyclerAdapter<POOrderEntry>(datas) {
    private val df = DecimalFormat("#.####")
    private var callBack: MyCallBack? = null

    override fun bindView(viewtype: Int): Int {
        return R.layout.pur_sel_order_item
    }

    override fun onBindHoder(holder: BaseRecyclerAdapter.RecyclerHolder, entity: POOrderEntry, pos: Int) {
        // 初始化id
        val tv_row = holder.obtainView<TextView>(R.id.tv_row)
        val tv_date = holder.obtainView<TextView>(R.id.tv_date)
        val tv_fbillNo = holder.obtainView<TextView>(R.id.tv_fbillNo)
        val tv_mtlName = holder.obtainView<TextView>(R.id.tv_mtlName)
        val tv_numUnit = holder.obtainView<TextView>(R.id.tv_numUnit)
        val tv_deptName = holder.obtainView<TextView>(R.id.tv_deptName)

        // 赋值
        val pur = entity.poOrder
        tv_row!!.setText((pos + 1).toString())
        tv_date!!.setText(pur.fdate)
        tv_fbillNo!!.setText(pur.fbillno)
        tv_mtlName!!.setText(entity.icItem.fname)
        tv_numUnit!!.setText(df.format(entity.useableQty) + "" + entity.unitName)
        tv_deptName!!.setText(pur.department.departmentName)

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
        fun onClick(entity: POOrderEntry, position: Int)
    }


}
