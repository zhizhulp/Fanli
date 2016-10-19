package com.qlqwgw.fanli.utils;

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
}
