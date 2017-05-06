package com.ascba.rebate.activities.me_page.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.activities.login.LoginActivity;
import com.ascba.rebate.activities.me_page.settings.child.PasswordProtect2Activity;
import com.ascba.rebate.activities.me_page.settings.child.PayPasswordChangeActivity;
import com.ascba.rebate.activities.me_page.settings.child.PersonalDataActivity;
import com.ascba.rebate.activities.me_page.settings.child.SafeSettingActivity;
import com.ascba.rebate.application.MyApplication;

public class SettingActivity extends BaseNetActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }


    //进入  个人资料  界面
    public void settingPersonalDataClick(View view) {
        Intent intent = new Intent(this, PersonalDataActivity.class);
        startActivity(intent);
    }

    //进入  安全中心  界面
    public void settingSafeSetting(View view) {
        Intent intent = new Intent(this, SafeSettingActivity.class);
        startActivity(intent);
    }

    //进入  支付设置  界面
    public void settingPay(View view) {
        Intent intent = new Intent(this, PayPasswordChangeActivity.class);
        startActivity(intent);
    }

    //进入  密保设置  界面
    public void goPasswordProtect(View view) {
        Intent intent = new Intent(this, PasswordProtect2Activity.class);
        startActivity(intent);
    }

    //点击退出，清除缓存，进入登录界面
    public void exitUser(View view) {
        SharedPreferences sf = getSharedPreferences("first_login_success_name_password", MODE_PRIVATE);
        sf.edit()
                .putInt("uuid", -1000)
                .putString("token", "")
                .putLong("expiring_time", -2000)
                .putString("login_phone", "")
                .putString("login_password", "").apply();
        //setResult(RESULT_OK, getIntent());
        MyApplication.isLoad=false;
        MyApplication.isSignOut=true;
        MyApplication.signOutSignInMoney=true;
        MyApplication.signOutSignInMe=true;
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

}
