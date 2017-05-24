package com.ascba.rebate.handlers;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.ascba.rebate.utils.LogUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class SendThread extends Thread {
    private static final String TAG = "123";
    private Socket s;
    private String msg;

    public SendThread(String msg) {
        this.msg = msg;
    }

    public void run() {
        try {
            s= new Socket("118.190.2.177", 8283);
            LogUtils.PrintLog("123","close-->"+s.isClosed()+",connect-->"+s.isConnected());
            OutputStream out = s.getOutputStream();
            out.write(((String) JSON.toJSONString(msg)).getBytes());
            Log.d(TAG, "发送消息: "+msg);
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "连接异常："+e.getMessage());
        }
    }
    //关闭连接
    public void disConnect() {
        if (s != null && !s.isClosed()) {
            try {
                s.close();
            } catch (IOException e) {
                throw new RuntimeException("关闭异常:" + e.getMessage());
            }
            s = null;
        }
    }
}
