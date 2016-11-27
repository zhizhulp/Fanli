package com.ascba.rebate.activities.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.ascba.rebate.utils.ExampleUtil;
import com.ascba.rebate.utils.LogUtils;
import com.lhh.apst.library.AdvancedPagerSlidingTabStrip;
import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseActivity;
import com.ascba.rebate.fragments.main.FirstFragment;
import com.ascba.rebate.fragments.me.FourthFragment;
import com.ascba.rebate.fragments.message.SecondFragment;
import com.ascba.rebate.fragments.shop.ThirdFragment;
import com.ascba.rebate.utils.ScreenDpiUtils;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.RequestQueue;

import java.util.LinkedHashSet;
import java.util.Set;


import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;

/**
 * 主界面
 */
public class MainActivity extends BaseActivity{

    public AdvancedPagerSlidingTabStrip mAPSTS;
    public APSTSViewPager mVP;
    private static final int VIEW_FIRST = 0;
    private static final int VIEW_SECOND = 1;
    private static final int VIEW_THIRD = 2;
    private static final int VIEW_FOURTH = 3;
    private static final int VIEW_SIZE = 4;

    private FirstFragment mFirstFragment = null;
    private SecondFragment mSecondFragment = null;
    private ThirdFragment mThirdFragment = null;
    private FourthFragment mFourthFragment = null;
    private RequestQueue requestQueue;


    public static boolean isForeground = false;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    private static final int MSG_SET_ALIAS = 1001;
    private static final int MSG_SET_TAGS = 1002;
    SharedPreferences sf;

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

