package com.ascba.rebate.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.List;

/**
 * Created by Administrator on 2017/6/6.
 * 竞拍购物车pagerAdapter
 */

public class AuctionCartPagerAdapter extends FragmentPagerAdapter {
    private String [] titles;
    public AuctionCartPagerAdapter(FragmentManager fm, List<Fragment> fragmentList,String [] titles) {
        super(fm, fragmentList);
        this.titles=titles;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
