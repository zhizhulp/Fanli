package com.ascba.rebate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWorkActivity;
import com.ascba.rebate.view.EditTextWithCustomHint;
import com.ascba.rebate.view.MoneyBar;

public class BusinessPhoneActivity extends BaseNetWorkActivity {
    private EditTextWithCustomHint edPhone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_phone);
        initViews();
    }

    private void initViews() {
        edPhone = ((EditTextWithCustomHint) findViewById(R.id.ed_business_data_phone));
        ((MoneyBar) findViewById(R.id.mb_phone)).setCallBack(new MoneyBar.CallBack() {
            @Override
            public void clickImage(View im) {

            }

            @Override
            public void clickComplete(View tv) {
                String s = edPhone.getText().toString();
                Intent intent=getIntent();
                intent.putExtra("business_data_phone",s);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }
}
