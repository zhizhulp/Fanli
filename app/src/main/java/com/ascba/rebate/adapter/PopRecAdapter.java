package com.ascba.rebate.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;

import com.ascba.rebate.R;
import com.ascba.rebate.beans.RecType;

import java.util.List;

/**
 * 推荐popupwindow适配器
 */

public class PopRecAdapter extends BaseAdapter {
    private List<RecType> data;
    private LayoutInflater inflater;
    public PopRecAdapter(List<RecType> data, Context context) {
        this.data=data;
        this.inflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data==null? 0 : data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView=inflater.inflate(R.layout.pop_rec_list_item,null);
        }
        RadioButton rb = (RadioButton) convertView.findViewById(R.id.pop_rb);
        RecType r = data.get(position);
        rb.setSelected(r.isSelect());
        rb.setText(r.getContent());
        rb.setTextSize(r.isSelect()? 14 : 13);
        return convertView;
    }
}
