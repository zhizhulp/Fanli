package com.ascba.rebate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWorkActivity;
import com.ascba.rebate.fragments.me.FourthFragment;
import com.ascba.rebate.handlers.DialogManager;
import com.ascba.rebate.utils.UrlUtils;
import com.jaeger.library.StatusBarUtil;

/**
 * 商户中心，入驻流程
 */
public class BCProcessActivity extends BaseNetWorkActivity {

    private WebView webview;
    private DialogManager dm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bcprocess);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.moneyBarColor));
        initViews();
    }

    private void initViews() {
        dm=new DialogManager(this);
        webview = ((WebView) findViewById(R.id.webView));
        webview.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                dm.dismissDialog();
            }

        });
        webview.loadUrl(UrlUtils.explain);
        dm.buildWaitDialog("请稍后").showDialog();
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
