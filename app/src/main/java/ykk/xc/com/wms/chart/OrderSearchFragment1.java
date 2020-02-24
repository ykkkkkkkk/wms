package ykk.xc.com.wms.chart;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import ykk.xc.com.wms.R;
import ykk.xc.com.wms.bean.ScanningRecord;
import ykk.xc.com.wms.bean.User;
import ykk.xc.com.wms.comm.BaseFragment;
import ykk.xc.com.wms.comm.Comm;
import ykk.xc.com.wms.util.JsonUtil;
import ykk.xc.com.wms.util.smallchart.chart.CombineChart;
import ykk.xc.com.wms.util.smallchart.data.BarData;
import ykk.xc.com.wms.util.smallchart.data.CurveData;
import ykk.xc.com.wms.util.smallchart.data.LineData;
import ykk.xc.com.wms.util.smallchart.interfaces.iData.IBarLineCurveData;

/**
 * 生产账号销售出库（根据发货通知单）
 */
public class OrderSearchFragment1 extends BaseFragment {

    @BindView(R.id.combineChart)
    CombineChart combineChart;

    private OrderSearchFragment1 context = this;
    private static final int SUCC1 = 200, UNSUCC1 = 500, SUCC2 = 201, UNSUCC2 = 501;
    private static final int SETFOCUS = 1, WRITE_CODE = 2;
    private List<ScanningRecord> checkDatas = new ArrayList<>();
    private int curPos; // 当前行
    private OkHttpClient okHttpClient = null;
    private User user;
    private Activity mContext;
    private OrderSearchMainActivity parent;
    private DecimalFormat df = new DecimalFormat("#.####");


    private CurveData mCurveData = new CurveData();
    private ArrayList<PointF> mPointFArrayList1 = new ArrayList<>();

    private LineData mLineData = new LineData();
    private ArrayList<PointF> mPointFArrayList2 = new ArrayList<>();

    private BarData mBarData = new BarData();
    private ArrayList<PointF> mPointFArrayList3 = new ArrayList<>();

    private ArrayList<IBarLineCurveData> mDatasList = new ArrayList<>();
    // 数据
    protected float[][] points = new float[][]{{1, 10}, {2, 47}, {3, 11}, {4, 38}, {5, 9}, {6, 52}, {7, 14}, {8, 37}, {9, 29},
            {10, 31}};
    protected float[][] points2 = new float[][]{{1, 52}, {2, 13}, {3, 51}, {4, 20}, {5, 19}, {6, 20}, {7, 54}, {8, 7}, {9, 19},
            {10, 41}};
    protected int[] mColors = {0xFFCCFF00, 0xFF6495ED, 0xFFE32636, 0xFF800000, 0xFF808000, 0xFFFF8C69, 0xFF808080,
            0xFFE6B800, 0xFF7CFC00};

