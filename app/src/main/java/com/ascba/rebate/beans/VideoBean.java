package com.ascba.rebate.beans;

import android.view.View;

/**
 * Created by 李鹏 on 2017/03/20 0020.
 */

public class VideoBean {
    private String videoUrl;
    private String imgUrl;
    private String title;
    private View view;

    public VideoBean() {
    }

    public VideoBean(String videoUrl, String imgUrl, String title, View view) {
        this.videoUrl = videoUrl;
        this.imgUrl = imgUrl;
        this.title = title;
        this.view = view;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }
}
