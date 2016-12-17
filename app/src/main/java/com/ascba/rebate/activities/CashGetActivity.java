package com.ascba.rebate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWorkActivity;
import com.ascba.rebate.handlers.DialogManager;
import com.ascba.rebate.utils.UrlUtils;
import com.yolanda.nohttp.rest.Request;

import org.json.JSONObject;

public class CashGetActivity extends BaseNetWorkActivity implements View.OnClickListener,BaseNetWorkActivity.Callback {

    private View cardView;
    private View noCardView;
    private String realname;
    private EditText edMoney;
    private DialogManager dm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_get);
        initViews();
        getDataFromIntent();
    }

    private void initViews() {
        dm=new DialogManager(this);
        edMoney = ((EditText) findViewById(R.id.money));
        cardView = findViewById(R.id.when_has_card);
        noCardView = findViewById(R.id.when_no_card);
        noCardView.setOnClickListener(this);
    }
    //获取银行卡数，显示不同界面
    private void getDataFromIntent() {
        Intent intent = getIntent();
        if(intent!=null){
            int bank_card_number = intent.getIntExtra("bank_card_number", -1);
            if(bank_card_number!=-1){
                if(bank_card_number==0){
                    cardView.setVisibility(View.GONE);
                    noCardView.setVisibility(View.VISIBLE);
                }else {
                    noCardView.setVisibility(View.GONE);
                    cardView.setVisibility(View.VISIBLE);
                }
            }
            realname = intent.getStringExtra("realname");
        }
    }
    //点击绑定银行卡
    @Override
    public void onClick(View v) {
        Intent intent=new Intent(this,AddCardActivity.class);
        intent.putExtra("realname",realname);
        startActivity(intent);
    }
    //去提现
    public void goGetCash(View view) {
        //判断有没有绑定银行卡
        String s = edMoney.getText().toString();
        if("".equals(s)){
            dm.buildAlertDialog("请输入提现金额");
            return;
        }
        requestHasCard();

    }

    private void requestHasCard() {
        Request<JSONObject> request = buildNetRequest(UrlUtils.checkCardId, 0, true);
        executeNetWork(request,"请稍后");
        setCallback(this);
    }

    @Override
    public void handle200Data(JSONObject dataObj, String message) {
        int isBankCard = dataObj.optInt("isBankCard");
        int isCardId = dataObj.optInt("isCardId");
        if(isBankCard==0){
            if(isCardId==0){
                dm.buildAlertDialog1("您还没有实名认证，是否立即实名？");
                dm.setCallback(new DialogManager.Callback() {
                    @Override
                    public void handleSure() {
                        Intent intent=new Intent(CashGetActivity.this,RealNameCofirmActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }else{
                JSONObject cardObj = dataObj.optJSONObject("cardInfo");
                final String realname = cardObj.optString("realname");
                dm.buildAlertDialog1("暂未绑定银行卡，是否立即绑定？");
                dm.setCallback(new DialogManager.Callback() {
                    @Override
                    public void handleSure() {
                        Intent intent=new Intent(CashGetActivity.this,AddCardActivity.class);
                        intent.putExtra("realname",realname);
                        startActivity(intent);
                        finish();
                    }
                });
            }

        }
    }
}
