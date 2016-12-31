package com.ascba.rebate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWorkActivity;
import com.jaeger.library.StatusBarUtil;

public class ShowDescriptionActivity extends BaseNetWorkActivity {

    private TextView tvDes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_description);
        StatusBarUtil.setColor(this, 0xffe52020);
        initViews();
        getDataFromIntent();//获取商家详情
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if(intent!=null){
            String seller_description = intent.getStringExtra("seller_description");
            tvDes.setText(seller_description);
        }
    }

    private void initViews() {
        tvDes = ((TextView) findViewById(R.id.tv_show_des));
    }
}
