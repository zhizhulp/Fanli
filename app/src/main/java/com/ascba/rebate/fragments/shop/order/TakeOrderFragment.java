package com.ascba.rebate.fragments.shop.order;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.WebViewBaseActivity;
import com.ascba.rebate.activities.shop.MyOrderActivity;
import com.ascba.rebate.activities.shop.order.TakeDetailsActivity;
import com.ascba.rebate.adapter.order.PayOrderAdapter;
import com.ascba.rebate.adapter.order.TakeOrderAdapter;
import com.ascba.rebate.application.MyApplication;
import com.ascba.rebate.beans.Goods;
import com.ascba.rebate.beans.OrderBean;
import com.ascba.rebate.fragments.base.BaseNetFragment;
import com.ascba.rebate.fragments.base.LazyLoadFragment;
import com.ascba.rebate.utils.DialogHome;
import com.ascba.rebate.utils.UrlUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.yanzhenjie.nohttp.rest.Request;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李鹏 on 2017/03/14 0014.
 * 待收货订单
 */

public class TakeOrderFragment extends LazyLoadFragment implements BaseNetFragment.CallbackWhat,SwipeRefreshLayout.OnRefreshListener {

    private static final int NET_LIST = 1;//列表数据请求what
    private static final int NET_RECEIVE_GOODS = 2;//点击收货接口请求what
    private static final int NET_DELEVER_FLOW = 3;//物流接口
    private Context context;
    private String orderId;
    private String ordertraces;

    /**
     * 每笔订单中的商品列表
     */
    private List<OrderBean> beanArrayList = new ArrayList<>();
    private TakeOrderAdapter adapter;
    private View view;


    @Override
    protected int setContentView() {
        return R.layout.fragment_orders;
    }

    @Override
    protected void lazyLoad() {
        requstData(UrlUtils.getOrderList, NET_LIST);
    }

    @Override
    protected void stopLoad() {
        cancelNetWork();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();
        this.view = view;
    }


    //获取数据
    private void requstData(String url, int what) {
        Request<JSONObject> jsonRequest = buildNetRequest(url, 0, true);
        if (what == NET_LIST) {
            jsonRequest.add("status", "wait_take");
        } else if (what == NET_RECEIVE_GOODS) {
            jsonRequest.add("order_id", orderId);
        }else if(what==NET_DELEVER_FLOW){
            jsonRequest.add("ordertraces",/*"58466927852"*/ordertraces);
        }

        executeNetWork(what, jsonRequest, "请稍后");
        setCallbackWhat(this);
    }

    @Override
    public void handle200Data(int what, JSONObject dataObj, String message) {
        switch (what) {
            case NET_LIST:
                initData(dataObj);
                break;
            case NET_RECEIVE_GOODS:
                MyApplication.isRefreshOrderCount=true;
                MyOrderActivity.setCurrTab(4);
                break;
            case NET_DELEVER_FLOW:
                String url = dataObj.optJSONObject("auction_exp").optString("exp_url");
                Intent intent=new Intent(getActivity(), WebViewBaseActivity.class);
                intent.putExtra("name","物流信息");
                intent.putExtra("url",url);
                startActivity(intent);
                break;
        }

    }

    @Override
    public void handleReqFailed(int what) {
    }

    @Override
    public void handle404(int what, String message, JSONObject dataObj) {
    }

    @Override
    public void handleReLogin() {
    }

    @Override
    public void handleNoNetWork() {
    }

