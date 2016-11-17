package com.ascba.fanli.activities.register;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ascba.fanli.R;
import com.ascba.fanli.activities.base.BaseActivity;
import com.ascba.fanli.activities.login.LoginActivity;
import com.ascba.fanli.handlers.CheckThread;
import com.ascba.fanli.handlers.PhoneHandler;
import com.ascba.fanli.utils.LogUtils;
import com.ascba.fanli.utils.UrlEncodeUtils;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

/**
 * 短信验证码已经发送
 */

public class RegisterAfterReceiveCodeActivity extends BaseActivity {

    private TextView tvPhone;
    private EditText confirmCode;//验证码
    private EditText confirmPassword;//密码
    private EditText confirmRePassword;//重复密码
    private EditText confirmRecommendMan;//推荐人
    private String phone_number;
    private String code;
    private String password;
    private String rePassword;
    private String recommendMan;
    private RequestQueue requestQueue;
    private PhoneHandler phoneHandler;
    private CheckThread checkThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_after_receive_code);
        initViews();
        tvPhone = ((TextView) findViewById(R.id.tv_phone_number));
        phone_number = getIntent().getStringExtra("phone_number");
        tvPhone.setText("+86 "+phone_number);
    }

    private void initViews() {
        confirmCode= (EditText) findViewById(R.id.confirm_code);
        confirmPassword= (EditText) findViewById(R.id.confirm_password);
        confirmRePassword= (EditText) findViewById(R.id.confirm_repassword);
        confirmRecommendMan= (EditText) findViewById(R.id.confirm_recommend_man);
    }

    //注册成功 跳转到登录页
    public void goMain2(View view) {
        code=confirmCode.getText().toString();
        password = confirmPassword.getText().toString();
        rePassword = confirmRePassword.getText().toString();
        recommendMan=confirmRecommendMan.getText().toString();
        if("".equals(code)){
            Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show();
            return;
        }
        if("".equals(password)){
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }
        if(password.length()< 6||password.length()>16){
            Toast.makeText(this, "密码必须为6位以上16位以下", Toast.LENGTH_SHORT).show();
            return;
        }
        if("".equals(rePassword)){
            Toast.makeText(this, "请输入确认密码", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!password.equals(rePassword)){
            Toast.makeText(this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
            return;
        }

        sendMsgToSevr("http://api.qlqwgw.com/v1/register");
    }

    private void sendMsgToSevr(String baseUrl) {
        requestQueue= NoHttp.newRequestQueue();
        Request<JSONObject> objRequest = NoHttp.createJsonObjectRequest(baseUrl+"?", RequestMethod.POST);
        objRequest.add("sign",UrlEncodeUtils.createSign(baseUrl));
        objRequest.add("mobile",phone_number);
        objRequest.add("captcha",code);
        objRequest.add("password",password);
        objRequest.add("repassword",rePassword);
        objRequest.add("referee",recommendMan);//可选
        phoneHandler=new PhoneHandler();
        phoneHandler.setCallback(new PhoneHandler.Callback() {
            @Override
            public void getMessage(Message msg) {
                JSONObject jObj= (JSONObject) msg.obj;
                LogUtils.PrintLog("123",jObj.toString());
                try {
                    int status = jObj.getInt("status");
                    LogUtils.PrintLog("123","状态码为："+status);
                    if(status==200){//服务端返回成功
                        Intent intent=new Intent(RegisterAfterReceiveCodeActivity.this, LoginActivity.class);
                        intent.putExtra("phone_number",phone_number);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(RegisterAfterReceiveCodeActivity.this, "注册失败，请重新发送验证码", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(RegisterAfterReceiveCodeActivity.this, RegisterInputNumberActivity.class);
                        startActivity(intent);
                        finish();
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
