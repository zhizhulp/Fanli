package com.ascba.rebate.fragments.base;

import android.support.v4.app.FragmentActivity;
import com.ascba.rebate.R;
import com.ascba.rebate.activities.main.MainActivity;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.application.MyApplication;
import com.ascba.rebate.handlers.DialogManager2;
import com.ascba.rebate.utils.NetUtils;
import com.ascba.rebate.utils.UrlEncodeUtils;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.CacheMode;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;
import org.json.JSONObject;

/**
 * Created by 李鹏 on 2017/04/20 0020.
 */

public abstract class BaseFragmentNet extends BaseFragment {

    private DialogManager2 dialogManager;
    private NetResponseListener netResponseListener = new NetResponseListener();

    //数据请求成功
    protected abstract void requstSuccess(int what, JSONObject object);

    //请求服务器失败
    protected abstract void requstFailed(int what, Exception e);

    //请求完成
    protected abstract void requstFinish(int what);

    protected abstract void isNetWork(boolean isNetWork);

    /**
     * 建立网络请求
     *
     * @param url          请求网址
     * @param method       请求方式 0 post 1 get
     * @param defaultParam 是否有默认请求参数
     */
    protected Request<JSONObject> buildNetRequest(String url, int method, boolean defaultParam) {
        Request<JSONObject> jsonRequest = NoHttp.createJsonObjectRequest(url, method == 0 ? RequestMethod.POST : RequestMethod.GET);

        if (hasCache()) {
            jsonRequest.setCacheMode(CacheMode.REQUEST_NETWORK_FAILED_READ_CACHE);
        }

        if (defaultParam) {
            int uuid = AppConfig.getInstance().getInt("uuid", -1000);
            String token = AppConfig.getInstance().getString("token", "");
            long expiring_time = AppConfig.getInstance().getLong("expiring_time", -2000);
            jsonRequest.add("sign", UrlEncodeUtils.createSign(url));
            jsonRequest.add("uuid", uuid);
            jsonRequest.add("token", token);
            jsonRequest.add("expiring_time", expiring_time);
        }
        return jsonRequest;
    }

    //执行网络请求
    protected void executeNetWork(int what, Request<JSONObject> jsonRequest, String message) {

        FragmentActivity activity = getActivity();
        if (activity instanceof MainActivity) {
            dialogManager = ((MainActivity) activity).getDm();
        } else {
            if (dialogManager == null) {
                dialogManager = new DialogManager2(getActivity());
            }
        }

        boolean netAva = NetUtils.isNetworkAvailable(getActivity());
        isNetWork(netAva);
        if (!netAva) {
            dialogManager.buildAlertDialog(getResources().getString(R.string.no_network));
            return;
        }
        MyApplication.getRequestQueue().add(what, jsonRequest, netResponseListener);
        dialogManager.buildWaitDialog(message);
    }

    //执行网络请求
    protected void executeNetWork(Request<JSONObject> jsonRequest, String message) {
        FragmentActivity activity = getActivity();
        if (activity instanceof MainActivity) {
            dialogManager = ((MainActivity) activity).getDm();
        } else {
            if (dialogManager == null) {
                dialogManager = new DialogManager2(getActivity());
            }
        }

        boolean netAva = NetUtils.isNetworkAvailable(getActivity());
        isNetWork(netAva);
        if (!netAva) {
            dialogManager.buildAlertDialog(getResources().getString(R.string.no_network));
            return;
        }
        MyApplication.getRequestQueue().add(1, jsonRequest, netResponseListener);
        dialogManager.buildWaitDialog(message);
    }


    protected class NetResponseListener implements OnResponseListener<JSONObject> {
        @Override
        public void onStart(int what) {

        }

        @Override
        public void onSucceed(int what, Response<JSONObject> response) {
            requstSuccess(what, response.get());
        }

        @Override
        public void onFailed(int what, Response<JSONObject> response) {
            requstFailed(what, response.getException());
        }

        @Override
        public void onFinish(int what) {
            requstFinish(what);
        }
    }

    /**
     * 获取DialogManager
     *
     * @return
     */
    public DialogManager2 getDm() {
        return dialogManager;
    }

    /**
     * 取消执行网络请求
     */
    protected void cancelNetWork() {
        MyApplication.getRequestQueue().cancelAll();
    }

    /**
     * 是否启用缓存
     *
     * @return
     */
    protected boolean hasCache() {
        return false;
    }
}