package com.ascba.rebate.beans;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

/**
 * 商品规格实体类
 */

public class GoodsAttr implements MultiItemEntity {
    private int layout;
    private int type;//布局类型
    private List<Attrs> strs;
    private String Title;//商品标题
    private boolean isSelect;//子类中是否有选择

    public GoodsAttr(int layout, int type,  List<Attrs> strs,boolean isSelect) {//用于商品规格
        this.layout = layout;
        this.type = type;
        this.strs = strs;
        this.isSelect=isSelect;
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

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
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

    public class Attrs {
        private String content;
        private int textStatus;
        private boolean hasCheck;
        private int itemId;

        public boolean isHasCheck() {
            return hasCheck;
        }

        public void setHasCheck(boolean hasCheck) {
            this.hasCheck = hasCheck;
        }

        public Attrs() {

        }

        public Attrs(int itemId,String content, int textStatus,boolean hasCheck) {
            this.itemId=itemId;
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

        public int getItemId() {
            return itemId;
        }

        public void setItemId(int itemId) {
            this.itemId = itemId;
        }
    }
}

