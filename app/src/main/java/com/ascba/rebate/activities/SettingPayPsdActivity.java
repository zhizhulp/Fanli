package com.ascba.rebate.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.view.ShopABar;

/**
 * Created by 李鹏 on 2017/04/28 0028.
 * 设置支付密码
 */

public class SettingPayPsdActivity extends BaseNetActivity {

    private Context context;
    private ShopABar shopABar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_pay_psd);
        context = this;
        initUI();
    }

    private void initUI() {
        shopABar = (ShopABar) findViewById(R.id.shopBar);
        shopABar.setCallback(new ShopABar.Callback() {
            @Override
            public void back(View v) {
                finish();
            }

            @Override
            public void clkMsg(View v) {

            }

            @Override
            public void clkOther(View v) {

            }
        });


    }
}
