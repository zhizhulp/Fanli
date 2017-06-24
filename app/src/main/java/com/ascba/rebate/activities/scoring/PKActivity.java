package com.ascba.rebate.activities.scoring;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.adapter.score.ScoringPKVPAdapter;
import com.ascba.rebate.fragments.scoring.FriendsRankFragment;
import com.ascba.rebate.fragments.scoring.NationalRankFragment;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.ArrayList;
import java.util.List;

public class PKActivity extends BaseNetActivity  {
    private ViewPager vp;
    private TabLayout tab;
    private ScoringPKVPAdapter vpAdapter;
//    private FragmentManager manager;
//    FragmentTransaction transaction;

    private List<Fragment> fragments = new ArrayList<>();
    private List<String> tablist = new ArrayList<>();
    private List<View> views = new ArrayList<>();
    private String[] mTitles = {"全国排行榜","好友排行榜"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pk);
        initSystemBar();
        initView();
    }

    private void initView() {
        initFragments();
        initTabStr();
        vp = (ViewPager) findViewById(R.id.scoring_pk_vp);
        vpAdapter = new ScoringPKVPAdapter(getSupportFragmentManager(),fragments, tablist);
        vp.setAdapter(vpAdapter);
        tab.setupWithViewPager(vp);
    }


    private void initTabStr() {
        tab = (TabLayout) findViewById(R.id.scoring_pk_tab);
//        tab.addTab(tab.newTab().setText(mTitles[0]));
//        tab.addTab(tab.newTab().setText(mTitles[1]));
        tablist.add(mTitles[0]);
        tablist.add(mTitles[1]);
    }

    private void initFragments() {
        fragments.add(new NationalRankFragment());
        fragments.add(new FriendsRankFragment());

//        manager = getSupportFragmentManager();
//        transaction=manager.beginTransaction();



    }
    private void initSystemBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }

        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        // 自定义颜色
        tintManager.setTintColor(Color.parseColor("#00a8af"));
    }
    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }



}
