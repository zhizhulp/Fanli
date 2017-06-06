package com.ascba.rebate.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.auction.GrabShootActivity;
import com.ascba.rebate.view.ImageViewDialog;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Administrator on 2017/6/5.
 * 竞拍商品头部大图适配器
 */

public class ImageAdapter extends PagerAdapter {
    private List<String> urls;

    public ImageAdapter(List<String> urls) {
        this.urls = urls;
    }

    @Override
    public int getCount() {
        return urls.size();
    }

    @Override
    public Object instantiateItem(final ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.goods_details_viewpager_item, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.goods_details_viewpager_item_img);
        TextView textView = (TextView) view.findViewById(R.id.goods_details_viewpager_item_text);
        Picasso.with(container.getContext()).load(urls.get(position)).placeholder(R.mipmap.loading_rect).error(R.mipmap.loading_rect).into(imageView);
        textView.setText((position+1) + "/" + (urls.size()));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ImageViewDialog(container.getContext(), urls);
            }
        });
        container.addView(view);
        return view;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
