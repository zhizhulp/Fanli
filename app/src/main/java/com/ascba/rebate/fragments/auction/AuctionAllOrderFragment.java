package com.ascba.rebate.fragments.auction;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ascba.rebate.R;
import com.ascba.rebate.adapter.AuctionOrderAdapter;
import com.ascba.rebate.beans.AcutionGoodsBean;
import com.ascba.rebate.fragments.base.LazyLoadFragment;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李鹏 on 2017/5/25.
 * 全部竞拍订单
 */

public class AuctionAllOrderFragment extends LazyLoadFragment {

    private AuctionOrderAdapter adapter;
    private List<AcutionGoodsBean> beanList = new ArrayList<>();
    private RecyclerView recyclerView;

    @Override
    protected int setContentView() {
        return R.layout.fragment_orders;
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {
        getData();
        recyclerView = (RecyclerView) view.findViewById(R.id.list_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new AuctionOrderAdapter(getActivity(), R.layout.item_auction_goods3, beanList);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {

            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemChildClick(adapter, view, position);

            }
        });
    }

    private void getData(){
        String imgUl = "https://img14.360buyimg.com/n0/jfs/t4594/342/1086676431/290324/feb08a33/58d87996N329d56d2.jpg";
        AcutionGoodsBean bean = new AcutionGoodsBean();
        bean.setImgUrl(imgUl);
        bean.setName("小米MIX 全网通 标准版 4GB内存 128GB ROM 陶瓷黑 移动联通电信4G手机");
        bean.setPrice(100.00);
        bean.setPayState("0");
        bean.setScore("购买增值100000积分");
        beanList.add(bean);

        bean = new AcutionGoodsBean();
        bean.setImgUrl(imgUl);
        bean.setName("小米MIX 全网通 标准版 4GB内存 128GB ROM 陶瓷黑 移动联通电信4G手机");
        bean.setPrice(100.00);
        bean.setPayState("1");
        bean.setScore("购买增值100000积分");
        beanList.add(bean);

        bean = new AcutionGoodsBean();
        bean.setImgUrl(imgUl);
        bean.setName("小米MIX 全网通 标准版 4GB内存 128GB ROM 陶瓷黑 移动联通电信4G手机");
        bean.setPrice(100.00);
        bean.setPayState("2");
        bean.setScore("购买增值100000积分");
        beanList.add(bean);
    }

    @Override
    protected void stopLoad() {
        cancelNetWork();
    }
}
