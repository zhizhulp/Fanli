package com.ascba.rebate.fragments.shop.auction;

import com.ascba.rebate.R;
import com.ascba.rebate.fragments.base.LazyLoadFragment;

/**
 * Created by 李鹏 on 2017/5/25.
 * 未支付竞拍订单
 */

public class AuctionPayOrderFragment extends LazyLoadFragment {

    @Override
    protected int setContentView() {
        return R.layout.fragment_orders;
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    protected void stopLoad() {
        cancelNetWork();
    }
}
