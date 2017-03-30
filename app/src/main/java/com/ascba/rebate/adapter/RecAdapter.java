package com.ascba.rebate.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.beans.FirstRec;

import java.util.List;

/**
 * 奖励适配器
 */

public class RecAdapter extends BaseAdapter {
    private List<FirstRec> mList;
    private LayoutInflater mInflater;

    public RecAdapter(List<FirstRec> mList, Context mContext) {
        this.mList = mList;
        this.mInflater = LayoutInflater.from(mContext);
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
            convertView= mInflater.inflate(R.layout.rec_list_item,null);
            viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        viewHolder= (ViewHolder) convertView.getTag();
        FirstRec firstRec = mList.get(position);
        viewHolder.tvName.setText("推荐-"+firstRec.getName()+"("+ firstRec.getGroupName()+")");
        //viewHolder.tvNum.setText(firstRec.getRecNum());
        viewHolder.tvMoney.setText("+"+firstRec.getMoney()+"元兑现券");
        viewHolder.tvTime.setText(firstRec.getTime());
        //viewHolder.imType.setImageResource(firstRec.getTypeIcon());
        return convertView;
    }
    private class ViewHolder{
        TextView tvName;
        //ImageView imType;
        //TextView tvNum;
        TextView tvMoney;
        TextView tvTime;
        public ViewHolder(View root){
            if(tvName==null){
                tvName= (TextView) root.findViewById(R.id.first_rec_name);
            }
            /*if(imType==null){
                imType= (ImageView) root.findViewById(R.id.first_rec_type);
            }*/
            /*if(tvNum==null){
                tvNum= (TextView) root.findViewById(R.id.first_rec_num);
            }*/
            if(tvMoney==null){
                tvMoney= (TextView) root.findViewById(R.id.first_rec_money);
            }
            if(tvTime==null){
                tvTime= (TextView) root.findViewById(R.id.first_rec_time);
            }
        }
    }
}
