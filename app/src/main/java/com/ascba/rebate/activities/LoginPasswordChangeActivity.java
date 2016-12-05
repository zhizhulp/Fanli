package com.ascba.rebate.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
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

public class LoginPasswordChangeActivity extends BaseActivity {

    private EditTextWithCustomHint edOldPassword;
    private EditTextWithCustomHint edNewPassword;
    private EditTextWithCustomHint edReNewPassword;
    private PhoneHandler phoneHandler;
    private CheckThread checkThread;
    private RequestQueue requestQueue;
    private SharedPreferences sf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_password_change);
        sf = getSharedPreferences("first_login_success_name_password", MODE_PRIVATE);
        initViews();

    }

    private void initViews() {
        edOldPassword = ((EditTextWithCustomHint) findViewById(R.id.ed_old_password));
        edNewPassword = ((EditTextWithCustomHint) findViewById(R.id.ed_new_password));
        edReNewPassword = ((EditTextWithCustomHint) findViewById(R.id.ed_re_new_password));
    }

    public void saveLoginPassword(View view) {

        sendMsgToSevr("http://api.qlqwgw.com/v1/changePwd");
    }

    private void sendMsgToSevr(String baseUrl) {
        String oldString = edOldPassword.getText().toString();
        String newString = edNewPassword.getText().toString();
        String reNewString = edReNewPassword.getText().toString();
        if(oldString.equals("")||newString.equals("")||reNewString.equals("")){
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if(newString.length()<6||newString.length()>16||reNewString.length()<6||reNewString.length()>16){
            Toast.makeText(this, "密码必须大于6位小于16位", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!newString.equals(reNewString)){
            Toast.makeText(this, "2次输入的密码不一致", Toast.LENGTH_SHORT).show();
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

        objRequest.add("oldpassword", oldString);
        objRequest.add("password", newString);
        objRequest.add("repassword", reNewString);
        phoneHandler = new PhoneHandler(this);
        phoneHandler.setCallback(new PhoneHandler.Callback() {
            @Override
            public void getMessage(Message msg) {
                dialog.dismiss();
                JSONObject jObj = (JSONObject) msg.obj;
                LogUtils.PrintLog("123LoginPasswordChangeActivity", jObj.toString());
                try {
                    int status = jObj.optInt("status");
                    JSONObject dataObj = jObj.optJSONObject("data");
                    int update_status = dataObj.optInt("update_status");
                    if (status == 200) {

                        Toast.makeText(LoginPasswordChangeActivity.this, jObj.optString("msg"), Toast.LENGTH_SHORT).show();
                        if (update_status == 1) {
                            sf.edit()
                                    .putString("token", dataObj.optString("token"))
                                    .putLong("expiring_time", dataObj.optLong("expiring_time"))
                                    .apply();
                        }
                    } else if (status == 5) {
                        Toast.makeText(LoginPasswordChangeActivity.this, jObj.optString("msg"), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginPasswordChangeActivity.this, LoginActivity.class);
                        sf.edit().putInt("uuid", -1000).apply();
                        startActivity(intent);
                        finish();
                    } else if (status == 3) {
                        Intent intent = new Intent(LoginPasswordChangeActivity.this, LoginActivity.class);
                        sf.edit().putInt("uuid", -1000).apply();
                        startActivity(intent);
                        finish();
                    } else if (status == 404) {
                        Toast.makeText(LoginPasswordChangeActivity.this, jObj.getString("msg"), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginPasswordChangeActivity.this, "未知原因", Toast.LENGTH_SHORT).show();
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
