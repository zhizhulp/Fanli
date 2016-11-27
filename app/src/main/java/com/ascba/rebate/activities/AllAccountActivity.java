package com.ascba.rebate.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.widget.RelativeLayout;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.NetworkBaseActivity;
import com.ascba.rebate.activities.main.APSTSViewPager;
import com.ascba.rebate.fragments.CashAccountFragment;
import com.ascba.rebate.fragments.WhiteAccountFragment;
import com.lhh.apst.library.AdvancedPagerSlidingTabStrip;
import com.lhh.apst.library.Margins;

public class AllAccountActivity extends NetworkBaseActivity implements View.OnClickListener {

    private AdvancedPagerSlidingTabStrip tabsContainer;
    private APSTSViewPager vp;
    private CashAccountFragment cashAccountFragment;
    private WhiteAccountFragment whiteAccountFragment;
    private View backView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_account);
        initViews();
    }

    private void initViews() {
        tabsContainer = ((AdvancedPagerSlidingTabStrip) findViewById(R.id.all_account_title_tabs));
        vp = ((APSTSViewPager) findViewById(R.id.vp_all_account));
        backView = findViewById(R.id.all_account_back);
        backView.setOnClickListener(this);
        FragmentAdapter adapter=new FragmentAdapter(getSupportFragmentManager());
        vp.setAdapter(adapter);
        tabsContainer.setViewPager(vp);
    }

    @Override
    public void onClick(View v) {
        finish();
    }

    public class FragmentAdapter extends FragmentStatePagerAdapter implements AdvancedPagerSlidingTabStrip.LayoutProvider {

        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

                switch (position){
                    case  0:
                        if(null == cashAccountFragment)
                            cashAccountFragment = new CashAccountFragment();
                        return cashAccountFragment;

                    case 1:
                        if(null == whiteAccountFragment)
                            whiteAccountFragment = new WhiteAccountFragment();
                        return whiteAccountFragment;

                    default:
                        break;
                }

            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if(position >= 0 && position < 2){
                switch (position){
                    case  0:
                        return  "现金账单";
                    case  1:
                        return  "积分账单";
                    default:
                        break;
                }
            }
            return null;
        }

        @Override
        public float getPageWeight(int position) {
            return 0;
        }

        @Override
        public int[] getPageRule(int position) {
            switch (position){
                case 0:
                    return new int[]{ RelativeLayout.ALIGN_PARENT_RIGHT};
                case 1:
                    return new int[]{ RelativeLayout.ALIGN_PARENT_LEFT};
            }
            return null;


        }

        @Override
        public Margins getPageMargins(int position) {
            return null;
        }
    }
}
