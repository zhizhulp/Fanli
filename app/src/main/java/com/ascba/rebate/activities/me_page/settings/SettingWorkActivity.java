package com.ascba.rebate.activities.me_page.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWorkActivity;
import com.ascba.rebate.activities.me_page.settings.child.PasswordProtect2WorkActivity;
import com.ascba.rebate.activities.me_page.settings.child.PayPasswordChangeWorkActivity;
import com.ascba.rebate.activities.me_page.settings.child.PersonalDataWorkActivity;
import com.ascba.rebate.activities.me_page.settings.child.SafeSettingWorkActivity;
import com.ascba.rebate.application.MyApplication;

public class SettingWorkActivity extends BaseNetWorkActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        //StatusBarUtil.setColor(this, getResources().getColor(R.color.moneyBarColor));
    }


    //进入  个人资料  界面
    public void settingPersonalDataClick(View view) {
        Intent intent = new Intent(this, PersonalDataWorkActivity.class);
        startActivity(intent);
    }

    //进入  安全中心  界面
    public void settingSafeSetting(View view) {
        Intent intent = new Intent(this, SafeSettingWorkActivity.class);
        startActivity(intent);
    }

    //进入  支付设置  界面
    public void settingPay(View view) {
        Intent intent = new Intent(this, PayPasswordChangeWorkActivity.class);
        startActivity(intent);
    }

    //进入  密保设置  界面
    public void goPasswordProtect(View view) {
        Intent intent = new Intent(this, PasswordProtect2WorkActivity.class);
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
        setResult(RESULT_OK, getIntent());
        MyApplication.isLoad=false;
        MyApplication.isSignOut=true;
        finish();
    }

}
