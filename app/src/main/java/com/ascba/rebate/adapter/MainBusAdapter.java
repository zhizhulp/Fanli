package com.ascba.rebate.adapter;

import android.widget.ImageView;

import com.ascba.rebate.R;
import com.ascba.rebate.beans.Business;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * 线下商家实体类
 */

public class MainBusAdapter extends BaseQuickAdapter<Business,BaseViewHolder> {
    public MainBusAdapter(int layoutResId, List<Business> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Business item) {
        Picasso.with(mContext).load(item.getLogo())/*.memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .networkPolicy(NetworkPolicy.NO_CACHE)*/.into((ImageView) helper.getView(R.id.iv_main_business_logo));
        helper.setText(R.id.tv_main_business_name,item.getbName());
        helper.setText(R.id.tv_main_business_category,item.getbCategory());
        helper.setText(R.id.tv_main_business_goodjob,item.getGoodComm());
        helper.setText(R.id.tv_main_business_distance,item.getDistance());
    }
}
