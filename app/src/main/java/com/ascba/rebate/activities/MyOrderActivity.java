package com.ascba.rebate.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.adapter.FragmentPagerAdapter;
import com.ascba.rebate.fragments.shop.order.AllOrderFragment;
import com.ascba.rebate.fragments.shop.order.DeliverOrderFragment;
import com.ascba.rebate.fragments.shop.order.EvaluateOrderFragment;
import com.ascba.rebate.fragments.shop.order.PayOrderFragment;
import com.ascba.rebate.fragments.shop.order.TakeOrderFragment;
import com.ascba.rebate.view.ShopABar;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.widget.MsgView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李鹏 on 2017/03/14 0014.
 * 我的订单页面
 */

public class MyOrderActivity extends BaseNetActivity {

    private ShopABar shopABar;
    private Context context;
    private static SlidingTabLayout slidingtablayout;
    private ViewPager mViewPager;
    private List<Bean> mTitleList = new ArrayList<>();//页卡标题集合
    private List<Fragment> fragmentList = new ArrayList<>();//页卡视图集合
    private static int index = 0;
    private int[] orderMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        context = this;
        getIndex();
        initView();
    }

    public static void startIntent(Context context, int index, int[] orderMsg) {
        Intent intent = new Intent(context, MyOrderActivity.class);
        intent.putExtra("index", index);
        intent.putExtra("msg", orderMsg);
        context.startActivity(intent);
    }

    public static void startIntent(Context context, int index) {
        Intent intent = new Intent(context, MyOrderActivity.class);
        intent.putExtra("index", index);
        context.startActivity(intent);
    }

    private void getIndex() {
        Intent intent = getIntent();
        if (intent != null) {
            index = intent.getIntExtra("index", 0);
            orderMsg = intent.getIntArrayExtra("msg");
        }
    }

    private void initView() {
        shopABar = (ShopABar) findViewById(R.id.activity_order_bar);
        shopABar.setImageOtherEnable(false);

        shopABar.setCallback(new ShopABar.Callback() {
            @Override
            public void back(View v) {
                finish();
            }

            @Override
            public void clkMsg(View v) {
                //消息中心
                ShopMessageActivity.startIntent(context);
            }

            @Override
            public void clkOther(View v) {

            }
        });
        slidingtablayout = (SlidingTabLayout) findViewById(R.id.slidingtablayout);
        mViewPager = (ViewPager) findViewById(R.id.activity_order_vp);
        initViewpagerView();
    }

    /**
     * 初始化Viewpager
     */
    private void initViewpagerView() {
        //全部
        AllOrderFragment allOrderFragment = new AllOrderFragment();
        fragmentList.add(allOrderFragment);

        //代付款
        PayOrderFragment payOrderFragment = new PayOrderFragment();
        fragmentList.add(payOrderFragment);

        //待发货
        DeliverOrderFragment goodsFragment = new DeliverOrderFragment();
        fragmentList.add(goodsFragment);

        //待收货
        TakeOrderFragment takeOrderFragment = new TakeOrderFragment();
        fragmentList.add(takeOrderFragment);

        //待评价
        EvaluateOrderFragment evaluateOrderFragment = new EvaluateOrderFragment();
        fragmentList.add(evaluateOrderFragment);

        FragmentPagerAdapter mAdapter = new FragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setAdapter(mAdapter);//给ViewPager设置适配器


        //添加页卡标题
        if (mTitleList != null && mTitleList.size() > 0) {
            mTitleList.add(new Bean("全部", orderMsg[0]));
            mTitleList.add(new Bean("待付款", orderMsg[1]));
            mTitleList.add(new Bean("待发货", orderMsg[2]));
            mTitleList.add(new Bean("待收货", orderMsg[3]));
            mTitleList.add(new Bean("待评价", orderMsg[4]));
        }

        String[] title = new String[]{"全部", "待付款", "待发货", "待收货", "待评价"};
        slidingtablayout.setViewPager(mViewPager, title);

        /**
         * 遍历添加消息
         */
//        for (int i = 0; i < mTitleList.size(); i++) {
//            Bean bean = mTitleList.get(i);
//            if (bean.getNum() > 0) {
//                slidingtablayout.showMsg(i, bean.getNum());
//                /**
//                 * position
//                 * 偏移量
//                 */
//                slidingtablayout.setMsgMargin(i, 3, 12);
//                MsgView msgView = slidingtablayout.getMsgView(i);
//                if (msgView != null) {
//                    msgView.setBackgroundColor(Color.parseColor("#ffffffff"));
//                    msgView.setTextColor(Color.parseColor("#DD0E0E"));
//                    msgView.setStrokeColor(Color.parseColor("#DD0E0E"));
//                }
//            }
//        }
        slidingtablayout.setCurrentTab(index);
    }

    public static void setCurrTab(int positon) {
        index = positon;
        slidingtablayout.setCurrentTab(positon);
    }

    public void setMsg(int position, int num) {
        if (num > 0) {
            slidingtablayout.showMsg(position, num);
            MsgView msgView = slidingtablayout.getMsgView(position);
            int msgNum = Integer.parseInt(msgView.getText().toString());
        } else {
            slidingtablayout.hideMsg(position);
        }
    }

    public class Bean {
        private String title;
        private int num;

        public Bean(String title, int num) {
            this.title = title;
            this.num = num;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }
    }

}
