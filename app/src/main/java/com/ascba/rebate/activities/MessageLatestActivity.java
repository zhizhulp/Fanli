package com.ascba.rebate.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWork4Activity;

/**
 * Created by 李鹏 on 2017/03/31 0031.
 * 最新公告
 */

public class MessageLatestActivity extends BaseNetWork4Activity {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_latest);
    }

    public static void startIntent(Context context) {
        Intent intent = new Intent(context, MessageDetailsActivity.class);
        context.startActivity(intent);
    }
}
