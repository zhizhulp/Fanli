package com.ascba.rebate.activities.password_loss;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWorkActivity;
import com.ascba.rebate.activities.login.LoginActivity;
import com.ascba.rebate.utils.UrlEncodeUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.EditTextWithCustomHint;
import com.yolanda.nohttp.rest.Request;
import org.json.JSONObject;


public class PasswordLossWithCodeActivity extends BaseNetWorkActivity implements BaseNetWorkActivity.Callback {

    private TextView tvLossPhone;
    private EditTextWithCustomHint edCode;
    private EditTextWithCustomHint edPassword;
    private EditTextWithCustomHint edRepassword;
    private String loss_phone;
    /*@SuppressLint("HandlerLeak")
    Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            if (msg.what == 1)
            {
                edCode.setText(msg.obj.toString());
            }
        }
    };*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_loss_with_code);
        initViews();
        getPhoneFromBefore();//获取上个界面传来的手机号码
    }
    /*private void obServeSms() {
        SMSContentObserver smsContentObserver = new SMSContentObserver(this, handler);

        getContentResolver().registerContentObserver(
                Uri.parse("content://sms/"), true, smsContentObserver);
    }*/

    private void getPhoneFromBefore() {
        Intent intent = getIntent();
        loss_phone = intent.getStringExtra("loss_phone");
        String sms_code = intent.getStringExtra("sms_code");
        if(loss_phone!=null){
            tvLossPhone.setText(loss_phone);
            edCode.setText(sms_code);
        }
    }

    private void initViews() {
        tvLossPhone = ((TextView) findViewById(R.id.tv_phone_come));
        edCode = ((EditTextWithCustomHint) findViewById(R.id.loss_code));
        edPassword = ((EditTextWithCustomHint) findViewById(R.id.loss_password));
        edRepassword = ((EditTextWithCustomHint) findViewById(R.id.loss_repassword));
    }

    public void goMain3(View view) {
        requestPasswordLoss(UrlUtils.getBackPwd);
    }

    private void requestPasswordLoss(String url) {
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
        Request<JSONObject> request = buildNetRequest(url, 0, false);
        request.add("sign", UrlEncodeUtils.createSign(url));
        request.add("mobile",loss_phone);
        request.add("captcha",code);
        request.add("password",password);
        request.add("repassword",repassword);
        executeNetWork(request,"请稍后");
        setCallback(this);
    }

    @Override
    public void handle200Data(JSONObject dataObj, String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();
    }

}
