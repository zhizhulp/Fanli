package com.ascba.rebate.activities.base;

import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.test.mock.MockApplication;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.ascba.rebate.application.MyApplication;
import com.ascba.rebate.handlers.CheckThread;
import com.ascba.rebate.handlers.PhoneHandler;
import com.ascba.rebate.utils.LogUtils;
import com.ascba.rebate.utils.NetUtils;
import com.ascba.rebate.utils.UrlEncodeUtils;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;

import org.json.JSONObject;

public class NetworkBaseActivity extends AppCompatActivity {
    private CheckThread checkThread;
    private SharedPreferences sf;


    public NetworkBaseActivity() {
    }

    public NetworkBaseActivity(CheckThread checkThread) {
        this.checkThread = checkThread;
    }

    public CheckThread getCheckThread() {
        return checkThread;
    }

    public void setCheckThread(CheckThread checkThread) {
        this.checkThread = checkThread;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApplication) getApplication()).addActivity(this);
        sf = getSharedPreferences("first_login_success_name_password", MODE_PRIVATE);
        setStatusBar();
        //setStatusBarColor();
    }
    @TargetApi(19)
    private void setStatusBar() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

    }
    @TargetApi(21)
    private void setStatusBarColor(){
        Window window = getWindow();
        //设置状态栏颜色
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((MyApplication) getApplication()).removeActivity(this);
    }


    public void sendMsgToSevr(String baseUrl, int type) {
        boolean netAva = NetUtils.isNetworkAvailable(this);
        if(!netAva){
            Toast.makeText(this, "请打开网络", Toast.LENGTH_SHORT).show();
            return;
        }
        int uuid = sf.getInt("uuid", -1000);
        String token = sf.getString("token", "");
        long expiring_time = sf.getLong("expiring_time", -2000);
        RequestQueue requestQueue = NoHttp.newRequestQueue();
        Request<JSONObject> objRequest = NoHttp.createJsonObjectRequest(baseUrl + "?", type == 0 ? RequestMethod.POST : RequestMethod.GET);
        objRequest.add("sign", UrlEncodeUtils.createSign(baseUrl));
        objRequest.add("uuid", uuid);
        objRequest.add("token", token);
        objRequest.add("expiring_time", expiring_time);
        PhoneHandler phoneHandler = new PhoneHandler(this);
        checkThread = new CheckThread(requestQueue, phoneHandler, objRequest);
    }
}
