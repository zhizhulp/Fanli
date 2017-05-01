package com.ascba.rebate.activities.me_page.white_score_child;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.me_page.TicketActivity;
import com.ascba.rebate.view.MoneyBar;

public class WSSuccActivity extends AppCompatActivity implements MoneyBar.CallBack2 {
    private TextView tvMoney;
    private MoneyBar mb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wssucc);
        initViews();
        getDataFromIntent();
    }
    private void getDataFromIntent() {
        Intent intent = getIntent();
        if(intent!=null){
            String money = intent.getStringExtra("money");
            if(money!=null){
                tvMoney.setText(money);
            }
        }
    }

    private void initViews() {
        tvMoney = ((TextView) findViewById(R.id.tv_money));
        mb = ((MoneyBar) findViewById(R.id.mb));
        mb.setCallBack2(this);
    }

    //进入代金券列表
    public void goAcc(View view) {
        Intent intent=new Intent(this,TicketActivity.class);
        startActivity(intent);
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
        setResult(RESULT_OK,getIntent());
        finish();
    }
    @Override
    public void clickImage(View im) {

    }
}
