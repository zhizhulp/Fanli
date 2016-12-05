package com.ascba.rebate.handlers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.ascba.rebate.activities.base.NetworkBaseActivity;
import com.ascba.rebate.activities.login.LoginActivity;
import com.ascba.rebate.utils.LogUtils;

import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;

/**
 * 注册消息处理
 */

public class PhoneHandler extends Handler {
    private Callback callback;
    private Context context;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public PhoneHandler(Context context) {
        this.context = context;
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
            LogUtils.PrintLog("123", jObj.toString());
                int status = jObj.optInt("status");
                JSONObject dataObj = jObj.optJSONObject("data");
                int update_status = dataObj.optInt("update_status");
                if (status == 200) {
                    //Toast.makeText(context, jObj.optString("msg"), Toast.LENGTH_SHORT).show();
                    if (update_status == 1) {
                        context.getSharedPreferences("first_login_success_name_password",MODE_PRIVATE).edit()
                                .putString("token", dataObj.optString("token"))
                                .putLong("expiring_time", dataObj.optLong("expiring_time"))
                                .apply();
                    }
                } else if (status == 5) {
                    //Toast.makeText(context, jObj.optString("msg"), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                    ((Activity) context).finish();
                } else if (status == 3) {
                    Toast.makeText(context, "请重新登录", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, LoginActivity.class);
                    SharedPreferences sf = context.getSharedPreferences("first_login_success_name_password", MODE_PRIVATE);
                    sf.edit().putInt("uuid", -1000).apply();
                    context.startActivity(intent);
                    ((Activity) context).finish();
                } else if (status == 404) {
                    Toast.makeText(context, jObj.optString("msg"), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "未知原因", Toast.LENGTH_SHORT).show();
                }
        }
    }
}
