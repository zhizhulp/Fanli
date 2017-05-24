package com.ascba.rebate.fragments.shop.auction;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.ascba.rebate.R;
import com.ascba.rebate.fragments.base.BaseNetFragment;

/**
 * Created by 李鹏 on 2017/5/24.
 */

public class AuctionMeFragment extends BaseNetFragment {

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
        findViewById(R.id.shopBar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });


    }

}
