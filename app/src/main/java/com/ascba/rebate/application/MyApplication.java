package com.ascba.rebate.application;

import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;
import android.view.WindowManager;
import com.ascba.rebate.utils.IDsUtils;
import com.taobao.sophix.PatchStatus;
import com.taobao.sophix.SophixManager;
import com.taobao.sophix.listener.PatchLoadStatusListener;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yanzhenjie.nohttp.Logger;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.OkHttpNetworkExecutor;
import com.yanzhenjie.nohttp.cookie.DBCookieStore;
import com.yanzhenjie.nohttp.rest.RequestQueue;

import cn.jpush.android.api.JPushInterface;


public class MyApplication extends MultiDexApplication {
    private static RequestQueue requestQueue;
    private static MyApplication app;
    //支付类型
    public static int payType = 0;//0 充值 1商城支付
    public static String orderId = null;//订单id

    public static MyApplication getInstance() {
        return app;
    }

    public static RequestQueue getRequestQueue() {
        return requestQueue;
    }

    public static String addressId;//当前收货地址id

    public static boolean isLoad = true;
    public static boolean isSignOut = false;//是否退出登陆
    public static boolean isChangePersonalData;//个人资料是否修改
    public static boolean signOutSignInMoney;//是否点击了设置里的退出 用于退出后登陆重新刷新界面(moneyfrgment判断)
    public static boolean signOutSignInMe;//是否点击了设置里的退出 用于退出后登陆重新刷新界面(mefrgment判断)
    public static boolean signOutSignInCart;//是否点击了设置里的退出 用于退出后登陆重新刷新界面(cartfrgment判断)
    public static boolean signOutSignInShopMe;//是否点击了设置里的退出 用于退出后登陆重新刷新界面(shopmefrgment判断)
    public static boolean isLoadCartData;//是否需要刷新购物车数据
    public static boolean isRefreshOrderCount;//是否刷新订单的数字图标
    public static boolean isRequestSuccess;//网络请求是否成功
    public static boolean isLoadAuctionCart;//是否需要刷新竞拍购物车数据
    public static boolean isKillAppToLoadPatch;//是否需要杀死app去加载补丁
    public final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        initNohttp();
        requestQueue = NoHttp.newRequestQueue();
        JPushInterface.init(this);//极光推送
        initWXPay();
        initHotFix();
    }
    /**
     * hotfix初始化
     */
    private void initHotFix() {
        String appVersion;
        try {
            appVersion = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
        } catch (Exception e) {
            appVersion = "1.0.0";
        }
        SophixManager.getInstance().setContext(this)
                .setAppVersion(appVersion)
                .setAesKey(null)
                //.setAesKey("0123456789123456")
                .setEnableDebug(true)
                .setPatchLoadStatusStub(new PatchLoadStatusListener() {
                    @Override
                    public void onLoad(final int mode, final int code, final String info, final int handlePatchVersion) {
                        String TAG ="hotfix";
                        //详细参数参照官方文档
                        String msg = "Mode:" + mode +
                                " Code:" + code +
                                " Info:" + info +
                                " HandlePatchVersion:" + handlePatchVersion;
                        Log.d(TAG, "onLoad: "+ msg);
                        // 补丁加载回调通知
                        if (code == PatchStatus.CODE_LOAD_SUCCESS) {
                            Log.d(TAG, "onLoad: patch load success");
                            // 表明补丁加载成功
                        } else if (code == PatchStatus.CODE_LOAD_RELAUNCH) {
                            Log.d(TAG, "onLoad: load relaunch");
                            isKillAppToLoadPatch=true;
                            // 表明新补丁生效需要重启. 开发者可提示用户或者强制重启;
                            // 建议: 用户可以监听进入后台事件, 然后应用自杀
                        } else if (code == PatchStatus.CODE_LOAD_FAIL) {
                            // 内部引擎异常, 推荐此时清空本地补丁, 防止失败补丁重复加载
                            Log.d(TAG, "onLoad: load failed");
                            SophixManager.getInstance().cleanPatches();
                        } else {
                            Log.d(TAG, "onLoad: other status");
                        }
                    }
                }).initialize();
        SophixManager.getInstance().queryAndLoadNewPatch();
    }

    /**
     * 微信支付分享初始化
     */
    private void initWXPay() {
        msgApi.registerApp(IDsUtils.WX_PAY_APP_ID);
    }

    /**
     * 网络初始化
     */
    private void initNohttp() {
        NoHttp.initialize(this, new NoHttp.Config()
                        // 设置全局连接超时时间，单位毫秒，默认10s。
//                .setConnectTimeout(30 * 1000)
                        // 设置全局服务器响应超时时间，单位毫秒，默认10s。
//                .setReadTimeout(30 * 1000)
                        // 配置缓存，默认保存数据库DBCacheStore，保存到SD卡使用DiskCacheStore。
                        /*.setCacheStore(
                                new DBCacheStore(this).setEnable(false) // 如果不使用缓存，设置false禁用。
                        )*/
                        // 配置Cookie，默认保存数据库DBCookieStore，开发者可以自己实现。
                        .setCookieStore(
                                new DBCookieStore(this).setEnable(true) // 如果不维护cookie，设置false禁用。
                        )
                        // 配置网络层，默认使用URLConnection，如果想用OkHttp：OkHttpNetworkExecutor。
                        .setNetworkExecutor(new OkHttpNetworkExecutor())
        );
        Logger.setDebug(false); // 开启/关闭NoHttp调试模式。
        Logger.setTag("NoHttpSample"); // 设置NoHttp打印Log的TAG。
    }

    private WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();


    public WindowManager.LayoutParams getMywmParams() {
        return wmParams;
    }
}