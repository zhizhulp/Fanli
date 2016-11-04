package com.ascba.fanli.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.ascba.fanli.R;
import com.ascba.fanli.activities.base.BaseActivity;
import com.ascba.fanli.activities.main.MainActivity;
import com.ascba.fanli.utils.ScreenDpiUtils;

public class PersonalDataActivity extends BaseActivity {
    public static final int nickNameRequest= 0x00;
    public static final int sexRequest= 0x00;
    public static final int locationRequest= 0x00;
    public static final int ageRequest= 0x00;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_data);
    }
    //进入修改昵称界面
    public void personalNickNameChange(View view) {
        Intent intent=new Intent(this, ModifyNicknameActivity.class);
        startActivityForResult(intent,nickNameRequest);
    }
    //处理返回数据
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(resultCode){
            case 0:
                break;
            case 1:
                break;
        }
    }
    //进入修改性别的页面
    public void personalSexChange(View view) {
        Intent intent=new Intent(this, SexChangeActivity.class);
        startActivityForResult(intent,sexRequest);
    }
    //进入修改地址的页面
    public void personalLocationChange(View view) {
        Intent intent=new Intent(this, LocationActivity.class);
        startActivityForResult(intent,locationRequest);
    }
    //进入修改年龄的页面
    public void personalAgeChange(View view) {
        Intent intent=new Intent(this, AgeChangeActivity.class);
        startActivityForResult(intent,ageRequest);
    }
    //进入修改用户头像的页面
    public void userIconClick(View view) {
        PopupWindow popupWindow=new PopupWindow(this);
        popupWindow.setContentView(getLayoutInflater().inflate(R.layout.personal_select_icon_pop,null));
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        int screenWideDp = ScreenDpiUtils.px2dp(this, getResources().getDisplayMetrics().widthPixels);
        int popWide = ScreenDpiUtils.dip2px(this, screenWideDp - 30);
        popupWindow.setWidth(popWide);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        int marinBottom=ScreenDpiUtils.dip2px(this, 15);
        popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM,0,marinBottom);
    }
}
