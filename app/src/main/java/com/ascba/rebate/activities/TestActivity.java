package com.ascba.rebate.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ascba.rebate.R;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy,MM,dd");
        String format = sdf.format(new Date(1481707683));
    }
}
