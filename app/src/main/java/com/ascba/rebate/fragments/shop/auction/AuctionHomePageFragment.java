package com.ascba.rebate.fragments.shop.auction;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ascba.rebate.R;
import com.ascba.rebate.adapter.AcutionHPAdapter;
import com.ascba.rebate.beans.AcutionGoodsBean;
import com.ascba.rebate.fragments.base.BaseNetFragment;
import com.ascba.rebate.view.ShopABar;
import com.ascba.rebate.view.pagerWithTurn.ShufflingViewPager;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李鹏 on 2017/5/22.
 * 拍卖首页
 */

public class AuctionHomePageFragment extends BaseNetFragment {
    private RecyclerView recyclerView;
    private AcutionHPAdapter adapter;
    private List<AcutionGoodsBean> acutionGoodsBeanList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_auction_homepage, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {
        ShopABar shopABar = (ShopABar) view.findViewById(R.id.shopBar);
        shopABar.setImageOtherEnable(false);
        shopABar.setImMsgSta(R.mipmap.abar_search);
        shopABar.setCallback(new ShopABar.Callback() {
            @Override
            public void back(View v) {
                getActivity().finish();
            }

            @Override
            public void clkMsg(View v) {
            }

            @Override
            public void clkOther(View v) {

            }
        });

        setData();
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new AcutionHPAdapter(getActivity(), R.layout.item_auction_hp, acutionGoodsBeanList);

        View headView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_auction_hp_headview, null, false);
        ShufflingViewPager viewPager = (ShufflingViewPager) headView.findViewById(R.id.shufflingViewPager);
        adapter.setHeaderView(headView);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {

            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemChildClick(adapter, view, position);

                switch (view.getId()) {
                    case R.id.auction_btn_get:
                        //立即拍
                        break;
                }
            }
        });
    }

    private void setData() {
        AcutionGoodsBean acutionGoodsBean = new AcutionGoodsBean();
        acutionGoodsBean.setImgUrl("https://img.alicdn.com/imgextra/i4/1134309335/TB2rficqpXXXXXpXpXXXXXXXXXX_!!1134309335.jpg");
        acutionGoodsBean.setTimeRemaining("距离结束：01小时 02分钟 03秒");
        acutionGoodsBean.setPersonNum("45");
        acutionGoodsBean.setName("高档腕表，百搭，实用");
        acutionGoodsBean.setPrice("￥189.00");
        acutionGoodsBeanList.add(acutionGoodsBean);

        acutionGoodsBean = new AcutionGoodsBean();
        acutionGoodsBean.setImgUrl("https://img.alicdn.com/imgextra/i3/2490370259/TB2PsFvcpXXXXX3XXXXXXXXXXXX-2490370259.jpg");
        acutionGoodsBean.setTimeRemaining("距离结束：3天 01小时 02分钟 03秒");
        acutionGoodsBean.setPersonNum("168");
        acutionGoodsBean.setName("高档机械手表，百搭，实用");
        acutionGoodsBean.setPrice("￥88.00");
        acutionGoodsBeanList.add(acutionGoodsBean);
    }
}
