package com.ascba.rebate.adapter.sweep;

import android.content.Context;

import com.ascba.rebate.R;
import com.ascba.rebate.beans.sweep.MyAllOrderEntity;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by lenovo on 2017/7/3.
 */

public class MyAllOrderAdapter extends BaseMultiItemQuickAdapter<MyAllOrderEntity,BaseViewHolder> {
private Context context;


    public MyAllOrderAdapter(List<MyAllOrderEntity> data,Context context) {
        super(data);
        this.context = context;
        addItemType(0, R.layout.white_bill_head);
        addItemType(1, R.layout.item_myallorders);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyAllOrderEntity item) {
        switch (helper.getItemViewType()) {
            case 0://头
               // helper.setText(R.id.tv_month, item.getMonth());
                break;
            case 1://item
//                helper.setText(R.id.myallorders_day,item.getCreate_time());
//                helper.setText(R.id.myallorders_time,item.getCreate_time());
//                helper.setText(R.id.myallorders_icon,item.getCreate_time());
//
//                helper.setText(R.id.wsaccount_day, item.getDay());
//                helper.setText(R.id.wsaccount_time, item.getTime());
//                helper.setText(R.id.wsaccount_money, item.getMoney());
//                helper.setText(R.id.wsaccount_category_txt, item.getFilterText());
//                Picasso.with(context).load(item.getImgUrl()).into((ImageView) helper.getView(R.id.wsaccount_category_icon));
                break;
        }
    }
}
