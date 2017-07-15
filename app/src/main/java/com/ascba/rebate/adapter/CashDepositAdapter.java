package com.ascba.rebate.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.ascba.rebate.R;
import com.ascba.rebate.beans.AcutionGoodsBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by 李鹏 on 2017/5/25.
 * 保证金列表适配器
 */

public class CashDepositAdapter extends BaseQuickAdapter<AcutionGoodsBean, BaseViewHolder> {

    private Context context;

    public CashDepositAdapter(Context context, @LayoutRes int layoutResId, @Nullable List<AcutionGoodsBean> data) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, AcutionGoodsBean item) {

        helper.setText(R.id.text_name, item.getName());//名称
        ImageView imageView = helper.getView(R.id.img_goods);
        Picasso.with(context).load(item.getImgUrl()).placeholder(R.mipmap.busi_loading).error(R.mipmap.busi_loading).into(imageView);
        View view = helper.getView(R.id.text_time);
        switch (item.getIntState()) {
            case 3:
                //未开始
                helper.setBackgroundColor(R.id.text_state, Color.parseColor("#FFA24F"));
                view.setVisibility(View.INVISIBLE);
                break;
            case 2:
                //已开始
                helper.setBackgroundColor(R.id.text_state, Color.parseColor("#F07303"));
                view.setVisibility(View.VISIBLE);
                helper.setText(R.id.text_time, getTimeRemaining(item));
                break;
            case 1:
                //已结束
                helper.setBackgroundColor(R.id.text_state, Color.parseColor("#A0A0A0"));
                view.setVisibility(View.INVISIBLE);
                break;
        }
        helper.setText(R.id.text_state, item.getStrState());
        helper.setText(R.id.text_cash, "￥"+item.getCashDeposit());//保证金
        helper.addOnClickListener(R.id.text_btn);
        switch (item.getStrPriceState()) {
            case "已支付":
                helper.setBackgroundRes(R.id.text_btn,R.drawable.btn_red_bg);
                break;
            case "已退还":
                helper.setBackgroundRes(R.id.text_btn,R.drawable.btn_gray_bg);
                break;
        }
        helper.setText(R.id.text_btn, item.getStrPriceState());

    }

    private String getTimeRemaining(AcutionGoodsBean item) {
        int leftTime = (int) (item.getEndTime() - System.currentTimeMillis() / 1000);
        if(leftTime <=0){
            return "竞拍结束";
        }
        int hour = leftTime % (24 * 3600) / 3600;
        int minute = leftTime % 3600 / 60;
        int second = leftTime % 60;
        return "离结束:" +hour + "时" + minute + "分" + second + "秒";
    }

}
