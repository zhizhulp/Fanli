package com.ascba.rebate.fragments.shop.order;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.shop.order.EvaluateDetailsActivity;
import com.ascba.rebate.adapter.order.EvaluateOrderAdapter;
import com.ascba.rebate.beans.Goods;
import com.ascba.rebate.beans.OrderBean;
import com.ascba.rebate.fragments.base.BaseNetFragment;
import com.ascba.rebate.fragments.base.LazyLoadFragment;
import com.ascba.rebate.utils.DialogHome;
import com.ascba.rebate.utils.TimeUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.yanzhenjie.nohttp.rest.Request;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李鹏 on 2017/03/14 0014.
 * 待评价
 */

public class EvaluateOrderFragment extends LazyLoadFragment implements BaseNetFragment.Callback {

    private RecyclerView recyclerView;
    private Context context;

    /**
     * 每笔订单中的商品列表
     */
    private List<OrderBean> beanArrayList = new ArrayList<>();
    private EvaluateOrderAdapter adapter;
    private View view;
    private View emptyView;
    private int flag = 0;//0——获取数据，1——删除订单
    private static final String TAG="EvaluateOrderFragment";


    @Override
    protected int setContentView() {
        return R.layout.fragment_orders;
    }


    @Override
    protected void lazyLoad() {
        requstListData();
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

    /*
      获取列表数据
    */
    private void requstListData() {
        flag = 0;
        Request<JSONObject> jsonRequest = buildNetRequest(UrlUtils.getOrderList, 0, true);
        jsonRequest.add("status", "wait_evaluate");
        executeNetWork(jsonRequest, "请稍后");
        setCallback(this);
    }


    private void requstData(int flag, String url, String order_id) {
        this.flag = flag;
        Request<JSONObject> jsonRequest = buildNetRequest(url, 0, true);
        jsonRequest.add("order_id", order_id);
        executeNetWork(jsonRequest, "请稍后");
        setCallback(this);
    }


    /*
    初始化数据
     */
    private void initData(JSONObject dataObj) {
        if (beanArrayList.size() > 0) {
            beanArrayList.clear();
        }
        JSONArray jsonArray = dataObj.optJSONArray("order_list");
        if (jsonArray != null && jsonArray.length() > 0) {
            parseJson1(jsonArray);
        }
        if (adapter == null) {
            initRecylerView();
        } else {
            adapter = new EvaluateOrderAdapter(beanArrayList, context);
            recyclerView.setAdapter(adapter);
        }

        if (beanArrayList.size() > 0) {
            emptyView.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.VISIBLE);
        }
    }

    private void parseJson1(JSONArray jsonArray) {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject go = jsonArray.optJSONObject(i);

            //订单id
            String orderId = go.optString("order_id");

            int goods_id = Integer.parseInt(go.optString("goods_id"));
            Log.d(TAG, "json_goods_id: "+goods_id +",json_order_id: "+orderId);
            //头部信息
            String time = go.optString("add_time");//时间
            time = TimeUtils.milliseconds2String((Long.parseLong(time) * 1000));
            OrderBean beanHead = new OrderBean(EvaluateOrderAdapter.TYPE1, R.layout.item_order_head, time, "交易成功");
            beanHead.setId(goods_id+"");
            beanArrayList.add(beanHead);

            //商品信息
            Goods good = new Goods();
            good.setTitleId(Integer.parseInt(orderId));//商品订单id
            good.setImgUrl(UrlUtils.baseWebsite + go.optString("goods_img"));//图片
            good.setGoodsTitle(go.optString("goods_name"));//商品名
            int num = Integer.parseInt(String.valueOf(go.opt("goods_num")));
            double goods_pay_price = Double.parseDouble(String.valueOf(go.optString("goods_pay_price")));//付款价格
            double tailPrice = num * goods_pay_price;
            DecimalFormat df=new DecimalFormat("##0.00");
            String formatGoodsPrice = df.format(tailPrice);

            good.setUserQuy(num);//购买数量
            good.setGoodsPrice(go.optString("goods_pay_price"));//购买价格
            good.setGoodsPriceOld(go.optString("goods_price"));//原价

            OrderBean orderBean = new OrderBean(EvaluateOrderAdapter.TYPE2, R.layout.item_goods, good);
            orderBean.setId(goods_id+"");
            beanArrayList.add(orderBean);

            //底部信息
            //String shippingFee = "(含" + go.optString("shipping_fee") + "元运费)";//运费
            String shippingFee =" (免邮费)";
            String goodsNum = "共" + num + "件商品";//商品数量
            OrderBean beadFoot = new OrderBean(EvaluateOrderAdapter.TYPE3, R.layout.item_order_evaluate_foot, goodsNum, "￥" + formatGoodsPrice, shippingFee);
            beadFoot.setId(goods_id+"");
            beanArrayList.add(beadFoot);
        }
    }

    private void initRecylerView() {

        recyclerView = (RecyclerView) view.findViewById(R.id.list_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        emptyView = view.findViewById(R.id.empty_view);
        adapter = new EvaluateOrderAdapter(beanArrayList, context);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                final OrderBean orderBean = beanArrayList.get(position);
                switch (view.getId()) {
                    case R.id.item_goods_rl:
                        //点击商品查看订单详情
                        int orderId = orderBean.getGoods().getTitleId();
                        Log.d(EvaluateOrderFragment.TAG, "click orderId: "+orderId);
                        Intent intent = new Intent(context, EvaluateDetailsActivity.class);
                        intent.putExtra("order_id", orderId+"");
                        startActivityForResult(intent, 1);
                        break;
                    case R.id.item_goods_order_total_after:
                        //售后
                        break;
                    case R.id.item_goods_order_total_evalute:
                        //评价
                        break;
                    case R.id.item_goods_order_total_delete:
                        Log.d(EvaluateOrderFragment.TAG, "click goodsId: "+orderBean.getId());
                        //删除订单
                        getDm().buildAlertDialogSure("您确定要删除订单吗？", new DialogHome.Callback() {
                            @Override
                            public void handleSure() {
                                requstData(1, UrlUtils.delOrder, orderBean.getId());
                            }
                        });
                        break;
                }
            }
        });
    }

    @Override
    public void handle200Data(JSONObject dataObj, String message) {
        switch (flag) {
            case 0:
                //获取数据
                initData(dataObj);
                break;
            case 1:
                //删除订单,成功后刷新数据
                requstListData();
                break;
        }
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
}