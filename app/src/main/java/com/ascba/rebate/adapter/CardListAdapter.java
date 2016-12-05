package com.ascba.rebate.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.beans.Card;
import com.ascba.rebate.view.deletelistview.ListViewCompat;
import com.ascba.rebate.view.deletelistview.SlideView;

import java.util.List;

/**
 * 银行卡列表适配器
 */

public class CardListAdapter extends BaseAdapter {
    private List<Card> mList;
    private Context mContext;
    private LayoutInflater mInflater;
    public SlideView mLastSlideViewWithStatusOn;


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
        SlideView slideView = (SlideView) convertView;
        if (slideView == null) {
            View itemView = mInflater.inflate(R.layout.card_list_item, null);

            slideView = new SlideView(mContext);
            slideView.setContentView(itemView);

            viewHolder = new ViewHolder(slideView);
            slideView.setOnSlideListener(new SlideView.OnSlideListener() {
                @Override
                public void onSlide(View view, int status) {
                    if (mLastSlideViewWithStatusOn != null && mLastSlideViewWithStatusOn != view) {
                        mLastSlideViewWithStatusOn.shrink();
                    }

                    if (status == SLIDE_STATUS_ON) {
                        mLastSlideViewWithStatusOn = (SlideView) view;
                    }
                }
            });
            slideView.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) slideView.getTag();
        final Card card = mList.get(position);
        card.slideView=slideView;
        card.slideView.reset();
        viewHolder.TVName.setText(card.getName());
        viewHolder.TVType.setText(card.getType());
        viewHolder.TVNumber.setText(card.getNumber());
        return slideView;
    }


    public class ViewHolder {
        TextView TVName;
        TextView TVType;
        TextView TVNumber;
        CheckBox checkBox;
        public ViewGroup rightHolder;

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
            if (checkBox == null) {
                checkBox = (CheckBox) root.findViewById(R.id.cb);
            }
            if (rightHolder == null) {
                rightHolder= (ViewGroup)root.findViewById(R.id.right_holder);
            }
        }
    }
}
