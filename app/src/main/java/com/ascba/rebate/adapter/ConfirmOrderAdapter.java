package com.ascba.rebate.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.ascba.rebate.R;
import com.ascba.rebate.beans.Goods;
import com.ascba.rebate.utils.StringUtils;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by 李鹏 on 2017/04/06 0006.
 * 确认订单
 */

public class ConfirmOrderAdapter extends BaseMultiItemQuickAdapter<Goods, BaseViewHolder> {
    public static final int TYPE1 = 1;
    public static final int TYPE2 = 2;
    public static final int TYPE3 = 3;

    private editTextString editTextString;
    private Context context;

    public void setEditTextString(ConfirmOrderAdapter.editTextString editTextString) {
        this.editTextString = editTextString;
    }

    public ConfirmOrderAdapter(Context context, List<Goods> data) {
        super(data);
        this.context = context;

        for (Goods goods : data) {
            addItemType(goods.getItemType(), goods.getLayout());
        }

    }

    @Override
    protected void convert(final BaseViewHolder helper, final Goods item) {
        switch (helper.getItemViewType()) {
            case TYPE1:
                helper.setText(R.id.confir_order_shop, item.getStore());
                break;
            case TYPE2:
                ImageView imageView = helper.getView(R.id.item_goods_img);
                Picasso.with(context).load(item.getImgUrl()).placeholder(R.mipmap.busi_loading).error(R.mipmap.busi_loading).into(imageView);
                helper.setText(R.id.item_goods_name, item.getGoodsTitle());
                helper.setText(R.id.item_goods_standard, item.getGoodsStandard());
                helper.setText(R.id.item_goods_price, "￥" + item.getGoodsPrice());
                helper.getView(R.id.item_goods_price_old).setVisibility(View.INVISIBLE);
                helper.setText(R.id.item_goods_price_num, "x" + item.getUserQuy());
                break;
            case TYPE3:
                helper.setText(R.id.item_cost_freightPrice, "￥" + item.getFreightPrice());
                final EditText etMsg = helper.getView(R.id.item_cost_message);
                etMsg.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (editTextString != null) {
                            editTextString.getString(etMsg.getText().toString(), item.getStoreId(), item.getMessageCart());
                        }
                    }
                });

                helper.setText(R.id.confir_order_text_num, "共" + item.getNum() + "件商品");
                helper.setText(R.id.item_cost_price, "￥" + item.getTotalPrice());

                helper.setVisible(R.id.line_real_reduce, !StringUtils.isEmpty(item.getSubtract()));
                helper.setVisible(R.id.lat_real_reduce, !StringUtils.isEmpty(item.getSubtract()));
                helper.setText(R.id.tv_real_reduce, item.getSubtract());

                helper.setVisible(R.id.line_cheap_desc, !StringUtils.isEmpty(item.getSubDesc()));
                helper.setVisible(R.id.tv_cheap_desc, !StringUtils.isEmpty(item.getSubDesc()));
                helper.setText(R.id.tv_cheap_desc, item.getSubDesc());

                break;
        }
    }

    public interface editTextString {
        void getString(String content, int storeId, String cartId);
    }
}
