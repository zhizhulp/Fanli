package com.ascba.rebate.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWork4Activity;
import com.ascba.rebate.view.MoneyBar;
import com.ascba.rebate.view.SuperSwipeRefreshLayout;

/**
 * Created by 李鹏 on 2017/04/11 0011.
 * 我——商家联盟——审核通过
 */

public class BusinessUnionActivity extends BaseNetWork4Activity implements SuperSwipeRefreshLayout.OnPullRefreshListener, View.OnClickListener {

    private Context context;
    private SuperSwipeRefreshLayout refreshLayout;
    private MoneyBar moneyBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_union);
        context = this;
        initVIew();
    }

    private void initVIew() {
        refreshLayout = (SuperSwipeRefreshLayout) findViewById(R.id.refresh_layout);
        refreshLayout.setOnPullRefreshListener(this);

        moneyBar = (MoneyBar) findViewById(R.id.moneyBar);
        moneyBar.setCallBack(new MoneyBar.CallBack() {
            @Override
            public void clickImage(View im) {

            }

            @Override
            public void clickComplete(View tv) {
                finish();
            }
        });

        findViewById(R.id.business_data).setOnClickListener(this);
        findViewById(R.id.business_account).setOnClickListener(this);
        findViewById(R.id.business_code).setOnClickListener(this);
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onPullDistance(int distance) {

    }

    @Override
    public void onPullEnable(boolean enable) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.business_code:
                //立即收款
                break;
            case R.id.business_account:
                //流水记录
                break;
            case R.id.business_data:
                //商家资料
                break;
        }
    }
}
