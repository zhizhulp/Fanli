package com.ascba.rebate.activities.offline_business;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.beans.sweep.SellerOrderInfoEntity;
import com.ascba.rebate.utils.DialogHome;
import com.ascba.rebate.utils.TimeUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.squareup.picasso.Picasso;
import com.yanzhenjie.nohttp.rest.Request;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

//商家返回的信息里面待确认
public class SellerOrderDetailActivity extends BaseNetActivity implements View.OnClickListener {
    private int order_id;
    private TextView seller_order_name,seller_order_cost,seller_order_status,seller_order_paytype,
            seller_order_acount,seller_order_score,seller_order_employee,seller_order_time,
            seller_order_trade_number,seller_order_contactway;
    private ImageView seller_order_icon;

    private RelativeLayout sure_order_vis;
    private String order_identity;
    private int paytype;
    private TextView seller_order_cancel,seller_order_confirm;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_order_detail);
        requestNetwork(UrlUtils.info,0);
        seller_order_icon= (ImageView) findViewById(R.id.seller_order_icon);
        seller_order_name= (TextView) findViewById(R.id.seller_order_name);
        seller_order_cost= (TextView) findViewById(R.id.seller_order_cost);
        seller_order_status= (TextView) findViewById(R.id.seller_order_status);
        seller_order_paytype= (TextView) findViewById(R.id.seller_order_paytype);
        seller_order_acount= (TextView) findViewById(R.id.seller_order_acount);
        seller_order_score= (TextView) findViewById(R.id.seller_order_score);
        seller_order_employee= (TextView) findViewById(R.id.seller_order_employee);
        seller_order_time= (TextView) findViewById(R.id.seller_order_time);

        seller_order_trade_number= (TextView) findViewById(R.id.seller_order_trade_number);
        seller_order_contactway= (TextView) findViewById(R.id.seller_order_contactway);
        sure_order_vis= (RelativeLayout) findViewById(R.id.sure_order_vis);
        seller_order_cancel= (TextView) findViewById(R.id.seller_order_cancel);
        seller_order_confirm= (TextView) findViewById(R.id.seller_order_confirm);
        seller_order_cancel.setOnClickListener(this);
        seller_order_confirm.setOnClickListener(this);

    }


    public void requestNetwork(String url, int what){
        Request<JSONObject> request = buildNetRequest(url, 0, true);
        getOrderId();
        request.add("order_id",order_id);
        executeNetWork(what, request, "请稍后");
    }

    @Override
    protected void mhandle200Data(int what, JSONObject object, JSONObject dataObj, String message) {
        super.mhandle200Data(what, object, dataObj, message);
        switch (what){
            case 0:

                SellerOrderInfoEntity sellerOrderInfoEntity = JSON.parseObject(dataObj.toString(), SellerOrderInfoEntity.class);
                SellerOrderInfoEntity.InfoBean infoBean = sellerOrderInfoEntity.getInfo();
                order_identity=infoBean.getOrder_identity();
                paytype=infoBean.getPay_type();
                setOrderSure();

                seller_order_name.setText(infoBean.getName());
                seller_order_cost.setText(infoBean.getMoney());
                seller_order_status.setText(infoBean.getOrder_status_text());
                seller_order_paytype.setText(infoBean.getPay_type_text());
                seller_order_acount.setText(infoBean.getMember_username());
                seller_order_score.setText(infoBean.getScore()+"");
                seller_order_employee.setText(infoBean.getPay_commission());
                seller_order_time.setText(TimeUtils.milliseconds2String(infoBean.getCreate_time()*1000));
                seller_order_trade_number.setText(infoBean.getOrder_number());
                seller_order_contactway.setText(infoBean.getSeller_contact());
                Picasso.with(this).load(UrlUtils.baseWebsite+infoBean.getAvatar()).into(seller_order_icon);
                break;
            case 1:
                Log.d("fanxi",object.toString());
                showToast("订单已取消！");

                break;
            case 2:
                break;

        }




    }

    private void setOrderSure() {
      if(order_identity.equals("seller")){//是商家
          if(paytype==1){//记账的方式
              sure_order_vis.setVisibility(View.VISIBLE);
          }else if(paytype==2){//余额支付的方式
              sure_order_vis.setVisibility(View.GONE);
          }
      }else{//消费者（记账的方式）
          //点击支付成功——到订单详情页-再次请求接口
          getOrderId();

      }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.seller_order_confirm:
                requestConfirm(UrlUtils.affirm,1);
                break;
            case R.id.seller_order_cancel:
                Dialog dialog1 = getDm().buildAlertDialog2("确定取消此笔订单吗？", new DialogHome.Callback() {
                    @Override
                    public void handleSure() {//点击取消时，商家重新请求支付。
                        requestCancel(UrlUtils.cancel,2);
                    }
                 });
                dialog1.show();
                break;


        }
    }

    public void requestCancel(String url, int what) {
        Request<JSONObject> request = buildNetRequest(url, 0, true);
        getOrderId();
        request.add("order_id",order_id);
        request.add("scenetype",2);
        executeNetWork(what, request, "请稍后");

    }

    public void requestConfirm(String url, int what) {
        Request<JSONObject> request = buildNetRequest(url, 0, true);
        getOrderId();
        request.add("order_id",order_id);
        request.add("scenetype",2);
        executeNetWork(what, request, "请稍后");

    }


    private void getOrderId(){
        Intent intent = getIntent();
        if (null != intent) {
            Bundle bundle = getIntent().getExtras();
            Log.d("fanxi",bundle.toString());
            String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
            try {
                JSONObject jObj = new JSONObject(extra);
                order_id = jObj.getInt("order_id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


}
