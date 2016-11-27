package com.ascba.rebate.activities;

import android.os.Bundle;
import android.view.View;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseActivity;

public class AccountRechargeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_recharge);
    }
    //点击充值界面，进入ping++支付界面
    public void goRechargeActivity(View view) {

    }
}
