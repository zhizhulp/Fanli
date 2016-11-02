package com.ascba.fanli.beans;

/**
 * 银行卡实体类
 */

public class Card {
    private String name;
    private String type;
    private String number;
    private boolean isSelect=false;

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
