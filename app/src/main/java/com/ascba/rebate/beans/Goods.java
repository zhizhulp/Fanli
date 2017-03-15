package com.ascba.rebate.beans;

/**
 * 购物车商品实体类
 */

public class Goods {
    private int titleId;//父id
    private String imgUrl;//缩略图链接
    private String goodsTitle;//商品标题
    private String goodsStandard;//商品规格
    private String goodsPrice;//商品价格
    private String goodsPriceOld;//商品原价
    private String userQuy;//用户选择数量
    private String goodsSelled;//商品已售数量

    public Goods() {
    }

    public Goods(String imgUrl, String goodsTitle, String goodsStandard, String goodsPrice, String goodsPriceOld, String userQuy) {
        this.imgUrl = imgUrl;
        this.goodsTitle = goodsTitle;
        this.goodsStandard = goodsStandard;
        this.goodsPrice = goodsPrice;
        this.goodsPriceOld = goodsPriceOld;
        this.userQuy = userQuy;
    }

    public Goods(String imgUrl, String goodsTitle, String goodsStandard, String goodsPrice, String userQuy, int titleId) {
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

    public String getUserQuy() {
        return userQuy;
    }

    public void setUserQuy(String userQuy) {
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
}
