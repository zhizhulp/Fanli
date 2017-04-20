package com.ascba.rebate.activities.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWorkActivity;
import com.ascba.rebate.activities.password_loss.PasswordLossWorkActivity;
import com.ascba.rebate.activities.register.RegisterInputNumberWorkActivity;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.application.MyApplication;
import com.ascba.rebate.handlers.DialogManager;
import com.ascba.rebate.utils.UrlEncodeUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.yanzhenjie.nohttp.rest.Request;

import org.json.JSONObject;

/**
 * 登录页面
 */

public class LoginWorkActivity extends BaseNetWorkActivity {
    private EditText edPhone;
    private EditText edPassword;
    private String loginPhone;
    private String loginPassword;
    private DialogManager dm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
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
        dm = new DialogManager(this);
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
        Intent intent = new Intent(this, PasswordLossWorkActivity.class);
        startActivity(intent);
    }

    //进入注册页面
    public void goRegister(View view) {
        Intent intent = new Intent(this, RegisterInputNumberWorkActivity.class);
        startActivity(intent);
    }

    private void loginRequest() {
        loginPhone = edPhone.getText().toString();
        loginPassword = edPassword.getText().toString();
        if (loginPhone.equals("") || loginPassword.equals("")) {
            Toast.makeText(this, "账号或密码不能为空", Toast.LENGTH_SHORT).show();
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

                int uuid = dataObj.optInt("uuid");
                String token = dataObj.optString("token");
                Long exTime = dataObj.optLong("expiring_time");

                AppConfig.getInstance().putInt("uuid", uuid);
                AppConfig.getInstance().putString("token", token);
                AppConfig.getInstance().putLong("expiring_time", exTime);
                AppConfig.getInstance().putString("login_phone", loginPhone);

                setResult(RESULT_OK, getIntent());
                MyApplication.isLoad=true;

                MyApplication.isPersonalData = true;
                finish();
            }

            @Override
            public void handle404(String message) {
                dm.buildAlertDialog(message);
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
            MyApplication.isLoad=false;
            setResult(RESULT_CANCELED, getIntent());
            finish();
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
