package com.ascba.rebate.beans;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

/**
 * Created by 李鹏 on 2017/03/02 0002.
 * 商品详情实体类
 */

public class GoodsDetailsItem implements MultiItemEntity {

    /**
     * 商品轮播
     */
    public static final int TYPE_VIEWPAGER = 1;

    /**
     * 商品简单介绍
     */
    public static final int TYPE_GOODS_SIMPLE_DESC = 2;

    /**
     * 分割线
     */
    public static final int TYPE_GOODS_CUTTINGLINE = 0;

    /**
     * 宽分割线
     */
    public static final int TYPE_GOODS_CUTTINGLINE_WIDE = -1;


    /**
     * 售货服务
     * 24小时发货、7天包退换
     *
     * @param itemType
     * @param resLat
     * @param sales_service
     */
    public static final int TYPE_GOODS_SALES_SERVICE = 3;

    /**
     * 增值：购买送积分
     */
    public static final int TYPE_GOODS_APPRECIATION = 4;

    /**
     * 规格选择
     */
    public static final int TYPE_GOODS_CHOOSE = 5;

    /**
     * 宝贝评价
     */
    public static final int TYPE_GOODS_EVALUATE = 6;

    /**
     * 评价流布局
     */
    public static final int TYPE_GOODS_FLOW = 7;

    /**
     * 宝贝评价示例
     */
    public static final int TYPE_GOODS_EVALUATE_FIRST = 8;

    /**
     * 店铺
     */
    public static final int TYPE_GOODS_SHOP = 9;

    private int itemType;

    private int spanSize = TypeWeight.TYPE_SPAN_SIZE_MAX;

    private int resLat;//布局ID
    private List<String> pagerUrls;//viewPager数据源
    private String store_type;//店铺类型-自营店
    private String goods_title;//商品类型
    private String goods_desc;//商品详细内容
    private String price_new;//当前价格
    private String price_old;//旧价格
    private int ev_all;//宝贝评价
    private double ev_good;//好评率
    private String goods_logo;//商品logo
    private String store_name;//商店名称
    private int goods_all;//全部宝贝数量
    private int goods_recomm;//达人推荐数量
    private double ev_desc;//描述相符
    private double ev_service;//服务态度
    private double ev_delivery;//发货速度
    private String[] strings;//售货服务
    private String content;
    private String username;//买家昵称
    private String time;//购买时间
    private String evDesc;//评价内容
    private String chooseDesc;//购买规格

    private String imgUrl;//商品首页图
    private String goodsUrl;//商品URL

    @Override
    public int getItemType() {
        return itemType;
    }


    /**
     * 分割线
     *
     * @param itemType 类型
     *                 * @param resLat    布局id
     */
    public GoodsDetailsItem(int itemType, int resLat) {
        this.itemType = itemType;
        this.resLat = resLat;
    }

    /**
     * 商品轮播展示
     *
     * @param itemType  类型
     * @param resLat    布局id
     * @param pagerUrls 图片地址
     */
    public GoodsDetailsItem(int itemType, int resLat, List<String> pagerUrls) {
        this.itemType = itemType;
        this.resLat = resLat;
        this.pagerUrls = pagerUrls;
    }

    /**
     * 商品简单介绍
     *
     * @param itemType    类型
     * @param resLat      布局id
     * @param store_type  商店类型
     * @param goods_title 商品类型
     * @param goods_desc  商品详细内容
     * @param price_new   当前价格
     * @param price_old   旧价格
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

    /**
     * 售货服务
     * 24小时发货、7天包退
     * flow流布局
     *
     * @param itemType
     * @param resLat
     * @param strings  内容数组
     */
    public GoodsDetailsItem(int itemType, int resLat, String[] strings) {
        this.itemType = itemType;
        this.resLat = resLat;
        this.strings = strings;
    }

    /**
     * 增值：购买送积分
     * 规格选择
     *
     * @param itemType
     * @param resLat
     * @param content  内容
     */
    public GoodsDetailsItem(int itemType, int resLat, String content) {
        this.itemType = itemType;
        this.resLat = resLat;
        this.content = content;
    }

