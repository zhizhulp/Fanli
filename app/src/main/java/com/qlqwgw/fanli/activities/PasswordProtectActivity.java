package com.qlqwgw.fanli.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.qlqwgw.fanli.R;
import com.qlqwgw.fanli.activities.base.BaseActivity;

public class PasswordProtectActivity extends BaseActivity {
    public static final int answerToPassPro=0x02;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_protect);
    }

    public void passwordProtectDetail(View view) {

    }
    //处理返回信息
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
    //点击下一步进去 密保详情 页面
    public void passwordProtectNext(View view) {
        Intent intent=new Intent(this,PasswordProtect2Activity.class);
        startActivityForResult(intent,answerToPassPro);
    }
}
