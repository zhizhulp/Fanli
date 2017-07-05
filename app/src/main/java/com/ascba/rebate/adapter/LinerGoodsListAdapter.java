package com.ascba.rebate.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.ascba.rebate.R;
import com.ascba.rebate.beans.Goods;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by 李平 on 2017/7/1.
 * 商城商品列表适配器
 */

public class LinerGoodsListAdapter extends BaseQuickAdapter<Goods,BaseViewHolder> {
    public LinerGoodsListAdapter(@LayoutRes int layoutResId, @Nullable List<Goods> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Goods item) {
        Picasso.with(mContext).load(item.getImgUrl()).placeholder(R.mipmap.shop_goods_loading).into((ImageView) helper.getView(R.id.goods_list_img));
        helper.setText(R.id.goods_list_name, item.getGoodsTitle());
        helper.setText(R.id.goods_list_price, item.getGoodsPrice());
        helper.addOnClickListener(R.id.goods_list_cart);
    }
}
