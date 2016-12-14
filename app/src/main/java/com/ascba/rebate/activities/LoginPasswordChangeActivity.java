package com.ascba.rebate.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.Base2Activity;
import com.ascba.rebate.activities.base.BaseActivity;
import com.ascba.rebate.activities.login.LoginActivity;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.handlers.CheckThread;
import com.ascba.rebate.handlers.PhoneHandler;
import com.ascba.rebate.utils.LogUtils;
import com.ascba.rebate.utils.NetUtils;
import com.ascba.rebate.utils.UrlEncodeUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.EditTextWithCustomHint;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginPasswordChangeActivity extends Base2Activity implements Base2Activity.Callback {

    private EditTextWithCustomHint edOldPassword;
    private EditTextWithCustomHint edNewPassword;
    private EditTextWithCustomHint edReNewPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_password_change);
        initViews();

    }

    private void initViews() {
        edOldPassword = ((EditTextWithCustomHint) findViewById(R.id.ed_old_password));
        edNewPassword = ((EditTextWithCustomHint) findViewById(R.id.ed_new_password));
        edReNewPassword = ((EditTextWithCustomHint) findViewById(R.id.ed_re_new_password));
    }

    public void saveLoginPassword(View view) {
        requestPwdChange(UrlUtils.changePwd);
    }

    private void requestPwdChange(String url) {
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
        Request<JSONObject> objRequest = buildNetRequest(url, 0, true);
        objRequest.add("oldpassword", oldString);
        objRequest.add("password", newString);
        objRequest.add("repassword", reNewString);
        executeNetWork(objRequest,"请稍后");
        setCallback(this);
    }

    @Override
    public void handle200Data(JSONObject dataObj, String message) {
        Toast.makeText(LoginPasswordChangeActivity.this, message, Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(this,LoginActivity.class);
        AppConfig.getInstance().putInt("uuid",-1000);
        startActivity(intent);
        finish();
    }
}