package com.ascba.rebate.activities.password_loss;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.handlers.DialogManager;
import com.ascba.rebate.utils.UrlEncodeUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.yanzhenjie.nohttp.rest.Request;

import org.json.JSONObject;

public class PasswordLossActivity extends BaseNetActivity implements BaseNetActivity.Callback {
    private static final int REQUEST_PASSWORD = 0;
    private EditText edLossNumber;
    private String phone;
    private DialogManager dm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_loss);
        //StatusBarUtil.setColor(this, 0xffe52020);
        initViews();
    }

    private void initViews() {
        dm=new DialogManager(this);
        edLossNumber = ((EditText) findViewById(R.id.loss_phone_number_ed));
    }

    //找回密码的下一个界面
    public void goCodePassword(View view) {
        phone = edLossNumber.getText().toString();
        if(phone.equals("")){
            dm.buildAlertDialog("请输入您的手机号码");
            return;
        }
        sendMsgToSevr(UrlUtils.sendMsg);
    }

    private void sendMsgToSevr(String baseUrl) {
        Request<JSONObject> request = buildNetRequest(baseUrl, 0, false);
        request.add("sign", UrlEncodeUtils.createSign(baseUrl));
        request.add("mobile",phone);
        request.add("type",1);
        executeNetWork(request,"请稍后");
        setCallback(this);
    }

    @Override
    public void handle200Data(JSONObject dataObj, String message) {
        /*String sms_code = dataObj.getString("sms_code");*/
        Intent intent=new Intent(PasswordLossActivity.this, PasswordLossWithCodeActivity.class);
        intent.putExtra("loss_phone",phone);
        /*intent.putExtra("sms_code",sms_code);*/
        startActivityForResult(intent,REQUEST_PASSWORD);
        finish();
    }

    @Override
    public void handle404(String message) {

    }

    @Override
    public void handleNoNetWork() {

    }

    public void goBack(View view) {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data==null){
            return;
        }
        switch (requestCode){
            case REQUEST_PASSWORD:
                if(resultCode==RESULT_OK){
                    finish();
                }
                break;
        }
    }
}
