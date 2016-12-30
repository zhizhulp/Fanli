package com.ascba.rebate.activities.password_loss;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWorkActivity;
import com.ascba.rebate.handlers.CheckThread;
import com.ascba.rebate.handlers.DialogManager;
import com.ascba.rebate.handlers.PhoneHandler;
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

public class PasswordLossActivity extends BaseNetWorkActivity implements BaseNetWorkActivity.Callback {
    private EditText edLossNumber;
    private String phone;
    private DialogManager dm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_loss);
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
    public void handle200Data(JSONObject dataObj, String message) throws JSONException {
        String sms_code = dataObj.getString("sms_code");
        Intent intent=new Intent(PasswordLossActivity.this, PasswordLossWithCodeActivity.class);
        intent.putExtra("loss_phone",phone);
        intent.putExtra("sms_code",sms_code);
        startActivity(intent);
        finish();
    }
    /*private void sendMsgToSevr(String baseUrl) {
        boolean netAva = NetUtils.isNetworkAvailable(this);
        if(!netAva){
            dm.buildAlertDialog("请打开网络");
            return;
        }

        requestQueue= NoHttp.newRequestQueue();
        final ProgressDialog dialog = new ProgressDialog(this,R.style.dialog);
        dialog.setMessage("正在发送验证码");
        Request<JSONObject> objRequest = NoHttp.createJsonObjectRequest(baseUrl+"?", RequestMethod.POST);
        objRequest.add("sign", UrlEncodeUtils.createSign(baseUrl));
        objRequest.add("mobile",phone);
        objRequest.add("type",1);

        phoneHandler=new PhoneHandler(this);
        phoneHandler.setCallback(new PhoneHandler.Callback() {
            @Override
            public void getMessage(Message msg) {
                dialog.dismiss();
                JSONObject jObj= (JSONObject) msg.obj;
                LogUtils.PrintLog("123",jObj.toString());
                try {
                    int status = jObj.getInt("status");
                    String message = jObj.getString("msg");
                    if(status==200){//短信验证码发送成功
                        JSONObject dataObj = jObj.getJSONObject("data");
                        String sms_code = dataObj.getString("sms_code");
                        Intent intent=new Intent(PasswordLossActivity.this, PasswordLossWithCodeActivity.class);
                        intent.putExtra("loss_phone",phone);
                        intent.putExtra("sms_code",sms_code);
                        startActivity(intent);
                        finish();
                    } else if(status==-1){//用户不存在
                        dm.buildAlertDialog(message);
                    } else if(status==1){//缺少sign参数
                        dm.buildAlertDialog(message);
                    } else if(status==2){//非法请求，sign验证失败
                        dm.buildAlertDialog(message);
                        Toast.makeText(PasswordLossActivity.this, message, Toast.LENGTH_SHORT).show();
                    } else if(status==3){//跳转登录
                        Intent intent=new Intent(PasswordLossActivity.this, LoginActivity.class);
                        intent.putExtra("uuid",-1000);
                        startActivity(intent);
                        finish();
                    } else if(status==4){//登陆后缺少uuid/token/expiring_time参数
                        dm.buildAlertDialog(message);
                    } else if(status==5){//token验证失败
                        dm.buildAlertDialog(message);
                    } else if(status==6){//用户已存在
                        dm.buildAlertDialog(message);
                    } else if(status==404){//失败
                        dm.buildAlertDialog(message);
                    } else if(status==500){//数据异常，内部错误
                        dm.buildAlertDialog(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        checkThread=new CheckThread(requestQueue,phoneHandler,objRequest);
        checkThread.start();
        dialog.show();
    }*/
}
