package ykk.xc.com.wms.entrance.page5.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import ykk.xc.com.wms.R;
import ykk.xc.com.wms.bean.prod.ProdOrder;
import ykk.xc.com.wms.util.basehelper.BaseArrayRecyclerAdapter;

/**
 * 物料列表适配器
 */
public class PrintFragment1Adapter extends BaseArrayRecyclerAdapter<ProdOrder> {
    //
    private Activity context;
    private MyCallBack callBack;
    private DecimalFormat df = new DecimalFormat("#.####");

    public PrintFragment1Adapter(Activity context, List<ProdOrder> datas) {
        super(datas);
        this.context = context;
    }

    @Override
    public int bindView(int viewtype) {
        return R.layout.ab_print_fragment1_item;
    }

    @Override
    public void onBindHoder(RecyclerHolder holder, final ProdOrder entity, final int pos) {
        // 初始化id
        TextView tv_row = holder.obtainView(R.id.tv_row);
        TextView tv_check = holder.obtainView(R.id.tv_check);
        TextView tv_prodNo = holder.obtainView(R.id.tv_prodNo);
        TextView tv_mtlNumber = holder.obtainView(R.id.tv_mtlNumber);
        TextView tv_mtlName = holder.obtainView(R.id.tv_mtlName);
        TextView tv_nums = holder.obtainView(R.id.tv_nums);
        // 赋值
        tv_row.setText(String.valueOf(pos + 1));
        tv_prodNo.setText(entity.getProdNo());
        tv_mtlNumber.setText(entity.getIcItemNumber());
        tv_mtlName.setText(entity.getIcItemName());
        tv_nums.setText(df.format(entity.getUseableQty())+""+entity.getUnitName());

        View view = (View) tv_row.getParent();
        if (entity.getIsCheck() == 1) {
            tv_check.setBackgroundResource(R.drawable.check_true);
            view.setBackgroundResource(R.drawable.back_style_check1_true);
        } else {
            tv_check.setBackgroundResource(R.drawable.check_false);
            view.setBackgroundResource(R.drawable.back_style_check1_false);
        }
    }

    public void setCallBack(MyCallBack callBack) {
        this.callBack = callBack;
    }

    public interface MyCallBack {
        void onPrint(ProdOrder entity, int position);
    }



}
