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
    private String cascade_id;//联动id
    private String cascade_name;//联动城市名字

    public City() {
    }

    public City(int cityId, String cascade_name) {
        this.cityId = cityId;
        this.cascade_name = cascade_name;
    }

    public City(int cityLevel, String cascade_name, String cascade_id, int cityId) {
        this.cityLevel = cityLevel;
        this.cascade_name = cascade_name;
        this.cascade_id = cascade_id;
        this.cityId = cityId;
    }

    public City(int cityId, String cityName, int cityLevel, int cityPid, String cityInitial) {
        this.cityId = cityId;
        this.cityName = cityName;
        this.cityLevel = cityLevel;
        this.cityPid = cityPid;
        this.cityInitial = cityInitial;
    }

    public City(int cityId, String cityName, int cityLevel, int cityPid, String cityInitial, String cascade_id, String cascade_name) {
        this.cityId = cityId;
        this.cityName = cityName;
        this.cityLevel = cityLevel;
        this.cityPid = cityPid;
        this.cityInitial = cityInitial;
        this.cascade_id = cascade_id;
        this.cascade_name = cascade_name;
    }

    public String getCascade_id() {
        return cascade_id;
    }

    public void setCascade_id(String cascade_id) {
        this.cascade_id = cascade_id;
    }

    public String getCascade_name() {
        return cascade_name;
    }

    public void setCascade_name(String cascade_name) {
        this.cascade_name = cascade_name;
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
