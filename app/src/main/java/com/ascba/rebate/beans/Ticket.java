package com.ascba.rebate.beans;

/**
 * Created by Administrator on 2016/11/27.
 */

public class Ticket {
    private int id;
    private int money;
    private String time;
    private String type;
    private int state;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Ticket() {
    }

    public Ticket(int id, int money, String time,String type,int state) {
        this.id = id;
        this.money = money;
        this.time = time;
        this.type=type;
        this.state=state;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
