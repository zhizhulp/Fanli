package com.ascba.rebate.beans;

/**
 * Created by 李鹏 on 2017/03/29 0029.
 * 交易记录
 */

public class TransactionBean {
    private String time;
    private String content;//内容
    private int icon;
    private String money;
    private String store;

    public TransactionBean(String time, String content, int icon, String money, String store) {
        this.time = time;
        this.content = content;
        this.icon = icon;
        this.money = money;
        this.store = store;
    }
}
