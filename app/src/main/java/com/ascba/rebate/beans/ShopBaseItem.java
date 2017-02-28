package com.ascba.rebate.beans;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * 商城所有实体类的基类
 */

public class ShopBaseItem implements MultiItemEntity {
    private int itemType;//item数量
    public ShopBaseItem(int itemType) {
        this.itemType = itemType;
    }
    @Override
    public int getItemType() {
        return itemType;
    }
}
