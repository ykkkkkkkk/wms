package ykk.xc.com.wms.warehouse.adapter

import android.app.Activity
import android.text.Html
import android.view.View
import android.widget.TextView
import ykk.xc.com.wms.R
import ykk.xc.com.wms.bean.ICChangeEntry
import ykk.xc.com.wms.util.BigdecimalUtil
import ykk.xc.com.wms.util.basehelper.BaseArrayRecyclerAdapter
import ykk.xc.com.wms.util.basehelper.BaseRecyclerAdapter
import java.text.DecimalFormat

class Ware_Disassembly_Adapter(private val context: Activity, datas: List<ICChangeEntry>) : BaseArrayRecyclerAdapter<ICChangeEntry>(datas) {
    private val df = DecimalFormat("#.######")
    private var callBack: MyCallBack? = null

    override fun bindView(viewtype: Int): Int {
        return R.layout.ware_disassembly_item
    }

    override fun onBindHoder(holder: BaseRecyclerAdapter.RecyclerHolder, entity: ICChangeEntry, pos: Int) {
        // 初始化id
        val tv_row = holder.obtainView<TextView>(R.id.tv_row)
        val tv_mtlName = holder.obtainView<TextView>(R.id.tv_mtlName)
        val tv_mtlNumber = holder.obtainView<TextView>(R.id.tv_mtlNumber)
        val tv_fmodel = holder.obtainView<TextView>(R.id.tv_fmodel)
        val tv_type = holder.obtainView<TextView>(R.id.tv_type)
        val tv_num = holder.obtainView<TextView>(R.id.tv_num)
        val tv_num2 = holder.obtainView<TextView>(R.id.tv_num2)
        val inventoryQty = holder.obtainView<TextView>(R.id.inventoryQty)
        val tv_stockName = holder.obtainView<TextView>(R.id.tv_stockName)
        val tv_stockPosName = holder.obtainView<TextView>(R.id.tv_stockPosName)

        // 赋值
        tv_row.text = (pos+1).toString()
        tv_mtlName.text = entity.icItem.fname
        tv_mtlNumber.text = Html.fromHtml("代码:&nbsp;<font color='#6a5acd'>"+ entity.icItem.fnumber +"</font>")
        if(entity.fmtrlType == 0) {
            tv_type.text = "拆卸母件"
        } else {
            tv_type.text = Html.fromHtml("<font color='#6a5acd'>拆卸子件</font>")
        }
        tv_fmodel.text = Html.fromHtml("规格型号:&nbsp;<font color='#6a5acd'>"+ entity.icItem.fmodel +"</font>")

        tv_num.text = Html.fromHtml("数量:&nbsp;<font color='#FF2200'>"+ df.format(entity.fqty) +"</font>")
        if(entity.fmtrlType == 1) {
            val divVal = BigdecimalUtil.div(entity.fauxQty_Base, entity.bom_FQty, entity.icItem.fqtydecimal)
            tv_num2.text = Html.fromHtml("用量:&nbsp;<font color='#6a5acd'>" + df.format(divVal) + "</font>&nbsp;<font color='#666666'>"+ entity.unit.unitName +"</font>")
            inventoryQty.visibility = View.GONE
        } else {
            tv_num2.text = Html.fromHtml("用量:&nbsp;<font color='#6a5acd'>" + df.format(entity.fauxQty_Base) + "</font>&nbsp;<font color='#666666'>"+ entity.unit.unitName +"</font>")
            inventoryQty.visibility = View.VISIBLE
            inventoryQty.text = Html.fromHtml("库存:&nbsp;<font color='#6a5acd'>" + df.format(entity.inventoryQty) + "</font>")
        }


        // 显示仓库组信息
        if(entity.stock != null ) {
            tv_stockName.visibility = View.VISIBLE
            tv_stockName.text = Html.fromHtml("仓库:&nbsp;<font color='#6a5acd'>"+entity.stock!!.fname+"</font>")
        } else {
            tv_stockName.visibility = View.INVISIBLE
        }
        if(entity.stockPos != null ) {
            tv_stockPosName.visibility = View.VISIBLE
            tv_stockPosName.text = Html.fromHtml("库位:&nbsp;<font color='#6a5acd'>"+entity.stockPos!!.fname+"</font>")
        } else {
            tv_stockPosName.visibility = View.INVISIBLE
        }

    }

    fun setCallBack(callBack: MyCallBack) {
        this.callBack = callBack
    }

    interface MyCallBack {
//        fun onDelete(entity: ICChangeEntry, position: Int)
    }

}
