package com.ascba.rebate.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWork4Activity;
import com.ascba.rebate.adapter.GoodsDetailsAdapter;
import com.ascba.rebate.beans.GoodsDetailsItem;
import com.ascba.rebate.beans.GoodsFootprint;
import com.ascba.rebate.view.dropDownMultiPager.DropDownMultiPagerView;
import com.ascba.rebate.view.dropDownMultiPager.ultraPullToRefash.component.PtrFrameLayout;
import com.ascba.rebate.view.dropDownMultiPager.ultraPullToRefash.handler.PtrDefaultHandler;
import com.ascba.rebate.view.pullUpToLoadMoreView.PullUpToLoadMoreView;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李鹏 on 2017/03/02 0002.
 * 商品详情页
 */
@SuppressLint("SetTextI18n")
public class GoodsDetailsActivity extends BaseNetWork4Activity {

    //足记控件
    private PtrFrameLayout ptrLayout;

    //向上拖动查看详情
    private PullUpToLoadMoreView pullUpToLoadMoreView;


    private RecyclerView recyclerView;

    private Context context;

    private GoodsDetailsAdapter adapter;

    private List<GoodsDetailsItem> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_details);
        context = this;

        InitFotoplace();
        InitPull();

        InitRecyclerView();
    }

    /**
     * 初始化滑动详情控件
     */
    private void InitPull() {
        pullUpToLoadMoreView = (PullUpToLoadMoreView) findViewById(R.id.activity_goods_details_pv);

        /**
         * 获取当前控件位置
         */
        pullUpToLoadMoreView.setOnCurrPosition(new PullUpToLoadMoreView.OnCurrPosition() {
            @Override
            public void currPosition(int currPosition, boolean isTop) {

                if (currPosition == 0 && isTop) {
                    ptrLayout.setEnabled(true);
                } else {
                    ptrLayout.setEnabled(false);
                }
            }
        });
    }


    /**
     * 初始化足迹控件
     */
    private void InitFotoplace() {
        ptrLayout = (PtrFrameLayout) findViewById(R.id.activity_goods_details_pl);
        final TextView textView = new TextView(this);
        textView.setText("下拉查看浏览足迹");
        ptrLayout.setHeaderView(textView);
        ptrLayout.disableWhenHorizontalMove(true);
        ptrLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return true;
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                ptrLayout.refreshComplete();
                DropDownMultiPagerView dropDownMultiPagerView = new DropDownMultiPagerView(context, getList());
                dropDownMultiPagerView.show();
                dropDownMultiPagerView.setOnDropDownMultiPagerViewItemClick(new DropDownMultiPagerView.OnDropDownMultiPagerViewItemClick() {
                    @Override
                    public void onItemClick(int position) {
                        Toast.makeText(context, "position:" + position, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private List<GoodsFootprint> getList() {
        List<GoodsFootprint> beanList = new ArrayList<>();
        String url = "http://img5.ph.126.net/6NHiP2WgCjVnd42P3BWFeQ==/2612932208822079285.jpg";
        for (int i = 0; i < 6; i++) {
            GoodsFootprint bean = new GoodsFootprint("￥ 399", url, "女士上衣" + i, "url地址：" + i);
            beanList.add(bean);
        }
        return beanList;
    }

    /**
     * 初始化RecyclerView
     */
    private void InitRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.activity_goods_details_recylerView);
        getData();

        adapter = new GoodsDetailsAdapter(data, context);
        final GridLayoutManager manager = new GridLayoutManager(this, GoodsDetailsItem.TYPE_SPAN_SIZE_DEFAULT);
        recyclerView.setLayoutManager(manager);
        adapter.setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
            @Override
            public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                return data.get(position).getSpanSize();
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void getData() {
        data = new ArrayList<>();

        /**
         * 商品轮播
         */
        List<String> url=new ArrayList<>();
        data.add(new GoodsDetailsItem(GoodsDetailsItem.TYPE_VIEWPAGER,R.layout.goods_details_viewpager,url));

        /**
         * 商品简单介绍
         */
        data.add(new GoodsDetailsItem(GoodsDetailsItem.TYPE_GOODS_SIMPLE_DESC,R.layout.goods_details_simple_desc,"【自营店】","NB 530系列男鞋女鞋","复古跑步鞋休闲鞋运动鞋M530CKA","￥455","899￥"));
    }
}
