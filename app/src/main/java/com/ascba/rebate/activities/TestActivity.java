package com.ascba.rebate.activities;

import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.NetworkBaseActivity;
import com.ascba.rebate.beans.City;
import com.ascba.rebate.handlers.CheckThread;
import com.ascba.rebate.handlers.PhoneHandler;
import com.yolanda.nohttp.rest.Request;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends NetworkBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }
    void test(){
        System.out.print("just a small test");
    }
}
