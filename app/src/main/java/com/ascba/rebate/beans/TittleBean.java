package com.ascba.rebate.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/6/2.
 * 主会场title实体类
 */

public class TittleBean implements Parcelable {
    private int id;
    private long startTime;
    private long endTime;
    private String status;
    private String nowTime;

    public TittleBean() {
    }

    public TittleBean(int id, long startTime, long endTime, String status, String nowTime) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.nowTime = nowTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNowTime() {
        return nowTime;
    }

    public void setNowTime(String nowTime) {
        this.nowTime = nowTime;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeLong(this.startTime);
        dest.writeLong(this.endTime);
        dest.writeString(this.status);
        dest.writeString(this.nowTime);
    }

    protected TittleBean(Parcel in) {
        this.id = in.readInt();
        this.startTime = in.readLong();
        this.endTime = in.readLong();
        this.status = in.readString();
        this.nowTime = in.readString();
    }

    public static final Parcelable.Creator<TittleBean> CREATOR = new Parcelable.Creator<TittleBean>() {
        @Override
        public TittleBean createFromParcel(Parcel source) {
            return new TittleBean(source);
        }

        @Override
        public TittleBean[] newArray(int size) {
            return new TittleBean[size];
        }
    };
}
