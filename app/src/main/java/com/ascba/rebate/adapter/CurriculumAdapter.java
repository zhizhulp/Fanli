package com.ascba.rebate.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.beans.CurriculumBean;
import com.ascba.rebate.view.pagerWithTurn.ShufflingViewPager;
import com.ascba.rebate.view.pagerWithTurn.ShufflingViewPagerAdapter;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by 李鹏 on 2017/03/16 0016.
 * 全部课程
 */

public class CurriculumAdapter extends BaseMultiItemQuickAdapter<CurriculumBean, BaseViewHolder> {

    private Context context;
    public static final int TYPE0 = 0;
    public static final int TYPE1 = 1;
    public static final int TYPE2 = 2;

    public CurriculumAdapter(List<CurriculumBean> data, Context context) {
        super(data);
        this.context = context;
        if (data != null && data.size() > 0) {
            for (CurriculumBean bean : data) {
                addItemType(bean.getItemType(), bean.getLayout());
            }
        }
    }

    @Override
    protected void convert(BaseViewHolder helper, CurriculumBean item) {
        switch (helper.getItemViewType()) {
            case 0:
                ShufflingViewPager shufflingViewPager = helper.getView(R.id.shop_pager);
                ShufflingViewPagerAdapter shufflingViewPagerAdapter = new ShufflingViewPagerAdapter(context, item.getStringList());
                shufflingViewPager.setAdapter(shufflingViewPagerAdapter);
                shufflingViewPager.start();
                break;
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
                Glide.with(context).load(item.getImgUrl()).into(imageView);
                helper.setText(R.id.curriculum_content_title, item.getContentTitle());
                helper.setText(R.id.curriculum_content_state, item.getContentState());
                break;
        }
    }
}
