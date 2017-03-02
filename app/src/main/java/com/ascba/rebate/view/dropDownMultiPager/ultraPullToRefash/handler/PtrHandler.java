package com.ascba.rebate.view.dropDownMultiPager.ultraPullToRefash.handler;

import android.view.View;

import com.ascba.rebate.view.dropDownMultiPager.ultraPullToRefash.component.PtrFrameLayout;


public interface PtrHandler {

    boolean checkCanDoRefresh(final PtrFrameLayout frame, final View content, final View header);

    void onRefreshBegin(final PtrFrameLayout frame);
}