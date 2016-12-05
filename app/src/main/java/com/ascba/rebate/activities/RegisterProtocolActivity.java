package com.ascba.rebate.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseActivity;

public class RegisterProtocolActivity extends BaseActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_protocol);
        initViews();
    }

    private void initViews() {
        webView = ((WebView) findViewById(R.id.webView));
        webView.loadUrl("http://app.qlqwgw.com/regAgree");
    }
}
