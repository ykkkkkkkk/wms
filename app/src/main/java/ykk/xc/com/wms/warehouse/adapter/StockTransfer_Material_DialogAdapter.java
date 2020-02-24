package ykk.xc.com.wms.warehouse.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import ykk.xc.com.wms.R;
import ykk.xc.com.wms.bean.k3Bean.ICInvBackup;
import ykk.xc.com.wms.bean.k3Bean.ICInventory;
import ykk.xc.com.wms.util.basehelper.BaseArrayRecyclerAdapter;

public class StockTransfer_Material_DialogAdapter extends BaseArrayRecyclerAdapter<ICInventory> {

    private Activity context;
    private MyCallBack callBack;
    private List<ICInventory> datas;
    private DecimalFormat df = new DecimalFormat("#.######");

    public StockTransfer_Material_DialogAdapter(Activity context, List<ICInventory> datas) {
        super(datas);
        this.context = context;
        this.datas = datas;
    }

    @Override
    public int bindView(int viewtype) {
        return R.layout.ware_stock_transfer_material_dialog_item;
    }

    @Override
    public void onBindHoder(RecyclerHolder holder, ICInventory entity, final int pos) {
        // 初始化id
        TextView tv_row = holder.obtainView(R.id.tv_row);
        TextView tv_fnumber = holder.obtainView(R.id.tv_fnumber);
        TextView tv_fname = holder.obtainView(R.id.tv_fname);
        TextView tv_stockQty = holder.obtainView(R.id.tv_stockQty);
        // 赋值
        tv_row.setText(String.valueOf(pos + 1));
        tv_fnumber.setText(entity.getMtlNumber());
        tv_fname.setText(entity.getMtlName());
        tv_stockQty.setText(df.format(entity.getfQty()));

        View view = (View) tv_row.getParent();
        if(entity.isCheck()) {
            view.setBackgroundResource(R.drawable.back_style_check1_true);
        } else {
            view.setBackgroundResource(R.drawable.back_style_check1_false);
        }
    }

    public void setCallBack(MyCallBack callBack) {
        this.callBack = callBack;
    }

    public interface MyCallBack {
        void onClick(ICInvBackup entity, int position);
    }

}
