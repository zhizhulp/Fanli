package com.ascba.rebate.adapter;

import com.ascba.rebate.R;
import com.ascba.rebate.beans.CashAccount;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by 李平 on 2017/4/12 0012.18:10
 * 商家流水记录适配器
 */

public class FlowRecordsAdapter extends BaseQuickAdapter<CashAccount,BaseViewHolder> {
    public FlowRecordsAdapter(int layoutResId, List<CashAccount> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CashAccount item) {
        helper.setText(R.id.wsaccount_day, item.getDay());
        helper.setText(R.id.wsaccount_time, item.getTime());
        helper.setText(R.id.wsaccount_money, item.getMoney());
        helper.setText(R.id.wsaccount_category_txt, item.getFilterText());
        helper.setImageResource(R.id.wsaccount_category_icon,item.getImgId());
    }
}
