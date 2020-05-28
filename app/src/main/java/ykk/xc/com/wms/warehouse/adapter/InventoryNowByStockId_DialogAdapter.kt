package ykk.xc.com.wms.warehouse.adapter

import android.app.Activity
import android.text.Html
import android.view.View
import android.widget.TextView
import ykk.xc.com.wms.R
import ykk.xc.com.wms.bean.InventoryNow
import ykk.xc.com.wms.comm.Comm
import ykk.xc.com.wms.util.basehelper.BaseArrayRecyclerAdapter
import ykk.xc.com.wms.util.basehelper.BaseRecyclerAdapter
import java.text.DecimalFormat

class InventoryNowByStockId_DialogAdapter(private val context: Activity, private val datas: List<InventoryNow>) : BaseArrayRecyclerAdapter<InventoryNow>(datas) {
    private var callBack: MyCallBack? = null
    private val df = DecimalFormat("#.######")

    override fun bindView(viewtype: Int): Int {
        return R.layout.ware_inventorynow_by_stock_dialog_item
    }

    override fun onBindHoder(holder: BaseRecyclerAdapter.RecyclerHolder, entity: InventoryNow, pos: Int) {
        // 初始化id
        val tv_row = holder.obtainView<TextView>(R.id.tv_row)
        val tv_fnumber = holder.obtainView<TextView>(R.id.tv_fnumber)
        val tv_fname = holder.obtainView<TextView>(R.id.tv_fname)
        val tv_fmodel = holder.obtainView<TextView>(R.id.tv_fmodel)
        val tv_batchCode = holder.obtainView<TextView>(R.id.tv_batchCode)
        val tv_nowQty = holder.obtainView<TextView>(R.id.tv_nowQty)
        val tv_avbQty = holder.obtainView<TextView>(R.id.tv_avbQty)

        // 赋值
        tv_row.text = (pos+1).toString()
        tv_fname!!.setText(entity.icItem.fname)
        tv_fnumber.text = Html.fromHtml("代码:&nbsp;<font color='#6a5acd'>"+entity.icItem.fnumber+"</font>")
        tv_fmodel.text = Html.fromHtml("规格:&nbsp;<font color='#6a5acd'>"+entity.icItem.fmodel+"</font>")
        tv_batchCode.text = Html.fromHtml("批次:&nbsp;<font color='#6a5acd'>"+ Comm.isNULLS(entity.batchCode)+"</font>")
        tv_nowQty.text = Html.fromHtml("库存数:&nbsp;<font color='#6a5acd'>"+ df.format(entity.nowQty) +"</font>&nbsp;<font color='#666666'>"+ entity.unit.unitName +"</font>")
        tv_avbQty.text = Html.fromHtml("可用数:&nbsp;<font color='#FF0000'>"+ df.format(entity.avbQty) +"</font>")
    }

    fun setCallBack(callBack: MyCallBack) {
        this.callBack = callBack
    }

    interface MyCallBack {
        fun onClick(entity: InventoryNow, position: Int)
    }

}
