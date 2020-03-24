package ykk.xc.com.wms.sales.adapter

import android.app.Activity
import android.text.Html
import android.view.View
import android.widget.TextView
import ykk.xc.com.wms.R
import ykk.xc.com.wms.bean.MaterialBinningRecord
import ykk.xc.com.wms.util.basehelper.BaseArrayRecyclerAdapter
import ykk.xc.com.wms.util.basehelper.BaseRecyclerAdapter
import java.text.DecimalFormat

class Sal_Box_Fragment1_Adapter(private val context: Activity, datas: List<MaterialBinningRecord>) : BaseArrayRecyclerAdapter<MaterialBinningRecord>(datas) {
    private val df = DecimalFormat("#.######")
    private var callBack: MyCallBack? = null

    override fun bindView(viewtype: Int): Int {
        return R.layout.sal_box_fragment1_item
    }

    override fun onBindHoder(holder: BaseRecyclerAdapter.RecyclerHolder, entity: MaterialBinningRecord, pos: Int) {
        // 初始化id
        val tv_row = holder.obtainView<TextView>(R.id.tv_row)
        val tv_mtlNumber = holder.obtainView<TextView>(R.id.tv_mtlNumber)
        val tv_mtlName = holder.obtainView<TextView>(R.id.tv_mtlName)
        val tv_fmodel = holder.obtainView<TextView>(R.id.tv_fmodel)
        val tv_prodNo = holder.obtainView<TextView>(R.id.tv_prodNo)
        val tv_prodQty = holder.obtainView<TextView>(R.id.tv_prodQty)
        val tv_num = holder.obtainView<TextView>(R.id.tv_num)
        val view_del = holder.obtainView<View>(R.id.view_del)
        val tv_boxBarcode = holder.obtainView<TextView>(R.id.tv_boxBarcode)

        // 赋值
        tv_row.text = entity.rowNo.toString()
        tv_mtlName.text = entity.icItem.fname
        tv_mtlNumber.text = Html.fromHtml("代码:&nbsp;<font color='#6a5acd'>"+entity.icItem.fnumber+"</font>")
        tv_fmodel.text = Html.fromHtml("规格:&nbsp;<font color='#6a5acd'>"+entity.icItem.fmodel+"</font>")
        tv_num.text = df.format(entity.fqty)
        tv_prodNo.text = Html.fromHtml("生产单号:&nbsp;<font color='#6a5acd'>"+ entity.fsourceNo +"</font>")
        tv_prodQty.text = Html.fromHtml("发货数:&nbsp;<font color='#FF2200'>"+ df.format(entity.fsourceQty) +"</font>")
        tv_boxBarcode.text = Html.fromHtml("箱码:&nbsp;<font color='#000000'>"+ if(entity.boxBarCode != null) entity.boxBarCode.barCode else ""  +"</font>")

        if(entity.icItem.batchManager.equals("Y") || entity.icItem.snManager.equals("Y")) {
            tv_num.isEnabled = false
            tv_num.setBackgroundResource(R.drawable.back_style_gray3b)
        } else {
            tv_num.isEnabled = true
            tv_num.setBackgroundResource(R.drawable.back_style_blue2)
        }

        val click = View.OnClickListener { v ->
            when (v.id) {
                R.id.tv_num -> {// 数量
                    if (callBack != null) {
                        callBack!!.onClickNum(entity, pos)
                    }
                }
                R.id.view_del // 删除行
                -> if (callBack != null) {
                    callBack!!.onDelete(entity, pos)
                }
            }
        }
        tv_num!!.setOnClickListener(click)
        view_del!!.setOnClickListener(click)
    }

    fun setCallBack(callBack: MyCallBack) {
        this.callBack = callBack
    }

    interface MyCallBack {
        fun onClickNum(entity: MaterialBinningRecord, position: Int)
        fun onDelete(entity: MaterialBinningRecord, position: Int)
    }

}
