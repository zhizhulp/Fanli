package com.ascba.rebate.adapter;

import android.content.Context;
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
 * Created by 李鹏 on 2017/5/24.
 * 抢拍盲拍列表适配器
 */

public class AuctionMainPlaceChildAdapter extends BaseQuickAdapter<AcutionGoodsBean, BaseViewHolder> {
    private Context context;

    public AuctionMainPlaceChildAdapter(Context context, @LayoutRes int layoutResId, @Nullable List<AcutionGoodsBean> data) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, AcutionGoodsBean item) {//1:拍卖结束,2:立即报名,3:即将开始,4:已报名,5:已拍
        ImageView imageView = helper.getView(R.id.img_goods);//商品图片
        Picasso.with(context).load(UrlUtils.baseWebsite+item.getImgUrl()).placeholder(R.mipmap.busi_loading).error(R.mipmap.busi_loading).into(imageView);
        helper.setText(R.id.text_auction_goods_price,"￥"+item.getPrice());//价格
        TextView view = helper.getView(R.id.btn_auction_goods_apply);//按钮
        int intState = item.getIntState();
        if(intState==3 ||intState==1 ){
            view.setEnabled(false);
            helper.setVisible(R.id.btn_auction_goods_add_cart,false);
            helper.setText(R.id.text_auction_goods_time,item.getStrState());
        }else if(intState==2){
            view.setEnabled(true);
            helper.setVisible(R.id.btn_auction_goods_add_cart,true);
            helper.setText(R.id.text_auction_goods_time,getRemainingTime(item));
        }else if(intState==4){
            view.setEnabled(true);
            helper.setVisible(R.id.btn_auction_goods_add_cart,true);
            helper.setText(R.id.text_auction_goods_time,getRemainingTime(item));
        }else if(intState==5){
            view.setEnabled(false);
            helper.setVisible(R.id.btn_auction_goods_add_cart,false);
            helper.setText(R.id.text_auction_goods_time,getRemainingTime(item));
        }
        view.setText(item.getStrState());
        helper.setText(R.id.text_auction_goods_name,item.getName());//名称
        helper.setText(R.id.text_auction_goods_score,"购买赠送"+item.getScore()+"礼品分");//增值积分
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
        int leftTime = item.getCurrentLeftTime();//单位s
        if(leftTime==0){
            if(item.getIntState()!=2){
                if(item.getReduceTimes()< item.getMaxReduceTimes()){
                    leftTime = item.getGapTime();
                }else {
                    return "竞拍结束";
                }
            }
        }
        int hour = leftTime % (24 * 3600) / 3600;
        int minute = leftTime % 3600 / 60;
        int second = leftTime % 60;
        return hour + "时" + minute +"分"+ second +"秒";
    }
}
