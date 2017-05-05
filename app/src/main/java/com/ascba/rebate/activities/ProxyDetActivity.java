package com.ascba.rebate.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.adapter.ProxyDetAdapter;
import com.ascba.rebate.beans.ProxyDet;
import com.ascba.rebate.view.MarqueeTextView;
import com.ascba.rebate.view.MoneyBar;
import com.ascba.rebate.view.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class ProxyDetActivity extends BaseNetActivity implements MoneyBar.CallBack
        ,SwipeRefreshLayout.OnRefreshListener {

    private MoneyBar mb;
    private RecyclerView rv;
    private ProxyDetAdapter adapter;
    private List<ProxyDet> data;
    private MarqueeTextView tvMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proxy_det);

        initViews();
    }

    private void initViews() {
        initRecyclerView();
        initRefreshLayout();
        refreshLayout.setOnRefreshListener(this);

        tvMsg = ((MarqueeTextView) findViewById(R.id.tv_msg));
        tvMsg.setText("数据展示，近期开放，敬请关注系统通知即可。数据展示，近期开放，敬请关注系统通知即可。");
    }

    private void initRecyclerView() {
        rv = ((RecyclerView) findViewById(R.id.rv));
        rv.setLayoutManager(new GridLayoutManager(this, 2));
        rv.addItemDecoration(new SpaceItemDecoration());

        mb = ((MoneyBar) findViewById(R.id.mb));
        mb.setTailTitle("切换地区");
        mb.setCallBack(this);
        getData();
        adapter = new ProxyDetAdapter(R.layout.proxy_det_item, data);
        rv.setAdapter(adapter);
    }

    private void getData() {
        data = new ArrayList<>();

        data.add(new ProxyDet(R.mipmap.proxy_jrls, "今日流水", "￥ 0"));
        data.add(new ProxyDet(R.mipmap.proxy_zlse, "总流水额", "￥ 0"));
        data.add(new ProxyDet(R.mipmap.proxy_qysj, "区域商家", "0家"));
        data.add(new ProxyDet(R.mipmap.proxy_qyhy, "区域会员", "0人"));

    }


    @Override
    public void clickImage(View im) {

    }

    /*
     * 切换地区
     */
    @Override
    public void clickComplete(View tv) {

    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(false);
            }
        }, 1000);
    }

}
