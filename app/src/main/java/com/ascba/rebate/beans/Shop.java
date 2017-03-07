package com.ascba.rebate.beans;

import java.util.List;

/**
 * 购物车商品实体类
 */

public class Shop {
    private List<Goods> goodsList;
    private String shopTitle;//商店名称

    public Shop(List<Goods> goodsList, String shopTitle) {
        this.goodsList = goodsList;
        this.shopTitle = shopTitle;
    }

    public class Goods{
        private String imgUrl;//缩略图链接
        private String goodsTitle;//商品标题
        private String goodsStandard;//商品规格
        private String goodsPrice;//商品价格
        private String userQuy;//用户选择数量

        public Goods(String imgUrl, String goodsTitle, String goodsStandard, String goodsPrice, String userQuy) {
            this.imgUrl = imgUrl;
            this.goodsTitle = goodsTitle;
            this.goodsStandard = goodsStandard;
            this.goodsPrice = goodsPrice;
            this.userQuy = userQuy;
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
    }




    public String getShopTitle() {
        return shopTitle;
    }

    public void setShopTitle(String shopTitle) {
        this.shopTitle = shopTitle;
    }

    public List<Goods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<Goods> goodsList) {
        this.goodsList = goodsList;
    }
}
