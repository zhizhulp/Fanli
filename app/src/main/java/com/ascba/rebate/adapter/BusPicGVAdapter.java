package com.ascba.rebate.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.ascba.rebate.utils.ScreenDpiUtils;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * 商家照片适配器
 */

public class BusPicGVAdapter extends BaseAdapter {
    private List<String> urls;
    private Context mContext;
    private LayoutInflater mInflater;
    public BusPicGVAdapter(List<String> urls,Context mContext){
        this.urls=urls;
        mInflater=LayoutInflater.from(mContext);
        this.mContext=mContext;
    }
    @Override
    public int getCount() {
        return urls.size();
    }

    @Override
    public Object getItem(int position) {
        return urls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView=new ImageView(mContext);
            Picasso.with(mContext).load(urls.get(position)).memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE).into((ImageView) convertView);
            convertView.setLayoutParams(new FrameLayout.LayoutParams(ScreenDpiUtils.dip2px(mContext,70),ScreenDpiUtils.dip2px(mContext,70)));
        }
        return convertView;
    }
}
