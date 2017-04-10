package com.ascba.rebate.beans;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by 李鹏 on 2017/2/25.
 */

public class PCMultipleItem implements MultiItemEntity {
    private int itemType;

    public static final int TYPE_0 = 0;//信息头
    public static final int TYPE_1 = 1;//横条
    public static final int TYPE_2 = 2;//细分割线
    public static final int TYPE_3 = 3;//待付款、待发货、已成交、待评价、退款
    public static final int TYPE_4 = 4;//粗分割线
    public static final int TYPE_5 = 5;//客服

    //权重

    public static final int TYPE_SPAN_SIZE_DEFAULT = 20;
    public static final int TYPE_SPAN_SIZE_1 = 1;
    public static final int TYPE_SPAN_SIZE_4 = 4;

    private int drawableLeft;//左边图片资源
    private int drawableRight;//右边边图片资源
    private String headImg;//头像
    private int drawableHead;
    private int drawableVIP;//会员
    private String title;//标题
    private String content;//内容
    private int messageNum;//消息数量

    private String contenLeft;
    private String contenRight;
    private int spanSize = TYPE_SPAN_SIZE_DEFAULT;

    public int getDrawableLeft() {
        return drawableLeft;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public void setDrawableLeft(int drawableLeft) {
        this.drawableLeft = drawableLeft;
    }

    public int getDrawableRight() {
        return drawableRight;
    }

    public void setDrawableRight(int drawableRight) {
        this.drawableRight = drawableRight;
    }

    public int getDrawableHead() {
        return drawableHead;
    }

    public void setDrawableHead(int drawableHead) {
        this.drawableHead = drawableHead;
    }

    public int getDrawableVIP() {
        return drawableVIP;
    }

    public void setDrawableVIP(int drawableVIP) {
        this.drawableVIP = drawableVIP;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContenLeft() {
        return contenLeft;
    }

    public void setContenLeft(String contenLeft) {
        this.contenLeft = contenLeft;
    }

    public String getContenRight() {
        return contenRight;
    }

    public void setContenRight(String contenRight) {
        this.contenRight = contenRight;
    }

    public void setSpanSize(int spanSize) {
        this.spanSize = spanSize;
    }

    public PCMultipleItem() {
    }

    public PCMultipleItem(int itemType, int drawableHead, int messageNum, String content, int spanSize) {
        this.itemType = itemType;
        this.drawableHead = drawableHead;
        this.messageNum = messageNum;
        this.content = content;
        this.spanSize = spanSize;
    }

    public PCMultipleItem(int itemType) {
        this.itemType = itemType;
    }

    public PCMultipleItem(int itemType, int drawableLeft, int drawableRight, String headImg, String title) {
        this.itemType = itemType;
        this.drawableLeft = drawableLeft;
        this.drawableRight = drawableRight;
        this.headImg = headImg;
        this.title = title;
    }

    public PCMultipleItem(int itemType, int drawableLeft, String contenLeft, int drawableRight, String contenRight) {
        this.itemType = itemType;
        this.drawableLeft = drawableLeft;
        this.contenLeft = contenLeft;
        this.drawableRight = drawableRight;
        this.contenRight = contenRight;
    }

    public PCMultipleItem(int itemType, int drawableLeft, String contenLeft, String contenRight) {
        this.itemType = itemType;
        this.drawableLeft = drawableLeft;
        this.contenLeft = contenLeft;
        this.contenRight = contenRight;
    }

    public int getMessageNum() {
        return messageNum;
    }

    public void setMessageNum(int messageNum) {
        this.messageNum = messageNum;
    }

    public int getSpanSize() {
        return spanSize;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
