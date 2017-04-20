package com.ascba.rebate.activities;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWorkActivity;
import com.ascba.rebate.adapter.AllEvaluationAdapter;
import com.ascba.rebate.beans.EvaluationBean;
import com.ascba.rebate.view.ShopABar;
import com.ascba.rebate.view.SuperSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李鹏 on 2017/03/08 0008.
 * 全部评价
 */

public class AllEvaluationWorkActivity extends BaseNetWorkActivity {

    private ShopABar shopABar;
    private RecyclerView recyclerView;
    private Context context;
    private SuperSwipeRefreshLayout refreshLayout;
    private Handler handler = new Handler();
    private AllEvaluationAdapter allEvaluationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_evaluation);
        context = this;
        InitUI();
    }

    private void InitUI() {

        shopABar = (ShopABar) findViewById(R.id.all_evaluation_bar);
        shopABar.setImMsgSta(R.mipmap.tab_shop);
        shopABar.setCallback(new ShopABar.Callback() {
            @Override
            public void back(View v) {
                finish();
            }

            @Override
            public void clkMsg(View v) {

            }

            @Override
            public void clkOther(View v) {

            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.all_evaluation_recylerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        allEvaluationAdapter = new AllEvaluationAdapter(getData(), context);
        recyclerView.setAdapter(allEvaluationAdapter);

        refreshLayout = (SuperSwipeRefreshLayout) findViewById(R.id.all_evaluation_refresh_layout);

        View view= LayoutInflater.from(context).inflate(R.layout.foot_view,null,false);
        refreshLayout.setFooterView(view);

        refreshLayout.setOnPullRefreshListener(new SuperSwipeRefreshLayout.OnPullRefreshListener() {
            @Override
            public void onRefresh() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                    }
                }, 1000);
            }

            @Override
            public void onPullDistance(int distance) {

            }

            @Override
            public void onPullEnable(boolean enable) {

            }
        });

        refreshLayout.setOnPushLoadMoreListener(new SuperSwipeRefreshLayout.OnPushLoadMoreListener() {
            @Override
            public void onLoadMore() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setLoadMore(false);
                    }
                }, 1000);
            }

            @Override
            public void onPushDistance(int distance) {

            }

            @Override
            public void onPushEnable(boolean enable) {

            }
        });
    }

    public List<EvaluationBean> getData() {
        List<EvaluationBean> data = new ArrayList<>();

        String headImg = "http://image18-c.poco.cn/mypoco/myphoto/20170308/16/18505011120170308160548098_640.jpg";

        String[] strings = new String[]{"http://image18-c.poco.cn/mypoco/myphoto/20170303/17/18505011120170303174057035_640.jpg"};
        EvaluationBean evaluationBean = new EvaluationBean(AllEvaluationAdapter.TYPE1,headImg, "153******27", "鞋很好，穿着很舒服", "2017.01.17", "颜色：蓝/灰 尺码：40", strings);
        data.add(evaluationBean);

        String[] strings1 = new String[]{"http://image18-c.poco.cn/mypoco/myphoto/20170303/17/18505011120170303174057035_640.jpg",
                "http://image18-c.poco.cn/mypoco/myphoto/20170303/17/18505011120170303174118033_640.jpg"};
        EvaluationBean evaluationBean1 = new EvaluationBean(AllEvaluationAdapter.TYPE1,headImg, "你好***O(∩_∩)O", "鞋很好，穿着很舒服", "2017.01.17", "颜色：蓝/灰 尺码：40", strings1);
        data.add(evaluationBean1);

        String[] strings2 = new String[]{"http://image18-c.poco.cn/mypoco/myphoto/20170303/17/18505011120170303174057035_640.jpg",
                "http://image18-c.poco.cn/mypoco/myphoto/20170303/17/18505011120170303174118033_640.jpg",
                "http://image18-c.poco.cn/mypoco/myphoto/20170303/18/18505011120170303180121047_640.jpg"};
        EvaluationBean evaluationBean2 = new EvaluationBean(AllEvaluationAdapter.TYPE1,headImg, "离***人", "鞋很好，穿着很舒服鞋很好，穿着很舒服鞋很好，穿着很舒服鞋很好，穿着很舒服鞋很好，穿着很舒服鞋很好，穿着很舒服鞋很好，穿着很舒服鞋很好，穿着很舒服鞋很好，穿着很舒服鞋很好，穿着很舒服", "2017.02.17", "颜色：白色 尺码：42", strings2);
        data.add(evaluationBean2);

        EvaluationBean evaluationBean3=new EvaluationBean(AllEvaluationAdapter.TYPE2);
        data.add(evaluationBean3);
        return data;
    }
}
