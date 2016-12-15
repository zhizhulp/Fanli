package com.ascba.rebate.beans;

import java.util.Comparator;

/**
 * Created by Administrator on 2016/11/27.
 */

public class Ticket implements Comparable<Ticket>{
    private int id;
    private int money;
    private long startTime;
    private long endTime;
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

    public Ticket(int id, int money, String type,int state,long startTime,long endTime) {
        this.id = id;
        this.money = money;
        this.type=type;
        this.state=state;
        this.startTime=startTime;
        this.endTime=endTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
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
    //1代表之前，0代表之后
    @Override
    public int compareTo(Ticket o) {

        return getState()-o.getState();
    }
}
