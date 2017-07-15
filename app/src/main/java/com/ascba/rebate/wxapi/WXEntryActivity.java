package com.ascba.rebate.wxapi;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;
import com.ascba.rebate.utils.IDsUtils;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * 微信登录和分享回调类
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, IDsUtils.WX_PAY_APP_ID,true);
        api.handleIntent(getIntent(),this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        switch (baseResp.errCode){

            case BaseResp.ErrCode.ERR_OK:
                if(baseResp.getType() == ConstantsAPI.COMMAND_SENDAUTH){    //登录回调
                    String code = ((SendAuth.Resp)baseResp).code;

                }else if(baseResp.getType() == ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX){  //分享回调

                }

                break;

            case BaseResp.ErrCode.ERR_AUTH_DENIED:// 用户拒绝授权
                Toast.makeText(getApplicationContext(),"取消授权",Toast.LENGTH_SHORT).show();
                break;

            case BaseResp.ErrCode.ERR_USER_CANCEL:// 用户取消
                Toast.makeText(getApplicationContext(),"操作取消",Toast.LENGTH_SHORT).show();
                break;
        }
        finish();
    }

}
