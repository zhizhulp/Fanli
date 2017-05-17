package com.ascba.rebate.beans;

/**
 * Created by 李鹏 on 2017/03/02 0002.
 * 商品详情实体类
 */

public class GoodsDetailsItem {


    private String goods_desc;//商品详细内容
    private String price_new;//当前价格
    private String imgUrl;//商品首页图
    private String goodsUrl;//商品URL
    private int id;//唯一标示


    /**
     * 足迹/首页数据
     * @param imgUrl 首页展示图片地址
     * @param goods_desc 商品名称
     * @param price_new 价格
     * @param goodsUrl 商品地址
     */
    public GoodsDetailsItem( int id,String imgUrl, String goods_desc, String price_new, String goodsUrl) {
        this.id=id;
        this.imgUrl = imgUrl;
        this.goods_desc = goods_desc;
        this.price_new = price_new;
        this.goodsUrl = goodsUrl;
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

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getGoodsUrl() {
        return goodsUrl;
    }

    public void setGoodsUrl(String goodsUrl) {
        this.goodsUrl = goodsUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
