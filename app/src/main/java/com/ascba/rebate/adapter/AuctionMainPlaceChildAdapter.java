package com.ascba.rebate.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.beans.AcutionGoodsBean;
import com.ascba.rebate.utils.NumberFormatUtils;
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
    protected void convert(BaseViewHolder helper, AcutionGoodsBean item) {//1:拍卖结束,2:立即报名,3:即将开始,4:已报名,5:抢拍完毕(抢光了) 6:待支付 7:已支付
        ImageView imageView = helper.getView(R.id.img_goods);//商品图片
        Picasso.with(context).load(item.getImgUrl()).placeholder(R.mipmap.shop_goods_loading).error(R.mipmap.shop_goods_loading).into(imageView);
        TextView view = helper.getView(R.id.btn_auction_goods_apply);//按钮
        int intState = item.getIntState();
        if(intState==3 ||intState==1 ){//1:拍卖结束3:即将开始
            view.setEnabled(false);
            helper.setVisible(R.id.btn_auction_goods_add_cart,false);
            helper.setText(R.id.text_auction_goods_time,item.getStrState());
            helper.setBackgroundRes(R.id.btn_auction_goods_apply,R.drawable.btn_gray_bg);
        }else if(intState==2){//2:立即报名
            view.setEnabled(true);
            helper.setVisible(R.id.btn_auction_goods_add_cart,true);
            helper.setText(R.id.text_auction_goods_time,getRemainingTime(item));
            helper.setBackgroundRes(R.id.btn_auction_goods_apply,R.drawable.btn_red_bg);
        }else if(intState==4){//4:立即拍
            view.setEnabled(true);
            helper.setVisible(R.id.btn_auction_goods_add_cart,false);
            helper.setText(R.id.text_auction_goods_time,getRemainingTime(item));
            helper.setBackgroundRes(R.id.btn_auction_goods_apply,R.drawable.btn_yellow_bg);
        }else if(intState==5){//抢拍完毕
            view.setEnabled(false);
            helper.setVisible(R.id.btn_auction_goods_add_cart,false);
            helper.setText(R.id.text_auction_goods_time,item.getStrState());
            helper.setBackgroundRes(R.id.btn_auction_goods_apply,R.drawable.btn_gray_bg);
        }else if(intState==6){//待支付
            view.setEnabled(true);
            helper.setVisible(R.id.btn_auction_goods_add_cart,false);
            helper.setText(R.id.text_auction_goods_time,item.getStrState());
            helper.setBackgroundRes(R.id.btn_auction_goods_apply,R.drawable.btn_red_bg);
        }else if(intState==7){//已支付
            view.setEnabled(false);
            helper.setVisible(R.id.btn_auction_goods_add_cart,false);
            helper.setText(R.id.text_auction_goods_time,item.getStrState());
            helper.setBackgroundRes(R.id.btn_auction_goods_apply,R.drawable.btn_gray_bg);
        }
        view.setText(item.getStrState());
        helper.setText(R.id.text_auction_goods_name,item.getName());//名称
        helper.setText(R.id.text_auction_goods_score,"购买赠送"+item.getScore()+"礼品分");//增值积分
        helper.setText(R.id.text_auction_goods_person,"￥"+ item.getCashDeposit());//人数改为保证金
        helper.addOnClickListener(R.id.btn_auction_goods_add_cart);//加入购物车
        helper.addOnClickListener(R.id.btn_auction_goods_apply);//立即报名


        int type = item.getType();//1抢拍 2盲拍
        if(type ==1){
            View viewDepositTitle = helper.getView(R.id.tv_deposit_title);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) viewDepositTitle.getLayoutParams();
            if(intState==1){//结束
                layoutParams.leftMargin=0;
                helper.setVisible(R.id.tv_reduce_price_title,false);
                helper.setVisible(R.id.text_reduce_times,false);
            }else {
                layoutParams.leftMargin= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,19,mContext.getResources().getDisplayMetrics());
                helper.setVisible(R.id.tv_reduce_price_title,true);
                helper.setVisible(R.id.text_reduce_times,true);
                helper.setText(R.id.text_reduce_times,item.getReduceTimes()+"次");//降价次数
            }
            helper.setText(R.id.text_auction_goods_price,"￥"+ NumberFormatUtils.getNewDouble(item.getPrice()));//价格
        }else if(type ==2){
            helper.addOnClickListener(R.id.btn_sub);//减号
            helper.addOnClickListener(R.id.btn_add);//加号
            helper.setText(R.id.text_auction_goods_price,"降价至￥"+ NumberFormatUtils.getNewDouble(item.getPrice()));//价格
        }

    }

    private String getRemainingTime(AcutionGoodsBean item){
        if(System.currentTimeMillis()>=item.getGoodsEndTime()*1000 || item.getPrice() <= item.getEndPrice()){
            return "竞拍结束";
        }
        int leftTime = item.getCurrentLeftTime();//单位s
        int hour = leftTime % (24 * 3600) / 3600;
        int minute = leftTime % 3600 / 60;
        int second = leftTime % 60;
        return format(hour) + "时:" + format(minute) +"分:"+ format(second) +"秒";
    }

    private String format(int number){
        String s = String.valueOf(number);
        if(s.length()==1){
            s="0"+s;
        }
        return s;
    }
}
