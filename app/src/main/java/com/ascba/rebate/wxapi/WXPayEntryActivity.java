package com.ascba.rebate.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.me_page.AccountRechargeActivity;
import com.ascba.rebate.activities.me_page.AllAccountActivity;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.application.MyApplication;
import com.ascba.rebate.utils.IDsUtils;
import com.ascba.rebate.view.MoneyBar;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler,MoneyBar.CallBack {


    private IWXAPI api;
	private TextView tvResult;
	private TextView tvMoney;
	private MoneyBar mb;
	private int finalErrorCode;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wx_pay_result);
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
		finalErrorCode=errCode;

        switch (MyApplication.payType){
            case 0://充值
                Intent intent=new Intent(this,AccountRechargeActivity.class);
                intent.putExtra("res_code",finalErrorCode);

                if(errCode==0){//成功
                    tvResult.setText("支付成功");
                    tvMoney.setText(AppConfig.getInstance().getString("wx_pay_money","0.00")+"元");

                }else if(errCode==-1){//错误
                    tvResult.setText("出现错误");
                    startActivity(intent);
                    finish();
                }else if(errCode==-2){//用户取消
                    tvResult.setText("取消支付");
                    startActivity(intent);
                    finish();
                }
                break;
            case 1:
				if(errCode==0){//成功
					tvResult.setText("支付成功");
				}else if(errCode==-1){//错误
					tvResult.setText("出现错误");
				}else if(errCode==-2){//用户取消
					tvResult.setText("取消支付");
				}
                break;
        }

	}

	//进入查看账单页面
	public void goAcc(View view) {
		Intent intent=new Intent(this,AllAccountActivity.class);
		intent.putExtra("order",3);
		startActivity(intent);
	}

	@Override
	public void clickImage(View im) {

	}

	@Override
	public void clickComplete(View tv) {
		Intent intent=new Intent(this,AccountRechargeActivity.class);
		intent.putExtra("res_code",finalErrorCode);
		startActivity(intent);
	}
}