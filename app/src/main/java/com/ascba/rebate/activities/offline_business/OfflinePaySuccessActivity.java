package com.ascba.rebate.activities.offline_business;

import android.os.Bundle;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.view.RoundImageView;

/**
 * 线下支付-用户付款成功详情
 */

public class OfflinePaySuccessActivity extends BaseNetActivity {

    private RoundImageView userIcon;
    private TextView tvName;
    private TextView tvCost;
    private TextView tvPayType;
    private TextView tvAccount;
    private TextView tvScore;
    private TextView tvTime;
    private TextView tvTradeNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_pay_success);

        initViews();
    }

    private void initViews() {
        userIcon = ((RoundImageView) findViewById(R.id.im_user_icon));
        tvName = ((TextView) findViewById(R.id.tv_user_name));
        tvCost = ((TextView) findViewById(R.id.tv_user_cost));

        tvPayType = ((TextView) findViewById(R.id.tv_pay_type));
        tvAccount = ((TextView) findViewById(R.id.tv_account));
        tvScore = ((TextView) findViewById(R.id.tv_score));
        tvTime = ((TextView) findViewById(R.id.tv_time));
        tvTradeNumber = ((TextView) findViewById(R.id.tv_trade_number));
    }
}
