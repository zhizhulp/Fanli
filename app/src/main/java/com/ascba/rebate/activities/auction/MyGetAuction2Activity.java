package com.ascba.rebate.activities.auction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.ShopMessageActivity;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.adapter.AuctionOrderAdapter;
import com.ascba.rebate.beans.AcutionGoodsBean;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.utils.ViewUtils;
import com.ascba.rebate.view.ShopABar;
import com.ascba.rebate.view.loadmore.CustomLoadMoreView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.yanzhenjie.nohttp.rest.Request;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.chad.library.adapter.base.loadmore.LoadMoreView.STATUS_DEFAULT;

/**
 * Created by 李鹏 on 2017/05/25.
 * 我的获拍页面
 */

public class MyGetAuction2Activity extends BaseNetActivity {

    private AuctionOrderAdapter adapter;
    private List<AcutionGoodsBean> beanList;
    private int now_page = 1;
    private int total_page;
    private CustomLoadMoreView loadMoreView;
    private static final int LOAD_MORE_END = 0;
    private static final int LOAD_MORE_ERROR = 1;
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
    private boolean isRefresh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_get_auction);
        initView();
        requestNetwork(UrlUtils.auctionPayList,0);
    }

    private void requestNetwork(String url, int what) {
        Request<JSONObject> request = buildNetRequest(url, 0, true);
        if(what==0){
            request.add("now_page",now_page);
        }
        executeNetWork(what,request,"请稍后");
    }

    @Override
    protected void mhandle200Data(int what, JSONObject object, JSONObject dataObj, String message) {
        if(what==0){
            stopLoadMore();
            if(isRefresh){
                clearData();
            }
            parseData(dataObj.optJSONArray("auctionPayList"));
        }
    }

    private void parseData(JSONArray array) {
        if(array!=null && array.length()>0){
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.optJSONObject(i);
                AcutionGoodsBean agb=new AcutionGoodsBean();
                agb.setId(obj.optInt("goods_id"));
                agb.setImgUrl(UrlUtils.baseWebsite+obj.optString("imghead"));
                agb.setName(obj.optString("name"));
                agb.setPrice(obj.optDouble("reserve_money"));
                agb.setScore(obj.optString("points"));
                agb.setIntPriceState(obj.optInt("auction_status"));
                agb.setStrPriceState(obj.optString("auction_status_tip"));
                beanList.add(agb);
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void mhandleFailed(int what, Exception e) {
        if(what==0){
            handler.sendEmptyMessage(LOAD_MORE_ERROR);
        }
    }

    private void initView() {
        initShopBar();
        initRefresh();
        initRecyclerView();
        initLoadMore();
    }

    private void initLoadMore() {
        if (loadMoreView == null) {
            loadMoreView = new CustomLoadMoreView();
            adapter.setLoadMoreView(loadMoreView);
        }

        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                isRefresh=false;
                if (now_page > total_page && total_page != 0) {
                    handler.sendEmptyMessage(LOAD_MORE_END);
                } else if(total_page==0){
                    handler.sendEmptyMessage(LOAD_MORE_END);
                } else {
                    requestNetwork(UrlUtils.auctionList,0);
                }
            }
        });
    }

    private void initRecyclerView() {
        beanList=new ArrayList<>();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new AuctionOrderAdapter( R.layout.item_auction_goods3, beanList);
        adapter.setEmptyView(ViewUtils.getEmptyView(this,"暂无数据"));
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {

            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemChildClick(adapter, view, position);
                AcutionGoodsBean agb = beanList.get(position);
                if(view.getId()==R.id.btn_auction){
                    if( agb.getIntPriceState()==0){
                        showToast("去支付");
                    }
                }
            }
        });
    }

    private void initRefresh() {
        initRefreshLayout();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                resetPage();
                isRefresh=true;
                requestNetwork(UrlUtils.auctionPayList,0);
            }
        });
    }

    private void initShopBar() {
        ShopABar shopABar = (ShopABar) findViewById(R.id.activity_order_bar);
        shopABar.setImageOtherEnable(false);
        shopABar.setCallback(new ShopABar.Callback() {
            @Override
            public void back(View v) {

            }

            @Override
            public void clkMsg(View v) {
                ShopMessageActivity.startIntent(MyGetAuction2Activity.this);
            }

            @Override
            public void clkOther(View v) {

            }
        });
    }
    private void clearData(){
        if(isRefresh){
            if(beanList.size()!=0){
                beanList.clear();
            }
        }
    }

    private void resetPage(){
        now_page=1;
        total_page=0;
    }

    private void stopLoadMore() {
        if (adapter != null) {
            adapter.loadMoreComplete();
        }
        if (loadMoreView != null) {
            loadMoreView.setLoadMoreStatus(STATUS_DEFAULT);
        }
    }

}
