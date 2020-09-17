package ykk.xc.com.wms.entrance


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import butterknife.OnClick
import ykk.xc.com.wms.R
import ykk.xc.com.wms.comm.BaseFragment
import ykk.xc.com.wms.warehouse.*


/**
 * 仓库
 */
class MainTabFragment4 : BaseFragment() {

    override fun setLayoutResID(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.aa_main_item4, container, false)
    }

    @OnClick(R.id.relative1, R.id.relative2, R.id.relative3, R.id.relative4, R.id.relative5, R.id.relative6, R.id.relative7, R.id.relative8, R.id.relative9)
    fun onViewClicked(view: View) {
        val bundle: Bundle? = null
        when (view.id) {
            R.id.relative1 -> { // 盘点
//                show(OtherInStock_MainActivity::class.java, null)
                show(ICInvBackup_MainActivity::class.java, null)
            }
            R.id.relative2 -> { // 复盘
//                show(OtherOutStock_MainActivity::class.java, null)
                show(ICInvBackup_Repeat_MainActivity::class.java, null)
            }
            R.id.relative3 -> { // 工具移库
                show(ToolMove_MainActivity::class.java, null)

            }
            R.id.relative4 -> { // 待上传
                val bundle = Bundle()
                bundle.putInt("pageId", 0)
                bundle.putString("billType", "QTRK")
                show(OutInStock_Search_MainActivity::class.java, bundle)
            }
            R.id.relative5 -> { // 库内装箱
                show(Ware_Box_MainActivity::class.java, null)
            }
            R.id.relative6 -> { // 拣货位置
                show(Ware_PickGoods_PositionActivity::class.java, null)
            }
            R.id.relative7 -> { // 物料位置
                show(Ware_Material_PositionActivity::class.java, null)
            }
            R.id.relative8 -> { // 自由调拨
                show(Ware_Transfer_MainActivity::class.java, null)
            }
            R.id.relative9 -> { // 待确认
                show(Ware_BillConfirmList_MainActivity::class.java, null)
            }
        }
    }
}
