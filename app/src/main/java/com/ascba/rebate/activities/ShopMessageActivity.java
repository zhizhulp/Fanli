package com.ascba.rebate.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWork4Activity;
import com.ascba.rebate.adapter.ShopMessageAdapter;
import com.ascba.rebate.beans.MessageBean;
import com.ascba.rebate.view.ShopABarText;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李鹏 on 2017/03/27 0027.
 * 消息
 */

public class ShopMessageActivity extends BaseNetWork4Activity {

    private ShopABarText bar;
    private RecyclerView recyclerView;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_message);
        context = this;
        initView();
    }

    public static void startIntent(Context context) {
        Intent intent = new Intent(context, ShopMessageActivity.class);
        context.startActivity(intent);
    }

    private void initView() {
        bar = (ShopABarText) findViewById(R.id.shopBar);
        bar.setBtnEnable(false);
        bar.setCallback(new ShopABarText.Callback() {
            @Override
            public void back(View v) {
                finish();
            }

            @Override
            public void clkBtn(View v) {
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recylerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        ShopMessageAdapter adapter = new ShopMessageAdapter(R.layout.item_message, getData());
        recyclerView.setAdapter(adapter);
    }

    public List<MessageBean> getData() {
        List<MessageBean> beanList = new ArrayList<>();
        beanList.add(new MessageBean(R.mipmap.icon_mess_sale, "促销活动", "商城促销折扣，更多优惠通知都在这里~"));
        beanList.add(new MessageBean(R.mipmap.icon_mess_sys, "系统通知", "您的订单号：231541321321541321123还未支付"));
        beanList.add(new MessageBean(R.mipmap.icon_mess_update, "升级提示", "app将于本月24号进行维护升级，带来的不便敬请谅解"));
        return beanList;
    }
}
