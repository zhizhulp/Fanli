package com.ascba.fanli.handlers;

import android.os.Handler;
import android.os.Message;

/**
 * 注册消息处理
 */

public class PhoneHandler extends Handler {
    private Callback callback;

    public Callback getCallback() {
        return callback;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {
        void getMessage(Message msg);
    }
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if(callback!=null){
            callback.getMessage(msg);
        }

    }

}
