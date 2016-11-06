package com.ascba.fanli.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.ascba.fanli.R;
import com.ascba.fanli.beans.Card;

import java.util.HashMap;
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
        final int index = position;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.card_list_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) convertView.getTag();
        final Card card = mList.get(index);
        viewHolder.TVName.setText(card.getName());
        viewHolder.TVType.setText(card.getType());
        viewHolder.TVNumber.setText(card.getNumber());
        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mList.get(index).setSelect(true);
                } else {
                    mList.get(index).setSelect(false);
                }
            }
        });
        if (card.isSelect() ){
            viewHolder.checkBox.setChecked(true);
        }else{
            viewHolder.checkBox.setChecked(false);
        }
        return convertView;
    }

    class ViewHolder {
        TextView TVName;
        TextView TVType;
        TextView TVNumber;
        CheckBox checkBox;

        public ViewHolder(View root) {
            if (TVName == null) {
                TVName = (TextView) root.findViewById(R.id.card_name);
            }
            if (TVType == null) {
                TVType = (TextView) root.findViewById(R.id.card_type);
            }
            if (TVNumber == null) {
                TVNumber = (TextView) root.findViewById(R.id.card_number);
            }
            if (checkBox == null) {
                checkBox = (CheckBox) root.findViewById(R.id.cb);
            }
        }
    }
}
