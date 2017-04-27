package com.ascba.rebate.adapter;


import android.content.Context;
import android.widget.ImageView;
import com.ascba.rebate.R;
import com.ascba.rebate.beans.CashAccount;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by 李平 on 2017/4/19 0019.17:32
 * 白积分账单适配器
 */

public class BillAdapter extends BaseMultiItemQuickAdapter<CashAccount, BaseViewHolder> {
    private Context context;

    public BillAdapter(List<CashAccount> data, Context context) {
        super(data);
        this.context = context;
        addItemType(0, R.layout.white_bill_head);
        addItemType(1, R.layout.wsaccount_list_item);
    }

    @Override
    protected void convert(BaseViewHolder helper, CashAccount item) {
        switch (helper.getItemViewType()) {
            case 0://头
                helper.setText(R.id.tv_month, item.getMonth());
                break;
            case 1://item
                helper.setText(R.id.wsaccount_day, item.getDay());
                helper.setText(R.id.wsaccount_time, item.getTime());
                helper.setText(R.id.wsaccount_money, item.getMoney());
                helper.setText(R.id.wsaccount_category_txt, item.getFilterText());
                Picasso.with(context).load(item.getImgUrl()).into((ImageView) helper.getView(R.id.wsaccount_category_icon));
                break;
        }
    }

}
