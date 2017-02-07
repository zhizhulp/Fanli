package com.ascba.rebate.beans;



public class Proxy {
    private String icon;
    private String type;
    private String money;
    private boolean open;
    private int group_id;
    private int group;
    private String desc;
    private String open_region_name;

    public String getOpen_region_name() {
        return open_region_name;
    }

    public void setOpen_region_name(String open_region_name) {
        this.open_region_name = open_region_name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Proxy() {
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
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

