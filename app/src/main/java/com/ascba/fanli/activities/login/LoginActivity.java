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

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 登录页面
 */

public class LoginActivity extends BaseActivity {
    private EditText edPhone;
    private EditText edPassword;
    private String loginPhone;
    private String loginPassword;
    private boolean isFirstLogin=true;//是否是第一次成功登陆
    private PhoneHandler phoneHandler;
    private CheckThread checkThread;
    private RequestQueue requestQueue;
    private SharedPreferences sf;

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
        if(loss_password!=null){
            Toast.makeText(this, "密码找回成功", Toast.LENGTH_SHORT).show();
            edPassword.setText("");
            sf.edit().putString("login_password",loss_password).apply();
        }
    }


    private void backFirstPhone() {
        String phone_number = getIntent().getStringExtra("phone_number");
        if(phone_number!=null){
            edPhone.setText(phone_number);
            edPassword.requestFocusFromTouch();
        }

    }

    private void initViews() {
        edPhone= (EditText) findViewById(R.id.login_phone_ed);
        edPassword= (EditText) findViewById(R.id.login_password_ed);
        sf = getSharedPreferences("first_login_success_name_password",MODE_PRIVATE);
        isFirstLogin=sf.getBoolean("first_login_success",true);
        if(!isFirstLogin){
            loginPhone=sf.getString("login_phone","");
            loginPassword=sf.getString("login_password","");
            edPhone.setText(loginPhone);
            edPassword.setText(loginPassword);
            edPassword.requestFocusFromTouch();
        }

    }


    //点击登录按钮
    public void goMain(View view) {
        loginPhone=edPhone.getText().toString();
        loginPassword=edPassword.getText().toString();
        if(loginPhone .equals("") || loginPassword.equals("")){
            Toast.makeText(this, "账号或密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        sendMsgToSevr("http://api.qlqwgw.com/v1/login");
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
        finish();
    }

    private void sendMsgToSevr(String baseUrl) {
        requestQueue= NoHttp.newRequestQueue();
        Request<JSONObject> objRequest = NoHttp.createJsonObjectRequest(baseUrl+"?", RequestMethod.POST);
        objRequest.add("sign", UrlEncodeUtils.createSign(baseUrl));
        objRequest.add("loginId",loginPhone);
        objRequest.add("password",loginPassword);
        phoneHandler=new PhoneHandler();
        phoneHandler.setCallback(new PhoneHandler.Callback() {
            @Override
            public void getMessage(Message msg) {
                JSONObject jObj= (JSONObject) msg.obj;
                LogUtils.PrintLog("123LoginActivity",jObj.toString());
                try {
                    int status = jObj.getInt("status");
                    if(status==200){//服务端返回成功
                        int uuid=jObj.getInt("uuid");
                        String token=jObj.getString("token");
                        if(isFirstLogin){
                            sf.edit()
                                    .putBoolean("first_login_success",false)
                                    .putString("login_phone",loginPhone)
                                    .putString("login_password",loginPassword)
                                    .putInt("uuid",uuid)
                                    .putString("token",token)
                                    .apply();
                        }
                        Intent intent=new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(LoginActivity.this, "未知原因", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        checkThread=new CheckThread(requestQueue,phoneHandler,objRequest);
        checkThread.start();
    }
}
