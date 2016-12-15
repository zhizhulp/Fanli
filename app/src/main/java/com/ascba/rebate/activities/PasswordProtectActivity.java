package com.ascba.rebate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWorkActivity;

public class PasswordProtectActivity extends BaseNetWorkActivity {
    public static final int answerToPassPro=0x02;
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_protect);
        type = getIntent().getIntExtra("type",0);
    }

    //处理返回信息
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
    //点击下一步进去 密保详情 页面
    public void passwordProtectNext(View view) {
        if(type==111){
            Intent intent=new Intent(this,PasswordProtect2Activity.class);
            startActivityForResult(intent,answerToPassPro);
        }else if(type==222){
            Intent intent=new Intent(this,PayPasswordChangeActivity.class);
            startActivityForResult(intent,answerToPassPro);
        }
    }
}
