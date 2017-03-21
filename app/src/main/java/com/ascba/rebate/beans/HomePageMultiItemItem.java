package com.ascba.rebate.beans;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

/**
 * Created by 李鹏 on 2017/03/11 0011.
 */

public class HomePageMultiItemItem implements MultiItemEntity {
    private int type;
    private int layout;
    public static final int TYPE1 = 1;//轮播广告
    private List<String> list;

    public static final int TYPE2 = 2;//花钱  赚钱

    public static final int TYPE3 = 3;//ASK商学院  创业扶持

    public static final int TYPE4 = 4;//分割线

    public static final int TYPE5 = 5;//券购商城

    public static final int TYPE6 = 6;//全球券购 天天特价 品牌精选

    public static final int TYPE7 = 7;//宽分割线

    public static final int TYPE8 = 8;//ASK资讯

    public static final int TYPE9 = 9;//视频

    public static final int TYPE10 = 10;//最新动态


    /**
     * 新闻
     */
    private List<NewsBean> newsBeens;
    private NewsBean bean;
    public static final int TYPE11 = 11;
    public static final int TYPE12 = 12;

    private String title;

    private List<VideoBean> videoList;

    public HomePageMultiItemItem() {
    }

    /**
     * 广告轮播
     *
     * @param type
     * @param layout
     * @param list
     */
    public HomePageMultiItemItem(int type, int layout, List<String> list) {
        this.type = type;
        this.layout = layout;
        this.list = list;
    }

    /**
     * @param type
     * @param layout
     */
    public HomePageMultiItemItem(int type, int layout) {
        this.type = type;
        this.layout = layout;
    }

    public HomePageMultiItemItem(List<VideoBean> videoList, int type, int layout) {
        this.videoList = videoList;
        this.type = type;
        this.layout = layout;
    }

    /**
     * 标题
     *
     * @param type
     * @param layout
     * @param title
     */
    public HomePageMultiItemItem(int type, int layout, String title) {
        this.type = type;
        this.layout = layout;
        this.title = title;
    }


    public List<NewsBean> getNewsBeens() {
        return newsBeens;
    }

    public void setNewsBeens(List<NewsBean> newsBeens) {
        this.newsBeens = newsBeens;
    }


    public int getLayout() {
        return layout;
    }

    public void setLayout(int layout) {
        this.layout = layout;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<VideoBean> getVideoList() {
        return videoList;
    }

    public void setVideoList(List<VideoBean> videoList) {
        this.videoList = videoList;
    }

    @Override
    public int getItemType() {
        return type;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<NewsBean> getNewsBeen() {
        return newsBeens;
    }

    public void setNewsBeen(List<NewsBean> newsBeen) {
        this.newsBeens = newsBeen;
    }

    /**
     * 新闻
     *
     * @param type
     * @param layout
     * @param newsBeens
     */
    public HomePageMultiItemItem(int type, List<NewsBean> newsBeens, int layout) {
        this.type = type;
        this.layout = layout;
        this.newsBeens = newsBeens;
    }

    public HomePageMultiItemItem(int type, NewsBean bean, int layout) {
        this.type = type;
        this.layout = layout;
        this.bean = bean;
    }

    public NewsBean getBean() {
        return bean;
    }

    public void setBean(NewsBean bean) {
        this.bean = bean;
    }
}
