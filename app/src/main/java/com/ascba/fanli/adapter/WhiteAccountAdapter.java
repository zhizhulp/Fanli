package com.ascba.fanli.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascba.fanli.R;
import com.ascba.fanli.beans.WhiteAccount;

import java.util.List;

/**
 * Created by Administrator on 2016/11/1.
 */

public class WhiteAccountAdapter extends BaseAdapter {
    private List<WhiteAccount> mList;
    private Context mContext;
    private LayoutInflater mInflater;

    public WhiteAccountAdapter(Context mContext, List<WhiteAccount> mList) {
        this.mList = mList;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mList==null? 0 :mList.size();
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
            convertView= mInflater.inflate(R.layout.wsaccount_list_item,null);
            viewHolder= new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        viewHolder= (ViewHolder) convertView.getTag();
        WhiteAccount whiteAccount = mList.get(position);
        viewHolder.day.setText(whiteAccount.getDay());
        viewHolder.time.setText(whiteAccount.getTime());
        //判断商家类别设置不同图标
        viewHolder.money.setText(whiteAccount.getMoney());
        viewHolder.category.setText(whiteAccount.getFilterText());
        return convertView;
    }
    class ViewHolder{
        TextView day;
        TextView time;
        ImageView icon;
        TextView money;
        TextView category;
        public ViewHolder(View root){
            day= (TextView) root.findViewById(R.id.wsaccount_day);
            time= (TextView) root.findViewById(R.id.wsaccount_time);
            icon= (ImageView) root.findViewById(R.id.wsaccount_category_icon);
            money= (TextView) root.findViewById(R.id.wsaccount_money);
            category= (TextView) root.findViewById(R.id.wsaccount_category_txt);
        }
    }
}
