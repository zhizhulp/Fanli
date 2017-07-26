package com.ascba.rebate.activities.base;

import android.content.Intent;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.login.LoginActivity;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.application.MyApplication;
import com.ascba.rebate.utils.JpushSetManager;

import org.json.JSONObject;

import java.util.LinkedHashSet;

/**
 * Created by 李鹏 on 2017/04/20 0020.
 * 网络回调
 */

public abstract class BaseNetActivity extends BaseActivityNet {
    public static int REQUEST_LOGIN;
    private Callback callback;

    private CallbackWhat callbackWhat;

    @Override
    protected void requstSuccess(int what, JSONObject jObj) {

        int status = jObj.optInt("status");
        String message = jObj.optString("msg");
        JSONObject dataObj = jObj.optJSONObject("data");
        if (status == 200) {
            getPageCount(dataObj);
            int update_status = dataObj.optInt("update_status");
            if (update_status == 1) {
                AppConfig.getInstance().putString("token", dataObj.optString("token"));
                AppConfig.getInstance().putLong("expiring_time", dataObj.optLong("expiring_time"));
            }
            if (callback != null) {//对于200额外的处理
                callback.handle200Data(dataObj, message);
            }
            if (callbackWhat != null) {
                callbackWhat.handle200Data(what, dataObj, message);
            }

            mhandle200Data(what, jObj, dataObj, message);

        } else if ( status == 3 || status == 4 || status == 5) {//需要登陆
            //jpush
            AppConfig.getInstance().putBoolean("jpush_set_tag_success",false);
            AppConfig.getInstance().putBoolean("jpush_set_alias_success",false);
            JpushSetManager js=new JpushSetManager(this,1);
            js.setTag(new LinkedHashSet<String>());
            js.setAlias("");

            mhandleReLogin(what);
            Intent intent = new Intent(this, LoginActivity.class);
            MyApplication.isSignOut=true;
            MyApplication.isLoadCartData=true;
            AppConfig.getInstance().putString("token", null);
            AppConfig.getInstance().putLong("expiring_time", -2000);
            AppConfig.getInstance().putInt("uuid", -1000);
            startActivityForResult(intent, REQUEST_LOGIN);
        } else if (status == 1 || status == 2 ) {//缺少sign参数
            showToast(message);
        } else if (status == 404) {
            if (callback != null) {
                callback.handle404(message);
            }
            if (callbackWhat != null) {
                callbackWhat.handle404(what, message);
            }
            mhandle404(what, dataObj, message);
            showToast(message);
        } else if (status == 500) {
            showToast(message);
        } else if (status == 6) {
            showToast(message);
        }

    }

    private void getPageCount(JSONObject dataObj) {
        total_page = dataObj.optInt("total_page");
        now_page++;
    }

    @Override
    protected void requstFailed(int what, Exception e) {
        mhandleFailed(what, e);
    }

    @Override
    protected void requstFinish(int what) {
        mhandleFinish(what);
    }

    @Override
    protected void isNetWork(boolean isNetWork) {
        if (!isNetWork) {
            //没网
            if (callback != null) {
                callback.handleNoNetWork();
            }
            if (callbackWhat != null) {
                callbackWhat.handleNoNetWork();
            }
            mhandleNoNetWord();
            showToast(getString(R.string.no_network));
            //getDm().buildAlertDialog(getString(R.string.no_network));
        }
    }

    protected void setCallback(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {
        void handle200Data(JSONObject dataObj, String message);

        void handle404(String message);

        void handleNoNetWork();
    }

    protected void setCallbackWhat(CallbackWhat callbackWhat) {
        this.callbackWhat = callbackWhat;
    }

    public interface CallbackWhat {
        void handle200Data(int what, JSONObject dataObj, String message);

        void handle404(int what, String message);

        void handleNoNetWork();
    }


    protected void mhandle200Data(int what, JSONObject object, JSONObject dataObj, String message) {
    }

    protected void mhandle404(int what, JSONObject object, String message) {
    }


    protected void mhandleFailed(int what, Exception e) {
    }

    protected void mhandleReLogin(int what) {
    }

    protected void mhandleFinish(int what) {
    }

    protected void mhandleNoNetWord() {
    }

    public static void setRequestCode(int requestLogin) {
        REQUEST_LOGIN = requestLogin;
    }

}
