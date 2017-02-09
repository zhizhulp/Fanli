package com.ascba.rebate.activities.register;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWorkActivity;
import com.ascba.rebate.handlers.DialogManager;
import com.ascba.rebate.utils.RegexUtils;
import com.ascba.rebate.utils.UrlEncodeUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.yolanda.nohttp.rest.Request;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 注册-输入手机号
 */

public class RegisterInputNumberActivity extends BaseNetWorkActivity implements BaseNetWorkActivity.Callback {

    private EditText phoneNumber;
    private String phone;
    private DialogManager dm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_input_number);
//        StatusBarUtil.setColor(this, 0xffe52020);
        initViews();
    }

    private void initViews() {
        dm=new DialogManager(this);
    }

    //进入注册的下一个界面
    public void goRegisterCode(View view) {
        //获取手机号码
        phoneNumber = ((EditText) findViewById(R.id.ed_input_number));
        phone=phoneNumber.getText().toString();
        if(phone.equals("")){
            dm.buildAlertDialog("请输入手机号码");
            return;
        }
        if(!RegexUtils.isMobileExact(phone)){
            dm.buildAlertDialog("请输入正确的11位手机号码");
            return;
        }

        sendMsgToSevr(UrlUtils.sendMsg);

    }

    private void sendMsgToSevr(String baseUrl) {
        Request<JSONObject> request = buildNetRequest(baseUrl, 0, false);
        request.add("sign",UrlEncodeUtils.createSign(baseUrl));
        request.add("mobile",phone);
        executeNetWork(request,"请稍后");
        setCallback(this);
    }

    //服务协议
    public void goServerProtocol(View view) {
        Intent intent=new Intent(this,RegisterProtocolActivity.class);
        startActivity(intent);
    }

    @Override
    public void handle200Data(JSONObject dataObj, String message) throws JSONException {
        /*String sms_code = dataObj.getString("sms_code");*/
        Intent intent=new Intent(RegisterInputNumberActivity.this, RegisterAfterReceiveCodeActivity.class);
        intent.putExtra("phone_number",phone);
        /*intent.putExtra("sms_code",sms_code);*/
        startActivity(intent);
        finish();
    }

    public void goBack(View view) {
        finish();
    }
}
