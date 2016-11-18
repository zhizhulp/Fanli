package com.ascba.fanli.utils;
import android.app.Activity;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/17.
 */

public class ActivityCollector {
    private List<Activity> mList=new ArrayList<>();
    public void addActivity(Activity a){
        mList.add(a);
    }

    public void removeActivity(Activity b){
        mList.remove(b);
    }
    //关闭所有界面
    public void finishAll(){
        if(mList.size()!=0){
            for (int i = 0; i < mList.size(); i++) {
                Activity a = mList.get(i);
                if(!a.isFinishing()){
                    a.finish();
                }
            }
        }
    }
}
