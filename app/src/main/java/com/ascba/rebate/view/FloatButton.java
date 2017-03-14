package com.ascba.rebate.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.ascba.rebate.R;
import com.ascba.rebate.application.MyApplication;

import java.lang.reflect.Field;

/**
 * Created by 李鹏 on 2017/03/13 0013.
 * 悬浮窗
 */

public class FloatButton extends RelativeLayout {
    private float mTouchStartX;
    private float mTouchStartY;
    private int mScreenWidth;//屏幕宽度
    private int mScreenHeight;//屏幕宽度
    View view;
    RelativeLayout layout;

    private WindowManager wm = (WindowManager) getContext().getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
    private WindowManager.LayoutParams param = ((MyApplication) getContext().getApplicationContext()).getMywmParams();



    public FloatButton(Context context) {
        super(context);
        initView(context);
    }

    public FloatButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public FloatButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(final Context context) {
        view = LayoutInflater.from(context).inflate(R.layout.view_float_button, this, true);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        mScreenWidth = dm.widthPixels;
        mScreenHeight = dm.heightPixels;
        param.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;    // 系统提示类型,重要
        param.format = 1;
        param.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE; // 不能抢占聚焦点
        param.flags = param.flags | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        param.flags = param.flags | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS; // 排版不受限制

        param.alpha = 0.8f;

        //设置悬浮窗口长宽数据
        param.width = 250;
        param.height = 250;

        param.gravity = Gravity.LEFT | Gravity.TOP;   //调整悬浮窗口至左上角
        //以屏幕左上角为原点，设置x、y初始值
        param.x = 0;
        param.y = 0;

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                param.alpha = 0.5f;
                mTouchStartX = event.getX();
                mTouchStartY = event.getY();
                wm.updateViewLayout(this, param);
                break;
            case MotionEvent.ACTION_MOVE:
                param.x = (int) (x - mTouchStartX);
                param.y = (int) (y - mTouchStartY - param.height / 2);

                /**
                 * 越界处理
                 */
                if (param.y <= 0) {
                    param.y = 0;
                }
                if (param.x <= 0) {
                    param.x = 0;
                }
                if (param.y >= mScreenHeight - param.height - getStatusBarHeight()) {
                    param.y = mScreenHeight - param.height - getStatusBarHeight();
                }
                if (param.x >= mScreenWidth - param.width) {
                    param.x = mScreenWidth - param.width;
                }

                wm.updateViewLayout(this, param);
                break;
            case MotionEvent.ACTION_UP:
                param.alpha = 0.8f;
                if (param.x >= (mScreenWidth - param.width) / 2) {
                    param.x = (mScreenWidth - param.width);
                } else {
                    param.x = 0;
                }
                wm.updateViewLayout(this, param);
                mTouchStartX = mTouchStartY = 0;

                break;
        }
        return false;
    }

    public void showView() {
        //显示myFloatView图像
        wm.addView(this, param);
    }

    public void removeView() {
        wm.removeView(this);
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    private int getStatusBarHeight() {
        Class<?> c = null;

        Object obj = null;

        Field field = null;

        int x = 0, sbar = 0;

        try {

            c = Class.forName("com.android.internal.R$dimen");

            obj = c.newInstance();

            field = c.getField("status_bar_height");

            x = Integer.parseInt(field.get(obj).toString());

            sbar = getResources().getDimensionPixelSize(x);

        } catch (Exception e1) {

            e1.printStackTrace();

        }

        return sbar;
    }

}