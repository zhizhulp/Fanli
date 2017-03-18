package com.ascba.rebate.activities.shop;


import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWorkActivity;
import com.ascba.rebate.beans.TabEntity;
import com.ascba.rebate.fragments.CartFragment;
import com.ascba.rebate.fragments.ShopMeFragment;
import com.ascba.rebate.fragments.TypeFragment;
import com.ascba.rebate.fragments.shop.ShopMainFragment;
import com.ascba.rebate.view.Rotate3D.ActivitySwitcher;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;

import java.util.ArrayList;

/**
 * 主界面
 */
public class ShopActivity extends BaseNetWorkActivity {
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private String[] mTitles = {"首页", "分类", "购物车", "我"};
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private ShopMainFragment mFirstFragment;
    private TypeFragment mSecondFragment;
    private CartFragment mThirdFragment;
    private ShopMeFragment mFourthFragment;
    private int[] mIconUnselectIds = {
            R.mipmap.tab_main, R.mipmap.fenlei,
            R.mipmap.tab_shop, R.mipmap.tab_me};
    private int[] mIconSelectIds = {
            R.mipmap.tab_main_select, R.mipmap.tab_type_select,
            R.mipmap.tab_shop_select, R.mipmap.tab_me_select};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        findViews();
    }

    private void findViews() {
        CommonTabLayout mTabLayout_2 = (CommonTabLayout) findViewById(R.id.tabs);

        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
        if (mFirstFragment == null) {
            mFirstFragment = new ShopMainFragment();
            mFragments.add(mFirstFragment);
        }
        if (mSecondFragment == null) {
            mSecondFragment = new TypeFragment();
            mFragments.add(mSecondFragment);
        }
        if (mThirdFragment == null) {
            mThirdFragment = new CartFragment();
            mFragments.add(mThirdFragment);
        }
        if (mFourthFragment == null) {
            mFourthFragment = new ShopMeFragment();
            mFragments.add(mFourthFragment);
        }
        mTabLayout_2.setTabData(mTabEntities, this, R.id.fl_change, mFragments);
        mTabLayout_2.setCurrentTab(0);
    }

}

