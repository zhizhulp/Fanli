package com.ascba.rebate.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.DeliverDetailsActivity;
import com.ascba.rebate.adapter.DeliverGoodsAdapter;
import com.ascba.rebate.beans.Goods;
import com.ascba.rebate.beans.OrderBean;
import com.ascba.rebate.fragments.base.Base2Fragment;
import com.ascba.rebate.utils.TimeUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.SuperSwipeRefreshLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.yolanda.nohttp.rest.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李鹏 on 2017/03/14 0014.
 * 待发货
 */

public class DeliverGoodsFragment extends Base2Fragment implements SuperSwipeRefreshLayout.OnPullRefreshListener {

    private RecyclerView recyclerView;
    private SuperSwipeRefreshLayout refreshLat;
    private Context context;

    /**
     * 每笔订单中的商品列表
     */
    private List<Goods> goodsList;
    private List<OrderBean> beanArrayList = new ArrayList<>();
    private DeliverGoodsAdapter adapter;
    private View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        return inflater.inflate(R.layout.fragment_deliver_goods, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        requstData();
    }

    /*
     获取数据
   */
    private void requstData() {
        Request<JSONObject> jsonRequest = buildNetRequest(UrlUtils.getOrderList, 0, true);
        jsonRequest.add("status", "wait_deliver");
        executeNetWork(jsonRequest, "请稍后");
        setCallback(new Callback() {
            @Override
            public void handle200Data(JSONObject dataObj, String message) {
                initData(dataObj);
                if (adapter == null) {
                    initRecylerView();
                } else {
                    //刷新数据
                    refreshLat.setRefreshing(false);
                    adapter.notifyDataSetChanged();
                }
                Log.d("DeliverGoodsFragment", "beanArrayList.size():" + beanArrayList.size());
            }

            @Override
            public void handleReqFailed() {
                refreshLat.setRefreshing(false);
            }

            @Override
            public void handle404(String message) {
                getDm().buildAlertDialog(message);
                refreshLat.setRefreshing(false);
            }

            @Override
            public void handleReLogin() {
                refreshLat.setRefreshing(false);
            }

            @Override
            public void handleNoNetWork() {
                getDm().buildAlertDialog("请检查网络！");
                refreshLat.setRefreshing(false);
            }
        });
    }

