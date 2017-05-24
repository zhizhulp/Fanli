package com.ascba.rebate.fragments.shop.auction;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.shop.auction.BlindShootActivity;
import com.ascba.rebate.activities.shop.auction.GrabShootActivity;
import com.ascba.rebate.adapter.AuctionMainPlaceChildAdapter;
import com.ascba.rebate.beans.AcutionGoodsBean;
import com.ascba.rebate.fragments.base.BaseNetFragment;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李鹏 on 2017/5/24.
 * 主会场——时间分支
 */

public class AuctionMainPlaceChildFragment extends BaseNetFragment {

    private RecyclerView recyclerView;
    private List<AcutionGoodsBean> beanList = new ArrayList<>();
    private AuctionMainPlaceChildAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_auction_main_place_child, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {

        setData();

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new AuctionMainPlaceChildAdapter(getActivity(), R.layout.item_auction_goods, beanList);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {

                switch (position) {
                    case 0:
                        BlindShootActivity.startIntent(getActivity(), 0);
                        break;
                    case 1:
                        BlindShootActivity.startIntent(getActivity(), 1);
                        break;
                    case 2:
                        BlindShootActivity.startIntent(getActivity(), 2);
                        break;
                    case 3:
                        GrabShootActivity.startIntent(getActivity(), 0);
                        break;
                    case 4:
                        GrabShootActivity.startIntent(getActivity(), 1);
                        break;
                    case 5:
                        GrabShootActivity.startIntent(getActivity(), 2);
                        break;
                }

            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                switch (view.getId()) {
                    case R.id.btn_sub:
                        showToast("减号-->" + position);
                        break;
                    case R.id.btn_add:
                        showToast("加号-->" + position);
                        break;
                    case R.id.btn_auction_goods_add_cart:
                        showToast("加入购物车-->" + position);
                        break;
                    case R.id.btn_auction_goods_apply:
                        showToast("立即报名-->" + position);
                        break;
                }

            }
        });

    }

    private void setData() {
        AcutionGoodsBean bean = new AcutionGoodsBean();
        for (int i = 0; i < 6; i++) {
            bean.setPrice("降价至:￥5000.00");
            bean.setImgUrl("https://img14.360buyimg.com/n1/s450x450_jfs/t3754/265/902031417/103522/d308968f/581af17fN1057992a.jpg");
            bean.setTimeRemaining("00时 02分 45秒");
            bean.setName("苹果iph7手机限时抢拍  白色128G 全网通");
            bean.setScore("购买增值5000000积分");
            bean.setPersonNum("100");
            beanList.add(bean);
        }
    }
}
