package com.ascba.rebate.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.beans.Business;
import com.ascba.rebate.beans.FirstRec;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 推广适配器
 */

public class TuiGAdapter extends BaseQuickAdapter<FirstRec,BaseViewHolder> {


    public TuiGAdapter(List<FirstRec> data,int layoutResId) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FirstRec item) {
        helper.setText(R.id.first_rec_name,item.getName());
        helper.setText(R.id.first_rec_num,item.getGroupName());
        helper.setText(R.id.first_rec_money,item.getMoney());
        helper.setText(R.id.first_rec_time,item.getTime());
    }

}
