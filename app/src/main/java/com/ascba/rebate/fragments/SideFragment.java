package com.ascba.rebate.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ascba.rebate.R;
import com.warmtel.expandtab.ExpandPopTabView;
import com.warmtel.expandtab.KeyValueBean;
import com.warmtel.expandtab.PopOneListView;
import com.warmtel.expandtab.PopTwoListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 周边
 */
public class SideFragment extends Fragment {


    private ExpandPopTabView popTab;
    private List<KeyValueBean> typeAll;//全部
    private List<KeyValueBean> typeSide;//附近
    private List<KeyValueBean> typeAuto;//智能排序

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
}
