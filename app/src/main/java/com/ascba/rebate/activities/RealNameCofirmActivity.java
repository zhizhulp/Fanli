package com.ascba.rebate.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseActivity;
import com.ascba.rebate.activities.login.LoginActivity;
import com.ascba.rebate.handlers.CheckThread;
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

public class RealNameCofirmActivity extends BaseActivity {
    private PhoneHandler phoneHandler;
    private CheckThread checkThread;
    private RequestQueue requestQueue;
    private SharedPreferences sf;
    private EditText edId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_name_cofirm);
        sf = getSharedPreferences("first_login_success_name_password", MODE_PRIVATE);
        initView();
    }

    private void initView() {
        edId = ((EditText) findViewById(R.id.editTextWithCustomHint2));
    }

    //
    public void goConfirm(View view) {
        sendMsgToSevr(UrlUtils.findCardInfo);
    }

    private void sendMsgToSevr(String baseUrl) {
        boolean netAva = NetUtils.isNetworkAvailable(this);
        if(!netAva){
            Toast.makeText(this, "请打开网络", Toast.LENGTH_SHORT).show();
            return;
        }
        int uuid = sf.getInt("uuid", -1000);
        String token = sf.getString("token", "");
        long expiring_time = sf.getLong("expiring_time", -2000);
        requestQueue = NoHttp.newRequestQueue();
        final ProgressDialog dialog = new ProgressDialog(this, R.style.dialog);
        dialog.setMessage("请稍后");
        Request<JSONObject> objRequest = NoHttp.createJsonObjectRequest(baseUrl + "?", RequestMethod.POST);
        objRequest.add("sign", UrlEncodeUtils.createSign(baseUrl));
        objRequest.add("uuid", uuid);
        objRequest.add("token", token);
        objRequest.add("expiring_time", expiring_time);
        objRequest.add("cardid", edId.getText().toString());
        phoneHandler = new PhoneHandler(this);
        phoneHandler.setCallback(new PhoneHandler.Callback() {
            @Override
            public void getMessage(Message msg) {
                dialog.dismiss();
                JSONObject jObj = (JSONObject) msg.obj;
                try {
                    int status = jObj.optInt("status");
                    String message = jObj.getString("msg");
                    if (status == 200) {
                        JSONObject dataObj = jObj.optJSONObject("data");
                        int update_status = dataObj.optInt("update_status");
                        JSONObject cardObj = dataObj.getJSONObject("cardIdInfo");
                        String card = cardObj.optString("cardid");
                        String sex = cardObj.optString("sex");
                        String age = cardObj.optString("age");
                        String location = cardObj.optString("location");
                        if (update_status == 1) {
                            sf.edit()
                                    .putString("token", dataObj.optString("token"))
                                    .putLong("expiring_time", dataObj.optLong("expiring_time"))
                                    .apply();
                        }
                        Intent intent=new Intent(RealNameCofirmActivity.this,CardDataActivity.class);
                        intent.putExtra("card",card);
                        intent.putExtra("sex",sex);
                        intent.putExtra("age",age);
                        intent.putExtra("location",location);
                        startActivity(intent);
                        finish();
                    } else if(status==1||status==2||status==3||status == 4||status==5){//缺少sign参数
                        Intent intent = new Intent(RealNameCofirmActivity.this, LoginActivity.class);
                        sf.edit().putInt("uuid", -1000).apply();
                        startActivity(intent);
                        finish();
                    } else if(status==404){
                        Toast.makeText(RealNameCofirmActivity.this, message, Toast.LENGTH_SHORT).show();
                    } else if(status==500){
                        Toast.makeText(RealNameCofirmActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        checkThread = new CheckThread(requestQueue, phoneHandler, objRequest);
        checkThread.start();
        //登录中对话框
        dialog.show();
    }
}
