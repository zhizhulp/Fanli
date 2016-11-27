package com.ascba.rebate.handlers;

import com.ascba.rebate.utils.LogUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * Created by Administrator on 2016/11/23.
 */

public class ReceiveThread extends Thread {
    private Socket s;
    @Override
    public void run() {
        InputStream is=null;
        try {
            s=new Socket("test.qlqwgw.com",2346);
            is = s.getInputStream();
            byte [] data=new byte[1024*4];
            if(s!=null){
                int len = is.read(data);
                LogUtils.PrintLog("123","收到消息："+ new String(data,0,len,"utf-8"));
                //LogUtils.PrintLog("123","收到消息："+ read);
            }
        } catch (IOException e) {
            e.printStackTrace();
            LogUtils.PrintLog("123","连接异常："+e.getMessage());
        } finally {
            try {
                s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
