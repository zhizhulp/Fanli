package com.qlqwgw.fanli.activities.main;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.jaeger.library.StatusBarUtil;
import com.lhh.apst.library.AdvancedPagerSlidingTabStrip;
import com.qlqwgw.fanli.R;
import com.qlqwgw.fanli.fragments.main.FirstFragment;
import com.qlqwgw.fanli.fragments.me.FourthFragment;
import com.qlqwgw.fanli.fragments.message.SecondFragment;
import com.qlqwgw.fanli.fragments.shop.ThirdFragment;
import com.qlqwgw.fanli.utils.LogUtils;
import com.qlqwgw.fanli.utils.ScreenDpiUtils;

import java.util.Random;

/**
 * 主界面
 */
public class MainActivity extends AppCompatActivity{

    public AdvancedPagerSlidingTabStrip mAPSTS;
    public APSTSViewPager mVP;

    private static final int VIEW_FIRST = 0;
    private static final int VIEW_SECOND = 1;
    private static final int VIEW_THIRD = 2;
    private static final int VIEW_FOURTH = 3;

    private static final int VIEW_SIZE = 4;

    private FirstFragment mFirstFragment = null;
    private SecondFragment mSecondFragment = null;
    private ThirdFragment mThirdFragment = null;
    private FourthFragment mFourthFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setStatusBar();
        //透明状态栏
        setStatusBar();
        findViews();
        init();
        LogUtils.PrintLog("123","当前屏幕密度是："+ScreenDpiUtils.getScreenDpi(this));
    }

    private void setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    private void findViews(){
        mAPSTS = (AdvancedPagerSlidingTabStrip)findViewById(R.id.tabs);
        mVP = (APSTSViewPager)findViewById(R.id.vp_main);
    }

    private void init(){
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());
        mVP.setAdapter(new FragmentAdapter(getSupportFragmentManager()));
        adapter.notifyDataSetChanged();
        mAPSTS.setViewPager(mVP);
        mVP.setNoFocus(false);//设置viewpager禁止滑动
        mVP.setCurrentItem(VIEW_FOURTH);//设置viewpager默认页
        //mAPSTS.showDot(VIEW_FIRST,"99+");
    }

    /**
     * 主页viewPager设配器
     */
    public class FragmentAdapter extends FragmentStatePagerAdapter implements AdvancedPagerSlidingTabStrip.IconTabProvider{

        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position >= 0 && position < VIEW_SIZE){
                switch (position){
                    case  VIEW_FIRST:
                        if(null == mFirstFragment)
                            mFirstFragment = FirstFragment.instance();
                        return mFirstFragment;

                    case VIEW_SECOND:
                        if(null == mSecondFragment)
                            mSecondFragment = SecondFragment.instance();
                        return mSecondFragment;

                    case VIEW_THIRD:
                        if(null == mThirdFragment)
                            mThirdFragment = ThirdFragment.instance();
                        return mThirdFragment;

                    case VIEW_FOURTH:
                        if(null == mFourthFragment)
                            mFourthFragment = FourthFragment.instance();
                        return mFourthFragment;
                    default:
                        break;
                }
            }
            return null;
        }

        @Override
        public int getCount() {
            return VIEW_SIZE;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if(position >= 0 && position < VIEW_SIZE){
                switch (position){
                    case  VIEW_FIRST:
                        return  "首页";
                    case  VIEW_SECOND:
                        return  "商城";
                    case  VIEW_THIRD:
                        return  "消息";
                    case  VIEW_FOURTH:
                        return  "我";
                    default:
                        break;
                }
            }
            return null;
        }

        @Override
        public Integer getPageIcon(int index) {
            if(index >= 0 && index < VIEW_SIZE){
                switch (index){
                    case  VIEW_FIRST:
                        return  R.mipmap.tab_main;
                    case VIEW_SECOND:
                        return  R.mipmap.tab_message;
                    case VIEW_THIRD:
                        return  R.mipmap.tab_shop;
                    case VIEW_FOURTH:
                        return  R.mipmap.tab_me;
                    default:
                        break;
                }
            }
            return 0;
        }

        @Override
        public Integer getPageSelectIcon(int index) {
            if(index >= 0 && index < VIEW_SIZE){
                switch (index){
                    case  VIEW_FIRST:
                        return  R.mipmap.tab_main_select;
                    case VIEW_SECOND:
                        return  R.mipmap.tab_message_select;
                    case VIEW_THIRD:
                        return  R.mipmap.tab_shop_select;
                    case VIEW_FOURTH:
                        return  R.mipmap.tab_me_select;
                    default:
                        break;
                }
            }
            return 0;
        }

        @Override
        public Rect getPageIconBounds(int position) {
            int pxw = ScreenDpiUtils.dip2px(MainActivity.this, 25);
            int pxh = ScreenDpiUtils.dip2px(MainActivity.this, 25);
            return new Rect(0,0,pxw,pxh);

        }
    }
}
