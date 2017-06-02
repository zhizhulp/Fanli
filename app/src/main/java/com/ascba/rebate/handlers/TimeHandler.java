package com.ascba.rebate.handlers;

import android.os.Handler;
import android.os.Message;

/**
 * Created by Administrator on 2017/6/2.
 */

public class TimeHandler extends Handler {
    public static final int REDUCE_TIMES = 0;
    public static final int REDUCE_TIME = 1;
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what){
            case REDUCE_TIMES:

                break;
            case REDUCE_TIME:

                break;
        }
    }
}
