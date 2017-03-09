package com.ascba.rebate.beans;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by 李鹏 on 2017/03/08 0008.
 * 商品评价实体类
 */

public class EvaluationBean implements MultiItemEntity {

    private String imgHead;//用户头像
    private String userName;//用户昵称
    private String imgVIP;//等级
    private String desc;
    private String time;
    private String choose;
    private String[] imgs;
    private int type;

    public EvaluationBean(int type) {
        this.type=type;
    }

    public EvaluationBean(int type,String imgHead, String userName, String desc, String time, String choose, String[] imgs) {
        this.type=type;
        this.imgHead = imgHead;
        this.userName = userName;
        this.desc = desc;
        this.time = time;
        this.choose = choose;
        this.imgs = imgs;
    }

    public String getImgHead() {
        return imgHead;
    }

    public String getUserName() {
        return userName;
    }

    public String getImgVIP() {
        return imgVIP;
    }

    public String getDesc() {
        return desc;
    }

    public String getTime() {
        return time;
    }

    public String getChoose() {
        return choose;
    }

    public String[] getImgs() {
        return imgs;
    }

    @Override
    public int getItemType() {
        return type;
    }
}
