package com.ascba.rebate.handlers;
import android.app.Dialog;

import com.ascba.rebate.utils.DialogHome;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Response;
import org.json.JSONObject;

/**
 * Created by 李平 on 2017/5/17 0017.15:21
 */

public class NetResponseListener implements OnResponseListener<JSONObject> {
    private DialogHome dialogManager;
    private Dialog dialog;
    private NetCallback netCallback;

    public void setNetCallback(NetCallback netCallback) {
        this.netCallback = netCallback;
    }
    public interface NetCallback{
        void requestSuccess(int what, JSONObject dataObj);
        void requestFailed(int what, Exception e);
        void requestFinish(int what);
    }
    public NetResponseListener(DialogHome dialogHome){
        this.dialogManager=dialogHome;
    }
    @Override
    public void onStart(int what) {
        dialog = dialogManager.buildWaitDialog("请稍候");
    }

    @Override
    public void onSucceed(int what, Response<JSONObject> response) {
        if(netCallback!=null){
            netCallback.requestSuccess(what, response.get());
        }
    }

    @Override
    public void onFailed(int what, Response<JSONObject> response) {
        dialogManager.buildAlertDialog("请求失败");
        if(netCallback!=null){
            netCallback.requestFailed(what, response.getException());
        }
    }

    @Override
    public void onFinish(int what) {
        dialog.dismiss();
        if(netCallback!=null){
            netCallback.requestFinish(what);
        }
    }
}
