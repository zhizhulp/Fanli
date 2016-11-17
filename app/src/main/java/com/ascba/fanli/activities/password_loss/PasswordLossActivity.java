package com.ascba.fanli.activities.password_loss;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ascba.fanli.R;
import com.ascba.fanli.activities.base.BaseActivity;
import com.ascba.fanli.activities.login.LoginActivity;
import com.ascba.fanli.activities.main.MainActivity;
import com.ascba.fanli.handlers.CheckThread;
import com.ascba.fanli.handlers.PhoneHandler;
import com.ascba.fanli.utils.LogUtils;
import com.ascba.fanli.utils.UrlEncodeUtils;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;

import org.json.JSONException;
import org.json.JSONObject;

public class PasswordLossActivity extends BaseActivity {
    private PhoneHandler phoneHandler;
    private CheckThread checkThread;
    private RequestQueue requestQueue;
    private EditText edLossNumber;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_loss);
        initViews();
    }

    private void initViews() {
        edLossNumber = ((EditText) findViewById(R.id.loss_phone_number_ed));
    }

    //找回密码的下一个界面
    public void goCodePassword(View view) {
        phone = edLossNumber.getText().toString();
        sendMsgToSevr("http://api.qlqwgw.com/v1/sendMsg");
    }

    private void sendMsgToSevr(String baseUrl) {
        requestQueue= NoHttp.newRequestQueue();
        Request<JSONObject> objRequest = NoHttp.createJsonObjectRequest(baseUrl+"?", RequestMethod.POST);
        objRequest.add("sign", UrlEncodeUtils.createSign(baseUrl));
        objRequest.add("mobile",phone);

        phoneHandler=new PhoneHandler();
        phoneHandler.setCallback(new PhoneHandler.Callback() {
            @Override
            public void getMessage(Message msg) {
                JSONObject jObj= (JSONObject) msg.obj;
                LogUtils.PrintLog("123",jObj.toString());
                try {
                    int status = jObj.getInt("status");
                    if(status==200){
                        Intent intent=new Intent(PasswordLossActivity.this, PasswordLossWithCodeActivity.class);
                        intent.putExtra("loss_phone",phone);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(PasswordLossActivity.this, "未知原因", Toast.LENGTH_SHORT).show();
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
