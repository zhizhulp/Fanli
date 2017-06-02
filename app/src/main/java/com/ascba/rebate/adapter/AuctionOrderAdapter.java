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
 * Created by 李鹏 on 2017/5/25.
 */

public class AuctionOrderAdapter extends BaseQuickAdapter<AcutionGoodsBean, BaseViewHolder> {
    private Context context;

    public AuctionOrderAdapter(Context context, @LayoutRes int layoutResId, @Nullable List<AcutionGoodsBean> data) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, AcutionGoodsBean item) {
        ImageView imageView = helper.getView(R.id.img_goods);
        Picasso.with(context).load(item.getImgUrl()).placeholder(R.mipmap.busi_loading).error(R.mipmap.busi_loading).into(imageView);

        helper.setText(R.id.text_auction_goods_name, item.getName());//名称

        helper.setText(R.id.text_price, item.getPrice()+"");//出价

        helper.setText(R.id.text_auction_goods_score, item.getScore());//积分

        helper.addOnClickListener(R.id.btn_auction);

        switch (item.getPayState()) {
            case "0":
                //待支付
                helper.setText(R.id.btn_auction, "去支付");
                helper.setText(R.id.text_youhui, "待支付");
                helper.setBackgroundRes(R.id.btn_auction, R.drawable.red_bg2);
                helper.setTextColor(R.id.btn_auction, context.getResources().getColor(R.color.white));
                break;
            case "1":
                //待收货
                helper.setText(R.id.btn_auction, "查物流");
                helper.setText(R.id.text_youhui, "待收货");
                helper.setBackgroundRes(R.id.btn_auction, R.drawable.red_bg);
                helper.setTextColor(R.id.btn_auction, context.getResources().getColor(R.color.fragment_money_red));
                break;
            case "2":
                //已收货
                helper.setText(R.id.btn_auction, "交易完成");
                helper.setText(R.id.text_youhui, "已收货");
                helper.setBackgroundRes(R.id.btn_auction, R.color.white);
                helper.setTextColor(R.id.btn_auction, context.getResources().getColor(R.color.fragment_money_red));
                break;
        }
    }
}
