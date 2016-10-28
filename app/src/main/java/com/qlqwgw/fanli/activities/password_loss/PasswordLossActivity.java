package com.qlqwgw.fanli.activities.password_loss;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.qlqwgw.fanli.R;
import com.qlqwgw.fanli.activities.base.BaseActivity;
import com.qlqwgw.fanli.activities.main.MainActivity;

public class PasswordLossActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_loss);
    }
    //找回密码的下一个界面
    public void goCodePassword(View view) {
        Intent intent=new Intent(this, PasswordLossWithCodeActivity.class);
        startActivity(intent);
    }
}
