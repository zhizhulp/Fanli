package com.ascba.rebate.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.beans.CurriculumBean;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by 李鹏 on 2017/03/20 0020.
 */

public class MyEvaluationAdapter extends BaseMultiItemQuickAdapter<CurriculumBean, BaseViewHolder> {

    private Context context;
    public static final int TYPE1 = 1;
    public static final int TYPE2 = 2;

    public MyEvaluationAdapter(List<CurriculumBean> data, Context context) {
        super(data);
        this.context = context;
        for (CurriculumBean bean : data) {
            addItemType(bean.getItemType(), bean.getLayout());
        }
    }

    @Override
    protected void convert(BaseViewHolder helper, CurriculumBean item) {
        switch (helper.getItemViewType()) {
            case TYPE1:
                helper.setText(R.id.curriculum_title_left, item.getTitle());
                TextView state = helper.getView(R.id.curriculum_title_right);
                if (item.isMore()) {
                    state.setVisibility(View.VISIBLE);
                } else {
                    state.setVisibility(View.INVISIBLE);
                }
                break;
            case TYPE2:
                ImageView imageView = helper.getView(R.id.curriculum_content_img);
                helper.addOnClickListener(R.id.curriculum_content_img);
                Picasso.with(context).load(item.getImgUrl()).placeholder(R.mipmap.loading_rect).error(R.mipmap.loading_rect).into(imageView);
                helper.setText(R.id.curriculum_content_title, item.getContentTitle());
                helper.setText(R.id.curriculum_content_state, item.getContentState());
                break;
        }
    }
}
