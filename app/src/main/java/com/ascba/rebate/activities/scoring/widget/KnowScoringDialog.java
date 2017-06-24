package com.ascba.rebate.activities.scoring.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.scoring.FragmentKnowscroing;

import java.util.ArrayList;
import java.util.List;

import static com.yanzhenjie.nohttp.NoHttp.getContext;

/**
 * Created by lenovo on 2017/6/23.
 */

public class KnowScoringDialog {
    private Dialog dialog;
    private ViewPager viewPager;
    private FmPagerAdapter pagerAdapter;
    private LinearLayout llDot;
    private Context context;
    private List<Fragment> mFragments;
    private int[] mPics = {R.mipmap.know_invitecount,R.mipmap.know_withdrawcount,R.mipmap.know_acount_rank,
                          R.mipmap.know_accountremainder,R.mipmap.know_consumedevote};
    private String[] mTitles = {"邀请次数","提现分享次数","账户等级","账户余额","消费贡献"};
    private String content1 = "账户余额越多，评分越高，当账户余额达到10000元时分数增加5分，账户余额每增加20000元，分数加10分，赶快存钱吧。";
    private String content2="当你完成一笔提现操作，并把这次提现成功分享给你的好友后，来往信用分会相应的增加，分享一次提现记录信用分会增加1分。";
    private String content3="账户等级越高，评分越高，消费者会员的基础信用分为200，商家会员：vip上家会员的基础分为300，金钻上家会员的基础为350。合伙会员：" +
            "初级合伙人初始分数为550，高级合伙人初始分数为500，中级合伙人初始分数为550，高级合伙人初始分数为600，精英合伙人初始分数为700。";
    private String content4="账户余额越多，评分越高，当账户余额达到10000元时分数增加5分，账户余额每增加10000元，分数增加5分";

    private String content5="当月在钱来钱往平台消费的金额越大，来往信用分越高，消费贡献金额每增加500元，来往信用分增加1分，可累计。";

    private String[] mContents = {content1,content2,content3,content4,content5};
    private View moveDot;
    private int distance;//两点之间的距离
    private int currentPage = 0;

//    private Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            viewPager.setCurrentItem(currentPage++ % mPics.length);
//            handler.sendEmptyMessageDelayed(1, 3000);
//        }
//    };

    public KnowScoringDialog(Context context, FragmentManager fragmentManager){
        this.context = context;
        dialog = new Dialog(context, R.style.dialog);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.know_scoring_dialog);
        dialog.show();

        init(fragmentManager);
    }

    private void init(FragmentManager fragmentManager) {
        mFragments = new ArrayList<>();
        viewPager = (ViewPager) dialog.findViewById(R.id.vp_know_scoring);
        llDot = (LinearLayout) dialog.findViewById(R.id.ll_know_dot);
        moveDot = dialog.findViewById(R.id.move_dot);
        FragmentKnowscroing fragmentKnowscroing;
        for(int i=0;i<mPics.length;i++){
            fragmentKnowscroing = new FragmentKnowscroing();
            Bundle argument = new Bundle();
            argument.putInt("pic",mPics[i]);
            argument.putString("title",mTitles[i]);
            argument.putString("content",mContents[i]);
            fragmentKnowscroing.setArguments(argument);
            mFragments.add(fragmentKnowscroing);
        }

        pagerAdapter = new FmPagerAdapter(mFragments,fragmentManager);
        viewPager.setAdapter(pagerAdapter);

        setDot(mPics);

        dialog.findViewById(R.id.ib_know_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    //设置圆点的方法
    private void setDot(int[] mPics) {
        View view;
        for (int i = 0; i < mPics.length; i++) {
            //灰点
            view=new View(getContext());
            view.setBackgroundResource(R.drawable.dot_grey_shape);
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(dp2px(6),dp2px(6));
            if(i!=0){
                params.leftMargin=dp2px(7);
            }
            view.setLayoutParams(params);
            llDot.addView(view);
        }
        setMoveDot();
    }

    public int dp2px(int values) {//58dp,根据屏幕分辨率转化成对应的px长度
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (values * density + 0.5f);
    }
    private void setMoveDot() {
        moveDot.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                distance = llDot.getChildAt(1).getLeft() - llDot.getChildAt(0).getLeft();
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

//                handler.removeMessages(1);
                position = position % mPics.length;
                float left = distance * (position + positionOffset);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) moveDot.getLayoutParams();
                params.leftMargin = Math.round(left);
                moveDot.setLayoutParams(params);
//                handler.sendEmptyMessageDelayed(1, 3000);
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

}
