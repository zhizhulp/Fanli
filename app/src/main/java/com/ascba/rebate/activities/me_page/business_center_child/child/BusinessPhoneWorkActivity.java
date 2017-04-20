package com.ascba.rebate.activities.me_page.business_center_child.child;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWorkActivity;
import com.ascba.rebate.view.EditTextWithCustomHint;
import com.ascba.rebate.view.MoneyBar;

public class BusinessPhoneWorkActivity extends BaseNetWorkActivity {
    private EditTextWithCustomHint edPhone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_phone);
        //StatusBarUtil.setColor(this, 0xffe52020);
        initViews();
        getDataFromIntent();
    }
    private void getDataFromIntent() {
        Intent intent = getIntent();
        if(intent!=null){
            String seller_tel = intent.getStringExtra("seller_tel");
            if(seller_tel!=null){
                edPhone.setText(seller_tel);
                edPhone.setSelection(seller_tel.length());
            }
        }
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
