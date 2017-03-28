package com.ascba.rebate.beans;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by 李鹏 on 2017/03/15 0015.
 * 订单实体类
 */

public class OrderBean implements MultiItemEntity {

    private Goods goods;//订单商品列表
    private String time;//订单时间
    private String state;//订单状态
    private String goodsNum;//订单商品数量
    private String orderPrice;//订单总价
    private String freight;//运费
    private int type;
    private int layout;
    private String id;//订单号
    private String imgUrl;//订单商品缩略图

    public OrderBean(String time, String id, String imgUrl) {
        this.time = time;
        this.id = id;
        this.imgUrl = imgUrl;
    }

    public OrderBean(int type, int layout, String time, String state) {
        this.type = type;
        this.layout = layout;
        this.time = time;
        this.state = state;
    }

    public OrderBean(int type, int layout, Goods goods) {
        this.type = type;
        this.layout = layout;
        this.goods = goods;
    }

    public OrderBean(int type, int layout, String goodsNum, String orderPrice, String freight) {
        this.type = type;
        this.layout = layout;
        this.goodsNum = goodsNum;
        this.orderPrice = orderPrice;
        this.freight = freight;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(String goodsNum) {
        this.goodsNum = goodsNum;
    }

    public String getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(String orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getFreight() {
        return freight;
    }

    public void setFreight(String freight) {
        this.freight = freight;
    }

    public int getLayout() {
        return layout;
    }

    public void setLayout(int layout) {
        this.layout = layout;
    }

    @Override

    public int getItemType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
