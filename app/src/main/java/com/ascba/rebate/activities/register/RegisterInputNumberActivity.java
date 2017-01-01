package com.ascba.rebate.activities.register;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.RegisterProtocolActivity;
import com.ascba.rebate.activities.base.BaseNetWorkActivity;
import com.ascba.rebate.handlers.DialogManager;
import com.ascba.rebate.utils.RegexUtils;
import com.ascba.rebate.utils.UrlEncodeUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.jaeger.library.StatusBarUtil;
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
    /*private void sendMsgToSevr(String baseUrl) {
        boolean netAva = NetUtils.isNetworkAvailable(this);
        if(!netAva){
            dm.buildAlertDialog("请打开网络");
            return;
        }
        requestQueue= NoHttp.newRequestQueue();
        final ProgressDialog dialog4 = new ProgressDialog(this,R.style.dialog);
        dialog4.setMessage("正在发送验证码");
        dialog4.setCanceledOnTouchOutside(false);//点击外部不消失
        Request<JSONObject> objRequest = NoHttp.createJsonObjectRequest(baseUrl+"?", RequestMethod.POST);
        objRequest.add("sign",UrlEncodeUtils.createSign(baseUrl));
        objRequest.add("mobile",phone);
        phoneHandler=new PhoneHandler(this);
        phoneHandler.setCallback(new PhoneHandler.Callback() {
            @Override
            public void getMessage(Message msg) {
                dialog4.dismiss();
                JSONObject jObj= (JSONObject) msg.obj;
                try {
                    int status = jObj.getInt("status");
                    String message = jObj.getString("msg");
                    if(status==200){//服务端返回成功
                        JSONObject dataObj = jObj.getJSONObject("data");
                        String sms_code = dataObj.getString("sms_code");
                        Intent intent=new Intent(RegisterInputNumberActivity.this, RegisterAfterReceiveCodeActivity.class);
                        intent.putExtra("phone_number",phone);
                        intent.putExtra("sms_code",sms_code);
                        startActivity(intent);
                        finish();
                    }else if(status==-1){//用户不存在
                        dm.buildAlertDialog(message);
                    } else if(status==1){//缺少sign参数
                        dm.buildAlertDialog(message);
                    } else if(status==2){//非法请求，sign验证失败
                        dm.buildAlertDialog(message);
                    } else if(status==3){//跳转登录
                        Intent intent=new Intent(RegisterInputNumberActivity.this, LoginActivity.class);
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
        dialog4.show();
    }*/
    //服务协议
    public void goServerProtocol(View view) {
        Intent intent=new Intent(this,RegisterProtocolActivity.class);
        startActivity(intent);
    }

    @Override
    public void handle200Data(JSONObject dataObj, String message) throws JSONException {
        String sms_code = dataObj.getString("sms_code");
        Intent intent=new Intent(RegisterInputNumberActivity.this, RegisterAfterReceiveCodeActivity.class);
        intent.putExtra("phone_number",phone);
        intent.putExtra("sms_code",sms_code);
        startActivity(intent);
        finish();
    }

    public void goBack(View view) {
        finish();
    }
}
