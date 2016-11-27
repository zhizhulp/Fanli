package com.ascba.rebate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseActivity;

public class BusinessDataActivity extends BaseActivity {
    public static final int REQUEST_BUSINESS_NAME=0;
    public static final int REQUEST_BUSINESS_TAG=1;
    public static final int REQUEST_BUSINESS_LOCATION=2;
    public static final int REQUEST_BUSINESS_PHONE=3;
    public static final int REQUEST_BUSINESS_TIME=4;
    public static final int REQUEST_BUSINESS_RATE=5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_data);
    }

    public void goBusinessName(View view) {
        Intent intent=new Intent(this,BusinessNameActivity.class);
        startActivityForResult(intent,REQUEST_BUSINESS_NAME);
    }

    public void goBusinessTag(View view) {
        Intent intent=new Intent(this,BusinessTagActivity.class);
        startActivityForResult(intent,REQUEST_BUSINESS_TAG);
    }

    public void goBusinessLocation(View view) {
        Intent intent=new Intent(this,BusinessLocationActivity.class);
        startActivityForResult(intent,REQUEST_BUSINESS_LOCATION);
    }

    public void goBusinessPhone(View view) {
        Intent intent=new Intent(this,BusinessPhoneActivity.class);
        startActivityForResult(intent,REQUEST_BUSINESS_PHONE);
    }

    public void goBusinessTime(View view) {
        Intent intent=new Intent(this,BusinessTimeActivity.class);
        startActivityForResult(intent,REQUEST_BUSINESS_TIME);
    }

    public void goBusinessRate(View view) {
        Intent intent=new Intent(this,EmployeeRateActivity.class);
        startActivityForResult(intent,REQUEST_BUSINESS_RATE);
    }

    public void goBusinessDetail(View view) {
        Intent intent=new Intent(this,BusinessDetailsActivity.class);
        startActivity(intent);
    }

    public void goBusinessPic(View view) {
        Intent intent=new Intent(this,BusinessPicActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data==null){
            return;
        }
        switch (requestCode){
            case REQUEST_BUSINESS_NAME:
                break;
            case REQUEST_BUSINESS_TAG:
                break;
            case REQUEST_BUSINESS_LOCATION:
                break;
            case REQUEST_BUSINESS_PHONE:
                break;
            case REQUEST_BUSINESS_TIME:
                break;
            case REQUEST_BUSINESS_RATE:
                break;
        }
    }


}
