package com.ascba.rebate.fragments.auction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.auction.AuctionListActivity;
import com.ascba.rebate.activities.auction.GrabShootActivity;
import com.ascba.rebate.activities.auction.PayDepositActivity;
import com.ascba.rebate.adapter.AuctionMainPlaceChildAdapter;
import com.ascba.rebate.beans.AcutionGoodsBean;
import com.ascba.rebate.beans.TittleBean;
import com.ascba.rebate.fragments.base.BaseNetFragment;
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
import java.util.Timer;
import java.util.TimerTask;

import static com.chad.library.adapter.base.loadmore.LoadMoreView.STATUS_DEFAULT;


/**
 * Created by 李鹏 on 2017/5/24.
 * 主会场——盲拍列表
 */

public class AuctionMainPlaceChildFragment extends BaseNetFragment {

    private List<AcutionGoodsBean> beanList = new ArrayList<>();
    private AuctionMainPlaceChildAdapter adapter;
    private int type = 1;
    private TittleBean tb;
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
    private AcutionGoodsBean selectAGB;

    public static AuctionMainPlaceChildFragment newInstance(int type, TittleBean tb) {
        Bundle b = new Bundle();
        b.putInt("type", type);
        b.putParcelable("title_bean", tb);
        AuctionMainPlaceChildFragment fragment = new AuctionMainPlaceChildFragment();
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
                if(type==1){
                    agb.setPrice(price);
                }
            }
            agb.setCurrentLeftTime(currentLeftTime);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_auction_main_place_child, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getParams();
        initView(view);
        requestNetwork(UrlUtils.auctionType, 0);
    }

    private void getParams() {
        Bundle b = getArguments();
        if (b != null) {
            this.type = b.getInt("type");
            this.tb = b.getParcelable("title_bean");

            if(tb!=null){//一场结束后切换到下一场
                if(tb.getStatus().equals("进行中")){
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Fragment parentFragment = getParentFragment();
                            FragmentActivity activity = getActivity();
                            if(isVisible() && parentFragment !=null && parentFragment instanceof AuctionMainPlaceFragment){
                                ((AuctionMainPlaceFragment) parentFragment).requestNetwork(UrlUtils.auctionType,0);
                            }
                            if(isVisible() && activity !=null && activity instanceof AuctionListActivity){
                                ((AuctionListActivity) activity).requestNetwork(UrlUtils.auctionType,0);
                            }
                        }
                    },tb.getEndTime()*1000-System.currentTimeMillis());
                }
            }
        }
    }


    private void requestNetwork(String url, int what) {
        if(what==0){
            Request<JSONObject> request = buildNetRequest(url, 0, false);
            request.add("type", type);
            request.add("strat_time", tb.getStartTime());
            request.add("end_time", tb.getEndTime());
            request.add("now_page", now_page);
            executeNetWork(what, request, "请稍后");
        }else if(what==1){
            Request<JSONObject> request = buildNetRequest(url, 0, true);
            request.add("goods_id", selectAGB.getId());
            request.add("reserve_money",selectAGB.getPrice());
            executeNetWork(what, request, "请稍后");
        }
    }



    @Override
    protected void mhandle200Data(int what, JSONObject object, JSONObject dataObj, String message) {
        if(what==0){
            stopLoadMore();
            if(isRefresh){//下拉刷新
                clearData();
            }
            getPageCount(dataObj);
            JSONArray array = dataObj.optJSONArray("auction_list");
            if (array != null && array.length() > 0) {
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.optJSONObject(i);
                    AcutionGoodsBean agb = new AcutionGoodsBean(obj.optInt("id"), obj.optInt("type"), obj.optString("imghead"),
                            obj.optString("name"), obj.optDouble("transaction_price"),
                            obj.optString("points"), obj.optString("cash_deposit"), obj.optInt("refresh_count"));
                    agb.setState(tb.getStatus());
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
        }else if(what==1){
            ViewUtils.showMyToast(getActivity(),R.layout.add_to_cart_toast);
        }
    }


    @Override
    protected void mhandleFailed(int what, Exception e) {
        if(what==0){
            handler.sendEmptyMessage(LOAD_MORE_ERROR);
        }
    }

    private void getPageCount(JSONObject dataObj) {
        total_page = dataObj.optInt("total_page");
        now_page++;
    }

    private void initView(View view) {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        if (type == 1) {
            adapter = new AuctionMainPlaceChildAdapter(getActivity(), R.layout.item_auction_goods2, beanList);
        } else if (type == 2) {
            adapter = new AuctionMainPlaceChildAdapter(getActivity(), R.layout.item_auction_goods, beanList);
        }
        recyclerView.setAdapter(adapter);
        timer = new Timer();
        timer.schedule(new MyTimerTask(),0,1000);
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                AcutionGoodsBean agb = beanList.get(position);
                int type = agb.getType();
                if(type==1){
                    GrabShootActivity.startIntent(getActivity(), agb);
                }else {
                    //BlindShootActivity.startIntent(getActivity(), agb);
                }
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                selectAGB = beanList.get(position);
                Double gapPrice = selectAGB.getGapPrice();
                Double startPrice = selectAGB.getStartPrice();
                Double endPrice = selectAGB.getEndPrice();
                Double nowPrice = selectAGB.getPrice();
                switch (view.getId()) {
                    case R.id.btn_sub:
                        if(nowPrice<= endPrice){
                            showToast("已经到最低价了");
                        }else {
                            selectAGB.setPrice(nowPrice -gapPrice);
                            adapter.notifyItemChanged(position);
                        }

                        break;
                    case R.id.btn_add:
                        if(nowPrice >= startPrice){
                            showToast("已经到最高价了");
                        }else {
                            selectAGB.setPrice(nowPrice +gapPrice);
                            adapter.notifyItemChanged(position);
                        }
                        break;
                    case R.id.btn_auction_goods_add_cart://加入购物车
                        requestNetwork(UrlUtils.addCard,1);
                        break;
                    case R.id.btn_auction_goods_apply://立即报名
                        Intent intent=new Intent(getActivity(), PayDepositActivity.class);
                        intent.putExtra("client_ids",getClientIds());
                        intent.putExtra("total_price",selectAGB.getCashDeposit());
                        startActivity(intent);
                        break;
                }

            }
        });

        initRefreshLayout(view);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefresh=true;
                resetPage();
                requestNetwork(UrlUtils.auctionType, 0);
            }
        });

        initLoadMore();
    }

    private String getClientIds() {
        return "\"" +
                selectAGB.getId() +
                "\"" +
                ":" +
                "\"" +
                selectAGB.getCashDeposit() +
                "\"";
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

    private class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            if(!isTimerOver()){
                handler.sendEmptyMessage(REDUCE_TIME);
            }
        }
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
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        resetPage();
        clearData();
        isRefresh=true;
        if(timer!=null){
            timer.cancel();
        }
    }
}
