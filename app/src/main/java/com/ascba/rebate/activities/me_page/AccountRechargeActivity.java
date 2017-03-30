
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
import com.ascba.rebate.activities.base.BaseNetWorkActivity;
import com.ascba.rebate.activities.me_page.recharge_child.RechaSuccActivity;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.fragments.me.FourthFragment;
import com.ascba.rebate.handlers.DialogManager;
import com.ascba.rebate.utils.EncryptUtils;
import com.ascba.rebate.utils.IDsUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.EditTextWithCustomHint;
import com.ascba.rebate.view.MoneyBar;
import com.ascba.rebate.view.pay.PayResult;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yolanda.nohttp.rest.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class AccountRechargeActivity extends BaseNetWorkActivity implements BaseNetWorkActivity.Callback, View.OnClickListener {
    private DialogManager dm = new DialogManager(this);
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
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
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
                    } else if(TextUtils.equals(resultStatus, "6002")) {
                        dm.buildAlertDialog("网络有问题");
                    }  else if(TextUtils.equals(resultStatus, "6001")) {
                        dm.buildAlertDialog("您已经取消支付");
                    } else {
                        dm.buildAlertDialog("支付失败");
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
    private View wxClick;
    private View aliClick;
    private MoneyBar moneyBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_recharge);
        //StatusBarUtil.setColor(this, getResources().getColor(R.color.moneyBarColor));
        initViews();
    }

    private void initViews() {
        moneyBar = (MoneyBar) findViewById(R.id.moneyBar);
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

        wxClick = findViewById(R.id.wx_click);
        aliClick = findViewById(R.id.ali_click);

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
            dm.buildAlertDialog("请输入金额");
            return;
        }
        if (select == 1) {
            Request<JSONObject> objRequest = buildNetRequest(UrlUtils.payment, 0, true);
            objRequest.add("price", money);
            executeNetWork(objRequest, "请稍后");
            setCallback(this);
        } else if (select == 0) {
            AppConfig.getInstance().putString("wx_pay_money",money);
            Request<JSONObject> objRequest = buildNetRequest(UrlUtils.wxpay, 0, true);
            Double v = Double.parseDouble(money);
            objRequest.add("total_fee", (int)(v*100));
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
            req.timeStamp = wxpay.getInt("timestamp")+"";
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
        if(res_code==0){//支付成功
            setResult(RESULT_OK,intent);
            finish();
        }else if(res_code==-1){//出现错误
            dm.buildAlertDialog("支付失败");
        }else {//支付取消
            dm.buildAlertDialog("您已经取消支付");
        }

    }

    public void testWX(View view) {
        Request<JSONObject> request = buildNetRequest("http://123.57.20.120:8050/PhonePospInterface/servlet/WXGalleryPayServlet", 0, false);
        /*request.add("body","test");//商品描述
        request.add("mch_create_ip","192.168.50.24");//终端ip
        request.add("mch_id","6000000002");//商户号
        request.add("nonce_str","lp377762984");//随机字符串
        request.add("notify_url","http://www.baidu.com");//通知地址
        request.add("out_trade_no","qw201703281958");//订单号
        request.add("service","unified.trade.pay");//接口类型
        request.add("sign",createSign());//sign
        request.add("total_fee","1");//*/
        request.add("saruLruid", "6000000001");// 商户号
        request.add("transAmt", "1");// 交易金额 单位为分 整数
        request.add("out_trade_no", "YBWX143631399129");// 订单号 不可重复
        request.add("body", "test");// 商品信息
        request.add("notify_url","http://123.57.20.120:8050/PhonePospInterface/WXTestCallbackServlet");

        executeNetWork(request,"请稍后");
        setCallback(new Callback() {
            @Override
            public void handle200Data(JSONObject dataObj, String message) throws JSONException {

            }
        });
        /*Request<String> request= NoHttp.createStringRequest("http://123.57.20.120:8050/PhonePospInterface/servlet/WXGalleryPayServlet", RequestMethod.POST);
        request.add("body","test");//商品描述
        request.add("mch_create_ip","192.168.50.24");//终端ip
        request.add("mch_id","6000000002");//商户号
        request.add("nonce_str","lp377762984");//随机字符串
        request.add("notify_url","http://www.baidu.com");//通知地址
        request.add("out_trade_no","qw201703281958");//订单号
        request.add("service","unified.trade.pay");//接口类型
        request.add("sign",createSign());//sign
        request.add("total_fee","100");//

        request.setDefineRequestBodyForXML("<xml>" +
                "<body><![CDATA[test]]></body>\n" +
                "<mch_create_ip><![CDATA[127.0.0.1]]></mch_create_ip>\n" +
                "<mch_id><![CDATA[6000000002]]></mch_id>\n" +
                "<nonce_str><![CDATA[lp377762984]]></nonce_str>\n" +
                "<notify_url><![CDATA[http://www.baidu.com]]></notify_url>\n" +
                "<out_trade_no><![CDATA[qw201703291402]]></out_trade_no>\n" +
                "<service><![CDATA[unified.trade.pay]]></service>\n" +
                "<sign><![CDATA["+createSign()+"]]></sign>\n" +
                "<total_fee><![CDATA[1]]></total_fee>\n" +
                "</xml>\n");
        request.setContentType("text/xml");
        MyApplication.getRequestQueue().add(0, request, new OnResponseListener<String>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<String> response) {

            }

            @Override
            public void onFailed(int what, Response<String> response) {

            }

            @Override
            public void onFinish(int what) {

            }
        });*/
    }

    private String createSign() {
        return EncryptUtils.MD5("body=test&" +
                "mch_create_ip=127.0.0.1&" +
                "mch_id=6000000001&" +
                "nonce_str=lp377762984&" +
                "notify_url=http:\\/\\/www.baidu.com&" +
                "out_trade_no=qw201703291402&" +
                "service=unified.trade.pay&" +
                "total_fee=1&"+
                "7daa4babae15ae17eee90c9e",true);
    }


}

