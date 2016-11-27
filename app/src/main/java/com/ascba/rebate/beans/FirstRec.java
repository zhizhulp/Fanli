package com.ascba.rebate.beans;

/**
 * Created by Administrator on 2016/11/6.
 */

public class FirstRec {
    private String name;
    private int typeIcon;
    private String recNum;
    private String money;
    private String time;

    public FirstRec() {
    }

    public FirstRec(String name, int typeIcon, String recNum, String money, String time) {
        this.name = name;
        this.typeIcon = typeIcon;
        this.recNum = recNum;
        this.money = money;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTypeIcon() {
        return typeIcon;
    }

    public void setTypeIcon(int typeIcon) {
        this.typeIcon = typeIcon;
    }

    public String getRecNum() {
        return recNum;
    }

    public void setRecNum(String recNum) {
        this.recNum = recNum;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
