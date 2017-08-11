package com.ascba.rebate.new_package;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李平 on 2017/8/10.
 * 管理activity
 */

public class ActivityManager {
    private static ActivityManager manager = new ActivityManager();
    private List<Activity> ayList = new ArrayList<>();

    private ActivityManager() {
    }

    public static ActivityManager getInstance() {
        return manager;
    }

    /**
     * 添加 activity
     */
    public void addActivity(Activity a) {
        if (!ayList.contains(a)) {
            ayList.add(a);
        }
    }

    /**
     * 添加 activity
     */
    public void removeActivity(Activity a) {
        if (ayList.contains(a)) {
            ayList.remove(a);
        }
    }

    /**
     * 退出所有activity（退出app）
     */
    public void removeAllActivity() {
        for (Activity a : ayList) {
            a.finish();
        }
        System.exit(0);
    }

    /**
     * 除指定的一个activity外，全部退出
     * @param a 不退出的activity
     */
    public void removeAllExceptOne(Activity a) {
        for (Activity aa : ayList) {
            if(aa!=a){
                aa.finish();
            }
        }
    }
}
