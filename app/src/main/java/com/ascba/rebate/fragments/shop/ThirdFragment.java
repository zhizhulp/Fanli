package com.ascba.rebate.fragments.shop;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.ascba.rebate.R;


/**
 * Created by linhonghong on 2015/8/11.
 */
public class ThirdFragment  extends Fragment {


    private WebView webShop;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.third_fragment, null);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        webShop = ((WebView) view.findViewById(R.id.shop_web));
        webShop.getSettings().setJavaScriptEnabled(true);
        webShop.loadUrl("http://home.qlqwgw.com/shop");
    }
}