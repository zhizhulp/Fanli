package com.ascba.rebate.activities.me_page.settings.child;

import android.os.Bundle;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWorkActivity;
import com.jaeger.library.StatusBarUtil;

public class PayPasswordChangeActivity extends BaseNetWorkActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_password_change);
        //StatusBarUtil.setColor(this, 0xffe52020);
    }
}
