package com.ascba.rebate.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;

import com.ascba.rebate.R;

/**
 * 拥有3中状态的按钮
 */

public class StatusBtn extends CheckBox {

    public StatusBtn(Context context) {
        super(context);
        init();
    }

    public StatusBtn(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StatusBtn(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setButtonDrawable(new ColorDrawable());
        setBackgroundDrawable(getResources().getDrawable(R.drawable.goods_standrad_bg));
    }

    public void setNotEnable(){
        setEnabled(false);
        setTextColor(0xffbbbbbb);
    }
}
