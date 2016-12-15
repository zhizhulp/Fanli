package com.ascba.rebate.activities;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWorkActivity;
import com.ascba.rebate.utils.ScreenDpiUtils;
import com.uuzuche.lib_zxing.activity.CodeUtils;

public class QRCodeActivity extends BaseNetWorkActivity {


    private ImageView qrImg;
    private SharedPreferences sf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        initViews();
    }

    private void initViews() {
        qrImg = ((ImageView) findViewById(R.id.qrcode));
        sf=getSharedPreferences("first_login_success_name_password",MODE_PRIVATE);
        int uuid = sf.getInt("uuid",-1000);
        if(uuid!=-1000){
            qrImg.setImageBitmap(createQRCode(uuid+"",250,250));
        }
    }


    public Bitmap createQRCode(String content,int width,int height){
        int dpWidth = ScreenDpiUtils.dip2px(this, width);
        int dpHeight = ScreenDpiUtils.dip2px(this, height);
        //return CodeUtils.createImage(content, dpWidth, dpHeight, null);
        return CodeUtils.createImage(content, dpWidth, dpHeight, BitmapFactory.decodeResource(getResources(), R.mipmap.logo));//带图片
    }
}
