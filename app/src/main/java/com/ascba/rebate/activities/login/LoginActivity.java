package com.ascba.rebate.activities.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.activities.main.MainActivity;
import com.ascba.rebate.activities.password_loss.PasswordLossActivity;
import com.ascba.rebate.activities.register.RegisterInputNumberActivity;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.application.MyApplication;
import com.ascba.rebate.utils.DialogHome;
import com.ascba.rebate.utils.ExampleUtil;
import com.ascba.rebate.utils.LogUtils;
import com.ascba.rebate.utils.RegexUtils;
import com.ascba.rebate.utils.UrlEncodeUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.taobao.sophix.SophixManager;
import com.yanzhenjie.nohttp.rest.Request;

import org.json.JSONObject;

import java.util.LinkedHashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * 登录页面
 */

public class LoginActivity extends BaseNetActivity {
    private EditText edPhone;
    private EditText edPassword;
    private String loginPhone;
    private static final int MSG_SET_ALIAS = 1001;
    private static final int MSG_SET_TAGS = 1002;
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    JPushInterface.setAliasAndTags(getApplicationContext(), (String) msg.obj, null, mAliasCallback);
                    break;
                case MSG_SET_TAGS:
                    JPushInterface.setAliasAndTags(getApplicationContext(), null, (Set<String>) msg.obj, mTagsCallback);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        backFirstPhone();//传回注册成功的手机账号
        backLossPhone();//密码找回成功
    }


    private void backLossPhone() {
        String loss_password = getIntent().getStringExtra("loss_password");
        if (loss_password != null) {
            Toast.makeText(this, "密码找回成功", Toast.LENGTH_SHORT).show();
            edPassword.setText("");
            AppConfig.getInstance().putString("login_password", "");
        }
    }


    private void backFirstPhone() {
        String phone_number = getIntent().getStringExtra("phone_number");
        if (phone_number != null) {
            Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
            edPhone.setText(phone_number);
            edPassword.requestFocusFromTouch();
        }
    }

    private void initViews() {
        edPhone = (EditText) findViewById(R.id.login_phone_ed);
        edPassword = (EditText) findViewById(R.id.login_password_ed);
        String login_phone = AppConfig.getInstance().getString("login_phone", "");
        if (!"".equals(login_phone)) {
            edPhone.setText(login_phone);
            edPassword.requestFocusFromTouch();
        }
    }


    //点击登录按钮
    public void goMain(View view) {
        loginRequest();
    }


    //进入密码找回页面
    public void goForgetPassword(View view) {
        Intent intent = new Intent(this, PasswordLossActivity.class);
        startActivity(intent);
    }

    //进入注册页面
    public void goRegister(View view) {
        Intent intent = new Intent(this, RegisterInputNumberActivity.class);
        startActivity(intent);
    }

    //返回
    public void goBack(View view) {
        MyApplication.isLoad = false;
        setResult(RESULT_CANCELED, getIntent());
        finish();
    }

    private void loginRequest() {
        loginPhone = edPhone.getText().toString();
        String loginPassword = edPassword.getText().toString();
        if (loginPhone.equals("") || loginPassword.equals("")) {
            Toast.makeText(this, "账号或密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!RegexUtils.isMobileExact(loginPhone)) {
            getDm().buildAlertDialog("请输入正确的11位手机号码");
            return;
        }
        Request<JSONObject> jsonRequest = buildNetRequest(UrlUtils.login, 0, false);
        jsonRequest.add("sign", UrlEncodeUtils.createSign(UrlUtils.login));
        jsonRequest.add("loginId", loginPhone);
        jsonRequest.add("password", loginPassword);
        executeNetWork(jsonRequest, "请稍后");
        setCallback(new Callback() {
            @Override
            public void handle200Data(JSONObject dataObj, String message) {
                init();//初始化极光推送
                int uuid = dataObj.optInt("uuid");
                String token = dataObj.optString("token");
                Long exTime = dataObj.optLong("expiring_time");

                AppConfig.getInstance().putInt("uuid", uuid);
                AppConfig.getInstance().putString("token", token);
                AppConfig.getInstance().putLong("expiring_time", exTime);
                AppConfig.getInstance().putString("login_phone", loginPhone);

                setResult(RESULT_OK, getIntent());
                MyApplication.isLoad = true;
                MyApplication.isSignOut = false;
                finish();
            }

            @Override
            public void handle404(String message) {
            }

            @Override
            public void handleNoNetWork() {

            }
        });
    }

    /**
     * 监听Back键按下事件,方法2:
     * 注意:
     * 返回值表示:是否能完全处理该事件
     * 在此处返回false,所以会继续传播该事件.
     * 在具体项目中此处的返回值视情况而定.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            MyApplication.isLoad = false;
            setResult(RESULT_CANCELED, getIntent());
            finish();
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    private void setAlias(String alias) {
        //调用JPush API设置Alias
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));
    }

    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            switch (code) {
                case 0://成功
                    Log.d(TAG, "gotResult: setTagSuccess");
                    AppConfig.getInstance().putBoolean("jpush_set_tag_success",true);
                    break;
                case 6002://失败，重试
                    if (ExampleUtil.isConnected(getApplicationContext())) {
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    } else {
                        Toast.makeText(LoginActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
            }
        }
    };
    private final TagAliasCallback mTagsCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            switch (code) {
                case 0:
                    Log.d(TAG, "gotResult: setAliasSuccess");
                    AppConfig.getInstance().putBoolean("jpush_set_alias_success",true);
                    break;
                case 6002:
                    if (ExampleUtil.isConnected(getApplicationContext())) {
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_TAGS, tags), 1000 * 60);
                    } else {
                        Toast.makeText(LoginActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
            }
        }

    };

    private void init() {
        int uuid = AppConfig.getInstance().getInt("uuid", -1000);
        if (uuid != -1000 ) {
            if(AppConfig.getInstance().getBoolean("jpush_set_alias_success",false)){
                setAlias(uuid + "");
            }
            if(AppConfig.getInstance().getBoolean("jpush_set_tag_success",false)){
                setTag(LogUtils.isAppDebug(this));
            }
        }
    }

    //调用JPush API设置Tag
    private void setTag(boolean appDebug) {
        Set<String> tagSet = new LinkedHashSet<>();
        if (appDebug) {
            tagSet.add("debug");
        } else {
            tagSet.add("release");
        }
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_TAGS, tagSet));
    }
}
