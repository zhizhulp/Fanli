package com.ascba.rebate.beans;

/**
 * Created by 李鹏 on 2017/03/25 0025.
 */

public class GoodsImgBean {
    private int id;
    private String imgUrl;

    public GoodsImgBean() {
    }

    public GoodsImgBean(int id, String imgUrl) {
        this.id = id;
        this.imgUrl = imgUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
