package com.ascba.rebate.adapter;

import com.ascba.rebate.R;
import com.ascba.rebate.beans.CashAccount;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by 李鹏 on 2017/03/30 0030.
 * 收支记录
 */

public class InOutComeAdapter extends BaseQuickAdapter<CashAccount, BaseViewHolder> {

    public InOutComeAdapter(int layoutResId, List<CashAccount> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CashAccount item) {
        helper.setText(R.id.wsaccount_day, item.getDay());
        helper.setText(R.id.wsaccount_time, item.getTime());
        helper.setText(R.id.wsaccount_money, item.getMoney());
        helper.setText(R.id.wsaccount_category_txt, item.getFilterText());
        helper.setText(R.id.tv_cash_get_status, item.getStatus());
        helper.setImageResource(R.id.wsaccount_category_icon,item.getImgId());
    }
}
