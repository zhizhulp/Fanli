package com.ascba.rebate.beans;

import com.ascba.rebate.utils.UrlUtils;

/**
 * Created by 李平 on 2017/7/12.
 * 商城banner实体类
 */

public class Banner {

    /**
     * target_type : 0
     * target_value :
     * "target_title": "商品详情"
     * img_url : /public/app/images/4.jpg
     * is_low_price :是否最低价，1是0否
     */

    private int target_type;
    private String target_value;
    private String img_url;
    private String target_title;
    private int is_low_price;

    public int getIs_low_price() {
        return is_low_price;
    }

    public void setIs_low_price(int is_low_price) {
        this.is_low_price = is_low_price;
    }

    public String getTarget_title() {
        return target_title;
    }

    public void setTarget_title(String target_title) {
        this.target_title = target_title;
    }

    public int getTarget_type() {
        return target_type;
    }

    public void setTarget_type(int target_type) {
        this.target_type = target_type;
    }

    public String getTarget_value() {
        return target_value;
    }

    public void setTarget_value(String target_value) {
        this.target_value = target_value;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }
}
