package com.ascba.rebate.beans;

/**
 * Created by 李鹏 on 2017/5/22.
 */

public class AcutionGoodsBean {
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
}
