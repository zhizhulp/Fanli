package com.ascba.rebate.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.ascba.rebate.beans.TittleBean;

import java.util.List;

/**
 * Created by 李鹏 on 2017/5/23.
 */

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentList;//fragment列表
    private List<TittleBean> titleList;//tab名的列表

    public MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragmentList, List<TittleBean> titleList) {
        super(fm, fragmentList);
        if (fragmentList.size() != titleList.size()) {
            try {
                throw new Exception("fragmentList.size must equals stringList.size!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.fragmentList = fragmentList;
        this.titleList = titleList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return titleList.size();
    }

    //此方法用来显示tab上的名字
    @Override
    public CharSequence getPageTitle(int position) {
        int newPosition = position % titleList.size();
        TittleBean tittleBean = titleList.get(newPosition);
        return tittleBean.getNowTime()+"\n"+tittleBean.getStatus();
    }
}
