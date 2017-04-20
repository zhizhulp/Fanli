package com.ascba.rebate.activities.me_page;

import android.os.Bundle;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetActivity;


public class ProxyActivity extends BaseNetActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proxy);
        //StatusBarUtil.setColor(this, 0xffe52020);
    }
}
