package com.ascba.rebate.activities.register;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseActivity;
import com.ascba.rebate.activities.login.LoginActivity;
import com.ascba.rebate.activities.password_loss.PasswordLossActivity;
import com.ascba.rebate.handlers.CheckThread;
import com.ascba.rebate.handlers.PhoneHandler;
import com.ascba.rebate.handlers.sms.SMSContentObserver;
import com.ascba.rebate.utils.LogUtils;
import com.ascba.rebate.utils.NetUtils;
import com.ascba.rebate.utils.UrlEncodeUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;

import org.json.JSONException;
import org.json.JSONObject;

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
   /* @SuppressLint("HandlerLeak")
    Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            if (msg.what == 1)
            {
                confirmCode.setText(msg.obj.toString());
            }
        }
    };*/



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_after_receive_code);
        initViews();
//        obServeSms();//短信自动获取
        tvPhone = ((TextView) findViewById(R.id.tv_phone_number));
        Intent intent = getIntent();
        if(intent!=null){
            phone_number =intent.getStringExtra("phone_number");
            String sms_code = intent.getStringExtra("sms_code");
            tvPhone.setText("+86 "+phone_number);
            confirmCode.setText(sms_code);
        }

    }

    /*private void obServeSms() {
        SMSContentObserver smsContentObserver = new SMSContentObserver(this, handler);

        getContentResolver().registerContentObserver(
                Uri.parse("content://sms/"), true, smsContentObserver);
    }*/

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

        sendMsgToSevr(UrlUtils.register);
    }

    private void sendMsgToSevr(String baseUrl) {
        boolean netAva = NetUtils.isNetworkAvailable(this);
        if(!netAva){
            Toast.makeText(this, "请打开网络", Toast.LENGTH_SHORT).show();
            return;
        }
        requestQueue= NoHttp.newRequestQueue();
        final ProgressDialog dialog4 = new ProgressDialog(this,R.style.dialog);
        dialog4.setMessage("注册中，请稍后");
        Request<JSONObject> objRequest = NoHttp.createJsonObjectRequest(baseUrl+"?", RequestMethod.POST);
        objRequest.add("sign",UrlEncodeUtils.createSign(baseUrl));
        objRequest.add("mobile",phone_number);
        objRequest.add("captcha",code);
        objRequest.add("password",password);
        objRequest.add("repassword",rePassword);
        objRequest.add("referee",recommendMan);//可选
        phoneHandler=new PhoneHandler(this);
        phoneHandler.setCallback(new PhoneHandler.Callback() {
            @Override
            public void getMessage(Message msg) {
                dialog4.dismiss();
                JSONObject jObj= (JSONObject) msg.obj;
                LogUtils.PrintLog("123RegisterAfterReceiveCodeActivity",jObj.toString());
                try {
                    int status = jObj.getInt("status");
                    String message = jObj.getString("msg");
                    if(status==200){//服务端返回成功
                        Intent intent=new Intent(RegisterAfterReceiveCodeActivity.this, LoginActivity.class);
                        intent.putExtra("phone_number",phone_number);
                        startActivity(intent);
                        finish();
                    } else if(status==-1){//用户不存在
                        Toast.makeText(RegisterAfterReceiveCodeActivity.this, message, Toast.LENGTH_SHORT).show();
                    } else if(status==1){//缺少sign参数
                        Toast.makeText(RegisterAfterReceiveCodeActivity.this, message, Toast.LENGTH_SHORT).show();
                    } else if(status==2){//非法请求，sign验证失败
                        Toast.makeText(RegisterAfterReceiveCodeActivity.this, message, Toast.LENGTH_SHORT).show();
                    } else if(status==3){//跳转登录
                        Intent intent=new Intent(RegisterAfterReceiveCodeActivity.this, LoginActivity.class);
                        intent.putExtra("uuid",-1000);
                        startActivity(intent);
                        finish();
                    } else if(status==4){//登陆后缺少uuid/token/expiring_time参数
                        Toast.makeText(RegisterAfterReceiveCodeActivity.this, message, Toast.LENGTH_SHORT).show();
                    } else if(status==5){//token验证失败
                        Toast.makeText(RegisterAfterReceiveCodeActivity.this, message, Toast.LENGTH_SHORT).show();
                    } else if(status==6){//用户已存在
                        Toast.makeText(RegisterAfterReceiveCodeActivity.this, message, Toast.LENGTH_SHORT).show();
                    } else if(status==404){//失败
                        Toast.makeText(RegisterAfterReceiveCodeActivity.this, message, Toast.LENGTH_SHORT).show();
                    } else if(status==500){//数据异常，内部错误
                        Toast.makeText(RegisterAfterReceiveCodeActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
        checkThread=new CheckThread(requestQueue,phoneHandler,objRequest);
        checkThread.start();
        dialog4.show();
    }
}
