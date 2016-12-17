package com.ascba.rebate.beans;

import java.io.Serializable;

/**
 * 银行卡实体类
 */

public class Card implements Serializable {
    private String name;
    private String type;
    private String number;
    private boolean isSelect=false;
    private int id;
    private String logo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public Card() {
    }

    public Card(String name, String type, String number,boolean isSelect) {
        this.name = name;
        this.type = type;
        this.number = number;
        this.isSelect=isSelect;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
