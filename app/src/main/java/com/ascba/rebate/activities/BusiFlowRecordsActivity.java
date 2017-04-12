package com.ascba.rebate.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWork4Activity;
import com.ascba.rebate.adapter.FlowRecordsAdapter;
import com.ascba.rebate.beans.CashAccount;
import com.ascba.rebate.view.ShopABarText;
import com.ascba.rebate.view.SuperSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class BusiFlowRecordsActivity extends BaseNetWork4Activity implements
        SuperSwipeRefreshLayout.OnPullRefreshListener
        ,ShopABarText.Callback{

    private SuperSwipeRefreshLayout refreshLat;
    private RecyclerView rv;
    private FlowRecordsAdapter adapter;
    private List<CashAccount> data;
    private ShopABarText sb;
    private TextView tvTime;
    private TextView tvMoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busi_flow_records);

        initViews();
    }

    private void initViews() {
        initRefreshLat();
        initRecyclerview();
        initShopBar();
    }

    private void initShopBar() {
        sb = ((ShopABarText) findViewById(R.id.sb));
        sb.setCallback(this);
    }

    private void initRecyclerview() {
        rv = ((RecyclerView) findViewById(R.id.rv));
        getData();
        adapter = new FlowRecordsAdapter(R.layout.wsaccount_list_item,data);
        View inflate = getLayoutInflater().inflate(R.layout.flow_records, null);
        initHeadView(inflate);
        adapter.addHeaderView(inflate);

        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
    }

    private void initHeadView(View view) {
        tvTime = ((TextView) view.findViewById(R.id.tv_flow_time));
        tvMoney = ((TextView) view.findViewById(R.id.tv_flow_money));
    }

    private void getData() {
        data=new ArrayList<>();
        data.add(new CashAccount("今天", "21:41", "+456.12", "农业银行-充值", null, R.mipmap.cash_cost));

        data.add(new CashAccount("今天", "21:41", "-456.12", "农业银行-提现", null, R.mipmap.cash_cost));

        data.add(new CashAccount("昨天", "21:41", "+456.12", "兑换积分-返利", null, R.mipmap.cash_cost));

        data.add(new CashAccount("昨天", "21:41", "-456.12", "老家肉饼-消费", null, R.mipmap.cash_cost));

        data.add(new CashAccount("前天", "21:41", "+456.12", "推荐会员-佣金", null, R.mipmap.cash_cost));
    }

    private void initRefreshLat() {
        refreshLat = ((SuperSwipeRefreshLayout) findViewById(R.id.refresh_layout));
        refreshLat.setOnPullRefreshListener(this);
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
    public void back(View v) {
        finish();
    }

    @Override
    public void clkBtn(View v) {

    }
}
