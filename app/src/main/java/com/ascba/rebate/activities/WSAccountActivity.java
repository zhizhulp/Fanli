package com.ascba.rebate.activities;

import android.os.Bundle;
import android.widget.ListView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWorkActivity;
import com.ascba.rebate.adapter.WhiteAccountAdapter;
import com.ascba.rebate.beans.WhiteAccount;
import java.util.ArrayList;
import java.util.List;

public class WSAccountActivity extends BaseNetWorkActivity {

    private ListView mWSAccountList;
    private WhiteAccountAdapter mAdapter;
    private List<WhiteAccount> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wsaccount);
        initListView();
    }

    private void initListView() {
        mWSAccountList = ((ListView) findViewById(R.id.wsaccount_list));
        initList();
        mAdapter = new WhiteAccountAdapter(this,mList);
        mWSAccountList.setAdapter(mAdapter);
    }

    private void initList() {
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
