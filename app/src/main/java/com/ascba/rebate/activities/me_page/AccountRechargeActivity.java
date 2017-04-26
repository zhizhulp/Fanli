
package com.ascba.rebate.activities.me_page;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.ascba.rebate.R;
import com.ascba.rebate.activities.TransactionRecordsActivity;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.activities.me_page.recharge_child.RechaSuccActivity;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.application.MyApplication;
import com.ascba.rebate.fragments.me.FourthFragment;
import com.ascba.rebate.utils.IDsUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.EditTextWithCustomHint;
import com.ascba.rebate.view.MoneyBar;
import com.ascba.rebate.view.pay.PayResult;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yanzhenjie.nohttp.rest.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class AccountRechargeActivity extends BaseNetActivity implements BaseNetActivity.Callback, View.OnClickListener {
    private int select;//选择支付方式 0 微信支付 1 支付宝支付
    private IWXAPI api;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    //对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(AccountRechargeActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        try {
                            JSONObject jObj = new JSONObject(resultInfo);
                            JSONObject trObj = jObj.optJSONObject("alipay_trade_app_pay_response");
                            String total_amount = trObj.optString("total_amount");
                            Intent intent = new Intent(AccountRechargeActivity.this, RechaSuccActivity.class);
                            intent.putExtra("money", total_amount + "元");
                            startActivityForResult(intent, FourthFragment.REQUEST_PAY);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else if (TextUtils.equals(resultStatus, "6002")) {
                        getDm().buildAlertDialog("网络有问题");
                    } else if (TextUtils.equals(resultStatus, "6001")) {
                        getDm().buildAlertDialog("您已经取消支付");
                    } else {
                        getDm().buildAlertDialog("支付失败");
                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };

    private static final int SDK_PAY_FLAG = 1;
    private EditTextWithCustomHint edMoney;
    private ImageView imAli;
    private ImageView imWx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_recharge);
        initViews();
    }

    private void initViews() {
        MoneyBar moneyBar = (MoneyBar) findViewById(R.id.moneyBar);
        moneyBar.setTailTitle(getString(R.string.inoutcome_record));
        moneyBar.setCallBack(new MoneyBar.CallBack() {
            @Override
            public void clickImage(View im) {
                finish();
            }

            @Override
            public void clickComplete(View tv) {
                TransactionRecordsActivity.startIntent(AccountRechargeActivity.this);
            }
        });

        edMoney = ((EditTextWithCustomHint) findViewById(R.id.ed_recharge_money));
        imAli = ((ImageView) findViewById(R.id.alipay_circle));
        imWx = ((ImageView) findViewById(R.id.wxpay_circle));

        View wxClick = findViewById(R.id.wx_click);
        View aliClick = findViewById(R.id.ali_click);

        imAli.setImageResource(R.mipmap.unselect);
        imWx.setImageResource(R.mipmap.select);

        aliClick.setOnClickListener(this);
        wxClick.setOnClickListener(this);

        api = WXAPIFactory.createWXAPI(this, IDsUtils.WX_PAY_APP_ID);
    }

    private void requestForAli(final String payInfo) {

        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(AccountRechargeActivity.this);
                Map<String, String> result = alipay.payV2(payInfo, true);
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
        if ("".equals(money)) {
            getDm().buildAlertDialog("请输入金额");
            return;
        }
        if (select == 1) {
            Request<JSONObject> objRequest = buildNetRequest(UrlUtils.payment, 0, true);
            objRequest.add("price", money);
            executeNetWork(objRequest, "请稍后");
            setCallback(this);
        } else if (select == 0) {
            AppConfig.getInstance().putString("wx_pay_money", money);
            Request<JSONObject> objRequest = buildNetRequest(UrlUtils.wxpay, 0, true);
            Double v = Double.parseDouble(money);
            objRequest.add("total_fee", (int) (v * 100));
            executeNetWork(objRequest, "请稍后");
            setCallback(this);
        }

    }

    @Override
    public void handle200Data(JSONObject dataObj, String message) {
        edMoney.setText("");
        if (select == 1) {
            String payInfo = dataObj.optString("payInfo");
            requestForAli(payInfo);//发起支付宝支付请求
        } else if (select == 0) {
            requestForWX(dataObj);//发起微信支付请求
        }
        MyApplication.payType = 0;

    }

    @Override
    public void handle404(String message) {

    }

    @Override
    public void handleNoNetWork() {

    }

    private void requestForWX(JSONObject dataObj) {

        try {
            JSONObject wxpay = dataObj.getJSONObject("wxpay");
            PayReq req = new PayReq();
            req.appId = wxpay.getString("appid");
            req.nonceStr = wxpay.getString("noncestr");
            req.packageValue = wxpay.getString("package");
            req.partnerId = wxpay.getString("partnerid");
            req.prepayId = wxpay.getString("prepayid");
            req.timeStamp = wxpay.getInt("timestamp") + "";
            req.sign = wxpay.getString("sign");
            // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
            api.sendReq(req);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case FourthFragment.REQUEST_PAY:
                Intent intent = getIntent();
                setResult(RESULT_OK, intent);
                finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wx_click:
                if (select == 1) {
                    imWx.setImageResource(R.mipmap.select);
                    imAli.setImageResource(R.mipmap.unselect);
                }
                select = 0;
                break;
            case R.id.ali_click:
                if (select == 0) {
                    imWx.setImageResource(R.mipmap.unselect);
                    imAli.setImageResource(R.mipmap.select);
                }
                select = 1;
                break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int res_code = intent.getIntExtra("res_code", -1);
        if (res_code == 0) {//支付成功
            setResult(RESULT_OK, intent);
            finish();
        } else if (res_code == -1) {//出现错误
            getDm().buildAlertDialog("支付失败");
        } else {//支付取消
            getDm().buildAlertDialog("您已经取消支付");
        }

    }
}

