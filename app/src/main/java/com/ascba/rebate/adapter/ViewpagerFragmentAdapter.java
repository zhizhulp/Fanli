package com.ascba.rebate.adapter;

import android.support.v4.app.*;

import java.util.List;

/**
 * Created by 李鹏 on 2017/03/15 0015.
 */

public class ViewpagerFragmentAdapter extends android.support.v4.app.FragmentPagerAdapter {

    private List<Fragment> fragmentList;

    public ViewpagerFragmentAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

}
