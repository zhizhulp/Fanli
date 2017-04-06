package com.ascba.rebate.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWork4Activity;
import com.ascba.rebate.adapter.SelectAddressAdapter;
import com.ascba.rebate.beans.ReceiveAddressBean;
import com.ascba.rebate.handlers.DialogManager;
import com.ascba.rebate.view.ShopABarText;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import java.util.ArrayList;

/**
 * Created by 李鹏 on 2017/04/05 0005.
 * 选择收货地址
 */

public class SelectAddrssActivity extends BaseNetWork4Activity {

    private ArrayList<ReceiveAddressBean> beanList;//收货地址
    private Context context;
    private ShopABarText shopbar;
    private RecyclerView recyclerview;
    private DialogManager dm;
    private SelectAddressAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_address);
        context = this;
        getAddressList();
    }

    /**
     * 接受收货地址list
     */
    private void getAddressList() {
        Intent intent = getIntent();
        if (intent != null) {
            beanList = intent.getParcelableArrayListExtra("address");
            initUI();
        }
    }

    /**
     * 初始化UI
     */
    private void initUI() {
        shopbar = (ShopABarText) findViewById(R.id.shopbar);
        shopbar.setBtnText("编辑");
        shopbar.setCallback(new ShopABarText.Callback() {
            @Override
            public void back(View v) {
                finish();
            }

            @Override
            public void clkBtn(View v) {
                Intent intentAddress = new Intent(context, ReceiveAddressActivity.class);
                startActivity(intentAddress);
            }
        });
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(context));
        adapter = new SelectAddressAdapter(R.layout.item_receive_address, beanList);
        recyclerview.setAdapter(adapter);

        /**
         * 选择收货地址回调给确认订单页面
         */
        recyclerview.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent();
                intent.putExtra("address", beanList.get(position));
                setResult(1, intent);
                finish();
            }
        });
    }


}
