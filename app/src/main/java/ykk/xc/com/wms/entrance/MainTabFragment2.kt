package ykk.xc.com.wms.entrance


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.OnClick
import ykk.xc.com.wms.R
import ykk.xc.com.wms.comm.BaseFragment
import ykk.xc.com.wms.produce.Prod_Box_MainActivity
import ykk.xc.com.wms.produce.Prod_Box_UnBind_MainActivity
import ykk.xc.com.wms.produce.Prod_InStock_MainActivity
import ykk.xc.com.wms.warehouse.OutInStock_Search_MainActivity

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
            R.id.relative1 -> {  // 生产装箱
                show(Prod_Box_MainActivity::class.java, null)
            }
            R.id.relative2 -> { // 开箱取物
                show(Prod_Box_UnBind_MainActivity::class.java, null)
            }
            R.id.relative3  -> { // 生产入库
                show(Prod_InStock_MainActivity::class.java, null)
            }
            R.id.relative4  -> { // 待上传
                val bundle = Bundle()
                bundle.putInt("pageId", 4)
                bundle.putString("billType", "SCRK")
                show(OutInStock_Search_MainActivity::class.java, bundle)
            }
        }
    }
}
