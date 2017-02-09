package com.ascba.rebate.fragments.shop;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ascba.rebate.R;
import com.ascba.rebate.handlers.DialogManager;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.SuperSwipeRefreshLayout;



public class ThirdFragment  extends Fragment {


    private WebView webShop;
    private DialogManager dm;
    private SuperSwipeRefreshLayout swipeLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.third_fragment, null);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dm=new DialogManager(getActivity());
        initWebView(view);
        initSwipe(view);

    }
    private void initSwipe(View view) {
        swipeLayout = ((SuperSwipeRefreshLayout) view.findViewById(R.id.swipe));
        swipeLayout.setOnPullRefreshListener(new SuperSwipeRefreshLayout.OnPullRefreshListener() {
            @Override
            public void onRefresh() {
                if(webShop!=null){
                    webShop.loadUrl(UrlUtils.shop);
                }
            }

            @Override
            public void onPullDistance(int distance) {

            }

            @Override
            public void onPullEnable(boolean enable) {

            }
        });
    }

    private void initWebView(View view) {
        webShop = ((WebView) view.findViewById(R.id.shop_web));
        webShop.getSettings().setJavaScriptEnabled(true);
        webShop.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                swipeLayout.setRefreshing(false);
                dm.dismissDialog();
            }

        });
        webShop.loadUrl(UrlUtils.shop);
        dm.showDialog();
    }
}