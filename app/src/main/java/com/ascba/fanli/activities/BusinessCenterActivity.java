package com.ascba.fanli.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ascba.fanli.R;
import com.ascba.fanli.activities.base.BaseActivity;

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
