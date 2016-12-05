package com.ascba.rebate.activities.base;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ascba.rebate.R;
import com.ascba.rebate.view.MoneyBar;

public class WebViewBaseActivity extends AppCompatActivity {
    private String url;
    private WebView webView;
    private MoneyBar mb;
    private String name;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_base);
        setStatusBar();
        initViews();
        getMsgFromBefore();
    }

    private void getMsgFromBefore() {
        Intent intent = getIntent();
        if (intent!=null){
            name = intent.getStringExtra("name");
            url = intent.getStringExtra("url");
            mb.setTextTitle(name);
            if(url!=null){
                webView.setWebViewClient(new WebViewClient());
                webView.loadUrl(url);
            }
        }
    }

    private void initViews() {
        webView = ((WebView) findViewById(R.id.webView));
        mb = ((MoneyBar) findViewById(R.id.mb_protocol));

    }

    private void setStatusBar() {
        if (Build.VERSION.SDK_INT >= 19) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (webView.canGoBack()) {
            webView.goBack();  //返回上一页
        }
    }
    /**
     * 拦截实体键(MENU  BACK  POWER  VOLUME  HOME)的点击事件
     * @param keyCode  实体按键的编码
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {  //按下实体的返回键
            if (webView.canGoBack()) {  //说明WebView有上一页
                webView.goBack();  //WebView返回上一页
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
