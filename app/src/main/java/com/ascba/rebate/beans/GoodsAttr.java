package com.ascba.rebate.beans;

import java.util.List;

/**
 * 商品规格实体类
 */

public class GoodsAttr {
    private String Title;

    private List<Attrs> strs;

    public GoodsAttr(String title, List<Attrs> strs) {
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

    public class Attrs{
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

