package com.ascba.rebate.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.util.Log;
import android.widget.Toast;

/**
 * 日志打印工具类
 */

public class LogUtils {
    public static void PrintLog(String tag,String content){
        Log.i(tag,content);
    }
    public static void makeToast(Context context,String content){
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }

    /**
     * 判断app是否是调试版
     * @param context 上下文
     * @return boolean
     */
    public static boolean isAppDebug(Context context){
        try {
            ApplicationInfo info= context.getApplicationInfo();
            return (info.flags&ApplicationInfo.FLAG_DEBUGGABLE)!=0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
