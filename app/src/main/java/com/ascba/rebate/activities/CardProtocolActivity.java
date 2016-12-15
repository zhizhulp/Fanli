package com.ascba.rebate.activities;

import android.os.Bundle;
import android.webkit.WebView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWorkActivity;

public class CardProtocolActivity extends BaseNetWorkActivity {
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
