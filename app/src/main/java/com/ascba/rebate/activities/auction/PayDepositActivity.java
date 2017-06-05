package com.ascba.rebate.activities.auction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.utils.UrlUtils;
import com.yanzhenjie.nohttp.rest.Request;

import org.json.JSONObject;

/**
 * Created by 李鹏 on 2017/5/24.
 * 交保证金
 */

public class PayDepositActivity extends BaseNetActivity{
    private String client_ids;
    private double total_price;
    private TextView tvName;
    private TextView tvMobile;
    private TextView tvAddress;
    private TextView tvAddressDet;
    private TextView tvCount;
    private TextView tvPrice;
    private Button btnApply;
    private String pay_bond_data;
    private double pay_bond_price;

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
            total_price = intent.getDoubleExtra("total_price",0);
        }
    }

    private void initView() {
        tvName = ((TextView) findViewById(R.id.tv_name));
        tvMobile = ((TextView) findViewById(R.id.tv_mobile));
        tvAddress = ((TextView) findViewById(R.id.tv_address));
        tvAddressDet = ((TextView) findViewById(R.id.tv_address_details));
        tvCount = ((TextView) findViewById(R.id.tv_count));
        tvPrice= ((TextView) findViewById(R.id.tv_price));

        btnApply = ((Button) findViewById(R.id.btn_apply));
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestNetwork(UrlUtils.systemPayBond,1);
            }
        });
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
            JSONObject obj = dataObj.optJSONObject("address");
            if(obj!=null){
                tvName.setText(obj.optString("consignee"));
                tvMobile.setText(obj.optString("mobile"));
                tvAddress.setText(obj.optString("address"));
                tvAddressDet.setText(obj.optString("address_detail"));
                tvCount.setText("共计"+dataObj.optInt("pay_bond_count")+"件");
                pay_bond_price = dataObj.optDouble("pay_bond_price");
                tvPrice.setText("￥"+pay_bond_price);
                pay_bond_data = dataObj.optString("pay_bond_data");
            }else {
                showToast("您未设置收货地址");
            }
        }else if(what==1){
            showToast(message);
            finish();
        }

    }
}
