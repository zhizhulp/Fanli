package com.ascba.rebate.beans;


/**
 * Created by 李平 on 2017/4/11 0011.16:59
 * 代理专区的实体类
 */

public class ProxyDet {
    private int icon;
    private String title;
    private String content;

    public ProxyDet(int icon, String title, String content) {
        this.icon = icon;
        this.title = title;
        this.content = content;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
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
