package com.ascba.rebate.beans;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

/**
 * 商城所有实体类的基类(用到recyclerView)
 */
public class ShopBaseItem implements MultiItemEntity {
    private int itemType;
    private int spanSize;
    private int resLat;//布局ID
    private List<String> pagerUrls;//viewPager数据源
    private float lineWidth;//横线粗细
    //导航栏
    private int resId;//本地图片
    private String desc;//描述

    private String url;//网络图片
    private String title;//标题
    private int color;//背景色

    private String saled;//商品已出售
    //限量抢购
    private List<String> titles;
    private List<String> contents;
    private List<String> descs;

    private ShopBaseItem(int itemType, int spanSize, int resLat,List<String> titles, List<String> contents
            ,List<String> descs,List<String> pagerUrls) {
        this(itemType, spanSize, resLat);
        this.titles=titles;
        this.contents=contents;
        this.descs=descs;
        this.pagerUrls=pagerUrls;
    }
    private ShopBaseItem(int itemType, int spanSize, int resLat){
        this.itemType = itemType;
        this.spanSize = spanSize;
        this.resLat = resLat;
    }

    /**
     * viewPager实体类
     * @param itemType  数据类型
     * @param spanSize  比重
     * @param resLat    对应的布局id
     * @param pagerUrls viewPager网络图片集合
     */
    public ShopBaseItem(int itemType, int spanSize, int resLat, List<String> pagerUrls) {
        this(itemType, spanSize, resLat);
        this.pagerUrls = pagerUrls;

    }

    /**
     * 横线 实体类
     *
     * @param itemType  数据类型
     * @param spanSize  比重
     * @param resLat    对应的布局id
     * @param lineWidth 线的粗细
     */
    public ShopBaseItem(int itemType, int spanSize, int resLat, float lineWidth) {
        this(itemType, spanSize, resLat);
        this.lineWidth = lineWidth;
    }

    /**
     * 商品分类导航实体类
     *
     * @param itemType 数据类型
     * @param spanSize 比重
     * @param resLat   对应的布局id
     * @param url      网络图片
     * @param desc     描述
     */
    public ShopBaseItem(int itemType, int spanSize, int resLat, String url, String desc) {
        this(itemType, spanSize, resLat);
        this.url = url;
        this.desc = desc;
    }

    /**
     * 标题
     *
     * @param itemType 数据类型
     * @param spanSize 比重
     * @param resLat   对应的布局id
     * @param url      网络图片
     * @param title    标题
     * @param color    字体颜色
     */
    public ShopBaseItem(int itemType, int spanSize, int resLat, String url, String title, int color) {
        this(itemType, spanSize, resLat);
        this.url = url;
        this.title = title;
        this.color = color;
    }

    /**
     * 图片+文字 实体类
     *
     * @param itemType 数据类型
     * @param spanSize 比重
     * @param resLat   对应的布局id
     * @param url      网络图片地址
     */
    public ShopBaseItem(int itemType, int spanSize, int resLat, String url) {
        this(itemType, spanSize, resLat);
        this.url = url;
    }

    /**
     * 首页（今日更新）
     *
     * @param itemType 数据类型
     * @param spanSize 比重
     * @param resLat   对应的布局id
     * @param url      网络图片地址
     * @param title    标题
     * @param desc     描述
     * @param color    背景色
     */
    public ShopBaseItem(int itemType, int spanSize, int resLat, String url, String title, String desc, int color) {
        this(itemType, spanSize, resLat, url);
        this.title = title;
        this.desc = desc;
        this.color = color;
    }

    /**
     * 商品 实体类
     *
     * @param itemType 数据类型
     * @param spanSize 比重
     * @param resLat   对应的布局id
     * @param url      网络图片地址
     * @param title    标题
     * @param desc     描述
     * @param saled    描述
     */
    public ShopBaseItem(int itemType, int spanSize, int resLat, String url, String title, String desc, String saled) {
        this(itemType, spanSize, resLat, url);
        this.title = title;
        this.desc = desc;
        this.saled = saled;
    }

    /**
     * 商品 实体类
     *
     * @param url   网络图片地址
     * @param title 标题
     * @param desc  描述
     * @param saled 描述
     */
    public ShopBaseItem(String url, String title, String desc, String saled) {
        this.url = url;
        this.title = title;
        this.desc = desc;
        this.saled = saled;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public int getSpanSize() {
        return spanSize;
    }

    public void setSpanSize(int spanSize) {
        this.spanSize = spanSize;
    }

    public int getResLat() {
        return resLat;
    }

    public void setResLat(int resLat) {
        this.resLat = resLat;
    }

    public List<String> getPagerUrls() {
        return pagerUrls;
    }

    public void setPagerUrls(List<String> pagerUrls) {
        this.pagerUrls = pagerUrls;
    }

    public float getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSaled() {
        return saled;
    }

    public void setSaled(String saled) {
        this.saled = saled;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
    }

    public List<String> getTitles() {
        return titles;
    }

    public void setTitles(List<String> titles) {
        this.titles = titles;
    }

    public List<String> getContents() {
        return contents;
    }

    public void setContents(List<String> contents) {
        this.contents = contents;
    }

    public List<String> getDescs() {
        return descs;
    }

    public void setDescs(List<String> descs) {
        this.descs = descs;
    }
}
