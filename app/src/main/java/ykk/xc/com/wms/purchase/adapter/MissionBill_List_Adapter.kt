package ykk.xc.com.wms.purchase.adapter

import android.app.Activity
import android.text.Html
import android.view.View
import android.widget.TextView
import ykk.xc.com.wms.R
import ykk.xc.com.wms.bean.MissionBill
import ykk.xc.com.wms.comm.Comm
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
        val tv_sourceBillNo = holder.obtainView<TextView>(R.id.tv_sourceBillNo)

        // 赋值
        tv_row.text = (pos + 1).toString()
        tv_date.text = entity.createTime
        tv_billNo.text = entity.billNo
        //任务类型 	1：代表外购收料任务，21：代表销售发货任务,31：代表仓库外购收货任务,41：代表投料调拨，42：代表生产入库调拨，51：拣货任务单，52：出库质检任务，53：仓管复核任务
        when (entity.missionType) {
            1 -> tv_missionType.text = "外购收料任务"
            21 -> tv_missionType.text = "销售发货任务"
            31 -> tv_missionType.text = "仓库外购收货任务"
            32 -> tv_missionType.text = "采购退货任务"
            41 -> tv_missionType.text = "投料调拨任务"
            42 -> tv_missionType.text = "生产入库调拨任务"
            51 -> tv_missionType.text = "销售拣货任务"
            52 -> tv_missionType.text = "出库质检任务"
            53 -> tv_missionType.text = "仓管复核任务"
            54 -> tv_missionType.text = "销售装箱任务"
            55 -> tv_missionType.text = "销售退货任务"
            61 -> tv_missionType.text = "其他入库任务"
            62 -> tv_missionType.text = "其他出库任务"
            71 -> tv_missionType.text = "补料调拨任务"
            72 -> tv_missionType.text = "其他调拨任务"
        }
        if(Comm.isNULLS(entity.missionContent).length > 0) {
            tv_missionContent.visibility = View.VISIBLE
        } else {
            tv_missionContent.visibility = View.INVISIBLE
        }
        tv_missionContent.text = Html.fromHtml("任务内容:&nbsp;<font color='#000000'>"+ Comm.isNULLS(entity.missionContent)+"</font>")
        tv_sourceBillNo.text = Html.fromHtml("来源单:&nbsp;<font color='#6a5acd'>"+ entity.sourceBillNo+"</font>")

        val view = tv_row.parent as View
        if(entity.isCheck) {
            view.setBackgroundResource(R.drawable.back_style_check1_true)
        } else {
            view.setBackgroundResource(R.drawable.back_style_check1_false)
        }
    }

    fun setCallBack(callBack: MyCallBack) {
        this.callBack = callBack
    }

    interface MyCallBack {
        fun onClick(entity: MissionBill, position: Int)
    }


}
