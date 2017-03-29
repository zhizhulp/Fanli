package com.ascba.rebate.activities.guide;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.login.LoginActivity;
import com.ascba.rebate.activities.main.MainActivity;
import com.ascba.rebate.utils.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xialo on 2016/7/25.
 */
public class GuideActivity extends Activity implements View.OnClickListener {
    private ViewPager vp;
    private GuideViewPagerAdapter adapter;
    private List<View> views;
    private ImageView startImg;

    // 引导页图片资源
    private static final int[] pics = { R.layout.guide_view1,
            R.layout.guide_view2, R.layout.guide_view3,R.layout.guide_view4};

    private Button btnEnter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //设置无标题
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);  //设置全屏
        setContentView(R.layout.activity_guide);
        views = new ArrayList<View>();
        // 初始化引导页视图列表
        for (int i = 0; i < pics.length; i++) {
            View view = LayoutInflater.from(this).inflate(pics[i], null);

            if (i == pics.length - 1) {
                startImg = (ImageView) view.findViewById(R.id.guide04_start);
                btnEnter = ((Button) view.findViewById(R.id.btn_enter));
                btnEnter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        enterMainActivity();
                    }
                });
                startImg.setTag("enter");
                startImg.setOnClickListener(this);
            }

            views.add(view);

        }

        vp = (ViewPager) findViewById(R.id.vp_guide);
        adapter = new GuideViewPagerAdapter(views);
        vp.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 如果切换到后台，就设置下次不进入功能引导页
        SharedPreferencesUtil.putBoolean(GuideActivity.this, SharedPreferencesUtil.FIRST_OPEN, false);
        finish();
    }


    @Override
    public void onClick(View v) {
        if (v.getTag().equals("enter")) {
            enterMainActivity();
        }
    }


    private void enterMainActivity() {
        Intent intent = new Intent(GuideActivity.this, LoginActivity.class);
        startActivity(intent);
        SharedPreferencesUtil.putBoolean(GuideActivity.this, SharedPreferencesUtil.FIRST_OPEN, false);
        finish();
    }

}
