package com.ascba.rebate.fragments.base;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWork4Activity;
import com.ascba.rebate.activities.login.LoginActivity;
import com.ascba.rebate.activities.main.MainActivity;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.application.MyApplication;
import com.ascba.rebate.handlers.DialogManager2;
import com.ascba.rebate.utils.NetUtils;
import com.ascba.rebate.utils.UrlEncodeUtils;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.error.NetworkError;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;

import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class Base3Fragment extends Fragment {
    private Callback callback;
    private DialogManager2 dm;
    private PermissionCallback pCallback;

    public PermissionCallback getpCallback() {
        return pCallback;
    }

    public void setpCallback(PermissionCallback pCallback) {
        this.pCallback = pCallback;
    }

    public interface PermissionCallback {
        void requestPermissionAndBack(boolean isOk);
    }

    public DialogManager2 getDm() {
        return dm;
    }

    public void setDm(DialogManager2 dm) {
        this.dm = dm;
    }

    public interface Callback {
        void handle200Data(JSONObject dataObj, String message);//数据请求成功

        void handleReqFailed();//请求服务器失败

        void handle404(String message,JSONObject dataObj);

        void handleReLogin();

        void handleNoNetWork();
    }

    public void checkAndRequestAllPermission(String[] permissions) {
        if (permissions == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions(permissions, 1);
        }

    }

    //申请权限的回调
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] per,
                                           @NonNull int[] grantResults) {
        boolean isAll = true;
        for (int i = 0; i < per.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                isAll = false;
                break;
            }
        }
        if (!isAll) {
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.no_permission), Toast.LENGTH_SHORT).show();
        }
        if (pCallback != null) {
            pCallback.requestPermissionAndBack(isAll);//isAll 用户是否拥有所有权限
        }
        super.onRequestPermissionsResult(requestCode, per, grantResults);
    }

    public Callback getCallback() {
        return callback;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public Base3Fragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return null;
    }

    //执行网络请求
    public void executeNetWork(Request<JSONObject> jsonRequest, String message) {
        FragmentActivity activity = getActivity();
        if(activity instanceof MainActivity){
            dm = ((MainActivity) activity).getDm();
        }else {
            if(dm==null){
                dm=new DialogManager2(getActivity());
            }
        }

        boolean netAva = NetUtils.isNetworkAvailable(getActivity());
        if (!netAva) {
            if (callback != null) {
                callback.handleNoNetWork();
            }
            return;
        }
        MyApplication.getRequestQueue().add(1, jsonRequest, new NetResponseListener());
        dm.buildWaitDialog(message);
    }

    //取消执行网络请求
    public void cancelNetWork() {

        MyApplication.getRequestQueue().cancelAll();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        cancelNetWork();
    }

    /**
     * 建立网络请求
     *
     * @param url          请求网址
     * @param method       请求方式 0 post 1 get
     * @param defaultParam 是否有默认请求参数
     */
    public Request<JSONObject> buildNetRequest(String url, int method, boolean defaultParam) {
        Request<JSONObject> jsonRequest = NoHttp.createJsonObjectRequest(url, method == 0 ? RequestMethod.POST : RequestMethod.GET);
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


    private class NetResponseListener implements OnResponseListener<JSONObject> {

        @Override
        public void onStart(int what) {

        }

        @Override
        public void onSucceed(int what, Response<JSONObject> response) {
            if (dm != null) {
                dm.dismissDialog();
            }
            try {
                JSONObject jObj = response.get();
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
                } else if (status == 1 || status == 2 || status == 3 || status == 4 || status == 5) {//缺少sign参数
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    AppConfig.getInstance().putInt("uuid", -1000);
                    startActivityForResult(intent, BaseNetWork4Activity.REQUEST_LOGIN);
                    if (callback != null) {//重新登录的处理
                        callback.handleReLogin();
                    }
                } else if (status == 404) {
                    if (callback != null) {//对于404额外的处理
                        callback.handle404(message,dataObj);
                    }
                } else if (status == 500) {
                    dm.buildAlertDialog(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailed(int what, Response<JSONObject> response) {
            if (dm != null) {
                dm.dismissDialog();
            }
            Exception exc = response.getException();
            if (exc instanceof NetworkError) {

            } else {
                if (callback != null) {
                    callback.handleReqFailed();
                }
                dm.buildAlertDialog("请求失败");
            }
        }

        @Override
        public void onFinish(int what) {
            if (dm != null) {
                dm.dismissDialog();
            }
        }
    }

    protected void showToast(String content) {
        Toast.makeText(getActivity(), content, Toast.LENGTH_SHORT).show();
    }

    protected void showToast(int content) {
        Toast.makeText(getActivity(), String.valueOf(content), Toast.LENGTH_SHORT).show();
    }

}
