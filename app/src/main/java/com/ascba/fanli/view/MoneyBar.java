package com.ascba.fanli.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ascba.fanli.R;

/**
 * app自定义toolBar
 */

public class MoneyBar extends LinearLayout implements View.OnClickListener {
    private TextView mTextView;
    private String title;
    private ImageView mImageView;
    private boolean needBack;


    public MoneyBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context,attrs);
    }

    public MoneyBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context,attrs);
    }

    //初始化
    private void initView(Context context,AttributeSet attrs){
        if(Build.VERSION.SDK_INT >=19){
            LayoutInflater.from(context).inflate(R.layout.money_bar_layout_status, this,true);
        }else{
            LayoutInflater.from(context).inflate(R.layout.money_bar_layout_no_status, this,true);
        }
        mTextView= (TextView) findViewById(R.id.money_bar_title);
        mImageView= (ImageView) findViewById(R.id.money_bar_back);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MoneyBar);
        title = ta.getString(R.styleable.MoneyBar_textTitle);
        needBack = ta.getBoolean(R.styleable.MoneyBar_needBack, true);
        //设置是否需要返回图标
        if( !needBack){
            mImageView.setVisibility(GONE);
        }else{
            mImageView.setOnClickListener(this);
        }
        mTextView.setText(title);
        ta.recycle();
    }


    @Override
    public void onClick(View v) {
        ((Activity) getContext()).finish();
    }
}
