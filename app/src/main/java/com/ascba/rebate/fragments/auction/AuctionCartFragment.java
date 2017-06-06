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
import com.ascba.rebate.adapter.AuctionCartPagerAdapter;
import com.ascba.rebate.fragments.base.BaseNetFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 竞拍购物车
 */
public class AuctionCartFragment extends BaseNetFragment {


    private List<Fragment> fragments;
    private String[] titles;

    public AuctionCartFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_auction_cart, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
    }

    private void initView(View view) {
        TabLayout tabLayout = ((TabLayout) view.findViewById(R.id.tabLayout));
        ViewPager viewPager = ((ViewPager) view.findViewById(R.id.viewPager));

        initFragments();
        initTitles();
        AuctionCartPagerAdapter adapter=new AuctionCartPagerAdapter(getChildFragmentManager(),fragments,titles);
        viewPager.setAdapter(adapter);
        addTabs(tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void addTabs(TabLayout tabLayout) {
        for (String title : titles) {
            tabLayout.addTab(tabLayout.newTab().setText(title));
        }
    }

    private void initTitles() {
        titles= new String[2];
        titles[0]="竞拍商品";
        titles[1]="交保证金";
    }

    private void initFragments() {
        fragments=new ArrayList<>();
        fragments.add(CartChildFragment.newInstance("0,1"));
        fragments.add(CartChildFragment.newInstance("2,3"));
    }
}
