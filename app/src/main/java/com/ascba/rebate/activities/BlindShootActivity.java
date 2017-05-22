package com.ascba.rebate.activities;

import android.os.Bundle;
import android.view.View;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetActivity;

/**
 * Created by 李鹏 on 2017/5/21.
 * 盲拍详情界面
 */

public class BlindShootActivity extends BaseNetActivity {

    public static final int STATE_NO_PAY = 1; //未交保证金
    public static final int STATE_NO_START = 2; //未开始
    public static final int STATE_START = 3; //开始

    private View noPay;//未交保证金
    private View noStart, viewNotic;//未开始
    private View payStart;//开始竞拍

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blind_shoot);
        initUI();
    }

    private void initUI() {
        //尚未开始
        viewNotic = findViewById(R.id.text_unstart);
        noStart = findViewById(R.id.view_noStart);

        //未交保证金
        noPay = findViewById(R.id.view_noPay);

        //开始竞拍
        payStart = findViewById(R.id.view_pay);
    }

    /*
        更改状态
     */
    private void setState(int flag) {
        viewNotic.setVisibility(View.GONE);
        noStart.setVisibility(View.GONE);
        noPay.setVisibility(View.GONE);
        payStart.setVisibility(View.GONE);
        switch (flag) {
            case STATE_NO_PAY:
                noPay.setVisibility(View.VISIBLE);
                break;

            case STATE_NO_START:
                viewNotic.setVisibility(View.VISIBLE);
                noStart.setVisibility(View.VISIBLE);
                break;
            case STATE_START:
                payStart.setVisibility(View.VISIBLE);
                break;
        }
    }
}
