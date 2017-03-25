package com.ascba.rebate.fragments.shop;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.BeginnerGuideActivity;
import com.ascba.rebate.activities.GoodsDetailsActivity;
import com.ascba.rebate.activities.GoodsListActivity;
import com.ascba.rebate.activities.clothes.TypeClothActivity;
import com.ascba.rebate.activities.milk.TypeMilkActivity;
import com.ascba.rebate.activities.supermaket.TypeMarketActivity;
import com.ascba.rebate.adapter.ShopTypeRVAdapter;
import com.ascba.rebate.beans.ShopBaseItem;
import com.ascba.rebate.beans.ShopItemType;
import com.ascba.rebate.beans.TypeWeight;
import com.ascba.rebate.view.SuperSwipeRefreshLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 商城
 */

public class ShopMainFragment extends Fragment implements SuperSwipeRefreshLayout.OnPullRefreshListener {
    private RecyclerView rv;
    private SuperSwipeRefreshLayout refreshLat;
    private ShopTypeRVAdapter adapter;
    private List<ShopBaseItem> data;
    private List<String> urls;//viewPager数据源
    private List<String> titles;
    private List<String> contents;
    private List<String> descs;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    private List<String> navUrls;//导航栏图片链接
    private List<String> navStr;//导航栏文字
    private RelativeLayout searchHead;//搜索头
    private int mDistanceY = 0;//下拉刷新滑动距离

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.third_fragment, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }

    private void initViews(View view) {
        searchHead = (RelativeLayout) view.findViewById(R.id.head_search_rr);
        //返回图标
        view.findViewById(R.id.head_ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        rv = ((RecyclerView) view.findViewById(R.id.list_clothes));
        refreshLat = ((SuperSwipeRefreshLayout) view.findViewById(R.id.refresh_layout));
        refreshLat.setOnPullRefreshListener(this);
        initData();
        adapter = new ShopTypeRVAdapter(data, getActivity());
        final GridLayoutManager manager = new GridLayoutManager(getActivity(), TypeWeight.TYPE_SPAN_SIZE_MAX);
        rv.setLayoutManager(manager);
        adapter.setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
            @Override
            public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                return data.get(position).getSpanSize();
            }
        });
        rv.setAdapter(adapter);
        rv.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                Log.d(TAG, "position:" + position);
                if (position == 1) {
                    Intent intent = new Intent(getContext(), TypeClothActivity.class);
                    startActivity(intent);
                } else if (position == 2) {
                    Intent intent = new Intent(getContext(), TypeMarketActivity.class);
                    startActivity(intent);
                } else if (position == 3) {
                    Intent intent = new Intent(getContext(), TypeMilkActivity.class);
                    startActivity(intent);
                } else if (position == 7) {
                    Intent intent = new Intent(getContext(), GoodsDetailsActivity.class);
                    startActivity(intent);
                } else if (position == 8) {
                    Intent intent = new Intent(getContext(), BeginnerGuideActivity.class);
                    startActivity(intent);
                } else if (position == 9) {
                    Intent intent = new Intent(getContext(), GoodsListActivity.class);
                    startActivity(intent);
                }
            }
        });

        /**
         * 滑动标题栏渐变
         */
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                //滑动的距离
                mDistanceY += dy;
                //toolbar的高度
                int toolbarHeight = searchHead.getBottom();

                //当滑动的距离 <= toolbar高度的时候，改变Toolbar背景色的透明度，达到渐变的效果
                if (mDistanceY <= toolbarHeight) {
                    float scale = (float) mDistanceY / toolbarHeight;
                    float alpha = scale * 255;
                    searchHead.setBackgroundColor(Color.argb((int) alpha, 255, 255, 255));
                } else {
                    //将标题栏的颜色设置为完全不透明状态
                    searchHead.setBackgroundResource(R.color.white);
                }
            }
        });
    }

    private void initData() {
        data = new ArrayList<>();
        //viewPager
        intPagerData();
        data.add(new ShopBaseItem(ShopItemType.TYPE_PAGER, TypeWeight.TYPE_SPAN_SIZE_60, R.layout.shop_pager, urls));
        //导航栏
        initNavData();
        for (int i = 0; i < navUrls.size(); i++) {
            data.add(new ShopBaseItem(ShopItemType.TYPE_NAVIGATION, TypeWeight.TYPE_SPAN_SIZE_12, R.layout.shop_navigation,
                    navUrls.get(i), navStr.get(i)));
        }
        //横线
        data.add(new ShopBaseItem(ShopItemType.TYPE_LINE, TypeWeight.TYPE_SPAN_SIZE_60, R.layout.shop_line, 0.5f));
//        //广告图(一张)
//        data.add(new ShopBaseItem(ShopItemType.TYPE_IMG, TypeWeight.TYPE_SPAN_SIZE_60, R.layout.shop_img,
//                "http://image18-c.poco.cn/mypoco/myphoto/20170301/17/18505011120170301174703033_640.jpg"));
//        //头条
//        data.add(new ShopBaseItem(ShopItemType.TYPE_HOT, TypeWeight.TYPE_SPAN_SIZE_60, R.layout.shop_hot,
//                "新手返福利，专享188元大礼包"));
//
//        //横线
//        data.add(new ShopBaseItem(ShopItemType.TYPE_LINE, TypeWeight.TYPE_SPAN_SIZE_60, R.layout.shop_line, 0.5f));
//        //限量抢购
//        List<String> titleList = new ArrayList<>();
//        titleList.add("限量抢购");
//        titleList.add("天天特价");
//        titleList.add("品牌精选");
//
//        List<String> contentList = new ArrayList<>();
//        contentList.add("Miss Dior迪奥香水 国际大牌");
//        contentList.add("每天都有大收获");
//        contentList.add("鞋服特卖 种类齐全");
//
//        List<String> descList = new ArrayList<>();
//        descList.add("仅剩三件");
//        descList.add("全场五折");
//        descList.add("首发价99元");
//
//        List<String> pagerUrls = new ArrayList<>();
//        pagerUrls.add("http://image18-c.poco.cn/mypoco/myphoto/20170309/11/18505011120170309114124067_640.jpg");
//        pagerUrls.add("http://image18-c.poco.cn/mypoco/myphoto/20170309/11/18505011120170309114124067_640.jpg");
//        pagerUrls.add("http://image18-c.poco.cn/mypoco/myphoto/20170309/11/18505011120170309114124067_640.jpg");
//
//        data.add(new ShopBaseItem(ShopItemType.TYPE_RUSH, TypeWeight.TYPE_SPAN_SIZE_60, R.layout.shop_rush, titleList, contentList, descList, pagerUrls));
//        //横线
//        data.add(new ShopBaseItem(ShopItemType.TYPE_LINE, TypeWeight.TYPE_SPAN_SIZE_60, R.layout.shop_line, 0.5f));
//
//        //今日更新
//        data.add(new ShopBaseItem(ShopItemType.TYPE_OTHER, TypeWeight.TYPE_SPAN_SIZE_15, R.layout.shop_other, "http://image18-c.poco.cn/mypoco/myphoto/20170301/17/18505011120170301175231074_640.jpg",
//                "今日更新", "上新抢先看", 0xffffeeee));
//        data.add(new ShopBaseItem(ShopItemType.TYPE_OTHER, TypeWeight.TYPE_SPAN_SIZE_15, R.layout.shop_other, "http://image18-c.poco.cn/mypoco/myphoto/20170301/17/18505011120170301175231074_640.jpg",
//                "一元购物", "一元购电视", 0xfffffdee));
//        data.add(new ShopBaseItem(ShopItemType.TYPE_OTHER, TypeWeight.TYPE_SPAN_SIZE_15, R.layout.shop_other, "http://image18-c.poco.cn/mypoco/myphoto/20170301/17/18505011120170301175231074_640.jpg",
//                "每日十件", "不将就 要好用", 0xffecf9fe));
//        data.add(new ShopBaseItem(ShopItemType.TYPE_OTHER, TypeWeight.TYPE_SPAN_SIZE_15, R.layout.shop_other, "http://image18-c.poco.cn/mypoco/myphoto/20170301/17/18505011120170301175231074_640.jpg",
//                "代金券购", "购券赢好礼", 0xfffff9ee));
//        //横线
//        data.add(new ShopBaseItem(ShopItemType.TYPE_LINE, TypeWeight.TYPE_SPAN_SIZE_60, R.layout.shop_line, 9.0f));
//        //标题栏
//        data.add(new ShopBaseItem(ShopItemType.TYPE_TITLE, TypeWeight.TYPE_SPAN_SIZE_60, R.layout.shop_title,
//                "http://image18-c.poco.cn/mypoco/myphoto/20170302/10/18505011120170302105506050_640.jpg",
//                "精品推荐", 0xff000000));
//        //横线
//        data.add(new ShopBaseItem(ShopItemType.TYPE_LINE, TypeWeight.TYPE_SPAN_SIZE_60, R.layout.shop_line, 0.5f));
        //商品
        for (int i = 0; i < 8; i++) {
            data.add(new ShopBaseItem(ShopItemType.TYPE_GOODS, TypeWeight.TYPE_SPAN_SIZE_30, R.layout.shop_goods
                    , "http://image18-c.poco.cn/mypoco/myphoto/20170301/16/18505011120170301161107098_640.jpg", "拉菲庄园2009珍酿原装进口红酒艾格力古堡干红葡", "￥ 498.00", "已售4件"));
        }
    }


    private void intPagerData() {
        urls = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            urls.add("http://image18-c.poco.cn/mypoco/myphoto/20170301/16/18505011120170301161128072_640.jpg");
        }
    }

    private void initNavData() {
        navUrls = new ArrayList<>();
        navStr = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            navUrls.add("http://image18-c.poco.cn/mypoco/myphoto/20170302/09/18505011120170302094130032_640.jpg");
            navStr.add("导航" + i);
        }

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
        //隐藏搜索栏
        if (distance > 0) {
            searchHead.setVisibility(View.GONE);
        } else {
            searchHead.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPullEnable(boolean enable) {

    }

}