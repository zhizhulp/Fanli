package com.ascba.rebate.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ascba.rebate.R;
import com.ascba.rebate.adapter.InOutComeAdapter;
import com.ascba.rebate.beans.CashAccount;
import com.ascba.rebate.fragments.base.Base2Fragment;
import com.ascba.rebate.view.SuperSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李鹏 on 2017/03/29 0029.
 * 交易记录——收入
 */

public class IncomeFragment extends Base2Fragment {

    private Context context;
    private SuperSwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private List<CashAccount> beanList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_income, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();
        InitView(view);
    }

    private void InitView(View view) {
        refreshLayout = (SuperSwipeRefreshLayout) view.findViewById(R.id.refresh_layout);

        initData();
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        InOutComeAdapter inOutComeAdapter = new InOutComeAdapter(R.layout.wsaccount_list_item, beanList);

        View empty = LayoutInflater.from(context).inflate(R.layout.empty_records, null);
        inOutComeAdapter.setEmptyView(empty);

        recyclerView.setAdapter(inOutComeAdapter);
    }

    private void initData() {
        beanList.add(new CashAccount("今天", "21:41", "+456.12", "农业银行-充值", null, R.mipmap.cash_recharge));

        beanList.add(new CashAccount("今天", "21:41", "-456.12", "农业银行-提现", "24小时内到账", R.mipmap.cash_cash_get));

        beanList.add(new CashAccount("昨天", "21:41", "+456.12", "兑换积分-返利", null, R.mipmap.cash_exchange));

        beanList.add(new CashAccount("昨天", "21:41", "-456.12", "老家肉饼-消费", null, R.mipmap.cash_cost));

        beanList.add(new CashAccount("前天", "21:41", "+456.12", "推荐会员-佣金", null, R.mipmap.cash_employee));
    }
}
