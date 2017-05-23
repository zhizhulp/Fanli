package com.ascba.rebate.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.ascba.rebate.R;
import com.ascba.rebate.beans.AcutionGoodsBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by 李鹏 on 2017/5/22.
 */

public class AcutionHPAdapter extends BaseQuickAdapter<AcutionGoodsBean, BaseViewHolder> {

    private Context context;

    public AcutionHPAdapter(Context context, @LayoutRes int layoutResId, @Nullable List<AcutionGoodsBean> data) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, AcutionGoodsBean item) {
        ImageView imageView = helper.getView(R.id.auction_img);
        Picasso.with(context).load(item.getImgUrl()).error(R.mipmap.loading_rect).placeholder(R.mipmap.loading_rect).into(imageView);

        //剩余时间
        helper.setText(R.id.auction_text_time, item.getTimeRemaining());

        //名称
        helper.setText(R.id.auction_text_name, item.getName());

        //竞拍人数
        helper.setText(R.id.auction_text_person, item.getPersonNum());

        //价格
        helper.setText(R.id.auction_text_price, item.getPrice());

        helper.addOnClickListener(R.id.auction_btn_get);
    }
}
