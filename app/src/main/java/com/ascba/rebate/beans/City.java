package com.ascba.rebate.beans;

/**
 * 城市实体类
 */

public class City {
    private int cityId;
    private String cityName;
    private int cityLevel;
    private int cityPid;
    private String cityInitial;//城市首字母

    public City() {
    }

    public City(int cityId, String cityName, int cityLevel, int cityPid, String cityInitial) {
        this.cityId = cityId;
        this.cityName = cityName;
        this.cityLevel = cityLevel;
        this.cityPid = cityPid;
        this.cityInitial = cityInitial;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getCityLevel() {
        return cityLevel;
    }

    public void setCityLevel(int cityLevel) {
        this.cityLevel = cityLevel;
    }

    public int getCityPid() {
        return cityPid;
    }

    public void setCityPid(int cityPid) {
        this.cityPid = cityPid;
    }

    public String getCityInitial() {
        return cityInitial;
    }

    public void setCityInitial(String cityInitial) {
        this.cityInitial = cityInitial;
    }
}
