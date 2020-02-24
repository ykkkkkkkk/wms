package ykk.xc.com.wms.warehouse.adapter;

import android.app.Activity;
import android.widget.TextView;

import java.util.List;

import ykk.xc.com.wms.R;
import ykk.xc.com.wms.bean.k3Bean.ICStockCheckProcess;
import ykk.xc.com.wms.util.basehelper.BaseArrayRecyclerAdapter;

public class ICStockCheckProcess_DialogAdapter extends BaseArrayRecyclerAdapter<ICStockCheckProcess> {

    private Activity context;
    private MyCallBack callBack;
    private List<ICStockCheckProcess> datas;

    public ICStockCheckProcess_DialogAdapter(Activity context, List<ICStockCheckProcess> datas) {
        super(datas);
        this.context = context;
        this.datas = datas;
    }

    @Override
    public int bindView(int viewtype) {
        return R.layout.ware_icstockcheckprocess_dialog_item;
    }

    @Override
    public void onBindHoder(RecyclerHolder holder, ICStockCheckProcess entity, final int pos) {
        // 初始化id
        TextView tv_row = holder.obtainView(R.id.tv_row);
        TextView tv_fname = holder.obtainView(R.id.tv_fname);
        // 赋值
        tv_row.setText(String.valueOf(pos + 1));
        tv_fname.setText(entity.getFprocessId());
    }

    public void setCallBack(MyCallBack callBack) {
        this.callBack = callBack;
    }

    public interface MyCallBack {
        void onClick(ICStockCheckProcess entity, int position);
    }

}
