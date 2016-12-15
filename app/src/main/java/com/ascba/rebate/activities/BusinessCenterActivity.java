package com.ascba.rebate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWorkActivity;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.EditTextWithCustomHint;
import com.yolanda.nohttp.rest.Request;

import org.json.JSONObject;

public class BusinessCenterActivity extends BaseNetWorkActivity implements BaseNetWorkActivity.Callback {

    private EditTextWithCustomHint edCompanyName;
    private EditTextWithCustomHint edLawMan;
    private TextView tvAddress;
    private EditTextWithCustomHint edPhone;
    private static final int REQUEST_LOCATION=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_center);
        initViews();
    }

    private void initViews() {
        edCompanyName = ((EditTextWithCustomHint) findViewById(R.id.ed_company_name));
        edLawMan = ((EditTextWithCustomHint) findViewById(R.id.ed_busi_law_man));
        tvAddress = ((TextView) findViewById(R.id.tv_busi_address));
        edPhone = ((EditTextWithCustomHint) findViewById(R.id.ed_busi_phone));
    }

    //商户申请成功之后进入商户详情
    public void go_business_center(View view) {
        final String companyName = edCompanyName.getText().toString();
        final String lawMan = edLawMan.getText().toString();
        final String address = tvAddress.getText().toString();
        final String phone = edPhone.getText().toString();

        Request<JSONObject> objRequest = buildNetRequest(UrlUtils.addCompany, 0, true);
        objRequest.add("name",companyName);
        objRequest.add("corporate",lawMan);
        objRequest.add("address",address);
        objRequest.add("tel",phone);
        objRequest.add("licence","");
        executeNetWork(objRequest,"请稍后");
    }

    public void goSelectLocation(View view) {
        Intent intent=new Intent(this,BusinessLocationActivity.class);
        startActivityForResult(intent,REQUEST_LOCATION);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_LOCATION:
                if(data==null){
                    return;
                }
                String location = data.getStringExtra("location");
                if(location!=null || !location.equals("")){
                    tvAddress.setText(location);
                }
                break;
        }
    }

    @Override
    public void handle200Data(JSONObject dataObj, String message) {
        Toast.makeText(BusinessCenterActivity.this, "资料已经提交", Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(BusinessCenterActivity.this,BusinessCenter2Activity.class);
                        /*intent.putExtra("company_name",companyName);
                        intent.putExtra("law_man",lawMan);
                        intent.putExtra("address",address);
                        intent.putExtra("phone",phone);
                        intent.putExtra("licence","");*/
        startActivity(intent);
        finish();
    }
}
