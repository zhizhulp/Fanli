package com.ascba.rebate.beans;

import java.util.List;

/**
 * 代理界面 title+list
 */

public class UpdateTitle implements Comparable<UpdateTitle> {
    private int groupId;
    private String title;
    private List<Proxy> mList;
    public UpdateTitle() {
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public UpdateTitle(String title, List<Proxy> mList) {
        this.title = title;
        this.mList = mList;
    }

    public UpdateTitle(int groupId, String title, List<Proxy> mList) {
        this.groupId = groupId;
        this.title = title;
        this.mList = mList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Proxy> getmList() {
        return mList;
    }

    public void setmList(List<Proxy> mList) {
        this.mList = mList;
    }

    /**
     * @return item数目，为集合大小+1
     */
    public int size(){
        return mList.size() + 1;
    }

    /**
     * @param position 如果position为0就返回标题
     * @return
     */
    public Object getItem(int position) {
        if (position == 0) {
            return title;
        } else {
            return mList.get(position - 1);
        }
    }

    @Override
    public int compareTo(UpdateTitle o) {
        return getGroupId()-o.getGroupId();
    }
}
