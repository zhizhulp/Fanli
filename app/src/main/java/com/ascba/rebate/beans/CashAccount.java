package com.ascba.rebate.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * 白积分账单实体类
 */

public class CashAccount implements Parcelable, MultiItemEntity {
    private String day;//今天
    private String time;//21:41
    private String money;//-456.12
    private String filterText;//农业银行-提现
    private String status;//24小时内到账
    private int imgId;//图片
    private String imgUrl;//网络图片
    private String month;//账单所在月份
    private CashAccountType type;//账单类型
    private int itemType;//用于账单区分头部和item
    private int is_money;
    private String is_money_tip;
    private int fivepercent_log_id;
    private int pay_type;

    public int getPay_type() {
        return pay_type;
    }

    public void setPay_type(int pay_type) {
        this.pay_type = pay_type;
    }

    public int getFivepercent_log_id() {
        return fivepercent_log_id;
    }

    public void setFivepercent_log_id(int fivepercent_log_id) {
        this.fivepercent_log_id = fivepercent_log_id;
    }

    public int getIs_money() {
        return is_money;
    }

    public void setIs_money(int is_money) {
        this.is_money = is_money;
    }

    public String getIs_money_tip() {
        return is_money_tip;
    }

    public void setIs_money_tip(String is_money_tip) {
        this.is_money_tip = is_money_tip;
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

    public CashAccount(String day, String time, String money, String filterText, String status, int imgId) {
        this.day = day;
        this.time = time;
        this.money = money;
        this.filterText = filterText;
        this.status = status;
        this.imgId = imgId;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
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

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }


    public void setItemType(int itemType) {
        this.itemType = itemType;
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

    public static final Creator CREATOR = new Creator() {
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

    @Override
    public int getItemType() {
        return itemType;
    }
}
