package com.ascba.rebate.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.ascba.rebate.R;
import com.jaeger.library.StatusBarUtil;



/**
 * 演示poi搜索功能
 */
public class TestActivity extends AppCompatActivity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        StatusBarUtil.setColor(this, 0xffe52020);
    }
}