package com.ascba.rebate.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.ascba.rebate.R;
import com.ascba.rebate.beans.CashAccount;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by 李平 on 2017/4/12 0012.18:10
 * 商家流水记录适配器
 */

public class FlowRecordsAdapter extends BaseQuickAdapter<CashAccount,BaseViewHolder> {
    private Context context;
    public FlowRecordsAdapter(int layoutResId, List<CashAccount> data, Context context) {
        super(layoutResId, data);
        this.context=context;
    }

    @Override
    protected void convert(BaseViewHolder helper, CashAccount item) {
        helper.setText(R.id.wsaccount_day, item.getDay());
        helper.setText(R.id.wsaccount_time, item.getTime());
        helper.setText(R.id.wsaccount_money, item.getMoney());
        helper.setText(R.id.wsaccount_category_txt, item.getFilterText());
        Picasso.with(context).load(item.getImgUrl()).into((ImageView) helper.getView(R.id.wsaccount_category_icon));
    }
}
