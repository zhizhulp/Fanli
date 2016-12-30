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
import com.ascba.rebate.beans.CashAccount;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class WhiteAccountFragment extends Fragment {

    private ListView mWSAccountList;
    private WhiteAccountAdapter mAdapter;
    private List<CashAccount> mList;

    public WhiteAccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_white_account, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initListView(view);
    }

    private void initListView(View view) {
        mWSAccountList = ((ListView) view.findViewById(R.id.wsaccount_list));
        initList();
        mAdapter = new WhiteAccountAdapter(getActivity(),mList);
        mWSAccountList.setAdapter(mAdapter);
    }

    private void initList() {
        mList=new ArrayList<>();
        /*for (int i = 0; i < 21; i++) {
            if(i %3==0){
                CashAccount whiteAccount=new CashAccount("昨天","21:14",11,"+456.12","登录注册-赠送");
                mList.add(whiteAccount);
            }else if(i %3==1){
                CashAccount whiteAccount=new CashAccount("今天","13:14",11,"+456.12","老家肉饼-消费");
                mList.add(whiteAccount);
            }else{
                CashAccount whiteAccount=new CashAccount("3天之前","10:14",11,"+456.12","推荐会员-返利");
                mList.add(whiteAccount);
            }
        }*/
    }

}
