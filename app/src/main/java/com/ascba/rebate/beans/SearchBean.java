package com.ascba.rebate.beans;


/**
 * Created by Administrator on 2016/12/26 0026.
 */

public class SearchBean {
    private String name;
    private String address;

    public SearchBean(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
