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
 * Created by Administrator on 2016/12/20 0020.
 */

public class BankAdapter extends BaseAdapter {
    private List<Card> mList;
    private LayoutInflater inflater;

    public BankAdapter(Context context, List<Card> mList) {
        inflater=LayoutInflater.from(context);
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList==null?0:mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null){
            view=inflater.inflate(R.layout.textview,null);
        }
        Card card=mList.get(i);
        String tail4Num = getTail4Num(card.getNumber());
        ((TextView) view).setText(card.getName()+"("+ tail4Num+")");
        return view;
    }
    private String getTail4Num(String bank_card) {

        return  bank_card.substring(bank_card.length()-4,bank_card.length());

    }
}
