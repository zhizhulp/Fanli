package com.ascba.rebate.activities.shop.auction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.ShopMessageActivity;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.adapter.FragmentPagerAdapter;
import com.ascba.rebate.fragments.shop.auction.AuctionAllOrderFragment;
import com.ascba.rebate.fragments.shop.auction.AuctionCompleteOrderFragment;
import com.ascba.rebate.fragments.shop.auction.AuctionPaiedOrderFragment;
import com.ascba.rebate.fragments.shop.auction.AuctionPayOrderFragment;
import com.ascba.rebate.view.ShopABar;
import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李鹏 on 2017/05/25.
 * 我的竞拍订单页面
 */

public class MyAuctionOrderActivity extends BaseNetActivity {

    private ShopABar shopABar;
    private Context context;
    @SuppressLint("StaticFieldLeak")
    private static SlidingTabLayout slidingtablayout;
    private ViewPager mViewPager;
    private List<Fragment> fragmentList = new ArrayList<>();//页卡视图集合
    private static int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        context = this;
        getIndex();
        initView();
    }



    public static void startIntent(Context context, int index) {
        Intent intent = new Intent(context, MyAuctionOrderActivity.class);
        intent.putExtra("index", index);
        context.startActivity(intent);
    }

    private void getIndex() {
        Intent intent = getIntent();
        if (intent != null) {
            index = intent.getIntExtra("index", 0);
        }
    }

    private void initView() {
        shopABar = (ShopABar) findViewById(R.id.activity_order_bar);
        shopABar.setImageOtherEnable(false);
        shopABar.setTitle("我的获拍");

        shopABar.setCallback(new ShopABar.Callback() {
            @Override
            public void back(View v) {
                finishAndCallback();
            }

            @Override
            public void clkMsg(View v) {
                //消息中心
                ShopMessageActivity.startIntent(context);
            }

            @Override
            public void clkOther(View v) {

            }
        });
        slidingtablayout = (SlidingTabLayout) findViewById(R.id.slidingtablayout);
        mViewPager = (ViewPager) findViewById(R.id.activity_order_vp);
        initViewpagerView();
    }

    /**
     * 初始化Viewpager
     */
    private void initViewpagerView() {
        //全部
        AuctionAllOrderFragment allOrderFragment = new AuctionAllOrderFragment();
        fragmentList.add(allOrderFragment);

        //未支付
        AuctionPayOrderFragment payOrderFragment = new AuctionPayOrderFragment();
        fragmentList.add(payOrderFragment);

        //已支付
        AuctionPaiedOrderFragment paiedOrderFragment = new AuctionPaiedOrderFragment();
        fragmentList.add(paiedOrderFragment);

        //已成功
        AuctionCompleteOrderFragment completeOrderFragment = new AuctionCompleteOrderFragment();
        fragmentList.add(completeOrderFragment);


        FragmentPagerAdapter mAdapter = new FragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(mAdapter);//给ViewPager设置适配器


        String[] title = new String[]{"全部", "未支付", "已支付", "已成功"};
        slidingtablayout.setViewPager(mViewPager, title);
        slidingtablayout.setCurrentTab(index);
    }


    private void finishAndCallback() {
        setResult(RESULT_OK, getIntent());
        finish();
    }

    @Override
    public void onBackPressed() {
        finishAndCallback();
        super.onBackPressed();
    }

}
