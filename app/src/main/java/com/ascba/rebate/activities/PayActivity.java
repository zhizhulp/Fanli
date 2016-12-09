package com.ascba.rebate.activities;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.os.Message;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.NetworkBaseActivity;
import com.ascba.rebate.activities.login.LoginActivity;
import com.ascba.rebate.handlers.CheckThread;
import com.ascba.rebate.handlers.OnPasswordInputFinish;
import com.ascba.rebate.handlers.PhoneHandler;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.EditTextWithCustomHint;
import com.ascba.rebate.view.PayPopWindow;
import com.yolanda.nohttp.rest.Request;

import org.json.JSONObject;


public class PayActivity extends NetworkBaseActivity {


    private int bus_uuid;
    private EditTextWithCustomHint edMoney;
    private PayPopWindow popWindow;
    private TextView tvTpye;
    private SharedPreferences sf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        initViews();
        getIntentFromBefore();

    }

    private void initViews() {
        sf=getSharedPreferences("first_login_success_name_password",MODE_PRIVATE);
        edMoney = ((EditTextWithCustomHint) findViewById(R.id.sweep_money));
        tvTpye = ((TextView) findViewById(R.id.tv_pay_type));
    }

    //获取支付方式，选择支付界面
    public void goPayPassword(View view) {
        initPop();

    }

    private void initPop() {
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        View background = new View(this);
        background.setBackgroundDrawable(new ColorDrawable(Color.WHITE) );
        popWindow = new PayPopWindow(this,background);
        popWindow.showAsDropDown(background);
        popWindow.setOnFinishInput(new OnPasswordInputFinish() {
            @Override
            public void inputFinish() {
                popWindow.onDismiss();
                String money = edMoney.getText().toString();
                sendMsgToSevr(UrlUtils.confirmOrder,0);
                CheckThread checkThread = getCheckThread();
                if(checkThread!=null){
                    final ProgressDialog p=new ProgressDialog(PayActivity.this,R.style.dialog);
                    p.setCanceledOnTouchOutside(false);
                    p.setMessage("请稍后");
                    Request<JSONObject> objRequest = checkThread.getObjRequest();
                    objRequest.add("seller",bus_uuid);
                    objRequest.add("money",money);
                    objRequest.add("region_id",1);
                    objRequest.add("pay_password",popWindow.getStrPassword());
                    objRequest.add("pay_type",2);
                    objRequest.add("scenetype",2);
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
//                            JSONObject dataObj = jObj.optJSONObject("data");
                            }
                        }
                    });
                    checkThread.start();
                    p.show();
                }

            }
        });
    }

    public void getIntentFromBefore() {
        Intent intent = getIntent();
        if(intent!=null){
            bus_uuid = intent.getIntExtra("bus_uuid",-200);
        }
    }
    //选择支付方式
    public void clickSelectPayType(View view) {
        String type = tvTpye.getText().toString();
        if("支付方式：其他".equals(type)){
            tvTpye.setText("支付方式：余额");
        }else if("支付方式：余额".equals(type)) {
            tvTpye.setText("支付方式：其他");
        }
    }
}
