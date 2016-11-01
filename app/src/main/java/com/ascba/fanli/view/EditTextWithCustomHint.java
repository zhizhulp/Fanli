package com.ascba.fanli.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.EditText;

import com.ascba.fanli.R;
import com.ascba.fanli.utils.HintUtils;
import com.ascba.fanli.utils.ScreenDpiUtils;

/**
 * 可以自定义hint大小的EditText
 */

public class EditTextWithCustomHint extends EditText {
    public EditTextWithCustomHint(Context context) {
        super(context);
    }


    public EditTextWithCustomHint(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public EditTextWithCustomHint(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context,AttributeSet attrs) {

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.EditTextWithCustomHint);
        int mpx = ScreenDpiUtils.dip2px(context, 14);
        float dimension = ta.getDimension(R.styleable.EditTextWithCustomHint_hintSize, mpx);
        int mdp2 = ScreenDpiUtils.px2dp(context, dimension);
        CharSequence hint = getHint();
        if(hint!=null){
            HintUtils.customHint(this,getHint().toString(),mdp2);
        }
        ta.recycle();
    }

}
