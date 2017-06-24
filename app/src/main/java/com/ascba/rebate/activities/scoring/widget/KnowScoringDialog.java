package com.ascba.rebate.activities.scoring.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
    private String[] mTitles = {"邀请人数","提现分享次数","账户等级","账户余额","消费贡献"};
    private String content = "账户余额越多，评分越高，当账户余额达到10000元时分数增加5分，账户余额每增加20000元，分数加10分，赶快存钱吧";
    private String[] mContents = {content,content,content,content,content};
    private View moveDot;
    private int distance;//两点之间的距离
    private int currentPage = 0;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            viewPager.setCurrentItem(currentPage++ % mPics.length);
            handler.sendEmptyMessageDelayed(1, 3000);
        }
    };

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
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(16,16);
            if(i!=0){
                params.leftMargin=30;
            }
            view.setLayoutParams(params);
            llDot.addView(view);
        }
        setMoveDot();
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

                handler.removeMessages(1);
                position = position % mPics.length;
                float left = distance * (position + positionOffset);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) moveDot.getLayoutParams();
                params.leftMargin = Math.round(left);
                moveDot.setLayoutParams(params);
                handler.sendEmptyMessageDelayed(1, 3000);
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
