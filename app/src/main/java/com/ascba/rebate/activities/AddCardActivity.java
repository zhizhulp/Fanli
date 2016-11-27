package com.ascba.rebate.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
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

public class AddCardActivity extends BaseActivity {

    private EditTextWithCustomHint edName;
    private EditTextWithCustomHint edCardNumber;
    private PhoneHandler phoneHandler;
    private CheckThread checkThread;
    private RequestQueue requestQueue;
    private SharedPreferences sf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_crad);
        initViews();
    }

    private void initViews() {
        sf=getSharedPreferences("first_login_success_name_password",MODE_PRIVATE);
        edName = ((EditTextWithCustomHint) findViewById(R.id.ed_add_card_name));
        edCardNumber = ((EditTextWithCustomHint) findViewById(R.id.ed_add_card_number));
    }

    public void next(View view) {
        sendMsgToSevr("http://api.qlqwgw.com/v1/getBankCard");
    }

    private void sendMsgToSevr(String baseUrl) {
        String name = edName.getText().toString();
        String cardNumber = edCardNumber.getText().toString();
        LogUtils.PrintLog("123",cardNumber);
        if(name.equals("")||cardNumber.equals("")){
            Toast.makeText(this, "请输入姓名或卡号", Toast.LENGTH_SHORT).show();
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
        objRequest.add("bank_card", cardNumber);

        phoneHandler = new PhoneHandler(this);
        phoneHandler.setCallback(new PhoneHandler.Callback() {
            @Override
            public void getMessage(Message msg) {
                dialog.dismiss();
                JSONObject jObj = (JSONObject) msg.obj;
                LogUtils.PrintLog("123AddCardActivity", jObj.toString());
                try {
                    int status = jObj.optInt("status");
                    JSONObject dataObj = jObj.optJSONObject("data");
                    int update_status = dataObj.optInt("update_status");
                    if (status == 200) {
                        String realname = dataObj.optString("realname");
                        String cardid = dataObj.optString("cardid");
                        String bank_card = dataObj.optString("bank_card");
                        String bank = dataObj.optString("bank");
                        String type = dataObj.optString("type");
                        String nature = dataObj.optString("nature");
                        String kefu = dataObj.optString("kefu");
                        String logo = dataObj.optString("logo");
                        String info = dataObj.optString("info");
                        if (update_status == 1) {
                            sf.edit()
                                    .putString("token", dataObj.optString("token"))
                                    .putLong("expiring_time", dataObj.optLong("expiring_time"))
                                    .apply();
                        }
                        Intent intent = new Intent(AddCardActivity.this,BankCardActivity.class);
                        intent.putExtra("realname",realname);
                        intent.putExtra("cardid",cardid);
                        intent.putExtra("bank_card",bank_card);
                        intent.putExtra("bank",bank);
                        intent.putExtra("type",type);
                        intent.putExtra("nature",nature);
                        intent.putExtra("kefu",kefu);
                        intent.putExtra("logo",logo);
                        intent.putExtra("info",info);
                        startActivity(intent);
                        finish();
                    } else if (status == 5) {
                        Toast.makeText(AddCardActivity.this, jObj.optString("msg"), Toast.LENGTH_SHORT).show();
                    } else if (status == 3) {
                        Intent intent = new Intent(AddCardActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else if (status == 404) {
                        Toast.makeText(AddCardActivity.this, jObj.getString("msg"), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AddCardActivity.this, "未知原因", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        checkThread = new CheckThread(requestQueue, phoneHandler, objRequest);
        checkThread.start();
        dialog.show();
    }
}
