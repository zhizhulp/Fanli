package com.ascba.rebate.beans;

/**
 * 白积分账单实体类
 */

public class WhiteAccount {
    private String day;
    private String time;
    private int filterIcon;
    private String money;
    private String filterText;

    public WhiteAccount() {
    }

    public WhiteAccount(String day, String time, int filterIcon, String money, String filterText) {
        this.day = day;
        this.time = time;
        this.filterIcon = filterIcon;
        this.money = money;
        this.filterText = filterText;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getFilterIcon() {
        return filterIcon;
    }

    public void setFilterIcon(int filterIcon) {
        this.filterIcon = filterIcon;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getFilterText() {
        return filterText;
    }

    public void setFilterText(String filterText) {
        this.filterText = filterText;
    }
}
