package com.ascba.rebate.adapter;

import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.ascba.rebate.R;
import com.ascba.rebate.beans.WhiteTicket;

import java.text.SimpleDateFormat;
import java.util.Date;
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
        ViewHolder viewHolder;
        if(convertView==null){
            convertView=layoutInflater.inflate(R.layout.white_ticket_list_item,null);
            viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        viewHolder= (ViewHolder) convertView.getTag();
        WhiteTicket wt = list.get(position);
        viewHolder.tvMoney.setText(wt.getMoney());
        if(wt.getStatus()==0){//冻结状态
            viewHolder.imFron.setImageResource(R.mipmap.white_ticket_bg_front_over);
            viewHolder.imCha.setImageResource(R.mipmap.white_ticket_bg_exchange_over);
            viewHolder.imTail.setImageResource(R.mipmap.white_ticket_bg_tail_over);
            viewHolder.tvMoney.setTextColor(context.getResources().getColor(R.color.text_deep_gray));
            viewHolder.tvCha.setEnabled(false);
            viewHolder.tvString.setTextColor(context.getResources().getColor(R.color.text_deep_gray));
            viewHolder.tvString.setBackgroundDrawable((context.getResources().getDrawable(R.drawable.white_ticket_9_over)));
            viewHolder.tvCreTime.setText("获得时间："+wt.getTime());
            viewHolder.tvLefTime.setVisibility(View.VISIBLE);
            viewHolder.tvLefTime.setText("距使用时间："+wt.getLeftTime());
        }else if(wt.getStatus()==1){//可以使用\
            viewHolder.tvCha.setEnabled(true);
            viewHolder.imFron.setImageResource(R.mipmap.white_ticket_bg_front);
            viewHolder.imCha.setImageResource(R.mipmap.white_ticket_bg_exchange);
            if(wt.getTest()==1){//测试
                viewHolder.imTail.setImageResource(R.mipmap.white_ticket_bg_no_active_over);
            }else {
                viewHolder.imTail.setImageResource(R.mipmap.white_ticket_bg_tail);
            }
            viewHolder.tvMoney.setTextColor(context.getResources().getColor(R.color.white2cash_color));
            viewHolder.tvString.setTextColor(context.getResources().getColor(R.color.white2cash_color));
            viewHolder.tvString.setBackgroundDrawable((context.getResources().getDrawable(R.drawable.white_ticket_9)));
            viewHolder.tvCreTime.setText("获得时间："+wt.getTime());
            viewHolder.tvLefTime.setVisibility(View.GONE);
            viewHolder.tvCha.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("tvCha", "onClick: ");

                    if(callback!=null){
                        callback.onExchangeClick(position);
                    }
                }
            });

        }

        return convertView;
    }
    private class ViewHolder{
        TextView tvMoney;
        TextView tvCreTime;
        TextView tvLefTime;
        TextView tvString;
        ImageView imFron;
        ImageView imCha;
        ImageView imTail;
        View tvCha;
        ViewHolder(View root){
            tvMoney= (TextView) root.findViewById(R.id.money);
            tvCreTime= (TextView) root.findViewById(R.id.tv_create_time);
            tvLefTime= (TextView) root.findViewById(R.id.tv_left_time);
            tvCha= root.findViewById(R.id.click_to_exchange);
            tvString= (TextView) root.findViewById(R.id.tv_cha_at_once);
            imFron=((ImageView) root.findViewById(R.id.im_front));
            imCha=((ImageView) root.findViewById(R.id.im_cha));
            imTail=((ImageView) root.findViewById(R.id.im_tail));
        }
    }

}
