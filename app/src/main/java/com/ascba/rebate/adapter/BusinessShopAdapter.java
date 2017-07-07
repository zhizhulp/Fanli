package com.ascba.rebate.adapter;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.beans.ShopBaseItem;
import com.ascba.rebate.utils.StringUtils;
import com.ascba.rebate.view.RadiusBackgroundSpan;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by 李鹏 on 2017/03/15 0015.
 * 商家店铺
 */

public class BusinessShopAdapter extends BaseQuickAdapter<ShopBaseItem, BaseViewHolder> {
    private Context context;

    public BusinessShopAdapter(int layoutResId, List<ShopBaseItem> data, Context context) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, ShopBaseItem item) {
        Picasso.with(context).load(item.getUrl()).placeholder(R.mipmap.shop_goods_loading).error(R.mipmap.shop_goods_loading).into((ImageView) helper.getView(R.id.goods_list_img));
        //set goods name
        TextView tvName = helper.getView(R.id.goods_list_name);
        String teiHui = item.getTeiHui();
        if(!StringUtils.isEmpty(teiHui)){
            SpannableString ss=new SpannableString(teiHui+item.getTitle());
            ss.setSpan(new RadiusBackgroundSpan(mContext,0xfffa5e5f,2),0,teiHui.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvName.setText(ss);
        }else {
            tvName.setText(item.getTitle());
        }
        helper.setText(R.id.goods_list_price, item.getDesc());
        helper.addOnClickListener(R.id.goods_list_cart);
        helper.setVisible(R.id.tv_use_ticket_reduce, !StringUtils.isEmpty(item.getUseTicketToReduce()));
        helper.setText(R.id.tv_use_ticket_reduce,item.getUseTicketToReduce());

        /*helper.setVisible(R.id.tv_teihui,!StringUtils.isEmpty(item.getTeiHui()));
        helper.setText(R.id.tv_teihui,item.getTeiHui());*/
    }
}
