package com.ascba.rebate.activities.base;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.login.LoginActivity;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.application.MyApplication;
import com.ascba.rebate.handlers.DialogManager;
import com.ascba.rebate.utils.LogUtils;
import com.ascba.rebate.utils.NetUtils;
import com.ascba.rebate.utils.UrlEncodeUtils;
import com.jaeger.library.StatusBarUtil;
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
public class BaseNetWorkActivity extends AppCompatActivity {
    private DialogManager dm;
    private Callback callback;
    private int count;


    public interface Callback{
        void handle200Data(JSONObject dataObj,String message) throws JSONException;
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
        //setStatusBar();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((MyApplication) getApplication()).removeActivity(this);
        cancelNetWork();//取消所有网络请求
    }

    @TargetApi(19)
    private void setStatusBar() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

    }
    //执行网络请求
    public void executeNetWork(Request<JSONObject> jsonRequest,String message) {
        if(dm==null){
            dm=new DialogManager(this);
        }
        boolean netAva = NetUtils.isNetworkAvailable(this);
        if(!netAva){
            dm.buildAlertDialog("请打开网络！");
            return;
        }
        MyApplication.getRequestQueue().add(1, jsonRequest, new NetResponseListener());
        dm.buildWaitDialog(message).showDialog();
    }

    //取消执行网络请求
    public void cancelNetWork() {
        MyApplication.getRequestQueue().cancelAll();
    }

    /**
     * 建立网络请求
     * @param url 请求网址
     * @param method 请求方式 0 post 1 get
     * @param defaultParam 是否有默认请求参数
     * @return
     */
    public Request<JSONObject> buildNetRequest(String url, int method, boolean defaultParam) {
        Request<JSONObject> jsonRequest = NoHttp.createJsonObjectRequest(url, method == 0 ? RequestMethod.POST : RequestMethod.GET);
        if(defaultParam){
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
            LogUtils.PrintLog("123","onStart"+count);
            count++;

        }

        @Override
        public void onSucceed(int what, Response<JSONObject> response) {
            LogUtils.PrintLog("123","onSucceed"+count);
            count++;
            if(dm!=null){
                dm.dismissDialog();
            }
            JSONObject jObj = response.get();
            int status = jObj.optInt("status");
            String message = jObj.optString("msg");
            if(status==200){
                JSONObject dataObj = jObj.optJSONObject("data");
                int update_status = dataObj.optInt("update_status");
                if (update_status == 1) {
                    AppConfig.getInstance().putString("token", dataObj.optString("token"));
                    AppConfig.getInstance().putLong("expiring_time", dataObj.optLong("expiring_time"));
                }
                if(callback!=null){//对于200额外的处理
                    try {
                        callback.handle200Data(dataObj,message);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else if(status==1||status==2||status==3||status == 4||status==5){//缺少sign参数
                Intent intent = new Intent(BaseNetWorkActivity.this,LoginActivity.class);
                AppConfig.getInstance().putInt("uuid",-1000);
                startActivity(intent);
                ((MyApplication) getApplication()).exit();
            } else if(status==404){
                dm.buildAlertDialog(message);
            } else if(status==500){
                dm.buildAlertDialog(message);
            }
        }

        @Override
        public void onFailed(int what, Response<JSONObject> response) {
            LogUtils.PrintLog("123","onFailed"+count);
            count++;
            if(dm!=null){
                dm.dismissDialog();
            }
            //请求失败的信息
            String message = response.getException().getMessage();
            dm.buildAlertDialog("请求失败");

        }

        @Override
        public void onFinish(int what) {
            LogUtils.PrintLog("123","onFinish"+count);
            count++;
        }
    }
}

