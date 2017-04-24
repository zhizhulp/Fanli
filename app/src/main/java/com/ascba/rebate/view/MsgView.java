package com.ascba.rebate.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ascba.rebate.R;

/**
 * Created by 李鹏 on 2017/04/24 0024.
 */

public class MsgView extends RelativeLayout {

    private ImageView img;
    private ImageView indicator;

    public MsgView(Context context) {
        super(context);
        initView(context, null);
    }

    public MsgView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.view_msg, this, true);
        img = (ImageView) findViewById(R.id.view_msg_im);
        indicator = (ImageView) findViewById(R.id.view_msg_indicator);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MsgView);
        int imgRes = array.getResourceId(R.styleable.MsgView_msg_img, R.mipmap.shop_no_msg);
        boolean isIndicator = array.getBoolean(R.styleable.MsgView_msg_indicator, false);
        array.recycle();
        img.setImageResource(imgRes);
        if (isIndicator) {
            indicator.setVisibility(VISIBLE);
        } else {
            indicator.setVisibility(INVISIBLE);
        }
    }

    public void setImage(int imgRes) {
        img.setImageResource(imgRes);
    }

    public void setIsIndicator(boolean isIndicator) {
        if (isIndicator) {
            indicator.setVisibility(VISIBLE);
        } else {
            indicator.setVisibility(INVISIBLE);
        }
    }
}
