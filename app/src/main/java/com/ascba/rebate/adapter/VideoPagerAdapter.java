package com.ascba.rebate.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.ascba.rebate.R;
import com.ascba.rebate.beans.VideoBean;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by 李鹏 on 2017/03/20 0020.
 */

public class VideoPagerAdapter extends PagerAdapter {

    private List<VideoBean> videoBean;
    private ImgOnClick imgOnClick;
    private ImageView imageView;
    private FrameLayout frameLayout;
    private Context context;

    public void setImgOnClick(ImgOnClick imgOnClick) {
        this.imgOnClick = imgOnClick;
    }

    public VideoPagerAdapter(List<VideoBean> videoBean, Context context) {
        this.videoBean = videoBean;
        this.context = context;
    }

    @Override
    public int getCount() {
        return videoBean.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(videoBean.get(position).getView());
        imgOnClick.onClick(null, null, position, null);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = videoBean.get(position).getView();
        container.addView(view);
        imageView = (ImageView) view.findViewById(R.id.item_video_img);
        Glide.with(context).load(videoBean.get(position).getImgUrl()).into(imageView);
        frameLayout = (FrameLayout) view.findViewById(R.id.item_video_fl);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgOnClick.onClick(v, imageView, position, frameLayout);
            }
        });

        return videoBean.get(position).getView();
    }


    public interface ImgOnClick {
        void onClick(View view, ImageView imageView, int position, FrameLayout frameLayout);
    }
}
