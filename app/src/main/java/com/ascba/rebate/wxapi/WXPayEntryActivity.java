package com.ascba.rebate.wxapi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.shop.order.MyOrderActivity;
import com.ascba.rebate.activities.RechargeBillActivity;
import com.ascba.rebate.activities.me_page.AccountRechargeActivity;
import com.ascba.rebate.activities.shop.order.DeliverDetailsActivity;
import com.ascba.rebate.activities.shop.order.PayDetailsActivity;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.application.MyApplication;
import com.ascba.rebate.utils.IDsUtils;
import com.ascba.rebate.utils.StringUtils;
import com.ascba.rebate.view.MoneyBar;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler, MoneyBar.CallBack {


    private static final String TAG = "WXPayEntryActivity";
    private IWXAPI api;
    private TextView tvResult;
    private TextView tvMoney;
    private MoneyBar mb;
    private int finalErrorCode;
    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wx_pay_result);
        context = this;
        tvResult = ((TextView) findViewById(R.id.pay_result));
        tvMoney = ((TextView) findViewById(R.id.tv_money));
        mb = ((MoneyBar) findViewById(R.id.mb));
        mb.setCallBack(this);
        api = WXAPIFactory.createWXAPI(this, IDsUtils.WX_PAY_APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {

    }

    @Override
    public void onResp(BaseResp resp) {
        int errCode = resp.errCode;
        finalErrorCode = errCode;

        switch (MyApplication.payType) {
            case 0://充值
                Log.d(TAG, "onResp: case0 "+errCode);
                Intent intent = new Intent(this, AccountRechargeActivity.class);
                intent.putExtra("res_code", finalErrorCode);

                if (errCode == 0) {//成功
                    tvResult.setText("支付成功");
                    tvMoney.setText(AppConfig.getInstance().getString("wx_pay_money", "0.00") + "元");

                } else if (errCode == -1) {//错误
                    tvResult.setText("出现错误");
                    startActivity(intent);
                    finish();
                } else if (errCode == -2) {//用户取消
                    tvResult.setText("取消支付");
                    startActivity(intent);
                    finish();
                }
                break;
            case 1:
                //支付
                Log.d(TAG, "onResp: case1"+errCode);
                if (errCode == 0) {//成功
                    Toast.makeText(this, "支付成功", Toast.LENGTH_SHORT).show();
                    if (StringUtils.isEmpty(MyApplication.orderId)) {
                        //跳转待付款列表
                        MyOrderActivity.startIntent(context, 2);
                    } else {
                        Intent successIntent = new Intent(context, DeliverDetailsActivity.class);
                        successIntent.putExtra("order_id", MyApplication.orderId);
                        startActivity(successIntent);
                    }
                    finish();
                } else if (errCode == -1) {//错误
                    Toast.makeText(this, "支付失败", Toast.LENGTH_SHORT).show();
                    if (StringUtils.isEmpty(MyApplication.orderId)) {
                        //跳转待付款列表
                        MyOrderActivity.startIntent(context, 1);
                    } else {
                        Intent errorIntent = new Intent(context, PayDetailsActivity.class);
                        errorIntent.putExtra("order_id", MyApplication.orderId);
                        startActivity(errorIntent);
                    }
                    finish();
                } else if (errCode == -2) {//用户取消
                    Toast.makeText(this, "支付取消", Toast.LENGTH_SHORT).show();
                    if (StringUtils.isEmpty(MyApplication.orderId)) {
                        //跳转待付款列表
                        MyOrderActivity.startIntent(context, 1);
                    } else {
                        Intent cancelIntent = new Intent(context, PayDetailsActivity.class);
                        cancelIntent.putExtra("order_id", MyApplication.orderId);
                        startActivity(cancelIntent);
                    }
                    finish();
                }
                break;
        }

    }

    //进入查看账单页面
    public void goAcc(View view) {
        Intent intent = new Intent(this, RechargeBillActivity.class);
        startActivity(intent);
    }

    @Override
    public void clickImage(View im) {

    }

    @Override
    public void clickComplete(View tv) {
        Intent intent = new Intent(this, AccountRechargeActivity.class);
        intent.putExtra("res_code", finalErrorCode);
        startActivity(intent);
    }
}