package com.ascba.rebate.adapter;

import com.ascba.rebate.R;
import com.ascba.rebate.beans.IntegralValueItem;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by 李鹏 on 2017/04/05 0005.
 */

public class IntegralValueAdapter extends BaseQuickAdapter<IntegralValueItem, BaseViewHolder> {


    public IntegralValueAdapter(int layoutResId, List<IntegralValueItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, IntegralValueItem item) {
            helper.setText(R.id.activity_se_item1_text1, item.getTitle());
            helper.setText(R.id.activity_se_item1_text2, item.getContent());
    }
}
