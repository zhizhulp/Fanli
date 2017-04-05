package com.ascba.rebate.activities;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWork4Activity;
import com.ascba.rebate.adapter.DeliverDetailsAdapter;
import com.ascba.rebate.beans.Goods;
import com.ascba.rebate.view.ShopABarText;
import com.ascba.rebate.view.SuperSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李鹏 on 2017/03/15 0015.
 * 确认订单
 */

public class ConfirmOrderActivity extends BaseNetWork4Activity implements SuperSwipeRefreshLayout.OnPullRefreshListener {

    private SuperSwipeRefreshLayout refreshLat;
    private Handler handler = new Handler();
    private Context context;
    private ShopABarText shopABarText;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        context=this;
        initView();
    }

    private void initView() {
        //刷新
        refreshLat = ((SuperSwipeRefreshLayout) findViewById(R.id.refresh_layout));
        refreshLat.setOnPullRefreshListener(this);

        //导航栏
        shopABarText = (ShopABarText) findViewById(R.id.shopbar);
        shopABarText.setBtnEnable(false);
        shopABarText.setCallback(new ShopABarText.Callback() {
            @Override
            public void back(View v) {
                finish();
            }

            @Override
            public void clkBtn(View v) {

            }
        });

        //recyclerView
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        DeliverDetailsAdapter adapter = new DeliverDetailsAdapter(R.layout.item_goods, getData(),context);
        recyclerView.setAdapter(adapter);
    }

    private List<Goods> getData() {
        List<Goods> goodsList = new ArrayList<>();
        String imgUrl = "http://image18-c.poco.cn/mypoco/myphoto/20170315/10/18505011120170315100507017_640.jpg";
        String title = "RCC男装 春夏 设计师款修身尖领翻领免烫薄长袖寸衫 韩国代购 2色";
        String standard = "颜色:深蓝色;尺码:S";
        String price = "368";
        String priceOld = "468";
        int num = 1;

        goodsList.add(new Goods(imgUrl, title, standard, price, priceOld, num));
        goodsList.add(new Goods(imgUrl, title, standard, price, priceOld, num));
        return goodsList;
    }


    @Override
    public void onRefresh() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshLat.setRefreshing(false);
            }
        }, 1000);
    }

    @Override
    public void onPullDistance(int distance) {

    }

    @Override
    public void onPullEnable(boolean enable) {

    }
}
