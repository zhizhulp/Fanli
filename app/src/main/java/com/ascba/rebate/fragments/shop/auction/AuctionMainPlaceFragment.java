package com.ascba.rebate.fragments.shop.auction;

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
import com.ascba.rebate.fragments.base.BaseNetFragment;
import com.ascba.rebate.fragments.shop.TypeFragment;
import com.ascba.rebate.view.ShopABar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李鹏 on 2017/5/23.
 * 主会场
 */

public class AuctionMainPlaceFragment extends BaseNetFragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<Fragment> fragmentList=new ArrayList<>();//fragment列表
    private List<String> stringList=new ArrayList<>();//tab名的列表
    private MyFragmentPagerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_auction_main_place, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {
        ShopABar shopABar = (ShopABar) view.findViewById(R.id.shopBar);
        shopABar.setImageOtherEnable(false);
        shopABar.setMsgEnable(false);
        shopABar.setCallback(new ShopABar.Callback() {
            @Override
            public void back(View v) {
                getActivity().finish();
            }

            @Override
            public void clkMsg(View v) {
            }

            @Override
            public void clkOther(View v) {

            }
        });

        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);

        getData();

        for (int i=0;i<stringList.size();i++){
            tabLayout.addTab(tabLayout.newTab().setText(stringList.get(i)));
        }

        adapter=new MyFragmentPagerAdapter(getActivity().getSupportFragmentManager(),fragmentList,stringList);
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);
    }
    private void getData(){
        fragmentList.add(new AuctionMainPlaceChildFragment());
        fragmentList.add(new AuctionMainPlaceChildFragment());
        fragmentList.add(new AuctionMainPlaceChildFragment());
        fragmentList.add(new AuctionMainPlaceChildFragment());

        stringList.add("16:00\n拍卖结束");
        stringList.add("17:00\n疯狂抢购中");
        stringList.add("18:00\n即将开始");
        stringList.add("19:00\n即将开始");
    }
}
