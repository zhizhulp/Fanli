package com.ascba.rebate.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWorkActivity;
import com.ascba.rebate.adapter.CurriculumAdapter;
import com.ascba.rebate.adapter.MyEvaluationAdapter;
import com.ascba.rebate.beans.CurriculumBean;
import com.ascba.rebate.view.ShopABarText;
import com.ascba.rebate.view.SuperSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李鹏 on 2017/03/20 0020.
 * 我的课程
 */

public class MyEvaluationWorkActivity extends BaseNetWorkActivity {

    private ShopABarText shopBar;
    private SuperSwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_evaluation);
        context = this;
        initView();
    }

    private void initView() {
        /**
         * bar
         */
        shopBar = (ShopABarText) findViewById(R.id.shopbar);
        shopBar.setBtnEnable(false);
        shopBar.setCallback(new ShopABarText.Callback() {
            @Override
            public void back(View v) {
                finish();
            }

            @Override
            public void clkBtn(View v) {

            }
        });

        refreshLayout = (SuperSwipeRefreshLayout) findViewById(R.id.refresh_layout);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        MyEvaluationAdapter myEvaluationAdapter = new MyEvaluationAdapter(initRecyclerViewData(), context);
        recyclerView.setAdapter(myEvaluationAdapter);
    }

    private List<CurriculumBean> initRecyclerViewData() {
        List<CurriculumBean> beanList = new ArrayList<>();

        beanList.add(new CurriculumBean(CurriculumAdapter.TYPE1, R.layout.curriculum_title1, "即将开始", false));

        String img1 = "http://image18-c.poco.cn/mypoco/myphoto/20170316/11/18505011120170316110646062_640.jpg";
        String title1 = "第三期爱思克初级班火热";
        String state1 = "报名中";
        beanList.add(new CurriculumBean(CurriculumAdapter.TYPE2, R.layout.curriculum_content, img1, title1, state1));

        String img2 = "http://image18-c.poco.cn/mypoco/myphoto/20170316/11/18505011120170316110703079_640.jpg";
        String title2 = "第二期爱思克初级班";
        String state2 = "报名已截止";
        beanList.add(new CurriculumBean(CurriculumAdapter.TYPE2, R.layout.curriculum_content, img2, title2, state2));

        beanList.add(new CurriculumBean(CurriculumAdapter.TYPE1, R.layout.curriculum_title1, "正在进行", false));

        String img3 = "http://image18-c.poco.cn/mypoco/myphoto/20170316/11/18505011120170316110722015_640.jpg";
        String title3 = "第三期爱思克初级班火热";
        String state3 = "正在进行中";
        beanList.add(new CurriculumBean(CurriculumAdapter.TYPE2, R.layout.curriculum_content, img3, title3, state3));

        beanList.add(new CurriculumBean(CurriculumAdapter.TYPE1, R.layout.curriculum_title1, "往期回顾", true));

        String img4 = "http://image18-c.poco.cn/mypoco/myphoto/20170316/11/18505011120170316110739017_640.jpg";
        String title4 = "第二期爱思克初级班";
        String state4 = "已参与";
        beanList.add(new CurriculumBean(CurriculumAdapter.TYPE2, R.layout.curriculum_content, img4, title4, state4));

        return beanList;
    }
}
