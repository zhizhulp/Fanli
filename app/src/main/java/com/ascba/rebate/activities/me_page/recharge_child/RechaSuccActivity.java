package com.ascba.rebate.activities.me_page.recharge_child;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import com.ascba.rebate.R;
import com.ascba.rebate.activities.RechargeBillActivity;
import com.ascba.rebate.view.MoneyBar;

public class RechaSuccActivity extends AppCompatActivity implements MoneyBar.CallBack2 {

    private TextView tvMoney;
    private MoneyBar mb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recha_succ);
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

    //进入查看账单页面
    public void goAcc(View view) {
        Intent intent=new Intent(this,RechargeBillActivity.class);
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
        Intent intent = getIntent();
        setResult(RESULT_OK,intent);
        finish();
    }
    @Override
    public void clickImage(View im) {

    }
}
