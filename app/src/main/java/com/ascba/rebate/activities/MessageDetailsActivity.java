package com.ascba.rebate.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWork4Activity;
import com.ascba.rebate.adapter.MessageOrderAdapter;
import com.ascba.rebate.beans.OrderBean;
import com.ascba.rebate.view.ShopABarText;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李鹏 on 2017/03/28 0028.
 * 消息详情
 */

public class MessageDetailsActivity extends BaseNetWork4Activity {

    private ShopABarText shopBar;
    private Context context;
    private RecyclerView recyclerView;
    private List<OrderBean> beanList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_details);
        context = this;
        initView();
    }

    public static void startIntent(Context context) {
        Intent intent = new Intent(context, MessageDetailsActivity.class);
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
        MessageOrderAdapter adapter = new MessageOrderAdapter(R.layout.item_message_order, beanList, context);
        recyclerView.setAdapter(adapter);
    }

    private void initData() {
        for (int i = 0; i < 5; i++) {
            String url1 = "http://image18-c.poco.cn/mypoco/myphoto/20170315/10/18505011120170315100507017_640.jpg";
            String time1 = "2017年02月12日";
            String id1 = "175142365789142";
            OrderBean orderBean1 = new OrderBean(time1, id1, url1);
            beanList.add(orderBean1);
        }
    }
}
