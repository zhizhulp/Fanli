package com.ascba.rebate.beans;

import java.util.List;

/**
 * 购物车商品实体类
 */

public class Goods {
    private int titleId;//父id
    private String imgUrl;//缩略图链接
    private String goodsTitle;//商品标题
    private String goodsStandard;//商品规格
    private String goodsPrice;//商品价格
    private String goodsPriceOld;//商品原价/市场价
    private int userQuy;//用户选择数量
    private String goodsSelled;//商品已售数量
    private List<GoodsImgBean> imgBeanList;//商品详情广告轮播
    private String goodsNumber;//商品编号
    private int storeId;//店铺id
    private int brand;//品牌id
    private int inventory;//总库存
    private int weight;//重量g
    private int freightPrice;//运费

    public Goods() {
    }

    public Goods(String imgUrl, String goodsTitle, String goodsStandard, String goodsPrice, String goodsPriceOld, int userQuy) {
        this.imgUrl = imgUrl;
        this.goodsTitle = goodsTitle;
        this.goodsStandard = goodsStandard;
        this.goodsPrice = goodsPrice;
        this.goodsPriceOld = goodsPriceOld;
        this.userQuy = userQuy;
    }

    public Goods(String imgUrl, String goodsTitle, String goodsStandard, String goodsPrice, int userQuy, int titleId) {
        this.imgUrl = imgUrl;
        this.goodsTitle = goodsTitle;
        this.goodsStandard = goodsStandard;
        this.goodsPrice = goodsPrice;
        this.userQuy = userQuy;
        this.titleId=titleId;
    }

    public int getTitle() {
        return titleId;
    }

    public void setTitle(int title) {
        this.titleId = title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getGoodsTitle() {
        return goodsTitle;
    }

    public void setGoodsTitle(String goodsTitle) {
        this.goodsTitle = goodsTitle;
    }

    public String getGoodsStandard() {
        return goodsStandard;
    }

    public void setGoodsStandard(String goodsStandard) {
        this.goodsStandard = goodsStandard;
    }

    public String getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(String goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public int getUserQuy() {
        return userQuy;
    }

    public void setUserQuy(int userQuy) {
        this.userQuy = userQuy;
    }

    public String getGoodsSelled() {
        return goodsSelled;
    }

    public void setGoodsSelled(String goodsSelled) {
        this.goodsSelled = goodsSelled;
    }

    public String getGoodsPriceOld() {
        return goodsPriceOld;
    }

    public void setGoodsPriceOld(String goodsPriceOld) {
        this.goodsPriceOld = goodsPriceOld;
    }

    public List<GoodsImgBean> getImgBeanList() {
        return imgBeanList;
    }

    public void setImgBeanList(List<GoodsImgBean> imgBeanList) {
        this.imgBeanList = imgBeanList;
    }

    public int getTitleId() {
        return titleId;
    }

    public void setTitleId(int titleId) {
        this.titleId = titleId;
    }

    public String getGoodsNumber() {
        return goodsNumber;
    }

    public void setGoodsNumber(String goodsNumber) {
        this.goodsNumber = goodsNumber;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getBrand() {
        return brand;
    }

    public void setBrand(int brand) {
        this.brand = brand;
    }

    public int getInventory() {
        return inventory;
    }

    public void setInventory(int inventory) {
        this.inventory = inventory;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getFreightPrice() {
        return freightPrice;
    }

    public void setFreightPrice(int freightPrice) {
        this.freightPrice = freightPrice;
    }
}
