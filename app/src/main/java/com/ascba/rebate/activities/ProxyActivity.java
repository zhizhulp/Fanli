package com.ascba.rebate.activities;

import android.os.Bundle;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWorkActivity;
import com.jaeger.library.StatusBarUtil;


public class ProxyActivity extends BaseNetWorkActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proxy);
        StatusBarUtil.setColor(this, 0xffe52020);
    }
}
