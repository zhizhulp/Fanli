package com.ascba.rebate.activities;

import android.os.Bundle;
import android.widget.ListView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWorkActivity;
import com.ascba.rebate.adapter.RecAdapter;
import com.ascba.rebate.beans.FirstRec;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

public class FirstRecActivity extends BaseNetWorkActivity {

    private ListView firstRecListView;
    private RecAdapter recAdapter;
    private List<FirstRec> mList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_rec);
        StatusBarUtil.setColor(this, 0xffe52020);
        initViews();
    }

    private void initViews() {
        firstRecListView = ((ListView) findViewById(R.id.first_rec_list));
        initData();
        recAdapter = new RecAdapter(mList,this);
        firstRecListView.setAdapter(recAdapter);
    }

    private void initData() {
        mList=new ArrayList<>();
        /*for (int i = 0; i < 20; i++) {
            FirstRec firstRec=new FirstRec("钱来钱往(金钻会员)",R.mipmap.me_user_img,"推荐5人","获得1000积分","2016.12.31");
            mList.add(firstRec);
        }*/
    }
}
