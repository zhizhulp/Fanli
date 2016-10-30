package com.ascba.fanli.utils;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by Administrator on 2016/10/18.
 */

public class ScreenDpiUtils {
    public static int getScreenDpi(Context context){
        DisplayMetrics dm = new DisplayMetrics();
        return context.getResources().getDisplayMetrics().densityDpi;
    }
    public static int dip2px(Context context,float dipValue){
        final float scale=context.getResources().getDisplayMetrics().densityDpi;
        return (int)(dipValue*(scale/160)+0.5f);
    }

    public static int px2dp(Context context,float pxValue){
        final float scale=context.getResources().getDisplayMetrics().densityDpi;
        return (int)((pxValue*160)/scale+0.5f);
    }
    private static int getStatusBarHeight(Context context) {
        // 获得状态栏高度
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }
}
