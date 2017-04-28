package com.ascba.rebate.activities.shop.order;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.adapter.order.DeliverDetailsAdapter;
import com.ascba.rebate.beans.Goods;
import com.ascba.rebate.utils.PayUtils;
import com.ascba.rebate.utils.StringUtils;
import com.ascba.rebate.utils.TimeUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.ShopABarText;
import com.yanzhenjie.nohttp.rest.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李鹏 on 2017/03/15 0015.
 * 待付款订单详情
 */

public class PayDetailsActivity extends BaseNetActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener, BaseNetActivity.Callback {

    private Context context;
    private ShopABarText shopABarText;
    private RecyclerView recyclerView;
    private String orderId;
    private DeliverDetailsAdapter adapter;
    private List<Goods> goodsList = new ArrayList<>();

    private String storePhone;//商家电话
    //收货地址
    private RelativeLayout addressView;
    private LinearLayout contactStoreTx, countdownView;
    private TextView phoneTx, nameTx, addressTx;
    private TextView storeTx, orderSnTx, orderTimeTx, addWayTx;
    private TextView orderAmountTx, shippingFeeTx, vouchersFeeTx, orderPriceTx;
    private TextView payTx, deleteTx, countdownTx, closeOrderTx;

    //倒计时
    private int maxTime = 30 * 60;//单位—秒
    private Handler handler = new Handler();
    private int countdownSecond;
    private boolean isCountdown;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (countdownSecond > 0) {
                countdownTx.setText(countdownSecond / 60 + "分" + countdownSecond % 60 + "秒");
                countdownSecond--;
                handler.postDelayed(this, 1000);
            } else {
                countdownView.setVisibility(View.INVISIBLE);
                closeOrderTx.setVisibility(View.VISIBLE);
                handler.removeCallbacks(runnable);
                requstData(UrlUtils.cancelOrder, 1);
            }
        }
    };

    private int flag = 0;//0-获取数据，1-取消订单,2-付款
    private String payType;
    private PayUtils pay;
    private double balance;//账户余额


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_details);
        context = this;
        initView();
        getOrderId();
    }

    private void initView() {
        //刷新
        initRefreshLayout();
        refreshLayout.setOnRefreshListener(this);

        //导航栏
        shopABarText = (ShopABarText) findViewById(R.id.shopbar);
        shopABarText.setBtnEnable(false);
        shopABarText.setCallback(new ShopABarText.Callback() {
            @Override
            public void back(View v) {
                finish();
            }

            @Override
            public void clkBtn(View v) {

            }
        });

        //recyclerView
        recyclerView = (RecyclerView) findViewById(R.id.deliver_details_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new DeliverDetailsAdapter(R.layout.item_goods, goodsList, context);
        recyclerView.setAdapter(adapter);

        addressView = (RelativeLayout) findViewById(R.id.address);
        addressView.setOnClickListener(this);
        phoneTx = (TextView) findViewById(R.id.address_phone);
        nameTx = (TextView) findViewById(R.id.address_name);
        addressTx = (TextView) findViewById(R.id.address_address);
        storeTx = (TextView) findViewById(R.id.store_name);
        orderSnTx = (TextView) findViewById(R.id.order_sn);
        orderTimeTx = (TextView) findViewById(R.id.order_time);
        addWayTx = (TextView) findViewById(R.id.value_add_way);
        contactStoreTx = (LinearLayout) findViewById(R.id.contact_store);
        contactStoreTx.setOnClickListener(this);
        orderAmountTx = (TextView) findViewById(R.id.order_amount);
        shippingFeeTx = (TextView) findViewById(R.id.shipping_fee);
        vouchersFeeTx = (TextView) findViewById(R.id.vouchers_fee);
        orderPriceTx = (TextView) findViewById(R.id.order_price);
        payTx = (TextView) findViewById(R.id.tx_pay);
        payTx.setOnClickListener(this);
        deleteTx = (TextView) findViewById(R.id.tx_delete);
        deleteTx.setOnClickListener(this);
        countdownTx = (TextView) findViewById(R.id.tx_countdown);
        closeOrderTx = (TextView) findViewById(R.id.tx_close_order);
        countdownView = (LinearLayout) findViewById(R.id.ll_countdown);
    }


    private void getOrderId() {
        Intent intent = getIntent();
        if (intent != null) {
            orderId = intent.getStringExtra("order_id");
            if (orderId != null) {
                requstData(UrlUtils.viewOrder, 0);
            } else {
                showToast(getString(R.string.no_data_txt));
                finish();
            }
        }
    }

    /*
     获取列表数据
    */
    private void requstData(String url, int flag) {
        this.flag = flag;
        Request<JSONObject> jsonRequest = buildNetRequest(url, 0, true);
        jsonRequest.add("order_id", orderId);
        switch (flag) {
            case 2:
                //付款
                jsonRequest.add("pay_type", payType);
                break;
        }
        executeNetWork(jsonRequest, "请稍后");
        setCallback(this);
    }

    /*
        用户余额信息
        "money": "71208.92",
		"white_score": "2580",
		"red_score": "21"
     */
    private void getBalance(JSONObject dataObj) {
        JSONObject member_info = dataObj.optJSONObject("member_info");
        if (member_info != null) {
            balance = member_info.optDouble("money");//余额
            int white_score = member_info.optInt("white_score");
            int red_score = member_info.optInt("red_score");
        }
    }

    /*
        收货地址
        "id":"23",
        "member_id":"681",
        "reciver_name":"波波",
        "province":"1",
        "city":"710682",
        "District":"1106",
        "twon":"1158",
        "reciver_address":"北京市大兴区石榴庄钱来钱往",
        "reciver_mobile":"18832919903",
        "default":"1"
     */
    private void getAddress(JSONObject dataObject) {
        try {
            JSONObject addressObject = dataObject.getJSONObject("order_member_address");
            String member_id = dataObject.optString("member_id");
            String name = addressObject.optString("reciver_name");//收货人姓名
            String phone = addressObject.optString("reciver_mobile");//手机号
            String address = addressObject.optString("reciver_address");//收货地址
            String defaultAddress = addressObject.optString("default");//是否是默认地址：1——是，0——不是
            String province = addressObject.optString("province");
            String city = addressObject.optString("city");
            String district = addressObject.optString("District");
            String twon = addressObject.optString("twon");
            phoneTx.setText(phone);
            nameTx.setText(name);
            addressTx.setText(address);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*
        商家信息
        "id":"12",
        "store_name":"小米官方旗舰店",
        "member_id":"81",
        "member_name":"15501052244"
     */
    private void getStoreInfo(JSONObject dataObject) {
        try {
            JSONObject storeObject = dataObject.getJSONObject("store_info");
            String storeName = storeObject.optString("store_name");//店铺
            storePhone = storeObject.optString("member_name");
            storeTx.setText(storeName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*
       订单信息
     */
    private void getGoodsInfo(JSONObject dataObject) {
        try {
            //订单信息
            JSONObject orderObject = dataObject.getJSONObject("order_info");
            String shippingFee = orderObject.optString("shipping_fee");//邮费
            String orderStatus = orderObject.optString("order_status");//订单状态
            String orderSn = orderObject.optString("order_sn");//订单号
            String goodsAmount = orderObject.optString("goods_amount");//商品价格
            String orderAmount = orderObject.optString("order_amount");//订单价格
            String orderTime = orderObject.optString("add_time");//订单时间
            orderTime = TimeUtils.milliseconds2String(Long.parseLong(orderTime) * 1000);

            /*
               开始支付倒计时
             */
            if (!isCountdown) {
                //时间差
                countdownSecond = TimeUtils.countdownTime(maxTime, orderTime);
                isCountdown = handler.postDelayed(runnable, 1000);
                Log.d("PayDetailsActivity", "isCountdown:" + isCountdown);
            }

            orderSnTx.setText(orderSn);
            orderTimeTx.setText(orderTime);
            orderAmountTx.setText("￥" + orderAmount);
            shippingFeeTx.setText("￥" + shippingFee);

            if (goodsList.size() > 0) {
                goodsList.clear();
            }

            //商品信息
            JSONArray goodsArray = orderObject.getJSONArray("orderGoods");
            if (goodsArray != null && goodsArray.length() > 0) {
                for (int i = 0; i < goodsArray.length(); i++) {
                    JSONObject goodObject = goodsArray.getJSONObject(i);
                    String goodName = goodObject.optString("goods_name");//商品名
                    String goodsPrice = goodObject.optString("goods_price");//商品价格
                    String specNames = goodObject.optString("spec_names");//商品规格
                    String goodNum = goodObject.optString("goods_num");//数量
                    String goodImg = UrlUtils.baseWebsite + goodObject.optString("goods_img");//商品图片
                    goodsList.add(new Goods(goodImg, goodName, specNames, goodsPrice, Integer.parseInt(goodNum)));
                }
            }
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRefresh() {
        requstData(UrlUtils.viewOrder, 0);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.address:
                //选择收货地址
                break;
            case R.id.contact_store:
                //联系商家
                if (!StringUtils.isEmpty(storePhone)) {
                    Intent intent1 = new Intent();
                    intent1.setAction(Intent.ACTION_DIAL);
                    intent1.setData(Uri.parse("tel:" + storePhone));
                    startActivity(intent1);
                }
                break;
            case R.id.tx_pay:
                //付款
                String price = orderAmountTx.getText().toString();
                if (StringUtils.isEmpty(price)) {
                    showToast("正在加载订单信息，请稍后");
                } else {
                    pay = new PayUtils(this, price, balance);
                    pay.showDialog(new PayUtils.OnCreatOrder() {
                        @Override
                        public void onCreatOrder(String arg) {
                            payType = arg;
                            requstData(UrlUtils.orderPay, 2);
                        }
                    });
                }

                break;
            case R.id.tx_delete:
                //取消订单
                countdownView.setVisibility(View.INVISIBLE);
                closeOrderTx.setVisibility(View.VISIBLE);
                handler.removeCallbacks(runnable);
                requstData(UrlUtils.cancelOrder, 1);
                break;
        }
    }

    @Override
    public void handle200Data(JSONObject dataObj, String message) {
        switch (flag) {
            case 0:
                /*
                获取订单数据
                */
                if (refreshLayout.isRefreshing()) {
                    refreshLayout.setRefreshing(false);
                }

                //余额信息
                getBalance(dataObj);

                //收货地址
                getAddress(dataObj);

                //商家信息
                getStoreInfo(dataObj);

                //订单信息
                getGoodsInfo(dataObj);
                break;
            case 1:
                /*
                订单超时，自动关闭
                */
                getDm().buildAlertDialog("订单已取消!");
                finish();
                break;
            case 2:
                try {
                    if ("balance".equals(payType)) {
                        //余额支付
                        pay.dismissDialog();
                        pay.requestForYuE(dataObj);
                    } else if ("alipay".equals(payType)) {
                        String payInfo = dataObj.optString("payInfo");
                        pay.requestForAli(payInfo);//发起支付宝支付请求
                    } else if ("wxpay".equals(payType)) {
                        JSONObject wxpay = dataObj.getJSONObject("wxpay");
                        pay.requestForWX(wxpay);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
        }
    }

    @Override
    public void handle404(String message) {
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
        getDm().buildAlertDialog(message);
    }

    @Override
    public void handleNoNetWork() {
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
    }

}
