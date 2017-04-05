package com.ascba.rebate.beans;

/**
 * Created by 李鹏 on 2017/04/05 0005.
 */

public class IntegralValueItem {
    public static final int TYPE_1 = 1;
    public static final int TYPE_2 = 2;//分割线

    private int spanSize = TypeWeight.TYPE_SPAN_SIZE_MAX;

    public int getSpanSize() {
        return spanSize;
    }

    private String title;
    private String content;

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public IntegralValueItem( String title, String content) {

        this.title = title;
        this.content = content;
    }

}
