package com.ascba.rebate.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.ascba.rebate.R;
import com.ascba.rebate.beans.CartGoods;
import com.ascba.rebate.beans.Goods;
import com.ascba.rebate.view.cartBtn.AmountView;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 购物车列表实体类
 */

public class CartAdapter extends BaseSectionQuickAdapter<CartGoods, BaseViewHolder> {
    private Context context;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param layoutResId      The layout resource id of each item.
     * @param sectionHeadResId The section head layout id for each item
     * @param data             A new list is created out of this one to avoid mutable list
     */
    public CartAdapter(int layoutResId, int sectionHeadResId, List<CartGoods> data, Context context) {
        super(layoutResId, sectionHeadResId, data);
        this.context = context;
    }

    @Override
    protected void convertHead(BaseViewHolder helper, CartGoods item) {
        helper.setText(R.id.cart_cb_title, item.header);
    }

    @Override
    protected void convert(BaseViewHolder helper, CartGoods item) {
        ImageView view = helper.getView(R.id.cart_goods_pic);
        AmountView av = helper.getView(R.id.btn_amount_view);
        Goods goods = item.t;
        Glide.with(context).load(goods.getImgUrl()).into(view);
        helper.setText(R.id.cart_goods_title, goods.getGoodsTitle());
        helper.setText(R.id.cart_goods_standard, goods.getGoodsStandard());
        helper.setText(R.id.cart_price, goods.getGoodsPrice());
        av.setAmount(goods.getUserQuy());
    }

}
