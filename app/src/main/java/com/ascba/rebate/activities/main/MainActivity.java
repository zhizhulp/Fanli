package com.ascba.rebate.activities.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.ascba.rebate.activities.base.NetworkBaseActivity;
import com.ascba.rebate.beans.City;
import com.ascba.rebate.beans.TabEntity;
import com.ascba.rebate.handlers.CheckThread;
import com.ascba.rebate.handlers.PhoneHandler;
import com.ascba.rebate.utils.ExampleUtil;
import com.ascba.rebate.utils.LogUtils;
import com.ascba.rebate.utils.MySqliteOpenHelper;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.lhh.apst.library.AdvancedPagerSlidingTabStrip;
import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseActivity;
import com.ascba.rebate.fragments.main.FirstFragment;
import com.ascba.rebate.fragments.me.FourthFragment;
import com.ascba.rebate.fragments.message.SecondFragment;
import com.ascba.rebate.fragments.shop.ThirdFragment;
import com.ascba.rebate.utils.ScreenDpiUtils;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


import org.json.JSONArray;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.yolanda.nohttp.rest.Request;

/**
 * 主界面
 */
public class MainActivity extends NetworkBaseActivity {
    private CommonTabLayout mTabLayout_2;
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private String[] mTitles = {"首页", "消息", "商城", "我"};
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    public static boolean isForeground = false;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";
    private static final int MSG_SET_ALIAS = 1001;
    private static final int MSG_SET_TAGS = 1002;
    private FirstFragment mFirstFragment;
    private SecondFragment mSecondFragment;
    private ThirdFragment mThirdFragment;
    private FourthFragment mFourthFragment;
    private SharedPreferences sf;
    private MySqliteOpenHelper db;
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
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        init();
        db=new MySqliteOpenHelper(this);
        //getCityFromServer();

    }

    private void findViews() {
        mTabLayout_2= (CommonTabLayout) findViewById(R.id.tabs);

        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
        if(mFirstFragment==null){
            mFirstFragment=new FirstFragment();
            mFragments.add(mFirstFragment);
        }
        if(mSecondFragment==null){
            mSecondFragment=new SecondFragment();
            mFragments.add(mSecondFragment);
        }
        if(mThirdFragment==null){
            mThirdFragment=new ThirdFragment();
            mFragments.add(mThirdFragment);
        }
        if(mFourthFragment==null){
            mFourthFragment=new FourthFragment();
            mFragments.add(mFourthFragment);
        }
        mTabLayout_2.setTabData(mTabEntities, this, R.id.fl_change, mFragments);
        mTabLayout_2.setCurrentTab(0);
    }

    private void init() {
        sf = getSharedPreferences("first_login_success_name_password", MODE_PRIVATE);
        int uuid = sf.getInt("uuid", -1000);
        if (uuid != -1000) {
            setAlias(uuid + "");
        }
    }

    @Override
    protected void onResume() {
        isForeground = true;
        super.onResume();
    }

    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.closeDB();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void test() {
        long start = System.currentTimeMillis();
        db=new MySqliteOpenHelper(this);
        List<City> cityList = db.getCity();
        for (int i = 0; i < cityList.size(); i++) {
            City city1 = cityList.get(i);
            if(city1.getCityName().equals("北京市")){
                break;
            }
        }
        long end = System.currentTimeMillis();
        LogUtils.PrintLog("789","查询时间为-->"+(end-start));
    }
    private void setAlias(String alias) {
        if (TextUtils.isEmpty(alias)) {
            Toast.makeText(this, R.string.error_alias_empty, Toast.LENGTH_SHORT).show();
            return;
        }
        if (!ExampleUtil.isValidTagAndAlias(alias)) {
            Toast.makeText(this, R.string.error_tag_gs_empty, Toast.LENGTH_SHORT).show();
            return;
        }
        //调用JPush API设置Alias
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));
    }
    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            switch (code) {
                case 0:
                    break;
                case 6002:
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
    private void getCityFromServer() {
        sendMsgToSevr("http://api.qlqwgw.com/v1/getRegion",0);
        CheckThread checkThread = getCheckThread();
        PhoneHandler phoneHandler = checkThread.getPhoneHandler();
        Request<JSONObject> objRequest = checkThread.getObjRequest();
        objRequest.add("type","all");
        phoneHandler.setCallback(phoneHandler.new Callback2(){
            @Override
            public void getMessage(Message msg) {
                super.getMessage(msg);
                JSONObject jObj = (JSONObject) msg.obj;
                int status = jObj.optInt("status");
                if(status==200){
                    JSONObject dataObj = jObj.optJSONObject("data");
                    JSONArray contentArray = dataObj.optJSONArray("content");
                    if(contentArray!=null){
                        for (int i = 0; i < contentArray.length(); i++) {
                            JSONObject jsonObject = contentArray.optJSONObject(i);
                            int id = jsonObject.optInt("id");
                            String name = jsonObject.optString("name");
                            int level = jsonObject.optInt("level");
                            int pid = jsonObject.optInt("pid");
                            String initial = jsonObject.optString("initial");
                            City city=new City(id,name,level,pid,initial);
                            db.insertIntoCity(city);
                        }
                        //test();
                    }
                }
            }
        });
        checkThread.start();
    }

}
