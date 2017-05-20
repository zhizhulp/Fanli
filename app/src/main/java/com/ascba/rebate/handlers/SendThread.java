package com.ascba.rebate.handlers;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class SendThread extends Thread {

    private Socket s;
    private String msg;

    public SendThread(Socket s,String msg) {
        this.s = s;
        this.msg = msg;
    }

    public void run() {
        OutputStream out ;
        try {
            out = s.getOutputStream();
            out.write(msg.getBytes());
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
