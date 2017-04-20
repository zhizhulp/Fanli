package com.ascba.rebate.activities.me_page.recharge_child;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.me_page.AllAccountWorkActivity;
import com.ascba.rebate.view.MoneyBar;

public class RechaSuccActivity extends AppCompatActivity implements MoneyBar.CallBack {

    private TextView tvMoney;
    private MoneyBar mb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recha_succ);
        //StatusBarUtil.setColor(this,getResources().getColor(R.color.moneyBarColor));
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
        mb.setCallBack(this);
    }

    //进入查看账单页面
    public void goAcc(View view) {
        Intent intent=new Intent(this,AllAccountWorkActivity.class);
        intent.putExtra("order",3);
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
