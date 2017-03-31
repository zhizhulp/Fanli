package com.ascba.rebate.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.ascba.rebate.R;
import com.ascba.rebate.beans.EvaluationBean;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.List;

/**
 * Created by 李鹏 on 2017/03/08 0008.
 * 全部评价adapter
 */

public class AllEvaluationAdapter extends BaseMultiItemQuickAdapter<EvaluationBean, BaseViewHolder> {

    private Context context;
    public static final int TYPE1 = 1;
    public static final int TYPE2 = 3;

    public AllEvaluationAdapter(List<EvaluationBean> data, Context context) {
        super(data);
        this.context = context;
        if (data != null && data.size() > 0) {
            addItemType(TYPE1, R.layout.all_evaluation_item);
            addItemType(TYPE2, R.layout.all_evaluation_item_end);
        }
    }

    @Override
    protected void convert(BaseViewHolder helper, EvaluationBean item) {
        switch (helper.getItemViewType()) {
            case TYPE1:
                helper.setText(R.id.all_evaluation_item_username, item.getUserName());
                helper.setText(R.id.all_evaluation_item_time, item.getTime());
                helper.setText(R.id.all_evaluation_item_desc, item.getDesc());
                helper.setText(R.id.all_evaluation_item_choose, item.getChoose());
                ImageView imgHead = helper.getView(R.id.all_evaluation_item_head);
                Picasso.with(context).load(item.getImgHead()).placeholder(R.mipmap.busi_loading).error(R.mipmap.busi_loading).into(imgHead);
                InitFlowLayout(helper, item);
                break;
        }
    }

    private void InitFlowLayout(final BaseViewHolder helper, final EvaluationBean item) {

        final TagFlowLayout flowLayout = helper.getView(R.id.all_evaluation_item_flow);
        final LayoutInflater mInflater = LayoutInflater.from(context);
        flowLayout.setAdapter(new TagAdapter<String>(item.getImgs()) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                ImageView imageView = (ImageView) mInflater.inflate(R.layout.all_evaluation_flow_item,
                        flowLayout, false);
                Picasso.with(context).load(s).placeholder(R.mipmap.busi_loading).error(R.mipmap.busi_loading).into(imageView);
                return imageView;
            }
        });
        flowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                Toast.makeText(context, item.getImgs()[position], Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }
}
