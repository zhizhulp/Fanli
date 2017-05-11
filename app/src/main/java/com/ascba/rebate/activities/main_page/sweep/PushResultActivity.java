package com.ascba.rebate.activities.main_page.sweep;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.MoneyBar;
import com.ascba.rebate.view.RoundImageView;
import com.squareup.picasso.Picasso;
import com.yanzhenjie.nohttp.rest.Request;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

public class PushResultActivity extends BaseNetActivity implements BaseNetActivity.Callback, MoneyBar.CallBack {

    private TextView tvSellerName;
    private TextView tvSellerAddress;
    private TextView tvNo;
    private TextView tvMoney;
    private TextView tvTime;
    private TextView tvType;

    private String order_number;//订单号
    private int seller;//商家id
    private int customer;//顾客id
    private int pay_type;//支付方式
    private int region_id;//地区id
    private String buy_time;//订单时间
    private String money;//订单金额
    private String pay_password;//订单密码
    private RoundImageView imageView;
    private MoneyBar mb;

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
            String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
            try {
                JSONObject jObj = new JSONObject(extra);
                order_number = jObj.getString("order_number");
                String seller_name = jObj.getString("customer_nickname");
                String customer_avatar = jObj.getString("customer_avatar");
                pay_type = jObj.getInt("pay_type");
                buy_time = jObj.getString("buy_time");
                int from_type = jObj.getInt("from_type");
                money = jObj.getString("money");
                pay_password = jObj.getString("pay_password");
                String seller_address = jObj.getString("seller_address");
                customer = jObj.getInt("customer");
                seller = jObj.getInt("seller");
                if (customer_avatar != null) {
                    Picasso.with(this).load(UrlUtils.baseWebsite + customer_avatar).placeholder(R.mipmap.me_user_img).into(imageView);
                }
                tvSellerName.setText(seller_name);
                tvSellerAddress.setText(seller_address);
                tvNo.setText("订单号码：" + order_number);
                tvMoney.setText("付款金额：" + money);
                tvTime.setText("购买时间：" + buy_time);
                if (pay_type == 0) {
                    tvType.setText("付款方式：未知方式");
                } else if (pay_type == 1) {
                    tvType.setText("付款方式：现金");
                } else if (pay_type == 2) {
                    tvType.setText("付款方式：余额");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void initViews() {
        mb = ((MoneyBar) findViewById(R.id.mb));
        mb.setTailTitle("取消");
        mb.setCallBack(this);
        imageView = ((RoundImageView) findViewById(R.id.usericon));
        tvSellerName = ((TextView) findViewById(R.id.tv_seller_name));
        tvSellerAddress = ((TextView) findViewById(R.id.tv_seller_address));
        tvNo = ((TextView) findViewById(R.id.tv_trade_no));
        tvMoney = ((TextView) findViewById(R.id.tv_trade_money));
        tvTime = ((TextView) findViewById(R.id.tv_trade_time));
        tvType = ((TextView) findViewById(R.id.tv_trade_type));
    }

    //商家确认订单
    public void confirmOrder(View view) {
        Request<JSONObject> request = buildNetRequest(UrlUtils.addTransaction, 0, true);
        request.add("order_number", order_number);
        request.add("seller", seller);
        request.add("customer", customer);
        request.add("money", money);
        request.add("pay_password", pay_password);
        request.add("pay_type", pay_type);
        request.add("scenetype", 2);
        executeNetWork(request, "请稍后");
        setCallback(this);
    }


    @Override
    public void handle200Data(JSONObject dataObj, String message) {
        Intent intent = new Intent(this, TradeResultActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void handle404(String message) {
        getDm().buildAlertDialog(message);
    }

    @Override
    public void handleNoNetWork() {

    }

    @Override
    public void clickImage(View im) {

    }

    @Override
    public void clickComplete(View tv) {
        finish();
    }
}
