package com.ascba.rebate.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWorkActivity;

public class RechargeResultActivity extends BaseNetWorkActivity{

    private TextView tvMoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);
        initViews();
        getDataFromIntent();
    }

    @SuppressLint("SetTextI18n")
    private void getDataFromIntent() {
        Intent intent = getIntent();
        if(intent!=null){
            String total_amount = intent.getStringExtra("total_amount");
            if(total_amount!=null && !"".equals(total_amount)){
                tvMoney.setText("¥ "+total_amount);
            }
        }
    }

    private void initViews() {
        tvMoney = ((TextView) findViewById(R.id.tv_money));
    }

    public void goMain(View view) {
        Intent intent = getIntent();
        setResult(RESULT_OK,intent);
        finish();
    }
}
