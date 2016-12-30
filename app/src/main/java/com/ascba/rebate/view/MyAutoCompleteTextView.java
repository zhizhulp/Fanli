package com.ascba.rebate.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

import com.ascba.rebate.R;
import com.ascba.rebate.utils.HintUtils;
import com.ascba.rebate.utils.ScreenDpiUtils;

/**
 * Created by Administrator on 2016/12/24 0024.
 */

public class MyAutoCompleteTextView extends AutoCompleteTextView {
    public MyAutoCompleteTextView(Context context) {
        super(context);
    }

    public MyAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MyAutoCompleteTextView(Context context, AttributeSet attrs, int defStyleAttr) {
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
