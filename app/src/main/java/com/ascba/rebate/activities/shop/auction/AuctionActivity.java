package com.ascba.rebate.activities.shop.auction;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.activities.login.LoginActivity;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.application.MyApplication;
import com.ascba.rebate.fragments.shop.auction.AuctionCartFragment;
import com.ascba.rebate.fragments.shop.auction.AuctionHomePageFragment;
import com.ascba.rebate.fragments.shop.auction.AuctionMainPlaceFragment;
import com.ascba.rebate.fragments.shop.auction.AuctionMeFragment;
import com.ascba.rebate.view.AuctionTabs;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李鹏 on 2017/5/22.
 * 拍卖
 */

public class AuctionActivity extends BaseNetActivity implements AuctionTabs.Callback {

    private static final int REQUEST_LOGIN_CART = 1;
    private static final int REQUEST_LOGIN_ME = 2;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private Fragment mFirstFragment=new AuctionHomePageFragment();
    private Fragment mSecondFragment=new AuctionMainPlaceFragment();
    private Fragment mThirdFragment=new AuctionCartFragment();
    private Fragment mFourthFragment = new AuctionMeFragment();
    private AuctionTabs shopTabs;

    public AuctionTabs getShopTabs() {
        return shopTabs;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            solveRebuildFragment();
        }
        setContentView(R.layout.activity_auction);
        findViews();
    }


    private void findViews() {
        shopTabs = ((AuctionTabs) findViewById(R.id.shop_tabs));
        shopTabs.setCallback(this);
        selFrgByPos(shopTabs.getFilPos());
    }

    //首页
    @Override
    public void clickZero(View v) {
        selFrgByPos(0);
    }

    //主会场
    @Override
    public void clickOne(View v) {
        selFrgByPos(1);
    }

    //购物车
    @Override
    public void clickThree(View v) {
        selFrgByPos(2);
        setRequestCode(REQUEST_LOGIN_CART);
        MyApplication.isLoad = true;
    }

    //我
    @Override
    public void clickFour(View v) {
        selFrgByPos(3);
        setRequestCode(REQUEST_LOGIN_ME);
        MyApplication.isLoad = true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //取消登陆
        if (resultCode == RESULT_CANCELED && (requestCode == REQUEST_LOGIN_ME )) {
            shopTabs.statusChaByPosition(0, shopTabs.getFilPos());
            selFrgByPos(0);
        }

        //取消登陆
        if (resultCode == RESULT_CANCELED && (requestCode == REQUEST_LOGIN_CART )) {
            shopTabs.statusChaByPosition(0, shopTabs.getFilPos());
            selFrgByPos(0);
        }

        //点击购物车，登陆成功
        if (requestCode == REQUEST_LOGIN_CART && resultCode == RESULT_OK) {
            shopTabs.statusChaByPosition(2, shopTabs.getFilPos());
            selFrgByPos(2);
        }

        //点击我的，登陆成功
        if (requestCode == REQUEST_LOGIN_ME && resultCode == RESULT_OK) {
            shopTabs.statusChaByPosition(3, shopTabs.getFilPos());
            selFrgByPos(3);
        }

    }

    //解决fragment重叠问题
    private void solveRebuildFragment() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null && fragments.size() != 0) {
            for (int i = 0; i < fragments.size(); i++) {
                Fragment fragment = fragments.get(i);
                if (fragment instanceof AuctionHomePageFragment)
                    mFirstFragment = fragment;
                mFragments.add(fragment);
                if (fragment instanceof AuctionMainPlaceFragment)
                    mSecondFragment = fragment;
                mFragments.add(fragment);
                if (fragment instanceof AuctionCartFragment)
                    mThirdFragment = fragment;
                mFragments.add(fragment);
                if (fragment instanceof AuctionMeFragment)
                    mFourthFragment = fragment;
                mFragments.add(fragment);
            }
        }
    }
    public void selFrgByPos(int position) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if(position==0){//首页
            if (!mFirstFragment.isAdded()) {
                ft.add(R.id.fl_change, mFirstFragment,String.valueOf(position));
                mFragments.add(mFirstFragment);
            }
            for (int i = 0; i < mFragments.size(); i++) {
                Fragment fragment = mFragments.get(i);
                if (fragment instanceof AuctionHomePageFragment ) {
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
                if (fragment instanceof AuctionMainPlaceFragment ) {
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
                if (fragment instanceof AuctionCartFragment ) {
                    ft.show(fragment);
                } else {
                    ft.hide(fragment);
                }
            }
        }else if(position==3){//
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
                if (fragment instanceof AuctionMeFragment ) {
                    ft.show(fragment);
                } else {
                    ft.hide(fragment);
                }
            }
        }
        ft.commit();
    }
}

