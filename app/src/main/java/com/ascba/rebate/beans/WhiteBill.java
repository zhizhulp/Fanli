package com.ascba.rebate.beans;

import java.util.List;

/**
 * Created by 李平 on 2017/4/19 0019.16:55
 * 白积分账单实体类
 */

public class WhiteBill  {
    private String month;//一月
    private boolean hasCalendar;//是否显示日历图标
    private List<CashAccount> accounts;//账单列表

    public WhiteBill(String month, boolean hasCalendar, List<CashAccount> accounts) {
        this.month = month;
        this.hasCalendar = hasCalendar;
        this.accounts = accounts;
    }

    public WhiteBill() {
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public boolean isHasCalendar() {
        return hasCalendar;
    }

    public void setHasCalendar(boolean hasCalendar) {
        this.hasCalendar = hasCalendar;
    }

    public List<CashAccount> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<CashAccount> accounts) {
        this.accounts = accounts;
    }
}
