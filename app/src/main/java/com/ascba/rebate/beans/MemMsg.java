package com.ascba.rebate.beans;

/**
 * 学员证信息实体类
 */

public class MemMsg {
    private String title;
    private String content;

    public MemMsg(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
