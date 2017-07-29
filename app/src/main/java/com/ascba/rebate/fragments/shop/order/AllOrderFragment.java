package com.ascba.rebate.fragments.shop.order;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.PayPsdSettingActivity;
import com.ascba.rebate.activities.base.WebViewBaseActivity;
import com.ascba.rebate.activities.shop.MyOrderActivity;
import com.ascba.rebate.activities.shop.order.CancelOrderDetailsActivity;
import com.ascba.rebate.activities.shop.order.DeliverDetailsActivity;
import com.ascba.rebate.activities.shop.order.EvaluateDetailsActivity;
import com.ascba.rebate.activities.shop.order.PayDetailsActivity;
import com.ascba.rebate.activities.shop.order.TakeDetailsActivity;
import com.ascba.rebate.adapter.order.AllOrderAdapter;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.application.MyApplication;
import com.ascba.rebate.beans.Goods;
import com.ascba.rebate.beans.OrderBean;
import com.ascba.rebate.fragments.base.BaseNetFragment;
import com.ascba.rebate.fragments.base.LazyLoadFragment;
import com.ascba.rebate.utils.DialogHome;
import com.ascba.rebate.utils.PayUtils;
import com.ascba.rebate.utils.StringUtils;
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
 * 全部订单
 */

public class AllOrderFragment extends LazyLoadFragment implements BaseNetFragment.Callback, SwipeRefreshLayout.OnRefreshListener {

    private Context context;
    //每笔订单中的商品列表
    private List<OrderBean> beanArrayList = new ArrayList<>();
    private AllOrderAdapter adapter;
    private View view;
    private int flag = 0;//0——获取数据，1——取消订单,2——删除订单
    private String balance;//账户余额
    private PayUtils pay;
    private String payType;
    private String orderAmount;

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


    //获取列表数据
    private void requstListData() {
        flag = 0;
        Request<JSONObject> jsonRequest = buildNetRequest(UrlUtils.getOrderList, 0, true);
        jsonRequest.add("status", "all");
        executeNetWork(jsonRequest, "请稍后");
        setCallback(this);
    }


    private void requstData(int flag, String url, String order_id) {
        this.flag = flag;
        Request<JSONObject> jsonRequest = buildNetRequest(url, 0, true);
        jsonRequest.add("order_id", order_id);
        if (flag == 3) {
            jsonRequest.add("pay_type", payType);
        }else if(flag ==4){
            jsonRequest.add("ordertraces","58466927852");
        }else if(flag==5){
            jsonRequest.add("ordertraces",order_id);
        }
        executeNetWork(jsonRequest, "请稍后");
        setCallback(this);
    }


