package com.ascba.rebate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWorkActivity;
import com.ascba.rebate.beans.Card;
import com.ascba.rebate.handlers.DialogManager;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.EditTextWithCustomHint;
import com.jaeger.library.StatusBarUtil;
import com.yolanda.nohttp.rest.Request;
import org.json.JSONObject;

public class BankCardActivity extends BaseNetWorkActivity implements BaseNetWorkActivity.Callback {

    private TextView tvName;
    private TextView tvCard;
    private TextView tvType;
    private TextView tvTypeType;
    private EditTextWithCustomHint edPhone;
    private EditTextWithCustomHint edCode;
    private String realname;
    private String cardid;
    private String bank_card;
    private String bank;
    private String type;
    private String nature;
    private String kefu;
    private String logo;
    private String info;
    private int finalScene;
    private DialogManager dm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_card);
        StatusBarUtil.setColor(this, 0xffe52020);
        initViews();
        getIntentFromBefore();
    }

    private void initViews() {
        dm=new DialogManager(this);
        tvName = ((TextView) findViewById(R.id.bank_card_name));
        tvCard = ((TextView) findViewById(R.id.bank_card_number));
        tvType = ((TextView) findViewById(R.id.bank_card_type));
        tvTypeType= ((TextView) findViewById(R.id.bank_card_type_type));
        edPhone = ((EditTextWithCustomHint) findViewById(R.id.ed_bank_card_phone));
        edCode = ((EditTextWithCustomHint) findViewById(R.id.ed_bank_card_code));
    }

    public void bankCardConfirm(View view) {
        requestBankCardData(0);//银行卡实名认证
    }

    public void getIntentFromBefore() {
        Intent intent = getIntent();
        if(intent!=null){
            realname = intent.getStringExtra("realname");
            cardid = intent.getStringExtra("cardid");
            bank_card = intent.getStringExtra("bank_card");
            bank = intent.getStringExtra("bank");
            type = intent.getStringExtra("type");
            nature = intent.getStringExtra("nature");
            kefu = intent.getStringExtra("kefu");
            logo = intent.getStringExtra("logo");
            info = intent.getStringExtra("info");
            tvName.setText(realname);
            tvCard.setText(bank_card);
            tvType.setText(bank);
            tvTypeType.setText(type);
        }
    }
    //发送验证码
    public void sendMsg(View view) {
        requestBankCardData(1);//发送验证码
    }


    private void requestBankCardData(int scene){
        String phone = edPhone.getText().toString();
        finalScene=scene;
        if(scene==0){//银行卡实名认证
            Request<JSONObject> request = buildNetRequest(UrlUtils.verifyBankCard, 0, true);
            request.add("mobile",phone);
            request.add("realname",realname);
            request.add("cardid",cardid);
            request.add("bankcard",bank_card);
            request.add("bank",bank);
            request.add("type",type);
            request.add("nature",nature);
            request.add("kefu",kefu);
            request.add("logo",logo);
            request.add("info",info);
            request.add("captcha",edCode.getText().toString());
            executeNetWork(request,"请稍后");
            setCallback(this);
        }else if(scene==1){//发送验证码
            Request<JSONObject> request = buildNetRequest(UrlUtils.sendMsg, 0, true);
            request.add("mobile",edPhone.getText().toString());
            request.add("type",2);
            executeNetWork(request,"请稍后");
            setCallback(this);
        }
    }

    @Override
    public void handle200Data(JSONObject dataObj, String message) {
        if(finalScene==0){
            Intent intent=new Intent(BankCardActivity.this,CardActivity.class);
            startActivity(intent);
            finish();
        }else if(finalScene==1){
            dm.buildAlertDialog(message);
            /*String sms_code = dataObj.optString("sms_code");
            edCode.setText(sms_code);
            edCode.setSelection(sms_code.length());*/
        }
    }
}
