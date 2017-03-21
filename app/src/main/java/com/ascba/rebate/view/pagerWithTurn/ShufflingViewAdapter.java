package com.ascba.rebate.view.pagerWithTurn;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ascba.rebate.R;
import com.bumptech.glide.Glide;

import java.util.List;

import static com.alipay.sdk.app.statistic.c.s;

/**
 * Created by 李鹏 on 2016/10/13.
 */

public class ShufflingViewAdapter extends PagerAdapter {

    private Context mContext;
    private List<String> mImageArr;
    private OnClick onClick;
    private int id;

    public void addOnClick(OnClick onClick) {
        this.onClick = onClick;
    }

    public ShufflingViewAdapter(Context context, List<String> imageArr) {
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
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_video, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.item_video_img);
        Glide.with(mContext).load(s).into(imageView);

        position %= mImageArr.size();
        if (position < 0) {
            position = mImageArr.size() + position;
        }

        id=position;
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClick!=null){
                    onClick.OnClick(id);
                }
            }
        });

        Glide.with(mContext).load(mImageArr.get(position)).into(imageView);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object view) {
        container.removeView((View) view);
    }

    public List<String> getStringList() {
        return mImageArr;
    }


    public interface OnClick {
        void OnClick(int position);
    }

}
