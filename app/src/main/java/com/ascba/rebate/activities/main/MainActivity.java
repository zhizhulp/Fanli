package com.ascba.rebate.activities.main;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWork4Activity;
import com.ascba.rebate.activities.login.LoginActivity;
import com.ascba.rebate.activities.shop.ShopActivity;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.fragments.HomePageFragment;
import com.ascba.rebate.fragments.MeFragment;
import com.ascba.rebate.fragments.MoneyFragment;
import com.ascba.rebate.fragments.SideFragment;
import com.ascba.rebate.handlers.DialogManager2;
import com.ascba.rebate.utils.ExampleUtil;
import com.ascba.rebate.utils.LogUtils;
import com.ascba.rebate.view.AppTabs;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * 主界面
 */
public class MainActivity extends BaseNetWork4Activity implements AppTabs.Callback {
    private static final int MSG_SET_ALIAS = 1001;
    private static final int MSG_SET_TAGS = 1002;
    private static final int REQUEST_LOGIN_CAIFU = 2016;
    private static final int REQUEST_LOGIN_ME = 2017;
    private static final int REQUEST_SHOP = 0;
    private List<Fragment> fgts = new ArrayList<>();
    private DialogManager2 dm;
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
    private String[] permissions = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_CONTACTS
    };

    private Fragment mHomePageFragment;
    private Fragment mSideFragment;
    private Fragment mMoneyFragment;
    private  Fragment mMeFragment;
    private AppTabs appTabs;

    public AppTabs getAppTabs() {
        return appTabs;
    }

    public DialogManager2 getDm() {
        return dm;
    }

    public void setDm(DialogManager2 dm) {
        this.dm = dm;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        findViews();
        checkAndRequestAllPermission(permissions);
    }
    private void findViews() {
        dm = new DialogManager2(this);
        initFragments();
        appTabs = ((AppTabs) findViewById(R.id.tabs));
        appTabs.setCallback(this);
        init();//设置极光推送用户标识
    }

    private void initFragments() {
        mHomePageFragment = new HomePageFragment();
        mSideFragment = new SideFragment();
        mMoneyFragment = new MoneyFragment();
        mMeFragment = new MeFragment();


        fgts.add(mHomePageFragment);
        fgts.add(mSideFragment);

        addAllFrgsToContai();
    }

    private void addAllFrgsToContai() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        ft.add(R.id.fl_change, mHomePageFragment);
        ft.add(R.id.fl_change, mSideFragment);
        ft.commit();
        selFrgByPos(0);
    }

    private void init() {
        int uuid = AppConfig.getInstance().getInt("uuid", -1000);
        if (uuid != -1000) {
            setAlias(uuid + "");
            boolean appDebug = LogUtils.isAppDebug(this);
            setTag(appDebug);
            if (appDebug) {
                LogUtils.PrintLog("123", "debug");
            } else {
                LogUtils.PrintLog("123", "release");
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
       /* tagSet.add(getPackageVersionCode()+"");//把版本号传给服务器*/
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_TAGS, tagSet));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_LOGIN_CAIFU://第一次登录回调财富
                if (resultCode == RESULT_OK) {
                    selFrgByPos(3);
                } else {
                    appTabs.statusChaByPosition(0,3);
                    appTabs.setFilPos(0);
                    selFrgByPos(0);

                }
                break;
            case REQUEST_LOGIN_ME://第一次登录回调个人中心
                if (resultCode == RESULT_OK) {
                    selFrgByPos(4);
                } else {
                    appTabs.statusChaByPosition(0,4);
                    appTabs.setFilPos(0);
                    selFrgByPos(0);
                }
                break;
            case REQUEST_SHOP:
                appTabs.statusChaByPosition(0,2);
                appTabs.setFilPos(0);
                selFrgByPos(0);
                break;
        }
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
                    LogUtils.PrintLog("123", "alias设置成功");
                    break;
                case 6002://失败，重试
                    if (ExampleUtil.isConnected(getApplicationContext())) {
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
                    LogUtils.PrintLog("123", "tag设置成功:");
                    break;

                case 6002:

                    if (ExampleUtil.isConnected(getApplicationContext())) {
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_TAGS, tags), 1000 * 60);
                    } else {
                        Toast.makeText(MainActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                    }
                    break;

                default:
            }
        }

    };

    //首页
    @Override
    public void clickZero(View v) {
        selFrgByPos(0);
    }

    //周边
    @Override
    public void clickOne(View v) {
        selFrgByPos(1);
    }

    //商城
    @Override
    public void clickTwo(View v) {
        Intent intent = new Intent(this, ShopActivity.class);
        startActivityForResult(intent,REQUEST_SHOP);
    }



    //财富
    @Override
    public void clickThree(View v) {
        if (AppConfig.getInstance().getInt("uuid", -1000) != -1000) {//登录
            selFrgByPos(3);
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, REQUEST_LOGIN_CAIFU);
        }
    }

    //我
    @Override
    public void clickFour(View v) {
        if (AppConfig.getInstance().getInt("uuid", -1000) != -1000) {
            selFrgByPos(4);
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, REQUEST_LOGIN_ME);
        }
    }


    //根据位置切换相应碎片
    public void selFrgByPos(int position) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        switch (position) {
            case 0:
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
                if(!fgts.contains(mMoneyFragment)){
                    ft.add(R.id.fl_change,mMoneyFragment);
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
                if(!fgts.contains(mMeFragment)){
                    ft.add(R.id.fl_change,mMeFragment);
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
}
