package com.ascba.rebate.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseActivity;
import com.ascba.rebate.activities.base.NetworkBaseActivity;
import com.ascba.rebate.handlers.CheckThread;
import com.ascba.rebate.handlers.PhoneHandler;
import com.ascba.rebate.view.cityList.Text;
import com.yolanda.nohttp.rest.Request;

import org.json.JSONObject;

public class OpenProxyActivity extends NetworkBaseActivity {

    private static final int REQUEST_CITY = 1;
    private TextView tvCity;
    int group_id;
    private TextView tvMoney;
    private TextView tvName;
    private TextView tvPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_proxy);
        initViews();
        getData();
        RequestForSevver();
    }

    private void RequestForSevver() {
        sendMsgToSevr("http://api.qlqwgw.com/v1/getUpgraded",0);
        CheckThread checkThread = getCheckThread();
        final ProgressDialog p=new ProgressDialog(this,R.style.dialog);
        p.setMessage("请稍后");
        Request<JSONObject> objRequest = checkThread.getObjRequest();
        objRequest.add("group_id",group_id);
        PhoneHandler phoneHandler = checkThread.getPhoneHandler();
        phoneHandler.setCallback(phoneHandler.new Callback2(){
            @Override
            public void getMessage(Message msg) {
                p.dismiss();
                super.getMessage(msg);
                JSONObject obj = (JSONObject) msg.obj;
                JSONObject dataObj = obj.optJSONObject("data");
                int status = obj.optInt("status");
                if(status==200){
                    JSONObject upObj = dataObj.optJSONObject("getUpgraded");
                    String price = upObj.optString("price");
                    String realname = upObj.optString("realname");
                    String mobile = upObj.optString("mobile");
                    int isReferee = upObj.optInt("isReferee");
                    tvMoney.setText(price);
                    tvName.setText(realname);
                    tvPhone.setText(mobile);
                }
            }
        });
        checkThread.start();
        p.show();
    }

    private void getData() {
        Intent intent = getIntent();
        if(intent!=null){
            group_id = intent.getIntExtra("group_id",-3000);
        }
    }

    private void initViews() {
        tvCity = ((TextView) findViewById(R.id.city));
        tvMoney = ((TextView) findViewById(R.id.proxy_money));
        tvName = ((TextView) findViewById(R.id.proxy_name));
        tvPhone = ((TextView) findViewById(R.id.proxy_phone));
    }

    public void goSelectCity(View view) {
        Intent intent=new Intent(this,BusinessLocationActivity.class);
        startActivityForResult(intent,REQUEST_CITY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_CITY:
                if(data!=null){
                    String location = data.getStringExtra("location");
                    if(location!=null && !"".equals(location)){
                        tvCity.setText(location);
                    }
                }

                break;
        }
    }

    public void goPopPhone(View view) {
        final PopupWindow p=new PopupWindow(this);
        p.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        p.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        p.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        p.setOutsideTouchable(true);
        View popView = getLayoutInflater().inflate(R.layout.pop_service,null);
        final TextView tvPhone = (TextView) popView.findViewById(R.id.service_phone);
        TextView tvCancel = (TextView) popView.findViewById(R.id.tv_cancel);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p.dismiss();
            }
        });
        tvPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = tvPhone.getText().toString();
                if(!phone.equals("")){
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
                    startActivity(intent);
                }
            }
        });
        p.setContentView(popView);
        p.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = getWindow().getAttributes();
                params.alpha = 1.0f;
                getWindow().setAttributes(params);
            }
        });
        p.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM,0,35);
        //设置背景变暗
        WindowManager.LayoutParams params =getWindow().getAttributes();
        params.alpha = 0.5f;
        getWindow().setAttributes(params);

    }
}
