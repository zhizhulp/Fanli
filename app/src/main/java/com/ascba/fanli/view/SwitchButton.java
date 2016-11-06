package com.ascba.fanli.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ascba.fanli.R;

/**
 * Created by Administrator on 2016/11/6.
 */

public class SwitchButton extends RadioGroup {
    private RadioButton btnLeft;
    private RadioButton btnRight;
    private Callback callback;
    public interface Callback{
        void onLeftClick();
        void onRightClick();
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

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.switch_btn,this,true);
        btnLeft = ((RadioButton) findViewById(R.id.btn_left));
        btnRight = ((RadioButton) findViewById(R.id.btn_right));
        btnLeft.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    btnLeft.setTextColor(getResources().getColor(R.color.moneyBarColor));
                }else{
                    btnLeft.setTextColor(getResources().getColor(R.color.white));
                }
                if(callback!=null){
                    callback.onLeftClick();
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
                if(callback!=null){
                    callback.onRightClick();
                }

            }
        });
    }
}
