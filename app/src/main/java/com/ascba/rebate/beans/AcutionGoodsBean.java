package com.ascba.rebate.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 李鹏 on 2017/5/22.
 */

public class AcutionGoodsBean implements Parcelable {
    private int id;
    private int type;//竞拍类型 抢拍和盲拍
    private String imgUrl;
    private String name;//商品名
    private String state;//状态
    private String specialOffer;//优惠活动
    private String timeRemaining;//剩余时间
    private String personNum;//竞拍人数
    private String score;//积分
    private String cashDeposit;//保证金
    private String payState;//支付状态

    private Double price;//当前价格
    private Double gapPrice;//降价幅度

    private int maxReduceTimes;//最大降价次数
    private int reduceTimes;//当前降价次数

    private int currentLeftTime;//本次剩余秒数
    private int gapTime;//降价时间间隔（s）

    private int intState;//1 拍卖结束 2 即将开始 3拍卖结束
    private String strState;//对应intState
    private Double startPrice;//起始价格
    private Double endPrice;//最低价

    private boolean isSelect;

    private long startTime;
    private String startTimeStr;
    private long endTime;

    private String blindState;//盲拍状态


    public AcutionGoodsBean() {

    }

    public AcutionGoodsBean(int id, int type, String imgUrl, String name, Double price, String score, String cashDeposit, int reduceTimes) {
        this.id = id;
        this.type = type;
        this.imgUrl = imgUrl;
        this.name = name;
        this.price = price;
        this.score = score;
        this.cashDeposit = cashDeposit;
        this.reduceTimes = reduceTimes;
    }

    public String getStartTimeStr() {
        return startTimeStr;
    }

    public void setStartTimeStr(String startTimeStr) {
        this.startTimeStr = startTimeStr;
    }

    public String getBlindState() {
        return blindState;
    }

    public void setBlindState(String blindState) {
        this.blindState = blindState;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public int getIntState() {
        return intState;
    }

    public void setIntState(int intState) {
        this.intState = intState;
    }

    public String getStrState() {
        return strState;
    }

    public void setStrState(String strState) {
        this.strState = strState;
    }
    public Double getGapPrice() {
        return gapPrice;
    }

    public void setGapPrice(Double gapPrice) {
        this.gapPrice = gapPrice;
    }

    public int getMaxReduceTimes() {
        return maxReduceTimes;
    }

    public void setMaxReduceTimes(int maxReduceTimes) {
        this.maxReduceTimes = maxReduceTimes;
    }

    public int getCurrentLeftTime() {
        return currentLeftTime;
    }

    public void setCurrentLeftTime(int currentLeftTime) {
        this.currentLeftTime = currentLeftTime;
    }

    public int getGapTime() {
        return gapTime;
    }

    public void setGapTime(int gapTime) {
        this.gapTime = gapTime;
    }

    public int getReduceTimes() {
        return reduceTimes;
    }

    public void setReduceTimes(int reduceTimes) {
        this.reduceTimes = reduceTimes;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSpecialOffer() {
        return specialOffer;
    }

    public void setSpecialOffer(String specialOffer) {
        this.specialOffer = specialOffer;
    }

    public String getTimeRemaining() {
        return timeRemaining;
    }

    public void setTimeRemaining(String timeRemaining) {
        this.timeRemaining = timeRemaining;
    }

    public String getPersonNum() {
        return personNum;
    }

    public void setPersonNum(String personNum) {
        this.personNum = personNum;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getCashDeposit() {
        return cashDeposit;
    }

    public void setCashDeposit(String cashDeposit) {
        this.cashDeposit = cashDeposit;
    }

    public String getPayState() {
        return payState;
    }

    public void setPayState(String payState) {
        this.payState = payState;
    }

    public Double getStartPrice() {
        return startPrice;
    }

    public void setStartPrice(Double startPrice) {
        this.startPrice = startPrice;
    }

    public Double getEndPrice() {
        return endPrice;
    }

    public void setEndPrice(Double endPrice) {
        this.endPrice = endPrice;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.type);
        dest.writeString(this.imgUrl);
        dest.writeString(this.name);
        dest.writeString(this.state);
        dest.writeString(this.specialOffer);
        dest.writeString(this.timeRemaining);
        dest.writeString(this.personNum);
        dest.writeString(this.score);
        dest.writeString(this.cashDeposit);
        dest.writeString(this.payState);
        dest.writeValue(this.price);
        dest.writeValue(this.gapPrice);
        dest.writeInt(this.maxReduceTimes);
        dest.writeInt(this.reduceTimes);
        dest.writeInt(this.currentLeftTime);
        dest.writeInt(this.gapTime);
        dest.writeInt(this.intState);
        dest.writeString(this.strState);
        dest.writeValue(this.startPrice);
        dest.writeValue(this.endPrice);
    }

    protected AcutionGoodsBean(Parcel in) {
        this.id = in.readInt();
        this.type = in.readInt();
        this.imgUrl = in.readString();
        this.name = in.readString();
        this.state = in.readString();
        this.specialOffer = in.readString();
        this.timeRemaining = in.readString();
        this.personNum = in.readString();
        this.score = in.readString();
        this.cashDeposit = in.readString();
        this.payState = in.readString();
        this.price = (Double) in.readValue(Double.class.getClassLoader());
        this.gapPrice = (Double) in.readValue(Double.class.getClassLoader());
        this.maxReduceTimes = in.readInt();
        this.reduceTimes = in.readInt();
        this.currentLeftTime = in.readInt();
        this.gapTime = in.readInt();
        this.intState = in.readInt();
        this.strState = in.readString();
        this.startPrice = (Double) in.readValue(Double.class.getClassLoader());
        this.endPrice = (Double) in.readValue(Double.class.getClassLoader());
    }

    public static final Parcelable.Creator<AcutionGoodsBean> CREATOR = new Parcelable.Creator<AcutionGoodsBean>() {
        @Override
        public AcutionGoodsBean createFromParcel(Parcel source) {
            return new AcutionGoodsBean(source);
        }

        @Override
        public AcutionGoodsBean[] newArray(int size) {
            return new AcutionGoodsBean[size];
        }
    };
}
