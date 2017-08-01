package com.ascba.rebate.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ascba.rebate.R;
import com.ascba.rebate.beans.Banner;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.R.attr.id;

/**
 * Created by 李平 on 2016/10/13.
 * banner adapter
 */

public class ShufflingViewPagerAdapter extends PagerAdapter {

    private Context mContext;
    private List mImageArr;
    private OnClick onClick;

    public void addOnClick(OnClick onClick) {
        this.onClick = onClick;
    }

    public ShufflingViewPagerAdapter(Context context, List imageArr) {
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
        position %= mImageArr.size();
        final Object o = mImageArr.get(position);
        if(o instanceof String){
            Picasso.with(mContext).load((String) o).placeholder(R.mipmap.banner_loading).error(R.mipmap.banner_loading).into(imageView);
        }else if(o instanceof Banner){
            Picasso.with(mContext).load(((Banner) o).getImg_url()).placeholder(R.mipmap.banner_loading).error(R.mipmap.banner_loading).into(imageView);
        }
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClick!=null){
                    onClick.onClickObject(o);
                }
            }
        });
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object view) {
        container.removeView((View) view);
    }

    public List getStringList() {
        return mImageArr;
    }

    interface OnClick {
        void onClickObject(Object o);
    }
}
