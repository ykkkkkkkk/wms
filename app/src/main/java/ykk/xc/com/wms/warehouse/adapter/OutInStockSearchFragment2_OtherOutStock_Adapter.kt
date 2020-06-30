package ykk.xc.com.wms.warehouse.adapter

import android.app.Activity
import android.text.Html
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import ykk.xc.com.wms.R
import ykk.xc.com.wms.bean.ICStockBill
import ykk.xc.com.wms.comm.Comm
import ykk.xc.com.wms.util.basehelper.BaseArrayRecyclerAdapter
import ykk.xc.com.wms.util.basehelper.BaseRecyclerAdapter
import java.text.DecimalFormat

class OutInStockSearchFragment2_OtherOutStock_Adapter(private val context: Activity, datas: List<ICStockBill>) : BaseArrayRecyclerAdapter<ICStockBill>(datas) {
    private val df = DecimalFormat("#.######")
    private var callBack: MyCallBack? = null

    override fun bindView(viewtype: Int): Int {
        return R.layout.ware_outin_stock_search_fragment1__other_in_stock_item
    }

    override fun onBindHoder(holder: BaseRecyclerAdapter.RecyclerHolder, entity: ICStockBill, pos: Int) {
        // 初始化id
        val tv_pdaNo = holder.obtainView<TextView>(R.id.tv_pdaNo)
        val tv_fdate = holder.obtainView<TextView>(R.id.tv_fdate)
        val tv_deptName = holder.obtainView<TextView>(R.id.tv_deptName)
        val tv_suppName = holder.obtainView<TextView>(R.id.tv_suppName)
        val tv_baoguanMan = holder.obtainView<TextView>(R.id.tv_baoguanMan)
        val tv_search = holder.obtainView<TextView>(R.id.tv_search)
        val tv_upload = holder.obtainView<TextView>(R.id.tv_upload)
        val tv_del = holder.obtainView<TextView>(R.id.tv_del)
        val lin_button = holder.obtainView<LinearLayout>(R.id.lin_button)

        // 赋值
        tv_pdaNo.text = Html.fromHtml("PDA单号:&nbsp;<font color='#000000'>"+entity.pdaNo+"</font>")
        tv_fdate.text = Html.fromHtml("出库日期:&nbsp;<font color='#000000'>"+entity.fdate+"</font>")
        tv_suppName.text = Html.fromHtml("客户:&nbsp;<font color='#FF4400'>"+ (if(entity.cust != null) entity.cust.fname else "") +"</font>")
        tv_deptName.text = Html.fromHtml("部门:&nbsp;<font color='#000000'>"+ Comm.isNULLS(entity.deptName)+"</font>")
        if(Comm.isNULLS(entity.deptName).length == 0) {
            tv_deptName.visibility = View.INVISIBLE
        } else {
            tv_deptName.visibility = View.VISIBLE
        }
        tv_baoguanMan.text = Html.fromHtml("<font color='#6a5acd'>"+entity.baoguanMan+"</font>")

        if (entity.isShowButton) {
            lin_button.setVisibility(View.VISIBLE)
        } else {
            lin_button.setVisibility(View.GONE)
        }

        val click = View.OnClickListener { v ->
            when (v.id) {
                R.id.tv_search -> { // 查询
                    if (callBack != null) {
                        callBack!!.onSearch(entity, pos)
                    }
                }
                R.id.tv_upload -> {// 上传
                    if (callBack != null) {
                        callBack!!.onUpload(entity, pos)
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
        tv_upload.setOnClickListener(click)
        tv_del.setOnClickListener(click)
    }

    fun setCallBack(callBack: MyCallBack) {
        this.callBack = callBack
    }

    interface MyCallBack {
        fun onSearch(entity: ICStockBill, position: Int)
        fun onUpload(entity: ICStockBill, position: Int)
        fun onDelete(entity: ICStockBill, position: Int)
    }

}
