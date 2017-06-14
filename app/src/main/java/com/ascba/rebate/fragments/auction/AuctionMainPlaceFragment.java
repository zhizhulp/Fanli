package com.ascba.rebate.fragments.auction;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
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
    private ViewPager viewPager;

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
            titleList.clear();
            fragmentList.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.optJSONObject(i);
                TittleBean tb = new TittleBean(obj.optInt("id"), obj.optLong("starttime"), obj.optLong("endtime"), obj.optString("auction_status"), obj.optString("now_time"));
                titleList.add(tb);
                AuctionMainPlaceChildFragment childFragment = AuctionMainPlaceChildFragment.newInstance(1, tb);
                childFragment.setListener(new AuctionMainPlaceChildFragment.EndTimeListener() {
                    @Override
                    public void timeCome() {
                        requestNetwork(UrlUtils.auctionType, 0);
                    }
                });
                fragmentList.add(childFragment);
            }
            for (int i = 0; i < titleList.size(); i++) {
                TittleBean tb = titleList.get(i);
                TabLayout.Tab tab = tabLayout.newTab().setText(tb.getNowTime() + "\n" + tb.getStatus());
                tabLayout.addTab(tab);
            }
            if(adapter==null){
                adapter = new MyFragmentPagerAdapter(getChildFragmentManager(), fragmentList, titleList);
                viewPager.setAdapter(adapter);
                tabLayout.setupWithViewPager(viewPager);
            }else {
                adapter.notifyDataSetChanged();
            }
            setTabSelect();
        }

    }


    private void initView(View view) {
        ShopABar shopABar = (ShopABar) view.findViewById(R.id.shopBar);
        shopABar.setImageOtherEnable(false);
        shopABar.setMsgEnable(false);
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);

    }

    private void setTabSelect() {
        for (int i = 0; i < titleList.size(); i++) {
            TittleBean tb = titleList.get(i);
            long endTime = tb.getEndTime() *1000;//ms
            long startTime = tb.getStartTime() * 1000;//ms
            long nowTime = System.currentTimeMillis();
            if(nowTime >= startTime && nowTime<= endTime){
                TabLayout.Tab tabAt = tabLayout.getTabAt(i);
                if(tabAt!=null){
                    tabAt.select();
                }
                break;
            }
        }
    }
}