    /**
     * 宝贝评价、好评率
     *
     * @param itemType
     * @param resLat
     * @param ev_all   所有评价
     * @param ev_good  好评率
     */
    public GoodsDetailsItem(int itemType, int resLat, int ev_all, double ev_good) {
        this.itemType = itemType;
        this.resLat = resLat;
        this.ev_all = ev_all;
        this.ev_good = ev_good;
    }

    /**
     * 买家评价示例
     * @param itemType
     * @param resLat
     * @param username 买家昵称
     * @param time 时间
     * @param evDesc    评价内容
     * @param chooseDesc 购买规格
     * @param pagerUrls 图片地址
     */
    public GoodsDetailsItem(int itemType, int resLat, String username, String time, String evDesc, String chooseDesc,List<String> pagerUrls) {
        this.itemType = itemType;
        this.resLat = resLat;
        this.username = username;
        this.time = time;
        this.evDesc = evDesc;
        this.chooseDesc = chooseDesc;
        this.pagerUrls=pagerUrls;
    }

    /**
     *店铺
     * @param itemType
     * @param resLat
     * @param pagerUrls 图片地址
     * @param goods_logo 店铺名称logo
     * @param store_name    店铺名称
     * @param goods_all 全部商品
     * @param goods_recomm 达人推荐
     * @param ev_desc 描述相符
     * @param ev_service 服务态度
     * @param ev_delivery 发货速度
     */
    public GoodsDetailsItem(int itemType, int resLat, List<String> pagerUrls, String goods_logo, String store_name, int goods_all, int goods_recomm, double ev_desc, double ev_service, double ev_delivery) {
        this.itemType = itemType;
        this.resLat = resLat;
        this.pagerUrls = pagerUrls;
        this.goods_logo = goods_logo;
        this.store_name = store_name;
        this.goods_all = goods_all;
        this.goods_recomm = goods_recomm;
        this.ev_desc = ev_desc;
        this.ev_service = ev_service;
        this.ev_delivery = ev_delivery;
    }

    /**
     * 足迹/首页数据
     * @param imgUrl 首页展示图片地址
     * @param goods_desc 商品名称
     * @param price_new 价格
     * @param goodsUrl 商品地址
     */
    public GoodsDetailsItem( String imgUrl, String goods_desc, String price_new, String goodsUrl) {
        this.imgUrl = imgUrl;
        this.goods_desc = goods_desc;
        this.price_new = price_new;
        this.goodsUrl = goodsUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getGoodsUrl() {
        return goodsUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String[] getStrings() {
        return strings;
    }

    public void setStrings(String[] strings) {
        this.strings = strings;
    }


    public int getSpanSize() {
        return spanSize;
    }


    public int getResLat() {
        return resLat;
    }


    public List<String> getPagerUrls() {
        return pagerUrls;
    }


    public String getStore_type() {
        return store_type;
    }


    public String getGoods_title() {
        return goods_title;
    }


    public String getGoods_desc() {
        return goods_desc;
    }


    public String getPrice_new() {
        return price_new;
    }


    public String getPrice_old() {
        return price_old;
    }


    public int getEv_all() {
        return ev_all;
    }


    public double getEv_good() {
        return ev_good;
    }



    public String getStore_name() {
        return store_name;
    }


    public int getGoods_all() {
        return goods_all;
    }


    public int getGoods_recomm() {
        return goods_recomm;
    }


    public double getEv_desc() {
        return ev_desc;
    }


    public double getEv_service() {
        return ev_service;
    }


    public double getEv_delivery() {
        return ev_delivery;
    }

    public String getGoods_logo() {
        return goods_logo;
    }

    public String getUsername() {
        return username;
    }

    public String getTime() {
        return time;
    }

    public String getEvDesc() {
        return evDesc;
    }

    public String getChooseDesc() {
        return chooseDesc;
    }

    @Override
    public String toString() {
        return "GoodsDetailsItem{" +
                "itemType=" + itemType +
                ", resLat=" + resLat +
                '}';
    }
}
