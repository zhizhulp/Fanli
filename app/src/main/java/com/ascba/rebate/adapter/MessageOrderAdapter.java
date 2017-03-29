package com.ascba.rebate.adapter;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.beans.OrderBean;
import com.ascba.rebate.view.RoundImageView;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by 李鹏 on 2017/03/28 0028.
 * 订单消息
 */

public class MessageOrderAdapter extends BaseQuickAdapter<OrderBean, BaseViewHolder> {

    private Context context;

    public MessageOrderAdapter(int layoutResId, List<OrderBean> data, Context context) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderBean item) {
        helper.setText(R.id.item_message_order_time, item.getTime());
        RoundImageView imageView = helper.getView(R.id.item_message_order_img);
        Glide.with(context).load(item.getImgUrl()).placeholder(R.mipmap.busi_loading).error(R.mipmap.busi_loading).into(imageView);
        TextView content = helper.getView(R.id.item_message_order_text);
        SpannableStringBuilder builder = new SpannableStringBuilder("您的订单号：" + item.getId() + "还未支付，订单24小时未支付，订单就会取消，前往支付~");
        ForegroundColorSpan redSpan = new ForegroundColorSpan(content.getResources().getColor(R.color.shop_red_text_color));
        int startIndex = 6;
        int endIndex = startIndex + item.getId().length();
        builder.setSpan(redSpan, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        content.setText(builder);
    }
}
