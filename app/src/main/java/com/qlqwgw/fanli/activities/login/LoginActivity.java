package com.qlqwgw.fanli.activities.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.qlqwgw.fanli.R;
import com.qlqwgw.fanli.activities.base.BaseActivity;
import com.qlqwgw.fanli.activities.main.MainActivity;
import com.qlqwgw.fanli.activities.password_loss.PasswordLossActivity;
import com.qlqwgw.fanli.activities.register.RegisterInputNumberActivity;

public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
    }
    //进入注册页面
    public void goRegister(View view) {
        Intent intent=new Intent(this, RegisterInputNumberActivity.class);
        startActivity(intent);
    }
    //登录成功，进入主页
    public void goMain(View view) {
        Intent intent=new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    //进入密码找回页面
    public void goForgetPassword(View view) {
        Intent intent=new Intent(this, PasswordLossActivity.class);
        startActivity(intent);
    }
}
