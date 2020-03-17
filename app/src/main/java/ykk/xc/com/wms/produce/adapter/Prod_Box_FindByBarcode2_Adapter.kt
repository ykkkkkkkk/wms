package ykk.xc.com.wms.produce.adapter

import android.app.Activity
import android.text.Html
import android.view.View
import android.widget.TextView
import ykk.xc.com.wms.R
import ykk.xc.com.wms.bean.MaterialBinningRecord
import ykk.xc.com.wms.util.basehelper.BaseArrayRecyclerAdapter
import ykk.xc.com.wms.util.basehelper.BaseRecyclerAdapter
import java.text.DecimalFormat

class Prod_Box_FindByBarcode2_Adapter(private val context: Activity, datas: List<MaterialBinningRecord>) : BaseArrayRecyclerAdapter<MaterialBinningRecord>(datas) {
    private val df = DecimalFormat("#.######")
    private var callBack: MyCallBack? = null

    override fun bindView(viewtype: Int): Int {
        return R.layout.prod_box_find_by_barcode_item2
    }

    override fun onBindHoder(holder: BaseRecyclerAdapter.RecyclerHolder, entity: MaterialBinningRecord, pos: Int) {
        // 初始化id
        val tv_row = holder.obtainView<TextView>(R.id.tv_row)
        val tv_mtlName = holder.obtainView<TextView>(R.id.tv_mtlName)
        val tv_fmodel = holder.obtainView<TextView>(R.id.tv_fmodel)
        val tv_prodNo = holder.obtainView<TextView>(R.id.tv_prodNo)
        val tv_fqty = holder.obtainView<TextView>(R.id.tv_fqty)

        // 赋值
        tv_row.text = (pos+1).toString()
        tv_mtlName.text = entity.icItem.fname
        tv_fmodel.text = Html.fromHtml("规格:&nbsp;<font color='#6a5acd'>"+entity.icItem.fmodel+"</font>")
        tv_prodNo.text = Html.fromHtml("生产单号:&nbsp;<font color='#6a5acd'>"+ entity.fsourceNo +"</font>")
        tv_fqty.text = Html.fromHtml("装箱数:&nbsp;<font color='#FF2200'>"+ df.format(entity.fqty) +"</font>")

    }

    fun setCallBack(callBack: MyCallBack) {
        this.callBack = callBack
    }

    interface MyCallBack {
        fun onClickNum(entity: MaterialBinningRecord, position: Int)
        fun onDelete(entity: MaterialBinningRecord, position: Int)
    }

}
