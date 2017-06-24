package com.ascba.rebate.activities.auction;

import android.annotation.SuppressLint;
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
import com.ascba.rebate.activities.base.WebViewBaseActivity;
import com.ascba.rebate.adapter.AuctionOrderAdapter;
import com.ascba.rebate.beans.AcutionGoodsBean;
import com.ascba.rebate.utils.StringUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.utils.ViewUtils;
import com.ascba.rebate.view.ShopABar;
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
    private AcutionGoodsBean agb;
    private static final int REQUEST_PAY=1;
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
        }else if(what==1){
            request.add("ordertraces","58466927852");
        }else if(what==2){
            request.add("order_id",agb.getOrderId());
            request.add("goods_id",agb.getId());
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
        }else if(what==1){
            String url = dataObj.optJSONObject("auction_exp").optString("exp_url");
            Intent intent=new Intent(this, WebViewBaseActivity.class);
            intent.putExtra("name","物流信息");
            intent.putExtra("url",url);
            startActivity(intent);
        }else if(what==2){//确认收获成功
            showToast(message);
            resetPageAndStatus();
            requestNetwork(UrlUtils.auctionPayList,0);
        }

    }
    private void resetPageAndStatus(){
        now_page=1;
        total_page=0;
        isRefresh=true;
    }

    private void parseData(JSONArray array) {
        if(array!=null && array.length()>0){
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.optJSONObject(i);
                AcutionGoodsBean agb=new AcutionGoodsBean();
                agb.setId(obj.optInt("goods_id"));
                agb.setOrderId(obj.optInt("id"));
                agb.setImgUrl(UrlUtils.baseWebsite+obj.optString("imghead"));
                agb.setName(obj.optString("name"));
                agb.setPrice(obj.optDouble("reserve_money"));
                agb.setScore(obj.optString("points"));
                agb.setIntPriceState(obj.optInt("auction_status"));
                agb.setStrPriceState(obj.optString("auction_status_tip"));
                agb.setExpressNum(obj.optString("express_number"));
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
                agb = beanList.get(position);
                int state = agb.getIntPriceState();
                boolean isEmpty = StringUtils.isEmpty(agb.getExpressNum());//物流账号是否为空
                int id = view.getId();
                if(id ==R.id.btn_auction){
                    if(state==0){//待支付
                        Intent intent=new Intent(MyGetAuction2Activity.this,AuctionConfirmOrderActivity.class);
                        intent.putExtra("goods_id",agb.getId());
                        startActivityForResult(intent,REQUEST_PAY);
                    }else if(state==1){
                        if(!isEmpty){//查看物流
                            requestNetwork(UrlUtils.getAuctionExp,1);
                        }else {//等待发货

                        }
                    }else if(state==2){//已收货
                    }else if(state==3){//已退款
                    }
                }else if(id ==R.id.btn_sure_receive){//确认收货
                    requestNetwork(UrlUtils.confirmReceipt,2);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_PAY && resultCode==RESULT_OK){
            resetPageAndStatus();
            requestNetwork(UrlUtils.auctionPayList,0);
        }
    }

    private void initRefresh() {
        initRefreshLayout();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                resetPageAndStatus();
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

}
