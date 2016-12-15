package com.ascba.rebate.activities;

import android.os.Bundle;
import android.widget.GridView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWorkActivity;
import com.ascba.rebate.adapter.BusPicGVAdapter;

public class BusinessPicActivity extends BaseNetWorkActivity {

    private GridView busPicGridView;
    private BusPicGVAdapter busPicGVAdapter;
    private int [] icons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_pic);
        initGridView();
    }

    private void initGridView() {
        busPicGridView = ((GridView) findViewById(R.id.business_pic_gv));
        initIcon();
        busPicGVAdapter = new BusPicGVAdapter(icons,this);
        busPicGridView.setAdapter(busPicGVAdapter);
    }

    private void initIcon() {
        icons=new int[6];
        for (int i = 0; i < icons.length; i++) {
            icons[i]=R.mipmap.logo;
        }
    }
}
