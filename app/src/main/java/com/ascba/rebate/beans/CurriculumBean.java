package com.ascba.rebate.beans;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

/**
 * Created by 李鹏 on 2017/03/16 0016.
 * 全部课程
 */

public class CurriculumBean implements MultiItemEntity {

    private int type;
    private int layout;
    private String title;
    private boolean isMore;
    private String imgUrl;
    private String contentTitle;
    private String contentState;
    private List<String> stringList;

    public CurriculumBean(int type, int layout, List<String> stringList) {
        this.type = type;
        this.layout = layout;
        this.stringList = stringList;
    }

    public CurriculumBean(int type, int layout, String title, boolean isMore) {
        this.type = type;
        this.layout = layout;
        this.title = title;
        this.isMore = isMore;
    }

    public CurriculumBean(int type, int layout, String imgUrl, String contentTitle, String contentState) {
        this.type = type;
        this.layout = layout;
        this.imgUrl = imgUrl;
        this.contentTitle = contentTitle;
        this.contentState = contentState;
    }

    public List<String> getStringList() {
        return stringList;
    }

    public void setStringList(List<String> stringList) {
        this.stringList = stringList;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getLayout() {
        return layout;
    }

    public void setLayout(int layout) {
        this.layout = layout;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean more) {
        isMore = more;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getContentTitle() {
        return contentTitle;
    }

    public void setContentTitle(String contentTitle) {
        this.contentTitle = contentTitle;
    }

    public String getContentState() {
        return contentState;
    }

    public void setContentState(String contentState) {
        this.contentState = contentState;
    }

    @Override
    public int getItemType() {
        return type;
    }
}