    //初始化数据
    private void initData(JSONObject dataObj) {
        if (beanArrayList.size() > 0) {
            beanArrayList.clear();
        }
        //商品信息
        JSONArray jsonArray = dataObj.optJSONArray("order_list");
        if (jsonArray != null && jsonArray.length() > 0) {
            for (int i = 0; i < jsonArray.length(); i++) {
                int totalNum = 0;//购买商品数量
                JSONObject object = jsonArray.optJSONObject(i);
                String orderId = object.optString("order_id");//订单id
                String order_sn = object.optString("order_sn");//订单号
                //头部信息
                OrderBean beanHead = new OrderBean(PayOrderAdapter.TYPE1, R.layout.item_order_head, object.optString("store_name"));
                beanHead.setId(orderId);
                beanHead.setState("卖家已发货");
                beanArrayList.add(beanHead);
                //商品信息
                JSONArray goodsArray = object.optJSONArray("orderGoods");
                if (goodsArray != null && goodsArray.length() > 0) {
                    for (int j = 0; j < goodsArray.length(); j++) {
                        JSONObject goodsObject = goodsArray.optJSONObject(j);
                        Goods good = new Goods();
                        good.setTitleId(Integer.parseInt(goodsObject.optString("order_id")));
                        good.setImgUrl( goodsObject.optString("goods_img"));//图片
                        good.setGoodsTitle(goodsObject.optString("goods_name"));//商品名

                        int num = Integer.parseInt(String.valueOf(goodsObject.opt("goods_num")));
                        totalNum = num + totalNum;

                        good.setUserQuy(num);//购买数量
                        good.setGoodsPrice(goodsObject.optString("goods_price"));//市场价格
                        good.setGoodsPriceOld(goodsObject.optString("market_price"));//商品价格
                        good.setDeliverNum(goodsObject.optString("invoice_no"));//运单号
                        good.setTeiHui(goodsObject.optString("promotion_text"));
                        good.setUseTicketToReduce(goodsObject.optString("promotion_mark"));
                        OrderBean orderBean = new OrderBean(PayOrderAdapter.TYPE2, R.layout.item_goods, good);
                        orderBean.setId(orderId);
                        beanArrayList.add(orderBean);
                    }
                    //底部信息
                    String orderAmount = object.optString("order_amount");//订单总价
                    String shippingFee = "(含" + object.optString("shipping_fee") + "元运费)";//运费
                    String goodsNum = "共" + totalNum + "件商品";//商品数量

                    OrderBean beadFoot = new OrderBean(PayOrderAdapter.TYPE3, R.layout.item_order_take_foot, goodsNum, orderAmount, shippingFee);
                    beadFoot.setId(orderId);
                    JSONObject object1 = object.optJSONObject("seller_info");
                    if(object1!=null){
                        beadFoot.setPhone(object1.optString("store_mobile"));
                    }
                    beadFoot.setOrderNum(order_sn);
                    beanArrayList.add(beadFoot);
                }
            }
        }
        if (adapter == null) {
            initRecylerView();
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    private void initRecylerView() {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new TakeOrderAdapter(beanArrayList, context);
        recyclerView.setAdapter(adapter);
        adapter.setEmptyView(R.layout.order_empty_view, recyclerView);
        recyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                OrderBean orderBean = beanArrayList.get(position);
                orderId = orderBean.getId();
                switch (view.getId()) {
                    case R.id.item_goods_order_total_refund://退款
                        break;
                    case R.id.item_goods_order_total_take://确认收货
                        getDm().buildAlertDialogSure("您确定要确认收货吗？", new DialogHome.Callback() {
                            @Override
                            public void handleSure() {
                                requstData(UrlUtils.orderReceive, NET_RECEIVE_GOODS);
                            }
                        });

                        break;
                    case R.id.tv_express_flow://查看物流
                        ordertraces=orderBean.getGoods().getDeliverNum();
                        requstData(UrlUtils.getAuctionExp,NET_DELEVER_FLOW);
                        break;
                }
            }
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemClick(adapter, view, position);
                Intent intent = new Intent(context, TakeDetailsActivity.class);
                intent.putExtra("order_id", beanArrayList.get(position).getId());
                startActivityForResult(intent, 1);
            }
        });
        initRefreshLayout(view);
        refreshLayout.setOnRefreshListener(this);
    }
    @Override
    public void onRefresh() {
        requstData(UrlUtils.getOrderList, NET_LIST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            requstData(UrlUtils.getOrderList, NET_LIST);
        }
    }
}