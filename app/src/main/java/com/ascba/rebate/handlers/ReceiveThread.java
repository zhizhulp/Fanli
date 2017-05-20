package com.ascba.rebate.handlers;

import android.util.Log;

import com.ascba.rebate.utils.LogUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * Created by 李平 on 2016/11/23.14:47
 */

public class ReceiveThread extends Thread {
    private static final String TAG = "LONG_LINK";
    private Socket s;
    @Override
    public void run() {
        InputStream is;
        try {
            s=new Socket("test.qlqwgw.com",2346);
            is = s.getInputStream();
            byte [] data=new byte[1024*4];
            if(s!=null){
                int len = is.read(data);
                Log.d(TAG, "收到消息："+ new String(data,0,len,"utf-8"));
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "连接异常："+e.getMessage());
        } finally {
            try {
                s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
