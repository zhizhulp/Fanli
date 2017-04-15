package com.ascba.rebate.beans;

/**
 * Created by 李鹏 on 2017/03/11 0011.
 */

public class NewsBean {
    private boolean isIcon;
    private String id;
    private String title;
    private String time;
    private String img;
    private String url;

    public NewsBean() {
    }

    public NewsBean(String id, String title, String time, String url) {
        this.id = id;
        this.title = title;
        this.time = time;
        this.url = url;
    }

    public NewsBean(String id, String title, String time) {
        this.title = title;
        this.time = time;
        this.id = id;
    }

    public NewsBean(String title, String img) {
        this.title = title;
        this.img = img;
    }

    public NewsBean(boolean isIcon, String title, String time) {
        this.isIcon = isIcon;
        this.title = title;
        this.time = time;
    }

    public boolean isIcon() {
        return isIcon;
    }

    public void setIcon(boolean icon) {
        isIcon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
