package com.ascba.rebate.activities.auction;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.adapter.AuctionListAdapter;
import com.ascba.rebate.adapter.CashDepositAdapter;
import com.ascba.rebate.beans.AcutionGoodsBean;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.utils.ViewUtils;
import com.ascba.rebate.view.loadmore.CustomLoadMoreView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.yanzhenjie.nohttp.rest.Request;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.chad.library.adapter.base.loadmore.LoadMoreView.STATUS_DEFAULT;

/**
 * 我的竞拍列表
 */
public class MyAuctionActivity extends BaseNetActivity {

    private AuctionListAdapter adapter;
    private List<AcutionGoodsBean> beanList = new ArrayList<>();
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
        setContentView(R.layout.activity_my_auction);
        initView();
        requestNetwork(UrlUtils.auctionList,0);
    }

    private void requestNetwork(String url, int what) {
        Request<JSONObject> request = buildNetRequest(url, 0, true);
        if(what==0){
            request.add("now_page",now_page);
        }
        executeNetWork(what,request,"请稍后");
    }

    private void initView() {

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        beanList = new ArrayList<>();
        adapter = new AuctionListAdapter( R.layout.auction_list_item, beanList);
        adapter.setEmptyView(ViewUtils.getEmptyView(this,"暂无数据"));
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {

            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
            }
        });

        initMyRefreshLayout();
        initLoadMore();
    }

    private void initMyRefreshLayout() {
        initRefreshLayout();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefresh=true;
                resetPage();
                requestNetwork(UrlUtils.auctionList,0);
            }
        });
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

    @Override
    protected void mhandle404(int what, JSONObject object, String message) {
        if(what==0){
            handler.sendEmptyMessage(LOAD_MORE_ERROR);
        }
    }
    private void parseData(JSONArray array) {
        if(array!=null && array.length()>0){
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.optJSONObject(i);
                AcutionGoodsBean bean = new AcutionGoodsBean();
                bean.setId(obj.optInt("id"));
                bean.setImgUrl(UrlUtils.baseWebsite+ obj.optString("imghead"));
                bean.setType(obj.optInt("type"));
                bean.setName(obj.optString("name"));
                bean.setStartTime(obj.optLong("starttime"));
                bean.setEndTime(obj.optLong("endtime"));
                bean.setPayState(obj.optString("bond_pay_tip"));
                bean.setCashDeposit(obj.optString("guarantee_money"));
                bean.setState("0");
                beanList.add(bean);
            }
        }
        adapter.notifyDataSetChanged();
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

    private void clearData(){
        if(beanList.size()!=0){
            beanList.clear();
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