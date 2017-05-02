package com.ascba.rebate.activities.me_page.business_center_child;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.fragments.me.FourthFragment;
import com.ascba.rebate.utils.UrlUtils;

/**
 * 商户中心，入驻流程
 */
public class BCProcessActivity extends BaseNetActivity {

    private WebView webview;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bcprocess);
        initViews();
    }

    private void initViews() {
        webview = ((WebView) findViewById(R.id.webView));
        webview.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                dialog.dismiss();
            }

        });
        webview.loadUrl(UrlUtils.explain);
        dialog = getDm().buildWaitDialog("请稍后");
    }

    //进入公司认证界面
    public void goApply(View view) {
        Intent intent=new Intent(this,BCInputNameActivity.class);
        startActivityForResult(intent, FourthFragment.REQUEST_APPLY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null){
            switch (requestCode){
                case FourthFragment.REQUEST_APPLY:
                    setResult(RESULT_OK,getIntent());
                    finish();
                    break;
            }
        }
    }
}
