package com.ascba.rebate.activities.me_page.business_center_child.child;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.ascba.rebate.R;
import com.ascba.rebate.view.EditTextWithCustomHint;
import com.ascba.rebate.view.MoneyBar;
import com.jaeger.library.StatusBarUtil;

/**
 * 商家详细地址页面
 */

public class BusLocDetActivity extends AppCompatActivity {
    private EditTextWithCustomHint edLoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_loc_det);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.moneyBarColor));
        initViews();
        getDataFromIntent();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if(intent!=null){
            String seller_localhost = intent.getStringExtra("seller_localhost");
            if(seller_localhost!=null){
                edLoc.setText(seller_localhost);
                edLoc.setSelection(seller_localhost.length());
            }
        }
    }

    private void initViews() {
        edLoc = ((EditTextWithCustomHint) findViewById(R.id.ed_bus_det_loc));
        ((MoneyBar) findViewById(R.id.mb)).setCallBack(new MoneyBar.CallBack() {
            @Override
            public void clickImage(View im) {

            }

            @Override
            public void clickComplete(View tv) {
                String s = edLoc.getText().toString();
                Intent intent=getIntent();
                intent.putExtra("seller_localhost",s);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }
}
