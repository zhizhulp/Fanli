package com.ascba.rebate.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.beans.AcutionGoodsBean;
import com.ascba.rebate.utils.UrlUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Administrator on 2017/6/7.
 * 我的竞拍列表适配器
 */

public class AuctionListAdapter extends BaseQuickAdapter<AcutionGoodsBean, BaseViewHolder> {

    public AuctionListAdapter(@LayoutRes int layoutResId, @Nullable List<AcutionGoodsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AcutionGoodsBean item) {
        ImageView imageView = helper.getView(R.id.img_goods);//商品图片
        Picasso.with(mContext).load(item.getImgUrl()).placeholder(R.mipmap.busi_loading).error(R.mipmap.busi_loading).into(imageView);
        int intState = item.getIntState();
        if (intState == 3 || intState == 1) {//本次剩余时间
            helper.setText(R.id.text_auction_goods_time, item.getStrState());
        } else if (intState == 2) {
            helper.setText(R.id.text_auction_goods_time, getRemainingTime(item));
        }
        helper.setText(R.id.text_auction_goods_name, item.getName());//名称
        helper.setText(R.id.text_auction_goods_score, "购买增值" + item.getScore()+"积分");
        helper.setText(R.id.text_auction_goods_price_rush, "￥" + item.getPrice());

        TextView view = helper.getView(R.id.tv_blind_state);
        view.setText(item.getStrState());
        int intPriceState = item.getIntPriceState();
        //String blindState = item.getBlindState();0:出局，1：领先，2：等待结果
        if(intPriceState==1){
            view.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.btn_red_bg));
        }else if(intPriceState==0){
            view.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.btn_gray_bg));
        }else if(intPriceState==2){
            view.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.btn_gray_bg));
        }
    }

    //时间倒计时
    private String getRemainingTime(AcutionGoodsBean item) {
        int leftTime = item.getCurrentLeftTime();//单位s
        if (leftTime == 0) {
            if (item.getReduceTimes() < item.getMaxReduceTimes()) {
                leftTime = item.getGapTime();
            } else {
                return "竞拍结束";
            }
        }
        int hour = leftTime % (24 * 3600) / 3600;
        int minute = leftTime % 3600 / 60;
        int second = leftTime % 60;
        return hour + " 时" + minute + " 分" + second + " 秒";
    }
}
