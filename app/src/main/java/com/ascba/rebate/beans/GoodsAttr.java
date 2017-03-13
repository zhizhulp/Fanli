package com.ascba.rebate.beans;

import java.util.List;

/**
 * 商品规格实体类
 */

public class GoodsAttr {
    private String Title;

    private List<String> strs;

    public GoodsAttr(String title, List<String> strs) {
        Title = title;
        this.strs = strs;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public List<String> getStrs() {
        return strs;
    }

    public void setStrs(List<String> strs) {
        this.strs = strs;
    }
}

