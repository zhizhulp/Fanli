package com.ascba.rebate.handlers;

import android.graphics.Bitmap;
import android.util.Log;

import com.squareup.picasso.Transformation;

import static android.content.ContentValues.TAG;

/**
 * Created by 李平 on 2017/5/17 0017.16:40
 */

public class BitmapTransformation implements Transformation {

    //以720*1280 &　1280*720为标准
    @Override
    public Bitmap transform(Bitmap source) {
        int orgWidth = source.getWidth();
        int orgHeight = source.getHeight();
        if(orgWidth >orgHeight){
            Bitmap result = Bitmap.createScaledBitmap(source,1280, 720, true);
            result.recycle();
            return result;
        }else {
            Bitmap result = Bitmap.createScaledBitmap(source,720, 1280, true);
            result.recycle();
            return result;
        }

    }

    @Override
    public String key() {
        return "rebate";
    }
}
