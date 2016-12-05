package com.ascba.rebate.beans;

/**
 * Created by Administrator on 2016/11/28.
 */

public class Proxy {
    private String icon;
    private String type;
    private String money;
    private boolean open;
    private int group_id;

    public Proxy() {
    }

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public Proxy(String icon, String type, String money, boolean open) {
        this.icon = icon;
        this.type = type;
        this.money = money;
        this.open = open;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }
}

