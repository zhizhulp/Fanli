package com.ascba.rebate.activities.me_page.cash_get_child;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.CashGetBillActivity;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.view.MoneyBar;

public class CashGetSuccActivity extends BaseNetActivity implements MoneyBar.CallBack2 {

    private MoneyBar mb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_get_succ);
        initViews();
    }
    private void initViews() {
        mb = ((MoneyBar) findViewById(R.id.mb));
        mb.setCallBack2(this);
    }

    public void goAcc(View view) {
        startActivity(new Intent(this, CashGetBillActivity.class));
    }
    @Override
    public void clickComplete(View tv) {
        finishAndCallback();
    }

    @Override
    public void clickBack(View back) {
        finishAndCallback();
    }

    @Override
    public void onBackPressed() {
        finishAndCallback();
        super.onBackPressed();
    }

    private void finishAndCallback(){
        Intent intent = getIntent();
        setResult(RESULT_OK,intent);
        finish();
    }
    @Override
    public void clickImage(View im) {

    }
}
