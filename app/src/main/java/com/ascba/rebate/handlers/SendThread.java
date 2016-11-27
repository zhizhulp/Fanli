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
        OutputStream out = null;
        try {
            out = s.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            out.write(msg.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
