package com.ascba.rebate.beans;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

/**
 * 商品规格实体类
 */

public class GoodsAttr implements MultiItemEntity {
    private int layout;
    private int type;//布局类型
    private String Title;//商品分类title(鞋码，尺寸...)
    private String imgUrl;//商品缩略图
    private float unitPrice;//商品单价
    private int inventory;//商品库存
    private String desc;//商品规格描述
    private List<Attrs> strs;

    public GoodsAttr(int layout, int type, String title, String imgUrl, float unitPrice,
                     int inventory, String desc, List<Attrs> strs) {//用于商品规格
        this.layout = layout;
        this.type = type;
        Title = title;
        this.imgUrl = imgUrl;
        this.unitPrice = unitPrice;
        this.inventory = inventory;
        this.desc = desc;
        this.strs = strs;
    }

    public GoodsAttr(int type, int layout) {
        this.type = type;
        this.layout = layout;
    }

    public GoodsAttr(int type, int layout, String title) {
        this.type = type;
        Title = title;
        this.layout = layout;
    }

    public GoodsAttr(int type, String title, List<Attrs> strs) {
        this.type = type;
        Title = title;
        this.strs = strs;
    }

    public GoodsAttr(int type, int layout, String title, List<Attrs> strs) {
        this.type = type;
        this.layout = layout;
        Title = title;
        this.strs = strs;
    }

    public GoodsAttr() {
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public List<Attrs> getStrs() {
        return strs;
    }

    public void setStrs(List<Attrs> strs) {
        this.strs = strs;
    }

    @Override
    public int getItemType() {
        return type;
    }

    public int getLayout() {
        return layout;
    }

    public void setLayout(int layout) {
        this.layout = layout;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public float getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(float unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getInventory() {
        return inventory;
    }

    public void setInventory(int inventory) {
        this.inventory = inventory;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public class Attrs {
        private String content;
        private int textStatus;
        private boolean hasCheck;

        public boolean isHasCheck() {
            return hasCheck;
        }

        public void setHasCheck(boolean hasCheck) {
            this.hasCheck = hasCheck;
        }

        public Attrs() {
        }

        public Attrs(String content, int textStatus,boolean hasCheck) {
            this.content = content;
            this.textStatus = textStatus;
            this.hasCheck=hasCheck;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getTextStatus() {
            return textStatus;
        }

        public void setTextStatus(int textStatus) {
            this.textStatus = textStatus;
        }
    }
}

