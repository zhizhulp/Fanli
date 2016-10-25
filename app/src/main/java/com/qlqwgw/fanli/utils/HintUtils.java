package com.qlqwgw.fanli.utils;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.style.AbsoluteSizeSpan;
import android.widget.EditText;

/**
 * Created by Administrator on 2016/10/25.
 */

public class HintUtils {
    /**
     * 设置EditText中Hint字体大小
     *
     * @param editText
     * @param hintString
     */
    public static void customHint(EditText editText, String hintString ,int mdp) {
        SpannableString spannableString = new SpannableString(hintString);
        AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(mdp, true);
        spannableString.setSpan(absoluteSizeSpan , 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        editText.setHint(new SpannedString(spannableString)); // 一定要进行转换,否则属性只有一次
    }
}
