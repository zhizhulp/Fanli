package com.ascba.rebate.activities.me_page.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWorkActivity;
import com.ascba.rebate.activities.me_page.settings.child.PasswordProtect2Activity;
import com.ascba.rebate.activities.me_page.settings.child.PayPasswordChangeActivity;
import com.ascba.rebate.activities.me_page.settings.child.PersonalDataActivity;
import com.ascba.rebate.activities.me_page.settings.child.SafeSettingActivity;
import com.ascba.rebate.application.MyApplication;
import com.ascba.rebate.fragments.MeFragment;

public class SettingActivity extends BaseNetWorkActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        //StatusBarUtil.setColor(this, getResources().getColor(R.color.moneyBarColor));
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
        MyApplication.isPersonalData = true;
        SharedPreferences sf = getSharedPreferences("first_login_success_name_password", MODE_PRIVATE);
        sf.edit()
                .putInt("uuid", -1000)
                .putString("token", "")
                .putLong("expiring_time", -2000)
                .putString("login_phone", "")
                .putString("login_password", "").apply();
        Intent intent = new Intent(this, MeFragment.class);
        setResult(RESULT_OK, intent);
        finish();
    }

}
