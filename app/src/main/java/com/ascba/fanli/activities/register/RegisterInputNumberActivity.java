package com.ascba.fanli.activities.register;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ascba.fanli.R;
import com.ascba.fanli.activities.base.BaseActivity;
import com.ascba.fanli.handlers.CheckThread;
import com.ascba.fanli.handlers.PhoneHandler;
import com.ascba.fanli.utils.LogUtils;
import com.ascba.fanli.utils.RegexUtils;
import com.ascba.fanli.utils.UrlEncodeUtils;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

/**
 * 注册-输入手机号
 */

public class RegisterInputNumberActivity extends BaseActivity {

    private EditText phoneNumber;
    private String phone;
    private PhoneHandler phoneHandler;
    private CheckThread checkThread;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_input_number);
        initViews();
    }

    private void initViews() {

    }

    //进入注册的下一个界面
    public void goRegisterCode(View view) {
        //获取手机号码
        phoneNumber = ((EditText) findViewById(R.id.ed_input_number));
        phone=phoneNumber.getText().toString();
        if(phone.equals("")){
            Toast.makeText(this, "请输入手机号码", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!RegexUtils.isMobileExact(phone)){
            Toast.makeText(this, "请输入正确的11位手机号码", Toast.LENGTH_SHORT).show();
            return;
        }

        sendMsgToSevr("http://api.qlqwgw.com/v1/sendMsg");

    }

    private void sendMsgToSevr(String baseUrl) {
        requestQueue= NoHttp.newRequestQueue();
        Request<JSONObject> objRequest = NoHttp.createJsonObjectRequest(baseUrl+"?", RequestMethod.POST);
        objRequest.add("sign",UrlEncodeUtils.createSign(baseUrl));
        objRequest.add("mobile",phone);
        LogUtils.PrintLog("123",baseUrl+"?"+"sign="+UrlEncodeUtils.createSign(baseUrl)+"&mobile="+phone);
        phoneHandler=new PhoneHandler();
        phoneHandler.setCallback(new PhoneHandler.Callback() {
            @Override
            public void getMessage(Message msg) {
                JSONObject jObj= (JSONObject) msg.obj;
                LogUtils.PrintLog("123RegisterInputNumberActivity",jObj.toString());
                try {
                    int status = jObj.getInt("status");
                    if(status==200){//服务端返回成功
                        Intent intent=new Intent(RegisterInputNumberActivity.this, RegisterAfterReceiveCodeActivity.class);
                        intent.putExtra("phone_number",phone);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(RegisterInputNumberActivity.this, "未知原因", Toast.LENGTH_SHORT).show();
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
