package com.ascba.rebate.fragments;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ascba.rebate.R;
import com.ascba.rebate.fragments.base.BaseFragment;
import com.ascba.rebate.view.ShopABar;
import com.ascba.rebate.view.SuperSwipeRefreshLayout;

import java.util.ArrayList;

/**
 * 购物车
 */
public class CartFragment extends BaseFragment implements SuperSwipeRefreshLayout.OnPullRefreshListener {


    private ShopABar sab;
    private ArrayList goodsList=new ArrayList();
    private SuperSwipeRefreshLayout refreshLayout;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    private RecyclerView rv;

    public CartFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }

    private void initViews(View view) {
        //初始化标题栏
        sab = ((ShopABar) view.findViewById(R.id.sab));
        sab.setBackEnable(false);
        sab.setImageOtherEnable(false);
        sab.setTitle("购物车(100)");
        //初始化刷新控件
        refreshLayout = ((SuperSwipeRefreshLayout) view.findViewById(R.id.refresh_layout));
        refreshLayout.setOnPullRefreshListener(this);

        //初始化recyclerView
        rv = ((RecyclerView) view.findViewById(R.id.cart_goods_list));
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        initData();
    }

    private void initData() {

    }

    @Override
    public void onRefresh() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(false);
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
