package com.ascba.rebate.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.ascba.rebate.R;
import com.ascba.rebate.beans.PCMultipleItem;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by 李鹏 on 2017/2/25.
 */

public class PCMultipleItemAdapter extends BaseMultiItemQuickAdapter<PCMultipleItem, BaseViewHolder> {

    private Context context;

    public PCMultipleItemAdapter(List<PCMultipleItem> data, Context context) {
        super(data);
        this.context = context;
        if (data != null && data.size() > 0) {
            addItemType(PCMultipleItem.TYPE_0, R.layout.activity_pc_item_head);
            addItemType(PCMultipleItem.TYPE_1, R.layout.activity_pc_item1);
            addItemType(PCMultipleItem.TYPE_2, R.layout.item_divider1);
            addItemType(PCMultipleItem.TYPE_3, R.layout.activity_pc_item3);
            addItemType(PCMultipleItem.TYPE_4, R.layout.item_divider2);
            addItemType(PCMultipleItem.TYPE_5, R.layout.activity_pc_item4);
        }
    }

    @Override
    protected void convert(BaseViewHolder helper, PCMultipleItem item) {
        switch (helper.getItemViewType()) {
            case PCMultipleItem.TYPE_0:
                //个人信息
                helper.setText(R.id.activity_pc_item_head_name, item.getTitle());
                ImageView headView = helper.getView(R.id.me_user_img);
                helper.addOnClickListener(R.id.me_user_img);
                helper.addOnClickListener(R.id.activity_pc_item_head_message);//消息
                Picasso.with(context).load(item.getHeadImg()).placeholder(R.mipmap.busi_loading).error(R.mipmap.busi_loading).into(headView);
                break;

            case PCMultipleItem.TYPE_1:
                //我的订单
                helper.setImageResource(R.id.activity_pc_item1_img_left, item.getDrawableLeft());
                helper.setText(R.id.activity_pc_item1_text_left, item.getContenLeft());
                helper.setText(R.id.activity_pc_item1_text_right, item.getContenRight());
                break;

            case PCMultipleItem.TYPE_3:
                //待付款、待发货、已成交、待评价、退款
                helper.setText(R.id.activity_pc_item3_text, item.getContent());

                if (item.getMessageNum() > 0) {
                    helper.setText(R.id.activity_pc_item3_text_notify, String.valueOf(item.getMessageNum()));
                } else {
                    helper.setVisible(R.id.activity_pc_item3_text_notify, false);
                }

                helper.setImageResource(R.id.activity_pc_item3_img, item.getDrawableHead());
                break;

            case PCMultipleItem.TYPE_5:
                helper.setText(R.id.activity_pc_item4_text_left,item.getContenLeft());
                helper.setText(R.id.activity_pc_item4_text_right, item.getContenRight());
                helper.setImageResource(R.id.activity_pc_item4_img_left,item.getDrawableLeft());
                break;
        }

    }
}
