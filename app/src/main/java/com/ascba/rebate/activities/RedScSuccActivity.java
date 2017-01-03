package com.ascba.rebate.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.fragments.me.FourthFragment;
import com.ascba.rebate.view.MoneyBar;

public class RedScSuccActivity extends AppCompatActivity implements MoneyBar.CallBack {
    private MoneyBar mb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_red_sc_succ);
        initViews();
    }
    private void initViews() {
        mb = ((MoneyBar) findViewById(R.id.mb));
        mb.setCallBack(this);
    }

    @Override
    public void clickImage(View im) {

    }

    @Override
    public void clickComplete(View tv) {
        setResult(FourthFragment.REQUEST_RED,getIntent());
        finish();
    }
    //进入查看账单页面
    public void goAcc(View view) {
        Intent intent=new Intent(this,AllAccountActivity.class);
        intent.putExtra("order",2);
        startActivity(intent);
    }
}
