package ykk.xc.com.wms.entrance


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import butterknife.OnClick
import ykk.xc.com.wms.R
import ykk.xc.com.wms.comm.BaseFragment
import ykk.xc.com.wms.produce.Prod_InStock_MainActivity

/**
 * 生产
 */
class MainTabFragment2 : BaseFragment() {

    override fun setLayoutResID(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.aa_main_item2, container, false)
    }

    @OnClick(R.id.relative1, R.id.relative2, R.id.relative3, R.id.relative4)
    fun onViewClicked(view: View) {
        when (view.id) {
            R.id.relative1 -> {  // 生产入库
                show(Prod_InStock_MainActivity::class.java, null)
            }
            R.id.relative2 -> { // 生产领料

            }
            R.id.relative3  -> { // 工艺查看
            }
            R.id.relative4  -> { // 产品入库
                show(Prod_InStock_MainActivity::class.java, null)
            }
        }//                show(Prod_ScInOtherMainActivity.class,null);
        //                show(Mtl_SmSearch_MainActivity.class,null);
    }
}
