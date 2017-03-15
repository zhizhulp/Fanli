package com.ascba.rebate.activities;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWork4Activity;
import com.ascba.rebate.adapter.BusinessShopAdapter;
import com.ascba.rebate.beans.ShopBaseItem;
import com.ascba.rebate.view.ShopABar;
import com.ascba.rebate.view.SuperSwipeRefreshLayout;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by 李鹏 on 2017/03/15 0015.
 * 商家店铺
 */

public class BusinessShopActivity extends BaseNetWork4Activity implements SuperSwipeRefreshLayout.OnPullRefreshListener {

    private SuperSwipeRefreshLayout refreshLat;
    private Handler handler = new Handler();
    private Context context;
    private ShopABar shopABar;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_shop);
        context = this;
        initView();
    }

    private void initView() {
        //刷新
        refreshLat = (SuperSwipeRefreshLayout) findViewById(R.id.refresh_layout);
        refreshLat.setOnPullRefreshListener(this);

        //导航栏
        shopABar = (ShopABar) findViewById(R.id.shopbar);
        shopABar.setImageOther(R.mipmap.icon_cart_black);
        shopABar.setCallback(new ShopABar.Callback() {
            @Override
            public void back(View v) {
                finish();
            }

            @Override
            public void clkMsg(View v) {
                Toast.makeText(context, "信息", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void clkOther(View v) {
                Toast.makeText(context, "购物车", Toast.LENGTH_SHORT).show();
            }
        });

        //recylerview
        recyclerView = (RecyclerView) findViewById(R.id.recylerview);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));

        BusinessShopAdapter businessShopAdapter = new BusinessShopAdapter(R.layout.shop_goods, getData(), context);
        businessShopAdapter.setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
            @Override
            public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                return 1;
            }
        });

        //头布局
        View headView = LayoutInflater.from(context).inflate(R.layout.business_shop_head, null);
        ImageView backImg = (ImageView) headView.findViewById(R.id.shop_img);
        Glide.with(context).load("http://image18-c.poco.cn/mypoco/myphoto/20170315/16/18505011120170315160125061_640.jpg").into(backImg);
        ImageView headImg = (ImageView) headView.findViewById(R.id.shop_img_head);
        Glide.with(context).load("http://image18-c.poco.cn/mypoco/myphoto/20170315/16/18505011120170315160144021_640.jpg").into(headImg);
        businessShopAdapter.addHeaderView(headView);

        recyclerView.setAdapter(businessShopAdapter);
    }

    private List<ShopBaseItem> getData() {
        List<ShopBaseItem> goodsList = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            goodsList.add(new ShopBaseItem("http://image18-c.poco.cn/mypoco/myphoto/20170301/16/18505011120170301161107098_640.jpg", "拉菲庄园2009珍酿原装进口红酒艾格力古堡干红葡", "￥ 498.00", "已售4件"));
        }
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
