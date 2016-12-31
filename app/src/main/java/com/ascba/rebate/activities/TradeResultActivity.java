package com.ascba.rebate.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.main.MainActivity;
import com.ascba.rebate.activities.splash.SplashActivity;
import com.jaeger.library.StatusBarUtil;

public class TradeResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_result);
        StatusBarUtil.setColor(this, 0xffe52020);
    }

    public void goComplete(View view) {
        finish();
    }
}
