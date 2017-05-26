package com.ascba.rebate.activities.register;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.activities.login.LoginActivity;
import com.ascba.rebate.utils.UrlEncodeUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.yanzhenjie.nohttp.rest.Request;

import org.json.JSONObject;


/**
 * 短信验证码已经发送
 */

public class RegisterAfterReceiveCodeActivity extends BaseNetActivity implements BaseNetActivity.Callback {

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_after_receive_code);
        initViews();
//        obServeSms();//短信自动获取
        tvPhone = ((TextView) findViewById(R.id.tv_phone_number));
        Intent intent = getIntent();
        if (intent != null) {
            phone_number = intent.getStringExtra("phone_number");
            tvPhone.setText("+86 " + phone_number);
        }
    }

    private void initViews() {
        confirmCode = (EditText) findViewById(R.id.confirm_code);
        confirmPassword = (EditText) findViewById(R.id.confirm_password);
        confirmRePassword = (EditText) findViewById(R.id.confirm_repassword);
        confirmRecommendMan = (EditText) findViewById(R.id.confirm_recommend_man);
    }

    //注册成功 跳转到登录页
    public void goMain2(View view) {
        code = confirmCode.getText().toString();
        password = confirmPassword.getText().toString();
        rePassword = confirmRePassword.getText().toString();
        recommendMan = confirmRecommendMan.getText().toString();
        if ("".equals(code)) {
            getDm().buildAlertDialog("请输入验证码");
            return;
        }
        if ("".equals(password)) {
            getDm().buildAlertDialog("请输入密码");
            return;
        }
        if (password.length() < 6 || password.length() > 16) {
            getDm().buildAlertDialog("密码必须为6位以上16位以下");
            return;
        }
        if ("".equals(rePassword)) {
            getDm().buildAlertDialog("请输入确认密码");
            return;
        }
        if (!password.equals(rePassword)) {
            getDm().buildAlertDialog("两次输入的密码不一致");
            return;
        }
        /*if("".equals(recommendMan)){
            dm.buildAlertDialog("请向推荐人索取推荐码（可选）");
            return;
        }*/
        requestRegister();
    }

    private void requestRegister() {
        Request<JSONObject> request = buildNetRequest(UrlUtils.register, 0, false);
        request.add("sign", UrlEncodeUtils.createSign(UrlUtils.register));
        request.add("mobile", phone_number);
        request.add("captcha", code);
        request.add("password", password);
        request.add("repassword", rePassword);
        request.add("referee", recommendMan);
        executeNetWork(request, "请稍后");
        setCallback(this);
    }

    @Override
    public void handle200Data(JSONObject dataObj, String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(RegisterAfterReceiveCodeActivity.this, LoginActivity.class);
        intent.putExtra("phone_number", phone_number);
        startActivity(intent);
        finish();
    }

    @Override
    public void handle404(String message) {
    }

    @Override
    public void handleNoNetWork() {

    }

}
