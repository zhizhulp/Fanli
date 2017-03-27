package com.ascba.rebate.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.ascba.rebate.R;
import com.ascba.rebate.beans.Business;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 周边列表适配器
 */

public class BusAdapter extends BaseQuickAdapter<Business,BaseViewHolder> {
    private Context context;
    public BusAdapter(int layoutResId, List<Business> data, Context context) {
        super(layoutResId, data);
        this.context=context;
    }

    @Override
    protected void convert(BaseViewHolder helper, Business item) {
        Glide.with(context).load(item.getLogo()).error(R.mipmap.busi_loading).placeholder(R.mipmap.busi_loading).into((ImageView) helper.getView(R.id.iv_main_business_logo));
        helper.setText(R.id.tv_main_business_name,item.getbName());
       // helper.setText(R.id.tv_main_business_category,item.getbCategory());
        helper.setText(R.id.tv_main_business_goodjob,item.getGoodComm());
        helper.setText(R.id.tv_main_business_distance,item.getDistance());
    }
}
