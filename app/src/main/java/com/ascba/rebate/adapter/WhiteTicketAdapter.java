package com.ascba.rebate.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.beans.WhiteTicket;

import java.util.List;

/**
 * Created by Administrator on 2016/12/16.
 */

public class WhiteTicketAdapter extends BaseAdapter {
    private List<WhiteTicket> list;
    private LayoutInflater layoutInflater;
    private Context context;
    private Callback callback;
    public interface Callback{
        void onExchangeClick(int position);
    }

    public Callback getCallback() {
        return callback;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public WhiteTicketAdapter(List<WhiteTicket> list, Context context) {
        this.list = list;
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public int getCount() {
        return list==null?0:list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView=layoutInflater.inflate(R.layout.white_ticket_list_item,null);
        }
        ((TextView) convertView.findViewById(R.id.money)).setText(list.get(position).getMoney());
        convertView.findViewById(R.id.click_to_exchange).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(callback!=null){
                    callback.onExchangeClick(position);
                }
            }
        });

        return convertView;
    }
}
