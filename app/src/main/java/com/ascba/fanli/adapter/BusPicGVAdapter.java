package com.ascba.fanli.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.ascba.fanli.utils.ScreenDpiUtils;

/**
 * 商家照片适配器
 */

public class BusPicGVAdapter extends BaseAdapter {
    private int [] icon;
    private Context mContext;
    private LayoutInflater mInflater;
    public BusPicGVAdapter(int [] icon,Context mContext){
        this.icon=icon;
        mInflater=LayoutInflater.from(mContext);
        this.mContext=mContext;
    }
    @Override
    public int getCount() {
        return icon.length;
    }

    @Override
    public Object getItem(int position) {
        return icon[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView=new ImageView(mContext);
            ((ImageView) convertView).setImageResource(icon[position]);
            convertView.setLayoutParams(new AbsListView.LayoutParams(ScreenDpiUtils.dip2px(mContext,70),ScreenDpiUtils.dip2px(mContext,70)));
        }
        return convertView;
    }
}
