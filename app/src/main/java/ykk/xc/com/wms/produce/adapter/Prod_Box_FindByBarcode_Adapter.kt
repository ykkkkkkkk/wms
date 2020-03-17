package ykk.xc.com.wms.produce.adapter

import android.app.Activity
import android.view.View
import android.widget.TextView
import ykk.xc.com.wms.R
import ykk.xc.com.wms.bean.BoxBarCode
import ykk.xc.com.wms.util.basehelper.BaseArrayRecyclerAdapter
import ykk.xc.com.wms.util.basehelper.BaseRecyclerAdapter
import java.text.DecimalFormat

class Prod_Box_FindByBarcode_Adapter(private val context: Activity, datas: List<BoxBarCode>) : BaseArrayRecyclerAdapter<BoxBarCode>(datas) {
    private val df = DecimalFormat("#.######")
    private var callBack: MyCallBack? = null

    override fun bindView(viewtype: Int): Int {
        return R.layout.prod_box_find_by_barcode_item1
    }

    override fun onBindHoder(holder: BaseRecyclerAdapter.RecyclerHolder, entity: BoxBarCode, pos: Int) {
        // 初始化id
        val tv_boxName = holder.obtainView<TextView>(R.id.tv_boxName)
        val tv_boxSize = holder.obtainView<TextView>(R.id.tv_boxSize)
        val tv_barcode = holder.obtainView<TextView>(R.id.tv_barcode)

        // 赋值
        tv_boxName.text = entity.box.boxName
        tv_boxSize.text = entity.box.boxSize
        tv_barcode.text = entity.barCode

        val view = tv_boxName.parent as View
        if (entity.isCheck) {
            view.setBackgroundResource(R.drawable.back_style_check1_true)
        } else {
            view.setBackgroundResource(R.drawable.back_style_check1_false)
        }
    }

    fun setCallBack(callBack: MyCallBack) {
        this.callBack = callBack
    }

    interface MyCallBack {
        fun onClickNum(entity: BoxBarCode, position: Int)
        fun onDelete(entity: BoxBarCode, position: Int)
    }

}
