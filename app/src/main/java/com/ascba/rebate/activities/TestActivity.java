package com.ascba.rebate.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.utils.UrlUtils;
import com.yanzhenjie.nohttp.rest.Request;

import org.json.JSONObject;


public class TestActivity extends BaseNetActivity {
    private Context context;
    private TextView test;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        context = this;
        Request<JSONObject> request = buildNetRequest(UrlUtils.getOrderList, 0, true);
        request.add("status", "all");
        executeNetWork(1, request, "请稍后");
        executeNetWork(request, "请稍后");
        setCallback(new Callback() {
            @Override
            public void handle200Data(JSONObject dataObj, String message) {
                test.setText("Callback:" + message);
                Log.d("TestActivity", "Callback-->" + message);
            }

            @Override
            public void handle404(String message) {

            }

            @Override
            public void handleNoNetWork() {

            }
        });

        setCallbackWhat(new CallbackWhat() {
            @Override
            public void handle200Data(int what, JSONObject dataObj, String message) {
                Log.d("TestActivity", "what-->" + what + "Callback-->" + message);
            }

            @Override
            public void handle404(int what, String message) {

            }

            @Override
            public void handleNoNetWork() {

            }
        });
    }

    @Override
    protected void mhandle200Data(int what, JSONObject object, JSONObject dataObj, String message) {
        Log.d("TestActivity", "200:" + "what-->" + what + "##object-->" + object);
    }

    @Override
    protected void mhandleFailed(int what, Exception e) {
        super.mhandleFailed(what, e);
    }



    @Override
    protected void mhandleFinish(int what) {
        super.mhandleFinish(what);
    }

    @Override
    protected void isNetWork(boolean isNetWork) {
        super.isNetWork(isNetWork);
    }
}