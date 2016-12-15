package com.ascba.rebate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWorkActivity;

public class BusinessCenter2Activity extends BaseNetWorkActivity {

    private TextView tvCompanyName;
    private TextView tvName;
    private TextView tvAddress;
    private TextView tvPhone;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_center2);
        initViews();
        getIntentFromBefore();
    }

    private void initViews() {
        btn = ((Button) findViewById(R.id.btn));
        tvCompanyName = ((TextView) findViewById(R.id.tv_company_name_loading));
        tvName = ((TextView) findViewById(R.id.tv_law_man_loading));
        tvAddress = ((TextView) findViewById(R.id.tv_address_loading));
        tvPhone = ((TextView) findViewById(R.id.tv_phone_loading));
    }

    public void getIntentFromBefore() {
        Intent intent = getIntent();
        if(intent==null){
            return;
        }
        int type = intent.getIntExtra("type", -1);
        if(type!=-1){
            if(type==0){
                String name = intent.getStringExtra("name_01");
                String corporate = intent.getStringExtra("corporate_01");
                String address = intent.getStringExtra("address_01");
                String tel = intent.getStringExtra("tel_01");
                tvCompanyName.setText(name);
                tvName.setText(corporate);
                tvAddress.setText(address);
                tvPhone.setText(tel);
                btn.setText("审核中");
            }else{
                String name = intent.getStringExtra("name");
                String corporate = intent.getStringExtra("corporate");
                String address = intent.getStringExtra("address");
                String tel = intent.getStringExtra("tel");
                tvCompanyName.setText(name);
                tvName.setText(corporate);
                tvAddress.setText(address);
                tvPhone.setText(tel);
                btn.setText("资料有误");
            }
        }

    }

    public void goBusinessData(View view) {
        Intent intent=new Intent(this,BusinessDataActivity.class);
        startActivity(intent);
        finish();
    }
}
