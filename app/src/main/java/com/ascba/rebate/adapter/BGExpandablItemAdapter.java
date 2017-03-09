package com.ascba.rebate.adapter;

import android.util.Log;
import android.view.View;

import com.ascba.rebate.R;
import com.ascba.rebate.beans.BGExpandableLevel0Item;
import com.ascba.rebate.beans.BGExpandableLevel1Item;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

/**
 * Created by 李鹏 on 2017/03/08 0008.
 * 新手指南 adapter二级菜单
 */

public class BGExpandablItemAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

    public static final int TYPE_LEVEL_0 = 0;
    public static final int TYPE_LEVEL_1 = 1;
    public static final int TYPE_LEVEL_2 = 2;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public BGExpandablItemAdapter(List<MultiItemEntity> data) {
        super(data);
        addItemType(TYPE_LEVEL_0, R.layout.beginner_guide_expandable_lv0);
        addItemType(TYPE_LEVEL_1, R.layout.beginner_guide_expandable_lv1);
        addItemType(TYPE_LEVEL_2, R.layout.goods_details_cuttingline);
    }


    @Override
    protected void convert(final BaseViewHolder holder, final MultiItemEntity item) {
        switch (holder.getItemViewType()) {
            case TYPE_LEVEL_0:
                final BGExpandableLevel0Item lv0 = (BGExpandableLevel0Item) item;
                holder.setText(R.id.beginner_guide_expandable_lv0_text, lv0.title);
                holder.setImageResource(R.id.beginner_guide_expandable_lv0_img, lv0.isExpanded() ? R.mipmap.bottom2_arrow : R.mipmap.right2_arrow);
                Log.d("BGExpandablItemAdapter", lv0.title);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = holder.getAdapterPosition();
                        if (lv0.isExpanded()) {
                            collapse(pos);
                        } else {
                            expand(pos);
                        }
                    }
                });
                break;
            case TYPE_LEVEL_1:
                final BGExpandableLevel1Item lv1 = (BGExpandableLevel1Item) item;
                holder.setText(R.id.beginner_guide_expandable_lv1_text, lv1.title);
                break;
        }
    }
}
