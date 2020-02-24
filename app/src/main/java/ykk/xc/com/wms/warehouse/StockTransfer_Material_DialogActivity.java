package ykk.xc.com.wms.warehouse;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import ykk.xc.com.wms.R;
import ykk.xc.com.wms.bean.k3Bean.ICInventory;
import ykk.xc.com.wms.comm.BaseDialogActivity;
import ykk.xc.com.wms.comm.Comm;
import ykk.xc.com.wms.util.JsonUtil;
import ykk.xc.com.wms.util.basehelper.BaseRecyclerAdapter;
import ykk.xc.com.wms.util.xrecyclerview.XRecyclerView;
import ykk.xc.com.wms.warehouse.adapter.StockTransfer_Material_DialogAdapter;

/**
 * 选择库存物料dialog
 */
public class StockTransfer_Material_DialogActivity extends BaseDialogActivity implements XRecyclerView.LoadingListener {

    @BindView(R.id.btn_close)
    Button btnClose;
    @BindView(R.id.xRecyclerView)
    XRecyclerView xRecyclerView;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.btn_search)
    Button btnSearch;
    private StockTransfer_Material_DialogActivity context = this;
    private static final int SUCC1 = 200, UNSUCC1 = 501;
    private List<ICInventory> listDatas = new ArrayList<>();
    private StockTransfer_Material_DialogAdapter mAdapter;
    private OkHttpClient okHttpClient = new OkHttpClient();
    private int limit = 1;
    private boolean isRefresh, isLoadMore, isNextPage;
    private int fStockID; // 仓库id
    private int fStockPlaceID; // 库位id
    private String accountType; // 账号类型（DS：电商，SC：生产）

    // 消息处理
    private MyHandler mHandler = new MyHandler(this);
    private static class MyHandler extends Handler {
        private final WeakReference<StockTransfer_Material_DialogActivity> mActivity;

        public MyHandler(StockTransfer_Material_DialogActivity activity) {
            mActivity = new WeakReference<StockTransfer_Material_DialogActivity>(activity);
        }

        public void handleMessage(Message msg) {
            StockTransfer_Material_DialogActivity m = mActivity.get();
            if (m != null) {
                m.hideLoadDialog();
                switch (msg.what) {
                    case SUCC1: // 成功
                        List<ICInventory> list = JsonUtil.strToList2((String) msg.obj, ICInventory.class);
                        m.listDatas.addAll(list);
                        m.mAdapter.notifyDataSetChanged();

                        if (m.isRefresh) {
                            m.xRecyclerView.refreshComplete(true);
                        } else if (m.isLoadMore) {
                            m.xRecyclerView.loadMoreComplete(true);
                        }

                        m.xRecyclerView.setLoadingMoreEnabled(m.isNextPage);

                        break;
                    case UNSUCC1: // 数据加载失败！
                        m.mAdapter.notifyDataSetChanged();
                        m.toasts("抱歉，没有加载到数据！");

                        break;
                }
            }
        }

    }

    @Override
    public int setLayoutResID() {
        return R.layout.ware_stock_transfer_material_dialog;
    }

    @Override
    public void initView() {
        xRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        xRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mAdapter = new StockTransfer_Material_DialogAdapter(context, listDatas);
        xRecyclerView.setAdapter(mAdapter);
        xRecyclerView.setLoadingListener(context);

        xRecyclerView.setPullRefreshEnabled(false); // 上啦刷新禁用
//        xRecyclerView.setLoadingMoreEnabled(false); // 不显示下拉刷新的view

        mAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseRecyclerAdapter adapter, BaseRecyclerAdapter.RecyclerHolder holder, View view, int pos) {
                ICInventory m = listDatas.get(pos-1);
                boolean isCheck = m.isCheck();
                if(isCheck) {
                    m.setCheck(false);
                } else {
                    m.setCheck(true);
                }
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void initData() {
        Bundle bundle = context.getIntent().getExtras();
        if(bundle != null) {
            fStockID = bundle.getInt("fStockID");
            fStockPlaceID = bundle.getInt("fStockPlaceID");
            accountType = bundle.getString("accountType");
        }

        initLoadDatas();

        etSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    etSearch.setBackgroundResource(R.drawable.back_style_gray);
                }
            }
        });
    }


    // 监听事件
    @OnClick({R.id.btn_close, R.id.btn_search, R.id.btn_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_close:
                closeHandler(mHandler);
                context.finish();

                break;
            case R.id.btn_search:
                initLoadDatas();

                break;
            case R.id.btn_confirm: // 确认
                int size = listDatas.size();
                if(size == 0) {
                    Comm.showWarnDialog(context,"请查询数据！");
                    return;
                }
                List<ICInventory> list = new ArrayList<>();
                for(int i=0; i<size; i++) {
                    ICInventory mtl = listDatas.get(i);
                    if(mtl.isCheck()) {
                        list.add(mtl);
                    }
                }
                if(list.size() == 0) {
                    Comm.showWarnDialog(context,"请至少选择一行数据！");
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra("obj", (Serializable) list);
                context.setResult(RESULT_OK, intent);
                context.finish();

                break;
        }
    }

    private void initLoadDatas() {
        limit = 1;
        listDatas.clear();
        run_okhttpDatas();
    }

    /**
     * 通过okhttp加载数据
     */
    private void run_okhttpDatas() {
        showLoadDialog("加载中...",false);
        String mUrl = getURL("icInventory/findMtlList");
        FormBody formBody = new FormBody.Builder()
                .add("fNumberAndName", getValues(etSearch).trim())
                .add("fStockID", String.valueOf(fStockID)) // 仓库id
                .add("fStockPlaceID", String.valueOf(fStockPlaceID)) // 库位id
                .add("accountType", isNULL2(accountType,"SC"))
                .add("limit", String.valueOf(limit))
                .add("pageSize", "50")
                .build();

        Request request = new Request.Builder()
                .addHeader("cookie", getSession())
                .url(mUrl)
                .post(formBody)
                .build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mHandler.sendEmptyMessage(UNSUCC1);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody body = response.body();
                String result = body.string();
                if (!JsonUtil.isSuccess(result)) {
                    mHandler.sendEmptyMessage(UNSUCC1);
                    return;
                }
                isNextPage = JsonUtil.isNextPage(result, limit);

                Message msg = mHandler.obtainMessage(SUCC1, result);
                Log.e("run_okhttpDatas --> onResponse", result);
                mHandler.sendMessage(msg);
            }
        });
    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        isLoadMore = false;
        initLoadDatas();
    }

    @Override
    public void onLoadMore() {
        isRefresh = false;
        isLoadMore = true;
        limit += 1;
        run_okhttpDatas();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            closeHandler(mHandler);
            context.finish();
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        closeHandler(mHandler);
        super.onDestroy();
    }
}
