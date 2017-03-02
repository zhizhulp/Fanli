package com.ascba.rebate.beans;

/**
 * Created by 李鹏 on 2017/03/01 0001.
 */

public class GoodsFootprint {
    private String price;//价格
    private String img;//图片地址
    private String name;//名称
    private String url;//商品地址

    public GoodsFootprint() {
    }

    public GoodsFootprint(String price, String img, String name, String url) {
        this.price = price;
        this.img = img;
        this.name = name;
        this.url = url;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "GoodsFootprint{" +
                "price=" + price +
                ", img='" + img + '\'' +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
