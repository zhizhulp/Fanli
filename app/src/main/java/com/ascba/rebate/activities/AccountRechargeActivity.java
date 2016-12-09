package com.ascba.rebate.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.NetworkBaseActivity;
import com.ascba.rebate.activities.login.LoginActivity;
import com.ascba.rebate.handlers.CheckThread;
import com.ascba.rebate.handlers.PhoneHandler;
import com.ascba.rebate.utils.LogUtils;
import com.ascba.rebate.utils.OrderInfoUtil2_0;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.EditTextWithCustomHint;
import com.ascba.rebate.view.pay.PayResult;
import com.yolanda.nohttp.rest.Request;

import org.json.JSONObject;

import java.util.Map;

public class AccountRechargeActivity extends NetworkBaseActivity {
    /** 支付宝支付业务：入参app_id */
    public static final String APPID = "2016091801917680";
    private SharedPreferences sf;

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
                        Toast.makeText(AccountRechargeActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
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
        sf=getSharedPreferences("first_login_success_name_password",MODE_PRIVATE);
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
            Toast.makeText(this, "请输入金额", Toast.LENGTH_SHORT).show();
            return;
        }
        sendMsgToSevr(UrlUtils.payment,0);
        CheckThread checkThread = getCheckThread();
        if(checkThread!=null){
            final ProgressDialog p=new ProgressDialog(this,R.style.dialog);
            p.setCanceledOnTouchOutside(false);
            p.setMessage("请稍后");
            Request<JSONObject> objRequest = checkThread.getObjRequest();
            objRequest.add("price",money);
            PhoneHandler phoneHandler = checkThread.getPhoneHandler();
            phoneHandler.setCallback(phoneHandler.new Callback2(){
                @Override
                public void getMessage(Message msg) {
                    p.dismiss();
                    super.getMessage(msg);
                    //服务器返回支付sign
                    JSONObject jObj = (JSONObject) msg.obj;
                    int status = jObj.optInt("status");
                    String message = jObj.optString("msg");
                    if(status==200){
                        JSONObject dataObj = jObj.optJSONObject("data");
                        String payInfo = dataObj.optString("payInfo");
                        requestForAli(payInfo);//发起支付请求
                    }
                }
            });
            checkThread.start();
            p.show();
        }
    }
}
