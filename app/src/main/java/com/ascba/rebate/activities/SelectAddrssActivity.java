package com.ascba.rebate.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWork4Activity;
import com.ascba.rebate.beans.ReceiveAddressBean;

import java.util.ArrayList;

/**
 * Created by 李鹏 on 2017/04/05 0005.
 * 选择收货地址
 */

public class SelectAddrssActivity extends BaseNetWork4Activity {

    private ArrayList<ReceiveAddressBean> beanList;//收货地址
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_address);
        context = this;
        getAddressList();
    }

    public static void startIntent(Context context, ArrayList<ReceiveAddressBean> beanList) {
        Intent intent = new Intent(context, SelectAddrssActivity.class);
        intent.putParcelableArrayListExtra("address", beanList);
        context.startActivity(intent);
    }

    private void getAddressList() {
        Intent intent = getIntent();
        if (intent != null) {
            beanList = intent.getParcelableArrayListExtra("address");
        }
    }
}
