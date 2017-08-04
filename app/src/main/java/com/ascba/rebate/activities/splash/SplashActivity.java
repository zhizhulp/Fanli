package com.ascba.rebate.activities.splash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseUIActivity;
import com.ascba.rebate.activities.guide.GuideActivity;
import com.ascba.rebate.activities.main.MainActivity;
import com.ascba.rebate.utils.SharedPreferencesUtil;


/**
 * 启动页
 */
public class SplashActivity extends BaseUIActivity {

    private Handler handler = new Handler();
    private ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setUIMode(BaseUIActivity.UIMODE_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        imageView = (ImageView) findViewById(R.id.iv_entry);

        startSplashActivity();
    }

    private void startSplashActivity() {
        startMainActivity();
        /*handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                imageView.setImageResource(R.mipmap.start1);

            }
        }, 3000);*/
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
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    SplashActivity.this.finish();
                }
            }
        }, 2000);
    }

    /**
     * 屏蔽物理返回按钮
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

