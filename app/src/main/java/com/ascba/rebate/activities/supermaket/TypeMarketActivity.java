package com.ascba.rebate.activities.supermaket;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.GoodsDetailsActivity;
import com.ascba.rebate.activities.GoodsListActivity;
import com.ascba.rebate.activities.ShopMessageActivity;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.adapter.ShopTypeRVAdapter;
import com.ascba.rebate.beans.ShopBaseItem;
import com.ascba.rebate.beans.ShopItemType;
import com.ascba.rebate.beans.TypeWeight;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.MsgView;
import com.ascba.rebate.view.loadmore.CustomLoadMoreView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.yanzhenjie.nohttp.rest.Request;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.chad.library.adapter.base.loadmore.LoadMoreView.STATUS_DEFAULT;

public class TypeMarketActivity extends BaseNetActivity implements
        SwipeRefreshLayout.OnRefreshListener, BaseNetActivity.Callback {

    private RecyclerView rv;
    private ShopTypeRVAdapter adapter;
    private List<ShopBaseItem> data = new ArrayList<>();
    private List<String> urls = new ArrayList<>();//viewPager数据源
    private int categoryId;
    private static final int LOAD_MORE_ERROR = 1;
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
    private Context context;
    private RelativeLayout searchHead;//搜索头
    private View searchHeadLine;//分割线
    private int mDistanceY = 0;//下拉刷新滑动距离
    private MsgView msgView;
    private String subTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_maket);
        context = this;
        getIntentId();
        initViews();
    }

    public static void startIntent(Context context, int id,String subTitle) {
        Intent intent = new Intent(context, TypeMarketActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("sub_title", subTitle);
        context.startActivity(intent);
    }

    private void getIntentId() {
        Intent intent = getIntent();
        if (intent != null) {
            categoryId = intent.getIntExtra("id", 0);
            subTitle = intent.getStringExtra("sub_title");
        }
    }

    private void initViews() {

        //标题栏
        searchHead = (RelativeLayout) findViewById(R.id.head_search_rr);
        searchHeadLine = findViewById(R.id.homepage_head_view);
        ((TextView) findViewById(R.id.tv_title)).setText(subTitle);

        //返回图标
        findViewById(R.id.head_ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //消息
        findViewById(R.id.head_rr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShopMessageActivity.startIntent(context);
            }
        });
        msgView= (MsgView) findViewById(R.id.head_img_xiaoxi);

        rv = ((RecyclerView) findViewById(R.id.list_market));
        initRefreshLayout();
        refreshLayout.setOnRefreshListener(this);

        rv.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {

                ShopBaseItem shopBaseItem = data.get(position);
                if (data.size() != 0) {
                    if (shopBaseItem.getItemType() == ShopItemType.TYPE_GOODS) {
                        GoodsDetailsActivity.startIntent(TypeMarketActivity.this, shopBaseItem.getColor());
                    } else if (shopBaseItem.getItemType() == ShopItemType.TYPE_NAVIGATION) {

                        TypeMarketActivity.startIntent(TypeMarketActivity.this, shopBaseItem.getColor(),shopBaseItem.getDesc());

                        /*Intent intent = new Intent(TypeMarketActivity.this, GoodsListActivity.class);
                        startActivity(intent);*/
                    }
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
                float maxAlpha = 229.5f;//最大透明度80%
                //当滑动的距离 <= toolbar高度的时候，改变Toolbar背景色的透明度，达到渐变的效果
                if (mDistanceY <= toolbarHeight) {
                    float scale = (float) mDistanceY / toolbarHeight;
                    float alpha = scale * maxAlpha;
                    searchHead.setBackgroundColor(Color.argb((int) alpha, 255, 255, 255));
                    searchHeadLine.setAlpha(alpha);
                }
            }
        });
        requestNetwork();
    }

