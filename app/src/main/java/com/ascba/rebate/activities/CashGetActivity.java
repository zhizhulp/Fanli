package com.ascba.rebate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWorkActivity;

public class CashGetActivity extends BaseNetWorkActivity implements View.OnClickListener {

    private View cardView;
    private View noCardView;
    private String realname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_get);
        initViews();
        getDataFromIntent();
    }

    private void initViews() {
        cardView = findViewById(R.id.when_has_card);
        noCardView = findViewById(R.id.when_no_card);
        noCardView.setOnClickListener(this);
    }
    //获取银行卡数，显示不同界面
    private void getDataFromIntent() {
        Intent intent = getIntent();
        if(intent!=null){
            int bank_card_number = intent.getIntExtra("bank_card_number", -1);
            if(bank_card_number!=-1){
                if(bank_card_number==0){
                    cardView.setVisibility(View.GONE);
                    noCardView.setVisibility(View.VISIBLE);
                }else {
                    noCardView.setVisibility(View.GONE);
                    cardView.setVisibility(View.VISIBLE);
                }
            }
            realname = intent.getStringExtra("realname");
        }
    }
    //点击绑定银行卡
    @Override
    public void onClick(View v) {
        Intent intent=new Intent(this,AddCardActivity.class);
        intent.putExtra("realname",realname);
        startActivity(intent);
    }
}
