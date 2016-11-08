package com.ascba.fanli.activities.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ascba.fanli.R;
import com.ascba.fanli.activities.base.BaseActivity;
import com.ascba.fanli.activities.main.MainActivity;
import com.ascba.fanli.activities.password_loss.PasswordLossActivity;
import com.ascba.fanli.activities.register.RegisterInputNumberActivity;
import com.ascba.fanli.utils.SharedPreferencesUtil;

public class LoginActivity extends BaseActivity {
    private EditText edPhone;
    private EditText edPassword;
    private String loginPhone;
    private String loginPassword;
    private boolean isFirstLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        initViews();
    }

    private void initViews() {
        edPhone= (EditText) findViewById(R.id.login_phone_ed);
        edPassword= (EditText) findViewById(R.id.login_password_ed);
    }

    //进入注册页面
    public void goRegister(View view) {
        Intent intent=new Intent(this, RegisterInputNumberActivity.class);
        startActivity(intent);
    }
    //登录成功，进入主页
    public void goMain(View view) {


        /**
         * 判断账号密码是否正确
         * 1.首次登陆
         * 2.以后登陆
         */
        confirmIsUserRight();
        boolean isRight=true;
        if(isRight){
            Intent intent=new Intent(this, MainActivity.class);
            startActivity(intent);
        }else{
            Toast.makeText(this, "账号或密码不正确", Toast.LENGTH_SHORT).show();
        }
    }

    private void confirmIsUserRight() {
        loginPhone=edPhone.getText().toString();
        loginPassword=edPassword.getText().toString();
        //账号或密码为空
        if(loginPhone .equals("") || loginPassword.equals("")){
            Toast.makeText(this, "账号或密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        isFirstLogin=SharedPreferencesUtil.getBoolean(this,"first_login_success",true);
        if(isFirstLogin){
            SharedPreferencesUtil.putBoolean(this,"first_login_success",false);
            SharedPreferences sf = getSharedPreferences("first_login_success_name_password", MODE_PRIVATE);
            sf.edit().putString("login_phone",loginPhone).putString("login_password",loginPassword).apply();
        }else{
            SharedPreferences sp = getSharedPreferences("first_login_success_name_password", MODE_PRIVATE);
            loginPhone=sp.getString("login_phone","");
            loginPassword=sp.getString("login_password","");
        }
    }

    //进入密码找回页面
    public void goForgetPassword(View view) {
        Intent intent=new Intent(this, PasswordLossActivity.class);
        startActivity(intent);
    }
}
