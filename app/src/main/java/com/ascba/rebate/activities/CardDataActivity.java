package com.ascba.rebate.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseActivity;
import com.ascba.rebate.activities.login.LoginActivity;
import com.ascba.rebate.handlers.CheckThread;
import com.ascba.rebate.handlers.PhoneHandler;
import com.ascba.rebate.utils.LogUtils;
import com.ascba.rebate.utils.UrlEncodeUtils;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;

import org.json.JSONException;
import org.json.JSONObject;

public class CardDataActivity extends BaseActivity {

    private TextView tvCard;
    private TextView tvSex;
    private TextView tvAge;
    private TextView tvLocation;
    private EditText etName;
    private String card;
    private String sex;
    private String age;
    private String location;
    private PhoneHandler phoneHandler;
    private CheckThread checkThread;
    private RequestQueue requestQueue;
    private SharedPreferences sf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_data);
        initViews();
        getDataFromBefore();
    }

    private void initViews() {
        sf=getSharedPreferences("first_login_success_name_password",MODE_PRIVATE);
        tvCard = ((TextView) findViewById(R.id.card));
        tvSex = ((TextView) findViewById(R.id.sex));
        tvAge = ((TextView) findViewById(R.id.age));
        tvLocation = ((TextView) findViewById(R.id.location));
        etName = ((EditText) findViewById(R.id.name));

    }

    public void getDataFromBefore() {
        Intent intent = getIntent();
        if(intent!=null){
            card = intent.getStringExtra("card");
            sex = intent.getStringExtra("sex");
            age = intent.getStringExtra("age");
            location = intent.getStringExtra("location");
            tvCard.setText(card);
            tvSex.setText(sex);
            tvAge.setText(age);
            tvLocation.setText(location);
        }
    }

    public void goRealNameConfirm(View view) {
        sendMsgToSevr("http://api.qlqwgw.com/v1/verifyCard");
    }

    private void sendMsgToSevr(String baseUrl) {
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

        objRequest.add("cardid", card);
        objRequest.add("realname", etName.getText().toString());
        if(sex.equals("男")){
            objRequest.add("sex", 1);
        }else if(sex.equals("女")){
            objRequest.add("sex", 0);
        }else{
            objRequest.add("sex", 2);
        }
        objRequest.add("age", age);
        objRequest.add("location", location);
        phoneHandler = new PhoneHandler(this);
        phoneHandler.setCallback(new PhoneHandler.Callback() {
            @Override
            public void getMessage(Message msg) {
                dialog.dismiss();
                JSONObject jObj = (JSONObject) msg.obj;
                LogUtils.PrintLog("123CardDataActivity", jObj.toString());
                try {
                    int status = jObj.optInt("status");
                    JSONObject dataObj = jObj.optJSONObject("data");
                    int update_status = dataObj.optInt("update_status");
                    if (status == 200) {
                        Toast.makeText(CardDataActivity.this, jObj.optString("msg"), Toast.LENGTH_SHORT).show();
                        if (update_status == 1) {
                            sf.edit()
                                    .putString("token", dataObj.optString("token"))
                                    .putLong("expiring_time", dataObj.optLong("expiring_time"))
                                    .apply();
                        }
                    } else if (status == 5) {
                        Toast.makeText(CardDataActivity.this, jObj.optString("msg"), Toast.LENGTH_SHORT).show();
                    } else if (status == 3) {
                        Intent intent = new Intent(CardDataActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else if (status == 404) {
                        Toast.makeText(CardDataActivity.this, jObj.getString("msg"), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(CardDataActivity.this, "未知原因", Toast.LENGTH_SHORT).show();
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
