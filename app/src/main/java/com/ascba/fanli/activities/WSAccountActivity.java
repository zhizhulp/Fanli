package com.ascba.fanli.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.ascba.fanli.R;
import com.ascba.fanli.activities.base.BaseActivity;
import com.ascba.fanli.adapter.WhiteAccountAdapter;
import com.ascba.fanli.beans.WhiteAccount;
import java.util.ArrayList;
import java.util.List;

public class WSAccountActivity extends BaseActivity {

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
            }else if(i %3==1){
                WhiteAccount whiteAccount=new WhiteAccount("今天","13:14",11,"+456.12","老家肉饼-消费");
            }else{
                WhiteAccount whiteAccount=new WhiteAccount("3天之前","10:14",11,"+456.12","推荐会员-返利");
            }
        }
    }
}
