package com.ascba.rebate.view.pagerWithTurn;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Kevin on 2016/10/13.
 */

public class ShufflingViewPagerAdapter extends PagerAdapter {

    private Context mContext;
    private List<String> mImageArr;

    public ShufflingViewPagerAdapter(Context context, List<String> imageArr) {
        this.mContext = context;
        this.mImageArr = imageArr;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(layoutParams);
        position %= mImageArr.size();
        if (position < 0) {
            position = mImageArr.size() + position;
        }
        Glide.with(mContext).load(mImageArr.get(position)).into(imageView);
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object view) {
        container.removeView((View) view);
    }

    public List<String> getStringList() {
        return mImageArr;
    }

}
