package com.ascba.rebate.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.ascba.rebate.R;
import com.ascba.rebate.beans.Card;
import java.util.List;

/**
 * 银行卡列表适配器
 */

public class CardListAdapter extends BaseAdapter {
    private List<Card> mList;
    private Context mContext;
    private LayoutInflater mInflater;

    public CardListAdapter(List<Card> mList, Context mContext) {
        this.mList = mList;
        this.mContext=mContext;
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
    public View getView(int position, View convertView, final ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.card_list_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        viewHolder= (ViewHolder) convertView.getTag();
        final Card card = mList.get(position);
        viewHolder.TVName.setText(card.getName());
        viewHolder.TVType.setText(card.getType());
        viewHolder.TVNumber.setText(getTail4(card.getNumber()));
        return convertView;
    }
    private String getTail4(String num){
        StringBuilder sb=new StringBuilder();
        sb.append("");
        if(num!=null){
            int length = num.length();
            for (int i = 0; i < length-4; i++) {
                sb.append("*");
            }
            sb.append(num.substring(length-4,length));
        }
        return sb.toString();
    }


    public class ViewHolder {
        TextView TVName;
        TextView TVType;
        TextView TVNumber;

        ViewHolder(View root) {
            if (TVName == null) {
                TVName = (TextView) root.findViewById(R.id.card_name);
            }
            if (TVType == null) {
                TVType = (TextView) root.findViewById(R.id.card_type);
            }
            if (TVNumber == null) {
                TVNumber = (TextView) root.findViewById(R.id.card_number);
            }
        }
    }
}
