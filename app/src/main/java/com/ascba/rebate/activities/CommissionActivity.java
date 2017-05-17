package com.ascba.rebate.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.adapter.FragmentPagerAdapter;
import com.ascba.rebate.fragments.rollout.GoBalanceFragment;
import com.ascba.rebate.fragments.rollout.GoBankCardFragment;
import com.ascba.rebate.view.MoneyBar;
import com.ascba.rebate.view.ShopABarText;
import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李鹏 on 2017/04/01 0001.
 * 财富-返佣账户
 */

public class CommissionActivity extends BaseNetActivity implements MoneyBar.CallBack {

    private ShopABarText shopABar;
    private Context context;
    private SlidingTabLayout slidingtablayout;
    private ViewPager viewPager;
    private String[] mTitleList = new String[]{"转出到余额", "转出到银行卡"};//页卡标题集合
    private List<Fragment> fragmentList = new ArrayList<>();//页卡视图集合
    private MoneyBar mb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commission);
        context = this;
        initView();
    }

    private void initView() {
        /**
         * 导航栏
         */
        /*shopABar = (ShopABarText) findViewById(R.id.shopBar);
        shopABar.setBtnEnable(false);
        shopABar.setCallback(new ShopABarText.Callback() {
            @Override
            public void back(View v) {
                finish();
            }

            @Override
            public void clkBtn(View v) {

            }
        });*/
        mb = ((MoneyBar) findViewById(R.id.mb));
        mb.setTailTitle(getString(R.string.inoutcome_record));
        mb.setCallBack(this);

        slidingtablayout = (SlidingTabLayout) findViewById(R.id.slidingtablayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        initViewpagerView();
    }

    /**
     * 初始化Viewpager
     */
    private void initViewpagerView() {
        GoBalanceFragment goBalanceFragment = new GoBalanceFragment();
        fragmentList.add(goBalanceFragment);

        GoBankCardFragment goBankCardFragment = new GoBankCardFragment();
        fragmentList.add(goBankCardFragment);

        FragmentPagerAdapter mAdapter = new FragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(mAdapter);//给ViewPager设置适配器

        slidingtablayout.setViewPager(viewPager, mTitleList);
    }

    @Override
    public void clickImage(View im) {

    }

    @Override
    public void clickComplete(View tv) {
        TransactionRecordsActivity.startIntent(this);
    }
}
