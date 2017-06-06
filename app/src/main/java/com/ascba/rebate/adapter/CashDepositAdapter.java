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

        switch (item.getState()) {
            case "0":
                //未开始
                helper.setBackgroundColor(R.id.text_state, Color.parseColor("#FFA24F"));
                helper.setText(R.id.text_state, "未开始");
                helper.setVisible(R.id.text_time, true);
                helper.setText(R.id.text_time, item.getTimeRemaining());
                break;
            case "1":
                //已开始
                helper.setBackgroundColor(R.id.text_state, Color.parseColor("#F63C3C"));
                helper.setText(R.id.text_state, "已开始");
                helper.setVisible(R.id.text_time, true);
                helper.setText(R.id.text_time, item.getTimeRemaining());
                break;
            case "2":
                //已结束
                helper.setBackgroundColor(R.id.text_state, Color.parseColor("#A0A0A0"));
                helper.setText(R.id.text_state, "已结束");
                helper.getView(R.id.text_time).setVisibility(View.INVISIBLE);
                break;
        }

        helper.setText(R.id.text_cash, item.getCashDeposit());//保证金
        helper.addOnClickListener(R.id.text_btn);

        switch (item.getPayState()) {
            case "0":
                //已支付
                helper.setBackgroundRes(R.id.text_btn,R.drawable.red_bg2);
                helper.setText(R.id.text_btn, "已支付");
                break;
            case "1":
                //已退还
                helper.setBackgroundRes(R.id.text_btn,R.drawable.gray_bg4);
                helper.setText(R.id.text_btn, "已退还");
                break;
        }

    }
}
