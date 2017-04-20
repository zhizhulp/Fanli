package com.ascba.rebate.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.ascba.rebate.R;
import com.ascba.rebate.fragments.base.BaseNetFragment;
import com.ascba.rebate.view.SuperSwipeRefreshLayout;

/**
 * Created by 李鹏 on 2017/04/01 0001.
 * 财富-返佣账户-转到余额
 */

public class GoBalanceFragment extends BaseNetFragment implements SuperSwipeRefreshLayout.OnPullRefreshListener {

    private Context context;
    private SuperSwipeRefreshLayout refreshLayout;
    private EditText editText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_go_balance, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();
        initView(view);
    }

    private void initView(View view) {
        refreshLayout = (SuperSwipeRefreshLayout) view.findViewById(R.id.refresh_layout);
        editText = (EditText) view.findViewById(R.id.go_balance_ed);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        refreshLayout.setOnPullRefreshListener(this);
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
}
