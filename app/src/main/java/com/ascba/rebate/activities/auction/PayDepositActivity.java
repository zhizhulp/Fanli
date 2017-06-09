package com.ascba.rebate.activities.auction;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.PayPsdSettingActivity;
import com.ascba.rebate.activities.SelectAddrssUpdateActivity;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.beans.ReceiveAddressBean;
import com.ascba.rebate.utils.DialogHome;
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
    private TextView tvName;
    private TextView tvMobile;
    private TextView tvAddress;
    private TextView tvAddressDet;
    private TextView tvCount;
    private TextView tvPrice;
    private Button btnApply;
    private String pay_bond_data;
    private View viewAddress;
    private View viewHasAddress;
    private View viewNoAddress;
    private AppCompatCheckBox checkbox;
    private double pay_bond_price;//保证金
    private TextView tvTicketInfo;
    private int is_pay_money;

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
        tvName = ((TextView) findViewById(R.id.tv_name));
        tvMobile = ((TextView) findViewById(R.id.tv_mobile));
        tvAddress = ((TextView) findViewById(R.id.tv_address));
        tvAddressDet = ((TextView) findViewById(R.id.tv_address_details));
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
                if(AppConfig.getInstance().getInt("is_level_pwd",0)==0){//没有设置支付密码
                    getDm().buildAlertDialogSure("您还未设置支付密码", "取消", "设置", new DialogHome.Callback() {
                        @Override
                        public void handleSure() {
                            Intent intent=new Intent(PayDepositActivity.this, PayPsdSettingActivity.class);
                            startActivity(intent);
                        }
                    });
                    return;
                }
                requestNetwork(UrlUtils.systemPayBond,1);
            }
        });

        viewAddress = findViewById(R.id.lat_address_details);
        viewAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PayDepositActivity.this, SelectAddrssUpdateActivity.class);
                startActivityForResult(intent,SelectAddrssUpdateActivity.REQUEST_ADDRESS);
            }
        });

        viewHasAddress = findViewById(R.id.lat_has_address);
        viewNoAddress = findViewById(R.id.tv_no_address);

        checkbox = ((AppCompatCheckBox) findViewById(R.id.check_box));
        tvTicketInfo = ((TextView) findViewById(R.id.tv_info_ticket));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case SelectAddrssUpdateActivity.REQUEST_ADDRESS:
                if (resultCode == RESULT_OK && data != null) {
                    ReceiveAddressBean addressBean = data.getParcelableExtra("address");
                    viewHasAddress.setVisibility(View.VISIBLE);
                    viewNoAddress.setVisibility(View.GONE);
                    setBtnStatus(R.drawable.register_btn_bg,true);
                    setReceiveData(addressBean);
                }
                break;
        }
    }

    private void setReceiveData(ReceiveAddressBean addressBean) {
        tvName.setText(addressBean.getName());
        tvMobile.setText(addressBean.getPhone());
        tvAddress.setText(addressBean.getAddress());
        tvAddressDet.setText(addressBean.getAddressDetl());
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
                setBtnStatus(R.drawable.register_btn_bg, true);
                viewHasAddress.setVisibility(View.VISIBLE);
                viewNoAddress.setVisibility(View.GONE);
                tvName.setText(obj.optString("consignee"));
                tvMobile.setText(obj.optString("mobile"));
                tvAddress.setText(obj.optString("address"));
                tvAddressDet.setText(obj.optString("address_detail"));
            }else {
                setBtnStatus(R.drawable.ticket_no_shop_bg, false);
                viewHasAddress.setVisibility(View.GONE);
                viewNoAddress.setVisibility(View.VISIBLE);
            }
            tvCount.setText("共计"+dataObj.optInt("pay_bond_count")+"件");
            pay_bond_price = dataObj.optDouble("pay_bond_price");
            tvPrice.setText("￥"+pay_bond_price);
            pay_bond_data = dataObj.optString("pay_bond_data");
            tvTicketInfo.setText("发票："+dataObj.optString("invoice_tip"));

            is_pay_money = dataObj.optInt("is_pay_money");//0余额不足 1可以
            int is_level_pwd = dataObj.optInt("is_level_pwd");//0没有设置支付密码 1有
            AppConfig.getInstance().putInt("is_level_pwd",is_level_pwd);
        }else if(what==1){//支付保证金成功
            setResult(RESULT_OK,getIntent());
            showToast(message);
            finish();
        }
    }

    //设置button状态
    private void setBtnStatus(int id, boolean enable) {
        btnApply.setBackgroundDrawable(getResources().getDrawable(id));
        btnApply.setEnabled(enable);
    }
}
