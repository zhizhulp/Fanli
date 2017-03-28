package com.ascba.rebate.activities.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.ascba.rebate.activities.login.LoginActivity;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.application.MyApplication;
import com.ascba.rebate.handlers.DialogManager;
import com.ascba.rebate.handlers.DialogManager2;
import com.ascba.rebate.utils.NetUtils;
import com.ascba.rebate.utils.UrlEncodeUtils;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 网络界面的基类
 */
public class BaseNetWork4Activity extends AppCompatActivity {
    private DialogManager2 dm;
    private Callback callback;

    public DialogManager2 getDm() {
        return dm;
    }

    public void setDm(DialogManager2 dm) {
        this.dm = dm;
    }

    public interface Callback {
        void handle200Data(JSONObject dataObj, String message);

        void handle404(String message);

        void handleNoNetWork();
    }

    public Callback getCallback() {
        return callback;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApplication) getApplication()).addActivity(this);
        if (dm == null) {
            dm = new DialogManager2(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((MyApplication) getApplication()).removeActivity(this);
        cancelNetWork();//取消所有网络请求
    }

    //执行网络请求
    public void executeNetWork(Request<JSONObject> jsonRequest, String message) {

        boolean netAva = NetUtils.isNetworkAvailable(this);
        if (!netAva) {
            dm.buildAlertDialog("请打开网络！");
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

    /**
     * 建立网络请求
     * @param url          请求网址
     * @param method       请求方式 0 post 1 get
     * @param defaultParam 是否有默认请求参数
     * @return 网络请求
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
            JSONObject jObj = response.get();
            int status = jObj.optInt("status");
            String message = jObj.optString("msg");
            if (status == 200) {
                JSONObject dataObj = jObj.optJSONObject("data");
                int update_status = dataObj.optInt("update_status");
                if (update_status == 1) {
                    AppConfig.getInstance().putString("token", dataObj.optString("token"));
                    AppConfig.getInstance().putLong("expiring_time", dataObj.optLong("expiring_time"));
                }
                if (callback != null) {//对于200额外的处理
                    callback.handle200Data(dataObj, message);
                }
            } else if (status == 1 || status == 2 || status == 3 || status == 4 || status == 5) {//缺少sign参数
                Intent intent = new Intent(BaseNetWork4Activity.this, LoginActivity.class);
                AppConfig.getInstance().putInt("uuid", -1000);
                startActivity(intent);
                ((MyApplication) getApplication()).exit();
            } else if (status == 404) {
                if (callback != null) {
                    callback.handle404(message);
                }
            } else if (status == 500) {
                dm.buildAlertDialog(message);
            } else if (status == 6) {
                dm.buildAlertDialog(message);
            }
        }

        @Override
        public void onFailed(int what, Response<JSONObject> response) {
            if (dm != null) {
                dm.dismissDialog();
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
        Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
    }

    protected void showToast(int content) {
        Toast.makeText(this, String.valueOf(content), Toast.LENGTH_SHORT).show();
    }
}

