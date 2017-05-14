package com.ascba.rebate.activities.main_page.sweep;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.EditTextWithCustomHint;
import com.ascba.rebate.view.PayPopWindow;
import com.ascba.rebate.view.RoundImageView;
import com.squareup.picasso.Picasso;
import com.yanzhenjie.nohttp.rest.Request;

import org.json.JSONObject;


public class PayActivity extends BaseNetActivity implements BaseNetActivity.Callback {


    private int bus_uuid;
    private int pay_type=1;//默认为其他支付方式
    private EditTextWithCustomHint edMoney;
    private PayPopWindow popWindow;
    private TextView tvTpye;
    private String avatar;
    private RoundImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        initViews();
        getIntentFromBefore();

    }

    private void initViews() {
        edMoney = ((EditTextWithCustomHint) findViewById(R.id.sweep_money));
        tvTpye = ((TextView) findViewById(R.id.tv_pay_type));
        imageView = ((RoundImageView) findViewById(R.id.imageView));
    }

    //获取支付方式，选择支付界面
    public void goPayPassword(View view) {
        initPop();
    }

    private void initPop() {
        final String money = edMoney.getText().toString();
        if("".equals(money)){
            getDm().buildAlertDialog("请输入支付金额");
            return;
        }
        Request<JSONObject> objRequest = buildNetRequest(UrlUtils.confirmOrder, 0, true);
        objRequest.add("seller",bus_uuid);
        objRequest.add("money",money);
        objRequest.add("region_id",1);
        objRequest.add("pay_password","");
        objRequest.add("pay_type",pay_type);
        objRequest.add("scenetype",2);
        executeNetWork(objRequest,"请稍后");
        setCallback(PayActivity.this);
    }

    public void getIntentFromBefore() {
        Intent intent = getIntent();
        if(intent!=null){
            bus_uuid = intent.getIntExtra("bus_uuid",-200);
            avatar = intent.getStringExtra("avatar");
            if(avatar!=null){
                Picasso.with(this).load(UrlUtils.baseWebsite+avatar).placeholder(R.mipmap.me_user_img).into(imageView);
            }
        }
    }
    //选择支付方式
    public void clickSelectPayType(View view) {
        String type = tvTpye.getText().toString();
        if("支付方式：其他".equals(type)){
            tvTpye.setText("支付方式：余额");
            pay_type=2;
        }else if("支付方式：余额".equals(type)) {
            tvTpye.setText("支付方式：记账");
            pay_type=1;
        }
    }

    @Override
    public void handle200Data(JSONObject dataObj, String message) {
        //校验成功
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void handle404(String message) {
        getDm().buildAlertDialog(message);
    }

    @Override
    public void handleNoNetWork() {

    }
}
