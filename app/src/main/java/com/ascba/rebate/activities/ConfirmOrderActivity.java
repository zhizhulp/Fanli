package com.ascba.rebate.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.activities.shop.order.DeliverDetailsActivity;
import com.ascba.rebate.activities.shop.order.PayDetailsActivity;
import com.ascba.rebate.adapter.ConfirmOrderAdapter;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.application.MyApplication;
import com.ascba.rebate.beans.Goods;
import com.ascba.rebate.beans.ReceiveAddressBean;
import com.ascba.rebate.utils.PayUtils;
import com.ascba.rebate.utils.StringUtils;
import com.ascba.rebate.utils.UrlEncodeUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.ShopABarText;
import com.yanzhenjie.nohttp.rest.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李鹏 on 2017/03/15 0015.
 * 购物车——结算——确认订单
 */

public class ConfirmOrderActivity extends BaseNetActivity implements View.OnClickListener {

    private Context context;
    private ShopABarText shopABarText;
    private RecyclerView recyclerView;
    private ArrayList<ReceiveAddressBean> beanList = new ArrayList<>();//收货地址
    private ReceiveAddressBean defaultAddressBean;//默认收货地址
    private RelativeLayout receiveAddress, noReceiveAddress;
    private TextView username;//收货人姓名
    private TextView userPhone;//收货人电话
    private TextView userAddress;//收货人地址
    private String json_data;
    private TextView tvTotal;
    private List<Goods> goodsList = new ArrayList<>();
    private JSONObject jsonMessage = new JSONObject();//留言信息
    private DecimalFormat fnum = new DecimalFormat("##0.00");//格式化，保留两位
    private PayUtils pay;
    private String balance;//账户余额
    private String orderId;//订单id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_confirm_order);
        context = this;
        getDataFromIntent();
        initUI();

