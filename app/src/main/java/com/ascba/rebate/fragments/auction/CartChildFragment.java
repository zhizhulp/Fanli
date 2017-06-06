package com.ascba.rebate.fragments.auction;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ascba.rebate.R;
import com.ascba.rebate.adapter.CartChildAdapter;
import com.ascba.rebate.beans.AcutionGoodsBean;
import com.ascba.rebate.fragments.base.BaseNetFragment;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.loadmore.CustomLoadMoreView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yanzhenjie.nohttp.rest.Request;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.chad.library.adapter.base.loadmore.LoadMoreView.STATUS_DEFAULT;

/**
 * 竞拍购物车
 */
public class CartChildFragment extends BaseNetFragment {


    private RecyclerView recyclerView;
    private CartChildAdapter adapter;
    private List<AcutionGoodsBean> beanList;
    private String status;
    private int now_page = 1;
    private int total_page;
    private CustomLoadMoreView loadMoreView;
    private static final int LOAD_MORE_END = 0;
    private static final int LOAD_MORE_ERROR = 1;
    private static final int REDUCE_TIME = 2;
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
                    if(beanList.size()==0){
                        return;
                    }
                    setBeanProperty();
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    };
    private Timer timer ;
    private boolean isRefresh=true;

    public CartChildFragment() {

    }

    public static CartChildFragment newInstance(String status){
        CartChildFragment fragment=new CartChildFragment();
        Bundle b=new Bundle();
        b.putString("status",status);
        fragment.setArguments(b);
        return fragment;
    }

    private void setBeanProperty(){
        for (int i = 0; i < beanList.size(); i++) {
            AcutionGoodsBean agb = beanList.get(i);
            int currentLeftTime = agb.getCurrentLeftTime();
            int reduceTimes = agb.getReduceTimes();
            int maxReduceTimes = agb.getMaxReduceTimes();
            Double price = agb.getPrice();
            if(reduceTimes >= maxReduceTimes ){
                return;
            }
            currentLeftTime--;
            if(currentLeftTime <=0){
                reduceTimes++;
                price -= agb.getGapPrice();
                currentLeftTime=agb.getGapTime();
                agb.setReduceTimes(reduceTimes);
                agb.setPrice(price);
            }
            agb.setCurrentLeftTime(currentLeftTime);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart_no_sure, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getParams();
        initViews(view);
        requestNetwork(UrlUtils.auctionCard,0);
    }

    private void requestNetwork(String url, int what) {
        Request<JSONObject> request = buildNetRequest(url, 0, true);
        request.add("between","2,3");
        request.add("now_page",now_page);
        executeNetWork(what,request,"请稍后");
    }

    private void getParams() {
        Bundle args = getArguments();
        if(args!=null){
            status = args.getString("status");
        }
    }

    private void initViews(View view) {
        initMyRefreshLayout(view);
        initRecyclerview(view);
        initLoadMore();
    }

    private void initRecyclerview(View view) {
        recyclerView = ((RecyclerView) view.findViewById(R.id.recyclerview));
        beanList=new ArrayList<>();
        adapter = new CartChildAdapter(R.layout.auction_list_item,beanList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }

    private void initMyRefreshLayout(View view) {
        initRefreshLayout(view);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefresh=true;
                resetPage();
                requestNetwork(UrlUtils.auctionCard,0);
            }
        });
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

    @Override
    protected void mhandle200Data(int what, JSONObject object, JSONObject dataObj, String message) {
        if(what==0){
            stopLoadMore();
            if(isRefresh){//下拉刷新
                clearData();
            }
            getPageCount(dataObj);
            refreshGoodsList(dataObj);
        }
    }

    @Override
    protected void mhandleFailed(int what, Exception e) {
        if(what==0){
            handler.sendEmptyMessage(LOAD_MORE_ERROR);
        }
    }

    private void refreshGoodsList(JSONObject dataObj) {
        JSONObject agent = dataObj.optJSONObject("agent");
        JSONArray array = agent.optJSONArray("auction_cart_list");
        if(array!=null && array.length() >0){
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.optJSONObject(i);
                AcutionGoodsBean agb = new AcutionGoodsBean(obj.optInt("id"), obj.optInt("type"), obj.optString("imghead"),
                        obj.optString("name"), obj.optDouble("transaction_price"),
                        obj.optString("points"), obj.optString("cash_deposit"), obj.optInt("refresh_count"));
                agb.setGapPrice(obj.optDouble("range"));
                agb.setMaxReduceTimes(obj.optInt("depreciate_count"));
                agb.setCurrentLeftTime(obj.optInt("count_down"));
                agb.setGapTime(obj.optInt("interval_second"));
                agb.setIntState(obj.optInt("is_status"));
                agb.setStrState(obj.optString("auction_tip"));
                agb.setStartPrice(obj.optDouble("begin_price"));
                agb.setEndPrice(obj.optDouble("end_price"));
                beanList.add(agb);
            }
        }
        adapter.notifyDataSetChanged();
        if(timer!=null){
            timer = new Timer();
            timer.schedule(new MyTimerTask(),0,1000);
        }
    }
    private void getPageCount(JSONObject dataObj) {
        total_page = dataObj.optInt("total_page");
        now_page++;
    }

    private void stopLoadMore() {
        if (adapter != null) {
            adapter.loadMoreComplete();
        }
        if (loadMoreView != null) {
            loadMoreView.setLoadMoreStatus(STATUS_DEFAULT);
        }
    }
    private class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            if(!isTimerOver()){
                handler.sendEmptyMessage(REDUCE_TIME);
            }
        }
    }
    //用于判断倒计时是否结束
    private boolean isTimerOver(){
        boolean isOver=true;
        for (int i = 0; i < beanList.size(); i++) {
            AcutionGoodsBean agb = beanList.get(i);
            int reduceTimes = agb.getReduceTimes();
            int maxReduceTimes = agb.getMaxReduceTimes();
            if(reduceTimes < maxReduceTimes ){
                isOver=false;
                break;
            }
        }
        return isOver;
    }
    private void resetPage(){
        now_page=1;
        total_page=0;
    }

    private void clearData(){
        if(beanList.size()!=0){
            beanList.clear();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(timer!=null){
            timer.cancel();
        }
    }
}
