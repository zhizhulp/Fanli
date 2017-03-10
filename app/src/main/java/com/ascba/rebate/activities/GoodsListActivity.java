package com.ascba.rebate.activities;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWork4Activity;
import com.ascba.rebate.adapter.GoodsListAdapter;
import com.ascba.rebate.beans.Goods;
import com.ascba.rebate.view.ShopABar;
import com.ascba.rebate.view.SuperSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李鹏 on 2017/03/09 0009.
 * 商品列表
 */

public class GoodsListActivity extends BaseNetWork4Activity {

    private ShopABar shopABar;
    private Spinner spinner;
    private RadioGroup radioGroup;
    private RadioButton button1, button2;
    private RecyclerView recyclerView;

    //切换标示,默认显示线性布局
    private boolean isLinearLayout = true;
    private GoodsListAdapter goodsListAdapter;

    private SuperSwipeRefreshLayout superSwipeRefreshLayout;
    private Handler handler = new Handler();
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_list);
        context = this;
        InitBar();
        InitSpinner();
        InitRadio();
        InitRecylerView();
        InitRefresh();
    }

    private void InitRefresh() {
        superSwipeRefreshLayout = (SuperSwipeRefreshLayout) findViewById(R.id.activity_goods_list_refresh_layout);
        View view = LayoutInflater.from(context).inflate(R.layout.foot_view, null, false);
        superSwipeRefreshLayout.setFooterView(view);

        superSwipeRefreshLayout.setOnPullRefreshListener(new SuperSwipeRefreshLayout.OnPullRefreshListener() {
            @Override
            public void onRefresh() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        superSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 1000);
            }

            @Override
            public void onPullDistance(int distance) {

            }

            @Override
            public void onPullEnable(boolean enable) {

            }
        });

        superSwipeRefreshLayout.setOnPushLoadMoreListener(new SuperSwipeRefreshLayout.OnPushLoadMoreListener() {
            @Override
            public void onLoadMore() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        superSwipeRefreshLayout.setLoadMore(false);
                    }
                }, 1000);
            }

            @Override
            public void onPushDistance(int distance) {

            }

            @Override
            public void onPushEnable(boolean enable) {

            }
        });
    }

    private void InitBar() {
        shopABar = (ShopABar) findViewById(R.id.activity_goods_list_bar);
        shopABar.setImageOtherEnable(false);
        shopABar.setImMsgSta(R.mipmap.swicth1);
        shopABar.setCallback(new ShopABar.Callback() {
            @Override
            public void back(View v) {
                finish();
            }

            @Override
            public void clkMsg(View v) {
                //切换

                if (isLinearLayout) {
                    //切换成网格布局
                    goodsListAdapter.setType(1);
                    recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
                    goodsListAdapter.notifyDataSetChanged();
                    isLinearLayout = false;

                } else {
                    //切换成垂直线性布局
                    goodsListAdapter.setType(0);
                    recyclerView.setLayoutManager(new GridLayoutManager(context, 1));
                    goodsListAdapter.notifyDataSetChanged();
                    isLinearLayout = true;
                }
            }

            @Override
            public void clkOther(View v) {

            }
        });
    }

    private void InitSpinner() {
        spinner = (Spinner) findViewById(R.id.activity_goods_list_spinner);
        // 建立数据源
        final String[] mItems = new String[]{"默认", "价格", "好评"};
        // 建立Adapter并且绑定数据源
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //绑定 Adapter到控件
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {

                Toast.makeText(context, mItems[pos], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void InitRadio() {
        radioGroup = (RadioGroup) findViewById(R.id.activity_goods_list_rdg);
        button1 = (RadioButton) findViewById(R.id.activity_goods_list_rdb1);
        button2 = (RadioButton) findViewById(R.id.activity_goods_list_rdb2);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (group.getCheckedRadioButtonId()) {
                    case R.id.activity_goods_list_rdb1:
                        Toast.makeText(context, "销量", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.activity_goods_list_rdb2:
                        Toast.makeText(context, "自营商店", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    private void InitRecylerView() {
        List<Goods> goodsBeen = new ArrayList<>();
        for (int i = 0; i < 10; i++) {

            String img1 = "http://image18-c.poco.cn/mypoco/myphoto/20170302/14/18505011120170302145124095_640.jpg";
            Goods goods1 = new Goods();
            goods1.setGoodsTitle("拉菲庄园2009珍酿原装酒进口红酒艾格丽古堡干红葡萄");
            goods1.setGoodsPrice("￥498.00");
            goods1.setImgUrl(img1);
            goods1.setGoodsSelled("已售4件");
            goodsBeen.add(goods1);

            String img2 = "http://image18-c.poco.cn/mypoco/myphoto/20170309/11/18505011120170309114124067_640.jpg";
            Goods goods2 = new Goods();
            goods2.setGoodsTitle("【精选超市】马来西亚进口Munchy马奇新新 秒乐迷你");
            goods2.setGoodsPrice("￥159.00");
            goods2.setImgUrl(img2);
            goods2.setGoodsSelled("已售8件");
            goodsBeen.add(goods2);
        }

        recyclerView = (RecyclerView) findViewById(R.id.activity_goods_list_recyclerview);
        goodsListAdapter = new GoodsListAdapter(context, goodsBeen);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 1));
        recyclerView.setAdapter(goodsListAdapter);
    }
}
