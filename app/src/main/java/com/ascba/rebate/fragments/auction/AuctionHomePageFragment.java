package com.ascba.rebate.fragments.auction;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.ascba.rebate.activities.auction.AuctionDetailsActivity;
import com.ascba.rebate.activities.auction.AuctionListActivity;
import com.ascba.rebate.activities.auction.PayDepositActivity;
import com.ascba.rebate.adapter.AcutionHPAdapter;
import com.ascba.rebate.adapter.ShufflingViewPagerAdapter;
import com.ascba.rebate.beans.AcutionGoodsBean;
import com.ascba.rebate.fragments.base.BaseNetFragment;
import com.ascba.rebate.utils.TimeUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.ShopABar;
import com.ascba.rebate.view.loadmore.CustomLoadMoreView;
import com.ascba.rebate.view.pagerWithTurn.ShufflingViewPager;
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
 * Created by 李鹏 on 2017/5/22.
 * 拍卖首页
 */

public class AuctionHomePageFragment extends BaseNetFragment {
    private AcutionHPAdapter adapter;
    private List<AcutionGoodsBean> beanList = new ArrayList<>();
    private int now_page = 1;
    private int total_page;
    private CustomLoadMoreView loadMoreView;
    private static final int LOAD_MORE_END = 0;
    private static final int LOAD_MORE_ERROR = 1;
    private static final int REDUCE_TIME = 2;
    private boolean isRefresh;//当前是否是下拉刷新状态
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
            }
        }
    };
    private Timer timer;

    private void setBeanProperty() {
        if (beanList.size() == 0) {
            return;
        }
        for (int i = 0; i < beanList.size(); i++) {
            AcutionGoodsBean agb = beanList.get(i);
            int currentLeftTime = agb.getCurrentLeftTime();
            int reduceTimes = agb.getReduceTimes();
            Double price = agb.getPrice();
            if (agb.getIntState() != 2) {
                continue;
            }
            if (currentLeftTime <= 0) {
                reduceTimes++;
                price -= agb.getGapPrice();
                currentLeftTime = agb.getGapTime();
                agb.setReduceTimes(reduceTimes);
                if (agb.getType() == 1) {
                    agb.setPrice(price);
                }
            } else {
                currentLeftTime--;
            }
            agb.setCurrentLeftTime(currentLeftTime);
        }
        adapter.notifyDataSetChanged();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_auction_homepage, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        requestNetwork(UrlUtils.auction, 0);
    }

    private void requestNetwork(String url, int what) {
        Request<JSONObject> request = buildNetRequest(url, 0, false);
        request.add("now_page", now_page);
        executeNetWork(what, request, "请稍后");
    }

    private void initView(View view) {
        initShopABar(view);
        initRecyclerView(view);
        initRefreshLayoutView(view);
        initLoadMore();
    }

    @Override
    protected void mhandle200Data(int what, JSONObject object, JSONObject dataObj, String message) {
        if (what == 0) {
            getPageCount(dataObj);//分页
            initHeadView(dataObj);//头部数据
            stopLoadMore();
            if (isRefresh) {//下拉刷新
                clearData();
                initAuctionData(dataObj);//列表数据
            } else {//上拉加载
                initAuctionData(dataObj);//列表数据
            }

        }
    }

    private void initAuctionData(JSONObject dataObj) {
        JSONArray goodsArray = dataObj.optJSONArray("auction_goods");
        if (goodsArray != null && goodsArray.length() > 0) {
            for (int i = 0; i < goodsArray.length(); i++) {
                JSONObject obj = goodsArray.optJSONObject(i);
                AcutionGoodsBean agb = new AcutionGoodsBean(obj.optInt("id"), obj.optInt("type"), UrlUtils.baseWebsite + obj.optString("index_img"),
                        obj.optString("name"), obj.optDouble("begin_price"),
                        obj.optString("points"), obj.optString("cash_deposit"), obj.optInt("refresh_count"));
                agb.setGapPrice(obj.optDouble("range"));
                agb.setGapTime(obj.optInt("interval_second"));
                agb.setStartPrice(obj.optDouble("begin_price"));
                agb.setEndPrice(obj.optDouble("end_price"));
                agb.setStartTime(obj.optLong("starttime"));
                agb.setEndTime(obj.optLong("endtime"));
                beanList.add(agb);
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


    @Override
    protected void mhandleFailed(int what, Exception e) {
        if (what == 0) {
            handler.sendEmptyMessage(LOAD_MORE_ERROR);
        }
    }

    private void getPageCount(JSONObject dataObj) {
        total_page = dataObj.optInt("total_page");
        now_page++;
    }

    private void initLoadMore() {
        if (loadMoreView == null) {
            loadMoreView = new CustomLoadMoreView();
            adapter.setLoadMoreView(loadMoreView);
        }
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                isRefresh = false;
                if (now_page > total_page && total_page != 0) {
                    handler.sendEmptyMessage(LOAD_MORE_END);
                } else if (total_page == 0) {
                    handler.sendEmptyMessage(LOAD_MORE_END);
                } else {
                    requestNetwork(UrlUtils.auction, 0);
                }
            }
        });
    }

    private void initRefreshLayoutView(View view) {
        initRefreshLayout(view);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                resetPage();
                isRefresh = true;
                requestNetwork(UrlUtils.auction, 0);
            }
        });
    }

    private void initRecyclerView(View view) {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new AcutionHPAdapter(getActivity(), R.layout.item_auction_hp, beanList);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent=new Intent(getActivity(), AuctionDetailsActivity.class);
                intent.putExtra("agb",beanList.get(position));
                startActivity(intent);
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemChildClick(adapter, view, position);
                AcutionGoodsBean agb = beanList.get(position);
                switch (view.getId()) {
                    case R.id.auction_btn_get://立即拍
                        Intent intent = new Intent(getActivity(), PayDepositActivity.class);
                        intent.putExtra("client_ids", getClientIds(agb));
                        intent.putExtra("total_price", agb.getCashDeposit());
                        startActivity(intent);
                        break;
                }
            }
        });
    }

    private String getClientIds(AcutionGoodsBean selectAGB) {
        return "\"" +
                selectAGB.getId() +
                "\"" +
                ":" +
                "\"" +
                selectAGB.getCashDeposit() +
                "\"";
    }

    private void initHeadView(JSONObject dataObj) {
        View headView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_auction_hp_headview, null, false);
        ShufflingViewPager viewPager = (ShufflingViewPager) headView.findViewById(R.id.shufflingViewPager);
        List<String> banner = new ArrayList<>();
        JSONArray array = dataObj.optJSONArray("banner");
        if (array != null && array.length() > 0) {
            for (int i = 0; i < array.length(); i++) {
                banner.add(UrlUtils.baseWebsite + array.optString(i));
            }
            ShufflingViewPagerAdapter adapter = new ShufflingViewPagerAdapter(getActivity(), banner);
            viewPager.setAdapter(adapter);
            viewPager.start();
        } else {
            viewPager.setVisibility(View.GONE);
        }
        //抢拍
        headView.findViewById(R.id.lat_rush_auction).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AuctionListActivity.class);
                intent.putExtra("type", 1);
                startActivity(intent);
            }
        });
        //盲拍
        headView.findViewById(R.id.lat_blind_auction).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AuctionListActivity.class);
                intent.putExtra("type", 2);
                startActivity(intent);
            }
        });
        this.adapter.setHeaderView(headView);
    }

    private void initShopABar(View view) {
        ShopABar shopABar = (ShopABar) view.findViewById(R.id.shopBar);
        shopABar.setImageOtherEnable(false);
        shopABar.setImMsgSta(R.mipmap.abar_search);
        shopABar.setCallback(new ShopABar.Callback() {
            @Override
            public void back(View v) {
                getActivity().finish();
            }

            @Override
            public void clkMsg(View v) {
            }

            @Override
            public void clkOther(View v) {

            }
        });
    }

    private void stopLoadMore() {
        if (adapter != null) {
            adapter.loadMoreComplete();
        }
        if (loadMoreView != null) {
            loadMoreView.setLoadMoreStatus(STATUS_DEFAULT);
        }
    }

    private void clearData() {
        if (beanList.size() != 0) {
            beanList.clear();
        }
    }

    private void resetPage() {
        now_page = 1;
        total_page = 0;
    }

    private class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            handler.sendEmptyMessage(REDUCE_TIME);
        }

    }

    //用于判断倒计时是否结束
    private boolean isTimerOver() {
        boolean isOver = true;
        for (int i = 0; i < beanList.size(); i++) {
            AcutionGoodsBean agb = beanList.get(i);
            int reduceTimes = agb.getReduceTimes();
            int maxReduceTimes = agb.getMaxReduceTimes();
            if (reduceTimes < maxReduceTimes) {
                isOver = false;
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
        isRefresh = true;
        if (timer != null) {
            timer.cancel();
        }
    }
}
