package com.ascba.rebate.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.ascba.rebate.R;
import com.ascba.rebate.beans.AcutionGoodsBean;
import com.ascba.rebate.utils.TimeUtils;
import com.ascba.rebate.utils.UrlUtils;
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
        Picasso.with(context).load(UrlUtils.baseWebsite+item.getImgUrl()).placeholder(R.mipmap.busi_loading).error(R.mipmap.busi_loading).into(imageView);
        helper.setText(R.id.text_auction_goods_price,item.getPrice()+"");//价格
        if(item.getState().equals("即将开始") ||item.getState().equals("拍卖结束") ){
            helper.setText(R.id.text_auction_goods_time,item.getState());
        }else if(item.getState().equals("进行中")){
            helper.setText(R.id.text_auction_goods_time,getRemainingTime(item));
        }
        helper.setText(R.id.text_auction_goods_name,item.getName());//名称
        helper.setText(R.id.text_auction_goods_score,"购买增值"+item.getScore()+"积分");//增值积分
        helper.setText(R.id.text_auction_goods_person,item.getCashDeposit());//人数改为保证金
        helper.addOnClickListener(R.id.btn_auction_goods_add_cart);//加入购物车
        helper.addOnClickListener(R.id.btn_auction_goods_apply);//立即报名
        int type = item.getType();//1抢拍 2盲拍
        if(type ==1){
            helper.setText(R.id.text_reduce_times,item.getReduceTimes()+"次");//降价次数
        }else if(type ==2){
            helper.addOnClickListener(R.id.btn_sub);//减号
            helper.addOnClickListener(R.id.btn_add);//加号
        }


    }
    //时间倒计时
    private String getRemainingTime(AcutionGoodsBean item){
        int leftTime = item.getCurrentLeftTime();
        if(leftTime==0){
            if(item.getReduceTimes()< item.getMaxReduceTimes()){
                leftTime = item.getGapTime();
            }else {
                return "竞拍结束";
            }
        }
        int hour = leftTime % (24 * 3600) / 3600;
        int minute = leftTime % 3600 / 60;
        int second = leftTime % 60;
        return hour + "时" + minute +"分"+ second +"秒";
    }
}
