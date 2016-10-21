package com.qlqwgw.fanli.activities.main;

import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.lhh.apst.library.AdvancedPagerSlidingTabStrip;
import com.qlqwgw.fanli.R;
import com.qlqwgw.fanli.fragments.main.FirstFragment;
import com.qlqwgw.fanli.fragments.me.FourthFragment;
import com.qlqwgw.fanli.fragments.message.SecondFragment;
import com.qlqwgw.fanli.fragments.shop.ThirdFragment;
import com.qlqwgw.fanli.utils.LogUtils;
import com.qlqwgw.fanli.utils.ScreenDpiUtils;

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
        //透明状态栏
//        if(Build.VERSION.SDK_INT >=19){
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        }
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        //hideStatusBar();
        //full();
        hell3();
        findViews();
        init();
        LogUtils.PrintLog("123","当前屏幕密度是："+ScreenDpiUtils.getScreenDpi(this));
    }

    private void hell3() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){//4.4 全透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0 全透明实现
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);//calculateStatusColor(Color.WHITE, (int) alphaValue)
        }
    }

    private void full() {
        View decor = getWindow().getDecorView();
        int systemUiVisibility = decor.getSystemUiVisibility();
        int flags =
//                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
//                | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                 View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        systemUiVisibility |= flags;
        getWindow().getDecorView().setSystemUiVisibility(systemUiVisibility);
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

    private void hideStatusBar() {
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(attrs);
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
