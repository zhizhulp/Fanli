package com.ascba.rebate.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWork4Activity;
import com.ascba.rebate.adapter.ProxyDetAdapter;
import com.ascba.rebate.beans.ProxyDet;
import com.ascba.rebate.view.MoneyBar;
import com.ascba.rebate.view.SpaceItemDecoration;
import com.ascba.rebate.view.SuperSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class ProxyDetActivity extends BaseNetWork4Activity implements MoneyBar.CallBack
        ,SuperSwipeRefreshLayout.OnPullRefreshListener{

    private MoneyBar mb;
    private RecyclerView rv;
    private ProxyDetAdapter adapter;
    private List<ProxyDet> data;
    private SuperSwipeRefreshLayout refreshLat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proxy_det);

        initViews();
    }

    private void initViews() {
        initRecyclerView();
        initRefreshLayout();

    }

    private void initRefreshLayout() {
        refreshLat = ((SuperSwipeRefreshLayout) findViewById(R.id.refresh_layout));
        refreshLat.setOnPullRefreshListener(this);
    }

    private void initRecyclerView() {
        rv = ((RecyclerView) findViewById(R.id.rv));
        rv.setLayoutManager(new GridLayoutManager(this, 2));
        rv.addItemDecoration(new SpaceItemDecoration(this,2, 2));
        getData();
        adapter = new ProxyDetAdapter(R.layout.proxy_det_item, data);
        View inflate = getLayoutInflater().inflate(R.layout.proxy_det_head, null);
        initMoneyBar(inflate);
        adapter.addHeaderView(inflate);
        rv.setAdapter(adapter);
    }

    private void getData() {
        data = new ArrayList<>();

        data.add(new ProxyDet(R.mipmap.proxy_jrls, "今日流水", "￥ 268010"));
        data.add(new ProxyDet(R.mipmap.proxy_zlse, "总流水额", "￥ 4862500"));
        data.add(new ProxyDet(R.mipmap.proxy_qysj, "区域商家", "236家"));
        data.add(new ProxyDet(R.mipmap.proxy_qyhy, "区域会员", "306人"));

    }

    private void initMoneyBar(View inflate) {
        mb = ((MoneyBar) inflate.findViewById(R.id.mb));
        mb.setTailTitle("切换地区");
        mb.setCallBack(this);
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
                refreshLat.setRefreshing(false);
            }
        },1000);
    }

    @Override
    public void onPullDistance(int distance) {

    }

    @Override
    public void onPullEnable(boolean enable) {

    }
}