    protected float pxTodp(float value) {
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        float valueDP = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, metrics);
        return valueDP;
    }

    // 消息处理
    private MyHandler mHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        private final WeakReference<OrderSearchFragment1> mActivity;

        public MyHandler(OrderSearchFragment1 activity) {
            mActivity = new WeakReference<OrderSearchFragment1>(activity);
        }

        public void handleMessage(Message msg) {
            OrderSearchFragment1 m = mActivity.get();
            if (m != null) {
                m.hideLoadDialog();

                String errMsg = null;
                String msgObj = null;
                if(msg.obj instanceof String) {
                    msgObj = (String) msg.obj;
                }
                switch (msg.what) {
                    case SUCC1:


                        break;
                    case UNSUCC1:
                        errMsg = JsonUtil.strToString(msgObj);
                        if(Comm.isNULLS(errMsg).length() == 0) errMsg = "服务器繁忙，请稍候再试！";
                        Comm.showWarnDialog(m.mContext, errMsg);

                        break;
                }
            }
        }
    }

    @Override
    public View setLayoutResID(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.chart_order_search_fragment1, container, false);
    }

    @Override
    public void initView() {
        mContext = getActivity();
        parent = (OrderSearchMainActivity) mContext;

    }

    @Override
    public void initData() {
        if (okHttpClient == null){
            okHttpClient = new OkHttpClient.Builder()
//                .connectTimeout(10, TimeUnit.SECONDS) // 设置连接超时时间（默认为10秒）
                    .writeTimeout(30, TimeUnit.SECONDS) // 设置写的超时时间
                    .readTimeout(30, TimeUnit.SECONDS) //设置读取超时时间
                    .build();
        }

        getUserInfo();
        combineChart.isAnimated = false;
        combineChart.setDataList(mDatasList);
    }

    /**
     * 查询的方法
     */
    public void findFun() {
        for (int i = 0; i < 8; i++) {
            mPointFArrayList3.add(new PointF(points[i][0], points[i][1]));
            mPointFArrayList2.add(new PointF(points[i][0], points[i][1] - 5));
            mPointFArrayList1.add(new PointF(points[i][0], points[i][1] + 10));
        }
        mBarData.setValue(mPointFArrayList3);
        mBarData.setColor(Color.CYAN);
        mBarData.setPaintWidth(pxTodp(5));
        mBarData.setTextSize(pxTodp(10));
        mDatasList.add(mBarData);

        mLineData.setValue(mPointFArrayList2);
        mLineData.setColor(Color.MAGENTA);
        mLineData.setPaintWidth(pxTodp(3));
        mLineData.setTextSize(pxTodp(10));
        mDatasList.add(mLineData);

        mCurveData.setValue(mPointFArrayList1);
        mCurveData.setColor(Color.YELLOW);
        mCurveData.setPaintWidth(pxTodp(3));
        mCurveData.setTextSize(pxTodp(10));
        mDatasList.add(mCurveData);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser) {
            mHandler.sendEmptyMessageDelayed(SETFOCUS, 200);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

//    @OnClick({R.id.btn_scan, R.id.btn_scan2, R.id.btn_save, R.id.btn_pass, R.id.btn_clone })
//    public void onViewClicked(View view) {
//    }


    /**
     * 选择保存之前的判断
     */
    private boolean saveBefore() {
        if (checkDatas == null || checkDatas.size() == 0) {
            Comm.showWarnDialog(mContext,"请先扫描发货单号！");
            return false;
        }
        // 检查数据
        int size = checkDatas.size();
        for (int i = 0; i < size; i++) {
            ScanningRecord sr = checkDatas.get(i);
//            if (sr.getSourceQty() > sr.getRealQty()) {
//                Comm.showWarnDialog(mContext,"第" + (i + 1) + "行货还没捡完货！");
//                return false;
//            }
            if (sr.getRealQty() > sr.getUseableQty()) {
                Comm.showWarnDialog(mContext,"第" + (i + 1) + "行,拣货数不能大于可用数！");
                return false;
            }
        }

        return true;
    }

    @Override
    public void setListener() {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case WRITE_CODE: // 输入条码返回
                if (resultCode == Activity.RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        String value = bundle.getString("resultValue", "");
                    }
                }

                break;
        }
    }

    /**
     * 判断表中存在该物料
     */
    private void run_findInStockSum() {
        showLoadDialog("加载中...",false);
        StringBuilder strFbillno = new StringBuilder();
        StringBuilder strEntryId = new StringBuilder();
        String mUrl = getURL("scanningRecord/findInStockSum");
        FormBody formBody = new FormBody.Builder()
                .add("fbillType", "4") // fbillType  1：采购订单入库，2：收料任务单入库，3：生产订单入库，4：销售订单出库，5：发货通知单出库
                .add("strFbillno", strFbillno.toString())
                .add("strEntryId", strEntryId.toString())
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
                mHandler.sendEmptyMessage(UNSUCC2);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody body = response.body();
                String result = body.string();
                if (!JsonUtil.isSuccess(result)) {
                    mHandler.sendEmptyMessage(UNSUCC2);
                    return;
                }
                Message msg = mHandler.obtainMessage(SUCC2, result);
                Log.e("run_findInStockSum --> onResponse", result);
                mHandler.sendMessage(msg);
            }
        });
    }


    /**
     *  得到用户对象
     */
    private void getUserInfo() {
        if(user == null) user = showUserByXml();
    }

    @Override
    public void onDestroyView() {
        closeHandler(mHandler);
        mBinder.unbind();
        super.onDestroyView();
    }

}
