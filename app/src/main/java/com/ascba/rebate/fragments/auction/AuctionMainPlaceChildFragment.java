package com.ascba.rebate.fragments.auction;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import com.ascba.rebate.activities.auction.AuctionConfirmOrderActivity;
import com.ascba.rebate.activities.auction.AuctionDetailsActivity;
import com.ascba.rebate.activities.auction.AuctionListActivity;
import com.ascba.rebate.activities.auction.PayDepositActivity;
import com.ascba.rebate.adapter.AuctionMainPlaceChildAdapter;
import com.ascba.rebate.application.MyApplication;
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
 * 主会场——竞拍列表
 */

public class AuctionMainPlaceChildFragment extends BaseNetFragment {

    private EndTimeListener listener;
    interface EndTimeListener{
        void timeCome();
    }
    private static final int REQUEST_PAY_DEPOSIT = 3;
    private static final int NEXT = 4;
    private static final int REQUEST_PAY_ORDER = 5;
    private List<AcutionGoodsBean> beanList = new ArrayList<>();
    private AuctionMainPlaceChildAdapter adapter;
    private int type = 1;//1 抢拍 2盲拍
    private TittleBean tb;
    private int client_key;
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
                    setBeanProperty();
                    break;
                case NEXT:
                    if(listener!=null){
                        listener.timeCome();
                    }
                    break;
            }
        }
    };
    private Timer timer ;
    private boolean isRefresh=true;
    private AcutionGoodsBean selectAGB;

    public static AuctionMainPlaceChildFragment newInstance(int type, int client_key, TittleBean tb) {
        Bundle b = new Bundle();
        b.putInt("type", type);
        b.putInt("client_key", client_key);
        b.putParcelable("title_bean", tb);
        AuctionMainPlaceChildFragment fragment = new AuctionMainPlaceChildFragment();
        fragment.setArguments(b);
        return fragment;
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
            this.client_key =b.getInt("client_key");
            if(tb!=null){//一场结束后切换到下一场
                if(tb.getStatus().equals("进行中")){
                    handler.sendEmptyMessageDelayed(NEXT,(tb.getEndTime()+1)*1000-System.currentTimeMillis());
                }
            }
        }
    }


    private void requestNetwork(String url, int what) {
        Request<JSONObject> request=null;
        if(what==0){
            request = buildNetRequest(url, 0, false);
            request.add("type", type);
            request.add("strat_time", tb.getStartTime());
            request.add("end_time", tb.getEndTime());
            request.add("client_key", client_key);
            request.add("now_page", now_page);

        }else if(what==1){
            request = buildNetRequest(url, 0, true);
            request.add("goods_id", selectAGB.getId());
            request.add("reserve_money",selectAGB.getPrice());
            executeNetWork(what, request, "请稍后");
        }else if(what==2){
            request = buildNetRequest(url, 0, true);
            request.add("client_str",getAutionIds());
            request.add("total_price",selectAGB.getPrice());
        }
        executeNetWork(what, request, "请稍后");
    }

    private String getAutionIds() {
        return "\"" +
                selectAGB.getId() +
                "\"" +
                ":" +
                "\"" +
                selectAGB.getPrice() +
                "\"";
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
                    agb.setGapPrice(obj.optDouble("range"));
                    agb.setMaxReduceTimes(obj.optInt("depreciate_count"));
                    agb.setCurrentLeftTime(obj.optInt("count_down"));
                    agb.setGapTime(obj.optInt("interval_second"));
                    int is_status = obj.optInt("is_status");
                    agb.setIntState(is_status);
                    agb.setStrState(obj.optString("auction_tip"));
                    agb.setStartTime(obj.optLong("starttime"));
                    agb.setEndTime(obj.optLong("price_time"));
                    agb.setStartPrice(obj.optDouble("begin_price"));
                    agb.setEndPrice(obj.optDouble("end_price"));
                    if(is_status==1 || is_status==5 || is_status==6 || is_status==7){
                        agb.setPrice(obj.optDouble("reserve_money"));
                    }
                    beanList.add(agb);
                }
            }
            if(beanList.size()>0){
                if(timer==null){
                    timer = new Timer();
                    timer.schedule(new MyTimerTask(),0,1000);
                }
            }
            adapter.notifyDataSetChanged();
        }else if(what==1){
            ViewUtils.showMyToast(getActivity(),R.layout.add_to_cart_toast);
            MyApplication.isLoadAuctionCart=true;
        }else if(what==2){
            if(type==1){
                Intent intent=new Intent(getActivity(), AuctionConfirmOrderActivity.class);
                intent.putExtra("goods_id",selectAGB.getId()+"");
                startActivityForResult(intent,REQUEST_PAY_ORDER);
            }else {
                showToast(message);
                resetPageAndStatus();
                requestNetwork(UrlUtils.auctionType, 0);
            }
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

    private void stopLoadMore() {
        if (adapter != null) {
            adapter.loadMoreComplete();
        }
        if (loadMoreView != null) {
            loadMoreView.setLoadMoreStatus(STATUS_DEFAULT);
        }
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

        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                AcutionGoodsBean agb = beanList.get(position);
                AuctionDetailsActivity.startIntent(getActivity(), agb.getId());
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                selectAGB = beanList.get(position);
                Double gapPrice = selectAGB.getGapPrice();
                Double startPrice = selectAGB.getStartPrice();
                Double endPrice = selectAGB.getEndPrice();
                Double nowPrice = selectAGB.getPrice();
                int state = selectAGB.getIntState();
                switch (view.getId()) {
                    case R.id.btn_sub:
                        if(nowPrice<= endPrice+gapPrice){
                            showToast("已是最低价");
                        }else {
                            selectAGB.setPrice(nowPrice -gapPrice);
                            adapter.notifyItemChanged(position);
                        }

                        break;
                    case R.id.btn_add:
                        if(nowPrice >= startPrice -gapPrice){
                            showToast("已加到最大");
                        }else {
                            selectAGB.setPrice(nowPrice +gapPrice);
                            adapter.notifyItemChanged(position);
                        }
                        break;
                    case R.id.btn_auction_goods_add_cart://加入购物车
                        requestNetwork(UrlUtils.addCard,1);
                        break;
                    case R.id.btn_auction_goods_apply:
                        if(state==2){//立即报名
                            Intent intent=new Intent(getActivity(), PayDepositActivity.class);
                            intent.putExtra("client_ids",getClientIds());
                            intent.putExtra("total_price",selectAGB.getCashDeposit());
                            startActivityForResult(intent,REQUEST_PAY_DEPOSIT);
                        }else if(state==4){//拍
                            requestNetwork(UrlUtils.payAuction,2);
                        }else if(state==6){//支付
                            Intent intent=new Intent(getActivity(),AuctionConfirmOrderActivity.class);
                            intent.putExtra("goods_id",selectAGB.getId()+"");
                            startActivityForResult(intent,REQUEST_PAY_ORDER);
                        }
                        break;
                }

            }
        });

        initRefreshLayout(view);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefresh=true;
                resetPageAndStatus();
                requestNetwork(UrlUtils.auctionType, 0);
            }
        });

        initLoadMore();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_PAY_DEPOSIT && resultCode== Activity.RESULT_OK){
            /*selectAGB.setIntState(4);//可拍
            selectAGB.setStrState("立即拍");
            adapter.notifyItemChanged(beanList.indexOf(selectAGB));*/
            resetPageAndStatus();
            requestNetwork(UrlUtils.auctionType, 0);
        }else if(requestCode==REQUEST_PAY_ORDER){
            /*selectAGB.setIntState(7);//已支付
            selectAGB.setStrState("已支付");
            adapter.notifyItemChanged(beanList.indexOf(selectAGB));*/
            resetPageAndStatus();
            requestNetwork(UrlUtils.auctionType, 0);
        }
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

    //用于判断倒计时是否结束
    private boolean isTimerOver(){
        boolean isOver=true;
        for (int i = 0; i < beanList.size(); i++) {
            AcutionGoodsBean agb = beanList.get(i);
            if(System.currentTimeMillis()< agb.getEndTime()*1000 || agb.getPrice() > agb.getEndPrice()){//没有结束
                isOver=false;
                break;
            }
        }
        return isOver;
    }
    private void setBeanProperty(){
        if(beanList.size()==0){
            return;
        }
        for (int i = 0; i < beanList.size(); i++) {
            AcutionGoodsBean agb = beanList.get(i);
            int currentLeftTime = agb.getCurrentLeftTime();
            int reduceTimes = agb.getReduceTimes();
            Double price = agb.getPrice();
            int state = agb.getIntState();
            if(System.currentTimeMillis()>=agb.getEndTime()*1000 || agb.getPrice() <= agb.getEndPrice()){
                continue;
            }
            if(currentLeftTime <=0){
                reduceTimes++;
                price -= agb.getGapPrice();
                currentLeftTime = agb.getGapTime();
                if(state<=4){
                    agb.setReduceTimes(reduceTimes);
                }
                if(type==1&&state<=4){
                    agb.setPrice(price);
                }
            }else {
                currentLeftTime--;
            }
            if(state<=4) {
                agb.setCurrentLeftTime(currentLeftTime);
            }
        }
        adapter.notifyDataSetChanged();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        resetPageAndStatus();
        clearData();
        isRefresh=true;
        if(handler.hasMessages(NEXT)){
            handler.removeMessages(NEXT);
        }
        if(timer!=null){
            timer.cancel();
        }
    }

    private void resetPageAndStatus() {
        isRefresh=true;
        now_page=1;
        total_page=0;
    }

    public EndTimeListener getListener() {
        return listener;
    }

    public void setListener(EndTimeListener listener) {
        this.listener = listener;
    }
}
