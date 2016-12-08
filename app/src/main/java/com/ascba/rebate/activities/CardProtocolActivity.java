package com.ascba.rebate.activities;

import android.os.Bundle;
import android.webkit.WebView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseActivity;

public class CardProtocolActivity extends BaseActivity {
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_protocol);
        initViews();
    }
    private void initViews() {
        webView = ((WebView) findViewById(R.id.webView));
        webView.loadUrl("http://home.qlqwgw.com/service");
    }
}
