package com.ascba.rebate.activities.offline_business;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.adapter.sweep.MyAllOrderAdapter;

public class MyAllOrdersActivity extends BaseNetActivity {
    private RecyclerView recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_all_orders);
        initView();

    }

    private void initView() {
        recycler = (RecyclerView) findViewById(R.id.myallorder_recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));

    }

}
