package com.qlqwgw.fanli.activities.register;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.qlqwgw.fanli.R;
import com.qlqwgw.fanli.activities.base.BaseActivity;

public class RegisterInputNumberActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_input_number);
    }
    //收到验证码，进入注册的下一个界面
    public void goRegisterCode(View view) {
        Intent intent=new Intent(this, RegisterAfterReceiveCodeActivity.class);
        startActivity(intent);
    }
}
