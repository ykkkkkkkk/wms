package ykk.xc.com.wms.warehouse.adapter

import android.app.Activity
import android.text.Html
import android.view.View
import android.widget.TextView
import ykk.xc.com.wms.R
import ykk.xc.com.wms.bean.InventoryNow
import ykk.xc.com.wms.util.basehelper.BaseArrayRecyclerAdapter
import ykk.xc.com.wms.util.basehelper.BaseRecyclerAdapter
import java.text.DecimalFormat

class InventoryNowByMtl_DialogAdapter(private val context: Activity, private val datas: List<InventoryNow>) : BaseArrayRecyclerAdapter<InventoryNow>(datas) {
    private var callBack: MyCallBack? = null
    private val df = DecimalFormat("#.######")

    override fun bindView(viewtype: Int): Int {
        return R.layout.ware_inventorynow_by_mtl_dialog_item
    }

    override fun onBindHoder(holder: BaseRecyclerAdapter.RecyclerHolder, entity: InventoryNow, pos: Int) {
        // 初始化id
        val tv_row = holder.obtainView<TextView>(R.id.tv_row)
        val tv_nowQty = holder.obtainView<TextView>(R.id.tv_nowQty)
        val tv_avbQty = holder.obtainView<TextView>(R.id.tv_avbQty)
        val tv_stockName = holder.obtainView<TextView>(R.id.tv_stockName)
        val tv_stockAreaName = holder.obtainView<TextView>(R.id.tv_stockAreaName)
        val tv_storageRackName = holder.obtainView<TextView>(R.id.tv_storageRackName)
        val tv_stockPosName = holder.obtainView<TextView>(R.id.tv_stockPosName)

        // 赋值
        tv_row.text = (pos+1).toString()

        tv_nowQty.text = Html.fromHtml("库存数:&nbsp;<font color='#6a5acd'>"+ df.format(entity.nowQty) +"</font>&nbsp;<font color='#666666'>"+ entity.unit.unitName +"</font>")
        tv_avbQty.text = Html.fromHtml("可用数:&nbsp;<font color='#FF0000'>"+ df.format(entity.avbQty) +"</font>")

        // 显示仓库组信息
        if(entity.stock != null ) {
            tv_stockName.visibility = View.VISIBLE
            tv_stockName.text = Html.fromHtml("仓库:&nbsp;<font color='#000000'>"+entity.stock!!.stockName+"</font>")
        } else {
            tv_stockName.visibility = View.INVISIBLE
        }
        if(entity.stockArea != null ) {
            tv_stockAreaName.visibility = View.VISIBLE
            tv_stockAreaName.text = Html.fromHtml("库区:&nbsp;<font color='#000000'>"+entity.stockArea!!.fname+"</font>")
        } else {
            tv_stockAreaName.visibility = View.INVISIBLE
        }
        if(entity.storageRack != null ) {
            tv_storageRackName.visibility = View.VISIBLE
            tv_storageRackName.text = Html.fromHtml("货架:&nbsp;<font color='#000000'>"+entity.storageRack!!.fnumber+"</font>")
        } else {
            tv_storageRackName.visibility = View.INVISIBLE
        }
        if(entity.stockPosition != null ) {
            tv_stockPosName.visibility = View.VISIBLE
            tv_stockPosName.text = Html.fromHtml("库位:&nbsp;<font color='#000000'>"+entity.stockPosition!!.stockPositionName+"</font>")
        } else {
            tv_stockPosName.visibility = View.INVISIBLE
        }
    }

    fun setCallBack(callBack: MyCallBack) {
        this.callBack = callBack
    }

    interface MyCallBack {
        fun onClick(entity: InventoryNow, position: Int)
    }

}
