package com.ascba.rebate.fragments.shop.auction;

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
import com.ascba.rebate.activities.shop.auction.PayDepositActivity;
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

import static com.chad.library.adapter.base.loadmore.LoadMoreView.STATUS_DEFAULT;

/**
 * Created by 李鹏 on 2017/5/22.
 * 拍卖首页
 */

public class AuctionHomePageFragment extends BaseNetFragment {
    private AcutionHPAdapter adapter;
    private List<AcutionGoodsBean> acutionGoodsBeanList = new ArrayList<>();
    private int now_page = 1;
    private int total_page;
    private CustomLoadMoreView loadMoreView;
    private static final int LOAD_MORE_END = 0;
    private static final int LOAD_MORE_ERROR = 1;
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
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_auction_homepage, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        requestNetwork(UrlUtils.auction,0);
    }

    private void requestNetwork(String url, int what) {
        Request<JSONObject> request = buildNetRequest(url, 0, false);
        request.add("now_page",now_page);
        executeNetWork(what,request,"请稍后");
    }

    private void initView(View view) {
        initShopABar(view);
        initRecyclerView(view);
        initRefreshLayoutView(view);
        initLoadMore();
    }



    @Override
    protected void mhandle200Data(int what, JSONObject object, JSONObject dataObj, String message) {
        if(what==0){
            getPageCount(dataObj);//分页
            initHeadView(dataObj);//头部数据
            stopLoadMore();
            if(isRefresh){//下拉刷新
                clearData();
                initAuctionData(dataObj);//列表数据
            }else {//上拉加载
                initAuctionData(dataObj);//列表数据
            }

        }
    }

    private void initAuctionData(JSONObject dataObj) {
        JSONArray goodsArray = dataObj.optJSONArray("auction_goods");
        if(goodsArray!=null && goodsArray.length()>0){
            for (int i = 0; i < goodsArray.length(); i++) {
                JSONObject obj = goodsArray.optJSONObject(i);
                AcutionGoodsBean bean=new AcutionGoodsBean();
                bean.setId(obj.optInt("id"));
                bean.setName(obj.optString("name"));
                bean.setImgUrl(UrlUtils.baseWebsite+obj.optString("index_img"));
                bean.setType(obj.optInt("type"));
                bean.setPrice(obj.optDouble("end_price"));
                int[] diff = TimeUtils.timeDifference(obj.optLong("endtime") * 1000);
                bean.setTimeRemaining("距离结束："+diff[1]+"时"+diff[2]+"分"+diff[3]+"秒" );
                acutionGoodsBeanList.add(bean);
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
                isRefresh=false;
                if (now_page > total_page && total_page != 0) {
                    handler.sendEmptyMessage(LOAD_MORE_END);
                } else if(total_page==0){
                    handler.sendEmptyMessage(LOAD_MORE_END);
                } else {
                    requestNetwork(UrlUtils.auction,0);
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
                isRefresh=true;
                requestNetwork(UrlUtils.auction,0);
            }
        });
    }
    private void initRecyclerView(View view) {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new AcutionHPAdapter(getActivity(), R.layout.item_auction_hp, acutionGoodsBeanList);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemChildClick(adapter, view, position);
                switch (view.getId()) {
                    case R.id.auction_btn_get://立即拍
                        startActivity(new Intent(getActivity(), PayDepositActivity.class));
                        break;
                }
            }
        });
    }

    private void initHeadView(JSONObject dataObj) {
        View headView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_auction_hp_headview, null, false);
        ShufflingViewPager viewPager = (ShufflingViewPager) headView.findViewById(R.id.shufflingViewPager);
        List<String> banner=new ArrayList<>();
        JSONArray array = dataObj.optJSONArray("banner");
        if(array!=null && array.length()> 0){
            for (int i = 0; i < array.length(); i++) {
                banner.add(UrlUtils.baseWebsite + array.optString(i));
            }
            ShufflingViewPagerAdapter adapter = new ShufflingViewPagerAdapter(getActivity(),banner);
            viewPager.setAdapter(adapter);
            viewPager.start();
        }else {
            viewPager.setVisibility(View.GONE);
        }
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

    private void clearData(){
        if(acutionGoodsBeanList.size()!=0){
            acutionGoodsBeanList.clear();
        }
    }
    private void resetPage(){
        now_page=1;
        total_page=0;
    }

}
