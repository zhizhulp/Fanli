package com.ascba.rebate.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 白积分账单实体类
 */

public class CashAccount implements Parcelable {
    private String day;
    private String time;
    private String money;
    private String filterText;
    private String status;
    private CashAccountType type;

    public CashAccountType getType() {
        return type;
    }

    public void setType(CashAccountType type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public CashAccount() {
    }

    public CashAccount(String day, String time, String money, String filterText, String status, CashAccountType type) {
        this.day = day;
        this.time = time;
        this.money = money;
        this.filterText = filterText;
        this.status = status;
        this.type = type;
    }

    public CashAccount(String day, String time, String money, String filterText) {
        this.day = day;
        this.time = time;
        this.money = money;
        this.filterText = filterText;
    }

    public CashAccount(String day, String time, String money, String filterText, CashAccountType type) {
        this.day = day;
        this.time = time;
        this.money = money;
        this.filterText = filterText;
        this.type = type;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(day);
        parcel.writeString(time);
        parcel.writeString(money);
        parcel.writeString(filterText);
        parcel.writeString(status);
        parcel.writeSerializable(type);
    }
    public static final Parcelable.Creator<CashAccount> CREATOR = new Creator(){

        @Override
        public CashAccount createFromParcel(Parcel source) {
            // TODO Auto-generated method stub
            // 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
            CashAccount p = new CashAccount();
            p.setDay(source.readString());
            p.setTime(source.readString());
            p.setMoney(source.readString());
            p.setFilterText(source.readString());
            p.setType((CashAccountType) source.readSerializable());
            return p;
        }

        @Override
        public CashAccount[] newArray(int size) {
            // TODO Auto-generated method stub
            return new CashAccount[size];
        }
    };
}
