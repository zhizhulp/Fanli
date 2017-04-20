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
import com.ascba.rebate.fragments.CartFragment;
import com.ascba.rebate.fragments.ShopMeFragment;
import com.ascba.rebate.fragments.TypeFragment;
import com.ascba.rebate.fragments.shop.ShopMainFragment;
import com.ascba.rebate.view.ShopTabs;

import java.util.ArrayList;

/**
 * 主界面
 */
public class ShopActivity extends BaseNetActivity implements ShopTabs.Callback {

    public static final int HOMEPAGE = 0;
    public static final int CLASSIFY = 1;
    public static final int CART = 2;
    public static final int ME = 3;
    private static final int REQUEST_LOGIN_CART = 0;
    private static final int REQUEST_LOGIN_ME = 1;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private ShopMainFragment mFirstFragment = new ShopMainFragment();
    private TypeFragment mSecondFragment = new TypeFragment();
    private CartFragment mThirdFragment = new CartFragment();
    private ShopMeFragment mFourthFragment = new ShopMeFragment();
    private ShopTabs shopTabs;
    private int currIndex = HOMEPAGE;//当前位置
    private static int index;
    private Fragment[] fragments;

    public ShopTabs getShopTabs() {
        return shopTabs;
    }

    public void setShopTabs(ShopTabs shopTabs) {
        this.shopTabs = shopTabs;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_shop);
        findViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (currIndex != index) {
            getShopTabs().statusChaByPosition(index, currIndex);
            getShopTabs().setFilPos(index);
            selFrgByPos(index);
        }
    }

    private void findViews() {
        shopTabs = ((ShopTabs) findViewById(R.id.shop_tabs));
        shopTabs.setCallback(this);
        fragments = new Fragment[]{mFirstFragment, mSecondFragment, mThirdFragment, mFourthFragment};
        selFrgByPos(currIndex);
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
        Fragment fragt = fragments[position];
        if (!mFragments.contains(fragt)) {
            if (fragt instanceof CartFragment && AppConfig.getInstance().getInt("uuid", -1000) == -1000) {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivityForResult(intent, REQUEST_LOGIN_CART);
                return;
            }
            if (fragt instanceof ShopMeFragment && AppConfig.getInstance().getInt("uuid", -1000) == -1000) {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivityForResult(intent, REQUEST_LOGIN_ME);
                return;
            }
            ft.add(R.id.fl_change, fragt);
            mFragments.add(fragt);
        }
        for (int i = 0; i < mFragments.size(); i++) {
            Fragment fragment = mFragments.get(i);
            if (fragt == fragment) {
                ft.show(fragment);
            } else {
                ft.hide(fragment);
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
            getShopTabs().setFilPos(index);
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
}

