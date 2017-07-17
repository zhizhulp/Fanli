package com.ascba.rebate.utils;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.ascba.rebate.appconfig.AppConfig;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by 李平 on 2017/7/17.
 * 一个极光推送的管理类
 */

public class JpushSetManager {
    private static final String TAG="JpushSetManager";
    private static final int MSG_SET_ALIAS = 1001;
    private static final int MSG_SET_TAGS = 1002;
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    JPushInterface.setAliasAndTags(context.getApplicationContext(), (String) msg.obj, null, mAliasCallback);
                    break;
                case MSG_SET_TAGS:
                    JPushInterface.setAliasAndTags(context.getApplicationContext(), null, (Set<String>) msg.obj, mTagsCallback);
                    break;
                default:
                    break;
            }
        }
    };
    private Context context;

    public JpushSetManager(Context context) {
        this.context=context;
    }

    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            switch (code) {
                case 0://成功
                    Log.d(TAG, "gotResult: setTagSuccess");
                    AppConfig.getInstance().putBoolean("jpush_set_tag_success",true);
                    break;
                case 6002://失败，重试
                    if (NetUtils.isNetworkAvailable(context)) {
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    } else {
                        Toast.makeText(context, "网络异常", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
            }
        }
    };
    private final TagAliasCallback mTagsCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            switch (code) {
                case 0:
                    Log.d(TAG, "gotResult: setAliasSuccess");
                    AppConfig.getInstance().putBoolean("jpush_set_alias_success",true);
                    break;
                case 6002:
                    if (NetUtils.isNetworkAvailable(context)) {
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_TAGS, tags), 1000 * 60);
                    } else {
                        Toast.makeText(context, "网络异常", Toast.LENGTH_SHORT).show();
                    }
                    break;

                default:
            }
        }

    };
    //设置标签
    public void setTag(Set<String> tagSet) {
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_TAGS, tagSet));
    }
    //设置别名
    public void setAlias(String alias) {
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));
    }
}
