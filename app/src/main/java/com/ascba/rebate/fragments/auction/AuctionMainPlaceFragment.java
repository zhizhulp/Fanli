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
import com.ascba.rebate.activities.auction.AuctionListActivity;
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
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 李鹏 on 2017/5/23.
 * 主会场
 */

public class AuctionMainPlaceFragment extends BaseNetFragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int position;
    private Timer timer;
    private long delay;
    private long between;
    private List<Fragment> fragmentList=new ArrayList<>();//fragment列表
    private List<TittleBean> titleList=new ArrayList<>();//tab名的列表

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
        titleList.clear();
        fragmentList.clear();
        parseData(jsonArray);
        setTabLayout();
    }

    private void setTabLayout() {
        MyFragmentPagerAdapter adapter=new MyFragmentPagerAdapter(getActivity().getSupportFragmentManager(),fragmentList,titleList);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        int tabCount = tabLayout.getTabCount();
        if(tabCount >0){
            if(isGoing(titleList)){
                TabLayout.Tab tabAt = tabLayout.getTabAt(position);
                if(tabAt!=null){
                    tabAt.select();
                    adapter.notifyDataSetChanged();
                }
            }else {
                delay = between = (titleList.get(0).getEndTime()-titleList.get(0).getStartTime())* 1000;
            }
            if(timer==null){
                timer= new Timer();
                timer.schedule(new MyTimerTask(),delay+1500,between);//1.5后进行请求，否则会有2个进行中
            }
        }
    }

    private void parseData(JSONArray jsonArray) {
        if(jsonArray!=null && jsonArray.length()>0){
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.optJSONObject(i);
                TittleBean tb = new TittleBean(obj.optInt("id"), obj.optLong("starttime"), obj.optLong("endtime"), obj.optString("auction_status"), obj.optString("now_time"));
                titleList.add(tb);
                AuctionMainPlaceChildFragment childFragment = AuctionMainPlaceChildFragment.newInstance(1, 0, tb);
                fragmentList.add(childFragment);
            }
        }
    }


    private void initView(View view) {
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        for (int i=0;i<titleList.size();i++){
            TittleBean tb = titleList.get(i);
            TabLayout.Tab tab = tabLayout.newTab().setText(tb.getNowTime() + "\n" + tb.getStatus());
            tabLayout.addTab(tab);
        }
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
    }

    private void setTabSelect(List<TittleBean> titleList) {
        for (int i = 0; i < titleList.size(); i++) {
            TittleBean tb = titleList.get(i);
            long endTime = tb.getEndTime() * 1000;//ms
            long startTime = tb.getStartTime() * 1000;//ms
            long nowTime = System.currentTimeMillis();
            if (nowTime >= startTime && nowTime <= endTime) {
                TabLayout.Tab tabAt = tabLayout.getTabAt(i);
                if (tabAt != null) {
                    tabAt.select();
                }
                break;
            }
        }
    }
    private class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            requestNetwork(UrlUtils.auctionType, 0);
        }
    }
    //是否有正在进行的商品
    private boolean isGoing(List<TittleBean> titleList){
        boolean hasGoing=false;//是否有正在进行中
        for (int i = 0; i < titleList.size(); i++) {
            TittleBean bean= titleList.get(i);
            String status = bean.getStatus();
            long endTime = bean.getEndTime();
            long startTime = bean.getStartTime();
            between = (endTime - startTime)*1000;
            if(status.equals("进行中")){
                delay = endTime * 1000 -System.currentTimeMillis();
                if(delay < 0){
                    delay =0;
                }
                position=i;
                hasGoing=true;
                break;
            }else {
                delay=between;
            }
        }
        return hasGoing;
    }
}
