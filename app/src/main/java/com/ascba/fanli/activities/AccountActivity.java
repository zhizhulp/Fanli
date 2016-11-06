package com.ascba.fanli.activities;

import android.os.Bundle;
import android.widget.ListView;

import com.ascba.fanli.R;
import com.ascba.fanli.activities.base.BaseActivity;
import com.ascba.fanli.adapter.WhiteAccountAdapter;
import com.ascba.fanli.beans.WhiteAccount;

import java.util.ArrayList;
import java.util.List;

public class AccountActivity extends BaseActivity {
    private ListView cashAccountListView;
    private WhiteAccountAdapter adapter;
    private List<WhiteAccount> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        init();
    }

    private void init() {
        cashAccountListView = ((ListView) findViewById(R.id.cash_account_list));
        initData();
        adapter = new WhiteAccountAdapter(this,mList);
        cashAccountListView.setAdapter(adapter);
    }

    private void initData() {
        mList=new ArrayList<>();
        for (int i = 0; i < 21; i++) {
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
        }
    }

}
