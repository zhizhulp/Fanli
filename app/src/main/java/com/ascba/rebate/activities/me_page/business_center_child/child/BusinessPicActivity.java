package com.ascba.rebate.activities.me_page.business_center_child.child;

import android.os.Bundle;
import android.widget.GridView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWorkActivity;
import com.ascba.rebate.adapter.BusPicGVAdapter;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

public class BusinessPicActivity extends BaseNetWorkActivity {
    private List<String> urls;
    private GridView busPicGridView;
    private BusPicGVAdapter busPicGVAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_pic);
        StatusBarUtil.setColor(this, 0xffe52020);
        initGridView();
    }

    private void initGridView() {

        busPicGridView = ((GridView) findViewById(R.id.business_pic_gv));
        initUrls();
        busPicGVAdapter = new BusPicGVAdapter(urls,this);
        busPicGridView.setAdapter(busPicGVAdapter);
    }

    private void initUrls() {
        urls=new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            urls.add("https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/logo/logo_white_fe6da1ec.png");
        }
    }
}
