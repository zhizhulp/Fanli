package com.ascba.rebate.view;

import android.content.Context;
import android.util.AttributeSet;

import com.baoyz.swipemenulistview.SwipeMenuListView;

/**
 * Created by Administrator on 2016/12/21 0021.
 */

public class SwipeMenuListView2 extends SwipeMenuListView {
    public SwipeMenuListView2(Context context) {
        super(context);
    }

    public SwipeMenuListView2(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public SwipeMenuListView2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    /**
     * Integer.MAX_VALUE >> 2,如果不设置，系统默认设置是显示两条
     */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);

    }
}
