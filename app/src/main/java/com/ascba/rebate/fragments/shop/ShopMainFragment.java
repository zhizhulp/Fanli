package com.ascba.rebate.fragments.shop;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.GoodsDetailsActivity;
import com.ascba.rebate.activities.ShopMessageActivity;
import com.ascba.rebate.activities.shop.ShopActivity;
import com.ascba.rebate.activities.supermaket.TypeMarketActivity;
import com.ascba.rebate.adapter.ShopTypeRVAdapter;
import com.ascba.rebate.beans.ShopBaseItem;
import com.ascba.rebate.beans.ShopItemType;
import com.ascba.rebate.beans.TypeWeight;
import com.ascba.rebate.fragments.base.Base2Fragment;
import com.ascba.rebate.utils.UrlEncodeUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.BezierCurveAnimater;
import com.ascba.rebate.view.SuperSwipeRefreshLayout;
import com.ascba.rebate.view.loadmore.CustomLoadMoreView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.yolanda.nohttp.rest.Request;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.chad.library.adapter.base.loadmore.LoadMoreView.STATUS_DEFAULT;

/**
 * 商城
 */

public class ShopMainFragment extends Base2Fragment implements
        SuperSwipeRefreshLayout.OnPullRefreshListener
        , Base2Fragment.Callback {
    private static final int LOAD_MORE_END = 0;
    private static final int LOAD_MORE_ERROR = 1;
    private RecyclerView rv;
    private SuperSwipeRefreshLayout refreshLat;
    private ShopTypeRVAdapter adapter;
    private List<ShopBaseItem> data = new ArrayList<>();
    private List<String> urls = new ArrayList<>();//viewPager数据源
    private RelativeLayout searchHead;//搜索头
    private View searchHeadLine;
    private int mDistanceY = 0;//下拉刷新滑动距离
    private int now_page = 1;
    private int total_page;
    private CustomLoadMoreView loadMoreView;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case LOAD_MORE_END:
                    if (adapter != null) {
                        adapter.loadMoreEnd(false);
                    }

                    break;
                case LOAD_MORE_ERROR:
                    if (adapter != null) {
                        adapter.loadMoreFail();
                    }
                    break;
            }
        }
    };
    private boolean isRefresh = true;//true 下拉刷新 false 上拉加载

    private LinearLayout messageBtn;
    private BezierCurveAnimater bezierCurveAnimater;//加入购物车动画

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
        searchHeadLine = view.findViewById(R.id.homepage_head_view);
        //返回图标
        view.findViewById(R.id.head_ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        /**
         * 消息
         */
        messageBtn = (LinearLayout) view.findViewById(R.id.head_rr);
        messageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShopMessageActivity.startIntent(getActivity());
            }
        });

        rv = ((RecyclerView) view.findViewById(R.id.list_clothes));
        refreshLat = ((SuperSwipeRefreshLayout) view.findViewById(R.id.refresh_layout));
        refreshLat.setOnPullRefreshListener(this);

        ShopActivity a = (ShopActivity) getActivity();
        //初始化加入购物车动画
        bezierCurveAnimater = new BezierCurveAnimater(a, ((RelativeLayout) a.findViewById(R.id.second_rr)), ((ShopActivity) getActivity()).getShopTabs().getImThree());

        rv.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                ShopBaseItem shopBaseItem = data.get(position);
                if (data.size() != 0) {
                    if (shopBaseItem.getItemType() == ShopItemType.TYPE_GOODS) {
                        GoodsDetailsActivity.startIntent(getActivity(), shopBaseItem.getColor());
                    } else if (shopBaseItem.getItemType() == ShopItemType.TYPE_NAVIGATION) {
                        TypeMarketActivity.startIntent(getActivity(), shopBaseItem.getColor());
                    }
                }
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemChildClick(adapter, view, position);
                //加入购物车动画
                if (view.getId() == R.id.goods_list_cart) {
                    ImageView addCart = (ImageView) view;
                    bezierCurveAnimater.addCart(addCart);
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

    private void requestNetwork() {
        Request<JSONObject> request = buildNetRequest(UrlUtils.shop, 0, false);
        request.add("sign", UrlEncodeUtils.createSign(UrlUtils.shop));
        request.add("now_page", now_page);
        executeNetWork(request, "请稍后");
        setCallback(this);
    }

    @Override
    public void onRefresh() {
        if (!isRefresh) {
            isRefresh = true;
        }
        now_page = 1;
        if (data.size() != 0) {
            data.clear();
        }
        requestNetwork();

    }

    private void initLoadMore() {

        if (isRefresh) {
            isRefresh = false;
        }
        if (loadMoreView == null) {
            loadMoreView = new CustomLoadMoreView();
            adapter.setLoadMoreView(loadMoreView);
        }
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (now_page > total_page  && total_page != 0) {
                    handler.sendEmptyMessage(LOAD_MORE_END);
                } else {

                    requestNetwork();
                }
            }
        });


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

    @Override
    public void handle200Data(JSONObject dataObj, String message) {

        refreshLat.setRefreshing(false);
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


    private void getPageCount(JSONObject dataObj) {
        //now_page = dataObj.optInt("now_page");
        total_page = dataObj.optInt("total_page");
        now_page++;
    }

    /**
     * 广告轮播
     *
     * @param dataObj
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
     *
     * @param dataObj
     */
    private void initShoopNave(JSONObject dataObj) {
        //商品导航
        JSONArray goodsAy = dataObj.optJSONArray("mallCategory");
        int weight = TypeWeight.TYPE_SPAN_SIZE_MAX / goodsAy.length();
        if (goodsAy != null && goodsAy.length() != 0) {
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
            //横线
            data.add(new ShopBaseItem(ShopItemType.TYPE_LINE, TypeWeight.TYPE_SPAN_SIZE_60, R.layout.shop_line, 4.0f));
        }
    }

    /**
     * 商品列表
     *
     * @param dataObj
     */
    private void initGoodsList(JSONObject dataObj) {
        JSONArray mallGoodsAy = dataObj.optJSONArray("mallGoods");
        if (mallGoodsAy != null && mallGoodsAy.length() != 0) {
            for (int i = 0; i < mallGoodsAy.length(); i++) {
                JSONObject gObj = mallGoodsAy.optJSONObject(i);
                String id = gObj.optString("id");
                String imgUrl = gObj.optString("img");
                String title = gObj.optString("title");
                String shop_price = gObj.optString("shop_price");
                ShopBaseItem shopBaseItem = new ShopBaseItem(ShopItemType.TYPE_GOODS, TypeWeight.TYPE_SPAN_SIZE_30, R.layout.shop_goods
                        , UrlUtils.baseWebsite + imgUrl, title, "￥" + shop_price, "");
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
        } else {
            adapter.notifyDataSetChanged();
        }


    }

    @Override
    public void handleReqFailed() {
        refreshLat.setRefreshing(false);
        handler.sendEmptyMessage(LOAD_MORE_ERROR);
    }

    @Override
    public void handle404(String message) {
        getDm().buildAlertDialog(message);
    }

    @Override
    public void handleReLogin() {
        refreshLat.setRefreshing(false);
    }

    @Override
    public void handleNoNetWork() {

        refreshLat.setRefreshing(false);
        handler.sendEmptyMessage(LOAD_MORE_ERROR);
        getDm().buildAlertDialog(getActivity().getResources().getString(R.string.no_network));
    }
}