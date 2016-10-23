package com.qlqwgw.fanli.beans;

/**
 * Created by ASUS on 2016/10/23.
 */

public class GridBean {
    private int gridIcon;
    private String gridTitle;
    private String gridContent;

    public GridBean(int gridIcon, String gridTitle, String gridContent) {
        this.gridIcon = gridIcon;
        this.gridTitle = gridTitle;
        this.gridContent = gridContent;
    }

    public GridBean() {
    }

    public int getGridIcon() {
        return gridIcon;
    }

    public void setGridIcon(int gridIcon) {
        this.gridIcon = gridIcon;
    }

    public String getGridTitle() {
        return gridTitle;
    }

    public void setGridTitle(String gridTitle) {
        this.gridTitle = gridTitle;
    }

    public String getGridContent() {
        return gridContent;
    }

    public void setGridContent(String gridContent) {
        this.gridContent = gridContent;
    }
}
