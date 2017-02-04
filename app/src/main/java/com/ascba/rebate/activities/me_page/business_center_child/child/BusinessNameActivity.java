package com.ascba.rebate.activities.me_page.business_center_child.child;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWorkActivity;
import com.ascba.rebate.view.EditTextWithCustomHint;
import com.ascba.rebate.view.MoneyBar;
import com.jaeger.library.StatusBarUtil;

public class BusinessNameActivity extends BaseNetWorkActivity {

    private EditTextWithCustomHint edName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_name);
        StatusBarUtil.setColor(this, 0xffe52020);
        initViews();
        getDataFromIntent();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if(intent!=null){
            String seller_name = intent.getStringExtra("seller_name");
            if(seller_name!=null){
                edName.setText(seller_name);
                edName.setSelection(seller_name.length());
            }
        }
    }

    private void initViews() {
        edName = ((EditTextWithCustomHint) findViewById(R.id.ed_business_data_name));
        ((MoneyBar) findViewById(R.id.mb_name)).setCallBack(new MoneyBar.CallBack() {
            @Override
            public void clickImage(View im) {

            }

            @Override
            public void clickComplete(View tv) {
                String s = edName.getText().toString();
                Intent intent=getIntent();
                intent.putExtra("business_data_name",s);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }
}
