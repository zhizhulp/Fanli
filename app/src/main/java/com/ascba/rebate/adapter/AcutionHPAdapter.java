package com.ascba.rebate.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.beans.AcutionGoodsBean;
import com.ascba.rebate.utils.NumberFormatUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by 李鹏 on 2017/5/22.
 * 拍卖首页列表适配器
 */

public class AcutionHPAdapter extends BaseQuickAdapter<AcutionGoodsBean, BaseViewHolder> {

    private Context context;

    public AcutionHPAdapter(Context context, @LayoutRes int layoutResId, @Nullable List<AcutionGoodsBean> data) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder  helper, AcutionGoodsBean item) {
        ImageView imageView = helper.getView(R.id.auction_img);
        Picasso.with(context).load(item.getImgUrl()).error(R.mipmap.banner_loading).placeholder(R.mipmap.banner_loading).into(imageView);
        //名称
        helper.setText(R.id.auction_text_name, item.getName());
        //竞拍保证金
        helper.setText(R.id.auction_text_person, "￥"+ item.getCashDeposit());
        //价格
        helper.setText(R.id.auction_text_price, "￥"+ NumberFormatUtils.getNewDouble(item.getPrice()));
        helper.addOnClickListener(R.id.auction_btn_get);
        helper.setText(R.id.auction_btn_get,item.getStrState());
        helper.setText(R.id.auction_text_state,item.getStrState());
        //最低价标志
        helper.setVisible(R.id.im_lowest_price,true);
        TextView view = helper.getView(R.id.auction_btn_get);
        int state = item.getIntState();//1:拍卖结束,2:立即报名,3:即将开始,4:已报名,5:抢拍完毕(抢光了) 6:待支付 7:已支付 (2 4 5 6 7属于进行中状态)
        //剩余时间
        helper.setText(R.id.auction_text_time, state==5? item.getCartStatusTip():getTimeRemainning(item));

        Drawable drawableTop1 = mContext.getResources().getDrawable(R.mipmap.already_auction);//已拍
        drawableTop1.setBounds(0,0,drawableTop1.getMinimumWidth(),drawableTop1.getMinimumHeight());
        Drawable drawableTop2 = mContext.getResources().getDrawable(R.mipmap.icon_auction);//未拍
        drawableTop2.setBounds(0,0,drawableTop2.getMinimumWidth(),drawableTop2.getMinimumHeight());
        View imAlreadyRush = helper.getView(R.id.im_already_rush);
        TextView tvPriceDesc = helper.getView(R.id.tv_price_desc);
        if(state==2){
            setViewStatus(view,true,R.color.main_red_normal,drawableTop2, tvPriceDesc,"最低价",imAlreadyRush,false);
        }else if(state==4){
            setViewStatus(view,true,R.color.main_red_normal,drawableTop2, tvPriceDesc,"最低价",imAlreadyRush,false);
        }else if(state==5){
            setViewStatus(view,false,R.color.main_text_gary,drawableTop1, tvPriceDesc,"成交价",imAlreadyRush,true);
        }else if(state==6){
            setViewStatus(view,true,R.color.main_red_normal,drawableTop2, tvPriceDesc,"待支付",imAlreadyRush,false);
        }else if(state==7){
            setViewStatus(view,false,R.color.main_text_gary,drawableTop1, tvPriceDesc,"已支付",imAlreadyRush,false);
        }
    }

    private void setViewStatus(TextView view,boolean enable,int color,Drawable drawable,
                               TextView priceDescId, String stateText, View imAlreadyRush,boolean isVisible ){
        view.setEnabled(enable);
        view.setTextColor(mContext.getResources().getColor(color));
        view.setCompoundDrawables(null,drawable,null,null);
        priceDescId.setText(stateText);
        imAlreadyRush.setVisibility(isVisible? View.VISIBLE : View.GONE);
    }
    private String getTimeRemainning(AcutionGoodsBean item) {
        int leftTime = (int) (item.getEndTime() - System.currentTimeMillis() / 1000);
        if(leftTime > 0){
            int goodsLeftTime = (int) (item.getGoodsEndTime() - System.currentTimeMillis() / 1000);
            if( goodsLeftTime>0){
                int hour = goodsLeftTime % (24 * 3600) / 3600;
                int minute = goodsLeftTime % 3600 / 60;
                int second = goodsLeftTime % 60;
                return "距离结束:"+hour + "小时" + minute + "分钟" + second + "秒";
            }else {
                return "商品拍卖结束";
            }

        }else {
            return "商品拍卖结束";
        }
    }
}
