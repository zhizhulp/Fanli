package com.ascba.rebate.activities;

import android.content.Context;
import android.os.Bundle;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWork4Activity;
import com.ascba.rebate.view.SuperSwipeRefreshLayout;

/**
 * Created by 李鹏 on 2017/04/11 0011.
 * 我——商家联盟——审核通过
 */

public class BusinessUnionActivity extends BaseNetWork4Activity {

    private Context context;
    private SuperSwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_union);
        context = this;
    }
}
