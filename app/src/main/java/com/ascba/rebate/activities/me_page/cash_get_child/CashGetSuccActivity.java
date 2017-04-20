package com.ascba.rebate.activities.me_page.cash_get_child;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.me_page.AllAccountActivity;
import com.ascba.rebate.view.MoneyBar;

public class CashGetSuccActivity extends AppCompatActivity implements MoneyBar.CallBack {

    private MoneyBar mb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_get_succ);
        //StatusBarUtil.setColor(this,getResources().getColor(R.color.moneyBarColor));
        initViews();
    }
    private void initViews() {
        mb = ((MoneyBar) findViewById(R.id.mb));
        mb.setCallBack(this);
    }

    public void goAcc(View view) {
        Intent intent=new Intent(this,AllAccountActivity.class);
        intent.putExtra("order",1);
        startActivity(intent);
    }
    @Override
    public void clickImage(View im) {

    }

    @Override
    public void clickComplete(View tv) {
        Intent intent = getIntent();
        setResult(RESULT_OK,intent);
        finish();
    }
}
