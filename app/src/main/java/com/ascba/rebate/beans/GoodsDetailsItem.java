package com.ascba.rebate.beans;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

/**
 * Created by 李鹏 on 2017/03/02 0002.
 * 商品详情实体类
 */

public class GoodsDetailsItem implements MultiItemEntity {

    public static final int TYPE_VIEWPAGER = 1;
    public static final int TYPE_GOODS_SIMPLE_DESC=2;

    public static final int TYPE_SPAN_SIZE_DEFAULT = 1;

    private int itemType = TYPE_SPAN_SIZE_DEFAULT;

    private int spanSize;
    private int resLat;//布局ID
    private List<String> pagerUrls;//viewPager数据源
    private String store_type;//店铺类型-自营店
    private String goods_title;//商品类型
    private String goods_desc;//商品详细内容
    private String price_new;//当前价格
    private String price_old;//旧价格
    private int ev_all;//宝贝评价
    private float ev_good;//好评率
    private String goods_logo;//商品logo
    private String store_name;//商店名称
    private int goods_all;//全部宝贝数量
    private int goods_recomm;//达人推荐数量
    private float ev_desc;//描述相符
    private float ev_service;//服务态度
    private float ev_delivery;//发货速度

    @Override
    public int getItemType() {
        return 0;
    }

    /**
     * 商品轮播展示
     * @param itemType 类型
     * @param resLat 布局id
     * @param pagerUrls 图片地址
     */
    public GoodsDetailsItem(int itemType,  int resLat, List<String> pagerUrls) {
        this.itemType = itemType;
        this.resLat = resLat;
        this.pagerUrls = pagerUrls;
    }

    /**
     * 商品简单介绍
     * @param itemType 类型
     * @param resLat 布局id
     * @param store_type 商店类型
     * @param goods_title 商品类型
     * @param goods_desc 商品详细内容
     * @param price_new 当前价格
     * @param price_old 旧价格
     */
    public GoodsDetailsItem(int itemType, int resLat, String store_type, String goods_title, String goods_desc, String price_new, String price_old) {
        this.itemType = itemType;
        this.resLat = resLat;
        this.store_type = store_type;
        this.goods_title = goods_title;
        this.goods_desc = goods_desc;
        this.price_new = price_new;
        this.price_old = price_old;
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

    public String getStore_type() {
        return store_type;
    }

    public void setStore_type(String store_type) {
        this.store_type = store_type;
    }

    public String getGoods_title() {
        return goods_title;
    }

    public void setGoods_title(String goods_title) {
        this.goods_title = goods_title;
    }

    public String getGoods_desc() {
        return goods_desc;
    }

    public void setGoods_desc(String goods_desc) {
        this.goods_desc = goods_desc;
    }

    public String getPrice_new() {
        return price_new;
    }

    public void setPrice_new(String price_new) {
        this.price_new = price_new;
    }

    public String getPrice_old() {
        return price_old;
    }

    public void setPrice_old(String price_old) {
        this.price_old = price_old;
    }

    public int getEv_all() {
        return ev_all;
    }

    public void setEv_all(int ev_all) {
        this.ev_all = ev_all;
    }

    public float getEv_good() {
        return ev_good;
    }

    public void setEv_good(float ev_good) {
        this.ev_good = ev_good;
    }

    public String getGoods_logo() {
        return goods_logo;
    }

    public void setGoods_logo(String goods_logo) {
        this.goods_logo = goods_logo;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public int getGoods_all() {
        return goods_all;
    }

    public void setGoods_all(int goods_all) {
        this.goods_all = goods_all;
    }

    public int getGoods_recomm() {
        return goods_recomm;
    }

    public void setGoods_recomm(int goods_recomm) {
        this.goods_recomm = goods_recomm;
    }

    public float getEv_desc() {
        return ev_desc;
    }

    public void setEv_desc(float ev_desc) {
        this.ev_desc = ev_desc;
    }

    public float getEv_service() {
        return ev_service;
    }

    public void setEv_service(float ev_service) {
        this.ev_service = ev_service;
    }

    public float getEv_delivery() {
        return ev_delivery;
    }

    public void setEv_delivery(float ev_delivery) {
        this.ev_delivery = ev_delivery;
    }
}
