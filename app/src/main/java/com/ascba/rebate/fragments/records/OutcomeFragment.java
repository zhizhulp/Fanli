package com.ascba.rebate.fragments.records;

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
import com.ascba.rebate.fragments.base.BaseNetFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李鹏 on 2017/03/29 0029.
 * 交易记录——支出
 */

public class OutcomeFragment extends BaseNetFragment {
    private Context context;
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

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        InOutComeAdapter inOutComeAdapter = new InOutComeAdapter(R.layout.wsaccount_list_item, beanList);

        View empty = LayoutInflater.from(context).inflate(R.layout.empty_records, null);
        inOutComeAdapter.setEmptyView(empty);

        recyclerView.setAdapter(inOutComeAdapter);
    }
}
