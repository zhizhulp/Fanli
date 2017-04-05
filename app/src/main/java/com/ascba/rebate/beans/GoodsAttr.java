package com.ascba.rebate.beans;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

/**
 * 商品规格实体类
 */

public class GoodsAttr implements MultiItemEntity {
    private String Title;
    private int layout;
    private List<Attrs> strs;
    private int type;

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

    public class Attrs {
        private String content;
        private int textColor;

        public Attrs() {
        }

        public Attrs(String content, int textColor) {
            this.content = content;
            this.textColor = textColor;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getTextColor() {
            return textColor;
        }

        public void setTextColor(int textColor) {
            this.textColor = textColor;
        }
    }
}

