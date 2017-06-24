package com.ascba.rebate.activities.scoring.widget;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;

public class FmPagerAdapter extends PagerAdapter {
    private List<Fragment> fragmentList;
    private FragmentManager fm;

    public FmPagerAdapter(List<Fragment> fragmentList, FragmentManager fm) {
        this.fragmentList = fragmentList;
        this.fm = fm;
    }

    @Override
    public int getCount() {
        return fragmentList != null && !fragmentList.isEmpty() ? fragmentList.size() : 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = fragmentList.get(position);
        if (!fragment.isAdded()) {
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(fragment, fragment.getClass().getName());
            ft.commitAllowingStateLoss();
            fm.executePendingTransactions();
//            ft.commit();
        }

        View view = fragment.getView();
        if (view != null && view.getParent() == null) {
            container.addView(view);
        }
        return view;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(fragmentList.get(position).getView());
    }
}