    /*
    初始化数据
     */
    private void initData(JSONObject dataObj) {
        beanArrayList.clear();
        JSONArray jsonArray = dataObj.optJSONArray("order_list");
        if (jsonArray != null && jsonArray.length() > 0) {
            for (int i = 0; i < jsonArray.length(); i++) {
                int totalNum = 0;//购买商品数量
                JSONObject object = jsonArray.optJSONObject(i);
                //头部信息
                String time = object.optString("add_time");//时间
                time = TimeUtils.milli2String((Long.parseLong(time) * 1000));
                OrderBean beanHead = new OrderBean(DeliverGoodsAdapter.TYPE1, R.layout.item_goods_order_head, time, "等待卖家发货");
                beanArrayList.add(beanHead);

                //商品信息
                JSONArray goodsArray = object.optJSONArray("orderGoods");
                Log.d("DeliverGoodsFragment", "goodsArray.length():" + goodsArray.length());
                if (goodsArray != null && goodsArray.length() > 0) {
                    for (int j = 0; j < goodsArray.length(); j++) {
                        try {
                            JSONObject goodsObject = goodsArray.getJSONObject(j);
                            Goods good = new Goods();
                            good.setTitleId(Integer.parseInt(goodsObject.optString("id")));//商品id
                            good.setImgUrl(UrlUtils.baseWebsite + goodsObject.optString("goods_img"));//图片
                            good.setGoodsTitle(goodsObject.optString("goods_name"));//商品名

                            int num = Integer.parseInt(String.valueOf(goodsObject.opt("goods_num")));
                            totalNum = num + totalNum;

                            good.setUserQuy(num);//购买数量
                            good.setGoodsPrice(goodsObject.optString("goods_pay_price"));//付款价格
                            good.setGoodsPriceOld(goodsObject.optString("goods_price"));//原价
                            beanArrayList.add(new OrderBean(DeliverGoodsAdapter.TYPE2, R.layout.item_goods, good));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                //底部信息
                String orderAmount = object.optString("order_amount");//订单总价
                String shippingFee = "(含" + object.optString("shipping_fee") + "元运费)";//运费
                String goodsNum = "共" + totalNum + "件商品";//商品数量
                OrderBean beadFoot = new OrderBean(DeliverGoodsAdapter.TYPE3, R.layout.item_goods_order_foot, goodsNum, "￥" + orderAmount, shippingFee);
                beanArrayList.add(beadFoot);
            }
        }
    }

    private void initRecylerView() {
        recyclerView = (RecyclerView) view.findViewById(R.id.list_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        adapter = new DeliverGoodsAdapter(beanArrayList, context);

        /**
         * empty
         */
        View emptyView = LayoutInflater.from(context).inflate(R.layout.view_empty, null);
        adapter.setEmptyView(emptyView);

        recyclerView.setAdapter(adapter);

        refreshLat = ((SuperSwipeRefreshLayout) view.findViewById(R.id.refresh_layout));
        refreshLat.setOnPullRefreshListener(this);

        recyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.item_goods_rl:
                        //点击商品查看订单详情
                        Intent intent = new Intent(context, DeliverDetailsActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });
    }

    /**
     * 商品数据
     */
    private List<OrderBean> getData() {
        List<OrderBean> beanArrayList = new ArrayList<>();

        String imgUrl = "http://image1+8-c.poco.cn/mypoco/myphoto/20170315/10/18505011120170315100507017_640.jpg";
        String title = "RCC男装 春夏 设计师款修身尖领翻领免烫薄长袖寸衫 韩国代购 2色";
        String standard = "颜色:深蓝色;尺码:S";
        String price = "368";
        String priceOld = "468";
        int num = 1;


        /**
         * 订单1：一个商品
         */
        //订单一；头部信息
        OrderBean beanHead1 = new OrderBean(DeliverGoodsAdapter.TYPE1, R.layout.item_goods_order_head, "2016-01-27", "等待卖家发货");
        beanArrayList.add(beanHead1);

        //订单一；商品信息
        goodsList = new ArrayList<>();
        goodsList.add(new Goods(imgUrl, title, standard, price, priceOld, num));
        /**
         * 遍历商品列表并添加
         */
        for (Goods goods : goodsList) {
            OrderBean beanGoods1 = new OrderBean(DeliverGoodsAdapter.TYPE2, R.layout.item_goods, goods);
            beanArrayList.add(beanGoods1);
        }

        //订单一；尾部信息
        String goodsNum = "共" + goodsList.size() + "件商品";//商品数量
        String freight = "(包含15.00运费)";
        /**
         * 遍历商品列表计算总价
         */
        double orderPrcie = 0.00;
        for (Goods goods : goodsList) {
            double goodsPrice = Double.valueOf(goods.getGoodsPrice()) * Double.valueOf(goods.getUserQuy());
            orderPrcie = orderPrcie + goodsPrice;
        }
        OrderBean beadFoot1 = new OrderBean(DeliverGoodsAdapter.TYPE3, R.layout.item_goods_order_foot, goodsNum, "￥" + String.valueOf(orderPrcie), freight);
        beanArrayList.add(beadFoot1);


        /**
         * 订单2：两个个商品
         */
        //订单2；头部信息
        OrderBean beanHead2 = new OrderBean(DeliverGoodsAdapter.TYPE1, R.layout.item_goods_order_head, "2016-01-27", "等待卖家发货");
        beanArrayList.add(beanHead2);

        //订单2；商品信息
        goodsList = new ArrayList<>();
        goodsList.add(new Goods(imgUrl, title, standard, price, priceOld, num));
        goodsList.add(new Goods(imgUrl, title, standard, price, priceOld, num));
        /**
         * 遍历商品列表并添加
         */
        for (Goods goods : goodsList) {
            OrderBean beanGoods2 = new OrderBean(DeliverGoodsAdapter.TYPE2, R.layout.item_goods, goods);
            beanArrayList.add(beanGoods2);
        }

        //订单2；尾部信息
        String goodsNum2 = "共" + goodsList.size() + "件商品";//商品数量
        String freight2 = "(包含15.00运费)";
        /**
         * 遍历商品列表计算总价
         */
        double orderPrcie2 = 0.00;
        for (Goods goods : goodsList) {
            double goodsPrice = Double.valueOf(goods.getGoodsPrice()) * Double.valueOf(goods.getUserQuy());
            orderPrcie2 = orderPrcie2 + goodsPrice;
        }
        OrderBean beadFoot2 = new OrderBean(DeliverGoodsAdapter.TYPE3, R.layout.item_goods_order_foot, goodsNum2, "￥" + String.valueOf(orderPrcie2), freight2);
        beanArrayList.add(beadFoot2);

        return beanArrayList;
    }

    @Override
    public void onRefresh() {
        requstData();
    }

    @Override
    public void onPullDistance(int distance) {

    }

    @Override
    public void onPullEnable(boolean enable) {

    }
}