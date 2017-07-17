package com.ascba.rebate.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.activities.base.WebViewBaseActivity;
import com.ascba.rebate.activities.me_page.business_center_child.child.BusinessDataActivity;
import com.ascba.rebate.activities.offline_business.ToBeSuredOrdersActivity;
import com.ascba.rebate.beans.sweep.ToBeSuredOrdersEntity;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.MoneyBar;
import com.yanzhenjie.nohttp.rest.Request;

import org.json.JSONObject;

/**
 * Created by 李鹏 on 2017/04/11 0011.
 * 我——商家联盟——审核通过
 */

public class BusinessUnionActivity extends BaseNetActivity implements
        SwipeRefreshLayout.OnRefreshListener, View.OnClickListener
        , BaseNetActivity.Callback {

    private Context context;
    private MoneyBar moneyBar;
    private int finalScene;
    private TextView tvTotalMoney;
    private TextView tvTotalExtra;
    private TextView tvTotalCount;
    private TextView tvTodayMoney;
    private TextView tvTodayExtra;
    private TextView tvTodayCount;
    private MoneyBar mb;
    public static final int REQUEST_CODE=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_union);
        context = this;
        initVIew();
        requestData(UrlUtils.businessManagement,1);
    }

    private void initVIew() {
        initRefreshLayout();
        refreshLayout.setOnRefreshListener(this);
        findViewById(R.id.business_data).setOnClickListener(this);
        findViewById(R.id.business_account).setOnClickListener(this);
        findViewById(R.id.business_code).setOnClickListener(this);

        tvTotalMoney = ((TextView) findViewById(R.id.tv_total_money));
        tvTotalExtra= ((TextView) findViewById(R.id.tv_total_extra));
        tvTotalCount = ((TextView) findViewById(R.id.tv_trade_count));
        tvTodayMoney = ((TextView) findViewById(R.id.tv_cash_today));
        tvTodayExtra = ((TextView) findViewById(R.id.tv_extra_today));
        tvTodayCount = ((TextView) findViewById(R.id.tv_trade_count_today));
        mb= (MoneyBar) findViewById(R.id.uion_moneyBar);
        mb.setCallBack(new MoneyBar.CallBack() {
            @Override
            public void clickImage(View im) {
                //商家的一些资料
                Intent intent1 = new Intent(context, BusinessDataActivity.class);
                startActivity(intent1);
            }

            @Override
            public void clickComplete(View tv) {

            }
        });

    }

    @Override
    public void onRefresh() {
        requestData(UrlUtils.businessManagement,1);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.business_code:
                //立即收款
                requestData(UrlUtils.receivables, 0);
                break;
            case R.id.business_account:
                //流水记录
                Intent intent = new Intent(this, BusinessBillActivity.class);
                startActivity(intent);
                break;
            case R.id.business_data://现金确认
                requestOrders(UrlUtils.sureOrderList, 2);
                break;
        }
    }

    private void requestData(String url, int scene) {
        finalScene = scene;
        Request<JSONObject> request = buildNetRequest(url, 0, true);
        executeNetWork(request, "请稍后");
        setCallback(this);
    }
    public void requestOrders(String url, int scene) {
        Request<JSONObject> request = buildNetRequest(url, 0, true);
        finalScene = scene;
        request.add("paged", 1);
        executeNetWork(request, "请稍后");
        setCallback(this);
    }

    @Override
    public void handle200Data(JSONObject dataObj, String message) {
        stopRefersh();
        if (finalScene == 0) {
            JSONObject obj = dataObj.optJSONObject("receivables");
            String url = obj.optString("url");
            Intent intent = new Intent(this, WebViewBaseActivity.class);
            intent.putExtra("name", "我要收款");
            intent.putExtra("url", url);
            startActivity(intent);
        }else if(finalScene==1){
            JSONObject obj = dataObj.optJSONObject("businessManagement");
            tvTotalMoney.setText(obj.optString("sum_money"));
            tvTotalExtra.setText(obj.optInt("sum_score")+"");
            tvTotalCount.setText(obj.optInt("sum_count")+"");
            tvTodayMoney.setText(obj.optString("today_money"));
            tvTodayExtra.setText(obj.optInt("today_score")+"");
            tvTodayCount.setText(obj.optInt("today_count")+"");
        }else if(finalScene==2){//现金确认
            ToBeSuredOrdersEntity toBeSuredOrdersEntity = JSON.parseObject(dataObj.toString(), ToBeSuredOrdersEntity.class);
            ToBeSuredOrdersEntity.IdenInfoBean iden_info = toBeSuredOrdersEntity.getIden_info();
            if (iden_info != null) {
                Intent intent1 = new Intent(context, ToBeSuredOrdersActivity.class);
                startActivityForResult(intent1,REQUEST_CODE);
            } else {
                showToast("暂无待确认订单！");
            }

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    resetPage();
                    requestData(UrlUtils.businessManagement,1);
                }
                break;
        }

    }

    @Override
    public void handle404(String message) {
        stopRefersh();
        getDm().buildAlertDialog(message);
    }

    @Override
    public void handleNoNetWork() {
        stopRefersh();
    }

    public void stopRefersh(){
        if(refreshLayout!=null && refreshLayout.isRefreshing()){
            refreshLayout.setRefreshing(false);
        }
    }


}
