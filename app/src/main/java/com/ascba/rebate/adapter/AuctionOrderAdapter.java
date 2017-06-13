package com.ascba.rebate.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.beans.AcutionGoodsBean;
import com.ascba.rebate.utils.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by 李鹏 on 2017/5/25.
 */

public class AuctionOrderAdapter extends BaseQuickAdapter<AcutionGoodsBean, BaseViewHolder> {

    public AuctionOrderAdapter( @LayoutRes int layoutResId, @Nullable List<AcutionGoodsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AcutionGoodsBean item) {
        ImageView imageView = helper.getView(R.id.img_goods);
        Picasso.with(mContext).load(item.getImgUrl()).placeholder(R.mipmap.busi_loading).error(R.mipmap.busi_loading).into(imageView);
        helper.setText(R.id.text_auction_goods_name, item.getName());//名称
        helper.setText(R.id.text_price, "￥"+item.getPrice());//出价
        helper.setText(R.id.text_auction_goods_score, "购买增值"+item.getScore()+"积分");//积分
        helper.addOnClickListener(R.id.btn_auction);
        TextView view= helper.getView(R.id.btn_auction);
        view.setText(item.getStrPriceState());
        int state = item.getIntPriceState();
        boolean isEmpty = StringUtils.isEmpty(item.getExpressNum());//物流账号是否为空
        if(state==0){// 0：未支付，1：已支付，2：已收货（交易完成），3：已退款
            setBgAndEnable(view,R.drawable.red_bg2,true);
        }else if(state==1){
            setBgAndEnable(view,R.drawable.red_bg2,true);
        }else if(state==2){
            setBgAndEnable(view,R.drawable.btn_gray_bg,false);
        }else if(state==3){
            setBgAndEnable(view,R.drawable.btn_gray_bg,false);
        }
        helper.setVisible(R.id.btn_sure_receive, state == 1 && !isEmpty);//确认收货是否显示
        helper.addOnClickListener(R.id.btn_sure_receive);
    }

    private void setBgAndEnable(View view, int resID, boolean enable){
        view.setEnabled(enable);
        view.setBackgroundDrawable(mContext.getResources().getDrawable(resID));
    }
}
