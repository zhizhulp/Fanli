package com.ascba.rebate.beans;

/**
 * Created by 李平 on 2017/4/20 0020.19:36
 */

public class BillType {
    public BillType(boolean hasSelect, String title, String count,CashAccountType type) {
        this.hasSelect = hasSelect;
        this.title = title;
        this.count = count;
        this.type=type;
    }
    public boolean hasSelect;//是否被选择
    public String title;//类型
    public String count;//账单数量
    public CashAccountType type;//类型

}