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
import com.ascba.rebate.activities.shop.order.DeliverDetailsActivity;
import com.ascba.rebate.adapter.order.DeliverOrderAdapter;
import com.ascba.rebate.adapter.order.PayOrderAdapter;
import com.ascba.rebate.beans.Goods;
import com.ascba.rebate.beans.OrderBean;
import com.ascba.rebate.fragments.base.LazyLoadFragment;
import com.ascba.rebate.utils.TimeUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.yanzhenjie.nohttp.rest.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李鹏 on 2017/03/14 0014.
 * 待发货
 */

public class DeliverOrderFragment extends LazyLoadFragment implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView recyclerView;
    private Context context;

    /**
     * 每笔订单中的商品列表
     */
    private List<OrderBean> beanArrayList = new ArrayList<>();
    private DeliverOrderAdapter adapter;
    private View view;


    @Override
    protected int setContentView() {
        return R.layout.fragment_orders;
    }

    @Override
    protected void lazyLoad() {
        requstData();
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
    private void requstData() {
        Request<JSONObject> jsonRequest = buildNetRequest(UrlUtils.getOrderList, 0, true);
        jsonRequest.add("status", "wait_deliver");
        executeNetWork(jsonRequest, "请稍后");
        setCallback(new Callback() {
            @Override
            public void handle200Data(JSONObject dataObj, String message) {
                initData(dataObj);

            }

            @Override
            public void handleReqFailed() {
            }

            @Override
            public void handle404(String message, JSONObject dataObj) {
                getDm().buildAlertDialog(message);
            }


            @Override
            public void handleReLogin() {
            }

            @Override
            public void handleNoNetWork() {
                getDm().buildAlertDialog(getString(R.string.no_network));
            }
        });
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
                //订单id
                String orderId = object.optString("order_id");
                //头部信息
                OrderBean beanHead = new OrderBean(PayOrderAdapter.TYPE1, R.layout.item_order_head, object.optString("store_name"));
                beanHead.setId(orderId);
                beanHead.setState("等待卖家发货");
                beanArrayList.add(beanHead);
                //商品信息
                JSONArray goodsArray = object.optJSONArray("orderGoods");
                if (goodsArray != null && goodsArray.length() > 0) {
                    for (int j = 0; j < goodsArray.length(); j++) {
                        JSONObject goodsObject = goodsArray.optJSONObject(j);
                        Goods good = new Goods();
                        good.setTitleId(Integer.parseInt(goodsObject.optString("order_id")));
                        good.setImgUrl(UrlUtils.baseWebsite + goodsObject.optString("goods_img"));//图片
                        good.setGoodsTitle(goodsObject.optString("goods_name"));//商品名

                        int num = Integer.parseInt(String.valueOf(goodsObject.opt("goods_num")));
                        totalNum = num + totalNum;

                        good.setUserQuy(num);//购买数量
                        good.setGoodsPrice(goodsObject.optString("goods_price"));//市场价格
                        good.setGoodsPriceOld(goodsObject.optString("market_price"));//商品价格
                        OrderBean orderBean = new OrderBean(PayOrderAdapter.TYPE2, R.layout.item_goods, good);
                        orderBean.setId(orderId);
                        beanArrayList.add(orderBean);
                    }
                    //底部信息
                    String orderAmount = object.optString("order_amount");//订单总价
                    String shippingFee = "(含" + object.optString("shipping_fee") + "元运费)";//运费
                    String goodsNum = "共" + totalNum + "件商品";//商品数量

                    OrderBean beadFoot = new OrderBean(PayOrderAdapter.TYPE3, R.layout.item_order_deliver_foot, goodsNum, "￥" + orderAmount, shippingFee);
                    beadFoot.setId(orderId);
                    beadFoot.setPhone(object.optJSONObject("seller_info").optString("store_mobile"));
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
        recyclerView = (RecyclerView) view.findViewById(R.id.list_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        adapter = new DeliverOrderAdapter(beanArrayList, context);
        recyclerView.setAdapter(adapter);
        adapter.setEmptyView(R.layout.order_empty_view,recyclerView);

        recyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                OrderBean orderBean = beanArrayList.get(position);
                String orderId = orderBean.getId();
                switch (view.getId()) {
                    case R.id.item_goods_rl:
                        //点击商品查看订单详情
                        Intent intent = new Intent(context, DeliverDetailsActivity.class);
                        intent.putExtra("order_id", orderId);
                        startActivityForResult(intent, 1);
                        break;
                    case R.id.item_goods_order_total_refund:
                        //退款
                        break;
                    case R.id.item_goods_order_total_logistics:
                        //查看物流
                        break;
                }
            }
        });

        initRefreshLayout(view);
        refreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        requstData();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}