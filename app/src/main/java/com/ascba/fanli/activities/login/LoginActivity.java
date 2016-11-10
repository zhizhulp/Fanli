package com.ascba.fanli.activities.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ascba.fanli.R;
import com.ascba.fanli.activities.base.BaseActivity;
import com.ascba.fanli.activities.main.MainActivity;
import com.ascba.fanli.activities.password_loss.PasswordLossActivity;
import com.ascba.fanli.activities.register.RegisterAfterReceiveCodeActivity;
import com.ascba.fanli.activities.register.RegisterInputNumberActivity;
import com.ascba.fanli.handlers.CheckThread;
import com.ascba.fanli.handlers.PhoneHandler;
import com.ascba.fanli.utils.LogUtils;
import com.ascba.fanli.utils.SharedPreferencesUtil;
import com.ascba.fanli.utils.UrlEncodeUtils;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;

import org.json.JSONObject;

/**
 * 登录页面
 */

public class LoginActivity extends BaseActivity {
    private EditText edPhone;
    private EditText edPassword;
    private String loginPhone;
    private String loginPassword;
    private boolean isBack;//判断是否是注册页传回的
    private boolean isFirstLogin;//是否是第一次成功登陆
    private PhoneHandler phoneHandler;
    private CheckThread checkThread;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        initViews();
        notFirstLogin();
        //backFirstPhone();//传回注册成功的手机账号
    }
    //以后登陆之前直接写到ed
    private void notFirstLogin() {
        SharedPreferences sf = getSharedPreferences("first_login_success_name_password", MODE_PRIVATE);
        isFirstLogin=sf.getBoolean("first_login_success",true);
        LogUtils.PrintLog("123",""+isFirstLogin);
        if(!isFirstLogin){
            loginPhone=sf.getString("login_phone","");
            loginPassword=sf.getString("login_password","");
            edPhone.setText(loginPhone);
            edPassword.setText(loginPassword);
            edPassword.requestFocusFromTouch();
        }

    }

    private void backFirstPhone() {
        isBack=getSharedPreferences("number_back",MODE_PRIVATE).getBoolean("number_back",false);
        String phone_number = getIntent().getStringExtra("phone_number");
        if(phone_number!=null){
            getSharedPreferences("number_back",MODE_PRIVATE).edit().putBoolean("number_back",true).apply();
            edPhone.setText(phone_number);
            edPhone.setFocusable(false);
            edPassword.setFocusable(true);
        }

    }

    private void initViews() {
        edPhone= (EditText) findViewById(R.id.login_phone_ed);
        edPassword= (EditText) findViewById(R.id.login_password_ed);

    }


    //点击登录按钮，验证账号，密码，验证成功进入主页。
    public void goMain(View view) {
        loginPhone=edPhone.getText().toString();
        loginPassword=edPassword.getText().toString();

        if(loginPhone .equals("") || loginPassword.equals("")){
            Toast.makeText(this, "账号或密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        sendMsgToSevr("http://api.qlqwgw.com/v1/register");
    }


    //进入密码找回页面
    public void goForgetPassword(View view) {
        Intent intent=new Intent(this, PasswordLossActivity.class);
        startActivity(intent);
    }

    //进入注册页面
    public void goRegister(View view) {
        Intent intent=new Intent(this, RegisterInputNumberActivity.class);
        startActivity(intent);
    }

    private void sendMsgToSevr(String baseUrl) {
        requestQueue= NoHttp.newRequestQueue();
        Request<JSONObject> objRequest = NoHttp.createJsonObjectRequest(baseUrl+"?", RequestMethod.POST);
        objRequest.add("sign", UrlEncodeUtils.createSign(baseUrl));
        objRequest.add("mobile","123");
        objRequest.add("captcha","李平");
        objRequest.add("password","1");
        objRequest.add("repassword","1");
        objRequest.add("referee","123");
        phoneHandler=new PhoneHandler();
        phoneHandler.setCallback(new PhoneHandler.Callback() {
            @Override
            public void getMessage(Message msg) {
                JSONObject jObj= (JSONObject) msg.obj;
                LogUtils.PrintLog("123",jObj.toString());
                if(true){//服务端返回成功
                    if(isFirstLogin){
                        LogUtils.PrintLog("123","数据写入之前");
                        SharedPreferences sf = getSharedPreferences("first_login_success_name_password", MODE_PRIVATE);
                        sf.edit().putBoolean("first_login_success",false).putString("login_phone",loginPhone).putString("login_password",loginPassword).apply();
                    }
                    Intent intent=new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(LoginActivity.this, "未知原因", Toast.LENGTH_SHORT).show();
                }
            }
        });
        checkThread=new CheckThread(requestQueue,phoneHandler,objRequest);
        checkThread.start();
    }
}
