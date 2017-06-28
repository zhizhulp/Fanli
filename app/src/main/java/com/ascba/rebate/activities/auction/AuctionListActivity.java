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

/**
 * 抢拍盲拍列表
 */
public class AuctionListActivity extends BaseNetActivity {
    private List<Fragment> fragmentList=new ArrayList<>();//fragment列表
    private List<TittleBean> titleList=new ArrayList<>();//tab名的列表
    private MyFragmentPagerAdapter adapter;
    private TabLayout tabLayout;
    private int position;
    private int type;
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
        }
    }

    public void requestNetwork(String url, int what) {
        Request<JSONObject> request = buildNetRequest(url, 0, false);
        request.add("type",type);
        request.add("strat_time",0);
        request.add("end_time",0);
        request.add("now_page",1);
        executeNetWork(what,request,"请稍后");
    }
    @Override
    protected void mhandle200Data(int what, JSONObject object, JSONObject dataObj, String message) {
        JSONArray jsonArray = dataObj.optJSONArray("auction_subcategory");
        if(jsonArray!=null && jsonArray.length()>0){
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.optJSONObject(i);
                TittleBean tb = new TittleBean(obj.optInt("id"), obj.optLong("starttime"), obj.optLong("endtime"), obj.optString("auction_status"), obj.optString("now_time"));
                titleList.add(tb);
                Log.d(TAG, "mhandle200Data:type--> "+type);
                fragmentList.add(AuctionMainPlaceChildFragment.newInstance(type,tb));
            }
        }
        adapter.notifyDataSetChanged();
        int tabCount = tabLayout.getTabCount();
        if(tabCount >0){
            if(isGoing()){
                TabLayout.Tab tabAt = tabLayout.getTabAt(position);
                if(tabAt!=null){
                    tabAt.select();
                }
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
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        adapter=new MyFragmentPagerAdapter(getSupportFragmentManager(),fragmentList,titleList);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
    //是否有正在进行的商品
    private boolean isGoing(){
        boolean hasGoing=false;//是否有正在进行中
        for (int i = 0; i < titleList.size(); i++) {
            TittleBean bean= titleList.get(i);
            String status = bean.getStatus();
            if(status.equals("进行中")){
                position=i;
                hasGoing=true;
                break;
            }
        }
        return hasGoing;
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
}
