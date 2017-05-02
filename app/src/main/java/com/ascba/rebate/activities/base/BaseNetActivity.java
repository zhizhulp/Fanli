package com.ascba.rebate.activities.base;

import android.content.Intent;

import com.ascba.rebate.activities.login.LoginActivity;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.application.MyApplication;

import org.json.JSONObject;

/**
 * Created by 李鹏 on 2017/04/20 0020.
 */

public abstract class BaseNetActivity extends BaseActivityNet {
    public static int REQUEST_LOGIN;
    private Callback callback;
    private CallbackWhat callbackWhat;

    @Override
    protected void requstSuccess(int what, JSONObject jObj) {
        try {
            int status = jObj.optInt("status");
            String message = jObj.optString("msg");
            JSONObject dataObj = jObj.optJSONObject("data");
            if (status == 200) {
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

            } else if (status == 1 || status == 2 || status == 3 || status == 4 || status == 5) {//缺少sign参数
                mhandleReLogin(what);
                Intent intent = new Intent(this, LoginActivity.class);
                AppConfig.getInstance().putInt("uuid", -1000);
                startActivityForResult(intent, REQUEST_LOGIN);
                ((MyApplication) getApplication()).exit();
            } else if (status == 404) {
                if (callback != null) {
                    callback.handle404(message);
                }
                if (callbackWhat != null) {
                    callbackWhat.handle404(what, message);
                }
                mhandle404(what, dataObj, message);
            } else if (status == 500) {
                getDm().buildAlertDialog(message);
            } else if (status == 6) {
                getDm().buildAlertDialog(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        mhandleHasNetWord(isNetWork);
        if (!isNetWork) {
            //没网
            if (callback != null) {
                callback.handleNoNetWork();
            }
            if (callbackWhat != null) {
                callbackWhat.handleNoNetWork();
            }
            mhandleNoNetWord();
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

    protected void mhandleHasNetWord(boolean isNetWork) {
    }
    protected void mhandleFinish(int what) {
    }
    protected void mhandleNoNetWord() {
    }

    public static void setRequestCode(int requestLogin) {
        REQUEST_LOGIN = requestLogin;
    }

}
