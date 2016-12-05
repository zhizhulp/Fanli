package com.ascba.rebate.application;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.URLConnectionNetworkExecutor;
import com.yolanda.nohttp.cache.DBCacheStore;
import com.yolanda.nohttp.cookie.DBCookieStore;
import com.yolanda.nohttp.rest.RequestQueue;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by zhy on 15/8/25.
 */
public class MyApplication extends Application {
    private static Application _instance;
    private static RequestQueue requestQueue;
    public static RequestQueue getHttpQueue(){
        return requestQueue;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        _instance = this;
        NoHttp.initialize(this);
        // 如果你需要自定义配置：
        NoHttp.initialize(this, new NoHttp.Config()
                // 设置全局连接超时时间，单位毫秒，默认10s。
                .setConnectTimeout(30 * 1000)
                // 设置全局服务器响应超时时间，单位毫秒，默认10s。
                .setReadTimeout(30 * 1000)
                // 配置缓存，默认保存数据库DBCacheStore，保存到SD卡使用DiskCacheStore。
                .setCacheStore(
                        new DBCacheStore(this).setEnable(true) // 如果不使用缓存，设置false禁用。
                )
                // 配置Cookie，默认保存数据库DBCookieStore，开发者可以自己实现。
                .setCookieStore(
                        new DBCookieStore(this).setEnable(false) // 如果不维护cookie，设置false禁用。
                )
                // 配置网络层，默认使用URLConnection，如果想用OkHttp：OkHttpNetworkExecutor。
                .setNetworkExecutor(new URLConnectionNetworkExecutor())
        );
        if(requestQueue==null){
            requestQueue=NoHttp.newRequestQueue();
        }
        SDKInitializer.initialize(this);
        ZXingLibrary.initDisplayOpinion(this);
        JPushInterface.init(this);

//        ClearableCookieJar cookieJar1 = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(getApplicationContext()));
//        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
//
////        CookieJarImpl cookieJar1 = new CookieJarImpl(new MemoryCookieStore());
//        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
//                .readTimeout(10000L, TimeUnit.MILLISECONDS)
//                .addInterceptor(new LoggerInterceptor("TAG"))
//                .cookieJar(cookieJar1)
//                .hostnameVerifier(new HostnameVerifier()
//                {
//                    @Override
//                    public boolean verify(String hostname, SSLSession session)
//                    {
//                        return true;
//                    }
//                })
//                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
//                .build();
//        OkHttpUtils.initClient(okHttpClient);
    }
    public static Application getInstance() {
        return _instance;
    }

}