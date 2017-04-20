package com.ascba.rebate.activities.me_page.red_score_child;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.me_page.AllAccountActivity;
import com.ascba.rebate.fragments.me.FourthFragment;
import com.ascba.rebate.view.MoneyBar;

public class RedScSuccActivity extends AppCompatActivity implements MoneyBar.CallBack {
    private MoneyBar mb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_red_sc_succ);
        //StatusBarUtil.setColor(this,getResources().getColor(R.color.moneyBarColor));
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
