package com.ascba.rebate.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import com.ascba.rebate.R;
import com.ascba.rebate.beans.AcutionGoodsBean;
import com.ascba.rebate.utils.UrlUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Administrator on 2017/6/6.
 * 竞拍购物车商品列表适配器
 */

public class CartChildAdapter extends BaseQuickAdapter <AcutionGoodsBean,BaseViewHolder>{
    public CartChildAdapter(@LayoutRes int layoutResId, @Nullable List<AcutionGoodsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AcutionGoodsBean item) {
        ImageView imageView = helper.getView(R.id.img_goods);//商品图片
        Picasso.with(mContext).load(UrlUtils.baseWebsite+item.getImgUrl()).placeholder(R.mipmap.busi_loading).error(R.mipmap.busi_loading).into(imageView);
        helper.setText(R.id.text_auction_goods_price,"￥"+item.getPrice());//价格
        int intState = item.getIntState();
        if(intState==3 ||intState==1 ){//本次剩余时间
            helper.setText(R.id.text_auction_goods_time,item.getStrState());
        }else if(intState==2){
            helper.setText(R.id.text_auction_goods_time,getRemainingTime(item));
        }
        helper.setText(R.id.text_auction_goods_name,item.getName());//名称
        helper.setText(R.id.text_auction_goods_person,item.getCashDeposit());//人数改为保证金
        int type = item.getType();//1抢拍 2盲拍
        if(type ==1){
            helper.setVisible(R.id.text_auction_goods_price_rush,true);
            helper.setVisible(R.id.lat_auction_goods_price_blind,false);
            helper.setText(R.id.text_auction_goods_price_rush,"￥"+item.getPrice());
        }else if(type ==2){
            helper.setVisible(R.id.text_auction_goods_price_rush,false);
            helper.setVisible(R.id.lat_auction_goods_price_blind,true);
            helper.addOnClickListener(R.id.btn_sub);//减号
            helper.addOnClickListener(R.id.btn_add);//加号
            helper.setText(R.id.text_auction_goods_price_blind,"降价至：￥"+item.getPrice());
        }
    }

    //时间倒计时
    private String getRemainingTime(AcutionGoodsBean item){
        int leftTime = item.getCurrentLeftTime();//单位s
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
