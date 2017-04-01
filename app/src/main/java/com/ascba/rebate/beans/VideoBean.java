package com.ascba.rebate.beans;

/**
 * Created by 李鹏 on 2017/03/21 0021.
 */

public class VideoBean {
    private String ImgUrl;
    private String VideoUrl;
    private String ImgTitle;

    public VideoBean() {
    }

    public VideoBean(String imgUrl, String videoUrl) {
        ImgUrl = imgUrl;
        VideoUrl = videoUrl;
    }

    public VideoBean(String imgUrl, String videoUrl, String imgTitle) {
        ImgUrl = imgUrl;
        VideoUrl = videoUrl;
        ImgTitle = imgTitle;
    }

    public String getImgTitle() {
        return ImgTitle;
    }

    public void setImgTitle(String imgTitle) {
        ImgTitle = imgTitle;
    }

    public String getImgUrl() {
        return ImgUrl;
    }

    public void setImgUrl(String imgUrl) {
        ImgUrl = imgUrl;
    }

    public String getVideoUrl() {
        return VideoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        VideoUrl = videoUrl;
    }
}
