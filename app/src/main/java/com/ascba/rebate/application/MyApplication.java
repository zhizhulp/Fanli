package com.ascba.rebate.application;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.rest.RequestQueue;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by zhy on 15/8/25.
 */
public class MyApplication extends Application {
    private static RequestQueue requestQueue;
    public static RequestQueue getHttpQueue(){
        return requestQueue;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        NoHttp.initialize(this);
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

}