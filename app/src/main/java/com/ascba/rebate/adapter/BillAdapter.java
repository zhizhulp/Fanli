package com.ascba.rebate.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.beans.CashAccount;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;


import java.util.List;

/**
 * Created by 李平 on 2017/4/19 0019.17:32
 * 白积分账单适配器
 */

public class BillAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder> {
    private List<CashAccount> data;
    public BillAdapter(List<CashAccount> data) {
        this.data=data;
    }
    //item
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.wsaccount_list_item, parent, false);
        return new ItemViewHolder(v);
    }
    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder ivh = (ItemViewHolder) holder;
        CashAccount ca = data.get(position);
        ivh.tvDay.setText(ca.getDay());
        ivh.tvTime.setText(ca.getTime());
        ivh.tvMoney.setText(ca.getMoney());
        ivh.tvType.setText(ca.getFilterText());
        ivh.imTypeIcon.setImageResource(ca.getImgId());
    }

    //item
    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.white_bill_head, parent, false);
        return new HeaderViewHolder(v);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        CashAccount ca = data.get(position);
        HeaderViewHolder hvh = (HeaderViewHolder) holder;
        hvh.headMonth.setText(ca.getMonth());
        //hvh.imCalendar.setVisibility(s.isHasCalendar() ? View.VISIBLE : View.GONE);
    }

    @Override
    public long getHeaderId(int position) {
        String[] split = data.get(position).getDay().split("\\.");
        return split[1].hashCode();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public long getItemId(int position) {
        return data.get(position).getDay().hashCode();
    }

    /**
     * 缓存item的viewHolder
     */
    private class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvDay;
        TextView tvTime;
        TextView tvMoney;
        TextView tvType;
        ImageView imTypeIcon;

        private ItemViewHolder(View itemView) {
            super(itemView);
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
    private class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView headMonth;
        ImageView imCalendar;
        private HeaderViewHolder(View itemView) {
            super(itemView);
            headMonth = (TextView) itemView.findViewById(R.id.tv_month);
            imCalendar = (ImageView) itemView.findViewById(R.id.im_calendar);
        }
    }
}
