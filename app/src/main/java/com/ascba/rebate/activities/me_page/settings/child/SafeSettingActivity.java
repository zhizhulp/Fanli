package com.ascba.rebate.activities.me_page.settings.child;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.PayPsdSettingActivity;
import com.ascba.rebate.activities.SettingPayPsdActivity;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.activities.me_page.settings.child.safe_setting_child.LoginPasswordChangeActivity;
import com.ascba.rebate.activities.me_page.settings.child.safe_setting_child.PasswordProtectActivity;
import com.ascba.rebate.activities.me_page.settings.child.safe_setting_child.PhoneChangeActivity;
import com.ascba.rebate.appconfig.AppConfig;

public class SafeSettingActivity extends BaseNetActivity {

    private TextView tvPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe_setting);
        initView();
    }

    private void initView() {
        tvPay = ((TextView) findViewById(R.id.tv_pay));
        int type = AppConfig.getInstance().getInt("is_level_pwd", 0);
        if(type==0){
            tvPay.setText("设置支付密码");
        }else if(type==1){
            tvPay.setText("修改支付密码");
        }
    }

    //修改手机号码
    public void safeSettingPhoneChange(View view) {
        Intent intent=new Intent(this,PhoneChangeActivity.class);
        startActivity(intent);
    }
    //修改密保问题
    public void safeSettingPasswordProtect(View view) {
        Intent intent=new Intent(this,PasswordProtectActivity.class);
        intent.putExtra("type",111);
        startActivity(intent);
    }
    //修改登录问题
    public void loginPassword(View view) {
        Intent intent=new Intent(this,LoginPasswordChangeActivity.class);
        startActivity(intent);
    }
    //修改支付密码
    public void goModifyPayPassword(View view) {
        Intent intent=new Intent(this,PayPsdSettingActivity.class);
        intent.putExtra("type",222);
        startActivity(intent);
    }

}
