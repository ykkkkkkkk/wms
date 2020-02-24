package ykk.xc.com.wms.entrance.page5;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
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
import ykk.xc.com.wms.basics.Dept_DialogActivity;
import ykk.xc.com.wms.bean.Department;
import ykk.xc.com.wms.bean.k3Bean.ICItem;
import ykk.xc.com.wms.bean.prod.ProdOrder;
import ykk.xc.com.wms.comm.BaseFragment;
import ykk.xc.com.wms.comm.Comm;
import ykk.xc.com.wms.entrance.page5.adapter.PrintFragment1Adapter;
import ykk.xc.com.wms.util.JsonUtil;
import ykk.xc.com.wms.util.LogUtil;
import ykk.xc.com.wms.util.basehelper.BaseRecyclerAdapter;
import ykk.xc.com.wms.util.xrecyclerview.XRecyclerView;

import static android.app.Activity.RESULT_OK;

public class PrintFragment1 extends BaseFragment implements XRecyclerView.LoadingListener {

    @BindView(R.id.tv_deptSel)
    TextView tvDeptSel;
    @BindView(R.id.tv_dateSel)
    TextView tvDateSel;
    @BindView(R.id.et_prodNo)
    EditText etProdNo;
    @BindView(R.id.btn_search)
    Button btnSearch;
    @BindView(R.id.cbAll)
    CheckBox cbAll;
    @BindView(R.id.xRecyclerView)
    XRecyclerView xRecyclerView;

    private PrintFragment1 context = this;
    private List<ProdOrder> listDatas = new ArrayList<>();
    private static final int SEL_DEPT = 10;
    private static final int SUCC1 = 200, UNSUCC1 = 500, SUCC2 = 201, UNSUCC2 = 501, UPDATE = 202, UNUPDATE = 502;
    private static final int RESULT_NUM = 1;
    private PrintFragment1Adapter mAdapter;
    private Department department; // 部门
    private OkHttpClient okHttpClient = new OkHttpClient();
    private Activity mContext;
    private PrintMainActivity parent;
    private int limit = 1;
    private boolean isRefresh, isLoadMore, isNextPage;
    private List<ProdOrder> prodOrderList = new ArrayList<>();
    private int curPos = -1; // 当前行
    private String strProdId; // 用，号拼接的生产订单id
    private int prodId; // 生产订单id

    // 消息处理
    final PrintFragment1.MyHandler mHandler = new PrintFragment1.MyHandler(this);

    private static class MyHandler extends Handler {
        private final WeakReference<PrintFragment1> mActivity;

        public MyHandler(PrintFragment1 activity) {
            mActivity = new WeakReference<PrintFragment1>(activity);
        }

        public void handleMessage(Message msg) {
            PrintFragment1 m = mActivity.get();
            if (m != null) {
                m.hideLoadDialog();

                String errMsg = null;
                String msgObj = (String) msg.obj;
                switch (msg.what) {
                    case SUCC1: // 成功
                        List<ProdOrder> list = JsonUtil.strToList2(msgObj, ProdOrder.class);
                        m.listDatas.addAll(list);
                        m.mAdapter.notifyDataSetChanged();

                        if (m.isRefresh) {
                            m.xRecyclerView.refreshComplete(true);
                        } else if (m.isLoadMore) {
                            m.xRecyclerView.loadMoreComplete(true);
                        }

//                        m.xRecyclerView.setPullRefreshEnabled(true); // 上啦刷新开启
                        m.xRecyclerView.setLoadingMoreEnabled(m.isNextPage);

                        break;
                    case UNSUCC1: // 数据加载失败！
                        m.mAdapter.notifyDataSetChanged();
                        m.toasts("抱歉，没有加载到数据！");

                        break;
                    case SUCC2: // 生码成功
                        m.prodOrderList.clear();
                        List<ProdOrder> list2 = JsonUtil.strToList(msgObj, ProdOrder.class);
                        m.prodOrderList.addAll(list2);
                        m.toasts("生码成功✔");
                        m.initLoadDatas();

                        break;
                    case UNSUCC2: // 生码失败
                        errMsg = JsonUtil.strToString(msgObj);
                        if(m.isNULLS(errMsg).length() == 0) errMsg = "服务器繁忙，请稍候再试！";
                        Comm.showWarnDialog(m.mContext, errMsg);

                        break;
                    case UPDATE: // 修改打印次数 成功
                        m.parent.setFragment1Data(1, m.prodOrderList);

                        break;
                    case UNUPDATE: // 修改打印次数 失败
                        errMsg = JsonUtil.strToString(msgObj);
                        if(m.isNULLS(errMsg).length() == 0) errMsg = "服务器繁忙，请稍候再试！";
                        Comm.showWarnDialog(m.mContext, errMsg);

                        break;
                }
            }
        }
    }

