package com.ascba.rebate.beans;

/**
 * Created by Administrator on 2016/11/27.
 */

public class Ticket {
    private int id;
    private int money;
    private long time;
    private String type;

    public Ticket() {
    }

    public Ticket(int id, int money, long time,String type) {
        this.id = id;
        this.money = money;
        this.time = time;
        this.type=type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
