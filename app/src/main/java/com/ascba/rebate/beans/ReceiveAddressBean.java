package com.ascba.rebate.beans;

/**
 * Created by 李鹏 on 2017/03/14 0014.
 * 收货地址实体类
 */

public class ReceiveAddressBean {
    private String name;
    private String phone;
    private String address;
    private boolean isSelect;
    private int city;//市ID
    private int district;//地区ID
    private int twon;//乡镇ID

    public ReceiveAddressBean() {
    }

    public ReceiveAddressBean(String name, String phone, String address, boolean isSelect) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.isSelect = isSelect;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public int getCity() {
        return city;
    }

    public void setCity(int city) {
        this.city = city;
    }

    public int getDistrict() {
        return district;
    }

    public void setDistrict(int district) {
        this.district = district;
    }

    public int getTwon() {
        return twon;
    }

    public void setTwon(int twon) {
        this.twon = twon;
    }
}
