package ykk.xc.com.wms.warehouse.adapter

import android.app.Activity
import android.text.Html
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import ykk.xc.com.wms.R
import ykk.xc.com.wms.bean.MaterialBinningRecord
import ykk.xc.com.wms.util.basehelper.BaseArrayRecyclerAdapter
import ykk.xc.com.wms.util.basehelper.BaseRecyclerAdapter
import java.text.DecimalFormat

class OutInStockSearchFragment13_XSZX_Adapter(private val context: Activity, datas: List<MaterialBinningRecord>) : BaseArrayRecyclerAdapter<MaterialBinningRecord>(datas) {
    private val df = DecimalFormat("#.######")
    private var callBack: MyCallBack? = null

    override fun bindView(viewtype: Int): Int {
        return R.layout.ware_outin_stock_search_fragment2__other_out_stock_item_xszx
    }

    override fun onBindHoder(holder: BaseRecyclerAdapter.RecyclerHolder, entity: MaterialBinningRecord, pos: Int) {
        // 初始化id
        val tv_boxName = holder.obtainView<TextView>(R.id.tv_boxName)
        val tv_boxSize = holder.obtainView<TextView>(R.id.tv_boxSize)
        val tv_boxBarcode = holder.obtainView<TextView>(R.id.tv_boxBarcode)
        val tv_fdate = holder.obtainView<TextView>(R.id.tv_fdate)
        val tv_custName = holder.obtainView<TextView>(R.id.tv_custName)
        val tv_search = holder.obtainView<TextView>(R.id.tv_search)
        val tv_del = holder.obtainView<TextView>(R.id.tv_del)
        val lin_button = holder.obtainView<LinearLayout>(R.id.lin_button)

        // 赋值
        tv_boxName.text = Html.fromHtml("箱子:&nbsp;<font color='#000000'>"+entity.box.boxName+"</font>")
        tv_boxSize.text = entity.box.boxSize
        tv_boxBarcode.text = Html.fromHtml("箱码:&nbsp;<font color='#6a5acd'>"+entity.boxBarCode.barCode+"</font>")
        tv_fdate.text = entity.createDate
        tv_custName.text = Html.fromHtml("客户:&nbsp;<font color='#FF4400'>"+entity.cust.fname+"</font>")

        if (entity.isShowButton) {
            lin_button!!.setVisibility(View.VISIBLE)
        } else {
            lin_button!!.setVisibility(View.GONE)
        }

        val click = View.OnClickListener { v ->
            when (v.id) {
                R.id.tv_search -> { // 查询
                    if (callBack != null) {
                        callBack!!.onSearch(entity, pos)
                    }
                }
                R.id.tv_del -> { // 删除行
                    if (callBack != null) {
                        callBack!!.onDelete(entity, pos)
                    }
                }
            }
        }
        tv_search.setOnClickListener(click)
        tv_del!!.setOnClickListener(click)
    }

    fun setCallBack(callBack: MyCallBack) {
        this.callBack = callBack
    }

    interface MyCallBack {
        fun onSearch(entity: MaterialBinningRecord, position: Int)
//        fun onUpload(entity: MaterialBinningRecord, position: Int)
        fun onDelete(entity: MaterialBinningRecord, position: Int)
    }

}
