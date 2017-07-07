package com.ascba.rebate.adapter.sweep;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.ascba.rebate.R;
import com.ascba.rebate.beans.sweep.ToBeSuredOrdersEntity;
import com.ascba.rebate.utils.UrlUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by lenovo on 2017/7/5.
 */

public class ToBeSuredOrdersAdapter extends BaseQuickAdapter<ToBeSuredOrdersEntity.DataListBean,BaseViewHolder>{


    public ToBeSuredOrdersAdapter(@LayoutRes int layoutResId, @Nullable List<ToBeSuredOrdersEntity.DataListBean> data) {
        super(layoutResId, data);
//        ImageView imageView = helper.getView(R.id.img_goods);//商品图片
//        Picasso.with(mContext).load(item.getImgUrl()).placeholder(R.mipmap.busi_loading).error(R.mipmap.busi_loading).into(imageView);
//        int intState = item.getIntState();
//        if (intState == 3 || intState == 1) {//本次剩余时间
//            helper.setText(R.id.text_auction_goods_time, item.getStrState());
//        } else if (intState == 2) {
//            helper.setText(R.id.text_auction_goods_time, getRemainingTime(item));
////        }
//        "order_id": 571,
//                "avatar": null,
//                "money": "+356.70",
//                "pay_type_text": "现金付款",
//                "order_status_text": "待商家确认"
//        helper.setText(R.id.text_auction_goods_name, item.getName());//名称
//        helper.setText(R.id.text_auction_goods_score, "获赠礼品分" + item.getScore()+"分");
//        helper.setText(R.id.text_auction_goods_price_rush, "￥" + NumberFormatUtils.getNewDouble(item.getPrice()));


    }

    @Override
    protected void convert(BaseViewHolder helper, ToBeSuredOrdersEntity.DataListBean item) {
        ImageView imageView = helper.getView(R.id.myallorders_icon);//商品图片
        Picasso.with(mContext).load(UrlUtils.baseWebsite+item.getAvatar()).into(imageView);
        helper.setText(R.id.myallorders_money, item.getMoney());//名称
        helper.setText(R.id.myallorders_category_txt, item.getMoney());//交易时间
        helper.setText(R.id.myallorders_pay_cash,item.getPay_type_text());//支付类型
        helper.setText(R.id.await_sure_order,item.getOrder_status_text());//确认状态


    }
}
