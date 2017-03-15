package com.ascba.rebate.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWork4Activity;
import com.ascba.rebate.adapter.FragmentPagerAdapter;
import com.ascba.rebate.fragments.DeliverGoodsFragment;
import com.ascba.rebate.fragments.TypeFragment;
import com.ascba.rebate.view.ShopABar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李鹏 on 2017/03/14 0014.
 * 我的订单页面
 */

public class MyOrderActivity extends BaseNetWork4Activity {

    private ShopABar shopABar;
    private Context context;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private LayoutInflater mInflater;
    private List<Bean> mTitleList = new ArrayList<>();//页卡标题集合
    private List<Fragment> fragmentList = new ArrayList<>();//页卡视图集合

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        context = this;
        mInflater = LayoutInflater.from(context);
        initView();
    }

    private void initView() {
        shopABar = (ShopABar) findViewById(R.id.activity_order_bar);
        shopABar.setImageOtherEnable(false);

        shopABar.setCallback(new ShopABar.Callback() {
            @Override
            public void back(View v) {
                finish();
            }

            @Override
            public void clkMsg(View v) {
                //消息中心
                Toast.makeText(context, "消息中心", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void clkOther(View v) {

            }
        });
        mTabLayout = (TabLayout) findViewById(R.id.activity_order_tab);
        mViewPager = (ViewPager) findViewById(R.id.activity_order_vp);
        initViewpagerView();
    }

    /**
     * 初始化Viewpager
     */
    private void initViewpagerView() {

        TypeFragment fragment1 = new TypeFragment();
        fragmentList.add(fragment1);

        TypeFragment fragment2 = new TypeFragment();
        fragmentList.add(fragment2);

        //待发货
        DeliverGoodsFragment goodsFragment = new DeliverGoodsFragment();
        fragmentList.add(goodsFragment);

        TypeFragment fragment3 = new TypeFragment();
        fragmentList.add(fragment3);

        TypeFragment fragment4 = new TypeFragment();
        fragmentList.add(fragment4);

        FragmentPagerAdapter mAdapter = new FragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
        mViewPager.setAdapter(mAdapter);//给ViewPager设置适配器
        mTabLayout.setupWithViewPager(mViewPager);//将TabLayout和ViewPager关联起来。

        //添加页卡标题
        mTitleList.add(new Bean("全部", 0));
        mTitleList.add(new Bean("待付款", 0));
        mTitleList.add(new Bean("待发货", 0));
        mTitleList.add(new Bean("待收货", 5));
        mTitleList.add(new Bean("待评价", 7));


        for (int i = 0; i < mAdapter.getCount(); i++) {
            TabLayout.Tab tab = mTabLayout.getTabAt(i);//获得每一个tab
            tab.setCustomView(R.layout.item_tablayout);//给每一个tab设置view
            if (i == 0) {
                // 设置第一个tab的TextView是被选择的样式
                tab.getCustomView().findViewById(R.id.item_tablayout_text).setSelected(true);//第一个tab被选中
            }
            TextView title = (TextView) tab.getCustomView().findViewById(R.id.item_tablayout_text);
            title.setText(mTitleList.get(i).getTitle());//设置tab上的文字

            TextView noti = (TextView) tab.getCustomView().findViewById(R.id.item_tablayout_noti);
            if (mTitleList.get(i).getNum() > 0) {
                noti.setVisibility(View.VISIBLE);
                noti.setText(String.valueOf(mTitleList.get(i).getNum()));//设置tab上的角标
            } else {
                noti.setVisibility(View.INVISIBLE);
            }

        }


        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getCustomView().findViewById(R.id.item_tablayout_text).setSelected(true);
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getCustomView().findViewById(R.id.item_tablayout_text).setSelected(false);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public class Bean {
        private String title;
        private int num;

        public Bean(String title, int num) {
            this.title = title;
            this.num = num;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }
    }

}
