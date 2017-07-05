package com.ascba.rebate.activities.offline_business;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.RoundImageView;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

public class OfflinePaySuccedActivity extends BaseNetActivity {
    private RoundImageView userIcon;
    private TextView tvName, tvCost, tvPayType, tvAccount, tvScore, tvTime, tvTradeNumber, tvEmployee,payCommission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_pay_succed);
        initViews();
        setData();
    }


    private void initViews() {
        userIcon = ((RoundImageView) findViewById(R.id.im_user_icon));
        tvName = ((TextView) findViewById(R.id.tv_user_name));
        tvCost = ((TextView) findViewById(R.id.tv_user_cost));
        tvPayType = ((TextView) findViewById(R.id.tv_pay_type));
        tvAccount = ((TextView) findViewById(R.id.tv_acount));
        tvScore = ((TextView) findViewById(R.id.tv_score));
        tvTime = ((TextView) findViewById(R.id.tv_time));
        tvTradeNumber = ((TextView) findViewById(R.id.tv_trade_number));
        tvEmployee = ((TextView) findViewById(R.id.tv_employee));
        payCommission= (TextView) findViewById(R.id.tv_employee);
    }


    private void setData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        Picasso.with(this).load(UrlUtils.baseWebsite + bundle.getString("seller_cover_logo")).into(userIcon);
        tvName.setText(bundle.getString("seller_name"));
        tvCost.setText("-" + bundle.getDouble("importMoney", 0.00f));
        tvPayType.setText(bundle.getString("pay_type_text"));
        tvAccount.setText(bundle.getString("seller_mobile"));
        tvScore.setText(bundle.getInt("accumulate_points", 0) + "积分");
        tvTradeNumber.setText(bundle.getString("order_number"));
        payCommission.setText(bundle.getString("pay_commission")+"元");
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate=new Date(System.currentTimeMillis());//获取当前时间
        String  str=sdf.format(curDate);
        tvTime.setText(str);


    }

}
