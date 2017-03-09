package com.ascba.rebate.beans;

import com.ascba.rebate.adapter.BGExpandablItemAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by 李鹏 on 2017/03/08 0008.
 */

public class BGExpandableLevel1Item implements MultiItemEntity {
    public String title;

    public BGExpandableLevel1Item(String title) {
        this.title = title;
    }

    @Override
    public int getItemType() {
        return BGExpandablItemAdapter.TYPE_LEVEL_1;
    }

}