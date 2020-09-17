package ykk.xc.com.wms.warehouse.adapter

import android.app.Activity
import android.text.Html
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import ykk.xc.com.wms.R
import ykk.xc.com.wms.bean.ICStockBill
import ykk.xc.com.wms.util.basehelper.BaseArrayRecyclerAdapter
import ykk.xc.com.wms.util.basehelper.BaseRecyclerAdapter
import java.text.DecimalFormat

class OutInStockSearchFragment12_CGFH_Adapter(private val context: Activity, datas: List<ICStockBill>) : BaseArrayRecyclerAdapter<ICStockBill>(datas) {
    private val df = DecimalFormat("#.######")
    private var callBack: MyCallBack? = null

    override fun bindView(viewtype: Int): Int {
        return R.layout.ware_outin_stock_search_fragment2__other_out_stock_item_cgfh
    }

    override fun onBindHoder(holder: BaseRecyclerAdapter.RecyclerHolder, entity: ICStockBill, pos: Int) {
        // 初始化id
        val tv_pdaNo = holder.obtainView<TextView>(R.id.tv_pdaNo)
        val tv_fdate = holder.obtainView<TextView>(R.id.tv_fdate)
        val tv_strSourceNo = holder.obtainView<TextView>(R.id.tv_strSourceNo)
        val tv_baoguanMan = holder.obtainView<TextView>(R.id.tv_baoguanMan)
        val tv_custName = holder.obtainView<TextView>(R.id.tv_custName)
        val tv_deliveryWay = holder.obtainView<TextView>(R.id.tv_deliveryWay)
        val tv_search = holder.obtainView<TextView>(R.id.tv_search)
        val tv_del = holder.obtainView<TextView>(R.id.tv_del)
        val lin_button = holder.obtainView<LinearLayout>(R.id.lin_button)

        // 赋值
        tv_pdaNo.text = entity.pdaNo
        tv_fdate.text = entity.fdate
        tv_baoguanMan.text = entity.baoguanMan
        tv_custName.text = entity.cust.fname
        // 发货方式( 发货运:990664，送货:990665 )
        if(entity.deliverWay == 990664) {
            tv_deliveryWay.text = "发货运"
        } else {
            tv_deliveryWay.text = "送货"
        }
        if(entity.strSourceNo != null) {
            tv_strSourceNo.text = Html.fromHtml("源单:&nbsp;<font color='#6a5acd'>" +entity.strSourceNo+ "</font>")
            tv_strSourceNo.visibility = View.VISIBLE
        } else {
            tv_strSourceNo.visibility = View.INVISIBLE
        }

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
        fun onSearch(entity: ICStockBill, position: Int)
//        fun onUpload(entity: ICStockBill, position: Int)
        fun onDelete(entity: ICStockBill, position: Int)
    }

}
