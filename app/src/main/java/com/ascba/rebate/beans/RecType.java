package com.ascba.rebate.beans;

/**
 * 推荐poplist实体类
 */

public class RecType {
    private boolean select;
    private String content;

    public RecType(boolean select, String content) {
        this.select = select;
        this.content = content;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
