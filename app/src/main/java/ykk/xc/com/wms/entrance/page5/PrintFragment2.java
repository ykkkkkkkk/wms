package ykk.xc.com.wms.entrance.page5;//package ykk.cb.com.zcws.basics;
//
//import android.app.Activity;
//import android.content.Context;
//import android.graphics.Color;
//import android.graphics.drawable.BitmapDrawable;
//import android.os.Handler;
//import android.os.Message;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.util.Log;
//import android.view.KeyEvent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.PopupWindow;
//import android.widget.TextView;
//
//import java.io.IOException;
//import java.lang.ref.WeakReference;
//
//import butterknife.BindView;
//import butterknife.OnClick;
//import okhttp3.Call;
//import okhttp3.Callback;
//import okhttp3.FormBody;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.Response;
//import okhttp3.ResponseBody;
//import ykk.cb.com.zcws.R;
//import ykk.cb.com.zcws.comm.BaseFragment;
//import ykk.cb.com.zcws.comm.Comm;
//import ykk.cb.com.zcws.util.JsonUtil;
//import ykk.cb.com.zcws.util.interfaces.IFragmentKeyeventListener;
//
//public class PrintFragment2 extends BaseFragment implements IFragmentKeyeventListener {
//
//    @BindView(R.id.et_getFocus)
//    EditText etGetFocus;
//    @BindView(R.id.et_code)
//    EditText etCode;
//    @BindView(R.id.tv_selectType)
//    TextView tvSelectType;
//    @BindView(R.id.btn_big)
//    Button btnBig;
//    @BindView(R.id.btn_small)
//    Button btnSmall;
//
//    private PrintFragment2 context = this;
//    private static final int SUCC1 = 200, UNSUCC1 = 501, SETFOCUS = 1;
//    private OkHttpClient okHttpClient = new OkHttpClient();
//    private int caseId = 34; // （34：生产订单）
//    private String barcode; // 对应的条码号
//    private Activity mContext;
//    private PrintMainActivity parent;
//    private int tabFormat = 1; // 1：大标签，2：小标签 ，4：生产装箱清单，5：复核装箱清单
//    private int smType = 1; // 扫码类型  1：生产订单号，2：生产顺序号，3：生产装箱清单，4：复核装箱清单
//    private Button curBtn;
//
//    // 消息处理
//    private PrintFragment2.MyHandler mHandler = new PrintFragment2.MyHandler(this);
//    private static class MyHandler extends Handler {
//        private final WeakReference<PrintFragment2> mActivity;
//
//        public MyHandler(PrintFragment2 activity) {
//            mActivity = new WeakReference<PrintFragment2>(activity);
//        }
//
//        public void handleMessage(Message msg) {
//            PrintFragment2 m = mActivity.get();
//            if (m != null) {
//                m.hideLoadDialog();
//
//                switch (msg.what) {
//                    case SUCC1: // 成功
//                        String result = (String) msg.obj;
//                        if(m.smType == 1 || m.smType == 2) {
//                            m.parent.setFragmentPrint2(m.tabFormat, result);
//                        } else if(m.smType == 3) { // 生产装箱清单
//                            m.tabFormat = 4;
//                            m.parent.setFragmentPrint2B(m.tabFormat, result);
//
//                        } else if(m.smType == 4) { // 复核装箱清单
//                            m.tabFormat = 5;
//                            m.parent.setFragmentPrint2C(m.tabFormat, result);
//                        }
//
//                        break;
//                    case UNSUCC1: // 数据加载失败！
//                        String str = JsonUtil.strToString((String) msg.obj);
//                        if(m.isNULLS(str).length() == 0) {
//                            str = "很抱歉，没有找到数据！";
//                        }
//                        Comm.showWarnDialog(m.mContext,str);
//
//                        break;
//                    case SETFOCUS: // 当弹出其他窗口会抢夺焦点，需要跳转下，才能正常得到值
//                        m.setFocusable(m.etGetFocus);
//                        m.setFocusable(m.etCode);
//                        break;
//                }
//            }
//        }
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        parent = (PrintMainActivity) context;
//        parent.setFragmentKeyeventListener(this);
//    }
//
//    @Override
//    public View setLayoutResID(LayoutInflater inflater, ViewGroup container) {
//        return inflater.inflate(R.layout.ab_print_fragment2, container, false);
//    }
//
//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if(isVisibleToUser) {
//            hideKeyboard(etCode);
//        }
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        mHandler.sendEmptyMessageDelayed(SETFOCUS,200);
//    }
//
//    @Override
//    public void initView() {
//        mContext = getActivity();
//        parent = (PrintMainActivity) mContext;
//        curBtn = btnSmall;
//    }
//
//    @Override
//    public void initData() {
//        hideSoftInputMode(mContext, etCode);
//    }
//
//    @OnClick({R.id.tv_selectType, R.id.btn_big, R.id.btn_small})
//    public void onViewClicked(View v) {
//        switch (v.getId()) {
//            case R.id.tv_selectType: // 选择打印的表
//                pop_selectType(v);
//                popWindow.showAsDropDown(v);
//
//                break;
//            case R.id.btn_big: // 大标签
//                tabFormat = 1;
//                tagSelected(btnBig);
//
//                break;
//            case R.id.btn_small: // 小标签
//                tabFormat = 2;
//                tagSelected(btnSmall);
//
//                break;
//        }
//    }
//
//    /**
//     * 选中之后改变样式
//     */
//    private void tagSelected(Button btn) {
//        if(curBtn.getId() == btn.getId()) {
//            return;
//        }
//        curBtn.setText(getValues(curBtn).replace("✔",""));
//        curBtn.setTextColor(Color.parseColor("#666666" +""));
//        curBtn.setBackgroundResource(R.drawable.back_style_gray3);
//        btn.setText(getValues(btn)+"✔");
//        btn.setTextColor(Color.parseColor("#FFFFFF"));
//        btn.setBackgroundResource(R.drawable.shape_purple1a);
//        curBtn = btn;
//    }
//
////    @Override
////    public boolean dispatchKeyEvent(KeyEvent event) {
////        // 按了删除键，回退键
////        if(event.getKeyCode() == KeyEvent.KEYCODE_FORWARD_DEL || event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
////            return false;
////        }
////        return super.dispatchKeyEvent(event);
////    }
//
//    @Override
//    public void setListener() {
//        // 扫码区
//        etCode.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) { }
//            @Override
//            public void afterTextChanged(Editable s) {
//                if(s.length() == 0) return;
//                barcode = s.toString();
//                // 执行查询方法
//                run_print();
//            }
//        });
//    }
//
//    /**
//     * 创建PopupWindow 【 查询来源类型 】
//     */
//    private PopupWindow popWindow;
//
//    @SuppressWarnings("deprecation")
//    private void pop_selectType(View v) {
//        if (null != popWindow) {//不为空就隐藏
//            popWindow.dismiss();
//            return;
//        }
//        // 获取自定义布局文件popupwindow_left.xml的视图
//        final View popV = getLayoutInflater().inflate(R.layout.ab_print_fragment2_type, null);
//        // 创建PopupWindow实例,200,LayoutParams.MATCH_PARENT分别是宽度和高度
//        popWindow = new PopupWindow(popV, v.getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT, true);
//        // 设置动画效果
//        // popWindow.setAnimationStyle(R.style.AnimationFade);
//        popWindow.setBackgroundDrawable(new BitmapDrawable());
//        popWindow.setOutsideTouchable(true);
//        popWindow.setFocusable(true);
//
//        // 点击其他地方消失
//        View.OnClickListener click = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int tmpId = 0;
//                switch (v.getId()) {
//                    case R.id.btn1: // 生产订单号
//                        smType = 1;
//                        tmpId = v.getId();
//                        caseId = 34;
//                        btnBig.setVisibility(View.VISIBLE);
//                        btnSmall.setVisibility(View.VISIBLE);
//
//                        break;
//                    case R.id.btn2: // 生产顺序号
//                        smType = 2;
//                        tmpId = v.getId();
//                        caseId = 34;
//                        btnBig.setVisibility(View.VISIBLE);
//                        btnSmall.setVisibility(View.VISIBLE);
//
//                        break;
//                    case R.id.btn3: // 生产装箱清单
//                        smType = 3;
//                        tmpId = v.getId();
//                        caseId = 34;
//                        btnBig.setVisibility(View.GONE);
//                        btnSmall.setVisibility(View.GONE);
//
//                        break;
//                    case R.id.btn4: // 复核装箱清单
//                        smType = 4;
//                        tmpId = v.getId();
//                        caseId = 37;
//                        btnBig.setVisibility(View.GONE);
//                        btnSmall.setVisibility(View.GONE);
//
//                        break;
//                }
//                popWindow.dismiss();
//                tvSelectType.setText(getValues((Button) popV.findViewById(tmpId)));
//            }
//        };
//        popV.findViewById(R.id.btn1).setOnClickListener(click);
//        popV.findViewById(R.id.btn2).setOnClickListener(click);
//        popV.findViewById(R.id.btn3).setOnClickListener(click);
//        popV.findViewById(R.id.btn4).setOnClickListener(click);
//    }
//
//    /**
//     * 得到条码号
//     */
//    private void run_print() {
//        showLoadDialog("打印连接中...");
//        String mUrl = null;
//
//        String fbillno = "", prodSeqNumber = "", boxBarCode = "", caseId = "";
//        switch (smType) {
//            case 1: // 生产订单号
//                fbillno = barcode;
//                mUrl = getURL("bigPrint");
//                break;
//            case 2: // 生产顺序号
//                prodSeqNumber = barcode;
//                mUrl = getURL("bigPrint");
//                break;
//            case 3: // 生产装箱清单
//                boxBarCode = barcode;
//                caseId = String.valueOf(context.caseId);
//                mUrl = getURL("boxBarCode/findBarcode");
//                break;
//            case 4: // 复核装箱清单
//                boxBarCode = barcode;
//                caseId = String.valueOf(context.caseId);
//                mUrl = getURL("boxBarCode/findBarcode");
//                break;
//        }
//        FormBody formBody = new FormBody.Builder()
//                .add("fbillno", fbillno) // 1,2
//                .add("prodSeqNumber", prodSeqNumber) // 1,2
//                .add("smType", String.valueOf(smType)) // 1,2
//                .add("barcode", boxBarCode)  // 3,4
//                .add("caseId", caseId)  // 3,4
//                .add("caseId2", caseId)  // 3,4
//                .build();
//
//        Request request = new Request.Builder()
//                .addHeader("cookie", getSession())
//                .url(mUrl)
//                .post(formBody)
//                .build();
//
//        Call call = okHttpClient.newCall(request);
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                mHandler.sendEmptyMessageDelayed(UNSUCC1, 1000);
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                ResponseBody body = response.body();
//                String result = body.string();
//                if(!JsonUtil.isSuccess(result)) {
//                    Message msg = mHandler.obtainMessage(UNSUCC1, result);
//                    mHandler.sendMessage(msg);
//                    return;
//                }
//                Message msg = mHandler.obtainMessage(SUCC1, result);
//                Log.e("SaoMaPrintActivity --> run_print", result);
//                mHandler.sendMessage(msg);
//            }
//        });
//    }
//
//    @Override
//    public boolean onFragmentKeyEvent(KeyEvent event) {
//        if(!(event.getKeyCode() == 240 || event.getKeyCode() == 241)) {
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public void onDestroyView() {
//        closeHandler(mHandler);
//        mBinder.unbind();
//        super.onDestroyView();
//    }
//}
