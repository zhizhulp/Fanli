package com.ascba.rebate.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;

import com.ascba.rebate.utils.NetUtils;

public class NetworkReceiver extends BroadcastReceiver {
    private NetStateListener netListener;
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)){
            if(netListener!=null){
                netListener.onNetChange(NetUtils.isNetworkAvailable(context));
                Log.d("network", "onReceive: "+NetUtils.isNetworkAvailable(context));
            }
        }
    }

    public interface NetStateListener{
        void onNetChange(boolean available);
    }

    public void setNetListener(NetStateListener netListener) {
        this.netListener = netListener;
    }
}
