package com.ascba.rebate.handlers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.ascba.rebate.activities.login.LoginActivity;

import org.json.JSONObject;
import static android.content.Context.MODE_PRIVATE;

/**
 * 消息处理
 */

public class PhoneHandler extends Handler {
    private Callback callback;
    private Context context;
    private SharedPreferences sf;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public PhoneHandler(Context context) {
        this.context = context;
        sf=context.getSharedPreferences("first_login_success_name_password",MODE_PRIVATE);
    }

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

    public class Callback2 implements Callback{
        public void getMessage(Message msg){
            JSONObject jObj = (JSONObject) msg.obj;
                int status = jObj.optInt("status");
            String message = jObj.optString("msg");
            if (status == 200) {
                JSONObject dataObj = jObj.optJSONObject("data");
                int update_status = dataObj.optInt("update_status");
                if (update_status == 1) {
                    sf.edit()
                            .putString("token", dataObj.optString("token"))
                            .putLong("expiring_time", dataObj.optLong("expiring_time"))
                            .apply();
                }
            } else if(status==1||status==2||status==3||status == 4||status==5){//缺少sign参数
                Intent intent = new Intent(context, LoginActivity.class);
                sf.edit().putInt("uuid", -1000).apply();
                context.startActivity(intent);
                ((Activity) context).finish();
            } else if(status==404){
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            } else if(status==500){
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
