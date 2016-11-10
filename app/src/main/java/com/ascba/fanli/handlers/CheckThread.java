package com.ascba.fanli.handlers;

import android.os.Handler;
import android.os.Message;

import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;

import org.json.JSONObject;

/**
 * Created by Administrator on 2016/11/10.
 */

public class CheckThread extends Thread {
    private RequestQueue requestQueue;
    private PhoneHandler phoneHandler;
    private Request<JSONObject> objRequest;
    public CheckThread (RequestQueue requestQueue,PhoneHandler phoneHandler,Request<JSONObject> objRequest) {
        this.requestQueue=requestQueue;
        this.phoneHandler=phoneHandler;
        this.objRequest=objRequest;
    }
    @Override
    public void run() {
        requestQueue.add(1, objRequest, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                Message msg = Message.obtain();
                msg.obj=response.get();
                phoneHandler.sendMessage(msg);
            }

            @Override
            public void onFailed(int what, Response<JSONObject> response) {

            }

            @Override
            public void onFinish(int what) {

            }
        });
    }
}
