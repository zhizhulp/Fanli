package com.ascba.rebate.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 李鹏 on 2017/03/14 0014.
 * 收货地址实体类
 */

public class ReceiveAddressBean implements Parcelable {
    private String id;
    private String name;
    private String phone;
    private String address;
    private String isDefault;//是否为默认：1——是;0——否
    private String province;//省份ID
    private String city;//市ID
    private String district;//地区ID
    private String twon;//乡镇ID

    public ReceiveAddressBean() {
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


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getTwon() {
        return twon;
    }

    public void setTwon(String twon) {
        this.twon = twon;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.phone);
        dest.writeString(this.address);
        dest.writeString(this.isDefault);
        dest.writeString(this.province);
        dest.writeString(this.city);
        dest.writeString(this.district);
        dest.writeString(this.twon);
    }

    protected ReceiveAddressBean(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.phone = in.readString();
        this.address = in.readString();
        this.isDefault = in.readString();
        this.province = in.readString();
        this.city = in.readString();
        this.district = in.readString();
        this.twon = in.readString();
    }

    public static final Parcelable.Creator<ReceiveAddressBean> CREATOR = new Parcelable.Creator<ReceiveAddressBean>() {
        @Override
        public ReceiveAddressBean createFromParcel(Parcel source) {
            return new ReceiveAddressBean(source);
        }

        @Override
        public ReceiveAddressBean[] newArray(int size) {
            return new ReceiveAddressBean[size];
        }
    };
}
