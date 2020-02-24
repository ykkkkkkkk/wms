package ykk.xc.com.wms.warehouse.adapter

import android.app.Activity
import android.text.Html
import android.view.View
import android.widget.TextView
import ykk.xc.com.wms.R
import ykk.xc.com.wms.bean.k3Bean.ICInventory
import ykk.xc.com.wms.util.basehelper.BaseArrayRecyclerAdapter
import ykk.xc.com.wms.util.basehelper.BaseRecyclerAdapter
import java.text.DecimalFormat

class StockTransferFragment1_DS_Adapter(private val context: Activity, datas: List<ICInventory>) : BaseArrayRecyclerAdapter<ICInventory>(datas) {
    private val df = DecimalFormat("#.######")
    private var callBack: MyCallBack? = null

    override fun bindView(viewtype: Int): Int {
        return R.layout.ware_sc_stock_transfer_fragment1_ds_item
    }

    override fun onBindHoder(holder: BaseRecyclerAdapter.RecyclerHolder, entity: ICInventory, pos: Int) {
        // 初始化id
        val tv_row = holder.obtainView(R.id.tv_row) as TextView
        val tv_mtlNumber = holder.obtainView(R.id.tv_mtlNumber) as TextView
        val tv_mtlName = holder.obtainView(R.id.tv_mtlName) as TextView
        val tv_nums = holder.obtainView(R.id.tv_nums) as TextView

        // 赋值
        tv_row!!.setText((pos + 1).toString())
        tv_mtlNumber!!.setText(entity.mtlNumber)
        tv_mtlName!!.setText(entity.mtlName)
        tv_nums!!.setText(Html.fromHtml(df.format(entity.getfQty()) + entity.unitName + "<br><font color='#009900'>" + df.format(entity.realQty) + "</font>"))

        val click = View.OnClickListener { v ->
            when (v.id) {
                R.id.tv_nums // 调拨数
                -> if (callBack != null) {
                    callBack!!.onClick_num(entity, pos)
                }
            }
        }
        tv_nums!!.setOnClickListener(click)
    }

    fun setCallBack(callBack: MyCallBack) {
        this.callBack = callBack
    }

    interface MyCallBack {
        fun onClick_num(entity: ICInventory, position: Int)
    }

}
