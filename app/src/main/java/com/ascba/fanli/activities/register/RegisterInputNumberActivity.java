package com.ascba.fanli.activities.register;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ascba.fanli.R;
import com.ascba.fanli.activities.base.BaseActivity;
import com.ascba.fanli.utils.LogUtils;
import com.ascba.fanli.utils.UrlEncodeUtils;

public class RegisterInputNumberActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_input_number);
    }
    //进入注册的下一个界面
    public void goRegisterCode(View view) {
        String sign = UrlEncodeUtils.createSign("http://api.xxx.com/register");

        LogUtils.PrintLog("123",sign+"&name=liping&password=123456");
        Intent intent=new Intent(this, RegisterAfterReceiveCodeActivity.class);
        startActivity(intent);
    }
}
