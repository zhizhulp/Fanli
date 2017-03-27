package com.ascba.rebate.adapter;

import com.ascba.rebate.R;
import com.ascba.rebate.beans.MessageBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by 李鹏 on 2017/03/27 0027.
 */

public class ShopMessageAdapter extends BaseQuickAdapter<MessageBean, BaseViewHolder> {

    public ShopMessageAdapter(int layoutResId, List<MessageBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MessageBean item) {
        helper.setImageResource(R.id.item_message_img, item.getDrawable());
        helper.setText(R.id.item_message_title, item.getTitle());
        helper.setText(R.id.item_message_content, item.getContent());
    }
}
