package com.ascba.rebate.view.dropDownMultiPager.ultraPullToRefash.handler;


import com.ascba.rebate.view.dropDownMultiPager.ultraPullToRefash.component.PtrFrameLayout;
import com.ascba.rebate.view.dropDownMultiPager.ultraPullToRefash.indicator.PtrIndicator;

public interface PtrUIHandler {

    void onUIReset(PtrFrameLayout frame);

    void onUIRefreshPrepare(PtrFrameLayout frame);

    void onUIRefreshBegin(PtrFrameLayout frame);

    void onUIRefreshComplete(PtrFrameLayout frame);

    void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator);
}
