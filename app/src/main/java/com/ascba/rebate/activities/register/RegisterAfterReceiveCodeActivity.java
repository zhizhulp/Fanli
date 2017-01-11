package com.ascba.rebate.activities.register;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWorkActivity;
import com.ascba.rebate.activities.login.LoginActivity;
import com.ascba.rebate.utils.UrlEncodeUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.yolanda.nohttp.rest.Request;

import org.json.JSONObject;


/**
 * 短信验证码已经发送
 */

public class RegisterAfterReceiveCodeActivity extends BaseNetWorkActivity implements BaseNetWorkActivity.Callback {

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
            confirmCode.setSelection(sms_code.length());
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
        requestRegister();
    }

    private void requestRegister() {
        Request<JSONObject> request = buildNetRequest(UrlUtils.register, 0, false);
        request.add("sign",UrlEncodeUtils.createSign(UrlUtils.register));
        request.add("mobile",phone_number);
        request.add("captcha",code);
        request.add("password",password);
        request.add("repassword",rePassword);
        request.add("referee",recommendMan);
        executeNetWork(request,"请稍后");
        setCallback(this);
    }

    @Override
    public void handle200Data(JSONObject dataObj, String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(RegisterAfterReceiveCodeActivity.this, LoginActivity.class);
        intent.putExtra("phone_number",phone_number);
        startActivity(intent);
        finish();
    }

}
