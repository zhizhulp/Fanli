package com.ascba.rebate.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.ascba.rebate.R;
import com.ascba.rebate.utils.ScreenDpiUtils;

import java.io.File;

/**
 * Created by Administrator on 2016/12/28 0028.
 */

public class SelectIconManager implements View.OnClickListener {
    private PopupWindow popupWindow;
    private Activity context;
    private Callback callback;

    public Callback getCallback() {
        return callback;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public interface Callback{
        void clickCamera();
        void clickAlbum();
    }

    public SelectIconManager(Activity context){
        popupWindow=new PopupWindow(context);
        this.context=context;
        initPop();
    }

    private void initPop() {
        View popView = LayoutInflater.from(context).inflate(R.layout.personal_select_icon_pop, null);
        View camera = popView.findViewById(R.id.head_icon_select_camera);
        View album = popView.findViewById(R.id.head_icon_select_album);
        View cancel = popView.findViewById(R.id.head_icon_select_cancel);
        camera.setOnClickListener(this);//相机拍照
        album.setOnClickListener(this);//相册选择
        cancel.setOnClickListener(this);//取消选择
        popupWindow.setContentView(popView);
        popupWindow.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.personal_head_icon_select_pop_bg));//外部点击消失
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = context.getWindow().getAttributes();
                params.alpha = 1.0f;
                context.getWindow().setAttributes(params);
            }
        });
        int screenWideDp = ScreenDpiUtils.px2dp(context, context.getResources().getDisplayMetrics().widthPixels);
        int popWide = ScreenDpiUtils.dip2px(context, screenWideDp - 30);
        popupWindow.setWidth(popWide);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        int marinBottom = ScreenDpiUtils.dip2px(context, 15);
        popupWindow.showAtLocation(context.getWindow().getDecorView(), Gravity.BOTTOM, 0, marinBottom);
        //设置背景变暗
        WindowManager.LayoutParams params = context.getWindow().getAttributes();
        params.alpha = 0.5f;
        context.getWindow().setAttributes(params);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_icon_select_camera:
                popupWindow.dismiss();
                if(callback!=null){
                    callback.clickCamera();
                }

                break;
            case R.id.head_icon_select_album:
                popupWindow.dismiss();
                if(callback!=null){
                    callback.clickAlbum();
                }

                break;
            case R.id.head_icon_select_cancel:
                popupWindow.dismiss();
                break;
        }
    }



}