    private final TagAliasCallback mTagsCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs ;
            switch (code) {
                case 0:
//                    logs = "Set tag and alias success";
//                    Log.i(TAG, logs);
                    break;

                case 6002:
//                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
//                    Log.i(TAG, logs);
                    if (ExampleUtil.isConnected(getApplicationContext())) {
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_TAGS, tags), 1000 * 60);
                    } else {
                        //Log.i(TAG, "No network");
                        Toast.makeText(MainActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                    }
                    break;

                default:
                    logs = "Failed with errorCode = " + code;
                    //Log.e(TAG, logs);
            }

            //ExampleUtil.showToast(logs, getApplicationContext());
        }

    };
    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs ;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    //Log.i(TAG, logs);
                    break;

                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    //Log.i(TAG, logs);
                    if (ExampleUtil.isConnected(getApplicationContext())) {
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    } else {
                        //Log.i(TAG, "No network");
                        Toast.makeText(MainActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                    }
                    break;

                default:
                    logs = "Failed with errorCode = " + code;
                    //Log.e(TAG, logs);
            }

            //ExampleUtil.showToast(logs, getApplicationContext());
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        init();
        //confirmFormSever();

    }

    private void findViews(){
        mAPSTS = (AdvancedPagerSlidingTabStrip)findViewById(R.id.tabs);
        mVP = (APSTSViewPager)findViewById(R.id.vp_main);
    }

    private void init(){
        sf=getSharedPreferences("first_login_success_name_password",MODE_PRIVATE);
        int uuid = sf.getInt("uuid", -1000);
        if(uuid!=-1000){
            setAlias(uuid+"");
        }
        setTag(new String[]{"餐饮","商家"});
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());
        mVP.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        mAPSTS.setViewPager(mVP);
        mVP.setNoFocus(false);//设置viewpager禁止滑动
        mVP.setCurrentItem(VIEW_FIRST);//设置viewpager默认页
        //mAPSTS.showDot(VIEW_FIRST,"99+");
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

    private void confirmFormSever() {
        requestQueue = NoHttp.newRequestQueue();

        final String ads="http://api.qlqwgw.com/v1/register?";
        new Thread(new Runnable() {
            @Override
            public void run() {
                // JsonObject
                Request<JSONObject> objRequest = NoHttp.createJsonObjectRequest(ads, RequestMethod.POST);
                objRequest.add("sign","21a7a81ae85237d028bafe3281afe0ca");
                objRequest.add("mobile","123");
                objRequest.add("captcha","李平");
                objRequest.add("password","1");
                objRequest.add("repassword","1");
                objRequest.add("referee","123");
                requestQueue.add(0, objRequest, new OnResponseListener<JSONObject>() {
                    @Override
                    public void onStart(int what) {

                    }

                    @Override
                    public void onSucceed(int what, Response<JSONObject> response) {
                        LogUtils.PrintLog("123MainActivity",response.get().toString());
                    }

                    @Override
                    public void onFailed(int what, Response<JSONObject> response) {

                    }

                    @Override
                    public void onFinish(int what) {

                    }
                });

// JsonArray
//                Request<JSONArray> arrayRequest = NoHttp.createJsonArrayRequest(ads, RequestMethod.PUT);
//                requestQueue.add(0, arrayRequest, listener);
//                OkHttpUtils
//                        .post()
//                        .url("http://api.qlqwgw.com/v1/register?sign=21a7a81ae85237d028bafe3281afe0ca&mobile=123&captcha=123&password=1&repassword=1&referee=123")
////                        .addParams("mobile", "123")
////                        .addParams("captcha", "123")
////                        .addParams("password","1")
////                        .addParams("repassword","1")
////                        .addParams("referee","123")
//                        .build()
//                        .execute(new StringCallback() {
//                            @Override
//                            public void onError(Call call, Exception e, int id) {
//                                LogUtils.PrintLog("123MainActivity",e.toString());
//                            }
//
//                            @Override
//                            public void onResponse(String response, int id) {
//                                LogUtils.PrintLog("123MainActivity",response);
//
//                            }
//                        });
//
            }
        }).start();
    }
    /**
     * 主页viewPager设配器
     */
    public class FragmentAdapter extends FragmentStatePagerAdapter implements AdvancedPagerSlidingTabStrip.IconTabProvider{

        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position >= 0 && position < VIEW_SIZE){
                switch (position){
                    case  VIEW_FIRST:
                        if(null == mFirstFragment)
                            mFirstFragment = FirstFragment.instance();
                        return mFirstFragment;

                    case VIEW_SECOND:
                        if(null == mSecondFragment)
                            mSecondFragment = SecondFragment.instance();
                        return mSecondFragment;

                    case VIEW_THIRD:
                        if(null == mThirdFragment)
                            mThirdFragment = ThirdFragment.instance();
                        return mThirdFragment;

                    case VIEW_FOURTH:
                        if(null == mFourthFragment)
                            mFourthFragment = FourthFragment.instance();
                        return mFourthFragment;
                    default:
                        break;
                }
            }
            return null;
        }

        @Override
        public int getCount() {
            return VIEW_SIZE;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if(position >= 0 && position < VIEW_SIZE){
                switch (position){
                    case  VIEW_FIRST:
                        return  "首页";
                    case  VIEW_SECOND:
                        return  "消息";
                    case  VIEW_THIRD:
                        return  "商城";
                    case  VIEW_FOURTH:
                        return  "我";
                    default:
                        break;
                }
            }
            return null;
        }

        @Override
        public Integer getPageIcon(int index) {
            if(index >= 0 && index < VIEW_SIZE){
                switch (index){
                    case  VIEW_FIRST:
                        return  R.mipmap.tab_main;
                    case VIEW_SECOND:
                        return  R.mipmap.tab_message;
                    case VIEW_THIRD:
                        return  R.mipmap.tab_shop;
                    case VIEW_FOURTH:
                        return  R.mipmap.tab_me;
                    default:
                        break;
                }
            }
            return 0;
        }

        @Override
        public Integer getPageSelectIcon(int index) {
            if(index >= 0 && index < VIEW_SIZE){
                switch (index){
                    case  VIEW_FIRST:
                        return  R.mipmap.tab_main_select;
                    case VIEW_SECOND:
                        return  R.mipmap.tab_message_select;
                    case VIEW_THIRD:
                        return  R.mipmap.tab_shop_select;
                    case VIEW_FOURTH:
                        return  R.mipmap.tab_me_select;
                    default:
                        break;
                }
            }
            return 0;
        }

        @Override
        public Rect getPageIconBounds(int position) {
            int pxw = ScreenDpiUtils.dip2px(MainActivity.this, 24.75f);
            int pxh = ScreenDpiUtils.dip2px(MainActivity.this, 22.5f);
            return new Rect(0,0,pxw,pxh);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void setTag(String[] sArray){
//        EditText tagEdit = (EditText) findViewById(R.id.et_tag);
//        String tag = tagEdit.getText().toString().trim();


        // ","隔开的多个 转换成 Set
//        String[] sArray = tag.split(",");
        Set<String> tagSet = new LinkedHashSet<String>();
        for (String sTagItme : sArray) {
            if (!ExampleUtil.isValidTagAndAlias(sTagItme)) {
                Toast.makeText(this,R.string.error_tag_gs_empty, Toast.LENGTH_SHORT).show();
                return;
            }
            tagSet.add(sTagItme);
        }

        //调用JPush API设置Tag
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_TAGS, tagSet));

    }

    private void setAlias(String alias){
//        EditText aliasEdit = (EditText) findViewById(R.id.et_alias);
//        String alias = aliasEdit.getText().toString().trim();
        if (TextUtils.isEmpty(alias)) {
            Toast.makeText(this,R.string.error_alias_empty, Toast.LENGTH_SHORT).show();
            return;
        }
        if (!ExampleUtil.isValidTagAndAlias(alias)) {
            Toast.makeText(this,R.string.error_tag_gs_empty, Toast.LENGTH_SHORT).show();
            return;
        }

        //调用JPush API设置Alias
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));
    }

}
