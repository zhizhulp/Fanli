package com.ascba.rebate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.utils.UrlEncodeUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.yanzhenjie.nohttp.rest.Request;

import org.json.JSONObject;

public class FindPayPasswordActivity extends BaseNetActivity {

    private TextView tvPhone;
    private EditText etCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pay_password);

        initViews();
    }

    private void initViews() {
        tvPhone = ((TextView) findViewById(R.id.tv_phone));
        tvPhone.setText(AppConfig.getInstance().getString("login_phone",null));

        etCode = ((EditText) findViewById(R.id.ed_bank_card_phone));


    }
    //发送验证码
    public void sendMsg(View view) {
        requestNetwork(UrlUtils.sendMsg,0);
    }

    private void requestNetwork(String url, int what) {
        Request<JSONObject> request = null;
        if(what==0){
            request = buildNetRequest(url, 0, false);
            request.add("sign", UrlEncodeUtils.createSign(url));
            request.add("mobile",tvPhone.getText().toString());
            request.add("type",3);
        }else if(what==1){
            request = buildNetRequest(url, 0, true);
            request.add("mobile",etCode.getText().toString());
        }

        executeNetWork(what,request,"请稍后");
    }

    @Override
    protected void mhandle200Data(int what, JSONObject object, JSONObject dataObj, String message) {
        super.mhandle200Data(what, object, dataObj, message);
        if(what==0){
            getDm().buildAlertDialog(message);
        }else if(what==1){
            setResult(RESULT_OK,getIntent());
            finish();
        }
    }

    @Override
    protected void mhandle404(int what, JSONObject object, String message) {
        super.mhandle404(what, object, message);
        getDm().buildAlertDialog(message);
    }

    @Override
    protected void mhandleFailed(int what, Exception e) {
        super.mhandleFailed(what, e);
        getDm().buildAlertDialog(getString(R.string.no_response));
    }
    //提交
    public void goCodePassword(View view) {
        if(tvPhone.getText().toString().equals("")){
            getDm().buildAlertDialog("手机号码不能为空");
            return;
        }
        if(etCode.getText().toString().equals("")){
            getDm().buildAlertDialog("请填写验证码");
            return;
        }
        requestNetwork(UrlUtils.getBackPayPwd,1);
    }
}
