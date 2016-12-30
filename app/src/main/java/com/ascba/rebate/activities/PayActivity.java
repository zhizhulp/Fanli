package com.ascba.rebate.activities;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWorkActivity;
import com.ascba.rebate.handlers.DialogManager;
import com.ascba.rebate.handlers.OnPasswordInputFinish;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.EditTextWithCustomHint;
import com.ascba.rebate.view.PayPopWindow;
import com.yolanda.nohttp.rest.Request;
import org.json.JSONObject;


public class PayActivity extends BaseNetWorkActivity implements BaseNetWorkActivity.Callback {


    private int bus_uuid;
    private int pay_type=1;//默认为其他支付方式
    private EditTextWithCustomHint edMoney;
    private PayPopWindow popWindow;
    private TextView tvTpye;
    private DialogManager dm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        initViews();
        getIntentFromBefore();

    }

    private void initViews() {
        dm=new DialogManager(this);
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
                Request<JSONObject> objRequest = buildNetRequest(UrlUtils.confirmOrder, 0, true);
                objRequest.add("seller",bus_uuid);
                objRequest.add("money",money);
                objRequest.add("region_id",1);
                objRequest.add("pay_password",popWindow.getStrPassword());
                objRequest.add("pay_type",pay_type);
                objRequest.add("scenetype",2);
                executeNetWork(objRequest,"请稍后");
                setCallback(PayActivity.this);
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
            pay_type=2;
        }else if("支付方式：余额".equals(type)) {
            tvTpye.setText("支付方式：其他");
            pay_type=1;
        }
    }

    @Override
    public void handle200Data(JSONObject dataObj, String message) {
        //校验成功
        dm.buildAlertDialog(message);

    }
}
