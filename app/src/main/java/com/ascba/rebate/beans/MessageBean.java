package com.ascba.rebate.beans;

/**
 * Created by 李鹏 on 2017/03/27 0027.
 */

public class MessageBean {
    private int drawable;
    private String title;
    private  String content;

    public MessageBean() {
    }

    public MessageBean(int drawable, String title, String content) {
        this.drawable = drawable;
        this.title = title;
        this.content = content;
    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
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
