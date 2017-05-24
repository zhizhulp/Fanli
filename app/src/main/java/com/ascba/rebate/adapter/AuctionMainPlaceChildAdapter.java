package com.ascba.rebate.adapter;

import android.content.Context;
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
 * Created by 李鹏 on 2017/5/24.
 */

public class AuctionMainPlaceChildAdapter extends BaseQuickAdapter<AcutionGoodsBean, BaseViewHolder> {

    private Context context;

    public AuctionMainPlaceChildAdapter(Context context, @LayoutRes int layoutResId, @Nullable List<AcutionGoodsBean> data) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, AcutionGoodsBean item) {
        ImageView imageView = helper.getView(R.id.img_goods);//商品图片
        Picasso.with(context).load(item.getImgUrl()).placeholder(R.mipmap.busi_loading).error(R.mipmap.busi_loading).into(imageView);

        helper.setText(R.id.text_auction_goods_name,item.getPrice());//价格
        helper.setText(R.id.text_auction_goods_time,item.getTimeRemaining());//时间
        helper.setText(R.id.text_auction_goods_name,item.getName());//名称
        helper.setText(R.id.text_auction_goods_score,item.getScore());//增值积分
        helper.setText(R.id.text_auction_goods_person,item.getPersonNum());//人数
        helper.addOnClickListener(R.id.btn_sub);//减号
        helper.addOnClickListener(R.id.btn_add);//加号
        helper.addOnClickListener(R.id.btn_auction_goods_add_cart);//加入购物车
        helper.addOnClickListener(R.id.btn_auction_goods_apply);//立即报名
    }
}
