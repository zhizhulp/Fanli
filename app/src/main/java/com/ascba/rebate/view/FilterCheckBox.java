package com.ascba.rebate.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.ascba.rebate.R;

/**
 * 筛选多选按钮
 */

public class FilterCheckBox extends CheckBox {
    public FilterCheckBox(Context context) {
        super(context);
        init();
    }

    public FilterCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FilterCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init() {
        setButtonDrawable(new ColorDrawable());
        setBackgroundDrawable(getResources().getDrawable(R.drawable.filter_cb_bg));
        setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    setTextColor(getResources().getColor(R.color.moneyBarColor));
                }else {
                    setTextColor(getResources().getColor(R.color.shop_normal_text_color));
                }
            }
        });
    }

}
