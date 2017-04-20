package com.ascba.rebate.activities.splash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.guide.GuideActivity;
import com.ascba.rebate.activities.main.MainWorkActivity;
import com.ascba.rebate.utils.SharedPreferencesUtil;


/**
 * 启动页
 */
public class SplashActivity extends Activity {

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //设置无标题
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);  //设置全屏
        setContentView(R.layout.activity_splash);
        startMainActivity();
    }

    private void startMainActivity() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //startAnim();
                boolean isFirstOpen = SharedPreferencesUtil.getBoolean(SplashActivity.this, SharedPreferencesUtil.FIRST_OPEN, true);
                if (isFirstOpen) {
                    startActivity(new Intent(SplashActivity.this, GuideActivity.class));
                    SplashActivity.this.finish();
                } else {
                    startActivity(new Intent(SplashActivity.this, MainWorkActivity.class));
                    SplashActivity.this.finish();
                }

            }
        }, 2000);
    }

    /**
     * 屏蔽物理返回按钮
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

