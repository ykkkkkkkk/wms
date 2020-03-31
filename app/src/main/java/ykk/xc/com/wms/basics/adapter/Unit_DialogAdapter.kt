package ykk.xc.com.wms.basics.adapter

import android.app.Activity
import android.widget.TextView
import ykk.xc.com.wms.R
import ykk.xc.com.wms.bean.Unit
import ykk.xc.com.wms.util.basehelper.BaseArrayRecyclerAdapter
import ykk.xc.com.wms.util.basehelper.BaseRecyclerAdapter

class Unit_DialogAdapter(private val context: Activity, private val datas: List<Unit>) : BaseArrayRecyclerAdapter<Unit>(datas) {
    private var callBack: MyCallBack? = null

    override fun bindView(viewtype: Int): Int {
        return R.layout.ab_unit_list_item
    }

    override fun onBindHoder(holder: BaseRecyclerAdapter.RecyclerHolder, entity: Unit, pos: Int) {
        // 初始化id
        val tv_row = holder.obtainView<TextView>(R.id.tv_row)
        val tv_fnumber = holder.obtainView<TextView>(R.id.tv_fnumber)
        val tv_fname = holder.obtainView<TextView>(R.id.tv_fname)
        // 赋值
        tv_row.text = (pos + 1).toString()
        tv_fnumber.text = entity.unitNumber
        tv_fname.text = entity.unitName
    }

    fun setCallBack(callBack: MyCallBack) {
        this.callBack = callBack
    }

    interface MyCallBack {
        fun onClick(entity: Unit, position: Int)
    }

}
