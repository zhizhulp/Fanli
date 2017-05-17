package com.ascba.rebate.view.dropDownMultiPager;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by zhaoyong
 * on 2016/8/2.
 */
public class DropDownMultiPagerAdapter extends PagerAdapter {

    private List<View> list;
    private OnDropDownMultiPagerViewItemClick onDropDownMultiPagerViewItemClick;
    public void setOnDropDownMultiPagerViewItemClick(OnDropDownMultiPagerViewItemClick onDropDownMultiPagerViewItemClick) {
        this.onDropDownMultiPagerViewItemClick = onDropDownMultiPagerViewItemClick;
    }
    public interface OnDropDownMultiPagerViewItemClick {
        void onItemClick(int position);
    }

    public DropDownMultiPagerAdapter(List<View> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        container.addView(list.get(position), 0);
        View view = list.get(position);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onDropDownMultiPagerViewItemClick!=null){
                    onDropDownMultiPagerViewItemClick.onItemClick(position);
                }
            }
        });
        return list.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(list.get(position));
    }

}
