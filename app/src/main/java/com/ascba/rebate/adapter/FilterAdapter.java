package com.ascba.rebate.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.ascba.rebate.R;
import com.ascba.rebate.beans.GoodsAttr;
import com.ascba.rebate.view.FilterCheckBox;
import com.ascba.rebate.view.FlowTagLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 筛选适配器
 */

public class FilterAdapter extends BaseQuickAdapter<GoodsAttr,BaseViewHolder> {
    private Context context;
    public FilterAdapter(int layoutResId, List<GoodsAttr> data, Context context) {
        super(layoutResId, data);
        this.context=context;
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodsAttr item) {
        helper.setText(R.id.filter_title,item.getTitle());
        FlowTagLayout flowLat = helper.getView(R.id.flow_layout);
        List<GoodsAttr.Attrs> strs = item.getStrs();
        for (int j = 0; j < strs.size(); j++) {
            GoodsAttr.Attrs attrs = strs.get(j);
            FilterCheckBox cb=new FilterCheckBox(context);
            cb.setText(attrs.getContent());
            flowLat.addView(cb);
        }
    }
}
