package com.ascba.rebate.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import com.ascba.rebate.handlers.CheckThread;
import com.ascba.rebate.handlers.PhoneHandler;
import com.ascba.rebate.utils.LogUtils;
import com.ascba.rebate.utils.OrderInfoUtil2_0;
import com.ascba.rebate.view.EditTextWithCustomHint;
import com.ascba.rebate.view.pay.PayResult;
import com.yolanda.nohttp.rest.Request;

import org.json.JSONObject;

import java.util.Map;

public class AccountRechargeActivity extends NetworkBaseActivity {
    /** 支付宝支付业务：入参app_id */
    public static final String APPID = "2016091801917680";

    /** 支付宝账户登录授权业务：入参pid值 */
    public static final String PID = "2088421801134208";
    /** 支付宝账户登录授权业务：入参target_id值 */
    public static final String TARGET_ID = "qlqw201611281827";
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

    /** 商户私钥，pkcs8格式 */
    public static final String RSA_PRIVATE =
                    "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAM1rDcR2khMg29/8" +
                    "1lMtHn8Wh2r9rgqfoPEPtx23y7CQGjsh8tJIob9kVQstsexIprZv5a/w1xRAso+N" +
                    "YqzTbhCqV3gT18LfgkOjPfy26N1BJHzrgGkIGBMj9vAgJHl9xRk/T0SJ1AYu0B62" +
                    "YlSBfOddZj3WhwJfK/gFLJFJemIbAgMBAAECgYAMZxtUsmgNeZ1s/8IdEYtW1xBk" +
                    "GF8KyyqXg4Bl7fQBfCHpUhpsMfB6Mt+jpiWpA5X1S/pNTjheQ63EVyAPGVD62spD" +
                    "22S6ghFOly7MSE7iRMA+lJpezVzZF5w7cBP3Y4kOOD2QpmxmQxkhdIYDyqEUl37C" +
                    "mYs/eJoALR5V5yIseQJBAP0nIBcrFIoqMKOpcyZQZ5HBEdbS028tWqSXaJ+CJD8M" +
                    "B9Ijwz6hait5eiSYdQH2+8pn3XbLmjwYRPFD0SvbBu8CQQDPun3Kj/Zq1bwlNTX4" +
                    "rgfbFtyyJmjH1/6IeWeF2SogF9zf45IdhPyzWXQii/BoHJTo4vB+7NVJu/yg+hZj" +
                    "njeVAkEAzKAJknCiI6RCuKfJihjH/srfDpRPj7hLYmt4iCZ8AfJJiBScR03WMdn5" +
                    "XwdU3Qe1M0CNhO7CdvzvDU3SD+71RQJAFR26B8tWm0ma1JVyJRNbzROn35wz7oyw" +
                    "XSMqONr4g0apt0Ck68dHANxJB/H9wDeXk4zuMZjVnac0aRDDEeFVeQJAOzALKPp6" +
                    "WHBnP566qFXlIbmE++t6QAX6ROZu8/uMC88M5s64DHmaOzImtR4p1Rb3zoI6wE0K" +
                    "sNveSwALK1EiiQ==";

    private static final int SDK_PAY_FLAG = 1;
    private EditTextWithCustomHint edMoney;
    //private static final int SDK_AUTH_FLAG = 2;

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
        if (TextUtils.isEmpty(APPID) || TextUtils.isEmpty(RSA_PRIVATE)) {
            new AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置APPID | RSA_PRIVATE")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            finish();
                        }
                    }).show();
            return;
        }
        //Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID, jObj);
        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID);//订单参数map
        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);
        LogUtils.PrintLog("123","订单参数-->"+orderParam);
        String sign = OrderInfoUtil2_0.getSign(params, RSA_PRIVATE);
        //String sign = jObj.optJSONObject("data").optJSONObject("getInfo").optString("sign");
        LogUtils.PrintLog("123","sign-->"+sign);
        final String orderInfo = orderParam + "&" + sign;
        LogUtils.PrintLog("123","最终请求参数"+orderInfo);
        LogUtils.PrintLog("123","最终请求参数"+payInfo);
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

    //点击充值界面，进入ping++支付界面
    public void goRechargeActivity(View view) {
        requestForServer();


    }

    private void requestForServer() {

        String money = edMoney.getText().toString();
        sendMsgToSevr("http://api.qlqwgw.com/v1/payment",0);
        final ProgressDialog p=new ProgressDialog(this,R.style.dialog);
        p.setCanceledOnTouchOutside(false);
        p.setMessage("请稍后");
        CheckThread checkThread = getCheckThread();
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
                JSONObject dataObj = jObj.optJSONObject("data");
                int status = jObj.optInt("status");
                if(status==200){
                    String payInfo = dataObj.optString("payInfo");
                    requestForAli(payInfo);
                }
            }
        });
        checkThread.start();
        p.show();
    }
}
