package ykk.xc.com.wms.purchase.adapter

import android.app.Activity
import android.widget.TextView
import ykk.xc.com.wms.R
import ykk.xc.com.wms.bean.MissionBill
import ykk.xc.com.wms.util.basehelper.BaseArrayRecyclerAdapter
import ykk.xc.com.wms.util.basehelper.BaseRecyclerAdapter
import java.text.DecimalFormat

class MissionBill_List_Adapter(private val context: Activity, private val datas: List<MissionBill>) : BaseArrayRecyclerAdapter<MissionBill>(datas) {
    private val df = DecimalFormat("#.####")
    private var callBack: MyCallBack? = null

    override fun bindView(viewtype: Int): Int {
        return R.layout.missionbill_list_item
    }

    override fun onBindHoder(holder: BaseRecyclerAdapter.RecyclerHolder, entity: MissionBill, pos: Int) {
        // 初始化id
        val tv_row = holder.obtainView<TextView>(R.id.tv_row)
        val tv_date = holder.obtainView<TextView>(R.id.tv_date)
        val tv_billNo = holder.obtainView<TextView>(R.id.tv_billNo)
        val tv_missionType = holder.obtainView<TextView>(R.id.tv_missionType)
        val tv_missionContent = holder.obtainView<TextView>(R.id.tv_missionContent)
        val tv_k3Number = holder.obtainView<TextView>(R.id.tv_k3Number)

        // 赋值
        tv_row!!.text = (pos + 1).toString()
        tv_date!!.text = entity.createTime
        tv_billNo!!.text = entity.billNo
        // 1：代表外购收料任务，21：代表销售发货任务,31：代表仓库外购收货任务,41：代表投料调拨
        if(entity.missionType == 1) {
            tv_missionType!!.text = "外购收料任务"
        } else if(entity.missionType == 21) {
            tv_missionType!!.text = "销售发货任务"
        } else if(entity.missionType == 31) {
            tv_missionType!!.text = "仓库外购收货任务"
        } else if(entity.missionType == 41) {
            tv_missionType!!.text = "投料调拨任务"
        } else {
            tv_missionType!!.text = "外购收料任务"
        }
        tv_missionContent!!.text = entity.missionContent
        tv_k3Number!!.text = entity.sourceBillNo

    }

    fun setCallBack(callBack: MyCallBack) {
        this.callBack = callBack
    }

    interface MyCallBack {
        fun onClick(entity: MissionBill, position: Int)
    }


}
