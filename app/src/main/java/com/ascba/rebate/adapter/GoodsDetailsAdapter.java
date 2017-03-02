package com.ascba.rebate.adapter;

import android.content.Context;

import com.ascba.rebate.R;
import com.ascba.rebate.beans.GoodsDetailsItem;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by 李鹏 on 2017/03/02 0002.
 */

public class GoodsDetailsAdapter extends BaseMultiItemQuickAdapter<GoodsDetailsItem,BaseViewHolder> {


    private Context context;

    public GoodsDetailsAdapter(List<GoodsDetailsItem> data,Context context) {
        super(data);
        this.context = context;
        for (int i = 0; i < data.size(); i++) {
            GoodsDetailsItem item = data.get(i);
            addItemType(item.getItemType(), item.getResLat());
        }
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodsDetailsItem item) {
        switch (helper.getItemViewType()) {
            case GoodsDetailsItem.TYPE_VIEWPAGER:
                break;
            case GoodsDetailsItem.TYPE_GOODS_SIMPLE_DESC:
                helper.setText(R.id.goods_details_simple_desc_type_store,item.getStore_type());
                helper.setText(R.id.goods_details_simple_desc_type_goods1,item.getGoods_title());
                helper.setText(R.id.goods_details_simple_desc_type_goods2,item.getGoods_desc());
                helper.setText(R.id.goods_details_simple_desc_price_new,item.getPrice_new());
                helper.setText(R.id.goods_details_simple_desc_price_old,item.getPrice_old());
                break;
        }
    }
}
