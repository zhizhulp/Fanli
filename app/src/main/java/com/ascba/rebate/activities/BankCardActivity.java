package com.ascba.rebate.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseActivity;
import com.ascba.rebate.activities.base.NetworkBaseActivity;
import com.ascba.rebate.activities.login.LoginActivity;
import com.ascba.rebate.beans.Card;
import com.ascba.rebate.handlers.CheckThread;
import com.ascba.rebate.handlers.PhoneHandler;
import com.ascba.rebate.utils.LogUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.EditTextWithCustomHint;
import com.yolanda.nohttp.rest.Request;

import org.json.JSONObject;

public class BankCardActivity extends NetworkBaseActivity {

    private TextView tvName;
    private TextView tvCard;
    private TextView tvType;
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
    private SharedPreferences sf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_card);
        initViews();
        getIntentFromBefore();
    }

    private void initViews() {
        sf=getSharedPreferences("first_login_success_name_password",MODE_PRIVATE);
        tvName = ((TextView) findViewById(R.id.bank_card_name));
        tvCard = ((TextView) findViewById(R.id.bank_card_number));
        tvType = ((TextView) findViewById(R.id.bank_card_type));
        edPhone = ((EditTextWithCustomHint) findViewById(R.id.ed_bank_card_phone));
        edCode = ((EditTextWithCustomHint) findViewById(R.id.ed_bank_card_code));
    }

    public void bankCardConfirm(View view) {
        String phone = edPhone.getText().toString();
        sendMsgToSevr(UrlUtils.verifyBankCard,0);
        CheckThread checkThread = getCheckThread();
        if(checkThread!=null){
            Request<JSONObject> objRequest = checkThread.getObjRequest();
            objRequest.add("mobile",phone);
            objRequest.add("realname",realname);
            objRequest.add("cardid",cardid);
            objRequest.add("bankcard",bank_card);
            objRequest.add("bank",bank);
            objRequest.add("type",type);
            objRequest.add("nature",nature);
            objRequest.add("kefu",kefu);
            objRequest.add("logo",logo);
            objRequest.add("info",info);
            PhoneHandler phoneHandler = checkThread.getPhoneHandler();
            final ProgressDialog dialog = new ProgressDialog(phoneHandler.getContext(), R.style.dialog);
            dialog.setMessage("请稍后");
            phoneHandler.setCallback(phoneHandler.new Callback2(){
                @Override
                public void getMessage(Message msg) {
                    super.getMessage(msg);
                    dialog.dismiss();
                    JSONObject jObj = (JSONObject) msg.obj;
                    int status = jObj.optInt("status");
                    String message = jObj.optString("msg");
                    if(status==200){
                        Intent intent=new Intent(BankCardActivity.this,CardActivity.class);
                        Card card=new Card(bank,type,bank_card,false);
                        Bundle bundle=new Bundle();
                        bundle.putSerializable("card",card);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                    } else if(status==1||status==2||status==3||status == 4||status==5){//缺少sign参数
                        Intent intent = new Intent(BankCardActivity.this, LoginActivity.class);
                        sf.edit().putInt("uuid", -1000).apply();
                        startActivity(intent);
                        finish();
                    } else if(status==404){
                        Toast.makeText(BankCardActivity.this, message, Toast.LENGTH_SHORT).show();
                    } else if(status==500){
                        Toast.makeText(BankCardActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                }
            });
            checkThread.start();
            dialog.show();
        }

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
            tvType.setText(type);
        }
    }
}
