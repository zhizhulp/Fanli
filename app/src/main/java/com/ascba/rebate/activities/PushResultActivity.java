package com.ascba.rebate.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseActivity;
import com.ascba.rebate.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

public class PushResultActivity extends BaseActivity {

    private TextView tvPushMsg;
    private TextView tvSellerName;
    private TextView tvSellerAddress;
    private TextView tvNo;
    private TextView tvMoney;
    private TextView tvTime;
    private TextView tvType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_result);
        initViews();
        receiveMsgFromServer();
    }

    private void receiveMsgFromServer() {
        Intent intent = getIntent();
        if (null != intent) {
            Bundle bundle = getIntent().getExtras();
            String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
            String content = bundle.getString(JPushInterface.EXTRA_ALERT);
            String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
            try {
                JSONObject jObj=new JSONObject(extra);
                String order_number = jObj.getString("order_number");
                String seller_name = jObj.getString("seller_name");
                int pay_type = jObj.getInt("pay_type");
                String buy_time = jObj.getString("buy_time");
                int from_type = jObj.getInt("from_type");
                int money = jObj.getInt("money");
                int region_id = jObj.getInt("region_id");
                String pay_password = jObj.getString("pay_password");
                String seller_address = jObj.getString("seller_address");
                int customer = jObj.getInt("customer");
                int seller = jObj.getInt("seller");
                tvSellerName.setText(seller_name);
                tvSellerAddress.setText(seller_address);
                tvNo.setText("订单号码："+order_number);
                tvMoney.setText("付款金额："+money);
                tvTime.setText("购买时间："+buy_time);
                if(pay_type==0){
                    tvType.setText("付款方式：未知方式");
                }else if(pay_type==1){
                    tvType.setText("付款方式：现金");
                }else if(pay_type==2){
                    tvType.setText("付款方式：余额");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void initViews() {
        tvSellerName = ((TextView) findViewById(R.id.tv_seller_name));
        tvSellerAddress = ((TextView) findViewById(R.id.tv_seller_address));
        tvNo = ((TextView) findViewById(R.id.tv_trade_no));
        tvMoney = ((TextView) findViewById(R.id.tv_trade_money));
        tvTime = ((TextView) findViewById(R.id.tv_trade_time));
        tvType = ((TextView) findViewById(R.id.tv_trade_type));
    }
    //商家确认订单
    public void confirmOrder(View view) {

    }
    //商家取消订单
    public void cancelOrder(View view) {

    }
}
