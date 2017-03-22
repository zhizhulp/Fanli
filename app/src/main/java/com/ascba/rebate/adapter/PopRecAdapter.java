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
    private Callback callback;
    public PopRecAdapter(List<RecType> data, Context context) {
        this.data=data;
        this.inflater=LayoutInflater.from(context);
    }
    public interface Callback{
        void clickItem(int position,View view);
    }

    public Callback getCallback() {
        return callback;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView=inflater.inflate(R.layout.pop_rec_list_item,null);
        }
        final RadioButton rb = (RadioButton) convertView.findViewById(R.id.pop_rb);
        final RecType r = data.get(position);
        rb.setSelected(r.isSelect());
        rb.setText(r.getContent());
        rb.setTextSize(r.isSelect()? 14 : 13);

        View view = convertView.findViewById(R.id.lat_rec);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecType recType = data.get(position);
                recType.setSelect(true);
                for (int i = 0; i < data.size(); i++) {
                    RecType recType1 = data.get(i);
                    if(recType1!=recType){
                        recType1.setSelect(false);
                    }
                }
                notifyDataSetChanged();
                if(callback!=null){
                    callback.clickItem(position,v);
                }
            }
        });


        /*rb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
        return convertView;
    }
}
