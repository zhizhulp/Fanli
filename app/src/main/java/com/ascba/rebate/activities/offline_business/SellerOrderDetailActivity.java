package com.ascba.rebate.activities.offline_business;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.beans.sweep.AffrimEntity;
import com.ascba.rebate.beans.sweep.CancelEntity;
import com.ascba.rebate.beans.sweep.SellerOrderInfoEntity;
import com.ascba.rebate.utils.DialogHome;
import com.ascba.rebate.utils.TimeUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.MoneyBar;
import com.squareup.picasso.Picasso;
import com.yanzhenjie.nohttp.rest.Request;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

//商家返回的信息里面待确认
public class SellerOrderDetailActivity extends BaseNetActivity implements View.OnClickListener {
    private int order_id;
    private TextView seller_order_name, seller_order_cost, seller_order_status, seller_order_paytype,
            seller_order_acount, seller_order_score, seller_order_employee, seller_order_time,
            seller_order_trade_number, seller_order_contactway;
    private ImageView seller_order_icon;
    private LinearLayout sure_order_vis;
    private String order_identity;
    private int paytype;
    private TextView seller_order_cancel, seller_order_confirm;
    private MoneyBar mb;
    private int intoType;
    private boolean backToRefresh = false;//返回操作是否刷新
    private RelativeLayout seller_order_detail_employpay, seller_order_detail_contactway;
    private Button seller_detail_close;
    private String type;//判断是否是通过用户的支付方式跳过来的
    private double member_money;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_order_detail);
        requestNetwork(UrlUtils.info, 0);
        seller_order_icon = (ImageView) findViewById(R.id.seller_order_icon);
        seller_order_name = (TextView) findViewById(R.id.seller_order_name);
        seller_order_cost = (TextView) findViewById(R.id.seller_order_cost);
        seller_order_status = (TextView) findViewById(R.id.seller_order_status);
        seller_order_paytype = (TextView) findViewById(R.id.seller_order_paytype);
        seller_order_acount = (TextView) findViewById(R.id.seller_order_acount);
        seller_order_score = (TextView) findViewById(R.id.seller_order_score);
        seller_order_employee = (TextView) findViewById(R.id.seller_order_employee);
        seller_order_time = (TextView) findViewById(R.id.seller_order_time);
        seller_detail_close = (Button) findViewById(R.id.seller_detail_close);
        seller_detail_close.setOnClickListener(new View.OnClickListener() {//用户点击关闭按钮
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED, getIntent());
                finish();
            }
        });
        mb = (MoneyBar) findViewById(R.id.seller_detail_mb);
        mb.setCallBack2(new MoneyBar.CallBack2() {
            @Override
            public void clickImage(View im) {
            }
            @Override
            public void clickComplete(View tv) {
            }
            @Override
            public void clickBack(View back) {//返回键

                if ("pay".equals(type)) {//账户支付的方式是pay的情况下
                    Intent intent1 = getIntent();
                    if(paytype==1){//记账的方式
                        intent1.putExtra("pay_type",paytype);
                    }else if(paytype==2){//余额支付
                        intent1.putExtra("pay_type",paytype);
                        intent1.putExtra("member_money", member_money);
                    }
                    setResult(RESULT_OK, intent1);
                    finish();
                } else {
                    setResultsAndBack();
                }
            }
        });
        seller_order_detail_employpay = (RelativeLayout) findViewById(R.id.seller_order_detail_employpay);
        seller_order_detail_contactway = (RelativeLayout) findViewById(R.id.seller_order_detail_contactway);
        seller_order_trade_number = (TextView) findViewById(R.id.seller_order_trade_number);
        seller_order_contactway = (TextView) findViewById(R.id.seller_order_contactway);
        sure_order_vis = (LinearLayout) findViewById(R.id.sure_order_vis);
        seller_order_cancel = (TextView) findViewById(R.id.seller_order_cancel);
        seller_order_confirm = (TextView) findViewById(R.id.seller_order_confirm);
        seller_order_cancel.setOnClickListener(this);
        seller_order_confirm.setOnClickListener(this);

    }

    //请求详情页面
    public void requestNetwork(String url, int what) {
        Request<JSONObject> request = buildNetRequest(url, 0, true);
        getOrderId();
        request.add("order_id", order_id);
        executeNetWork(what, request, "请稍后");
    }

    @Override
    protected void mhandle200Data(int what, JSONObject object, JSONObject dataObj, String message) {
        super.mhandle200Data(what, object, dataObj, message);
        switch (what) {
            case 0:
                SellerOrderInfoEntity sellerOrderInfoEntity = JSON.parseObject(dataObj.toString(), SellerOrderInfoEntity.class);
                SellerOrderInfoEntity.InfoBean infoBean = sellerOrderInfoEntity.getInfo();
                order_identity = infoBean.getOrder_identity();
                paytype = infoBean.getPay_type();
                member_money = infoBean.getMember_money();
                setOrderSure(infoBean.getOrder_status());

                seller_order_name.setText(infoBean.getName());
                seller_order_cost.setText(infoBean.getMoney());
                seller_order_status.setText(infoBean.getOrder_status_text());
                seller_order_paytype.setText(infoBean.getPay_type_text());
                seller_order_acount.setText(infoBean.getMember_username());
                seller_order_score.setText(infoBean.getScore() + "");
                seller_order_employee.setText(infoBean.getPay_commission());
                seller_order_time.setText(TimeUtils.milliseconds2String(infoBean.getCreate_time() * 1000));
                seller_order_trade_number.setText(infoBean.getOrder_number());
                seller_order_contactway.setText(infoBean.getSeller_contact());

                Picasso.with(this).load(UrlUtils.baseWebsite + infoBean.getAvatar()).into(seller_order_icon);
                break;
            case 1:
                AffrimEntity affrimEntity = JSON.parseObject(dataObj.toString(), AffrimEntity.class);
                AffrimEntity.InfoBean infoBean1 = affrimEntity.getInfo();
                seller_order_status.setText(infoBean1.getOrder_status_text());
                seller_order_time.setText(TimeUtils.milliseconds2String(infoBean1.getCreate_time() * 1000));
                mb.setTextTitle("订单详情");
                showToast(message);
                backToRefresh = true;
                break;
            case 2:
                CancelEntity cancelEntity = JSON.parseObject(dataObj.toString(), CancelEntity.class);
                CancelEntity.InfoBean info = cancelEntity.getInfo();
                seller_order_status.setText(info.getOrder_status_text());
                mb.setTextTitle("订单详情");
                showToast(message);
                backToRefresh = true;
                break;
        }
    }

    private void setOrderSure(int status) {
        if (order_identity.equals("seller")) {//是商家
            seller_order_detail_employpay.setVisibility(View.VISIBLE);//佣金可见
            if (paytype == 1) {//记账的方式
                mb.setTextTitle("订单确定");
                if (status == 0) {   //交易中
                    sure_order_vis.setVisibility(View.VISIBLE);
                }
            } else if (paytype == 2) {//余额支付的方式
                sure_order_vis.setVisibility(View.GONE);
                seller_order_detail_contactway.setVisibility(View.GONE);
            }
        } else {//消费者（记账的方式）--消费明细的入口
            sure_order_vis.setVisibility(View.GONE);
            if (paytype == 1) {//记账
                seller_order_detail_contactway.setVisibility(View.VISIBLE);//用户有商家联系方式
            } else {//余额支付
                seller_order_detail_contactway.setVisibility(View.GONE);
            }

            if ("pay".equals(type)) {//用户通过消费支付
                mb.setTextTitle("付款成功");
                seller_detail_close.setVisibility(View.VISIBLE);
            } else {//通过其他方式
                seller_detail_close.setVisibility(View.GONE);
            }
            }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.seller_order_confirm://确认
                requestConfirm(UrlUtils.affirm, 1);
                sure_order_vis.setVisibility(View.GONE);
                break;
            case R.id.seller_order_cancel://取消
                Dialog dialog1 = getDm().buildAlertDialogSure("确定取消此笔订单吗？", "取消", "确定", new DialogHome.Callback() {
                    @Override
                    public void handleSure() {//点击取消时，商家重新请求支付。
                        requestCancel(UrlUtils.cancel, 2);
                        sure_order_vis.setVisibility(View.GONE);
                    }

                });
                dialog1.show();
                break;
        }
    }

    public void requestCancel(String url, int what) {
        Request<JSONObject> request = buildNetRequest(url, 0, true);
        getOrderId();
        request.add("order_id", order_id);
        request.add("scenetype", 2);
        executeNetWork(what, request, "请稍后");

    }

    public void requestConfirm(String url, int what) {
        Request<JSONObject> request = buildNetRequest(url, 0, true);
        getOrderId();
        request.add("order_id", order_id);
        request.add("scenetype", 2);
        executeNetWork(what, request, "请稍后");

    }


    private void getOrderId() {
        Intent intent = getIntent();
        intoType = intent.getIntExtra("into_type", 0);
        if (intent != null) {
            if (intoType != 1) {
                Bundle bundle = intent.getExtras();
                String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
                if (null != extra) {//通过极光推送传过来的信息
                    try {
                        JSONObject jObj = new JSONObject(extra);
                        order_id = jObj.getInt("order_id");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else {  //通过页面跳转穿过来的信息
                order_id = intent.getIntExtra("order_id", 0);
                type = intent.getStringExtra("type");

            }
        }
    }

    @Override
    public void onBackPressed() {
        setResultsAndBack();
        super.onBackPressed();
    }

    private void setResultsAndBack() {
        if (backToRefresh) {
            setResult(RESULT_OK, getIntent());
        }
        finish();
    }
}



