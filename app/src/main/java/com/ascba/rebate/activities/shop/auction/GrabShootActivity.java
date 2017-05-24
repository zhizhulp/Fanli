package com.ascba.rebate.activities.shop.auction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetActivity;

/**
 * Created by 李鹏 on 2017/5/24.
 * 抢拍详情界面
 */

public class GrabShootActivity extends BaseNetActivity {

    public static final int STATE_NO_PAY = 0; //未交保证金
    public static final int STATE_NO_START = 1; //未开始
    public static final int STATE_START = 2; //开始

    private View noPay;//未交保证金
    private View noStart, viewNotic;//未开始
    private View payStart;//开始竞拍

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grab_shoot);
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

        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        getFlag();
    }

    public static void startIntent(Context context, int flag) {
        Intent intent = new Intent(context, GrabShootActivity.class);
        intent.putExtra("flag", flag);
        context.startActivity(intent);
    }

    private void getFlag() {
        Intent intent = getIntent();
        setState(intent.getIntExtra("flag", 0));
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
                viewNotic.setVisibility(View.VISIBLE);
                noPay.setVisibility(View.VISIBLE);
                break;

            case STATE_NO_START:
                viewNotic.setVisibility(View.VISIBLE);
                noStart.setVisibility(View.VISIBLE);
                break;
            case STATE_START:
                viewNotic.setVisibility(View.VISIBLE);
                payStart.setVisibility(View.VISIBLE);
                break;
        }
    }
}
