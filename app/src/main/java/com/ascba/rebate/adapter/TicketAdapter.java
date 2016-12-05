package com.ascba.rebate.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.beans.Ticket;

import java.util.List;

/**
 * Created by Administrator on 2016/11/27.
 */

public class TicketAdapter extends BaseAdapter {
    private List<Ticket> mList;
    private Context context;
    private LayoutInflater mInflater;

    public TicketAdapter(List<Ticket> mList, Context context) {
        this.mList = mList;
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null){
            convertView=mInflater.inflate(R.layout.ticket_list_item,null);
            viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        viewHolder= (ViewHolder) convertView.getTag();
        Ticket ticket = mList.get(position);
        viewHolder.tvType.setText(ticket.getType());
        viewHolder.tvTime.setText(ticket.getTime()+"");
        viewHolder.tvMoney.setText(ticket.getMoney()+"");
        return convertView;
    }

    class ViewHolder{
        TextView tvType;
        TextView tvTime;
        TextView tvMoney;
        public ViewHolder(View root){
            if(tvType==null){
                tvType= (TextView) root.findViewById(R.id.ticket_type);
            }
            if(tvTime==null){
                tvTime= (TextView) root.findViewById(R.id.ticket_time);
            }
            if(tvMoney==null){
                tvMoney= (TextView) root.findViewById(R.id.ticket_money);
            }
        }
    }
}
