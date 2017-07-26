package com.ascba.rebate.activities.auction;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.adapter.MyFragmentPagerAdapter;
import com.ascba.rebate.beans.TittleBean;
import com.ascba.rebate.fragments.auction.AuctionMainPlaceChildFragment;
import com.ascba.rebate.utils.UrlUtils;
import com.yanzhenjie.nohttp.rest.Request;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 抢拍盲拍列表
 */
public class AuctionListActivity extends BaseNetActivity {
    private List<Fragment> fragmentList=new ArrayList<>();//fragment列表
    private List<TittleBean> titleList=new ArrayList<>();//tab名的列表
    private TabLayout tabLayout;
    private int position;
    private int type;
    private int client_key;
    private Timer timer;
    private long delay;
    private long between;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_auction_main_place);
        getParams();
        initView();
        requestNetwork(UrlUtils.auctionType,0);
    }
    private void getParams() {
        Intent b = getIntent();
        if (b != null) {
            this.type = b.getIntExtra("type",1);
            this.client_key = b.getIntExtra("client_key",0);
        }
    }

    public void requestNetwork(String url, int what) {
        Request<JSONObject> request = buildNetRequest(url, 0, false);
        request.add("type",type);
        request.add("strat_time",0);
        request.add("end_time",0);
        request.add("now_page",1);
        request.add("client_key",client_key);
        executeNetWork(what,request,"请稍后");
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
        MyFragmentPagerAdapter adapter=new MyFragmentPagerAdapter(getSupportFragmentManager(),fragmentList,titleList);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        int tabCount = tabLayout.getTabCount();
        if(tabCount >0){
            if(isGoing()){
                TabLayout.Tab tabAt = tabLayout.getTabAt(position);
                if(tabAt!=null){
                    tabAt.select();
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
                AuctionMainPlaceChildFragment childFragment = AuctionMainPlaceChildFragment.newInstance(type, client_key, tb);
                fragmentList.add(childFragment);
            }
        }
    }

    private void initView() {
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        for (int i=0;i<titleList.size();i++){
            TittleBean tb = titleList.get(i);
            TabLayout.Tab tab = tabLayout.newTab().setText(tb.getNowTime() + "\n" + tb.getStatus());
            tabLayout.addTab(tab);
        }
        viewPager = (ViewPager) findViewById(R.id.viewPager);
    }
    //是否有正在进行的商品
    private boolean isGoing(){
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(timer!=null){
            timer.cancel();
        }
    }

    private class MyTimerTask extends TimerTask{
        @Override
        public void run() {
            requestNetwork(UrlUtils.auctionType, 0);
        }
    }
}
