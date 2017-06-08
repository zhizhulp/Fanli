package com.ascba.rebate.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.ascba.rebate.R;
import com.ascba.rebate.beans.AcutionGoodsBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;
import java.util.List;

/**
 * Created by Administrator on 2017/6/8.
 * 竞拍确认订单
 */

public class AuctionConfirmOrderAdapter extends BaseQuickAdapter<AcutionGoodsBean,BaseViewHolder> {
    public AuctionConfirmOrderAdapter(@LayoutRes int layoutResId, @Nullable List<AcutionGoodsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AcutionGoodsBean item) {
        helper.setText(R.id.confir_order_title,item.getStartTimeStr());
        Picasso.with(mContext).load(item.getImgUrl()).placeholder(R.mipmap.loading_rect).into((ImageView) helper.getView(R.id.item_goods_img));
        helper.setText(R.id.item_goods_name,item.getName());
        helper.setText(R.id.item_goods_price,"￥"+ item.getPrice());
    }
}
