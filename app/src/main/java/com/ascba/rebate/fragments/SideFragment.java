package com.ascba.rebate.fragments;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ascba.rebate.R;
import com.ascba.rebate.adapter.BusAdapter;
import com.ascba.rebate.beans.Business;
import com.ascba.rebate.fragments.base.BaseFragment;
import com.ascba.rebate.view.SuperSwipeRefreshLayout;
import com.warmtel.expandtab.ExpandPopTabView;
import com.warmtel.expandtab.KeyValueBean;
import com.warmtel.expandtab.PopOneListView;
import com.warmtel.expandtab.PopTwoListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 周边
 */
public class SideFragment extends BaseFragment implements SuperSwipeRefreshLayout.OnPullRefreshListener {


    private ExpandPopTabView popTab;
    private List<KeyValueBean> typeAll;//全部
    private List<KeyValueBean> typeSide;//附近
    private List<KeyValueBean> typeAuto;//智能排序
    private RecyclerView busRV;
    private List<Business> data;
    private BusAdapter busAdapter;
    private SuperSwipeRefreshLayout ssr;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    public SideFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_side, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {
        popTab = ((ExpandPopTabView) view.findViewById(R.id.expandtab_view));
        initData();
        addItem(popTab,typeAll,"全部0","全部0");
        addItem(popTab,typeSide,"附近0","附近0");
        addItem(popTab,typeAuto,"智能排序0","智能排序0");

        busRV = ((RecyclerView) view.findViewById(R.id.bus_list));
        initBusData();
        busAdapter = new BusAdapter(R.layout.main_bussiness_list_item,data,getActivity());
        busRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        busRV.setAdapter(busAdapter);

        ssr = ((SuperSwipeRefreshLayout) view.findViewById(R.id.refresh_layout));
        ssr.setOnPullRefreshListener(this);

    }

    private void initBusData() {
        data=new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            data.add(new Business("http://image18-c.poco.cn/mypoco/myphoto/20170301/16/18505011120170301161107098_640.jpg","金牌炒面","五星级商家",R.mipmap.main_business_category,"0个评论","0m"));
        }
    }

    private void initData() {
        typeAll=new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            typeAll.add(new KeyValueBean("all","全部"+i));
        }
        typeSide=new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            typeSide.add(new KeyValueBean("side","附近"+i));
        }
        typeAuto=new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            typeAuto.add(new KeyValueBean("auto","智能排序"+i));
        }
    }
    //一级
    public void addItem(ExpandPopTabView expandTabView, List<KeyValueBean> lists, String defaultSelect, String defaultShowText) {
        PopOneListView popOneListView = new PopOneListView(getActivity());
        popOneListView.setDefaultSelectByValue(defaultSelect);
        //popViewOne.setDefaultSelectByKey(defaultSelect);
        popOneListView.setCallBackAndData(lists, expandTabView, new PopOneListView.OnSelectListener() {
            @Override
            public void getValue(String key, String value) {
                Log.e("tag", "key :" + key + " ,value :" + value);
            }
        });
        expandTabView.addItemToExpandTab(defaultShowText, popOneListView);
    }
    //二级
    public void addItem(ExpandPopTabView expandTabView, List<KeyValueBean> parentLists,
                        List<ArrayList<KeyValueBean>> childrenListLists, String defaultParentSelect, String defaultChildSelect, String defaultShowText) {
        PopTwoListView popTwoListView = new PopTwoListView(getActivity());
        popTwoListView.setDefaultSelectByValue(defaultParentSelect, defaultChildSelect);
        //distanceView.setDefaultSelectByKey(defaultParent, defaultChild);
        popTwoListView.setCallBackAndData(expandTabView, parentLists, childrenListLists, new PopTwoListView.OnSelectListener() {
            @Override
            public void getValue(String showText, String parentKey, String childrenKey) {
                Log.e("tag", "showText :" + showText + " ,parentKey :" + parentKey + " ,childrenKey :" + childrenKey);
            }
        });
        expandTabView.addItemToExpandTab(defaultShowText, popTwoListView);
    }

    @Override
    public void onRefresh() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ssr.setRefreshing(false);
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
