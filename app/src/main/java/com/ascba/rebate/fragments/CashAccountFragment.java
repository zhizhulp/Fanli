package com.ascba.rebate.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ascba.rebate.R;
import com.ascba.rebate.adapter.WhiteAccountAdapter;
import com.ascba.rebate.beans.WhiteAccount;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CashAccountFragment extends Fragment {
    private ListView cashAccountListView;
    private WhiteAccountAdapter adapter;
    private List<WhiteAccount> mList;


    public CashAccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cash_account, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    private void init(View view) {
        cashAccountListView = ((ListView) view.findViewById(R.id.cash_account_list));
        initData();
        adapter = new WhiteAccountAdapter(getActivity(),mList);
        cashAccountListView.setAdapter(adapter);
    }

    private void initData() {
        mList=new ArrayList<>();
        /*for (int i = 0; i < 21; i++) {
            if(i %3==0){
                WhiteAccount whiteAccount=new WhiteAccount("昨天","21:14",11,"+456.12","登录注册-赠送");
                mList.add(whiteAccount);
            }else if(i %3==1){
                WhiteAccount whiteAccount=new WhiteAccount("今天","13:14",11,"+456.12","老家肉饼-消费");
                mList.add(whiteAccount);
            }else{
                WhiteAccount whiteAccount=new WhiteAccount("3天之前","10:14",11,"+456.12","推荐会员-返利");
                mList.add(whiteAccount);
            }
        }*/
    }
}
