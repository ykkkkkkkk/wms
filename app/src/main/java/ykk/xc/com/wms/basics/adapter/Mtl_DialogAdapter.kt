package ykk.xc.com.wms.basics.adapter

import android.app.Activity
import android.widget.TextView
import ykk.xc.com.wms.R
import ykk.xc.com.wms.bean.k3Bean.ICItem
import ykk.xc.com.wms.util.basehelper.BaseArrayRecyclerAdapter
import ykk.xc.com.wms.util.basehelper.BaseRecyclerAdapter

class Mtl_DialogAdapter(private val context: Activity, private val datas: List<ICItem>) : BaseArrayRecyclerAdapter<ICItem>(datas) {
    private var callBack: MyCallBack? = null

    override fun bindView(viewtype: Int): Int {
        return R.layout.ab_mtl_list_item
    }

    override fun onBindHoder(holder: BaseRecyclerAdapter.RecyclerHolder, entity: ICItem, pos: Int) {
        // 初始化id
        val tv_row = holder.obtainView<TextView>(R.id.tv_row)
        val tv_fnumber = holder.obtainView<TextView>(R.id.tv_fnumber)
        val tv_fname = holder.obtainView<TextView>(R.id.tv_fname)
        val tv_FModel = holder.obtainView<TextView>(R.id.tv_FModel)
        // 赋值
        tv_row.text = (pos + 1).toString()
        tv_fnumber.text = entity.fnumber
        tv_fname.text = entity.fname
        tv_FModel.text = entity.fmodel
    }

    fun setCallBack(callBack: MyCallBack) {
        this.callBack = callBack
    }

    interface MyCallBack {
        fun onClick(entity: ICItem, position: Int)
    }

}
