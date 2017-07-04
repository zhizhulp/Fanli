package com.ascba.rebate.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.ascba.rebate.R;
import com.ascba.rebate.beans.ShopBaseItem;
import com.ascba.rebate.utils.StringUtils;
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
        Picasso.with(context).load(item.getUrl()).placeholder(R.mipmap.shop_goods_loading).error(R.mipmap.shop_goods_loading).into((ImageView) helper.getView(R.id.goods_list_img));
        helper.setText(R.id.goods_list_name, item.getTitle());
        helper.setText(R.id.goods_list_price, item.getDesc());
        helper.addOnClickListener(R.id.goods_list_cart);
        helper.setVisible(R.id.tv_use_ticket_reduce, !StringUtils.isEmpty(item.getUseTicketToReduce()));
        helper.setText(R.id.tv_use_ticket_reduce,item.getUseTicketToReduce());

        helper.setVisible(R.id.tv_teihui,!StringUtils.isEmpty(item.getTeiHui()));
        helper.setText(R.id.tv_teihui,item.getTeiHui());
    }
}
