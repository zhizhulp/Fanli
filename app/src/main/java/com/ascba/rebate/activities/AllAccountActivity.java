package com.ascba.rebate.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWorkActivity;
import com.ascba.rebate.fragments.CashAccountFragment;
import com.ascba.rebate.fragments.WhiteAccountFragment;
import com.ascba.rebate.utils.LogUtils;
import com.ascba.rebate.view.SwitchButton;
import com.lhh.apst.library.AdvancedPagerSlidingTabStrip;

public class AllAccountActivity extends BaseNetWorkActivity implements View.OnClickListener {

    private AdvancedPagerSlidingTabStrip tabsContainer;
    //private APSTSViewPager vp;
    private CashAccountFragment cash;
    private WhiteAccountFragment white;
    private SwitchButton sb;
    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_account);
        fm=getSupportFragmentManager();
        initViews();
    }

    private void initViews() {
        //tabsContainer = ((AdvancedPagerSlidingTabStrip) findViewById(R.id.all_account_title_tabs));
        //vp = ((APSTSViewPager) findViewById(R.id.vp_all_account));
        //backView = findViewById(R.id.all_account_back);
        //backView.setOnClickListener(this);
        //FragmentAdapter adapter=new FragmentAdapter(getSupportFragmentManager());
        //vp.setAdapter(adapter);
        //tabsContainer.setViewPager(vp);
        sb = ((SwitchButton) findViewById(R.id.switchButton));
        switchFragment();

    }
    private void switchFragment() {
        cash = new CashAccountFragment();
        white = new WhiteAccountFragment();
        fm.beginTransaction()
                .add(R.id.recommend_fragment, cash)
                .add(R.id.recommend_fragment, white)
                .commit();
        fm.beginTransaction().show(cash).hide(white).commit();
        sb.setCallback(new SwitchButton.Callback() {
            @Override
            public void onLeftClick() {
                LogUtils.PrintLog("123", "left is click");
                android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
                ft.show(cash);
                ft.hide(white);
                ft.commit();
            }

            @Override
            public void onRightClick() {
                LogUtils.PrintLog("123", "right is click");
                android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
                ft.show(white);
                ft.hide(cash);
                ft.commit();
            }

            @Override
            public void onCenterClick() {
                /*LogUtils.PrintLog("123", "center is click");
                android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
                ft.hide(cash);
                ft.hide(white);
                ft.commit();*/
            }
        });
    }

    @Override
    public void onClick(View v) {
        finish();
    }

}
