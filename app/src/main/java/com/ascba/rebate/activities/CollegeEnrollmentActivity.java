package com.ascba.rebate.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWork4Activity;
import com.ascba.rebate.adapter.ViewPagerAdapter;
import com.ascba.rebate.view.ShopABar;
import com.ascba.rebate.view.pagerWithTurn.ShufflingViewPager;
import com.ascba.rebate.view.pagerWithTurn.ShufflingViewPagerAdapter;
import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李鹏 on 2017/03/16 0016.
 * 商学院报名
 */

public class CollegeEnrollmentActivity extends BaseNetWork4Activity {

    private ShopABar shopABar;
    private SlidingTabLayout slidingtablayout;
    private ViewPager mViewPager;
    private List<View> viewList = new ArrayList<>();//页卡视图集合
    private Context context;
    private ShufflingViewPager shufflingViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enrollment);
        context = this;
        initView();
    }

    private void initView() {
        //导航栏
        shopABar = (ShopABar) findViewById(R.id.shopbar);
        shopABar.setImageOtherEnable(false);
        shopABar.setImMsgSta(R.mipmap.icon_person_black);
        shopABar.setCallback(new ShopABar.Callback() {
            @Override
            public void back(View v) {
                finish();
            }

            @Override
            public void clkMsg(View v) {
                /**
                 * 学员证
                 */
                Intent intent = new Intent(context, MemCardActivity.class);
                startActivity(intent);
            }

            @Override
            public void clkOther(View v) {

            }
        });

        //广告轮播
        shufflingViewPager = (ShufflingViewPager) findViewById(R.id.shop_pager);
        List<String> stringList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            stringList.add("http://image18-c.poco.cn/mypoco/myphoto/20170316/14/18505011120170316143842041_640.jpg");
        }
        ShufflingViewPagerAdapter shufflingViewPagerAdapter = new ShufflingViewPagerAdapter(context, stringList);
        shufflingViewPager.setAdapter(shufflingViewPagerAdapter);
        shufflingViewPager.start();

        initViewpager();
    }

    private void initViewpager() {
        slidingtablayout = (SlidingTabLayout) findViewById(R.id.slidingtablayout);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        View viewNotice = LayoutInflater.from(context).inflate(R.layout.enrollment_notice, null);
        View viewDetails = LayoutInflater.from(context).inflate(R.layout.enrollment_details, null);
        viewList.add(viewNotice);
        viewList.add(viewDetails);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(viewList);
        mViewPager.setAdapter(viewPagerAdapter);
        String[] title = new String[]{"报名须知", "培训详情"};
        slidingtablayout.setViewPager(mViewPager, title);
    }
}
