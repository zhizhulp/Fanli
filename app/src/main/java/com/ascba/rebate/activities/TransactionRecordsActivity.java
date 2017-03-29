package com.ascba.rebate.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWork4Activity;
import com.ascba.rebate.adapter.FragmentPagerAdapter;
import com.ascba.rebate.fragments.IncomeFragment;
import com.ascba.rebate.fragments.OutcomeFragment;
import com.ascba.rebate.view.ShopABarText;
import com.flyco.tablayout.SlidingTabLayout;
import java.util.ArrayList;
import java.util.List;
import static com.ascba.rebate.R.id.shopBar;

/**
 * Created by 李鹏 on 2017/03/29 0029.
 * 交易记录
 */

public class TransactionRecordsActivity extends BaseNetWork4Activity {

    private ShopABarText shopABar;
    private Context context;
    private SlidingTabLayout slidingtablayout;
    private ViewPager viewPager;
    private String[] mTitleList = new String[]{"收入", "支出"};//页卡标题集合
    private List<Fragment> fragmentList = new ArrayList<>();//页卡视图集合

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_records);
        context = this;
        initView();
    }

    public static void startIntent(Context context) {
        Intent intent = new Intent(context, TransactionRecordsActivity.class);
        context.startActivity(intent);
    }

    private void initView() {
        /**
         * 导航栏
         */
        shopABar = (ShopABarText) findViewById(shopBar);
        shopABar.setBtnEnable(false);
        shopABar.setCallback(new ShopABarText.Callback() {
            @Override
            public void back(View v) {
                finish();
            }

            @Override
            public void clkBtn(View v) {

            }
        });

        slidingtablayout = (SlidingTabLayout) findViewById(R.id.slidingtablayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        initViewpagerView();
    }

    /**
     * 初始化Viewpager
     */
    private void initViewpagerView() {
        IncomeFragment incomeFragment = new IncomeFragment();
        fragmentList.add(incomeFragment);

        OutcomeFragment outcomeFragment = new OutcomeFragment();
        fragmentList.add(outcomeFragment);

        FragmentPagerAdapter mAdapter = new FragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(mAdapter);//给ViewPager设置适配器

        slidingtablayout.setViewPager(viewPager, mTitleList);
    }
}
