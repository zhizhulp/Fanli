package com.ascba.rebate.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by 李鹏 on 2017/04/01 0001.
 */

public class PhotoViewPager extends ViewPager {
    public PhotoViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public PhotoViewPager(Context context) {
        super(context);
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
