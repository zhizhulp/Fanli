package com.ascba.rebate.activities.register;

import android.os.Bundle;
import android.webkit.WebView;
import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.utils.UrlUtils;


public class RegisterProtocolActivity extends BaseNetActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_protocol);
        //StatusBarUtil.setColor(this, 0xffe52020);
        initViews();
    }

    private void initViews() {
        webView = ((WebView) findViewById(R.id.webView));
        webView.loadUrl(UrlUtils.regAgree);
    }
}
