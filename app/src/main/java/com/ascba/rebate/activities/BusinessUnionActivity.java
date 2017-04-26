package com.ascba.rebate.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.activities.base.WebViewBaseActivity;
import com.ascba.rebate.activities.me_page.business_center_child.child.BusinessDataActivity;
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
    private Handler handler = new Handler();
    private int finalScene;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_union);
        context = this;
        initVIew();
    }

    private void initVIew() {
        initRefreshLayout();
        refreshLayout.setOnRefreshListener(this);

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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.business_code:
                //立即收款
                requestData(UrlUtils.receivables, 0);
                break;
            case R.id.business_account:
                //流水记录
                Intent intent = new Intent(this, BusiFlowRecordsActivity.class);
                startActivity(intent);
                break;
            case R.id.business_data:
                //商家资料
                //getData();
                Intent intent1 = new Intent(context, BusinessDataActivity.class);
                startActivity(intent1);
                break;
        }
    }

    private void requestData(String url, int scene) {
        finalScene = scene;
        Request<JSONObject> request = buildNetRequest(url, 0, true);
        executeNetWork(request, "请稍后");
        setCallback(this);
    }


    @Override
    public void handle200Data(JSONObject dataObj, String message) {
        if (finalScene == 0) {
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
    }


}
