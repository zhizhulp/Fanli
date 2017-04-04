package com.ascba.rebate.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by 李平 on 2017/4/4 0004.14:49
 */

public class ViewUtils {
    public static View getEmptyView(Context context,String text){
        FrameLayout ft = new FrameLayout(context);
        TextView textView = new TextView(context);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                , ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER;
        textView.setLayoutParams(lp);
        textView.setText(text);
        ft.addView(textView);
        return ft;
    }
}
