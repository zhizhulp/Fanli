package com.ascba.rebate.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWorkActivity;
import com.ascba.rebate.handlers.DialogManager;
import com.ascba.rebate.utils.LogUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.EditTextWithCustomHint;
import com.ascba.rebate.view.pay.PayResult;
import com.yolanda.nohttp.rest.Request;

import org.json.JSONObject;

import java.util.Map;

public class AccountRechargeActivity extends BaseNetWorkActivity implements BaseNetWorkActivity.Callback {
    private DialogManager dm=new DialogManager(this);

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    LogUtils.PrintLog("pay_result-->",payResult.toString());
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(AccountRechargeActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        Intent intent = getIntent();
                        setResult(RESULT_OK,intent);
                        finish();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        dm.buildAlertDialog("支付失败");
                    }
                    break;
                }
                default:
                    break;
            }
        };
    };

    private static final int SDK_PAY_FLAG = 1;
    private EditTextWithCustomHint edMoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_recharge);
        initViews();
    }

    private void initViews() {
        edMoney = ((EditTextWithCustomHint) findViewById(R.id.ed_recharge_money));
    }

    private void requestForAli(final String payInfo) {

        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(AccountRechargeActivity.this);
                Map<String, String> result = alipay.payV2(payInfo, true);
                Log.i("msp", result.toString());
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    public void goRechargeActivity(View view) {
        requestForServer();
    }

    private void requestForServer() {
        String money = edMoney.getText().toString();

        if("".equals(money)){
            dm.buildAlertDialog("请输入金额");
            return;
        }
        Request<JSONObject> objRequest = buildNetRequest(UrlUtils.payment, 0, true);
        objRequest.add("price",money);
        executeNetWork(objRequest,"请稍后");
        setCallback(this);
    }

    @Override
    public void handle200Data(JSONObject dataObj, String message) {
        String payInfo = dataObj.optString("payInfo");
        requestForAli(payInfo);//发起支付请求
    }
}
