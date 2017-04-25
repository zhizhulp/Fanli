package com.ascba.rebate.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.ascba.rebate.R;
import com.ascba.rebate.beans.MessageBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by 李鹏 on 2017/03/27 0027.
 */

public class ShopMessageAdapter extends BaseQuickAdapter<MessageBean, BaseViewHolder> {
    private Context context;

    public ShopMessageAdapter(Context context, int layoutResId, List<MessageBean> data) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, MessageBean item) {

        helper.setText(R.id.item_message_title, item.getTitle());
        helper.setText(R.id.item_message_content, item.getContent());
        helper.setText(R.id.item_message_time, item.getTime());
        ImageView imageView = helper.getView(R.id.item_message_img);
        Picasso.with(context).load(item.getImg()).placeholder(R.mipmap.busi_loading).error(R.mipmap.busi_loading).into(imageView);
        if (item.getCount() > 0) {
            helper.setVisible(R.id.item_message_img_indicator, true);
        } else {
            helper.setVisible(R.id.item_message_img_indicator, false);
        }
    }
}
