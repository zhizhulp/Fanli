package com.ascba.rebate.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.utils.IDsUtils;
import com.jaeger.library.StatusBarUtil;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
	

    private IWXAPI api;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wx_pay_result);
		StatusBarUtil.setColor(this,getResources().getColor(R.color.moneyBarColor));
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
		if(errCode==0){//成功
			//tvResult.setText("支付成功");
		}else if(errCode==-1){//错误
			//tvResult.setText("出现错误");
		}else if(errCode==-2){//用户取消
			//tvResult.setText("取消支付");
		}
	}
}