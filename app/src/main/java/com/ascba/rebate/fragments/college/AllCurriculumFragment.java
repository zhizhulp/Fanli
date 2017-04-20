package com.ascba.rebate.fragments.college;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.CollegeEnrollmentWorkActivity;
import com.ascba.rebate.adapter.CurriculumAdapter;
import com.ascba.rebate.beans.CurriculumBean;
import com.ascba.rebate.fragments.base.BaseFragment;
import com.ascba.rebate.view.SuperSwipeRefreshLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李鹏 on 2017/03/15 0015.
 * 全部课程
 */

public class AllCurriculumFragment extends BaseFragment implements SuperSwipeRefreshLayout.OnPullRefreshListener {


    public AllCurriculumFragment() {
    }

    private SuperSwipeRefreshLayout refreshLat;
    private Handler handler = new Handler();
    private Context context;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_all_curriculum, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();
        initView(view);
    }

    private void initView(View view) {
        //刷新
        refreshLat = (SuperSwipeRefreshLayout) view.findViewById(R.id.refresh_layout);
        View footView = LayoutInflater.from(context).inflate(R.layout.foot_view, null);
        refreshLat.setFooterView(footView);
        refreshLat.setOnPullRefreshListener(this);

        //recylerview
        recyclerView = (RecyclerView) view.findViewById(R.id.recylerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        CurriculumAdapter adapter = new CurriculumAdapter(initRecyclerViewData(), context);

        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                showToast("position" + position);
                switch (position) {
                    case 2:
                        Intent enrollment = new Intent(context, CollegeEnrollmentWorkActivity.class);
                        startActivity(enrollment);
                        break;
                }
            }
        });
    }


    private List<CurriculumBean> initRecyclerViewData() {
        List<CurriculumBean> beanList = new ArrayList<>();

        //广告轮播

        List<String> urls = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            urls.add("http://image18-c.poco.cn/mypoco/myphoto/20170316/11/18505011120170316110628088_640.jpg");
        }
        beanList.add(new CurriculumBean(CurriculumAdapter.TYPE0, R.layout.shop_pager, urls));

        beanList.add(new CurriculumBean(CurriculumAdapter.TYPE1, R.layout.curriculum_title, "即将开始", false));

        String img1 = "http://image18-c.poco.cn/mypoco/myphoto/20170316/11/18505011120170316110646062_640.jpg";
        String title1 = "第三期爱思克初级班火热";
        String state1 = "报名中";
        beanList.add(new CurriculumBean(CurriculumAdapter.TYPE2, R.layout.curriculum_content, img1, title1, state1));

        String img2 = "http://image18-c.poco.cn/mypoco/myphoto/20170316/11/18505011120170316110703079_640.jpg";
        String title2 = "第二期爱思克初级班";
        String state2 = "报名已截止";
        beanList.add(new CurriculumBean(CurriculumAdapter.TYPE2, R.layout.curriculum_content, img2, title2, state2));

        beanList.add(new CurriculumBean(CurriculumAdapter.TYPE1, R.layout.curriculum_title, "正在进行", false));

        String img3 = "http://image18-c.poco.cn/mypoco/myphoto/20170316/11/18505011120170316110722015_640.jpg";
        String title3 = "第三期爱思克初级班火热";
        String state3 = "正在进行中";
        beanList.add(new CurriculumBean(CurriculumAdapter.TYPE2, R.layout.curriculum_content, img3, title3, state3));

        beanList.add(new CurriculumBean(CurriculumAdapter.TYPE1, R.layout.curriculum_title, "往期回顾", true));

        String img4 = "http://image18-c.poco.cn/mypoco/myphoto/20170316/11/18505011120170316110739017_640.jpg";
        String title4 = "第二期爱思克初级班";
        String state4 = "已参与";
        beanList.add(new CurriculumBean(CurriculumAdapter.TYPE2, R.layout.curriculum_content, img4, title4, state4));

        return beanList;
    }

    @Override
    public void onRefresh() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshLat.setRefreshing(false);
            }
        }, 1000);
    }

    @Override
    public void onPullDistance(int distance) {

    }

    @Override
    public void onPullEnable(boolean enable) {

    }


}