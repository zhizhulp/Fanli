package com.ascba.fanli.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ascba.fanli.R;
import com.ascba.fanli.activities.base.BaseActivity;

public class SafeSettingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe_setting);
    }
    //修改手机号码
    public void safeSettingPhoneChange(View view) {
        Intent intent=new Intent(this,PhoneChangeActivity.class);
        startActivity(intent);
    }
    //修改密保问题
    public void safeSettingPasswordProtect(View view) {
        Intent intent=new Intent(this,PasswordProtectActivity.class);
        startActivity(intent);
    }
}
