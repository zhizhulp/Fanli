package com.ascba.rebate.activities.base;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.ascba.rebate.R;

/**
 * UI层
 */
public class BaseUIActivity extends AppCompatActivity {
    protected static final int UIMODE_NORMAL = 0;//状态栏着色，占位置(默认)
    protected static final int UIMODE_FULLSCREEN_NOTALL = 1;//全屏,虚拟键透明
    protected static final int UIMODE_FULLSCREEN = 2;//全屏(状态栏透明，虚拟键透明)
    protected static final int UIMODE_TRANSPARENT = 3;//状态栏透明，且不占位置 全透明
    protected static final int UIMODE_TRANSPARENT_NOTALL = 4;//状态栏透明，且不占位置 半透明

    private int uiMode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//屏幕方向固定为竖屏
        setUITheme();
    }

    /**
     * 设置主题样式（主要针对是否全屏，状态栏是否透明）
     */
    private void setUITheme() {
        Window window = getWindow();
        if(uiMode==UIMODE_FULLSCREEN){
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            }
        }else if(uiMode==UIMODE_FULLSCREEN_NOTALL){
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }else if(uiMode==UIMODE_TRANSPARENT || uiMode==UIMODE_TRANSPARENT_NOTALL){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT &&
                    Build.VERSION.SDK_INT<Build.VERSION_CODES.LOLLIPOP) {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if(uiMode==UIMODE_TRANSPARENT_NOTALL){
                    window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//全透明或半透明的关键
                }
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                window.setStatusBarColor(Color.TRANSPARENT);
            }
        }
    }

    /**
     * 设置主题样式的值
     * @param uiMode 主题样式的值
     */
    protected void setUIMode(int uiMode){
        this.uiMode=uiMode;
    }

    /**
     * 设置状态栏的颜色
     * @param color 要设置的颜色
     */
    protected void setStatusBarColor(int color){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && uiMode!=UIMODE_TRANSPARENT && uiMode!=UIMODE_TRANSPARENT_NOTALL){
            getWindow().setStatusBarColor(color);
        }
    }

    protected void addStatusView(){

    }
}
