package com.ascba.rebate.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.NetworkBaseActivity;
import com.ascba.rebate.activities.login.LoginActivity;
import com.ascba.rebate.handlers.CheckThread;
import com.ascba.rebate.handlers.PhoneHandler;
import com.ascba.rebate.utils.LogUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.EditTextWithCustomHint;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;

import org.json.JSONObject;

public class BusinessCenterActivity extends NetworkBaseActivity {

    private EditTextWithCustomHint edCompanyName;
    private EditTextWithCustomHint edLawMan;
    private TextView tvAddress;
    private EditTextWithCustomHint edPhone;
    private static final int REQUEST_LOCATION=0;
    private SharedPreferences sf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_center);
        initViews();
    }

    private void initViews() {
        sf=getSharedPreferences("first_login_success_name_password",MODE_PRIVATE);
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

        sendMsgToSevr(UrlUtils.addCompany,0);
        CheckThread checkThread = getCheckThread();
        if(checkThread!=null){
            final ProgressDialog p=new ProgressDialog(this);
            p.setMessage("请稍后");
            Request<JSONObject> objRequest = checkThread.getObjRequest();
            objRequest.add("name",companyName);
            objRequest.add("corporate",lawMan);
            objRequest.add("address",address);
            objRequest.add("tel",phone);
            objRequest.add("licence","");
            PhoneHandler phoneHandler = checkThread.getPhoneHandler();
            phoneHandler.setCallback(phoneHandler.new Callback2(){
                @Override
                public void getMessage(Message msg) {
                    p.dismiss();
                    super.getMessage(msg);
                    JSONObject jObj = (JSONObject) msg.obj;
                    int status = jObj.optInt("status");
                    String message = jObj.optString("msg");
                    if(status==200){
                        Intent intent=new Intent(BusinessCenterActivity.this,BusinessCenter2Activity.class);
                        intent.putExtra("company_name",companyName);
                        intent.putExtra("law_man",lawMan);
                        intent.putExtra("address",address);
                        intent.putExtra("phone",phone);
                        intent.putExtra("licence","");
                        startActivity(intent);
                        finish();
                    } else if(status==1||status==2||status==3||status == 4||status==5){//缺少sign参数
                        Intent intent = new Intent(BusinessCenterActivity.this, LoginActivity.class);
                        sf.edit().putInt("uuid", -1000).apply();
                        startActivity(intent);
                        finish();
                    } else if(status==404){
                        Toast.makeText(BusinessCenterActivity.this, message, Toast.LENGTH_SHORT).show();
                    } else if(status==500){
                        Toast.makeText(BusinessCenterActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                }
            });
            checkThread.start();
            p.show();
        }


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
}
