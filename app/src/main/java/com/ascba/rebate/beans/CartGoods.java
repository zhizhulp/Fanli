package com.ascba.rebate.beans;

import com.chad.library.adapter.base.entity.SectionEntity;

/**
 * 购物车商品section实体类
 */

public class CartGoods extends SectionEntity<Goods> {

    public CartGoods(boolean isHeader, String header) {
        super(isHeader, header);
    }

    public CartGoods(Goods goods) {
        super(goods);
    }
}
