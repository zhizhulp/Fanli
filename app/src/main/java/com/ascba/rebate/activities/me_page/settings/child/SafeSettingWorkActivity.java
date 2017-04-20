package com.ascba.rebate.activities.me_page.settings.child;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWorkActivity;
import com.ascba.rebate.activities.me_page.settings.child.safe_setting_child.LoginPasswordChangeWorkActivity;
import com.ascba.rebate.activities.me_page.settings.child.safe_setting_child.PasswordProtectWorkActivity;
import com.ascba.rebate.activities.me_page.settings.child.safe_setting_child.PhoneChangeWorkActivity;

public class SafeSettingWorkActivity extends BaseNetWorkActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe_setting);
        //StatusBarUtil.setColor(this, 0xffe52020);
    }
    //修改手机号码
    public void safeSettingPhoneChange(View view) {
        Intent intent=new Intent(this,PhoneChangeWorkActivity.class);
        startActivity(intent);
    }
    //修改密保问题
    public void safeSettingPasswordProtect(View view) {
        Intent intent=new Intent(this,PasswordProtectWorkActivity.class);
        intent.putExtra("type",111);
        startActivity(intent);
    }
    //修改登录问题
    public void loginPassword(View view) {
        Intent intent=new Intent(this,LoginPasswordChangeWorkActivity.class);
        startActivity(intent);
    }
    //修改支付密码
    public void goModifyPayPassword(View view) {
        Intent intent=new Intent(this,PasswordProtectWorkActivity.class);
        intent.putExtra("type",222);
        startActivity(intent);
    }

}
