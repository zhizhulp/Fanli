package com.ascba.rebate.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by 李鹏 on 2017/03/20 0020.
 */

public class VideoPagerAdapter extends PagerAdapter {

    private List<View> viewList;

    public VideoPagerAdapter(List<View> drawables) {
        this.viewList = drawables;
    }

    @Override
    public int getCount() {
        return viewList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(viewList.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(viewList.get(position));
        return viewList.get(position);
    }

    public List<View> getListView() {
        return viewList;
    }
}
