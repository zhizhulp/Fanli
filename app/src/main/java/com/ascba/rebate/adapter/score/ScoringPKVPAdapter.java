package com.ascba.rebate.adapter.score;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.ascba.rebate.adapter.ViewpagerFragmentAdapter;

import java.util.List;

/**
 * Created by lenovo on 2017/6/14.
 */

public class ScoringPKVPAdapter extends ViewpagerFragmentAdapter{
    //    这个是viewpager的填充视图
    private List<Fragment> fragments;
    //    这个是table导航条里面的内容填充
    private List<String> tabstrs;

    public ScoringPKVPAdapter(FragmentManager fm, List<Fragment> fragments, List<String> tabstrs) {
        super(fm,fragments);
        this.tabstrs=tabstrs;
        this.fragments=fragments;

    }


    @Override
    public int getCount() {
        return fragments.size();
    }


    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return tabstrs.get(position);

    }




}