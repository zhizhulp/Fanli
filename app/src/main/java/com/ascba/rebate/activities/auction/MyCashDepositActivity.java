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
import com.ascba.rebate.adapter.CashDepositAdapter;
import com.ascba.rebate.beans.AcutionGoodsBean;
import com.ascba.rebate.fragments.auction.AuctionHomePageFragment;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.ShopABarText;
import com.ascba.rebate.view.loadmore.CustomLoadMoreView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.yanzhenjie.nohttp.rest.Request;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.ProcessingInstruction;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.chad.library.adapter.base.loadmore.LoadMoreView.STATUS_DEFAULT;

/**
 * Created by 李鹏 on 2017/5/25.
 * 我的保证金列表
 */

public class MyCashDepositActivity extends BaseNetActivity {

    private static final int REDUCE_TIME = 2;
    private CashDepositAdapter adapter;
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
                case REDUCE_TIME:
                    if (beanList.size() == 0) {
                        return;
                    }
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    };
    private boolean isRefresh;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_deposit);
        initView();
        requestNetwork(UrlUtils.bondList,0);
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
        adapter = new CashDepositAdapter(this, R.layout.item_auction_cash_deposit, beanList);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                AuctionDetailsActivity.startIntent(MyCashDepositActivity.this,beanList.get(position));
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
                requestNetwork(UrlUtils.bondList,0);
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
            parseData(dataObj.optJSONArray("bondList"));
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
                bean.setId(obj.optInt("goods_id"));
                bean.setImgUrl(UrlUtils.baseWebsite+ obj.optString("imghead"));
                bean.setType(obj.optInt("type"));
                bean.setName(obj.optString("name"));
                bean.setStartTime(obj.optLong("starttime"));
                bean.setEndTime(obj.optLong("endtime"));
                bean.setPayState(obj.optString("bond_pay_tip"));
                bean.setCashDeposit(obj.optString("guarantee_money"));
                bean.setIntState(obj.optInt("is_status"));
                bean.setStrState(obj.optString("auction_tip"));
                bean.setStrPriceState(obj.optString("bond_pay_tip"));
                bean.setEndTime(obj.optLong("endtime"));
                beanList.add(bean);
            }
        }
        if (beanList.size() > 0) {
            if (timer == null) {
                timer = new Timer();
                timer.schedule(new MyTimerTask(), 0, 1000);
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
                    requestNetwork(UrlUtils.auctionType,0);
                }
            }
        });
    }

    private void clearData(){
        if(beanList.size()!=0){
            beanList.clear();
        }
    }

    private class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            if (!isTimerOver()) {
                handler.sendEmptyMessage(REDUCE_TIME);
            }
        }
    }

    //用于判断倒计时是否结束
    private boolean isTimerOver() {
        boolean isOver = true;
        for (int i = 0; i < beanList.size(); i++) {
            AcutionGoodsBean agb = beanList.get(i);
            if ((agb.getEndTime() - System.currentTimeMillis() / 1000) >= 0) {
                isOver = false;
            }
        }
        return isOver;
    }

}
