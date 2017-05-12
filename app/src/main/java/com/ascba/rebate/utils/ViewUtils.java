package com.ascba.rebate.utils;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

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
     * id装换成view
     * @param context 上下文
     * @param latId layour id
     * @return view
     */
    public static View getView(Context context,int latId){
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(latId,null);
    }

    public static void showMyToast(Context context,int latId){
        Toast toast = new Toast(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(latId, null);
        toast.setView(view);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

}
