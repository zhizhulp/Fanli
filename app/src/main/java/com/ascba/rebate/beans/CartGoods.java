package com.ascba.rebate.beans;

import com.chad.library.adapter.base.entity.SectionEntity;

/**
 * 购物车商品section实体类
 */

public class CartGoods extends SectionEntity<Goods> {
    private int id;
    private boolean check;

    public CartGoods(boolean isHeader, String header,int id,boolean check) {
        super(isHeader, header);
        this.id=id;
        this.check=check;
    }

    public CartGoods(Goods goods,int id,boolean check) {
        super(goods);
        this.id=id;
        this.check=check;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
