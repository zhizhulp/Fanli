package com.ascba.rebate.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWork4Activity;
import com.ascba.rebate.adapter.MessageLatestAdapter;
import com.ascba.rebate.beans.NewsBean;
import com.ascba.rebate.view.ShopABarText;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李鹏 on 2017/03/31 0031.
 * 消息-最新公告
 */

public class MessageLatestActivity extends BaseNetWork4Activity {

    private ShopABarText shopBar;
    private Context context;
    private RecyclerView recyclerView;
    private List<NewsBean> beanList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_latest);
        context = this;
        initView();
    }

    public static void startIntent(Context context) {
        Intent intent = new Intent(context, MessageLatestActivity.class);
        context.startActivity(intent);
    }

    private void initView() {
        shopBar = (ShopABarText) findViewById(R.id.shopBar);
        shopBar.setBtnEnable(false);
        shopBar.setCallback(new ShopABarText.Callback() {
            @Override
            public void back(View v) {
                finish();
            }

            @Override
            public void clkBtn(View v) {
            }
        });
        initData();
        recyclerView = (RecyclerView) findViewById(R.id.recylerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        MessageLatestAdapter adapter = new MessageLatestAdapter(R.layout.item_message_latest, beanList);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                switch (position) {

                }
            }
        });
    }

    private void initData() {
        for (int i = 0; i <3 ; i++) {
            beanList.add(new NewsBean("关于钱来钱往2017年发布会通知","2017-03-03 10:07:56"));
        }
    }
}
