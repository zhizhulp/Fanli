package com.ascba.rebate.activities.me_page.settings.child.safe_setting_child;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWorkActivity;
import com.jaeger.library.StatusBarUtil;

public class PhoneChangeActivity extends BaseNetWorkActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_change);
        StatusBarUtil.setColor(this, 0xffe52020);
    }

    public void goModifyPhone(View view) {
        Intent intent=new Intent(this,PhoneChange2Activity.class);
        startActivity(intent);
    }
}
