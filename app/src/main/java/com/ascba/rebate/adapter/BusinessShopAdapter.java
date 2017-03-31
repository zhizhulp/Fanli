package com.ascba.rebate.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.ascba.rebate.R;
import com.ascba.rebate.beans.ShopBaseItem;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by 李鹏 on 2017/03/15 0015.
 * 商家店铺
 */

public class BusinessShopAdapter extends BaseQuickAdapter<ShopBaseItem, BaseViewHolder> {
    private Context context;

    public BusinessShopAdapter(int layoutResId, List<ShopBaseItem> data, Context context) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, ShopBaseItem item) {
        Picasso.with(context).load(item.getUrl()).placeholder(R.mipmap.busi_loading).error(R.mipmap.busi_loading).centerCrop().into((ImageView) helper.getView(R.id.goods_list_img));
        helper.setText(R.id.goods_list_name, item.getTitle());
        helper.setText(R.id.goods_list_price, item.getDesc());
    }
}
