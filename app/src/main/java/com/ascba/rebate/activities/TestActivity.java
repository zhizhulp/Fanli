package com.ascba.rebate.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.ascba.rebate.R;
import com.jaeger.library.StatusBarUtil;




public class TestActivity extends AppCompatActivity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        StatusBarUtil.setColor(this, 0xffe52020);
        //test
        //lipeng
        Log.d("TestActivity", "test");
        //lp修改
        String s="HelloMan";
        System.out.print("lipeng");
        String s1="";
    }
}