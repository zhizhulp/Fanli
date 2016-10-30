package com.ascba.fanli.utils;

import android.content.Context;
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
}
