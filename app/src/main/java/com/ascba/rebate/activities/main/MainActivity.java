package com.ascba.rebate.activities.main;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.activities.base.BaseUIActivity;
import com.ascba.rebate.activities.login.LoginActivity;
import com.ascba.rebate.activities.shop.ShopActivity;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.application.MyApplication;
import com.ascba.rebate.fragments.main.HomePageFragment;
import com.ascba.rebate.fragments.main.MeFragment;
import com.ascba.rebate.fragments.main.MoneyFragment;
import com.ascba.rebate.fragments.main.SideFragment;
import com.ascba.rebate.utils.LogUtils;
import com.ascba.rebate.utils.NetUtils;
import com.ascba.rebate.view.AppTabs;
import com.taobao.sophix.SophixManager;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;


/**
 * 主界面
 */
public class MainActivity extends BaseNetActivity implements AppTabs.Callback {
    private int currIndex = HOMEPAGE;//当前位置
    private static int index = 0;
    public static final int HOMEPAGE = 0;
    public static final int SLIDE = 1;
    public static final int SHOP = 2;
    public static final int CAIFU = 3;
    public static final int ME = 4;
    private static final int MSG_SET_ALIAS = 1001;
    private static final int MSG_SET_TAGS = 1002;
    private static final int REQUEST_LOGIN_SHOP = 2015;
    public static final int REQUEST_LOGIN_CAIFU = 2016;
    private static final int REQUEST_LOGIN_ME = 2017;
    private List<Fragment> fgts = new ArrayList<>();
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    JPushInterface.setAliasAndTags(getApplicationContext(), (String) msg.obj, null, mAliasCallback);
                    break;
                case MSG_SET_TAGS:
                    JPushInterface.setAliasAndTags(getApplicationContext(), null, (Set<String>) msg.obj, mTagsCallback);
                    break;
                default:
                    break;
            }
        }
    };
    private Fragment mHomePageFragment;
    private Fragment mSideFragment;
    private Fragment mMoneyFragment;
    private Fragment mMeFragment;
    private AppTabs appTabs;
    private Timer timer=new Timer();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setUIMode(BaseUIActivity.UIMODE_TRANSPARENT);
        super.onCreate(savedInstanceState);
        SophixManager.getInstance().queryAndLoadNewPatch();
        //解决fragment重叠问题
        resolveProblems(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
    }
    private void findViews() {
        appTabs = ((AppTabs) findViewById(R.id.tabs));
        appTabs.setCallback(this);
        appTabs.statusChaByPosition(index, currIndex);
        selFrgByPos(index);
        init();//设置极光推送用户标识
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //取消登陆
        if (resultCode == RESULT_CANCELED && (requestCode == REQUEST_LOGIN_ME || requestCode == REQUEST_LOGIN_CAIFU)) {
            index = HOMEPAGE;
            appTabs.statusChaByPosition(index, currIndex);
            selFrgByPos(index);
        }

        //点击我的，登陆成功
        if (requestCode == REQUEST_LOGIN_ME && resultCode == RESULT_OK) {
            index = ME;
            selFrgByPos(index);
        }

        //点击财富，登陆成功
        if (requestCode == REQUEST_LOGIN_CAIFU && resultCode == RESULT_OK) {
            index = CAIFU;
            appTabs.statusChaByPosition(index, currIndex);
            selFrgByPos(index);
        }

        //商城返回
        if (requestCode == REQUEST_LOGIN_SHOP) {
            appTabs.statusChaByPosition(index, currIndex);
        }
    }


    //首页
    @Override
    public void clickZero(View v) {
        selFrgByPos(HOMEPAGE);
    }

    //周边
    @Override
    public void clickOne(View v) {
        selFrgByPos(SLIDE);
    }

    //商城
    @Override
    public void clickTwo(View v) {
        currIndex = SHOP;
        Intent intent = new Intent(this, ShopActivity.class);
        startActivityForResult(intent, REQUEST_LOGIN_SHOP);
    }


    //财富
    @Override
    public void clickThree(View v) {
        currIndex = CAIFU;
        if (AppConfig.getInstance().getInt("uuid", -1000) != -1000) {//登录
            selFrgByPos(CAIFU);
            setRequestCode(REQUEST_LOGIN_CAIFU);
            MyApplication.isLoad = true;
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, REQUEST_LOGIN_CAIFU);
        }
    }

    //我
    @Override
    public void clickFour(View v) {
        currIndex = ME;
        if (AppConfig.getInstance().getInt("uuid", -1000) != -1000) {
            selFrgByPos(ME);
            setRequestCode(REQUEST_LOGIN_ME);
            MyApplication.isLoad = true;
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, REQUEST_LOGIN_ME);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(timer!=null){
            timer.cancel();
        }
    }

    //根据位置切换相应碎片
    public void selFrgByPos(int position) {
        index = currIndex = position;
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        switch (position) {
            case 0:
                if (mHomePageFragment == null) {
                    mHomePageFragment = new HomePageFragment();
                }
                if (!mHomePageFragment.isAdded()) {
                    ft.add(R.id.fl_change, mHomePageFragment, String.valueOf(position));
                    fgts.add(mHomePageFragment);
                }
                for (int i = 0; i < fgts.size(); i++) {
                    Fragment fragment = fgts.get(i);
                    if (fragment instanceof HomePageFragment) {
                        ft.show(fragment);
                    } else {
                        ft.hide(fragment);
                    }
                }
                ft.commit();
                break;
            case 1:
                if (mSideFragment == null) {
                    mSideFragment = new SideFragment();
                }
                if (!mSideFragment.isAdded()) {
                    ft.add(R.id.fl_change, mSideFragment, String.valueOf(position));
                    fgts.add(mSideFragment);
                }
                for (int i = 0; i < fgts.size(); i++) {
                    Fragment fragment = fgts.get(i);
                    if (fragment instanceof SideFragment) {
                        ft.show(fragment);
                    } else {
                        ft.hide(fragment);
                    }

                }
                ft.commit();

                break;
            case 2:
                break;
            case 3:
                if (mMoneyFragment == null) {
                    mMoneyFragment = new MoneyFragment();
                }
                if (!mMoneyFragment.isAdded()) {
                    ft.add(R.id.fl_change, mMoneyFragment, String.valueOf(position));
                    fgts.add(mMoneyFragment);
                }
                for (int i = 0; i < fgts.size(); i++) {
                    Fragment fragment = fgts.get(i);
                    if (fragment instanceof MoneyFragment) {
                        ft.show(fragment);
                    } else {
                        ft.hide(fragment);
                    }
                }
                ft.commit();

                break;
            case 4:
                if (mMeFragment == null) {
                    mMeFragment = new MeFragment();
                }
                if (!mMeFragment.isAdded()) {
                    ft.add(R.id.fl_change, mMeFragment, String.valueOf(position));
                    fgts.add(mMeFragment);
                }
                for (int i = 0; i < fgts.size(); i++) {
                    Fragment fragment = fgts.get(i);
                    if (fragment instanceof MeFragment) {
                        ft.show(fragment);
                    } else {
                        ft.hide(fragment);
                    }
                }
                ft.commit();

                break;
        }

    }

    public AppTabs getAppTabs() {
        return appTabs;
    }

    private void setAlias(String alias) {
        //调用JPush API设置Alias
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));
    }

    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            switch (code) {
                case 0://成功
                    Log.d(TAG, "gotResult: setTagSuccess");
                    AppConfig.getInstance().putBoolean("jpush_set_tag_success",true);
                    break;
                case 6002://失败，重试
                    if (NetUtils.isNetworkAvailable(MainActivity.this)) {
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    } else {
                        Toast.makeText(MainActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
            }
        }
    };
    private final TagAliasCallback mTagsCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            switch (code) {
                case 0:
                    Log.d(TAG, "gotResult: setAliasSuccess");
                    AppConfig.getInstance().putBoolean("jpush_set_alias_success",true);
                    break;
                case 6002:
                    if (NetUtils.isNetworkAvailable(MainActivity.this)) {
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_TAGS, tags), 1000 * 60);
                    } else {
                        Toast.makeText(MainActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                    }
                    break;

                default:
            }
        }

    };

    private void init() {
        int uuid = AppConfig.getInstance().getInt("uuid", -1000);
        if (uuid != -1000 ) {
            if(!AppConfig.getInstance().getBoolean("jpush_set_alias_success",false)){
                setAlias(uuid + "");
            }
            if(!AppConfig.getInstance().getBoolean("jpush_set_tag_success",false)){
                setTag(LogUtils.isAppDebug(this));
            }
        }
    }

    //调用JPush API设置Tag
    private void setTag(boolean appDebug) {
        Set<String> tagSet = new LinkedHashSet<String>();
        if (appDebug) {
            tagSet.add("debug");
        } else {
            tagSet.add("release");
        }
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_TAGS, tagSet));
    }

    //解决fragment崩溃后重叠的问题
    private void resolveProblems(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            List<Fragment> fragments = getSupportFragmentManager().getFragments();
            if (fragments != null && fragments.size() != 0) {
                for (int i = 0; i < fragments.size(); i++) {
                    Fragment fragment = fragments.get(i);
                    if (mHomePageFragment == null && fragment instanceof HomePageFragment)
                        mHomePageFragment = fragment;
                    fgts.add(fragment);
                    if (mSideFragment == null && fragment instanceof SideFragment)
                        mSideFragment = fragment;
                    fgts.add(fragment);
                    if (mMoneyFragment == null && fragment instanceof MoneyFragment)
                        mMoneyFragment = fragment;
                    fgts.add(fragment);
                    if (mMeFragment == null && fragment instanceof MeFragment)
                        mMeFragment = fragment;
                    fgts.add(fragment);
                }
                index = 0;
            }
        }
    }
}