//    private void initData() {
//        data = new ArrayList<>();
//        //viewPager
//        intPagerData();
//        data.add(new ShopBaseItem(ShopItemType.TYPE_PAGER, TypeWeight.TYPE_SPAN_SIZE_60, R.layout.shop_pager, urls));
//        //导航栏
//        initNavData();
//        for (int i = 0; i < navUrls.size(); i++) {
//            data.add(new ShopBaseItem(ShopItemType.TYPE_NAVIGATION, TypeWeight.TYPE_SPAN_SIZE_12, R.layout.shop_navigation,
//                    navUrls.get(i), navStr.get(i)));
//        }
//        //广告图(一张)
//        data.add(new ShopBaseItem(ShopItemType.TYPE_IMG, TypeWeight.TYPE_SPAN_SIZE_60, R.layout.shop_img,
//                "http://image18-c.poco.cn/mypoco/myphoto/20170301/17/18505011120170301174703033_640.jpg"));
//        //横线
//        data.add(new ShopBaseItem(ShopItemType.TYPE_LINE, TypeWeight.TYPE_SPAN_SIZE_60, R.layout.shop_line, 10));
//        //标题栏
//        data.add(new ShopBaseItem(ShopItemType.TYPE_TITLE, TypeWeight.TYPE_SPAN_SIZE_60, R.layout.shop_title,
//                "http://image18-c.poco.cn/mypoco/myphoto/20170302/10/18505011120170302105506050_640.jpg",
//                "精品推荐", 0xff000000));
//        //横线
//        data.add(new ShopBaseItem(ShopItemType.TYPE_LINE, TypeWeight.TYPE_SPAN_SIZE_60, R.layout.shop_line, 0.5f));
//        //商品1
//        data.add(new ShopBaseItem(ShopItemType.TYPE_GOODS_STYLE2, TypeWeight.TYPE_SPAN_SIZE_20, R.layout.shop_goods_style2, "http://image18-c.poco.cn/mypoco/myphoto/20170302/14/18505011120170302145124095_640.jpg?495x418_130",
//                "拉菲庄园2009珍酿原装进口红酒艾格力古堡干红葡萄酒", "￥ 498.00", "￥ 698.00"));
//        data.add(new ShopBaseItem(ShopItemType.TYPE_GOODS_STYLE2, TypeWeight.TYPE_SPAN_SIZE_20, R.layout.shop_goods_style2, "http://image18-c.poco.cn/mypoco/myphoto/20170302/14/18505011120170302145124095_640.jpg?495x418_130",
//                "拉菲庄园2009珍酿原装进口红酒艾格力古堡干红葡萄酒", "￥ 498.00", "￥ 698.00"));
//        data.add(new ShopBaseItem(ShopItemType.TYPE_GOODS_STYLE2, TypeWeight.TYPE_SPAN_SIZE_20, R.layout.shop_goods_style2, "http://image18-c.poco.cn/mypoco/myphoto/20170302/14/18505011120170302145124095_640.jpg?495x418_130",
//                "拉菲庄园2009珍酿原装进口红酒艾格力古堡干红葡萄酒", "￥ 498.00", "￥ 698.00"));
//        //横线
//        data.add(new ShopBaseItem(ShopItemType.TYPE_LINE, TypeWeight.TYPE_SPAN_SIZE_60, R.layout.shop_line, 10));
//        //商品2
//        for (int i = 0; i < 8; i++) {
//            data.add(new ShopBaseItem(ShopItemType.TYPE_GOODS, TypeWeight.TYPE_SPAN_SIZE_30, R.layout.shop_goods
//                    , "http://image18-c.poco.cn/mypoco/myphoto/20170301/16/18505011120170301161107098_640.jpg", "拉菲庄园2009珍酿原装进口红酒艾格力古堡干红葡", "￥ 498.00", "已售4件"));
//        }
//    }

