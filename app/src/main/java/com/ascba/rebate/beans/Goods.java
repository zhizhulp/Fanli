package com.ascba.rebate.beans;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import org.w3c.dom.ProcessingInstruction;

import java.util.List;

/**
 * 购物车商品实体类
 */

public class Goods implements MultiItemEntity {
    private int titleId;//
    private String goodsId;//商品id
    private String imgUrl;//缩略图链接
    private String goodsTitle;//商品标题
    private String goodsStandard;//商品规格
    private String goodsPrice;//商品价格
    private String goodsPriceOld;//商品原价/市场价
    private String cartId;//商品购物车id
    private int userQuy;//用户选择数量
    private String goodsSelled;//商品已售数量
    private List<GoodsImgBean> imgBeanList;//商品详情广告轮播
    private String goodsNumber;//商品编号
    private String store;//店铺名称
    private int storeId;//店铺id
    private int brand;//品牌id
    private int inventory;//总库存
    private int weight;//重量g
    private String freightPrice;//运费
    private String totalPrice;//总价(数量*单价+数量*单价***)
    private int num;//商品数量总数
    private String specKeys;//商品对应的描述id145_147_151
    private String specNames;//品对应的描述文字
    private int type;
    private int layout;
    private String messageCart;//买家留言，购物车id信息拼接字符串
    private boolean hasStandard;//是否有规格
    private String orderGoodsId;
    private String subtract;//对应订单的 实际立减
    private String subDesc;//满减描述 对应订单的 满500立减200礼品券
    private String deliverNum;//运单号码
    private String teiHui;//特惠信息
    private String useTicketToReduce;//用券立减信息

    public String getDeliverNum() {
        return deliverNum;
    }

    public void setDeliverNum(String deliverNum) {
        this.deliverNum = deliverNum;
    }

    public String getTeiHui() {
        return teiHui;
    }

    public void setTeiHui(String teiHui) {
        this.teiHui = teiHui;
    }

    public String getUseTicketToReduce() {
        return useTicketToReduce;
    }

    public void setUseTicketToReduce(String userTicketToReduce) {
        this.useTicketToReduce = userTicketToReduce;
    }

    public Goods() {
    }

    public Goods(int type, int layout, String store) {
        this.type = type;
        this.layout = layout;
        this.store = store;
    }

    public Goods(int type, int layout, String freightPrice, int num, String totalPrice, int storeId, String messageCart) {
        this.type = type;
        this.layout = layout;
        this.freightPrice = freightPrice;
        this.num = num;
        this.totalPrice = totalPrice;
        this.storeId = storeId;
        this.messageCart = messageCart;
    }

    public Goods(String imgUrl, String goodsTitle, String goodsStandard, String goodsPrice, String goodsPriceOld, int userQuy) {
        this.imgUrl = imgUrl;
        this.goodsTitle = goodsTitle;
        this.goodsStandard = goodsStandard;
        this.goodsPrice = goodsPrice;
        this.goodsPriceOld = goodsPriceOld;
        this.userQuy = userQuy;
    }

    public Goods(String imgUrl, String goodsTitle, String goodsStandard, String goodsPrice, int userQuy) {
        this.imgUrl = imgUrl;
        this.goodsTitle = goodsTitle;
        this.goodsStandard = goodsStandard;
        this.goodsPrice = goodsPrice;
        this.userQuy = userQuy;
    }

    public Goods(int type, int layout, String imgUrl, String goodsTitle, String goodsStandard, String goodsPrice, String goodsPriceOld, int userQuy) {
        this.type = type;
        this.layout = layout;
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
        this.titleId = titleId;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public boolean isHasStandard() {
        return hasStandard;
    }

    public void setHasStandard(boolean hasStandard) {
        this.hasStandard = hasStandard;
    }

    public String getSpecNames() {
        return specNames;
    }

    public void setSpecNames(String specNames) {
        this.specNames = specNames;
    }

    public int getType() {
        return type;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
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

    public String getFreightPrice() {
        return freightPrice;
    }

    public void setFreightPrice(String freightPrice) {
        this.freightPrice = freightPrice;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getLayout() {
        return layout;
    }

    public void setLayout(int layout) {
        this.layout = layout;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @Override
    public int getItemType() {
        return type;
    }

    public String getSpecKeys() {
        return specKeys;
    }

    public void setSpecKeys(String specKeys) {
        this.specKeys = specKeys;
    }

    public String getMessageCart() {
        return messageCart;
    }

    public void setMessageCart(String messageCart) {
        this.messageCart = messageCart;
    }

    public String getOrderGoodsId() {
        return orderGoodsId;
    }

    public void setOrderGoodsId(String orderGoodsId) {
        this.orderGoodsId = orderGoodsId;
    }

    public String getSubtract() {
        return subtract;
    }

    public void setSubtract(String subtract) {
        this.subtract = subtract;
    }

    public String getSubDesc() {
        return subDesc;
    }

    public void setSubDesc(String subDesc) {
        this.subDesc = subDesc;
    }
}
