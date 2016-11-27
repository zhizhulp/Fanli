package com.ascba.rebate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseActivity;

public class BusinessCenterActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_center);
    }
    //商户申请成功之后进入商户详情
    public void go_business_center(View view) {
        Intent intent=new Intent(this,BusinessDataActivity.class);
        startActivity(intent);
    }
}
