package com.ascba.rebate.activities.me_page.business_center_child.child;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWorkActivity;
import com.jaeger.library.StatusBarUtil;

public class EmployeeRateActivity extends BaseNetWorkActivity {

    private RadioGroup rgRate;
    private RadioButton rbRate1;
    private RadioButton rbRate2;
    private RadioButton rbRate3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_rate);
        StatusBarUtil.setColor(this, 0xffe52020);
        initViews();
        getDataFromIntent();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if(intent!=null){
            String seller_return_ratio = intent.getStringExtra("seller_return_ratio");
            if(seller_return_ratio!=null){
                if(seller_return_ratio.equals(rbRate1.getText().toString())){
                    rbRate2.setChecked(true);
                }else if(seller_return_ratio.equals(rbRate2.getText().toString())){
                    rbRate2.setChecked(true);
                }else if(seller_return_ratio.equals(rbRate3.getText().toString())){
                    rbRate2.setChecked(true);
                }
            }
        }
    }

    private void initViews() {
        rgRate = ((RadioGroup) findViewById(R.id.rg_rate));
        rbRate1 = ((RadioButton) findViewById(R.id.rate_8));
        rbRate2 = ((RadioButton) findViewById(R.id.rate_16));
        rbRate3 = ((RadioButton) findViewById(R.id.rate_24));

        rbRate1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                intent.putExtra("business_data_rate", rbRate1.getText());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        rbRate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                intent.putExtra("business_data_rate", rbRate2.getText());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        rbRate3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                intent.putExtra("business_data_rate", rbRate3.getText());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
