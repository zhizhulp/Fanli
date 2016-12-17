package com.ascba.rebate.beans;

/**
 * Created by Administrator on 2016/12/16.
 */

public class WhiteTicket {
    private String money;
    private int id;

    public WhiteTicket(String money, int id) {
        this.money = money;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public WhiteTicket(String money) {
        this.money = money;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }
}
