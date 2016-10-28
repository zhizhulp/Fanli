package com.qlqwgw.fanli.activities.password_loss;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.qlqwgw.fanli.R;
import com.qlqwgw.fanli.activities.base.BaseActivity;
import com.qlqwgw.fanli.activities.main.MainActivity;

public class PasswordLossWithCodeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_loss_with_code);
    }

    public void goMain3(View view) {
        Intent intent=new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
