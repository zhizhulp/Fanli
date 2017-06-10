package com.ascba.rebate.activities.shop;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.WindowManager;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.activities.login.LoginActivity;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.application.MyApplication;
import com.ascba.rebate.fragments.shop.CartFragment;
import com.ascba.rebate.fragments.shop.ShopMeFragment;
import com.ascba.rebate.fragments.shop.TypeFragment;
import com.ascba.rebate.fragments.shop.ShopMainFragment;
import com.ascba.rebate.view.ShopTabs;

import java.util.ArrayList;
import java.util.List;

/**
 * 商城主界面
 */
public class ShopActivity extends BaseNetActivity implements ShopTabs.Callback {

    public static final int HOMEPAGE = 0;
    public static final int CLASSIFY = 1;
    public static final int CART = 2;
    public static final int ME = 3;
    private static final int REQUEST_LOGIN_CART = 0;
    private static final int REQUEST_LOGIN_ME = 1;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private Fragment mFirstFragment=new ShopMainFragment();
    private Fragment mSecondFragment=new TypeFragment();
    private Fragment mThirdFragment=new CartFragment();
    private Fragment mFourthFragment = new ShopMeFragment();
    private ShopTabs shopTabs;
    private int currIndex = HOMEPAGE;//当前位置
    private static int index=0;

    public ShopTabs getShopTabs() {
        return shopTabs;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            List<Fragment> fragments = getSupportFragmentManager().getFragments();
            if (fragments != null && fragments.size() != 0) {
                for (int i = 0; i < fragments.size(); i++) {
                    Fragment fragment = fragments.get(i);
                    if (fragment instanceof ShopMainFragment)
                        mFirstFragment = fragment;
                        mFragments.add(fragment);
                    if (fragment instanceof TypeFragment)
                        mSecondFragment = fragment;
                        mFragments.add(fragment);
                    if (fragment instanceof CartFragment)
                        mThirdFragment = fragment;
                        mFragments.add(fragment);
                    if (fragment instanceof ShopMeFragment)
                        mFourthFragment = fragment;
                        mFragments.add(fragment);
                }
                index=0;
            }
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_shop);
        findViews();
    }

    private void findViews() {
        shopTabs = ((ShopTabs) findViewById(R.id.shop_tabs));
        shopTabs.setCallback(this);
        shopTabs.statusChaByPosition(index,currIndex);
        selFrgByPos(index);
    }

    //首页
    @Override
    public void clickZero(View v) {
        selFrgByPos(HOMEPAGE);
    }

    //分类
    @Override
    public void clickOne(View v) {
        selFrgByPos(CLASSIFY);
    }

    //购物车
    @Override
    public void clickThree(View v) {
        selFrgByPos(CART);
        setRequestCode(REQUEST_LOGIN_CART);
        MyApplication.isLoad = true;
    }

    //我
    @Override
    public void clickFour(View v) {
        selFrgByPos(ME);
        setRequestCode(REQUEST_LOGIN_ME);
        MyApplication.isLoad = true;
    }

    public void selFrgByPos(int position) {
        index = currIndex = position;
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if(position==0){//首页
            if (!mFirstFragment.isAdded()) {
                ft.add(R.id.fl_change, mFirstFragment,String.valueOf(position));
                mFragments.add(mFirstFragment);
            }
            for (int i = 0; i < mFragments.size(); i++) {
                Fragment fragment = mFragments.get(i);
                if (fragment instanceof ShopMainFragment ) {
                    ft.show(fragment);
                } else {
                    ft.hide(fragment);
                }
            }

        }else if(position==1){//分类
            if (!mSecondFragment.isAdded()) {
                ft.add(R.id.fl_change, mSecondFragment,String.valueOf(position));
                mFragments.add(mSecondFragment);
            }
            for (int i = 0; i < mFragments.size(); i++) {
                Fragment fragment = mFragments.get(i);
                if (fragment instanceof TypeFragment ) {
                    ft.show(fragment);
                } else {
                    ft.hide(fragment);
                }
            }
        }else if(position==2){//购物车
            if (AppConfig.getInstance().getInt("uuid", -1000) == -1000) {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivityForResult(intent, REQUEST_LOGIN_CART);
                return;
            }
            if (!mThirdFragment.isAdded()) {
                ft.add(R.id.fl_change, mThirdFragment,String.valueOf(position));
                mFragments.add(mThirdFragment);
            }
            for (int i = 0; i < mFragments.size(); i++) {
                Fragment fragment = mFragments.get(i);
                if (fragment instanceof CartFragment ) {
                    ft.show(fragment);
                } else {
                    ft.hide(fragment);
                }
            }
        }else if(position==3){//我
            if (AppConfig.getInstance().getInt("uuid", -1000) == -1000) {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivityForResult(intent, REQUEST_LOGIN_ME);
                return;
            }
            if (!mFourthFragment.isAdded()) {
                ft.add(R.id.fl_change, mFourthFragment,String.valueOf(position));
                mFragments.add(mFourthFragment);
            }
            for (int i = 0; i < mFragments.size(); i++) {
                Fragment fragment = mFragments.get(i);
                if (fragment instanceof ShopMeFragment ) {
                    ft.show(fragment);
                } else {
                    ft.hide(fragment);
                }
            }
        }
        ft.commit();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //取消登陆
        if (resultCode == RESULT_CANCELED && (requestCode == REQUEST_LOGIN_ME || requestCode == REQUEST_LOGIN_CART)) {
            index = HOMEPAGE;
            getShopTabs().statusChaByPosition(index, currIndex);
            selFrgByPos(index);
        }

        //点击我的，登陆成功
        if (requestCode == REQUEST_LOGIN_ME && resultCode == RESULT_OK) {
            index = ME;
            selFrgByPos(index);
        }

        //点击购物车，登陆成功
        if (requestCode == REQUEST_LOGIN_CART && resultCode == RESULT_OK) {
            index = CART;
            selFrgByPos(index);
        }
    }

    public static void setIndex(int position) {
        index = position;
    }
    @Override
    protected void onResume() {
        super.onResume();
        /*if (currIndex != index) {
            getShopTabs().statusChaByPosition(index, currIndex);
            selFrgByPos(index);
        }
        if (AppConfig.getInstance().getInt("uuid", -1000) == -1000) {
            try {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                if (index == 2) {
                    ft.remove(fragments[3]);
                    mFragments.remove(fragments[3]);
                } else if (index == 3) {
                    ft.remove(fragments[2]);
                    mFragments.remove(fragments[2]);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/
    }
}