//    private void intPagerData() {
//        urls = new ArrayList<>();
//        for (int i = 0; i < 3; i++) {
//            urls.add("http://image18-c.poco.cn/mypoco/myphoto/20170301/16/18505011120170301161128072_640.jpg");
//        }
//    }
//
//    private void initNavData() {
//        navUrls = new ArrayList<>();
//        navStr = new ArrayList<>();
//        for (int i = 0; i < 5; i++) {
//            navUrls.add("http://image18-c.poco.cn/mypoco/myphoto/20170302/09/18505011120170302094130032_640.jpg");
//            navStr.add("导航" + i);
//        }
//
//    }


    @Override
    public void onRefresh() {
        if (!isRefresh) {
            isRefresh = true;
        }
        now_page = 1;
        total_page=0;
        if (data.size() != 0) {
            data.clear();
            adapter.notifyDataSetChanged();
        }
        requestNetwork();
    }

    private void requestNetwork() {
        Request<JSONObject> request = buildNetRequest(UrlUtils.category, 0, false);
        request.add("now_page", now_page);
        request.add("category_id", categoryId);
        executeNetWork(request, "请稍后");
        setCallback(this);
    }

    @Override
    public void handle200Data(JSONObject dataObj, String message) {
        refreshLayout.setRefreshing(false);
        if (adapter != null) {
            adapter.loadMoreComplete();
        }
        if (loadMoreView != null) {
            loadMoreView.setLoadMoreStatus(STATUS_DEFAULT);
        }
        //分页
        getPageCount(dataObj);
        if (isRefresh) {//下拉刷新
            if (urls.size() != 0) {
                urls.clear();
            }
            //广告轮播
            initViewpager(dataObj);

            //商城首页导航栏
            initShoopNave(dataObj);

            //商品列表
            initGoodsList(dataObj);

            initadapter();

            initLoadMore();
        } else {//上拉加载
            initGoodsList(dataObj);
        }
    }

    /**
     * 广告轮播
     */
    private void initViewpager(JSONObject dataObj) {
        //轮播数据
        JSONArray pagerArray = dataObj.optJSONArray("banner");
        if (pagerArray != null && pagerArray.length() != 0) {

            for (int i = 0; i < pagerArray.length(); i++) {
                String s = pagerArray.optString(i);
                urls.add(UrlUtils.baseWebsite + s);
            }
            data.add(new ShopBaseItem(ShopItemType.TYPE_PAGER, TypeWeight.TYPE_SPAN_SIZE_60, R.layout.shop_pager, urls));
        }
        //横线
        data.add(new ShopBaseItem(ShopItemType.TYPE_LINE, TypeWeight.TYPE_SPAN_SIZE_60, R.layout.shop_line, 0.5f));
    }


    /**
     * 商城首页导航栏
     */
    private void initShoopNave(JSONObject dataObj) {
        //商品导航
        JSONArray goodsAy = dataObj.optJSONArray("mallCategory");

        if (goodsAy != null && goodsAy.length() != 0) {
            int length = goodsAy.length();
            int weight;
            if(length<10){
                weight = TypeWeight.TYPE_SPAN_SIZE_MAX / length;
            }else {
                weight = TypeWeight.TYPE_SPAN_SIZE_MAX / (length/2);
            }
            for (int i = 0; i < goodsAy.length(); i++) {
                JSONObject gObj = goodsAy.optJSONObject(i);
                String id = gObj.optString("id");
                String cover = gObj.optString("cover");
                String subtitle = gObj.optString("sub_title");

                ShopBaseItem baseItem = new ShopBaseItem(ShopItemType.TYPE_NAVIGATION, weight, R.layout.shop_navigation,
                        UrlUtils.baseWebsite + cover, subtitle);
                baseItem.setColor(Integer.parseInt(id));
                data.add(baseItem);
            }
            data.add(new ShopBaseItem(ShopItemType.TYPE_LINE, TypeWeight.TYPE_SPAN_SIZE_MAX, R.layout.shop_line, 4));
        }
    }

    /**
     * 商品列表
     */
    private void initGoodsList(JSONObject dataObj) {
        JSONArray mallGoodsAy = dataObj.optJSONArray("mallGoods");
        if (mallGoodsAy != null && mallGoodsAy.length() != 0) {
            if(isRefresh){
                data.add(new ShopBaseItem(ShopItemType.TYPE_GUESS,TypeWeight.TYPE_SPAN_SIZE_60,R.layout.shop_title));
                data.add(new ShopBaseItem(ShopItemType.TYPE_LINE, TypeWeight.TYPE_SPAN_SIZE_60, R.layout.shop_line, 1.0f));
            }
            for (int i = 0; i < mallGoodsAy.length(); i++) {
                JSONObject gObj = mallGoodsAy.optJSONObject(i);
                String id = gObj.optString("id");
                String imgUrl = gObj.optString("img");
                String title = gObj.optString("title");
                String shop_price = gObj.optString("shop_price");
                ShopBaseItem shopBaseItem = new ShopBaseItem(ShopItemType.TYPE_GOODS, TypeWeight.TYPE_SPAN_SIZE_30, R.layout.shop_goods
                        , UrlUtils.baseWebsite + imgUrl, title, shop_price, "",false);
                shopBaseItem.setColor(Integer.parseInt(id));
                data.add(shopBaseItem);
            }
        }
    }

    /**
     * 初始化adapter
     */
    private void initadapter() {
        if (adapter == null) {
            adapter = new ShopTypeRVAdapter(data, context);
            final GridLayoutManager manager = new GridLayoutManager(context, TypeWeight.TYPE_SPAN_SIZE_MAX);
            rv.setLayoutManager(manager);
            adapter.setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
                @Override
                public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                    return data.get(position).getSpanSize();
                }
            });
            rv.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void handle404(String message) {
    }


    @Override
    public void handleNoNetWork() {
        refreshLayout.setRefreshing(false);
        handler.sendEmptyMessage(LOAD_MORE_ERROR);
    }

    private void initLoadMore() {
        if (loadMoreView == null) {
            loadMoreView = new CustomLoadMoreView();
            adapter.setLoadMoreView(loadMoreView);
        }
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (isRefresh) {
                    isRefresh = false;
                }
                if (now_page > total_page && total_page != 0) {
                    handler.sendEmptyMessage(LOAD_MORE_END);
                } else if(total_page==0){
                    handler.sendEmptyMessage(LOAD_MORE_END);
                } else {
                    requestNetwork();
                }
            }
        });
    }

    private void getPageCount(JSONObject dataObj) {
        total_page = dataObj.optInt("total_page");
        now_page++;
    }
}
