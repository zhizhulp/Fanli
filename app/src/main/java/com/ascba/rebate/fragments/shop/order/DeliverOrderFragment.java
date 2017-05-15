package com.ascba.rebate.fragments.shop.order;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.shop.order.DeliverDetailsActivity;
import com.ascba.rebate.adapter.order.DeliverOrderAdapter;
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

public class DeliverOrderFragment extends LazyLoadFragment {

    private RecyclerView recyclerView;
    private Context context;

    /**
     * 每笔订单中的商品列表
     */
    private List<OrderBean> beanArrayList = new ArrayList<>();
    private DeliverOrderAdapter adapter;
    private View view;
    private View emptyView;
    private String orderId;//订单id


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
        JSONArray jsonArray = dataObj.optJSONArray("order_list");
        if (jsonArray != null && jsonArray.length() > 0) {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.optJSONObject(i);
                //订单id
                orderId = object.optString("order_id");
                //头部信息
                OrderBean beanHead = new OrderBean(DeliverOrderAdapter.TYPE1, R.layout.item_order_head, object.optString("store_name"), "等待卖家发货");
                beanHead.setId(orderId);
                beanArrayList.add(beanHead);
                //商品信息
                Goods good = new Goods();
                good.setImgUrl(UrlUtils.baseWebsite + object.optString("goods_img"));//图片
                good.setGoodsTitle(object.optString("goods_name"));//商品名
                good.setOrderGoodsId(object.optString("order_goods_id"));
                int num = Integer.parseInt(String.valueOf(object.opt("goods_num")));

                good.setUserQuy(num);//购买数量
                String goods_pay_price = object.optString("goods_pay_price");
                double price = Double.parseDouble(goods_pay_price);

                good.setGoodsPrice(object.optString("goods_price"));//市场价格
                good.setGoodsPriceOld(object.optString("market_price"));//商品价格
                OrderBean orderBean = new OrderBean(DeliverOrderAdapter.TYPE2, R.layout.item_goods, good);
                orderBean.setId(orderId);
                beanArrayList.add(orderBean);

                //底部信息
                String shippingFee = "(含" + object.optString("shipping_fee") + "元运费)";//运费
                String goodsNum = "共" + num + "件商品";//商品数量
                OrderBean beadFoot = new OrderBean(DeliverOrderAdapter.TYPE3, R.layout.item_order_deliver_foot, goodsNum, "￥" + (num *price), shippingFee);
                beadFoot.setId(orderId);
                beanArrayList.add(beadFoot);
            }
        }

        if (adapter == null) {
            initRecylerView();
        } else {
            adapter = new DeliverOrderAdapter(beanArrayList, context);
            recyclerView.setAdapter(adapter);
        }
        if (beanArrayList.size() > 0) {
            emptyView.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.VISIBLE);
        }
    }

    private void initRecylerView() {
        recyclerView = (RecyclerView) view.findViewById(R.id.list_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        adapter = new DeliverOrderAdapter(beanArrayList, context);
        recyclerView.setAdapter(adapter);

        emptyView = view.findViewById(R.id.empty_view);

        recyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                OrderBean orderBean = beanArrayList.get(position);
                String orderGoodsId = orderBean.getGoods().getOrderGoodsId();
                switch (view.getId()) {
                    case R.id.item_goods_rl:
                        //点击商品查看订单详情
                        Intent intent = new Intent(context, DeliverDetailsActivity.class);
                        intent.putExtra("order_id", orderGoodsId);
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
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}