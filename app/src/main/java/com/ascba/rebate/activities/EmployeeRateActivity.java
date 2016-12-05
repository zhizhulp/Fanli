package com.ascba.rebate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseActivity;

public class EmployeeRateActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    private RadioGroup rgRate;
    private RadioButton rbRate1;
    private RadioButton rbRate2;
    private RadioButton rbRate3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_rate);
        initViews();
    }

    private void initViews() {
        rgRate = ((RadioGroup) findViewById(R.id.rg_rate));
        rbRate1 = ((RadioButton) findViewById(R.id.rate_8));
        rbRate2 = ((RadioButton) findViewById(R.id.rate_16));
        rbRate3 = ((RadioButton) findViewById(R.id.rate_24));
        rgRate.setOnCheckedChangeListener(this);

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        Intent intent = getIntent();
        switch (group.getCheckedRadioButtonId()) {

            case R.id.rate_8:
                intent.putExtra("business_data_rate",rbRate1.getText());
                setResult(RESULT_OK,intent);
                finish();
                break;
            case R.id.rate_16:
                intent.putExtra("business_data_rate",rbRate2.getText());
                setResult(RESULT_OK,intent);
                finish();
                break;
            case R.id.rate_24:
                intent.putExtra("business_data_rate",rbRate3.getText());
                setResult(RESULT_OK,intent);
                finish();
                break;
            default:
                break;
        }
    }
}
