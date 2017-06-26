package com.ascba.rebate.activities.auction;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.activities.base.WebViewBaseActivity;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.utils.UrlUtils;
import com.yanzhenjie.nohttp.rest.Request;

import org.json.JSONObject;

/**
 * Created by 李鹏 on 2017/5/24.
 * 交保证金
 */

public class PayDepositActivity extends BaseNetActivity{
    private String client_ids;
    private String total_price;
    private TextView tvCount;
    private TextView tvPrice;
    private Button btnApply;
    private String pay_bond_data;
    private AppCompatCheckBox checkbox;
    private double pay_bond_price;//保证金
    private TextView tvTicketInfo;
    private int is_pay_money=-1;
    private String auction_url;
    private String deposit_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_deposit);
        getParams();
        initView();
        requestNetwork(UrlUtils.payBond,0);
    }

    private void getParams() {
        Intent intent = getIntent();
        if(intent!=null){
            client_ids = intent.getStringExtra("client_ids");
            total_price = intent.getStringExtra("total_price");
        }
    }

    private void initView() {
        tvCount = ((TextView) findViewById(R.id.tv_count));
        tvPrice= ((TextView) findViewById(R.id.tv_price));
        btnApply = ((Button) findViewById(R.id.btn_apply));
        setBtnStatus(R.drawable.ticket_no_shop_bg, false);
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!checkbox.isChecked()){
                    showToast("请同意竞拍服务协议和保证金规则");
                    return;
                }
                if(is_pay_money==0){//余额不足
                    showToast("余额不足");
                    return;
                }
                /*if(AppConfig.getInstance().getInt("is_level_pwd",0)==0){//没有设置支付密码
                    getDm().buildAlertDialogSure("您还未设置支付密码", "取消", "设置", new DialogHome.Callback() {
                        @Override
                        public void handleSure() {
                            Intent intent=new Intent(PayDepositActivity.this, PayPsdSettingActivity.class);
                            startActivity(intent);
                        }
                    });
                    return;
                }*/
                requestNetwork(UrlUtils.systemPayBond,1);
            }
        });

        checkbox = ((AppCompatCheckBox) findViewById(R.id.check_box));
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    setBtnStatus(R.drawable.register_btn_bg, true);
                }else {
                    setBtnStatus(R.drawable.ticket_no_shop_bg, false);
                }
            }
        });
        tvTicketInfo = ((TextView) findViewById(R.id.tv_info_ticket));
    }

    private void requestNetwork(String url, int what) {
        Request<JSONObject> request = buildNetRequest(url, 0, true);
        if(what==0){
            request.add("client_ids", client_ids);
            request.add("total_price",total_price);
        }else if(what==1){
            request.add("client_str", pay_bond_data);
            request.add("total_price",pay_bond_price);
        }
        executeNetWork(what, request, "请稍后");
    }

    @Override
    protected void mhandle200Data(int what, JSONObject object, JSONObject dataObj, String message) {
        if(what==0){
            tvCount.setText("共计"+dataObj.optInt("pay_bond_count")+"件");
            pay_bond_price = dataObj.optDouble("pay_bond_price");
            tvPrice.setText("￥"+pay_bond_price);
            pay_bond_data = dataObj.optString("pay_bond_data");
            tvTicketInfo.setText("发票："+dataObj.optString("invoice_tip"));

            is_pay_money = dataObj.optInt("is_pay_money");//0余额不足 1可以
            int is_level_pwd = dataObj.optInt("is_level_pwd");//0没有设置支付密码 1有
            AppConfig.getInstance().putInt("is_level_pwd",is_level_pwd);

            auction_url = dataObj.optString("auction_url");
            deposit_url = dataObj.optString("deposit_url");
        }else if(what==1){//支付保证金成功
            setResult(RESULT_OK,getIntent());
            showToast(message);
            finish();
        }
    }

    @Override
    protected void mhandleReLogin(int what) {
        super.mhandleReLogin(what);
        if(what==0){
            finish();
        }
    }

    //设置button状态
    private void setBtnStatus(int id, boolean enable) {
        btnApply.setBackgroundDrawable(getResources().getDrawable(id));
        btnApply.setEnabled(enable);
    }

    public void seeAuctionProxy(View view) {
        Intent intent=new Intent(this, WebViewBaseActivity.class);
        intent.putExtra("name","用户竞拍服务协议");
        intent.putExtra("url",auction_url);
        startActivity(intent);
    }

    public void seeDepositProxy(View view) {
        Intent intent=new Intent(this, WebViewBaseActivity.class);
        intent.putExtra("name","保证金规则");
        intent.putExtra("url",deposit_url);
        startActivity(intent);
    }
}
