package com.ascba.rebate.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.ascba.rebate.R;
import com.ascba.rebate.beans.GoodsAttr;
import com.ascba.rebate.view.FilterCheckBox;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 筛选适配器
 */

public class FilterAdapter extends BaseQuickAdapter<GoodsAttr, BaseViewHolder> {
    private Context context;
    private TagFlowLayout flowLat;
    private LayoutInflater inflate;
    private List<String> list;

    public FilterAdapter(int layoutResId, List<GoodsAttr> data, Context context) {
        super(layoutResId, data);
        this.context = context;
        inflate = LayoutInflater.from(context);
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodsAttr item) {
        helper.setText(R.id.filter_title, item.getTitle());
        flowLat = helper.getView(R.id.flowlayout);
        List<GoodsAttr.Attrs> strs = item.getStrs();
        list = new ArrayList<>();
        for (int j = 0; j < strs.size(); j++) {
            GoodsAttr.Attrs attrs = strs.get(j);
            list.add(attrs.getContent());
        }
        TagAdapter<String> adapter = new TagAdapter<String>(list) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {

                FilterCheckBox filterCheckBox = (FilterCheckBox) inflate.inflate(R.layout.item_filter,
                        flowLat, false);
                filterCheckBox.setText(s);
                return filterCheckBox;
            }
        };
        flowLat.setAdapter(adapter);
    }
}
