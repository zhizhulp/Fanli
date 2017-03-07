package com.ascba.rebate.adapter;

import com.ascba.rebate.beans.CartGoods;
import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 购物车列表实体类
 */

public class CartAdapter extends BaseSectionQuickAdapter<CartGoods,BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param layoutResId      The layout resource id of each item.
     * @param sectionHeadResId The section head layout id for each item
     * @param data             A new list is created out of this one to avoid mutable list
     */
    public CartAdapter(int layoutResId, int sectionHeadResId, List<CartGoods> data) {
        super(layoutResId, sectionHeadResId, data);
    }

    @Override
    protected void convertHead(BaseViewHolder helper, CartGoods item) {

    }

    @Override
    protected void convert(BaseViewHolder helper, CartGoods item) {

    }
}
