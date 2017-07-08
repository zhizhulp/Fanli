package com.ascba.rebate.view;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.style.ImageSpan;

/**
 * Created by 李平 on 2017/7/7.
 * 可以使用圆角背景的Span(和SpannableString结合使用)
 */

public class DrawableBgSpan extends ImageSpan {
    public DrawableBgSpan(Drawable d) {
        super(d);
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        //paint.setTypeface(Typeface.create("normal", Typeface.BOLD));
        paint.setTextSize(50);
        int len = Math.round(paint.measureText(text, start, end));
        getDrawable().setBounds(0, 0, len, 55);
        super.draw(canvas, text, start, end, x, top, y, bottom, paint);
        paint.setColor(0xfffa5e5f);
        //paint.setTypeface(Typeface.create("normal", Typeface.BOLD));
        paint.setTextSize(40);
        canvas.drawText(text.subSequence(start, end).toString(), x + 10, y, paint);
    }
}
