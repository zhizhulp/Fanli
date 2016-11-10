package com.ascba.fanli.activities.main;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.widget.Toast;

import com.ascba.fanli.beans.Constants;
import com.ascba.fanli.beans.HttpListener;
import com.ascba.fanli.utils.LogUtils;
import com.ascba.fanli.utils.UrlEncodeUtils;
import com.lhh.apst.library.AdvancedPagerSlidingTabStrip;
import com.ascba.fanli.R;
import com.ascba.fanli.activities.base.BaseActivity;
import com.ascba.fanli.fragments.main.FirstFragment;
import com.ascba.fanli.fragments.me.FourthFragment;
import com.ascba.fanli.fragments.message.SecondFragment;
import com.ascba.fanli.fragments.shop.ThirdFragment;
import com.ascba.fanli.utils.ScreenDpiUtils;
import com.lhh.apst.library.Margins;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.RequestQueue;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Locale;


import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.Call;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        init();
        confirmFormSever();

    }

    private void findViews(){
        mAPSTS = (AdvancedPagerSlidingTabStrip)findViewById(R.id.tabs);
        mVP = (APSTSViewPager)findViewById(R.id.vp_main);
    }

    private void init(){
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());
        mVP.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        mAPSTS.setViewPager(mVP);
        mVP.setNoFocus(false);//设置viewpager禁止滑动
        mVP.setCurrentItem(VIEW_FOURTH);//设置viewpager默认页
        //mAPSTS.showDot(VIEW_FIRST,"99+");
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
                        LogUtils.PrintLog("123",response.get().toString());
                    }

                    @Override
                    public void onFailed(int what, Response<JSONObject> response) {

                    }

                    @Override
                    public void onFinish(int what) {

                    }
                });
//                Request<JSONObject> objRequest = NoHttp.createJsonObjectRequest(ads, RequestMethod.GET);
//                requestQueue.add(0, objRequest, new OnResponseListener<JSONObject>() {
//                    @Override
//                    public void onStart(int what) {
//
//                    }
//
//                    @Override
//                    public void onSucceed(int what, Response<JSONObject> response) {
//                        LogUtils.PrintLog("123",response.toString());
//                    }
//
//                    @Override
//                    public void onFailed(int what, Response<JSONObject> response) {
//                        LogUtils.PrintLog("123",response.toString());
//                    }
//
//                    @Override
//                    public void onFinish(int what) {
//
//                    }
//                });

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
//                                LogUtils.PrintLog("123",e.toString());
//                            }
//
//                            @Override
//                            public void onResponse(String response, int id) {
//                                LogUtils.PrintLog("123",response);
//
//                            }
//                        });
//                try {
//                    URL url=new URL("http://api.qlqwgw.com/v1/register?sign=21a7a81ae85237d028bafe3281afe0ca&mobile=123&captcha=123&password=1&repassword=1&referee=123");
//                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                    conn.setRequestMethod("POST");
//                    conn.setDoOutput(true);
//                    conn.setConnectTimeout(8000);
//                    conn.setReadTimeout(8000);
//                    conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
//                    conn.setRequestProperty("Content-Encoding", "gzip");
//                    conn.setRequestProperty("Connection", "keep-alive");
//                    conn.connect();
//                    LogUtils.PrintLog("123","response：：："+conn.getResponseCode());
//                    if(conn.getResponseCode()==200){
//                        InputStream is = conn.getInputStream();
//                    }
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                //request(httpUrl,httpArg);
            }
        }).start();
    }
//    String httpUrl = "http://apis.baidu.com/thinkpage/weather_api/suggestion";
//    String httpArg = "location=beijing&language=zh-Hans&unit=c&start=0&days=3";

//    public static String request(String httpUrl, String httpArg) {
//        BufferedReader reader = null;
//        String result = ",,,";
//        StringBuffer sbf = new StringBuffer();
//        httpUrl = httpUrl + "?" + httpArg;
//        String ads="http://api.qlqwgw.com/v1/register?sign=21a7a81ae85237d028bafe3281afe0ca&mobile=123&captcha=123&password=1&repassword=1&referee=123";
//        String ad2="https://www.baidu.com";
//
//
//        URL url = null;
//        HttpURLConnection connection = null;
//        try {
//            url = new URL(ads);
//        } catch (MalformedURLException e) {
//            LogUtils.PrintLog("123","1");
//            e.printStackTrace();
//        }
//
//        try {
//            connection = (HttpURLConnection) url
//                        .openConnection();
//        } catch (IOException e) {
//            LogUtils.PrintLog("123","2");
//            e.printStackTrace();
//        }
//        try {
//            connection.setRequestMethod("GET");
//        } catch (ProtocolException e) {
//            LogUtils.PrintLog("123","3");
//            e.printStackTrace();
//        }
//        //connection.setDoInput(true);
//            //connection.setDoOutput(false);
//
//            // 填入apikey到HTTP header
//            //connection.setRequestProperty("apikey",  "800df6eb77392d2205b55cfccbcc1662");
////            connection.setRequestProperty("Content-Type", "application/json");
////            connection.setRequestProperty("Accept-Charset", "UTF-8");
//        try {
//            connection.connect();
//        } catch (IOException e) {
//            LogUtils.PrintLog("123","4");
//            e.printStackTrace();
//        }
//        try {
//            LogUtils.PrintLog("123","getResponseCode::"+connection.getResponseCode());
//        } catch (IOException e) {
//            LogUtils.PrintLog("123","5");
//            e.printStackTrace();
//        }
//        LogUtils.PrintLog("123","getContentType::"+connection.getContentType());
//        InputStream is = null;
//        try {
//            is = connection.getInputStream();
//        } catch (IOException e) {
//            LogUtils.PrintLog("123",e.toString());
//            e.printStackTrace();
//        }
//        try {
//            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//        } catch (UnsupportedEncodingException e) {
//            LogUtils.PrintLog("123","7");
//            e.printStackTrace();
//        }
//        String strRead = null;
//
//        try {
//            while ((strRead = reader.readLine()) != null) {
//                sbf.append(strRead);
//                sbf.append("\r\n");
//            }
//        } catch (IOException e) {
//            LogUtils.PrintLog("123","8");
//            e.printStackTrace();
//        }
//        try {
//            reader.close();
//        } catch (IOException e) {
//            LogUtils.PrintLog("123","9");
//            e.printStackTrace();
//        }
//        result = sbf.toString();
//
//        LogUtils.PrintLog("123",result);
//        return result;
//    }


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

}
