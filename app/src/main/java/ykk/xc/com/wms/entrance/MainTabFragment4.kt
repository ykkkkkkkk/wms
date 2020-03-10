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

    @OnClick(R.id.relative1, R.id.relative2, R.id.relative3, R.id.relative4, R.id.relative5, R.id.relative6, R.id.relative7)
    fun onViewClicked(view: View) {
        val bundle: Bundle? = null
        when (view.id) {
            R.id.relative1 // 其他入库
            -> show(OtherInStock_MainActivity::class.java, null)
            R.id.relative2 // 其他出库
            -> show(OtherOutStock_MainActivity::class.java, null)
            R.id.relative3 // 调拨
            -> show(StockTransferMainActivity::class.java, null)
            R.id.relative4 // 盘点
            -> show(ICInvBackup_MainActivity::class.java, null)
            R.id.relative5 // 复盘
            -> show(ICInvBackup_Repeat_MainActivity::class.java, null)
            R.id.relative6 // 工具移库
            -> show(ToolMove_MainActivity::class.java, null)
            R.id.relative7 // 库存查询
            -> {
            }
        }//                bundle = new Bundle();
        //                show(OtherInStock_MainActivity.class, bundle);
        //                bundle = new Bundle();
        //                show(OtherOutStock_MainActivity.class, bundle);
        //                show(InventoryNowSearchActivity.class, null);
    }
}
