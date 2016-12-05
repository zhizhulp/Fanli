package com.ascba.rebate.beans;

/**
 * Created by Administrator on 2016/12/3.
 */

public class ProxyProtocol {
    private String name;
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProxyProtocol(String name, String url) {
        this.name = name;
        this.url = url;
    }
}
