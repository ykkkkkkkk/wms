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

class OutInStockSearchFragment7_PurReceiveQC_Adapter(private val context: Activity, datas: List<ICStockBill>) : BaseArrayRecyclerAdapter<ICStockBill>(datas) {
    private val df = DecimalFormat("#.######")
    private var callBack: MyCallBack? = null

    override fun bindView(viewtype: Int): Int {
        return R.layout.ware_outin_stock_search_fragment1__other_in_stock_item
    }

    override fun onBindHoder(holder: BaseRecyclerAdapter.RecyclerHolder, entity: ICStockBill, pos: Int) {
        // 初始化id
        val tv_pdaNo = holder.obtainView<TextView>(R.id.tv_pdaNo)
        val tv_missionBillNo = holder.obtainView<TextView>(R.id.tv_missionBillNo)
        val tv_fdate = holder.obtainView<TextView>(R.id.tv_fdate)
        val tv_deptName = holder.obtainView<TextView>(R.id.tv_deptName)
        val tv_suppName = holder.obtainView<TextView>(R.id.tv_suppName)
        val tv_baoguanMan = holder.obtainView<TextView>(R.id.tv_baoguanMan)
        val tv_search = holder.obtainView<TextView>(R.id.tv_search)
        val tv_upload = holder.obtainView<TextView>(R.id.tv_upload)
        val tv_del = holder.obtainView<TextView>(R.id.tv_del)
        val lin_button = holder.obtainView<LinearLayout>(R.id.lin_button)

        // 赋值
        tv_pdaNo.text = entity.pdaNo
        tv_fdate.text = entity.fdate
        tv_deptName?.text = entity.deptName
        tv_suppName?.text = entity.suppName
        tv_baoguanMan.text = entity.baoguanMan

        // 赋值
        tv_pdaNo.text = Html.fromHtml("PDA单号:&nbsp;<font color='#000000'>"+entity.pdaNo+"</font>")
        if(entity.missionBill != null) {
            tv_missionBillNo.text = Html.fromHtml("任务单:&nbsp;<font color='#FF4400'>"+entity.missionBill.billNo+"</font>")
            tv_missionBillNo.visibility = View.VISIBLE
        } else {
            tv_missionBillNo.visibility = View.INVISIBLE
        }
        tv_fdate.text = Html.fromHtml("入库日期:&nbsp;<font color='#000000'>"+entity.fdate+"</font>")
        tv_deptName?.text = Html.fromHtml("部门:&nbsp;<font color='#000000'>"+entity.deptName+"</font>")
        tv_suppName?.text = Html.fromHtml("供应商:&nbsp;<font color='#FF4400'>"+entity.suppName+"</font>")
        tv_baoguanMan.text = Html.fromHtml("保管人:&nbsp;<font color='#000000'>"+entity.baoguanMan+"</font>")

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
        tv_upload!!.setOnClickListener(click)
        tv_del!!.setOnClickListener(click)
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
