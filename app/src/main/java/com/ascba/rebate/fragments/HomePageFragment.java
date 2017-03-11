package com.ascba.rebate.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.fragments.base.BaseFragment;
import com.ascba.rebate.view.SuperSwipeRefreshLayout;
import com.ascba.rebate.view.pagerWithTurn.ShufflingViewPager;
import com.ascba.rebate.view.pagerWithTurn.ShufflingViewPagerAdapter;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李鹏 on 2017/03/10 0010.
 */

public class HomePageFragment extends BaseFragment implements View.OnClickListener {

    private ShufflingViewPager pager_news;
    private List<String> navUrls;//导航栏图片链接
    private Context context;
    private Button speedMoney, makeMoney;//花钱、赚钱
    private LinearLayout btnCollege;//ASK商学院
    private LinearLayout btnPolicy;//创业扶持
    private RelativeLayout btnShop;//券购商城更多

    /**
     * 全球券购
     */
    private LinearLayout btnGlobal;
    private TextView btnGlobalText1, btnGlobalText2;
    private ImageView btnGlobalImg;

    /**
     * 天天特价
     */
    private LinearLayout btnOffer;
    private TextView btnOfferText1, btnOfferText2;
    private ImageView btnOfferImg;

    /**
     * 品牌精选
     */
    private LinearLayout btnSelected;
    private TextView btnSelectedText1, btnSelectedText2;
    private ImageView btnSelectedImg;

    private RelativeLayout btnInformation;//ASK资讯更多
    private ShufflingViewPager pagerVideo;//ASK视频
    private RelativeLayout btnDynamic;//最新动态更多
    private RecyclerView recylerview;
    private SuperSwipeRefreshLayout refreshLayout;//刷新

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_homepage, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();
        InitViews(view);
    }

    private void InitViews(View view) {
        /**
         * 轮播广告
         */
        initNavData();
        pager_news = (ShufflingViewPager) view.findViewById(R.id.homepage_pager);
        ShufflingViewPagerAdapter adapter = new ShufflingViewPagerAdapter(context, navUrls);
        pager_news.setAdapter(adapter);
        pager_news.start();

        /**
         * 花钱  赚钱
         */
        speedMoney = (Button) view.findViewById(R.id.homepage_btn_speedmon);
        speedMoney.setOnClickListener(this);
        makeMoney = (Button) view.findViewById(R.id.homepage_btn_makemon);
        makeMoney.setOnClickListener(this);

        /**
         * ASK商学院   创业扶持
         */
        btnCollege = (LinearLayout) view.findViewById(R.id.homepage_btn_college);
        btnCollege.setOnClickListener(this);
        btnPolicy = (LinearLayout) view.findViewById(R.id.homepage_btn_policy);
        btnPolicy.setOnClickListener(this);

        /**
         * 券购商城更多
         */
        btnShop = (RelativeLayout) view.findViewById(R.id.homepage_btn_shop);
        btnShop.setOnClickListener(this);

        /**
         * 全球券购
         */
        btnGlobal = (LinearLayout) view.findViewById(R.id.homepage_btn_global);
        btnGlobal.setOnClickListener(this);
        btnGlobalText1 = (TextView) view.findViewById(R.id.homepage_btn_global_text1);
        btnGlobalText2 = (TextView) view.findViewById(R.id.homepage_btn_global_text2);
        btnGlobalImg = (ImageView) view.findViewById(R.id.homepage_btn_global_img);
        Glide.with(context).load("http://image18-c.poco.cn/mypoco/myphoto/20170309/11/18505011120170309114124067_640.jpg").into(btnGlobalImg);

        /**
         * 天天特价
         */
        btnOffer = (LinearLayout) view.findViewById(R.id.homepage_btn_offer);
        btnOffer.setOnClickListener(this);
        btnOfferText1 = (TextView) view.findViewById(R.id.homepage_btn_offer_text1);
        btnOfferText2 = (TextView) view.findViewById(R.id.homepage_btn_offer_text2);
        btnOfferImg = (ImageView) view.findViewById(R.id.homepage_btn_offer_img);
        Glide.with(context).load("http://image18-c.poco.cn/mypoco/myphoto/20170309/11/18505011120170309114124067_640.jpg").into(btnOfferImg);

        /**
         * 品牌精选
         */
        btnSelected = (LinearLayout) view.findViewById(R.id.homepage_btn_selected);
        btnSelected.setOnClickListener(this);
        btnSelectedText1 = (TextView) view.findViewById(R.id.homepage_btn_selected_text1);
        btnSelectedText2 = (TextView) view.findViewById(R.id.homepage_btn_selected_text2);
        btnSelectedImg = (ImageView) view.findViewById(R.id.homepage_btn_selected_img);
        Glide.with(context).load("http://image18-c.poco.cn/mypoco/myphoto/20170309/11/18505011120170309114124067_640.jpg").into(btnSelectedImg);

        /**
         * ASK资讯
         */
        btnInformation = (RelativeLayout) view.findViewById(R.id.homepage_btn_information);
        btnInformation.setOnClickListener(this);
        pagerVideo = (ShufflingViewPager) view.findViewById(R.id.homepage_video);

        /**
         * 最新动态
         */
        btnDynamic = (RelativeLayout) view.findViewById(R.id.homepage_btn_dynamic);
        btnDynamic.setOnClickListener(this);
        recylerview = (RecyclerView) view.findViewById(R.id.homepage_recylerview);
        initRecylerviewData();
        /**
         * 刷新
         */
        refreshLayout = (SuperSwipeRefreshLayout) view.findViewById(R.id.refresh_layout);
    }

    /**
     * 轮播广告数据
     */
    private void initNavData() {
        navUrls = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            navUrls.add("http://image18-c.poco.cn/mypoco/myphoto/20170301/16/18505011120170301161128072_640.jpg");
        }
    }

    /**
     * recylerview数据
     */
    private void initRecylerviewData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.homepage_btn_speedmon:
                break;
            case R.id.homepage_btn_makemon:
                break;
            case R.id.homepage_btn_college:
                break;
        }
    }
}