    //初始化数据
    private void initData(JSONObject dataObj) {
        if (beanArrayList.size() > 0) {
            beanArrayList.clear();
        }
        //用户信息
        JSONObject member_info = dataObj.optJSONObject("member_info");
        if (member_info != null) {
            balance = member_info.optString("money");//余额
            int is_level_pwd = member_info.optInt("is_level_pwd");//是否设置支付密码
            AppConfig.getInstance().putInt("is_level_pwd", is_level_pwd);
        }
        //商品列表信息
        JSONArray jsonArray = dataObj.optJSONArray("order_list");
        if (jsonArray != null && jsonArray.length() > 0) {
            for (int i = 0; i < jsonArray.length(); i++) {
                int totalNum = 0;//购买商品数量
                JSONObject object = jsonArray.optJSONObject(i);
                //订单id
                String orderId = object.optString("order_id").trim();
                //订单状态 订单状态：0(已取消)10(默认):未付款;20:已付款;30:已发货;40:已收货;
                String orderStatus = object.optString("order_status").trim();
                //头部信息
                OrderBean beanHead = new OrderBean(AllOrderAdapter.TYPE_Head, R.layout.item_order_head, object.optString("store_name"));
                beanHead.setId(orderId);
                beanHead.setStateCode(orderStatus);

                if (orderStatus.equals("10")) {
                    //等待卖家付款
                    beanHead.setState("等待买家付款");
                } else if (orderStatus.equals("0")) {
                    //交易关闭
                    beanHead.setState("交易关闭");
                } else if (orderStatus.equals("20")) {
                    //等待卖家发货
                    beanHead.setState("等待卖家发货");
                } else if (orderStatus.equals("30")) {
                    //等待买家收货
                    beanHead.setState("卖家已发货");
                } else if (orderStatus.equals("40")) {
                    //交易成功
                    beanHead.setState("交易成功");
                }
                beanArrayList.add(beanHead);

                //商品信息
                JSONArray goodsArray = object.optJSONArray("orderGoods");
                if (goodsArray != null && goodsArray.length() > 0) {
                    for (int j = 0; j < goodsArray.length(); j++) {
                        JSONObject goodsObject = goodsArray.optJSONObject(j);
                        Goods good = new Goods();
                        good.setImgUrl(UrlUtils.getNewUrl(goodsObject.optString("goods_img")));//图片
                        good.setGoodsTitle(goodsObject.optString("goods_name"));//商品名
                        good.setGoodsId(goodsObject.optString("goods_id"));
                        good.setOrderGoodsId(goodsObject.optString("order_goods_id"));
                        good.setTitleId(Integer.parseInt(orderId));
                        int num = Integer.parseInt(String.valueOf(goodsObject.opt("goods_num")));
                        totalNum = num + totalNum;

                        good.setUserQuy(num);//购买数量
                        good.setGoodsPrice(goodsObject.optString("goods_price"));//市场价格
                        good.setGoodsPriceOld(goodsObject.optString("market_price"));//商品价格
                        good.setDeliverNum(goodsObject.optString("invoice_no"));//运单号
                        good.setTeiHui(goodsObject.optString("promotion_text"));
                        good.setUseTicketToReduce(goodsObject.optString("promotion_mark"));
                        OrderBean orderBean = new OrderBean(AllOrderAdapter.TYPE_GOODS, R.layout.item_goods, good);
                        orderBean.setId(orderId);
                        orderBean.setStateCode(orderStatus);
                        beanArrayList.add(orderBean);
                    }
                }

                //底部信息
                String orderAmount = object.optString("order_amount");//订单总价
                String shippingFee = "(含" + object.optString("shipping_fee") + "元运费)";//运费
                String goodsNum = "共" + totalNum + "件商品";//商品数量

                OrderBean beadFoot = null;
                if (orderStatus.equals("10")) {
                    //等待卖家付款
                    beadFoot = new OrderBean(AllOrderAdapter.TYPE1, R.layout.item_order_pay1_foot, goodsNum, orderAmount, shippingFee);
                } else if (orderStatus.equals("0")) {
                    //交易关闭
                    beadFoot = new OrderBean(AllOrderAdapter.TYPE2, R.layout.item_order_pay2_foot, goodsNum, orderAmount, shippingFee);
                } else if (orderStatus.equals("20")) {
                    //等待卖家发货
                    beadFoot = new OrderBean(AllOrderAdapter.TYPE3, R.layout.item_order_deliver_foot, goodsNum, orderAmount, shippingFee);
                } else if (orderStatus.equals("30")) {
                    //等待买家收货
                    beadFoot = new OrderBean(AllOrderAdapter.TYPE4, R.layout.item_order_take_foot, goodsNum, orderAmount, shippingFee);
                } else if (orderStatus.equals("40")) {
                    //交易成功
                    beadFoot = new OrderBean(AllOrderAdapter.TYPE5, R.layout.item_order_evaluate_foot, goodsNum, orderAmount, shippingFee);
                }
                if (beadFoot != null) {
                    beadFoot.setId(orderId);
                    JSONObject object1 = object.optJSONObject("seller_info");
                    if(object1!=null){
                        beadFoot.setPhone(object1.optString("store_mobile"));
                    }
                    beadFoot.setStateCode(orderStatus);
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

        adapter = new AllOrderAdapter(beanArrayList, context);
        recyclerView.setAdapter(adapter);

        adapter.setEmptyView(R.layout.order_empty_view, recyclerView);
        recyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                OrderBean orderBean = beanArrayList.get(position);
                final String orderId = orderBean.getId();
                switch (view.getId()) {
                    case R.id.item_goods_order_total_pay:
                        //付款
                        payPrice(position, orderId);
                        break;
                    case R.id.item_goods_order_total_cancel:
                        //取消订单
                        final String finalOrderId = orderId;
                        getDm().buildAlertDialogSure("您确定要取消订单吗？", new DialogHome.Callback() {
                            @Override
                            public void handleSure() {
                                requstData(1, UrlUtils.cancelOrder, finalOrderId);
                            }
                        });
                        break;
                    case R.id.item_goods_order_total_call:
                        //联系卖家
                        String phone = orderBean.getPhone();
                        if (StringUtils.isEmpty(phone)) {
                            getDm().buildAlertDialog("该商家暂无电话");
                        } else {
                            startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + orderBean.getPhone())));
                        }
                        break;
                    case R.id.item_goods_order_total_delete:
                        //删除订单
                        getDm().buildAlertDialogSure("您确定要删除订单吗？", new DialogHome.Callback() {
                            @Override
                            public void handleSure() {
                                requstData(2, UrlUtils.delOrder, orderId);
                            }
                        });
                        break;
                    case R.id.item_goods_order_total_refund:
                        //退款
                        break;
                    case R.id.item_goods_order_total_logistics:
                        //查看物流
                        break;
                    case R.id.item_goods_order_total_take:
                        //确认收货
                        getDm().buildAlertDialogSure("您确定要确认收货吗？", new DialogHome.Callback() {
                            @Override
                            public void handleSure() {
                                requstData(5,UrlUtils.orderReceive, orderId);
                            }
                        });

                        break;
                    case R.id.item_goods_order_total_after:
                        //售后
                        break;
                    case R.id.item_goods_order_total_evalute:
                        //评价
                        break;
                    case R.id.tv_express_flow:
                        requstData(4,UrlUtils.getAuctionExp,null);
                        break;
                }
            }

            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemClick(adapter, view, position);
                Log.d(TAG, "onItemClick: ");
                OrderBean orderBean = beanArrayList.get(position);
                if(orderBean.getGoods()!=null){
                    Log.d(TAG, "onItemClick: order_id "+orderBean.getGoods().getTitleId());
                    Intent intent = new Intent();
                    String orderStatus = orderBean.getStateCode();
                    if (orderStatus.equals("10")) {
                        //等待卖家付款
                        intent.setClass(context, PayDetailsActivity.class);
                    } else if (orderStatus.equals("0")) {
                        //交易关闭
                        intent.setClass(context, CancelOrderDetailsActivity.class);
                    } else if (orderStatus.equals("20")) {
                        //等待卖家发货
                        intent.setClass(context, DeliverDetailsActivity.class);
                    } else if (orderStatus.equals("30")) {
                        //卖家已发货
                        intent.setClass(context, TakeDetailsActivity.class);
                    } else if (orderStatus.equals("40")) {
                        //交易成功
                        intent.setClass(context, EvaluateDetailsActivity.class);
                    }
                    intent.putExtra("order_id", orderBean.getGoods().getTitleId()+"");
                    startActivityForResult(intent, 1);
                }
            }
        });
        initRefreshLayout(view);
        refreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        requstListData();
    }

    @Override
    public void handle200Data(JSONObject dataObj, String message) {
        switch (flag) {
            case 0:
                //获取数据
                initData(dataObj);
                break;
            case 1:
                //取消订单,成功后刷新数据
                requstListData();
                MyApplication.isRefreshOrderCount = true;
                break;
            case 2:
                //删除订单,成功后刷新数据
                requstListData();
                MyApplication.isRefreshOrderCount = true;
                break;
            case 3:
                if ("balance".equals(payType)) {
                    //余额支付
                    pay.dismissDialog();
                    pay.requestForYuE(dataObj);
                } else if ("alipay".equals(payType)) {
                    String payInfo = dataObj.optString("payInfo");
                    pay.requestForAli(payInfo);//发起支付宝支付请求
                } else if ("wxpay".equals(payType)) {
                    JSONObject wxpay = dataObj.optJSONObject("wxpay");
                    pay.requestForWX(wxpay);
                }
                MyApplication.isRefreshOrderCount = true;
                break;
            case 4:
                String url = dataObj.optJSONObject("auction_exp").optString("exp_url");
                Intent intent=new Intent(getActivity(), WebViewBaseActivity.class);
                intent.putExtra("name","物流信息");
                intent.putExtra("url",url);
                startActivity(intent);
                break;
            case 5:
                showToast(message);
                requstListData();
                break;
        }
    }

    @Override
    public void handleReqFailed() {
    }

    @Override
    public void handle404(String message, JSONObject dataObj) {
        if (flag == 3) {
            PayUtils.onPayCallBack payCallBack = pay.getPayCallBack();
            if (payCallBack != null) {
                pay.getPayCallBack().onFinish(payType);
                pay.getPayCallBack().onCancel(payType);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            requstListData();
        }
    }

    //点击付款
    private void payPrice(int position, final String orderId) {
        orderAmount = beanArrayList.get(position).getOrderPrice();
        //开启支付
        pay = new PayUtils(getActivity(), orderAmount, balance);
        pay.showDialog(new PayUtils.OnCreatOrder() {
            @Override
            public void onCreatOrder(String arg) {
                payType = arg;
                if (StringUtils.isEmpty(balance)) {
                    balance = "0";
                }
                if (StringUtils.isEmpty(orderAmount)) {
                    orderAmount = "0";
                }

                if ("balance".equals(payType) && Double.parseDouble(orderAmount) > Double.parseDouble(balance)) {
                    showToast("余额不足");
                    return;
                }
                //检测用户是否设置了支付密码
                if ("balance".equals(payType) && AppConfig.getInstance().getInt("is_level_pwd", 0) == 0) {
                    getDm().buildAlertDialogSure("您还未设置支付密码，是否去设置？", new DialogHome.Callback() {
                        @Override
                        public void handleSure() {
                            Intent intent1 = new Intent(getActivity(), PayPsdSettingActivity.class);
                            startActivity(intent1);
                        }
                    });
                    return;
                }
                requstData(3, UrlUtils.orderPay, orderId);
            }
        });
        //支付回调
        pay.setPayCallBack(new PayUtils.onPayCallBack() {
            @Override
            public void onFinish(String payStype) {

            }

            @Override
            public void onSuccess(String payStype) {
                //刷新数据
                //requstListData();
                MyOrderActivity.setCurrTab(2);
                showToast("成功支付");
            }

            @Override
            public void onCancel(String payStype) {
                showToast("取消支付");
            }

            @Override
            public void onFailed(String payStype, String msg) {
                showToast(msg);
            }

            @Override
            public void onNetProblem(String payStype) {
                showToast("手机网络有问题");
            }
        });

    }

    @Override
    public void handleReLogin() {

    }

    @Override
    public void handleNoNetWork() {

    }
}