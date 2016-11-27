package com.ascba.rebate.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.ascba.rebate.R;

import cn.jpush.android.api.JPushInterface;

public class PushResultActivity extends AppCompatActivity {

    private TextView tvPushMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_result);
        initViews();
        receiveMsgFromServer();
    }

    private void receiveMsgFromServer() {
        Intent intent = getIntent();
        if (null != intent) {
            Bundle bundle = getIntent().getExtras();
            String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
            String content = bundle.getString(JPushInterface.EXTRA_ALERT);
            tvPushMsg.setText("Title : " + title + "  " + "Content : " + content);
        }
    }

    private void initViews() {
        tvPushMsg = ((TextView) findViewById(R.id.receive_push_msg));
    }
}
