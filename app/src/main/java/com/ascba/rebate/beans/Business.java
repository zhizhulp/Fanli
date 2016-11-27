package com.ascba.rebate.beans;

/**
 * Created by Administrator on 2016/10/19.
 */

public class Business {
    private int logo;
    private String bName;
    private String bCategory;
    private int bCategoryIcon;
    private String goodComm;
    private String distance;
    public Business(){

    }
    public Business(int logo, String bName, String bCategory, int bCategoryIcon, String goodComm, String distance) {
        this.logo = logo;
        this.bName = bName;
        this.bCategory = bCategory;
        this.bCategoryIcon = bCategoryIcon;
        this.goodComm = goodComm;
        this.distance = distance;
    }

    public int getLogo() {
        return logo;
    }

    public void setLogo(int logo) {
        this.logo = logo;
    }

    public String getbName() {
        return bName;
    }

    public void setbName(String bName) {
        this.bName = bName;
    }

    public String getbCategory() {
        return bCategory;
    }

    public void setbCategory(String bCategory) {
        this.bCategory = bCategory;
    }

    public int getbCategoryIcon() {
        return bCategoryIcon;
    }

    public void setbCategoryIcon(int bCategoryIcon) {
        this.bCategoryIcon = bCategoryIcon;
    }

    public String getGoodComm() {
        return goodComm;
    }

    public void setGoodComm(String goodComm) {
        this.goodComm = goodComm;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
