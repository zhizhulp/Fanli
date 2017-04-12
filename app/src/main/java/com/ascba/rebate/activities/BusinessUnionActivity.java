package com.ascba.rebate.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWork4Activity;
import com.ascba.rebate.activities.base.WebViewBaseActivity;
import com.ascba.rebate.activities.me_page.business_center_child.child.BusinessDataActivity;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.MoneyBar;
import com.ascba.rebate.view.SuperSwipeRefreshLayout;
import com.yolanda.nohttp.rest.Request;

import org.json.JSONObject;

/**
 * Created by 李鹏 on 2017/04/11 0011.
 * 我——商家联盟——审核通过
 */

public class BusinessUnionActivity extends BaseNetWork4Activity implements SuperSwipeRefreshLayout.OnPullRefreshListener, View.OnClickListener
            ,BaseNetWork4Activity.Callback{

    private Context context;
    private SuperSwipeRefreshLayout refreshLayout;
    private MoneyBar moneyBar;
    private Handler handler = new Handler();
    private int finalScene;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_union);
        context = this;
        initVIew();
    }

    public static void startIntent(Context context, String json) {
        Intent intent = new Intent(context, BusinessUnionActivity.class);
        intent.putExtra("json", json);
        context.startActivity(intent);
    }

    private void initVIew() {
        refreshLayout = (SuperSwipeRefreshLayout) findViewById(R.id.refresh_layout);
        refreshLayout.setOnPullRefreshListener(this);

        moneyBar = (MoneyBar) findViewById(R.id.moneyBar);
        moneyBar.setCallBack(new MoneyBar.CallBack() {
            @Override
            public void clickImage(View im) {

            }

            @Override
            public void clickComplete(View tv) {
                finish();
            }
        });

        findViewById(R.id.business_data).setOnClickListener(this);
        findViewById(R.id.business_account).setOnClickListener(this);
        findViewById(R.id.business_code).setOnClickListener(this);
    }

    @Override
    public void onRefresh() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(false);
            }
        }, 1000);
    }

    @Override
    public void onPullDistance(int distance) {

    }

    @Override
    public void onPullEnable(boolean enable) {

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
                Intent intent=new Intent(this,BusiFlowRecordsActivity.class);
                startActivity(intent);
                break;
            case R.id.business_data:
                //商家资料
                getData();
                break;
        }
    }

    private void requestData(String url, int scene) {
        finalScene=scene;
        Request<JSONObject> request = buildNetRequest(url, 0, true);
        executeNetWork(request,"请稍后");
        setCallback(this);
    }

    /*
    解析商家信息，并跳转到资料页面
     */
    private void getData() {
        Intent dataIntent = getIntent();
        try {
            String dataIntentStringExtra = dataIntent.getStringExtra("json");
            JSONObject dataObj = new JSONObject(dataIntentStringExtra);
            JSONObject company = dataObj.optJSONObject("company");
            int seller_enable_time = dataObj.optInt("seller_enable_time");
            String seller_enable_tip = dataObj.optString("seller_enable_tip");

            String seller_name = company.optString("seller_name");
            String seller_cover_logo = company.optString("seller_cover_logo");
            String seller_image = company.optString("seller_image");
            String seller_taglib = company.optString("seller_taglib");
            String seller_address = company.optString("seller_address");
            String seller_localhost = company.optString("seller_localhost");
            String seller_lon = company.optString("seller_lon");
            String seller_lat = company.optString("seller_lat");
            String seller_tel = company.optString("seller_tel");
            String seller_business_hours = company.optString("seller_business_hours");
            String seller_return_ratio = company.optString("seller_return_ratio");
            String seller_return_ratio_tip = company.optString("seller_return_ratio_tip");
            String seller_description = company.optString("seller_description");
            Intent intent = new Intent(context, BusinessDataActivity.class);
            intent.putExtra("seller_name", seller_name);
            intent.putExtra("seller_cover_logo", seller_cover_logo);
            intent.putExtra("seller_image", seller_image);
            intent.putExtra("seller_taglib", seller_taglib);
            intent.putExtra("seller_address", seller_address);
            intent.putExtra("seller_localhost", seller_localhost);
            intent.putExtra("seller_lon", seller_lon);
            intent.putExtra("seller_lat", seller_lat);
            intent.putExtra("seller_tel", seller_tel);
            intent.putExtra("seller_business_hours", seller_business_hours);
            intent.putExtra("seller_return_ratio", seller_return_ratio);
            intent.putExtra("seller_return_ratio_tip", seller_return_ratio_tip);
            intent.putExtra("seller_description", seller_description);
            intent.putExtra("seller_enable_time", seller_enable_time);
            intent.putExtra("seller_enable_tip", seller_enable_tip);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handle200Data(JSONObject dataObj, String message) {
        if(finalScene==0){
            JSONObject obj = dataObj.optJSONObject("receivables");
            String url = obj.optString("url");
            Intent intent = new Intent(this, WebViewBaseActivity.class);
            intent.putExtra("name", "收款");
            intent.putExtra("url", url);
            startActivity(intent);
        }
    }

    @Override
    public void handle404(String message) {
        getDm().buildAlertDialog(message);
    }

    @Override
    public void handleNoNetWork() {
        getDm().buildAlertDialog(getString(R.string.no_network));
    }
}
