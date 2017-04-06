package com.ascba.rebate.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWork4Activity;
import com.ascba.rebate.adapter.ConfirmOrderAdapter;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.application.MyApplication;
import com.ascba.rebate.beans.Goods;
import com.ascba.rebate.beans.ReceiveAddressBean;
import com.ascba.rebate.handlers.DialogManager;
import com.ascba.rebate.utils.UrlEncodeUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.ShopABarText;
import com.ascba.rebate.view.SuperSwipeRefreshLayout;
import com.yolanda.nohttp.rest.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李鹏 on 2017/03/15 0015.
 * 确认订单
 */

public class ConfirmOrderActivity extends BaseNetWork4Activity implements SuperSwipeRefreshLayout.OnPullRefreshListener, View.OnClickListener {

    private SuperSwipeRefreshLayout refreshLat;
    private Handler handler = new Handler();
    private Context context;
    private ShopABarText shopABarText;
    private RecyclerView recyclerView;
    private DialogManager dm;
    private ArrayList<ReceiveAddressBean> beanList = new ArrayList<>();//收货地址
    private ReceiveAddressBean defaultAddressBean;//默认收货地址
    private RelativeLayout receiveAddress, noReceiveAddress;
    private TextView username;//收货人姓名
    private TextView userPhone;//收货人电话
    private TextView userAddress;//收货人地址

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_confirm_order);
        context = this;
        initUI();
        //获取收货地址
        getAddress();
    }

    private void initUI() {
        //刷新
        refreshLat = ((SuperSwipeRefreshLayout) findViewById(R.id.refresh_layout));
        refreshLat.setOnPullRefreshListener(this);

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
    }

    private List<Goods> getData() {
        List<Goods> goodsList = new ArrayList<>();
        String imgUrl = "http://image18-c.poco.cn/mypoco/myphoto/20170315/10/18505011120170315100507017_640.jpg";
        String title = "RCC男装 春夏 设计师款修身尖领翻领免烫薄长袖寸衫 韩国代购 2色";
        String standard = "颜色:深蓝色;尺码:S";
        String price = "368";
        String priceOld = "468";

        //店铺信息
        goodsList.add(new Goods(ConfirmOrderAdapter.TYPE1, R.layout.item_store, "RCC韩国代购——男装"));
        //商品信息
        goodsList.add(new Goods(ConfirmOrderAdapter.TYPE2, R.layout.item_goods, imgUrl, title, standard, price, priceOld, 1));
        //费用
        goodsList.add(new Goods(ConfirmOrderAdapter.TYPE3, R.layout.item_cost, 10, "没什么想说的，就是品牌名字不认识", 1, "368.00"));

        //店铺信息
        goodsList.add(new Goods(ConfirmOrderAdapter.TYPE1, R.layout.item_store, "RCC韩国代购——女装"));
        //商品信息
        goodsList.add(new Goods(ConfirmOrderAdapter.TYPE2, R.layout.item_goods, imgUrl, title, standard, price, priceOld, 2));
        goodsList.add(new Goods(ConfirmOrderAdapter.TYPE2, R.layout.item_goods, imgUrl, title, standard, price, priceOld, 4));
        //费用
        goodsList.add(new Goods(ConfirmOrderAdapter.TYPE3, R.layout.item_cost, 10, "没什么想说的，就是来凑内容的", 6, "2208.00"));

        //店铺信息
        goodsList.add(new Goods(ConfirmOrderAdapter.TYPE1, R.layout.item_store, "平哥国际购物中心"));
        //商品信息
        goodsList.add(new Goods(ConfirmOrderAdapter.TYPE2, R.layout.item_goods, imgUrl, title, standard, price, priceOld, 4));
        goodsList.add(new Goods(ConfirmOrderAdapter.TYPE2, R.layout.item_goods, imgUrl, title, standard, price, priceOld, 6));
        goodsList.add(new Goods(ConfirmOrderAdapter.TYPE2, R.layout.item_goods, imgUrl, title, standard, price, priceOld, 8));
        goodsList.add(new Goods(ConfirmOrderAdapter.TYPE2, R.layout.item_goods, imgUrl, title, standard, price, priceOld, 20));
        //费用
        goodsList.add(new Goods(ConfirmOrderAdapter.TYPE3, R.layout.item_cost, 10, "我想说，我平威武！！！", 38, "13984.00"));

        return goodsList;
    }

    /**
     * 获取收货地址数据
     */
    private void getAddress() {
        dm = new DialogManager(context);
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
                if (MyApplication.addressId != null) {
                    for (ReceiveAddressBean bean : beanList) {
                        if (bean.getId().equals(MyApplication.addressId)) {
                            defaultAddressBean = bean;
                        }
                    }
                } else {
                    //如果没有保存数据，就设置默认收货地址为当前地址，并保存
                    if (beanList.get(0).getIsDefault().equals("1")) {
                        defaultAddressBean = beanList.get(0);
                        MyApplication.addressId = defaultAddressBean.getId();
                    }
                }
                setReceiveData();
            }

            @Override
            public void handle404(String message) {
                dm.buildAlertDialog(message);
            }

            @Override
            public void handleNoNetWork() {
                dm.buildAlertDialog("请检查网络！");
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
            userAddress.setText(defaultAddressBean.getAddress());
        }
    }

    @Override
    public void onRefresh() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshLat.setRefreshing(false);
            }
        }, 1000);
    }

    @Override
    public void onPullDistance(int distance) {

    }

    @Override
    public void onPullEnable(boolean enable) {

    }

    /**
     * 创建订单
     *
     * @param json
     */
    private void creatOrder(JSONObject json) {
        dm = new DialogManager(context);
        Request<JSONObject> jsonRequest = buildNetRequest(UrlUtils.createOrder, 0, true);
        jsonRequest.add("sign", UrlEncodeUtils.createSign(UrlUtils.createOrder));
        jsonRequest.add("member_id", AppConfig.getInstance().getInt("uuid", -1000));
        jsonRequest.add("buy_data", json.toString());
        jsonRequest.add("member_address_id", defaultAddressBean.getId());//用户收货地址id
        jsonRequest.add("payment_type", "balance");//支付方式(余额支付：balance，支付宝：alipay，微信：wxpay)

        executeNetWork(jsonRequest, "请稍后");
        setCallback(new Callback() {
            @Override
            public void handle200Data(JSONObject dataObj, String message) {

            }

            @Override
            public void handle404(String message) {
                dm.buildAlertDialog(message);
            }

            @Override
            public void handleNoNetWork() {
                dm.buildAlertDialog("请检查网络！");
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
        }
    }

    /**
     * 接受选择地址回调结果
     *
     * @param requestCode
     * @param resultCode
     * @param data
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
}
