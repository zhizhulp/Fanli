package com.ascba.rebate.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.beans.CashAccount;



import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by 李平 on 2017/4/19 0019.17:32
 * 白积分账单适配器
 */

public class BillAdapter extends BaseAdapter implements StickyListHeadersAdapter {
    private List<CashAccount> data;
    public BillAdapter(List<CashAccount> data) {
        this.data=data;
    }

    @Override
    public int getCount() {
        return data.size();
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
        ItemViewHolder holder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if(convertView==null){
            convertView = inflater.inflate(R.layout.wsaccount_list_item, parent, false);
            holder=new ItemViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder= (ItemViewHolder) convertView.getTag();
        }
        CashAccount ca = data.get(position);
        holder.tvDay.setText(ca.getDay());
        holder.tvTime.setText(ca.getTime());
        holder.tvMoney.setText(ca.getMoney());
        holder.tvType.setText(ca.getFilterText());
        holder.imTypeIcon.setImageResource(ca.getImgId());

        return convertView;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if(convertView==null){
            convertView = inflater.inflate(R.layout.white_bill_head, parent, false);
            holder=new HeaderViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder= (HeaderViewHolder) convertView.getTag();
        }
        CashAccount ca = data.get(position);
        if(position==0){
            holder.headMonth.setText(ca.getMonth());
            holder.type.setVisibility(View.VISIBLE);
            holder.imCalendar.setVisibility(View.VISIBLE);
            switch (ca.getType()){
                case ALL:
                    holder.type.setText("全部");
                    break;
                case AWARD:
                    holder.type.setText("奖励");
                    break;
                case COST:
                    holder.type.setText("消费");
                    break;
                case EXCHANGE:
                    holder.type.setText("兑换");
                    break;
            }

        }else {
            holder.headMonth.setText(ca.getMonth());
            holder.type.setVisibility(View.GONE);
            holder.imCalendar.setVisibility(View.GONE);
        }
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        String[] split = data.get(position).getDay().split("\\.");
        return split[1].hashCode();
    }

    /**
      * 缓存item的viewHolder
     */
    private class ItemViewHolder {
        TextView tvDay;
        TextView tvTime;
        TextView tvMoney;
        TextView tvType;
        ImageView imTypeIcon;

        private ItemViewHolder(View itemView) {
            tvDay = (TextView) itemView.findViewById(R.id.wsaccount_day);
            tvTime = (TextView) itemView.findViewById(R.id.wsaccount_time);
            tvMoney = (TextView) itemView.findViewById(R.id.wsaccount_money);
            tvType = (TextView) itemView.findViewById(R.id.wsaccount_category_txt);
            imTypeIcon = (ImageView) itemView.findViewById(R.id.wsaccount_category_icon);
        }
    }
    /**
     * 缓存head的viewHolder
     */
    private class HeaderViewHolder{
        TextView headMonth;
        ImageView imCalendar;
        TextView type;
        private HeaderViewHolder(View itemView) {
            headMonth = (TextView) itemView.findViewById(R.id.tv_month);
            imCalendar = (ImageView) itemView.findViewById(R.id.im_calendar);
            type = (TextView) itemView.findViewById(R.id.tv_desc);
        }
    }
}
