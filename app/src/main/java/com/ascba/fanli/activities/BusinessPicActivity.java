package com.ascba.fanli.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;

import com.ascba.fanli.R;

public class BusinessPicActivity extends AppCompatActivity {

    private GridView busPicGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_pic);
        initGridView();
    }

    private void initGridView() {
        busPicGridView = ((GridView) findViewById(R.id.business_pic_gv));
    }
}
