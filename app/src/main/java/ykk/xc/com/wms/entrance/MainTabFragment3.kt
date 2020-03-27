package ykk.xc.com.wms.entrance


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import butterknife.OnClick
import ykk.xc.com.wms.R
import ykk.xc.com.wms.comm.BaseFragment
import ykk.xc.com.wms.sales.Sal_FinanceSignatureActivity
import ykk.xc.com.wms.sales.Sal_OperationSignatureActivity
import ykk.xc.com.wms.sales.Sal_OutStockMainActivity
import ykk.xc.com.wms.warehouse.OutInStock_Search_MainActivity

/**
 * 销售
 */
class MainTabFragment3 : BaseFragment() {

    override fun setLayoutResID(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.aa_main_item3, container, false)
    }

    @OnClick(R.id.relative1, R.id.relative2, R.id.relative3, R.id.relative4, R.id.relative5, R.id.relative6)
    fun onViewClicked(view: View) {
        when (view.id) {
            R.id.relative1 -> {// 销售出库
                val bundle = Bundle()
                bundle.putInt("pageId", 11)
                bundle.putString("billType", "CGFH")
                show(OutInStock_Search_MainActivity::class.java, bundle)
//                show(Sal_OutStockMainActivity::class.java, null)
            }
            R.id.relative2 -> {// 业助回签
                show(Sal_OperationSignatureActivity::class.java, null)
            }
            R.id.relative3 -> {// 财务回签
                show(Sal_FinanceSignatureActivity::class.java, null)
            }
            R.id.relative4 -> {// （内销）销售退货
            }
            R.id.relative5 // 电商退生产
            -> {
            }
            R.id.relative6 // 销售装箱
            -> {
            }
        }//                show(Sal_ScOutMainActivity.class, null);
        //                show(Sal_DsOutReturnMainActivity.class, null);
        //                show(Sal_NxOutReturnMainActivity.class, null);
        //                show(Sal_DsBToRFromPurchaseInStockMainActivity.class, null);
        //                show(Sal_OutStockMainActivity.class, null);
    }
}
