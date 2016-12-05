package com.ascba.rebate.activities.password_loss;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseActivity;
import com.ascba.rebate.activities.login.LoginActivity;
import com.ascba.rebate.handlers.CheckThread;
import com.ascba.rebate.handlers.PhoneHandler;
import com.ascba.rebate.utils.LogUtils;
import com.ascba.rebate.utils.UrlEncodeUtils;
import com.ascba.rebate.view.EditTextWithCustomHint;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;

import org.json.JSONException;
import org.json.JSONObject;

public class PasswordLossWithCodeActivity extends BaseActivity {

    private TextView tvLossPhone;
    private PhoneHandler phoneHandler;
    private CheckThread checkThread;
    private RequestQueue requestQueue;
    private EditTextWithCustomHint edCode;
    private EditTextWithCustomHint edPassword;
    private EditTextWithCustomHint edRepassword;
    private String loss_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_loss_with_code);
        initViews();
        getPhoneFromBefore();
    }

    private void getPhoneFromBefore() {
        loss_phone = getIntent().getStringExtra("loss_phone");
        if(loss_phone!=null){
            tvLossPhone.setText(loss_phone);
        }
    }

    private void initViews() {
        tvLossPhone = ((TextView) findViewById(R.id.tv_phone_come));
        edCode = ((EditTextWithCustomHint) findViewById(R.id.loss_code));
        edPassword = ((EditTextWithCustomHint) findViewById(R.id.loss_password));
        edRepassword = ((EditTextWithCustomHint) findViewById(R.id.loss_repassword));
    }

    public void goMain3(View view) {

        sendMsgToSevr("http://api.qlqwgw.com/v1/getBackPwd");

    }

    private void sendMsgToSevr(String baseUrl) {
        String code = edCode.getText().toString();
        final String password = edPassword.getText().toString();
        String repassword = edRepassword.getText().toString();
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
        if("".equals(repassword)){
            Toast.makeText(this, "请再次输入密码", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!password.equals(repassword)){
            Toast.makeText(this, "两次输入密码不匹配", Toast.LENGTH_SHORT).show();
            return;
        }
        requestQueue= NoHttp.newRequestQueue();
        final ProgressDialog dialog = new ProgressDialog(this,R.style.dialog);
        dialog.setMessage("密码找回中");
        Request<JSONObject> objRequest = NoHttp.createJsonObjectRequest(baseUrl+"?", RequestMethod.POST);
        objRequest.add("sign", UrlEncodeUtils.createSign(baseUrl));
        objRequest.add("mobile",loss_phone);
        objRequest.add("captcha",code);
        objRequest.add("password",password);
        objRequest.add("repassword",repassword);
        phoneHandler=new PhoneHandler(this);
        phoneHandler.setCallback(new PhoneHandler.Callback() {
            @Override
            public void getMessage(Message msg) {
                dialog.dismiss();
                JSONObject jObj= (JSONObject) msg.obj;
                LogUtils.PrintLog("123",jObj.toString());
                try {
                    int status = jObj.getInt("status");
                    if(status==200){//服务端返回成功
                        Intent intent=new Intent(PasswordLossWithCodeActivity.this, LoginActivity.class);
                        intent.putExtra("loss_password",password);
                        startActivity(intent);
                        finish();
                    }else if(status==-1){
                        Toast.makeText(PasswordLossWithCodeActivity.this, jObj.getString("msg"), Toast.LENGTH_SHORT).show();
                    }else if(status==404){
                        Toast.makeText(PasswordLossWithCodeActivity.this, jObj.getString("msg"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        checkThread=new CheckThread(requestQueue,phoneHandler,objRequest);
        checkThread.start();
        dialog.show();
    }
}
