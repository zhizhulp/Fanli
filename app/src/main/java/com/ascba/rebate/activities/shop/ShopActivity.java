package com.ascba.rebate.activities.shop;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.WindowManager;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWork4Activity;
import com.ascba.rebate.activities.login.LoginActivity;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.fragments.CartFragment;
import com.ascba.rebate.fragments.ShopMeFragment;
import com.ascba.rebate.fragments.TypeFragment;
import com.ascba.rebate.fragments.shop.ShopMainFragment;
import com.ascba.rebate.view.ShopTabs;

import java.util.ArrayList;

/**
 * 主界面
 */
public class ShopActivity extends BaseNetWork4Activity implements ShopTabs.Callback {

    private static final int REQUEST_LOGIN_CART = 0;
    private static final int REQUEST_LOGIN_ME = 1;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private ShopMainFragment mFirstFragment = new ShopMainFragment();
    private TypeFragment mSecondFragment = new TypeFragment();
    private CartFragment mThirdFragment = new CartFragment();
    private ShopMeFragment mFourthFragment = new ShopMeFragment();
    private ShopTabs shopTabs;

    public ArrayList<Fragment> getmFragments() {
        return mFragments;
    }

    public ShopMainFragment getmFirstFragment() {
        return mFirstFragment;
    }

    public TypeFragment getmSecondFragment() {
        return mSecondFragment;
    }

    public CartFragment getmThirdFragment() {
        return mThirdFragment;
    }

    public ShopMeFragment getmFourthFragment() {
        return mFourthFragment;
    }

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

    private void findViews() {

        shopTabs = ((ShopTabs) findViewById(R.id.shop_tabs));
        shopTabs.setCallback(this);
        selFrgByPos(0, mFirstFragment);
    }

    //首页
    @Override
    public void clickZero(View v) {
        selFrgByPos(0, mFirstFragment);
    }

    //分类
    @Override
    public void clickOne(View v) {
        selFrgByPos(1, mSecondFragment);
    }

    //购物车
    @Override
    public void clickThree(View v) {
        selFrgByPos(2, mThirdFragment);
    }

    //我
    @Override
    public void clickFour(View v) {
        selFrgByPos(3, mFourthFragment);
    }

    public void selFrgByPos(int position, Fragment fragt) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
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
            if (fragt== fragment) {
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

        if (data == null) {
            switch (requestCode){
                case REQUEST_LOGIN_CART:
                    selFrgByPos(0, getmFirstFragment());
                    getShopTabs().statusChaByPosition(0, 2);
                    getShopTabs().setFilPos(0);
                    break;
                case REQUEST_LOGIN_ME:
                    selFrgByPos(0, getmFirstFragment());
                    getShopTabs().statusChaByPosition(0, 3);
                    getShopTabs().setFilPos(0);
                    break;
            }

        } else {
            switch (requestCode) {
                case REQUEST_LOGIN_CART:
                    if (resultCode == RESULT_OK) {
                        selFrgByPos(2,mThirdFragment);
                    }else {
                        selFrgByPos(0, mFirstFragment);
                        getShopTabs().statusChaByPosition(0, 2);
                        getShopTabs().setFilPos(0);
                    }
                    break;
                case REQUEST_LOGIN_ME:
                    if (resultCode == RESULT_OK) {
                        selFrgByPos(3,mFourthFragment);
                    }else {
                        selFrgByPos(0, mFirstFragment);
                        getShopTabs().statusChaByPosition(0, 3);
                        getShopTabs().setFilPos(0);
                    }
                    break;

            }
        }
    }
}

