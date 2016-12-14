package com.ascba.rebate.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.Base2Activity;
import com.ascba.rebate.activities.base.NetworkBaseActivity;
import com.jaeger.library.StatusBarUtil;

public class TestActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

    }
}
