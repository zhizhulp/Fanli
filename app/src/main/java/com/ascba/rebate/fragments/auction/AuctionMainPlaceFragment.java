package com.ascba.rebate.fragments.auction;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ascba.rebate.R;
import com.ascba.rebate.adapter.MyFragmentPagerAdapter;
import com.ascba.rebate.beans.TittleBean;
import com.ascba.rebate.fragments.base.BaseNetFragment;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.ShopABar;
import com.yanzhenjie.nohttp.rest.Request;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李鹏 on 2017/5/23.
 * 主会场
 */

public class AuctionMainPlaceFragment extends BaseNetFragment {
    private List<Fragment> fragmentList = new ArrayList<>();//fragment列表
    private List<TittleBean> titleList = new ArrayList<>();//tab名的列表
    private MyFragmentPagerAdapter adapter;
    private TabLayout tabLayout;
    private int position;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_auction_main_place, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        requestNetwork(UrlUtils.auctionType, 0);
    }

    public void requestNetwork(String url, int what) {
        Request<JSONObject> request = buildNetRequest(url, 0, false);
        request.add("type", 1);
        request.add("strat_time", 0);
        request.add("end_time", 0);
        request.add("now_page", 1);
        executeNetWork(what, request, "请稍后");
    }

    @Override
    protected void mhandle200Data(int what, JSONObject object, JSONObject dataObj, String message) {
        JSONArray jsonArray = dataObj.optJSONArray("auction_subcategory");
        if (jsonArray != null && jsonArray.length() > 0) {
            tabLayout.removeAllTabs();
            titleList.clear();
            fragmentList.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.optJSONObject(i);
                TittleBean tb = new TittleBean(obj.optInt("id"), obj.optLong("starttime"), obj.optLong("endtime"), obj.optString("auction_status"), obj.optString("now_time"));
                titleList.add(tb);
                fragmentList.add(AuctionMainPlaceChildFragment.newInstance(1, tb));
            }
        }
        adapter.notifyDataSetChanged();
        setTabSelect();
    }

    private void setTabSelect() {
        int tabCount = tabLayout.getTabCount();
        if (tabCount > 0) {
            if (isGoing()) {
                TabLayout.Tab tabAt = tabLayout.getTabAt(position);
                if (tabAt != null) {
                    if (!tabAt.isSelected()) {
                        tabAt.select();
                    }
                }
            }
        }
    }

    public void setTabNextSelect() {
        int tabCount = tabLayout.getTabCount();
        if (tabCount > 0) {
            int position = tabLayout.getSelectedTabPosition();
            TabLayout.Tab tabAt = tabLayout.getTabAt(position+1);
            if (tabAt != null) {
                if (!tabAt.isSelected()) {
                    tabAt.select();
                }
            }
        }
    }

    private void initView(View view) {
        ShopABar shopABar = (ShopABar) view.findViewById(R.id.shopBar);
        shopABar.setImageOtherEnable(false);
        shopABar.setMsgEnable(false);
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        for (int i = 0; i < titleList.size(); i++) {
            TittleBean tb = titleList.get(i);
            TabLayout.Tab tab = tabLayout.newTab().setText(tb.getNowTime() + "\n" + tb.getStatus());
            tabLayout.addTab(tab);
        }
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        adapter = new MyFragmentPagerAdapter(getChildFragmentManager(), fragmentList, titleList);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    //是否有正在进行的商品
    private boolean isGoing() {
        boolean hasGoing = false;//是否有正在进行中
        for (int i = 0; i < titleList.size(); i++) {
            TittleBean bean = titleList.get(i);
            String status = bean.getStatus();
            if (status.equals("进行中")) {
                position = i;
                hasGoing = true;
                break;
            }
        }
        return hasGoing;
    }
}
