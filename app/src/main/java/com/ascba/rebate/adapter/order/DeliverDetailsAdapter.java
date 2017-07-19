package com.ascba.rebate.adapter.order;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.beans.Goods;
import com.ascba.rebate.utils.StringUtils;
import com.ascba.rebate.view.RadiusBackgroundSpan;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by 李鹏 on 2017/03/15 0015.
 * 待发货详情
 */

public class DeliverDetailsAdapter extends BaseQuickAdapter<Goods, BaseViewHolder> {

    private Context context;

    public DeliverDetailsAdapter(int layoutResId, List<Goods> data, Context context) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, Goods item) {
        ImageView imageView = helper.getView(R.id.item_goods_img);
        Picasso.with(context).load(item.getImgUrl()).placeholder(R.mipmap.busi_loading).error(R.mipmap.busi_loading).into(imageView);
        helper.setText(R.id.item_goods_standard, item.getGoodsStandard());
        helper.setText(R.id.item_goods_price, "￥" + item.getGoodsPrice());
        helper.setText(R.id.item_goods_price_num, "x" + item.getUserQuy());
        //查看物流
        helper.addOnClickListener(R.id.tv_express_flow);
        helper.setVisible(R.id.tv_express_flow, !StringUtils.isEmpty(item.getDeliverNum()));
        //set goods name
        TextView tvName = helper.getView(R.id.item_goods_name);
        String teiHui = item.getTeiHui();
        if(!StringUtils.isEmpty(teiHui)){
            SpannableString ss=new SpannableString(teiHui+item.getGoodsTitle());
            ss.setSpan(new RadiusBackgroundSpan(mContext,0xfffa5e5f,2,11),0,teiHui.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvName.setText(ss);
        }else {
            tvName.setText(item.getGoodsTitle());
        }
        //set reduce tag
        helper.setVisible(R.id.tv_use_ticket_reduce, !StringUtils.isEmpty(item.getUseTicketToReduce()));
    }
}
