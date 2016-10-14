package com.qlqwgw.fanli.activities.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.qlqwgw.fanli.R;
import com.qlqwgw.fanli.activities.base.BaseActivity;
import com.qlqwgw.fanli.activities.guide.GuideActivity;

public class SplashActivity extends BaseActivity {
    private static final int MESSAGE_JUMP=0X00;

    //Handler的创建
    private void initHandler(){
        mHandler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                int order=msg.what;
                switch (order){
                    case MESSAGE_JUMP:
                        Intent intent=new Intent(SplashActivity.this,GuideActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        };
    }
    //Runnable的创建
    private void initThread(){
        mThread=new Thread(new Runnable() {
            @Override
            public void run() {
                mHandler.sendEmptyMessageDelayed(MESSAGE_JUMP,3000);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }
}
