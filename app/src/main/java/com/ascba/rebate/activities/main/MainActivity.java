package com.ascba.rebate.activities.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.ascba.rebate.activities.base.BaseNetWorkActivity;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.beans.TabEntity;
import com.ascba.rebate.handlers.DialogManager2;
import com.ascba.rebate.utils.ExampleUtil;
import com.ascba.rebate.utils.LogUtils;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.ascba.rebate.R;
import com.ascba.rebate.fragments.main.FirstFragment;
import com.ascba.rebate.fragments.me.FourthFragment;
import com.ascba.rebate.fragments.message.SecondFragment;
import com.ascba.rebate.fragments.shop.ThirdFragment;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * 主界面
 */
public class MainActivity extends BaseNetWorkActivity {
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private String[] mTitles = {"首页", "消息", "商城", "我"};
    private static final int MSG_SET_ALIAS = 1001;
    private static final int MSG_SET_TAGS = 1002;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private FirstFragment mFirstFragment;
    private SecondFragment mSecondFragment;
    private ThirdFragment mThirdFragment;
    private FourthFragment mFourthFragment;
    private DialogManager2 dm;
    private int[] mIconUnselectIds = {
            R.mipmap.tab_main, R.mipmap.tab_message,
            R.mipmap.tab_shop, R.mipmap.tab_me};
    private int[] mIconSelectIds = {
            R.mipmap.tab_main_select, R.mipmap.tab_message_select,
            R.mipmap.tab_shop_select, R.mipmap.tab_me_select};
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
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

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
        StatusBarUtil.setColor(this, 0xffe52020);
        findViews();
        checkAllPermission();
    }

    private void checkAllPermission() {

        if (Build.VERSION.SDK_INT >= 23) {
            boolean isAll=true;
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    isAll = false;
                    break;
                }
            }
            if(!isAll){
                ActivityCompat.requestPermissions(this, permissions, 1);
            }

        }

    }
    //申请权限的回调
    @Override
    public void onRequestPermissionsResult ( int requestCode, @NonNull String[] per,
                                             @NonNull int[] grantResults){
        boolean isAll=true;
        for (int i = 0; i < permissions.length; i++) {
            if (per[i].equals(permissions[i])&& grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                isAll=false;
                break;
            }
        }
        if(!isAll){
            Toast.makeText(this, "部分功能可能无法使用，因为你拒绝了权限", Toast.LENGTH_SHORT).show();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void findViews() {
        dm = new DialogManager2(this);
        CommonTabLayout mTabLayout_2 = (CommonTabLayout) findViewById(R.id.tabs);

        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
        if (mFirstFragment == null) {
            mFirstFragment = new FirstFragment();
            mFragments.add(mFirstFragment);
        }
        if (mSecondFragment == null) {
            mSecondFragment = new SecondFragment();
            mFragments.add(mSecondFragment);
        }
        if (mThirdFragment == null) {
            mThirdFragment = new ThirdFragment();
            mFragments.add(mThirdFragment);
        }
        if (mFourthFragment == null) {
            mFourthFragment = new FourthFragment();
            mFragments.add(mFourthFragment);
        }
        mTabLayout_2.setTabData(mTabEntities, this, R.id.fl_change, mFragments);
        mTabLayout_2.setCurrentTab(0);
        init();//设置极光推送用户标识
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

}
