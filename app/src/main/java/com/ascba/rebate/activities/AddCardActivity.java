package com.ascba.rebate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWorkActivity;
import com.ascba.rebate.activities.base.WebViewBaseActivity;
import com.ascba.rebate.handlers.DialogManager;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.EditTextWithCustomHint;
import com.jaeger.library.StatusBarUtil;
import com.yolanda.nohttp.rest.Request;
import org.json.JSONObject;

public class AddCardActivity extends BaseNetWorkActivity implements BaseNetWorkActivity.Callback {

    private TextView tvName;
    private EditTextWithCustomHint edCardNumber;
    private DialogManager dm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_crad);
        StatusBarUtil.setColor(this, 0xffe52020);
        initViews();
        getDataFromIntent();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if(intent!=null){
            String realname = intent.getStringExtra("realname");
            if(realname!=null && !"".equals(realname)){
                tvName.setText(realname);
            }
        }
    }

    private void initViews() {
        tvName = ((TextView) findViewById(R.id.ed_add_card_name));
        edCardNumber = ((EditTextWithCustomHint) findViewById(R.id.ed_add_card_number));
    }

    public void next(View view) {
        requestBankCard(UrlUtils.getBankCard);
    }

    private void requestBankCard(String url) {
        if(dm==null){
            dm=new DialogManager(this);
        }
        String name = tvName.getText().toString();
        String cardNumber = edCardNumber.getText().toString();
        if(name.equals("")||cardNumber.equals("")){
            dm.buildAlertDialog("请输入持卡人银行卡号码");
            return;
        }
        Request<JSONObject> objRequest = buildNetRequest(url, 0, true);
        objRequest.add("bank_card", cardNumber);
        executeNetWork(objRequest,"请稍后");
        setCallback(this);
    }

    public void goCardProtocol(View view) {
        Intent intent=new Intent(this,WebViewBaseActivity.class);
        intent.putExtra("url",UrlUtils.service);
        intent.putExtra("name","银行卡服务协议");
        startActivity(intent);
    }

    @Override
    public void handle200Data(JSONObject dataObj, String message) {
        JSONObject bankObj = dataObj.optJSONObject("bankCardInfo");
        String realname = bankObj.optString("realname");
        String cardid = bankObj.optString("cardid");
        String bank_card = bankObj.optString("bank_card");
        String bank = bankObj.optString("bank");
        String type = bankObj.optString("type");
        String nature = bankObj.optString("nature");
        String kefu = bankObj.optString("kefu");
        String logo = bankObj.optString("logo");
        String info = bankObj.optString("info");
        Intent intent = new Intent(AddCardActivity.this,BankCardActivity.class);
        intent.putExtra("realname",realname);
        intent.putExtra("cardid",cardid);
        intent.putExtra("bank_card",bank_card);
        intent.putExtra("bank",bank);
        intent.putExtra("type",type);
        intent.putExtra("nature",nature);
        intent.putExtra("kefu",kefu);
        intent.putExtra("logo",logo);
        intent.putExtra("info",info);
        startActivity(intent);
        finish();
    }
}
