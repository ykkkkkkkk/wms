package ykk.xc.com.wms.warehouse.adapter

import android.app.Activity
import android.text.Html
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import ykk.xc.com.wms.R
import ykk.xc.com.wms.bean.k3Bean.ICInvBackup
import ykk.xc.com.wms.comm.Comm
import ykk.xc.com.wms.util.basehelper.BaseArrayRecyclerAdapter
import ykk.xc.com.wms.util.basehelper.BaseRecyclerAdapter
import java.text.DecimalFormat

class ICInvBackup_Search_Fragment1_Adapter(private val context: Activity, datas: List<ICInvBackup>) : BaseArrayRecyclerAdapter<ICInvBackup>(datas) {
    private val df = DecimalFormat("#.######")
    private var callBack: MyCallBack? = null

    override fun bindView(viewtype: Int): Int {
        return R.layout.ware_icinvbackup_search_fragment1_item
    }

    override fun onBindHoder(holder: BaseRecyclerAdapter.RecyclerHolder, entity: ICInvBackup, pos: Int) {
        // 初始化id
        val tv_row = holder.obtainView<TextView>(R.id.tv_row)
        val tv_mtlNumber = holder.obtainView<TextView>(R.id.tv_mtlNumber)
        val tv_mtlName = holder.obtainView<TextView>(R.id.tv_mtlName)
        val tv_batchNo = holder.obtainView<TextView>(R.id.tv_batchNo)
        val tv_fmodel = holder.obtainView<TextView>(R.id.tv_fmodel)
        val tv_stockName = holder.obtainView<TextView>(R.id.tv_stockName)
        val tv_stockAreaName = holder.obtainView<TextView>(R.id.tv_stockAreaName)
        val tv_storageRackName = holder.obtainView<TextView>(R.id.tv_storageRackName)
        val tv_stockPosName = holder.obtainView<TextView>(R.id.tv_stockPosName)
        val tv_containerName = holder.obtainView<TextView>(R.id.tv_containerName)
        val tv_num = holder.obtainView<TextView>(R.id.tv_num)
        val view_del = holder.obtainView<View>(R.id.view_del)
        val tv_num_hint = holder.obtainView<TextView>(R.id.tv_num_hint)
        val rela_parent = holder.obtainView<RelativeLayout>(R.id.lin_parent)

        // 赋值
        tv_row!!.text = (pos + 1).toString()
        tv_mtlName!!.text = entity.mtlName
        tv_mtlNumber!!.text = Html.fromHtml("物料代码:&nbsp;<font color='#000000'>"+entity.mtlNumber+"</font>")
        tv_batchNo!!.text = Html.fromHtml("批次:&nbsp;<font color='#000000'>"+Comm.isNULLS(entity.fbatchNo)+"</font>")
        tv_fmodel!!.text = Html.fromHtml("规格型号:&nbsp;<font color='#000000'>"+Comm.isNULLS(entity.fmodel)+"</font>")
//        tv_num!!.setText(Html.fromHtml("<small><font color='#777777'>已盘</font>" + df.format(entity.fauxCheckQty) + "</small><br><font color='#009900'>" + df.format(entity.realQty) + "</font>"))
        tv_num!!.text = df.format(entity.realQty)

        if(entity.icItem != null && entity.icItem.batchManager.equals("Y")) {
//            tv_num_hint.visibility = View.VISIBLE
            tv_batchNo.visibility = View.VISIBLE
        } else {
//            tv_num_hint.visibility = View.INVISIBLE
            tv_batchNo.visibility = View.INVISIBLE
        }

        if(entity.stock != null ) {
            tv_stockName.text = Html.fromHtml("仓库:&nbsp;<font color='#000000'>"+entity.stock!!.stockName+"</font>")
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
        if(entity.stockPos != null ) {
            tv_stockPosName.visibility = View.VISIBLE
            tv_stockPosName.text = Html.fromHtml("库位:&nbsp;<font color='#000000'>"+entity.stockPos!!.stockPositionName+"</font>")
        } else {
            tv_stockPosName.visibility = View.INVISIBLE
        }
        if(entity.container != null ) {
            tv_containerName.visibility = View.VISIBLE
            tv_containerName.text = Html.fromHtml("容器:&nbsp;<font color='#000000'>"+entity.container!!.fnumber+"</font>")
        } else {
            tv_containerName.visibility = View.INVISIBLE
        }

        if (entity.isCheck) {
            rela_parent.setBackgroundResource(R.drawable.back_style_check1_true)
        } else {
            rela_parent.setBackgroundResource(R.drawable.back_style_check1_false)
        }

//        val click = View.OnClickListener { v ->
//            when (v.id) {
//                R.id.tv_num -> {// 盘点数
//                    if (callBack != null) {
//                        callBack!!.onClick_num(entity, pos)
//                    }
//                }
//                R.id.view_del -> {// 删除行
//                     if (callBack != null) {
//                         callBack!!.onDelete(entity, pos)
//                     }
//                }
//            }
//        }
//        tv_num!!.setOnClickListener(click)
//        view_del!!.setOnClickListener(click)

        // 长按输入批次
//        tv_num.setOnLongClickListener {
//            if(entity.batchManager.equals("Y")) {
//                if (callBack != null) {
//                    callBack!!.onClick_batch(entity, pos)
//                }
//                true
//            } else {
//                if (callBack != null) {
//                    callBack!!.onClick_num(entity, pos)
//                }
//                false
//            }
//        }
    }

    fun setCallBack(callBack: MyCallBack) {
        this.callBack = callBack
    }

    interface MyCallBack {
        fun onClick_num(entity: ICInvBackup, position: Int)
        fun onClick_batch(entity: ICInvBackup, position: Int)
        fun onDelete(entity: ICInvBackup, position: Int)
    }

}
