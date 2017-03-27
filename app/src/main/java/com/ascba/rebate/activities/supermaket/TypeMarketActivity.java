package com.ascba.rebate.activities.supermaket;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWork4Activity;
import com.ascba.rebate.adapter.ShopTypeRVAdapter;
import com.ascba.rebate.beans.ShopBaseItem;
import com.ascba.rebate.beans.ShopItemType;
import com.ascba.rebate.beans.TypeWeight;
import com.ascba.rebate.utils.UrlEncodeUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.ShopABar;
import com.ascba.rebate.view.SuperSwipeRefreshLayout;
import com.ascba.rebate.view.loadmore.CustomLoadMoreView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yolanda.nohttp.rest.Request;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TypeMarketActivity extends BaseNetWork4Activity implements
        SuperSwipeRefreshLayout.OnPullRefreshListener, ShopABar.Callback, BaseNetWork4Activity.Callback {

    private RecyclerView rv;
    private SuperSwipeRefreshLayout refreshLat;
    private ShopTypeRVAdapter adapter;
    private List<ShopBaseItem> data;
    private List<String> urls;//viewPager数据源

    private ShopABar sab;
    private List<String> navUrls;//导航栏图片链接
    private List<String> navStr;//导航栏文字
    private int categoryId = 1327;

    private static final int LOAD_MORE_END = 0;
    private int now_page = 1;
    private int total_page;
    private CustomLoadMoreView loadMoreView;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case LOAD_MORE_END:
                    adapter.loadMoreEnd(false);
                    break;
            }
        }
    };
    private boolean isRefresh = true;//true 下拉刷新 false 上拉加载

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_maket);
        initViews();
        requestNetwork();
    }

    public static void startIntent(Context context, int id) {
        Intent intent = new Intent(context, TypeMarketActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }

    private void getIntentId() {
        Intent intent = getIntent();
        if (intent != null) {
            categoryId = intent.getIntExtra("id", 0);
        }
    }

    private void initViews() {
        rv = ((RecyclerView) findViewById(R.id.list_market));
        refreshLat = ((SuperSwipeRefreshLayout) findViewById(R.id.refresh_layout));
        refreshLat.setOnPullRefreshListener(this);
        initData();
        adapter = new ShopTypeRVAdapter(data, this);
        final GridLayoutManager manager = new GridLayoutManager(this, TypeWeight.TYPE_SPAN_SIZE_MAX);
        rv.setLayoutManager(manager);
        adapter.setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
            @Override
            public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                return data.get(position).getSpanSize();
            }
        });
        rv.setAdapter(adapter);

        sab = ((ShopABar) findViewById(R.id.sab));
        sab.setCallback(this);
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
        //广告图(一张)
        data.add(new ShopBaseItem(ShopItemType.TYPE_IMG, TypeWeight.TYPE_SPAN_SIZE_60, R.layout.shop_img,
                "http://image18-c.poco.cn/mypoco/myphoto/20170301/17/18505011120170301174703033_640.jpg"));
        //横线
        data.add(new ShopBaseItem(ShopItemType.TYPE_LINE, TypeWeight.TYPE_SPAN_SIZE_60, R.layout.shop_line, 10));
        //标题栏
        data.add(new ShopBaseItem(ShopItemType.TYPE_TITLE, TypeWeight.TYPE_SPAN_SIZE_60, R.layout.shop_title,
                "http://image18-c.poco.cn/mypoco/myphoto/20170302/10/18505011120170302105506050_640.jpg",
                "精品推荐", 0xff000000));
        //横线
        data.add(new ShopBaseItem(ShopItemType.TYPE_LINE, TypeWeight.TYPE_SPAN_SIZE_60, R.layout.shop_line, 0.5f));
        //商品1
        data.add(new ShopBaseItem(ShopItemType.TYPE_GOODS_STYLE2, TypeWeight.TYPE_SPAN_SIZE_20, R.layout.shop_goods_style2, "http://image18-c.poco.cn/mypoco/myphoto/20170302/14/18505011120170302145124095_640.jpg?495x418_130",
                "拉菲庄园2009珍酿原装进口红酒艾格力古堡干红葡萄酒", "￥ 498.00", "￥ 698.00"));
        data.add(new ShopBaseItem(ShopItemType.TYPE_GOODS_STYLE2, TypeWeight.TYPE_SPAN_SIZE_20, R.layout.shop_goods_style2, "http://image18-c.poco.cn/mypoco/myphoto/20170302/14/18505011120170302145124095_640.jpg?495x418_130",
                "拉菲庄园2009珍酿原装进口红酒艾格力古堡干红葡萄酒", "￥ 498.00", "￥ 698.00"));
        data.add(new ShopBaseItem(ShopItemType.TYPE_GOODS_STYLE2, TypeWeight.TYPE_SPAN_SIZE_20, R.layout.shop_goods_style2, "http://image18-c.poco.cn/mypoco/myphoto/20170302/14/18505011120170302145124095_640.jpg?495x418_130",
                "拉菲庄园2009珍酿原装进口红酒艾格力古堡干红葡萄酒", "￥ 498.00", "￥ 698.00"));
        //横线
        data.add(new ShopBaseItem(ShopItemType.TYPE_LINE, TypeWeight.TYPE_SPAN_SIZE_60, R.layout.shop_line, 10));
        //商品2
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

    }

    @Override
    public void onPullEnable(boolean enable) {

    }

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

    private void requestNetwork() {
        Request<JSONObject> request = buildNetRequest(UrlUtils.shop, 0, false);
        request.add("sign", UrlEncodeUtils.createSign(UrlUtils.shop));
        request.add("now_page", now_page);
        request.add("category_id", categoryId);
        executeNetWork(request, "请稍后");
        setCallback(this);
    }

    @Override
    public void handle200Data(JSONObject dataObj, String message) {

    }

    @Override
    public void handle404(String message) {

    }

    @Override
    public void handleNoNetWork() {

    }
}
