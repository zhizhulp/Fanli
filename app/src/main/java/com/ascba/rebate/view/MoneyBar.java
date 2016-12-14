package com.ascba.rebate.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ascba.rebate.R;

/**
 * app自定义toolBar
 */

public class MoneyBar extends LinearLayout implements View.OnClickListener {
    private TextView mTextView;
    private String title;
    private ImageView mImageView;
    private boolean needBack;
    private boolean needTailIcon;
    public ImageView tailIcon;
    private int tailIconId;
    private boolean needComplete;
    private TextView completeText;
    private CallBack callBack;
    private View bgView;
    private int color;

    public MoneyBar(Context context){
        super(context);
    }
    public MoneyBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context,attrs);
    }

    public MoneyBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context,attrs);
    }

    public void setCallBack(CallBack callBack){
        this.callBack=callBack;
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
        tailIcon = ((ImageView) findViewById(R.id.money_bar_tail_icon));
        completeText = ((TextView) findViewById(R.id.money_bar_ok));
        bgView = findViewById(R.id.money_bar_parent);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MoneyBar);
        color = ta.getColor(R.styleable.MoneyBar_barBg,getResources().getColor(R.color.moneyBarColor));
        title = ta.getString(R.styleable.MoneyBar_textTitle);
        needBack = ta.getBoolean(R.styleable.MoneyBar_needBack, true);
        needTailIcon= ta.getBoolean(R.styleable.MoneyBar_needTailIcon,false);
        tailIconId = ta.getResourceId(R.styleable.MoneyBar_tailIcon,R.mipmap.ic_search);
        needComplete = ta.getBoolean(R.styleable.MoneyBar_needComplete,false);
        //设置背景
        bgView.setBackgroundDrawable(new ColorDrawable(color));
        //设置是否需要完成选项
        if(needComplete){
            completeText.setVisibility(VISIBLE);
            completeText.setOnClickListener(this);
        }
        //设置是否需要返回图标
        if( !needBack){
            mImageView.setVisibility(GONE);
        }else{
            mImageView.setOnClickListener(this);
        }
        //设置标题
        mTextView.setText(title);
        //设置尾部的图标
        if(needTailIcon){
            tailIcon.setVisibility(VISIBLE);
            tailIcon.setImageResource(tailIconId);
            tailIcon.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    callBack.clickImage(v);
                }
            });
        }
        ta.recycle();
    }

    //返回导航的功能
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.money_bar_back:
                ((Activity) getContext()).finish();
                break;
            case R.id.money_bar_ok:
                if(callBack!=null){
                    callBack.clickComplete(completeText);
                }
                break;
        }
    }
    public interface CallBack{
        void clickImage(View im);
        void clickComplete(View tv);
    }
    public void setTextTitle(String title){
        this.title=title;
        invalidate();
    }
}
