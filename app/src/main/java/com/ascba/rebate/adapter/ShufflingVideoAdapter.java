package com.ascba.rebate.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.beans.VideoBean;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by 李鹏 on 2016/10/13.
 */

public class ShufflingVideoAdapter extends PagerAdapter {

    private Context mContext;
    private List<VideoBean> beanList;
    private OnClick onClick;
    private int id;

    public void addOnClick(OnClick onClick) {
        this.onClick = onClick;
    }

    public ShufflingVideoAdapter(Context context, List<VideoBean> beanList) {
        this.mContext = context;
        this.beanList = beanList;
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

        TextView title = (TextView) view.findViewById(R.id.item_video_title);

        position %= beanList.size();
        if (position < 0) {
            position = beanList.size() + position;
        }

        id = position;
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClick != null) {
                    onClick.OnClick(id);
                }
            }
        });
        Picasso.with(mContext).load(beanList.get(position).getImgUrl()).placeholder(R.mipmap.loading_rect).error(R.mipmap.loading_rect).into(imageView);
        title.setText(beanList.get(position).getImgTitle());
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object view) {
        container.removeView((View) view);
    }

    public List<VideoBean> getStringList() {
        return beanList;
    }


    public interface OnClick {
        void OnClick(int position);
    }

}
