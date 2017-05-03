package com.ascba.rebate.utils;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ascba.rebate.R;

/*
 * Created by 李平 on 2017/4/4 0004.14:49
 */

public class ViewUtils {
    public static View getEmptyView(Context context,String text){
        FrameLayout ft = new FrameLayout(context);
        ft.setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(R.color.main_bg)));
        TextView textView = new TextView(context);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                , ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER;
        textView.setLayoutParams(lp);
        textView.setText(text);
        ft.addView(textView);
        return ft;
    }

    /**
     *
     * @param context 上下文
     * @param latId layour id
     * @return
     */
    public static View getBillEmptyView(Context context,int latId){
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(latId,null);
    }

}
