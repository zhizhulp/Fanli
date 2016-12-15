package com.ascba.rebate.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.beans.Ticket;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/11/27.
 */

public class TicketAdapter2 extends BaseAdapter {
    private List<Ticket> mList;
    private Context context;
    private LayoutInflater mInflater;

    public TicketAdapter2(List<Ticket> mList, Context context) {
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
            convertView=mInflater.inflate(R.layout.ticket_list_item_update,null);
            viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        viewHolder= (ViewHolder) convertView.getTag();
        Ticket ticket = mList.get(position);
        int state = ticket.getState();
        if(state==0){//没过期
            viewHolder.bg.setBackgroundDrawable(context.getResources().getDrawable(R.mipmap.ticket_bg_about));
            viewHolder.imFront.setBackgroundDrawable(context.getResources().getDrawable(R.mipmap.ticket_front_normal));
            viewHolder.imSymbol.setBackgroundDrawable(context.getResources().getDrawable(R.mipmap.ticket_money_symbol_normal));
        }else if(state==1){//过期
            viewHolder.bg.setBackgroundDrawable(context.getResources().getDrawable(R.mipmap.ticket_bg_normal));
            viewHolder.imFront.setBackgroundDrawable(context.getResources().getDrawable(R.mipmap.ticket_front_normal));
            viewHolder.imSymbol.setBackgroundDrawable(context.getResources().getDrawable(R.mipmap.ticket_money_symbol_normal));
        }else if(state==2){//快过期
            viewHolder.bg.setBackgroundDrawable(context.getResources().getDrawable(R.mipmap.ticket_bg_over));
            viewHolder.imFront.setBackgroundDrawable(context.getResources().getDrawable(R.mipmap.ticket_front_over));
            viewHolder.imSymbol.setBackgroundDrawable(context.getResources().getDrawable(R.mipmap.ticket_money_symbol_over));
        }
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy.MM.dd");
        String startTimeString = sdf.format(new Date(ticket.getStartTime()*1000));
        String endTimeString = sdf.format(new Date(ticket.getEndTime()*1000));

        viewHolder.tvTime.setText("有效期 "+startTimeString+"-"+endTimeString);
        viewHolder.tvMoney.setText(ticket.getMoney()+"");
        return convertView;
    }




    class ViewHolder{

        TextView tvTime;
        TextView tvMoney;
        ImageView imFront;
        ImageView imSymbol;
        View bg;
        public ViewHolder(View root){

            if(tvTime==null){
                tvTime= (TextView) root.findViewById(R.id.ticket_time);
            }
            if(tvMoney==null){
                tvMoney= (TextView) root.findViewById(R.id.ticket_money);
            }
            if(imFront==null){
                imFront= (ImageView) root.findViewById(R.id.ticket_front_bg_id);
            }
            if(imSymbol==null){
                imSymbol= (ImageView) root.findViewById(R.id.ticket_money_symbol_id);
            }
            if(bg==null){
                bg=root.findViewById(R.id.ticket_bg_id);
            }
        }
    }
}
