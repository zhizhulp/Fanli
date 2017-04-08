package com.ascba.rebate.utils;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.adapter.ProfileAdapter;
import com.ascba.rebate.beans.GoodsAttr;
import com.ascba.rebate.view.cart_btn.NumberButton;

import java.util.ArrayList;
import java.util.List;

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
