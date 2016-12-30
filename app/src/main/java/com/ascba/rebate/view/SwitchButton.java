package com.ascba.rebate.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ascba.rebate.R;

/**
 * Created by Administrator on 2016/11/6.
 */

public class SwitchButton extends RadioGroup {
    private RadioButton btnLeft;
    private RadioButton btnRight;
    private Callback callback;
    private RadioButton btnCenter;
    private ImageView imBack;

    public interface Callback{
        void onLeftClick();
        void onRightClick();
        void onCenterClick();
    }

    public Callback getCallback() {
        return callback;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public SwitchButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    //设置按钮左边文字
    public void setLeftText(String text){
        btnLeft.setText(text);
    }
    //设置按钮中间文字
    public void setCenterText(String text){
        btnCenter.setText(text);
    }
    //设置按钮右边文字
    public void setRightText(String text){
        btnRight.setText(text);
    }


    private void init(final Context context) {
        LayoutInflater.from(context).inflate(R.layout.switch_btn,this,true);
        initBack(context);//返回按钮
        init3Btn(context);//3个tab


    }
    //设置是否有返回图标
    public void setIsBack(boolean isBackIcon){
        if(!isBackIcon){
            imBack.setVisibility(GONE);
        }
    }

    private void init3Btn(Context context) {
        btnLeft = ((RadioButton) findViewById(R.id.btn_left));
        btnRight = ((RadioButton) findViewById(R.id.btn_right));
        btnCenter = ((RadioButton) findViewById(R.id.btn_center));
        btnLeft.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    btnLeft.setTextColor(getResources().getColor(R.color.moneyBarColor));
                }else{
                    btnLeft.setTextColor(getResources().getColor(R.color.white));
                }


            }
        });
        btnLeft.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(callback!=null){
                    callback.onLeftClick();
                }
            }
        });
        btnCenter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    btnCenter.setTextColor(getResources().getColor(R.color.moneyBarColor));
                }else{
                    btnCenter.setTextColor(getResources().getColor(R.color.white));
                }

            }
        });
        btnCenter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(callback!=null){
                    callback.onCenterClick();
                }
            }
        });
        btnRight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    btnRight.setTextColor(getResources().getColor(R.color.moneyBarColor));
                }else{
                    btnRight.setTextColor(getResources().getColor(R.color.white));
                }


            }
        });
        btnRight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(callback!=null){
                    callback.onRightClick();
                }
            }
        });
    }

    private void initBack(final Context context) {
        imBack = ((ImageView) findViewById(R.id.recommend_title_back));
        imBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) context).finish();
            }
        });
    }
}
