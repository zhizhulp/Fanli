package com.ascba.rebate.fragments.shop.auction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.shop.auction.MyAuctionOrderActivity;
import com.ascba.rebate.activities.shop.auction.MyCashDepositActivity;
import com.ascba.rebate.fragments.base.BaseNetFragment;

/**
 * Created by 李鹏 on 2017/5/24.
 */

public class AuctionMeFragment extends BaseNetFragment implements View.OnClickListener {

    private Context context;

    @Override
    protected int setContentView() {
        return R.layout.fragment_auction_me;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();
        initView(view);

    }

    private void initView(View view) {
        view.findViewById(R.id.shopBar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        view.findViewById(R.id.btn_auction_me_bao_zheng_jin).setOnClickListener(this);

        view.findViewById(R.id.btn_auction_me_jing_pai).setOnClickListener(this);

        view.findViewById(R.id.btn_auction_me_huo_pai).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_auction_me_bao_zheng_jin:
                //我的保证金
                startActivity(new Intent(getActivity(), MyCashDepositActivity.class));
                break;
            case R.id.btn_auction_me_jing_pai:
                //我的竞拍

                break;
            case R.id.btn_auction_me_huo_pai:
                //我的获拍
                MyAuctionOrderActivity.startIntent(getActivity(), 0);
                break;
        }
    }
}
