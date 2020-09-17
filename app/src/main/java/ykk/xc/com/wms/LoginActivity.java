package ykk.xc.com.wms;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import ykk.xc.com.wms.bean.SystemSet;
import ykk.xc.com.wms.bean.User;
import ykk.xc.com.wms.comm.BaseActivity;
import ykk.xc.com.wms.comm.Comm;
import ykk.xc.com.wms.comm.Consts;
import ykk.xc.com.wms.entrance.MainTabFragmentActivity;
import ykk.xc.com.wms.entrance.page5.ServiceSetActivity;
import ykk.xc.com.wms.util.JsonUtil;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.et_userName)
    EditText etUserName;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.btn_set)
    Button btnSet;

    private LoginActivity context = this;

    private OkHttpClient okHttpClient = new OkHttpClient();
    private String result;
    private static final int SUCC1 = 200, UNSUCC1 = 500;
    private static final int REQUESTCODE = 101;
    private boolean isEntryPermission = false; // 是否打开了设置权限的页面


    // 消息处理
    final MyHandler mHandler = new MyHandler(this);
    private static class MyHandler extends Handler {
        private final WeakReference<LoginActivity> mActivity;

        public MyHandler(LoginActivity activity) {
            mActivity = new WeakReference<LoginActivity>(activity);
        }

        public void handleMessage(Message msg) {
            LoginActivity m = mActivity.get();
            if (m != null) {
                m.hideLoadDialog();
                switch (msg.what){
                    case SUCC1: // 登录成功
                        User user = JsonUtil.strToObject((String) msg.obj, User.class);
                        user.setPassword(m.getValues(m.etPwd).trim());
                        // user对象保存到xml
                        m.saveUserToXml(user);
                        // 保存系统参数设置信息到Xml
                        List<SystemSet> systemSets = user.getSysSetList();
                        if(systemSets != null && systemSets.size() > 0) {
                            SharedPreferences sp = m.context.getSharedPreferences(m.getResStr(R.string.saveSystemSet), MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            for(int i=0; i<systemSets.size(); i++) {
                                SystemSet sysSet = systemSets.get(i);
                                editor.putString(sysSet.getSetItem().name(), sysSet.getValue());
                            }
                            editor.commit();
                        }

                        m.show(MainTabFragmentActivity.class, null);
                        m.context.finish();

                        break;
                    case UNSUCC1: // 登录失败！
                        String str = JsonUtil.strToString((String) msg.obj);
                        if(m.isNULLS(str).length() > 0) {
                            m.toasts(str);
                        } else {
                            m.toasts("服务器繁忙，请稍候再试！");
                        }

                        break;
                }
            }
        }
    }

    @Override
    public int setLayoutResID() {
        return R.layout.login;
    }

    @Override
    public void initData() {
        SharedPreferences spfConfig = spf(getResStr(R.string.saveConfig));
        String ip = spfConfig.getString("ip", "192.168.3.198");
        String port = spfConfig.getString("port", "8080");
        Consts.setIp(ip);
        Consts.setPort(port);
        // 保存在xml中的对象
        User user = showUserByXml();
        if(user != null) {
            setTexts(etUserName, user.getUsername());
            setTexts(etPwd, user.getPassword());
        }
        requestPermission();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isEntryPermission) {
            isEntryPermission = false;
            requestPermission();
        }
    }

    @OnClick({R.id.btn_set, R.id.btn_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_set: // 服务器设置
                show(ServiceSetActivity.class, null);

                break;
            case R.id.btn_login: // 登录
                String userName = getValues(etUserName).trim();
                if(userName.length() == 0) {
                    toasts("请输入账号！");
                    return;
                }
                String pwd = getValues(etPwd).trim();
                if(pwd.length() == 0) {
                    toasts("请输入密码！");
                    return;
                }
                hideKeyboard(getCurrentFocus());
                run_appLogin();

                break;
        }
    }

    /**
     * 登录的方法
     */
    private void run_appLogin() {
        showLoadDialog("登录中...",false);
        String mUrl = getURL("appLogin");
        FormBody formBody = new FormBody.Builder()
                .add("username", getValues(etUserName).trim())
                .add("password", getValues(etPwd).trim())
                .build();
        final Request request = new Request.Builder()
                .url(mUrl)
                .post(formBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mHandler.sendEmptyMessage(UNSUCC1);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody body = response.body();
                String result = body.string();
                if(!JsonUtil.isSuccess(result)) {
                    Message msg = mHandler.obtainMessage(UNSUCC1, result);
                    mHandler.sendMessage(msg);
                    return;
                }
                Message msg = mHandler.obtainMessage(SUCC1, result);
                Log.e("run_appLogin --> onResponse", result);
                mHandler.sendMessage(msg);

                //获取session的操作，session放在cookie头，且取出后含有“；”，取出后为下面的 s （也就是jsesseionid）
                Headers headers = response.headers();
                Log.d("info_headers", "header " + headers);
                List<String> cookies = headers.values("Set-Cookie");
                String session = cookies.get(0);
                Log.d("info_cookies", "onResponse-size: " + cookies);
                String s = session.substring(0, session.indexOf(";"));
                // 保存到xml中
                SharedPreferences spfOther = spf(getResStr(R.string.saveOther));
                spfOther.edit().putString("session", s).commit();


            }
        });
    }

    /**
     * 请求用户授权SD卡读写权限
     */
    private void requestPermission() {
        // 判断sdk是是否大于等于6.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};

            // 逐个判断哪些权限未授权，将未授权的权限存储到mPermissionList中
            List<String>  mPermissionList = new ArrayList<>();

            mPermissionList.clear();//清空已经允许的没有通过的权限

            //逐个判断是否还有未通过的权限
            for (int i = 0;i<permissions.length;i++){
                if (checkSelfPermission(permissions[i]) != PackageManager.PERMISSION_GRANTED){
                    mPermissionList.add(permissions[i]);//添加还未授予的权限到mPermissionList中
                }
            }

            //申请权限
            if (mPermissionList.size() > 0){//有权限没有通过，需要申请
                requestPermissions(permissions, REQUESTCODE);
            }else {
                //权限已经都通过了，可以将程序继续打开了
                createFile();
            }

            // 之前的写法：    读写文件
//            int checkSelfPermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
//            if (checkSelfPermission != PackageManager.PERMISSION_DENIED) {
//                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUESTCODE);
//
//            } else {
//                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUESTCODE);
//            }
        } else { // 6.0以下，直接执行
            createFile();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUESTCODE: // 读写权限
                boolean hasPermissionDismiss = false;//有权限没有通过
                for (int i=0;i<grantResults.length;i++){
                    if (grantResults[i] == -1){
                        hasPermissionDismiss = true;
                        break;
                    }
                }
                if (hasPermissionDismiss){//如果有没有被允许的权限
                    showPermissionDialog();
                }else {
                    //权限已经都通过了
                    createFile();
                }

                // 之前的写法
//                if (permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    //用户同意
//                    createFile();
//                } else {
//                    //用户不同意
//                    AlertDialog alertDialog = new AlertDialog.Builder(context)
//                            .setTitle("授权提示").setMessage("您已禁用了SD卡的读写权限,会导致部分功能不能用，去打开吧！")
//                            .setPositiveButton("好的", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//                                    Intent mIntent = new Intent();
//                                    mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                    if (Build.VERSION.SDK_INT >= 9) {
//                                        mIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
//                                        mIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
//                                    } else if (Build.VERSION.SDK_INT <= 8) {
//                                        mIntent.setAction(Intent.ACTION_VIEW);
//                                        mIntent.setClassName("com.android.settings", "com.android.setting.InstalledAppDetails");
//                                        mIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
//                                    }
//
//                                    context.startActivity(mIntent);
//                                    isEntryPermission = true;
//                                }
//                            })
////                        .setNegativeButton("不了", null)
//                            .create();// 创建
//                    alertDialog.setCancelable(false);
//                    alertDialog.setCanceledOnTouchOutside(false);
//                    alertDialog.show();// 显示
//                }

                break;
        }
    }

    private void showPermissionDialog() {
        //用户不同意
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setTitle("授权提示").setMessage("您已禁用了部分权限，请手动授予！")
                .setPositiveButton("好的", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent mIntent = new Intent();
                        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        if (Build.VERSION.SDK_INT >= 9) {
                            mIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                            mIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
                        } else if (Build.VERSION.SDK_INT <= 8) {
                            mIntent.setAction(Intent.ACTION_VIEW);
                            mIntent.setClassName("com.android.settings", "com.android.setting.InstalledAppDetails");
                            mIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
                        }

                        context.startActivity(mIntent);
                        isEntryPermission = true;
                    }
                })
//                        .setNegativeButton("不了", null)
                .create();// 创建
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();// 显示
    }

    private void createFile() {
        File file = new File(Comm.publicPaths+"updateFile");
        if (!file.exists()) {
            boolean isSuccess = file.mkdirs();
            Log.d("isSuccess:", "----------0------------------" + isSuccess);
        }
    }

    @Override
    protected void onDestroy() {
        closeHandler(mHandler);
        super.onDestroy();
    }
}
