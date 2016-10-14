package com.qlqwgw.fanli.activities.splash;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.qlqwgw.fanli.R;
import com.qlqwgw.fanli.activities.base.BaseActivity;

public class SplashActivity extends BaseActivity {

    //Handler的创建
    private void initHandler(){
        mHandler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
            }
        };
    }
    //Runnable的创建
    private void initThread(){
        mThread=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }
}
