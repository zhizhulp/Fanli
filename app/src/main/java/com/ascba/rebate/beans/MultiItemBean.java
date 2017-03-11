package com.ascba.rebate.beans;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by 李鹏 on 2017/03/11 0011.
 */

public class MultiItemBean implements MultiItemEntity {
    private int type;
    private int weight;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public int getItemType() {
        return type;
    }

}
