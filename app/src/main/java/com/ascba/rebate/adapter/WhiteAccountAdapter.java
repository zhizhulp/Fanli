package com.ascba.rebate.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.ascba.rebate.R;
import com.ascba.rebate.beans.CashAccount;
import com.ascba.rebate.beans.CashAccountType;
import java.util.List;

/**
 * 现金账单适配器
 */

public class WhiteAccountAdapter extends BaseAdapter {
    private List<CashAccount> mList;
    private LayoutInflater mInflater;
    private int[] aa=new int[]{R.mipmap.cash_employee,
            R.mipmap.cash_cash_get,R.mipmap.cash_exchange,
            R.mipmap.cash_recharge,R.mipmap.cash_cost};

    public WhiteAccountAdapter(Context mContext, List<CashAccount> mList) {
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
        CashAccount cashAccount = mList.get(position);
        viewHolder.day.setText(cashAccount.getDay());
        viewHolder.time.setText(cashAccount.getTime());
        //判断商家类别设置不同图标
        CashAccountType type = cashAccount.getType();
        switch (type){
            case EMPLOYEE:
                viewHolder.icon.setImageResource(aa[0]);
                break;
            case CASH_GET:
                viewHolder.icon.setImageResource(aa[1]);
                viewHolder.cashStatus.setText(cashAccount.getStatus());
                break;
            case EXCHANGE:
                viewHolder.icon.setImageResource(aa[2]);
                break;
            case RECHARGE:
                viewHolder.icon.setImageResource(aa[3]);
                break;
            case COST:
                viewHolder.icon.setImageResource(aa[4]);
                break;

        }
        viewHolder.money.setText(cashAccount.getMoney());
        viewHolder.category.setText(cashAccount.getFilterText());
        viewHolder.cashStatus.setText(cashAccount.getStatus()==null?"":cashAccount.getStatus());
        return convertView;
    }
    private class ViewHolder{
        TextView day;
        TextView time;
        ImageView icon;
        TextView money;
        TextView category;
        TextView cashStatus;
        ViewHolder(View root){
            day= (TextView) root.findViewById(R.id.wsaccount_day);
            time= (TextView) root.findViewById(R.id.wsaccount_time);
            icon= (ImageView) root.findViewById(R.id.wsaccount_category_icon);
            money= (TextView) root.findViewById(R.id.wsaccount_money);
            category= (TextView) root.findViewById(R.id.wsaccount_category_txt);
            cashStatus=(TextView) root.findViewById(R.id.tv_cash_get_status);
        }
    }
}
