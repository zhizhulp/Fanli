package com.ascba.rebate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWorkActivity;
import com.ascba.rebate.fragments.account.CashAccountFragment;
import com.ascba.rebate.fragments.account.WhiteAccountFragment;
import com.ascba.rebate.view.SwitchButton;
import com.jaeger.library.StatusBarUtil;

public class AllAccountActivity extends BaseNetWorkActivity implements View.OnClickListener {

    private CashAccountFragment cash;
    private WhiteAccountFragment white;
    private SwitchButton sb;
    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_account);
        StatusBarUtil.setColor(this, 0xffe52020);
        getDataFromIntent();
    }
    //红积分兑换成功，点击历史记录，查看账单
    private void getDataFromIntent() {
        initViews();
        Intent intent = getIntent();
        if(intent!=null){
            int order = intent.getIntExtra("order", 0);
            switchFragment(order);
        }


    }
    private void initViews() {
        fm=getSupportFragmentManager();
        sb = ((SwitchButton) findViewById(R.id.switchButton));
        sb.setLeftText("现金账单");
        sb.setRightText("积分账单");
    }
    private void switchFragment(int position) {
        cash = CashAccountFragment.getInstance(position);
        white = WhiteAccountFragment.getInstance(position);
        fm.beginTransaction()
                .add(R.id.recommend_fragment, cash)
                .commit();
        sb.setCallback(new SwitchButton.Callback() {
            @Override
            public void onLeftClick() {
                android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
                ft.show(cash);
                if(white.isAdded()){
                    ft.hide(white);
                }
                ft.commit();
            }

            @Override
            public void onRightClick() {
                android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
                if(white.isAdded()){
                    ft.show(white);
                }else {
                    ft.add(R.id.recommend_fragment,white);
                }
                ft.hide(cash);
                ft.commit();
            }
            @Override
            public void onCenterClick() {

            }
        });
    }
    @Override
    public void onClick(View v) {
        finish();
    }

}