    @Override
    public View setLayoutResID(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.ab_print_fragment1, container, false);
    }

    @Override
    public void initView() {
        mContext = getActivity();
        parent = (PrintMainActivity) mContext;

        xRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        xRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new PrintFragment1Adapter(mContext, listDatas);
        xRecyclerView.setAdapter(mAdapter);
        xRecyclerView.setLoadingListener(context);

        xRecyclerView.setPullRefreshEnabled(false); // 上啦刷新禁用
        xRecyclerView.setLoadingMoreEnabled(false); // 不显示下拉刷新的view

        mAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseRecyclerAdapter adapter, BaseRecyclerAdapter.RecyclerHolder holder, View view, int pos) {
                ProdOrder prodOrder = listDatas.get(pos - 1);
                prodOrder.setIsCheck(prodOrder.getIsCheck() == 0 ? 1 : 0);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void initData() {
        hideKeyboard(etProdNo);
        tvDateSel.setText(Comm.getSysDate(7));
        // initLoadDatas();
    }

    @OnClick({R.id.btn_search, R.id.tv_deptSel, R.id.tv_dateSel, R.id.btn_singleProdCode, R.id.btn_batchProdCode, R.id.btn_print})
    public void onViewClicked(View v) {
        Bundle bundle = null;
        switch (v.getId()) {
            case R.id.btn_search: // 查询数据
                initLoadDatas();

                break;
            case R.id.tv_deptSel: // 选择生产车间
                bundle = new Bundle();
                bundle.putInt("departmentProperty", 1070); // 属性：1070-车间,1071-非车间
                showForResult(Dept_DialogActivity.class, SEL_DEPT, bundle);

                break;
            case R.id.tv_dateSel: // 日期
                Comm.showDateDialog(mContext, v, 0);

                break;
            case R.id.btn_singleProdCode: // 单行生码
                prodId = 0;
                double useableQty = 0;
                ICItem icItem = null;
                int count = 0;
                int size = listDatas.size();
                for(int i=0; i<size; i++) {
                    ProdOrder p = listDatas.get(i);
                    icItem = p.getIcItem();
                    if(p.getIsCheck() == 1) {
                        count += 1;
                        curPos = i;
                        prodId = p.getProdId();
                        useableQty = p.getUseableQty();
                    }
                }
                if(count == 0) {
                    Comm.showWarnDialog(mContext, "请选中生码的行！");
                    return;
                }
                if(count > 1) {
                    Comm.showWarnDialog(mContext,"只能选择一行来生码！");
                    return;
                }

                // 是否启用批次号管理。Y代表启用，N代表不启用*/
                if(icItem.getBatchManager().equals("Y") && icItem.getSnManager().equals("N")) {
                    run_prodOrderCreate_app(String.valueOf(prodId), useableQty); // 批量生码
                } else {
                    showInputDialog("生码数量", String.valueOf(useableQty), "0.0", RESULT_NUM);
                }
//                createCodeBefore();

                break;
            case R.id.btn_batchProdCode: // 批量生码
                createCodeBefore();

                break;
            case R.id.btn_print: // 打印
                if(prodOrderList != null && prodOrderList.size() > 0) {
//                    parent.setFragment1Data(1, prodOrderList);
                    String createDateTime = prodOrderList.get(0).getBarcodeCreateDate();
                    run_modifyPrintNumberByBarcode(createDateTime);
                } else {
                    Comm.showWarnDialog(mContext, "请生码，再打印！");
                }


                break;
        }
    }

    /**
     * 生码之前的处理
     */
    private void createCodeBefore() {
        int size = listDatas.size();
        StringBuilder strIds = new StringBuilder();
        for(int i=0; i<size; i++) {
            ProdOrder p = listDatas.get(i);
            if(p.getIsCheck() == 1) {
                strIds.append(p.getProdId()+",");
            }
        }
        int len = strIds.length();
        if(len == 0) {
            Comm.showWarnDialog(mContext, "请选中生码的行！");
            return;
        }
        strIds.delete(len-1, len);
        run_prodOrderCreate_app(strIds.toString(), 0); // 批量生码
    }

    @Override
    public void setListener() {
        cbAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int dataSize = listDatas.size();
                if (dataSize > 0) {
                    for (int i = 0; i < dataSize; i++) {
                        listDatas.get(i).setIsCheck(isChecked ? 1 : 0);
                    }
                    cbAll.setText(isChecked ? "反选" : "全选");
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SEL_DEPT: //查询部门	返回
                if (resultCode == Activity.RESULT_OK) {
                    department = (Department) data.getSerializableExtra("obj");
                    LogUtil.e("onActivityResult --> SEL_DEPT", department.getDepartmentName());
                    tvDeptSel.setText(department.getDepartmentName());
                }

                break;
            case RESULT_NUM: // 数量
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        String value = bundle.getString("resultValue", "");
                        double num = parseDouble(value);
                        if(num <= 0) {
                            Comm.showWarnDialog(mContext,"生码数量必须大于0！");
                            return;
                        }
                        run_prodOrderCreate_app(String.valueOf(prodId), num); // 批量生码
                    }
                }

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
     * 仓库信息，库区，库位，部门，物料
     */
    private void run_okhttpDatas() {
        showLoadDialog("加载中...",false);
        String mUrl = getURL("prodOrder/findProdOrderByPage");
        String prodNo = getValues(etProdNo).trim();
        String date = getValues(tvDateSel);
        FormBody formBody = new FormBody.Builder()
                .add("fbillNo", prodNo) // 生产单号
                .add("deptNumber", department != null ? department.getDepartmentNumber() : "")
                .add("prodFdateBeg", date) // 开始日期
                .add("prodFdateEnd", date) // 结束日期
                .add("prodStatus", "1") // 0：计划，1：下达，3：结案
                .add("createCodeStatus", "1") // 生码状态 1：未生完，2：已生完
                .add("limit", String.valueOf(limit))
                .add("pageSize", "30")
                .build();

        Request request = new Request.Builder()
                .addHeader("cookie", getSession())
                .url(mUrl)
                .post(formBody)
                .build();

        // step 3：创建 Call 对象
        Call call = okHttpClient.newCall(request);

        //step 4: 开始异步请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mHandler.sendEmptyMessage(UNSUCC1);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody body = response.body();
                String result = body.string();
                LogUtil.e("run_okhttpDatas --> onResponse", result);
                if (!JsonUtil.isSuccess(result)) {
                    mHandler.sendEmptyMessage(UNSUCC1);
                    return;
                }
                isNextPage = JsonUtil.isNextPage(result, limit);

                Message msg = mHandler.obtainMessage(SUCC1, result);
                mHandler.sendMessage(msg);
            }
        });
    }

    /**
     * 生产任务单生码（app调用）
     */
    private void run_prodOrderCreate_app(String strIds, double createCodeQty) {
        strProdId = strIds;
        showLoadDialog("加载中...",false);
        String mUrl = getURL("barCodeCreate/prodOrderCreate_app");
        if(createCodeQty > 0) {
            mUrl = getURL("barCodeCreate/prodOrderPartCreate_app");
        }
        FormBody formBody = new FormBody.Builder()
                .add("fIds", strIds) // 生产id拼接
                .add("createCodeQty", createCodeQty > 0 ? String.valueOf(createCodeQty) : "")
                .build();

        Request request = new Request.Builder()
                .addHeader("cookie", getSession())
                .url(mUrl)
                .post(formBody)
                .build();

        // step 3：创建 Call 对象
        Call call = okHttpClient.newCall(request);

        //step 4: 开始异步请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mHandler.sendEmptyMessage(UNSUCC2);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody body = response.body();
                String result = body.string();
                LogUtil.e("run_prodOrderCreate_app --> onResponse", result);
                if (!JsonUtil.isSuccess(result)) {
                    Message msg = mHandler.obtainMessage(UNSUCC2, result);
                    mHandler.sendMessage(msg);
                    return;
                }

                Message msg = mHandler.obtainMessage(SUCC2, result);
                mHandler.sendMessage(msg);
            }
        });
    }

    /**
     * 修改打印次数
     */
    private void run_modifyPrintNumberByBarcode(String createDateTime) {
        showLoadDialog("正在打印...",false);
        String mUrl = getURL("barCodeTable/modifyPrintNumberByBarcode");
        FormBody formBody = new FormBody.Builder()
                .add("createDateTime", createDateTime)
                .add("strProdId", strProdId)
                .build();

        Request request = new Request.Builder()
                .addHeader("cookie", getSession())
                .url(mUrl)
                .post(formBody)
                .build();

        // step 3：创建 Call 对象
        Call call = okHttpClient.newCall(request);

        //step 4: 开始异步请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mHandler.sendEmptyMessage(UNUPDATE);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody body = response.body();
                String result = body.string();
                LogUtil.e("run_prodOrderCreate_app --> onResponse", result);
                if (!JsonUtil.isSuccess(result)) {
                    Message msg = mHandler.obtainMessage(UNUPDATE, result);
                    mHandler.sendMessage(msg);
                    return;
                }

                Message msg = mHandler.obtainMessage(UPDATE, result);
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
    public void onDestroyView() {
        closeHandler(mHandler);
        mBinder.unbind();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        mContext.unregisterReceiver(mReceiver);
    }
}
