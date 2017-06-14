package com.ascba.rebate.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.ascba.rebate.R;
import com.ascba.rebate.beans.AcutionGoodsBean;
import com.ascba.rebate.utils.NumberFormatUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Administrator on 2017/6/6.
 * 竞拍购物车商品列表适配器
 */

public class CartChildAdapter extends BaseQuickAdapter<AcutionGoodsBean, BaseViewHolder> {

    private CheckBox cbTotal;
    private Callback callback;
    private String status;

    public interface Callback{
        void clickCbChild();
        void clickCbTotal();
    }

    public Callback getCallback() {
        return callback;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public CartChildAdapter(@LayoutRes int layoutResId, @Nullable List<AcutionGoodsBean> data, CheckBox cbTotal,String status) {
        super(layoutResId, data);
        this.cbTotal = cbTotal;
        this.status = status;
        cbTotal.setOnClickListener(createTotalClickListener());
    }

    @Override
    protected void convert(BaseViewHolder helper, AcutionGoodsBean item) {
        ImageView imageView = helper.getView(R.id.img_goods);//商品图片
        Picasso.with(mContext).load(UrlUtils.baseWebsite + item.getImgUrl()).placeholder(R.mipmap.busi_loading).error(R.mipmap.busi_loading).into(imageView);
        int intState = item.getIntState();
        if (intState == 3 || intState == 1) {//本次剩余时间
            helper.setText(R.id.text_auction_goods_time, item.getStrState());
        } else if (intState == 2) {
            helper.setText(R.id.text_auction_goods_time, getRemainingTime(item));
        }
        helper.setText(R.id.text_auction_goods_name, item.getName());//名称
        if("0,1".equals(status)){
            helper.setVisible(R.id.text_auction_goods_person,true);
            helper.setText(R.id.text_auction_goods_person, "￥" + item.getCashDeposit());//保证金
        }else if("2,3".equals(status)){
            helper.setVisible(R.id.text_auction_goods_person,false);
            helper.setText(R.id.tv_deposit_or_score, "购买获赠" + item.getScore()+"礼品分");//积分
        }

        int type = item.getType();//1抢拍 2盲拍
        if (type == 1) {
            helper.setVisible(R.id.text_auction_goods_price_rush, true);
            helper.setVisible(R.id.lat_auction_goods_price_blind, false);
            helper.setText(R.id.text_auction_goods_price_rush, "￥" + NumberFormatUtils.getNewDouble(item.getPrice()));
        } else if (type == 2) {
            helper.setVisible(R.id.text_auction_goods_price_rush, false);
            helper.setVisible(R.id.lat_auction_goods_price_blind, true);
            helper.addOnClickListener(R.id.btn_sub);//减号
            helper.addOnClickListener(R.id.btn_add);//加号
            helper.setText(R.id.text_auction_goods_price_blind, "￥" + NumberFormatUtils.getNewDouble(item.getPrice()));
        }
        CheckBox cbChild = helper.getView(R.id.cb_item);
        cbChild.setChecked(item.isSelect());
        cbChild.setOnClickListener(createChildClickListener(cbChild, item));

        helper.addOnClickListener(R.id.lat_see_details);
    }

    private View.OnClickListener createChildClickListener(final CheckBox cbChild, final AcutionGoodsBean item) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = cbChild.isChecked();
                item.setSelect(checked);
                notifyDataSetChanged();
                setCbtotal(item,checked);
                if(callback!=null){
                    callback.clickCbChild();
                }
            }
        };
    }

    private void setCbtotal(AcutionGoodsBean item,boolean checked) {
        boolean isAllSame = true;
        for (int i = 0; i < mData.size(); i++) {
            AcutionGoodsBean agb = mData.get(i);
            if (agb != item && (agb.isSelect() != checked)) {
                isAllSame = false;
                break;
            }
        }
        if(isAllSame){
            cbTotal.setChecked(checked);
        }else {
            cbTotal.setChecked(false);
        }
    }

    private View.OnClickListener createTotalClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < mData.size(); i++) {
                    AcutionGoodsBean agb = mData.get(i);
                    if (cbTotal.isChecked() && !agb.isSelect()) {
                        agb.setSelect(true);
                    } else if (!cbTotal.isChecked() && agb.isSelect()) {
                        agb.setSelect(false);
                    }
                }
                notifyDataSetChanged();
                if(callback!=null){
                    callback.clickCbTotal();
                }
            }
        };
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
