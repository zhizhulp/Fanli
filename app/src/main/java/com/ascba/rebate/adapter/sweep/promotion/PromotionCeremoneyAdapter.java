package com.ascba.rebate.adapter.sweep.promotion;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.ascba.rebate.R;
import com.ascba.rebate.beans.sweep.promotion_ceremony.PromotionCeremoneyEntity;
import com.ascba.rebate.utils.UrlUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by lenovo on 2017/7/12.
 */

public class PromotionCeremoneyAdapter extends BaseQuickAdapter<PromotionCeremoneyEntity.RefereeListBean, BaseViewHolder> {
    public PromotionCeremoneyAdapter(@LayoutRes int layoutResId, @Nullable List<PromotionCeremoneyEntity.RefereeListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PromotionCeremoneyEntity.RefereeListBean item) {

        ImageView iv = helper.getView(R.id.item_promotion_iv);
        Picasso.with(mContext).load(UrlUtils.baseWebsite + item.getAvatar()).placeholder(R.mipmap.new_man).into(iv);
        helper.setText(R.id.item_promotion_tv_name, item.getMobile());
        helper.setText(R.id.item_promotion_content,item.getRemarks());//完成订单否



    }
}
