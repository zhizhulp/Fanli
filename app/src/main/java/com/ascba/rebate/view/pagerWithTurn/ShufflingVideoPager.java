package com.ascba.rebate.view.pagerWithTurn;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ascba.rebate.R;
import com.ascba.rebate.adapter.ShufflingVideoAdapter;
import com.ascba.rebate.beans.VideoBean;
import com.ascba.rebate.utils.ScreenDpiUtils;

import java.util.List;

/**
 * Created by 李鹏 on 2017/02/28 0028.
 */

public class ShufflingVideoPager extends RelativeLayout {

    private ViewPager viewPager;
    private LinearLayout indicator;
    private List<VideoBean> getList;
    private ImageView[] mImageView;
    private Context context;
    private ShufflingVideoAdapter adapter;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                handler.sendEmptyMessageDelayed(0, 2000);
            }
        }
    };

    public ShufflingVideoPager(Context context) {
        super(context, null);
        this.context = context;
        Init();
    }

    public ShufflingVideoPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        Init();
    }

    private void Init() {
        LayoutInflater.from(context).inflate(R.layout.shuffling_viewpager, this, true);
        viewPager = (ViewPager) findViewById(R.id.head_viewPager);
        indicator = (LinearLayout) findViewById(R.id.indicator);
    }

    public void setAdapter(ShufflingVideoAdapter adapter) {
        this.adapter=adapter;
        getList = adapter.getStringList();
        if(getList.size()!=0){
            initViewPager();
            viewPager.setAdapter(adapter);
        }

    }

    public ShufflingVideoAdapter getAdapter() {
        return adapter;
    }


    public void start() {
        if (handler != null && !handler.hasMessages(0)) {
            handler.sendEmptyMessageDelayed(0, 2000);
        }
    }

    private void initViewPager() {

        viewPager.setCurrentItem(5000 * (getList.size()));

        if (mImageView == null) {
            initIndicator();
        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setIndicator(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initIndicator() {
        mImageView = new ImageView[getList.size()];
        for (int i = 0; i < getList.size(); i++) {
            View view = LayoutInflater.from(context).inflate(R.layout.shuffling_indicator, null);
            view.findViewById(R.id.indicator_iamge).setBackgroundResource(R.mipmap.hong_indicator);
            mImageView[i] = new ImageView(context);
            if (i == 0) {
                mImageView[i].setBackgroundResource(R.mipmap.hong_indicator);
            } else {
                mImageView[i].setBackgroundResource(R.mipmap.hui_indicator);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(ScreenDpiUtils.dip2px(context, 5), 0, 0, 0);
                mImageView[i].setLayoutParams(layoutParams);
            }
            indicator.addView(mImageView[i]);
        }
    }

    private void setIndicator(int position) {
        position %= getList.size();
        for (int i = 0; i < getList.size(); i++) {
            mImageView[i].setBackgroundResource(R.mipmap.hong_indicator);
            if (position != i) {
                mImageView[i].setBackgroundResource(R.mipmap.hui_indicator);
            }
        }
    }


}
