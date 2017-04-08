package com.ascba.rebate.beans;

/**
 * 推荐poplist实体类
 */

public class RecType {
    private boolean select;
    private String content;
    private int id;
    private String name;
    private int num;

    public RecType(boolean select, String content,int id) {
        this.select = select;
        this.content = content;
        this.id=id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
