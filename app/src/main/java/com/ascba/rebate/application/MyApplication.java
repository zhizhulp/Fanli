package com.ascba.rebate.application;

import android.app.Activity;
import android.app.Application;

import com.ascba.rebate.activities.login.LoginActivity;
import com.ascba.rebate.utils.IDsUtils;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.URLConnectionNetworkExecutor;
import com.yolanda.nohttp.cache.DBCacheStore;
import com.yolanda.nohttp.cookie.DBCookieStore;
import com.yolanda.nohttp.rest.RequestQueue;

import java.net.CookieStore;
import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;


public class MyApplication extends Application {
    private static RequestQueue requestQueue;
    private static MyApplication app;
    private List<Activity> activities = new ArrayList<>();

    public static MyApplication getInstance() {
        return app;
    }

    public static RequestQueue getRequestQueue() {
        return requestQueue;
    }

    /**
     * 刷新个人中心
     */
    public static boolean isPersonalCenterRefresh = false;


    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        initNohttp();
        requestQueue = NoHttp.newRequestQueue();
        JPushInterface.setDebugMode(false);//release版改为false
        JPushInterface.init(this);//极光推送
        initWXPay();
    }

    private void initWXPay() {
        final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
        // 将该app注册到微信
        msgApi.registerApp(IDsUtils.WX_PAY_APP_ID);
    }

    private void initNohttp() {
        //NoHttp.initialize(this);
        NoHttp.initialize(this, new NoHttp.Config()
                        // 设置全局连接超时时间，单位毫秒，默认10s。
//                .setConnectTimeout(30 * 1000)
                        // 设置全局服务器响应超时时间，单位毫秒，默认10s。
//                .setReadTimeout(30 * 1000)
                        // 配置缓存，默认保存数据库DBCacheStore，保存到SD卡使用DiskCacheStore。
                        .setCacheStore(
                                new DBCacheStore(this).setEnable(false) // 如果不使用缓存，设置false禁用。
                        )
                        // 配置Cookie，默认保存数据库DBCookieStore，开发者可以自己实现。
                        .setCookieStore(
                                new DBCookieStore(this).setEnable(true) // 如果不维护cookie，设置false禁用。
                        )
                        // 配置网络层，默认使用URLConnection，如果想用OkHttp：OkHttpNetworkExecutor。
                        .setNetworkExecutor(new URLConnectionNetworkExecutor())
        );
    }

    // 添加Activity到容器中
    public void addActivity(Activity activity) {
        if (activities.size() > 0) {
            if (!activities.contains(activity)) {
                activities.add(activity);
            }
        } else {
            activities.add(activity);
        }

    }

    // 从容器中移除Activity
    public void removeActivity(Activity activity) {
        if (activities.size() > 0) {
            if (activities.contains(activity)) {
                activities.remove(activity);
            }
        }

    }

    // 遍历所有Activity并finish
    public void exit() {
        if (activities.size() > 0) {
            for (Activity activity : activities) {
                if (activity instanceof LoginActivity) {

                } else {
                    activity.finish();
                }

            }
        }
        //System.exit(0);
    }
}