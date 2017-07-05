package com.ascba.rebate.activities.offline_business;

import android.os.Bundle;
import android.view.View;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.view.MoneyBar;

public class ToBeSuredOrdersActivity extends BaseNetActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_be_sured_orders);
        MoneyBar mb=new MoneyBar(this);
        mb.setCallBack2(new MoneyBar.CallBack2() {
            @Override
            public void clickImage(View im) {

            }

            @Override
            public void clickComplete(View tv) {

            }

            @Override
            public void clickBack(View back) {

            }
        });
    }
}
