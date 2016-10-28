package com.qlqwgw.fanli.activities;

import android.graphics.Rect;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lhh.apst.library.AdvancedPagerSlidingTabStrip;
import com.qlqwgw.fanli.R;
import com.qlqwgw.fanli.activities.base.BaseActivity;
import com.qlqwgw.fanli.activities.main.APSTSViewPager;
import com.qlqwgw.fanli.activities.main.MainActivity;
import com.qlqwgw.fanli.fragments.CashAccountFragment;
import com.qlqwgw.fanli.fragments.WhiteScoreAccountFragment;
import com.qlqwgw.fanli.fragments.main.FirstFragment;
import com.qlqwgw.fanli.fragments.me.FourthFragment;
import com.qlqwgw.fanli.fragments.message.SecondFragment;
import com.qlqwgw.fanli.fragments.shop.ThirdFragment;
import com.qlqwgw.fanli.utils.ScreenDpiUtils;

public class AccountActivity extends BaseActivity {
    private Fragment cashAccountFragment;
    private Fragment whiteScoreFragment;
    private APSTSViewPager accountPager;
    private AdvancedPagerSlidingTabStrip accountTabContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        findViews();
        init();
    }

    private void init() {
        AccountFragmentAdapter adapter=new AccountFragmentAdapter(getSupportFragmentManager());
        accountPager.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        accountTabContainer.setViewPager(accountPager);
        accountPager.setNoFocus(true);//设置viewpager滑动
        accountPager.setCurrentItem(0);//设置viewpager默认页
        //mAPSTS.showDot(VIEW_FIRST,"99+");
    }

    private void findViews() {
        accountPager = ((APSTSViewPager) findViewById(R.id.vp_account));
        accountTabContainer = ((AdvancedPagerSlidingTabStrip) findViewById(R.id.tabs_account));
    }

    /**
     * 账单设配器
     */
    /**ccount
     * 主页viewPager设配器
     */
    public class AccountFragmentAdapter extends FragmentStatePagerAdapter{

        public AccountFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position >= 0 && position < 2){
                switch (position){
                    case  0:
                        if(null == cashAccountFragment)
                            cashAccountFragment = new CashAccountFragment();
                        return cashAccountFragment;

                    case   1:
                        if(null == whiteScoreFragment)
                            whiteScoreFragment = new WhiteScoreAccountFragment();
                        return whiteScoreFragment;
                    default:
                        break;
                }
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
                        return  "白积分账单";
                    default:
                        break;
                }
            }
            return null;
        }




    }
}
