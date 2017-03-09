package com.ascba.rebate.beans;

import com.ascba.rebate.adapter.BGExpandablItemAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by 李鹏 on 2017/03/08 0008.
 */

public class BGExpandableLevel implements MultiItemEntity {

    public BGExpandableLevel() {
    }

    @Override
    public int getItemType() {
        return BGExpandablItemAdapter.TYPE_LEVEL_2;
    }

}