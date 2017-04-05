package com.ascba.rebate.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWork4Activity;
import com.ascba.rebate.activities.login.LoginActivity;
import com.ascba.rebate.adapter.BusinessShopAdapter;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.beans.ShopBaseItem;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.utils.ViewUtils;
import com.ascba.rebate.view.ShopABar;
import com.ascba.rebate.view.SuperSwipeRefreshLayout;
import com.ascba.rebate.view.loadmore.CustomLoadMoreView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.squareup.picasso.Picasso;
import com.yolanda.nohttp.rest.Request;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.chad.library.adapter.base.loadmore.LoadMoreView.STATUS_DEFAULT;


/**
 * Created by 李鹏 on 2017/03/15 0015.
 * 商家店铺
 */

public class BusinessShopActivity extends BaseNetWork4Activity implements
        SuperSwipeRefreshLayout.OnPullRefreshListener
        , BaseNetWork4Activity.Callback {

    private SuperSwipeRefreshLayout refreshLat;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case LOAD_MORE_END:
                    adapter.loadMoreEnd(false);
                    break;
                case LOAD_MORE_ERROR:
                    if (adapter != null) {
                        adapter.loadMoreFail();
                    }
                    break;
            }
        }
    };
    private Context context;
    private ShopABar shopABar;
    private RecyclerView recyclerView;
    private int store_id;
    private static final int REQUEST_LOGIN = 0;
    private BusinessShopAdapter adapter;
    private int now_page = 1;
    private int total_page;
    private View headView;
    private ImageView backImg;
    private ImageView headImg;
    private TextView tvShopName;
    private List<ShopBaseItem> goodsList = new ArrayList<>();
    private CustomLoadMoreView loadMoreView;
    private boolean isRefresh = true;//true 下拉刷新 false 上拉加载

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_shop);
        context = this;
        initView();
        isLogin();//此页面需要登录
    }

    private void isLogin() {
        if (AppConfig.getInstance().getInt("uuid", -1000) != -1000) {
            getStoreFromIntent();
            requestData(UrlUtils.getStore);
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, REQUEST_LOGIN);
        }
    }

    private void getStoreFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            store_id = intent.getIntExtra("store_id", -1000);
        }
    }

    private void requestData(String url) {
        Request<JSONObject> request = buildNetRequest(url, 0, true);
        request.add("store_id", store_id);
        executeNetWork(request, "请稍后");
        setCallback(this);
    }

    private void initView() {
        //刷新
        refreshLat = (SuperSwipeRefreshLayout) findViewById(R.id.refresh_layout);
        refreshLat.setOnPullRefreshListener(this);

        //导航栏
        shopABar = (ShopABar) findViewById(R.id.shopbar);
        shopABar.setImageOther(R.mipmap.icon_cart_black);
        shopABar.setCallback(new ShopABar.Callback() {
            @Override
            public void back(View v) {
                finish();
            }

            @Override
            public void clkMsg(View v) {
                //消息中心
                ShopMessageActivity.startIntent(context);
            }

            @Override
            public void clkOther(View v) {
                Toast.makeText(context, "购物车", Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recylerview);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));

        //头布局
        headView = LayoutInflater.from(context).inflate(R.layout.business_shop_head, null);
        backImg = (ImageView) headView.findViewById(R.id.shop_img);
        tvShopName = ((TextView) headView.findViewById(R.id.shop_name));
        headImg = (ImageView) headView.findViewById(R.id.shop_img_head);


    }


    @Override
    public void onRefresh() {
        if (!isRefresh) {
            isRefresh = true;
        }
        clearData();
        resetPage();
        requestData(UrlUtils.getStore);
    }

    @Override
    public void onPullDistance(int distance) {

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
        getPageCount(dataObj);//分页数据
        if (isRefresh) {//下拉刷新
            resetPage();
            clearData();
            refreshHeadData(dataObj);//头部数据
            refreshGoodsData(dataObj);//商品列表数据
        } else {//上拉加载
            refreshGoodsData(dataObj);//商品列表数据
        }
        initAdapterAndRefresh();
        initLoadMore();
    }

    private void initLoadMore() {
        if (isRefresh) {
            isRefresh = false;
        }
        if (loadMoreView == null) {
            loadMoreView = new CustomLoadMoreView();
            adapter.setLoadMoreView(loadMoreView);
            adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                @Override
                public void onLoadMoreRequested() {
                    if (now_page > total_page - 1 && total_page != 0) {
                        handler.sendEmptyMessage(LOAD_MORE_END);
                    } else {
                        requestData(UrlUtils.getStore);
                    }
                }
            });
        }
    }

    private void initAdapterAndRefresh() {
        if (adapter == null) {
            adapter = new BusinessShopAdapter(R.layout.shop_goods, goodsList, context);
            adapter.setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
                @Override
                public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                    return 1;
                }
            });
            adapter.addHeaderView(headView);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }

    }

    private void refreshGoodsData(JSONObject dataObj) {
        JSONArray array = dataObj.optJSONArray("mallGoods");
        if (array == null || array.length() == 0) {
            adapter.setEmptyView(ViewUtils.getEmptyView(this, "暂无商品数据"));
        } else {
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.optJSONObject(i);
                int id = obj.optInt("id");
                String title = obj.optString("title");
                String img = obj.optString("img");
                String shop_price = obj.optString("shop_price");
                goodsList.add(new ShopBaseItem(UrlUtils.baseWebsite + img, title, "￥ " + shop_price, "", id));
            }
        }
    }

    private void refreshHeadData(JSONObject dataObj) {
        JSONObject head = dataObj.optJSONObject("store");
        if (head == null) {
            adapter.removeHeaderView(headView);
        } else {
            Picasso.with(this).load(UrlUtils.baseWebsite + head.optString("store_banner")).into(backImg);
            Picasso.with(this).load(UrlUtils.baseWebsite + head.optString("store_logo")).into(headImg);
            tvShopName.setText(head.optString("store_name"));
        }
    }

    private void getPageCount(JSONObject dataObj) {
        total_page = dataObj.optInt("total_page");
        now_page++;
    }

    @Override
    public void handle404(String message) {
        getDm().buildAlertDialog(message);
    }

    @Override
    public void handleNoNetWork() {
        refreshLat.setRefreshing(false);
        handler.sendEmptyMessage(LOAD_MORE_ERROR);
        getDm().buildAlertDialog(getResources().getString(R.string.no_network));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            finish();
            return;
        }
        switch (requestCode) {
            case REQUEST_LOGIN:
                if (resultCode == RESULT_OK) {//登录成功
                    getStoreFromIntent();
                    requestData(UrlUtils.getStore);
                } else {//登录失败
                    finish();
                }
                break;
        }
    }

    private void clearData() {
        if (goodsList.size() != 0) {
            goodsList.clear();
        }
    }

    private void resetPage() {
        if (now_page != 1) {
            now_page = 1;
        }
    }
}
