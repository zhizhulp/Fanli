package com.ascba.rebate.beans;

/**
 * 支付方式实体类
 */

public class PayType {
    private boolean isSelect;
    private int icon;
    private String title;
    private String content;

    public PayType(boolean isSelect, int icon, String title, String content) {
        this.isSelect = isSelect;
        this.icon = icon;
        this.title = title;
        this.content = content;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
