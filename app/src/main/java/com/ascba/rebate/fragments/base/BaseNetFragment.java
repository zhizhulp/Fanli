package com.ascba.rebate.fragments.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.activities.login.LoginActivity;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.application.MyApplication;

import org.json.JSONObject;


/**
 * Created by 李鹏 on 2017/04/20 0020.
 */

public abstract class BaseNetFragment extends BaseFragmentNet {

    private Callback callback;
    private CallbackWhat callbackWhat;
    private View view;
    public String TAG;
    private boolean debug=false;

    public BaseNetFragment() {
        TAG=getClass().getSimpleName();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(setContentView(), container, false);

        return view;
    }


    /**
     * 设置Fragment要显示的布局
     * @return 布局的layoutId
     */
    protected int setContentView() {
        return 0;
    }

    /**
     * 获取设置的布局
     */
    protected View getContentView() {
        return view;
    }

    /**
     * 找出对应的控件
     */
    protected <T extends View> T findViewById(int id) {

        return (T) getContentView().findViewById(id);
    }


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
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                MyApplication.isSignOut=true;
                MyApplication.isLoadCartData=true;
                AppConfig.getInstance().putString("token", null);
                AppConfig.getInstance().putLong("expiring_time", -2000);
                AppConfig.getInstance().putInt("uuid", -1000);
                getActivity().startActivityForResult(intent, BaseNetActivity.REQUEST_LOGIN);
                if(callback!=null){
                    callback.handleReLogin();
                }
                if(callbackWhat != null){
                    callbackWhat.handleReLogin();
                }
            } else if (status == 404) {
                if (callback != null) {
                    callback.handle404(message, dataObj);
                }
                if (callbackWhat != null) {
                    callbackWhat.handle404(what, message, dataObj);
                }
                mhandle404(what, message, dataObj);
                showToast(message);
            } else if (status == 500) {
                showToast(message);
                //getDm().buildAlertDialog(message);
            } else if (status == 6) {
                showToast(message);
                //getDm().buildAlertDialog(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void requstFailed(int what, Exception e) {
        mhandleFailed(what, e);
        if (callback != null) {
            callback.handleReqFailed();
        }
        if (callbackWhat != null) {
            callbackWhat.handleReqFailed(what);
        }

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
            showToast(getString(R.string.no_network));
        }
    }

    protected void setCallback(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {
        void handle200Data(JSONObject dataObj, String message);//数据请求成功

        void handleReqFailed();//请求服务器失败

        void handle404(String message, JSONObject dataObj);

        void handleReLogin();

        void handleNoNetWork();
    }

    protected void setCallbackWhat(CallbackWhat callbackWhat) {
        this.callbackWhat = callbackWhat;
    }

    public interface CallbackWhat {
        void handle200Data(int what, JSONObject dataObj, String message);//数据请求成功

        void handleReqFailed(int what);//请求服务器失败

        void handle404(int what, String message, JSONObject dataObj);

        void handleReLogin();

        void handleNoNetWork();
    }

    protected void mhandle200Data(int what, JSONObject object, JSONObject dataObj, String message) {
    }

    protected void mhandle404(int what, String message, JSONObject object) {
    }


    protected void mhandleFinish(int what) {
    }

    protected void mhandleFailed(int what, Exception e) {
    }

}
