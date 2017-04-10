package com.ascba.rebate.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.BeginnerGuideActivity;
import com.ascba.rebate.activities.MyOrderActivity;
import com.ascba.rebate.activities.ReceiveAddressActivity;
import com.ascba.rebate.activities.ShopMessageActivity;
import com.ascba.rebate.activities.shop.ShopActivity;
import com.ascba.rebate.adapter.PCMultipleItemAdapter;
import com.ascba.rebate.beans.PCMultipleItem;
import com.ascba.rebate.fragments.base.Base2Fragment;
import com.ascba.rebate.handlers.DialogManager;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.SuperSwipeRefreshLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.yolanda.nohttp.rest.Request;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 商城设置
 */
public class ShopMeFragment extends Base2Fragment implements SuperSwipeRefreshLayout.OnPullRefreshListener,
        Base2Fragment.Callback {
    private Context context;
    private DialogManager dm;
    private RecyclerView pc_RecyclerView;
    private List<PCMultipleItem> pcMultipleItems = new ArrayList<>();
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
        context =getActivity();
        initView(view);
        getMeData();

    }

    /*
       获取me数据
     */
    private void getMeData() {
        dm = new DialogManager(context);
        Request<JSONObject> jsonRequest = buildNetRequest(UrlUtils.myPageInfo, 0, true);
        executeNetWork(jsonRequest, "请稍后");
        setCallback(this);
    }

    /*
    初始化UI
     */
    private void initView(View view) {
        pc_RecyclerView = (RecyclerView) view.findViewById(R.id.list_pc);
        PCMultipleItemAdapter pcMultipleItemAdapter = new PCMultipleItemAdapter(pcMultipleItems, context);
        final GridLayoutManager manager = new GridLayoutManager(getActivity(), PCMultipleItem.TYPE_SPAN_SIZE_DEFAULT);
        pc_RecyclerView.setLayoutManager(manager);
        pcMultipleItemAdapter.setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
            @Override
            public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                return pcMultipleItems.get(position).getSpanSize();
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
                        MyOrderActivity.startIntent(getActivity(), 0);
                        break;
                    case 3:
                        //待付款
                        MyOrderActivity.startIntent(getActivity(), 1);
                        break;
                    case 4:
                        //待发货
                        MyOrderActivity.startIntent(getActivity(), 2);
                        break;
                    case 5:
                        //已成交
                        MyOrderActivity.startIntent(getActivity(), 3);
                        break;
                    case 6:
                        //待评价
                        MyOrderActivity.startIntent(getActivity(), 4);
                        break;
                    case 9:
                        //新手指南
                        Intent intent1 = new Intent(getContext(), BeginnerGuideActivity.class);
                        startActivity(intent1);
                        break;
                    case 17:
                        //收货地址管理
                        Intent intent = new Intent(getActivity(), ReceiveAddressActivity.class);
                        startActivity(intent);
                        break;
                    case 19:
                        Intent phone = new Intent();
                        phone.setAction(Intent.ACTION_DIAL);
                        phone.setData(Uri.parse("tel:15206292150"));
                        startActivity(phone);
                        break;
                }
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemChildClick(adapter, view, position);
                switch (view.getId()) {
                    case R.id.activity_pc_item_head_back:
                        getActivity().finish();
                        break;
                    case R.id.activity_pc_item_head_message:
                        ShopMessageActivity.startIntent(getActivity());
                        break;
                }
            }
        });


        refreshLat = ((SuperSwipeRefreshLayout) view.findViewById(R.id.refresh_layout));
        refreshLat.setOnPullRefreshListener(this);
    }

    /*
    初始化数据
     */
    private void initData() {

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

    @Override
    public void handle200Data(JSONObject dataObj, String message) {

    }

    @Override
    public void handleReqFailed() {

    }

    @Override
    public void handle404(String message) {
        dm.buildAlertDialog(message);
    }

    @Override
    public void handleReLogin() {

    }

    @Override
    public void handleNoNetWork() {
        dm.buildAlertDialog("请检查网络！");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            FragmentActivity activity = getActivity();
            if (activity instanceof ShopActivity) {
                ShopActivity a = (ShopActivity) activity;
                a.selFrgByPos(0, a.getmFirstFragment());
                a.getShopTabs().statusChaByPosition(0, 3);
                a.getShopTabs().setFilPos(0);
            }
        } else {
            if (resultCode != Activity.RESULT_OK) {
                FragmentActivity activity = getActivity();
                if (activity instanceof ShopActivity) {
                    ShopActivity a = (ShopActivity) activity;
                    a.selFrgByPos(0, a.getmFirstFragment());
                    a.getShopTabs().statusChaByPosition(0, 3);
                    a.getShopTabs().setFilPos(0);
                }
            }
        }
    }
}