        //获取收货地址
        getAddress();

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        json_data = intent.getStringExtra("json_data");
    }

    private void initUI() {

        //总金额
        tvTotal = ((TextView) findViewById(R.id.confir_order_text_total_price));

        //提交订单
        findViewById(R.id.confir_order_btn_commit).setOnClickListener(this);
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

        /**
         * 收货人信息
         */
        receiveAddress = (RelativeLayout) findViewById(R.id.confirm_order_addrss_rl);
        receiveAddress.setOnClickListener(this);

        noReceiveAddress = (RelativeLayout) findViewById(R.id.confirm_order_addrss_rl2);
        noReceiveAddress.setOnClickListener(this);

        username = (TextView) findViewById(R.id.confirm_order_username);
        userPhone = (TextView) findViewById(R.id.confirm_order_phone);
        userAddress = (TextView) findViewById(R.id.confirm_order_address);

        //recyclerView
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        ConfirmOrderAdapter confirmOrderAdapter = new ConfirmOrderAdapter(context, getData());
        recyclerView.setAdapter(confirmOrderAdapter);

        //买家留言
        confirmOrderAdapter.setEditTextString(new ConfirmOrderAdapter.editTextString() {
            @Override
            public void getString(String content, int storeId, String mesaagesCartId) {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("cart_ids", mesaagesCartId);
                    jsonObject.put("message", content);
                    jsonMessage.put(String.valueOf(storeId), jsonObject);
                    Log.d("ConfirmOrderActivity", jsonMessage.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private List<Goods> getData() {
        try {
            if (goodsList.size() != 0) {
                goodsList.clear();
            }
            JSONObject dataObj = new JSONObject(json_data);

            //用户信息
            JSONObject member_info = dataObj.optJSONObject("member_info");
            balance = member_info.optString("money");//余额
            int white_score = member_info.optInt("white_score");
            int red_score = member_info.optInt("red_score");

            //商品店铺信息
            JSONArray storeList = dataObj.optJSONArray("order_store_list");
            if (storeList != null && storeList.length() != 0) {
                float totalPrice = 0;
                for (int i = 0; i < storeList.length(); i++) {
                    JSONObject storeObj = storeList.optJSONObject(i);
                    if (storeObj != null) {
                        JSONObject titleObj = storeObj.optJSONObject("store_info");
                        goodsList.add(new Goods(ConfirmOrderAdapter.TYPE1, R.layout.item_store, titleObj.optString("store_name")));
                        JSONArray goodsArray = storeObj.optJSONArray("goods_list");
                        if (goodsArray != null && goodsArray.length() != 0) {
                            float yunfei = 0;//运费
                            int num = 0;
                            float price = 0;
                            int storeId = 0;
                            String cartId = null;
                            StringBuffer mesaagesCartId = new StringBuffer();
                            for (int j = 0; j < goodsArray.length(); j++) {
                                JSONObject obj = goodsArray.optJSONObject(j);
                                String goods_price = obj.optString("goods_price");
                                String goods_num = obj.optString("goods_num");
                                //商品信息
                                goodsList.add(new Goods(ConfirmOrderAdapter.TYPE2, R.layout.item_goods, UrlUtils.baseWebsite + obj.optString("goods_img"),
                                        obj.optString("goods_name"), obj.optString("spec_names"), goods_price,
                                        "no_old_price", Integer.parseInt(goods_num)));

                                num += Integer.parseInt(goods_num);
                                price += Float.parseFloat(goods_price) * Integer.parseInt(goods_num);

                                try {
                                    //店铺id
                                    storeId = Integer.valueOf(String.valueOf(obj.opt("store_id")));
                                    //购物车id
                                    cartId = obj.optString("cart_id");
                                    mesaagesCartId.append(cartId + ",");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            /**
                             * 拼接空白留言信息
                             */
                            JSONObject jsonObject = new JSONObject();
                            try {
                                mesaagesCartId.delete(mesaagesCartId.length() - 1, mesaagesCartId.length());
                                jsonObject.put("cart_ids", mesaagesCartId.toString());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            jsonObject.put("message", "");
                            jsonMessage.put(String.valueOf(storeId), jsonObject);

                            price += yunfei;
                            totalPrice += price;
                            goodsList.add(new Goods(ConfirmOrderAdapter.TYPE3, R.layout.item_cost, fnum.format(yunfei), num, fnum.format(price), storeId, mesaagesCartId.toString()));
                        }
                    }
                }
                tvTotal.setText("￥" + fnum.format(totalPrice));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return goodsList;
    }

    /*
     * 获取收货地址数据
     */
    private void getAddress() {
        Request<JSONObject> jsonRequest = buildNetRequest(UrlUtils.getMemberAddress, 0, true);
        jsonRequest.add("sign", UrlEncodeUtils.createSign(UrlUtils.getMemberAddress));
        jsonRequest.add("member_id", AppConfig.getInstance().getInt("uuid", -1000));
        executeNetWork(jsonRequest, "请稍后");
        setCallback(new Callback() {
            @Override
            public void handle200Data(JSONObject dataObj, String message) {
                beanList.clear();
                JSONArray jsonArray = dataObj.optJSONArray("member_address_list");
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject object = jsonArray.getJSONObject(i);
                        ReceiveAddressBean addressBean = new ReceiveAddressBean();
                        addressBean.setId(object.optString("id"));
                        addressBean.setName(object.optString("consignee"));
                        addressBean.setPhone(object.optString("mobile"));
                        addressBean.setAddress(object.optString("address"));
                        addressBean.setAddressDetl(object.optString("address_detail"));
                        addressBean.setProvince(object.optString("province"));
                        addressBean.setCity(object.optString("city"));
                        addressBean.setDistrict(object.optString("district"));
                        addressBean.setTwon(object.optString("twon"));
                        String isSelected = object.optString("default");
                        addressBean.setIsDefault(isSelected);
                        if (isSelected.equals("1")) {
                            beanList.add(0, addressBean);
                        } else {
                            beanList.add(addressBean);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                //如果保存了收货地址id就遍历查找
                if (!StringUtils.isEmpty(MyApplication.addressId)) {
                    for (ReceiveAddressBean bean : beanList) {
                        if (bean.getId().equals(MyApplication.addressId)) {
                            defaultAddressBean = bean;
                        }
                    }
                } else {
                    //如果没有保存数据，就设置默认收货地址为当前地址，并保存
                    if (beanList.size() != 0 && beanList.get(0).getIsDefault().equals("1")) {
                        defaultAddressBean = beanList.get(0);
                        MyApplication.addressId = defaultAddressBean.getId();
                    }
                }
                setReceiveData();
            }

            @Override
            public void handle404(String message) {
                getDm().buildAlertDialog(message);
            }

            @Override
            public void handleNoNetWork() {
            }
        });
    }

    /**
     * set收货地址信息
     */
    private void setReceiveData() {
        if (defaultAddressBean == null) {
            receiveAddress.setVisibility(View.GONE);
            noReceiveAddress.setVisibility(View.VISIBLE);
        } else {
            receiveAddress.setVisibility(View.VISIBLE);
            noReceiveAddress.setVisibility(View.GONE);
            /**
             * 初始化收货地址数据
             */
            username.setText(defaultAddressBean.getName());
            userPhone.setText(defaultAddressBean.getPhone());
            userAddress.setText(defaultAddressBean.getAddressDetl());
        }
    }

    /*
     * 创建订单
     */
    private void creatOrder(String receiveId, String message, final String payType) {
        Request<JSONObject> jsonRequest = buildNetRequest(UrlUtils.createOrder, 0, true);
        jsonRequest.add("member_id", AppConfig.getInstance().getInt("uuid", -1000));
        jsonRequest.add("extra_data", message);
        jsonRequest.add("member_address_id", receiveId);//用户收货地址id
        jsonRequest.add("payment_type", payType);//支付方式(余额支付：balance，支付宝：alipay，微信：wxpay)
        executeNetWork(jsonRequest, "请稍后");
        setCallback(new Callback() {
            @Override
            public void handle200Data(JSONObject dataObj, String message) {
                MyApplication.isLoadCartData = true;//需要刷新购物车数据
                //创建订单并开始支付
                payOrder(dataObj, payType, message);
            }

            @Override
            public void handle404(String message) {
                getDm().buildAlertDialog(message);
            }

            @Override
            public void handleNoNetWork() {

            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirm_order_addrss_rl:
                //选择收货地址
                Intent intent = new Intent(context, SelectAddrssActivity.class);
                intent.putParcelableArrayListExtra("address", beanList);
                startActivityForResult(intent, 1);
                break;
            case R.id.confirm_order_addrss_rl2:
                //选择收货地址
                Intent intent2 = new Intent(context, SelectAddrssActivity.class);
                intent2.putParcelableArrayListExtra("address", beanList);
                startActivityForResult(intent2, 1);
                break;
            case R.id.confir_order_btn_commit:
                //提交订单
                if (defaultAddressBean != null && !StringUtils.isEmpty(defaultAddressBean.getId())) {
                    pay = new PayUtils(this, tvTotal.getText().toString(), balance);
                    pay.showDialog(new PayUtils.OnCreatOrder() {
                        @Override
                        public void onCreatOrder(String payType) {
                            creatOrder(defaultAddressBean.getId(), jsonMessage.toString(), payType);
                        }
                    });
                } else {
                    showToast("请先填写收货地址");
                }
                break;
        }
    }

    /**
     * 接受选择地址回调结果
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            getAddress();//刷新数据
            if (resultCode == 1 && data != null) {
                //更改当前收货地址
                defaultAddressBean = data.getParcelableExtra("address");
                setReceiveData();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.addressId = null;
    }

    /**
     * 支付
     */
    private void payOrder(JSONObject dataObj, final String payType, String message) {

        orderId = dataObj.optString("order_id", null);
        MyApplication.orderId = orderId;
        JSONObject object = dataObj.optJSONObject("payInfo");
        //调起支付
        if ("balance".equals(payType)) {
            //余额支付
            pay.dismissDialog();
            if (object != null) {
                pay.requestForYuE(dataObj);
            } else {
                //余额不足
                showToast(message);
                //跳转待付款列表
                MyOrderActivity.startIntent(context, 1);
                finish();
            }
        } else if ("alipay".equals(payType)) {
            String payInfo = dataObj.optString("payInfo");
            pay.requestForAli(payInfo);//发起支付宝支付请求
        } else if ("wxpay".equals(payType)) {
            JSONObject wxpay = dataObj.optJSONObject("wxpay");
            pay.requestForWX(wxpay);
        }


        /**
         * 支付结果回调
         */
        pay.setPayCallBack(new PayUtils.onPayCallBack() {
            @Override
            public void onFinish(String payStype) {
                if ("balance".equals(payType)) {
                    finish();
                } else if ("alipay".equals(payType)) {
                    //支付宝支付
                    setResult(RESULT_OK, getIntent());
                    finish();
                } else if ("wxpay".equals(payType)) {
                    finish();
                    //微信支付
                    MyApplication.payType = 1;
                }
            }

            @Override
            public void onSuccess(String payStype, String resultStatus) {
                showToast("成功支付");
                if (StringUtils.isEmpty(orderId)) {
                    //跳转待付款列表
                    MyOrderActivity.startIntent(context, 2);
                } else {
                    Intent intent = new Intent(context, DeliverDetailsActivity.class);
                    intent.putExtra("order_id", orderId);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancel(String payStype, String resultStatus) {
                showToast("取消支付");
                if (StringUtils.isEmpty(orderId)) {
                    //跳转待付款列表
                    MyOrderActivity.startIntent(context, 1);
                } else {
                    Intent intent = new Intent(context, PayDetailsActivity.class);
                    intent.putExtra("order_id", orderId);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailed(String payStype, String resultStatus) {
                showToast("支付失败");
            }

            @Override
            public void onNetProblem(String payStype, String resultStatus) {
                showToast("支付失败");
            }
        });

    }

}
