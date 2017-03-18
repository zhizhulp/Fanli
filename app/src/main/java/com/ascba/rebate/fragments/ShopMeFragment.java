package com.ascba.rebate.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.MyOrderActivity;
import com.ascba.rebate.activities.ReceiveAddressActivity;
import com.ascba.rebate.adapter.PCMultipleItemAdapter;
import com.ascba.rebate.beans.PCMultipleItem;
import com.ascba.rebate.fragments.base.BaseFragment;
import com.ascba.rebate.view.SuperSwipeRefreshLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 商城设置
 */
public class ShopMeFragment extends BaseFragment implements SuperSwipeRefreshLayout.OnPullRefreshListener {
    private RecyclerView pc_RecyclerView;
    private SuperSwipeRefreshLayout refreshLat;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    public ShopMeFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shop_me, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        InitRecylerView(view);
    }

    private void InitRecylerView(View view) {
        pc_RecyclerView = (RecyclerView) view.findViewById(R.id.list_pc);
        PCMultipleItemAdapter pcMultipleItemAdapter = new PCMultipleItemAdapter(getData(), getActivity());
        final GridLayoutManager manager = new GridLayoutManager(getActivity(), PCMultipleItem.TYPE_SPAN_SIZE_DEFAULT);
        pc_RecyclerView.setLayoutManager(manager);
        pcMultipleItemAdapter.setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
            @Override
            public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                return getData().get(position).getSpanSize();
            }
        });
        pc_RecyclerView.setAdapter(pcMultipleItemAdapter);

        pc_RecyclerView.addOnItemTouchListener(new OnItemClickListener() {

            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(getActivity(), "position:" + Integer.toString(position), Toast.LENGTH_SHORT).show();
                switch (position) {
                    case 1:
                        //全部订单
                        Intent order = new Intent(getActivity(), MyOrderActivity.class);
                        startActivity(order);
                        break;
                    case 17:
                        //收货地址管理
                        Intent intent = new Intent(getActivity(), ReceiveAddressActivity.class);
                        startActivity(intent);
                        break;
                }
                switch (view.getId()) {
                    case R.id.activity_pc_item_head_back:
                        getActivity().finish();
                        break;
                }
            }

        });
        refreshLat = ((SuperSwipeRefreshLayout) view.findViewById(R.id.refresh_layout));
        refreshLat.setOnPullRefreshListener(this);
    }

    private List<PCMultipleItem> getData() {
        List<PCMultipleItem> pcMultipleItems = new ArrayList<>();

        //头信息
        pcMultipleItems.add(new PCMultipleItem(PCMultipleItem.TYPE_0, R.mipmap.pc_xiaoxi, R.mipmap.pc_dianpu, R.mipmap.pc_touxiang, R.mipmap.pc_huiyuan, "钱来钱往", "金钻会员"));

        //我的订单
        pcMultipleItems.add(new PCMultipleItem(PCMultipleItem.TYPE_1, R.mipmap.pc_wodedingdan, "我的订单", R.mipmap.pc_qianjin, "查看全部订单"));

        //分割线
        pcMultipleItems.add(new PCMultipleItem(PCMultipleItem.TYPE_2));

        //待付款、待发货、已成交、待评价、退款
        pcMultipleItems.add(new PCMultipleItem(PCMultipleItem.TYPE_3, R.mipmap.pc_daifukuan, "待付款", PCMultipleItem.TYPE_SPAN_SIZE_4));
        pcMultipleItems.add(new PCMultipleItem(PCMultipleItem.TYPE_3, R.mipmap.pc_daifahuo, "待发货", PCMultipleItem.TYPE_SPAN_SIZE_4));
        pcMultipleItems.add(new PCMultipleItem(PCMultipleItem.TYPE_3, R.mipmap.pc_yichengjiao, "已成交", PCMultipleItem.TYPE_SPAN_SIZE_4));
        pcMultipleItems.add(new PCMultipleItem(PCMultipleItem.TYPE_3, R.mipmap.pc_daipingjia, "待评价", PCMultipleItem.TYPE_SPAN_SIZE_4));
        pcMultipleItems.add(new PCMultipleItem(PCMultipleItem.TYPE_3, R.mipmap.pc_tuikuan, "退款/售后", PCMultipleItem.TYPE_SPAN_SIZE_4));

        //粗分割线
        pcMultipleItems.add(new PCMultipleItem(PCMultipleItem.TYPE_4));

        //学堂
        pcMultipleItems.add(new PCMultipleItem(PCMultipleItem.TYPE_1, R.mipmap.pc_xuetang, "钱来钱往学堂", R.mipmap.pc_qianjin, "APP教学指南"));

        //粗分割线
        pcMultipleItems.add(new PCMultipleItem(PCMultipleItem.TYPE_4));

        //代金券
        pcMultipleItems.add(new PCMultipleItem(PCMultipleItem.TYPE_1, R.mipmap.pc_daijinquan, "代金券", R.mipmap.pc_qianjin, "60张"));
        //分割线
        pcMultipleItems.add(new PCMultipleItem(PCMultipleItem.TYPE_2));

        //账户余额
        pcMultipleItems.add(new PCMultipleItem(PCMultipleItem.TYPE_1, R.mipmap.pc_zhanghuyue, "账户余额", R.mipmap.pc_qianjin, "3000元"));
        //分割线
        pcMultipleItems.add(new PCMultipleItem(PCMultipleItem.TYPE_2));

        //当日任务
        pcMultipleItems.add(new PCMultipleItem(PCMultipleItem.TYPE_1, R.mipmap.pc_dangrirenwu, "当日任务", R.mipmap.pc_qianjin, "完成60%"));
        //分割线
        pcMultipleItems.add(new PCMultipleItem(PCMultipleItem.TYPE_2));

        //收货地址
        pcMultipleItems.add(new PCMultipleItem(PCMultipleItem.TYPE_1, R.mipmap.pc_shouhuodizhi, "收货地址", R.mipmap.pc_qianjin, "北京市丰台区建邦钱来钱往"));

        //粗分割线
        pcMultipleItems.add(new PCMultipleItem(PCMultipleItem.TYPE_4));


        //在线客服
        pcMultipleItems.add(new PCMultipleItem(PCMultipleItem.TYPE_5, R.mipmap.pc_kefu, "在线客服", "400-4865217"));
        //分割线
        pcMultipleItems.add(new PCMultipleItem(PCMultipleItem.TYPE_2));

        //设置
        pcMultipleItems.add(new PCMultipleItem(PCMultipleItem.TYPE_1, R.mipmap.pc_shezhi, "设置", R.mipmap.pc_qianjin, ""));
        //分割线
        pcMultipleItems.add(new PCMultipleItem(PCMultipleItem.TYPE_2));
        //粗分割线
        pcMultipleItems.add(new PCMultipleItem(PCMultipleItem.TYPE_4));

        return pcMultipleItems;
    }

    @Override
    public void onRefresh() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshLat.setRefreshing(false);
            }
        }, 1000);
    }

    @Override
    public void onPullDistance(int distance) {

    }

    @Override
    public void onPullEnable(boolean enable) {

    }
}
