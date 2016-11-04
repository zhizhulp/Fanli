package com.ascba.fanli.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ascba.fanli.R;
import com.ascba.fanli.activities.base.BaseActivity;

public class SettingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }
    //进入  个人资料  界面
    public void settingPersonalDataClick(View view) {
        Intent intent=new Intent(this,PersonalDataActivity.class);
        startActivity(intent);
    }
    //进入  实名认证  界面
    public void settingRealNameConfirm(View view) {
        Intent intent=new Intent(this,RealNameCofirmActivity.class);
        startActivity(intent);
    }

    //进入  我的二维码  界面
    public void settingMyQRCode(View view) {
        Intent intent=new Intent(this,QRCodeActivity.class);
        startActivity(intent);
    }
    //进入  安全中心  界面
    public void settingSafeSetting(View view) {
        Intent intent=new Intent(this,SafeSettingActivity.class);
        startActivity(intent);
    }

}
