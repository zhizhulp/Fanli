package com.ascba.rebate.activities;

import android.os.Bundle;
import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.NetworkBaseActivity;

public class TestActivity extends NetworkBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }
}
