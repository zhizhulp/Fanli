package com.ascba.rebate.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.ascba.rebate.R;
import com.ascba.rebate.beans.Goods;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by 李鹏 on 2017/03/15 0015.
 * 待发货详情
 */

public class DeliverDetailsAdapter extends BaseQuickAdapter<Goods, BaseViewHolder> {

    private Context context;

    public DeliverDetailsAdapter(int layoutResId, List<Goods> data, Context context) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, Goods item) {
        ImageView imageView = helper.getView(R.id.item_goods_img);
        Glide.with(context).load(item.getImgUrl()).into(imageView);
        helper.setText(R.id.item_goods_name, item.getGoodsTitle());
        helper.setText(R.id.item_goods_standard, item.getGoodsStandard());
        helper.setText(R.id.item_goods_price, "￥" + item.getGoodsPrice());
        helper.getView(R.id.item_goods_price_old).setVisibility(View.INVISIBLE);
        helper.setText(R.id.item_goods_price_num, "x" + item.getUserQuy());
    }
}
