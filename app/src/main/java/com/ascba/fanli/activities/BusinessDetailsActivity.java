package com.ascba.fanli.activities;

import android.os.Bundle;
import android.view.View;

import com.ascba.fanli.R;
import com.ascba.fanli.activities.base.BaseActivity;

public class BusinessDetailsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_details);
    }
    //返回按钮
    public void back(View view) {
        finish();
    }
}
