package com.ascba.rebate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseActivity;
import com.ascba.rebate.fragments.me.FourthFragment;

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
    //进入  支付设置  界面
    public void settingPay(View view) {
        Intent intent=new Intent(this,PayPasswordChangeActivity.class);
        startActivity(intent);
    }
    //进入  密保设置  界面
    public void goPasswordProtect(View view) {
        Intent intent=new Intent(this,PasswordProtect2Activity.class);
        startActivity(intent);
    }
    //点击退出，清除缓存，进入登录界面
    public void exitUser(View view) {
        Intent intent=new Intent(this, FourthFragment.class);
        setResult(2,intent);
        finish();
    }
}
