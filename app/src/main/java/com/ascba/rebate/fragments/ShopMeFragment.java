package com.ascba.rebate.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.BeginnerGuideActivity;
import com.ascba.rebate.activities.MyOrderActivity;
import com.ascba.rebate.activities.ReceiveAddressActivity;
import com.ascba.rebate.activities.RefundOrderActivity;
import com.ascba.rebate.activities.ShopMessageActivity;
import com.ascba.rebate.activities.shop.ShopActivity;
import com.ascba.rebate.adapter.PCMultipleItemAdapter;
import com.ascba.rebate.beans.PCMultipleItem;
import com.ascba.rebate.fragments.base.Base2Fragment;
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
    private View view;
    private RecyclerView pc_RecyclerView;
    private PCMultipleItemAdapter pcMultipleItemAdapter;
    private List<PCMultipleItem> pcMultipleItems = new ArrayList<>();
    private SuperSwipeRefreshLayout refreshLat;
    private int[] orderMsg;
    private View headView;
    private View headViewLine;
    private int mDistanceY = 0;//下拉刷新滑动距离

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shop_me, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();
        getMeData();
        this.view = view;
    }

    /*
       获取me数据
     */
    private void getMeData() {
        Request<JSONObject> jsonRequest = buildNetRequest(UrlUtils.myPageInfo, 0, true);
        executeNetWork(jsonRequest, "请稍后");
        setCallback(this);
    }

    /*
    初始化UI
     */
    private void initView(View view) {
        headView = view.findViewById(R.id.head_view);
        headViewLine = view.findViewById(R.id.head_view_line);
        //返回
        view.findViewById(R.id.activity_pc_item_head_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        //消息
        view.findViewById(R.id.activity_pc_item_head_message).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShopMessageActivity.startIntent(getActivity());
            }
        });

        pc_RecyclerView = (RecyclerView) view.findViewById(R.id.list_pc);
        pcMultipleItemAdapter = new PCMultipleItemAdapter(pcMultipleItems, context);
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
                PCMultipleItem item = pcMultipleItems.get(position);
                switch (position) {
                    case 1:
                        //全部订单
                        MyOrderActivity.startIntent(getActivity(), 0, orderMsg);
                        break;
                    case 3:
                        //待付款
                        MyOrderActivity.startIntent(getActivity(), 1, orderMsg);
                        break;
                    case 4:
                        //待发货
                        MyOrderActivity.startIntent(getActivity(), 2, orderMsg);
                        break;
                    case 5:
                        //已成交
                        MyOrderActivity.startIntent(getActivity(), 3, orderMsg);
                        break;
                    case 6:
                        //待评价
                        MyOrderActivity.startIntent(getActivity(), 4, orderMsg);
                        break;
                    case 7:
                        //退货
                        RefundOrderActivity.startIntent(getActivity());
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
                        phone.setData(Uri.parse("tel:" + item.getContenRight()));
                        startActivity(phone);
                        break;
                }
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemChildClick(adapter, view, position);
                switch (view.getId()) {

                }
            }
        });

        refreshLat = ((SuperSwipeRefreshLayout) view.findViewById(R.id.refresh_layout));
        refreshLat.setOnPullRefreshListener(this);

        /**
         * 滑动标题栏渐变
         */
        pc_RecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                //滑动的距离
                mDistanceY += dy;
                //toolbar的高度
                int toolbarHeight = headView.getBottom();
                float maxAlpha = 229.5f;//最大透明度80%
                //当滑动的距离 <= toolbar高度的时候，改变Toolbar背景色的透明度，达到渐变的效果
                if (mDistanceY <= toolbarHeight) {
                    float scale = (float) mDistanceY / toolbarHeight;
                    float alpha = scale * maxAlpha;
                    headView.setBackgroundColor(Color.argb((int) alpha, 255, 255, 255));
                    headViewLine.setAlpha(alpha);
                }
            }
        });
    }


    @Override
    public void onRefresh() {
        getMeData();
    }

    @Override
    public void onPullDistance(int distance) {
        if (distance > 0) {
            headView.setVisibility(View.INVISIBLE);
        } else {
            headView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPullEnable(boolean enable) {

    }

    @Override
    public void handle200Data(JSONObject dataObj, String message) {
        JSONObject jsonObject = dataObj.optJSONObject("my_page_info");
        initDat(jsonObject);
        if (pcMultipleItemAdapter == null) {
            //初次获取数据
            initView(view);
        } else {
            //刷新数据
            refreshLat.setRefreshing(false);
            pcMultipleItemAdapter.notifyDataSetChanged();
        }

    }

    private void initDat(JSONObject Object) {
        pcMultipleItems.clear();
        //头信息
        JSONObject meObject = Object.optJSONObject("member_info");
        String headImg = UrlUtils.baseWebsite + meObject.optString("avatar");
        String realname = meObject.optString("realname");
        String nickname = meObject.optString("nickname");
        pcMultipleItems.add(new PCMultipleItem(PCMultipleItem.TYPE_0, R.mipmap.pc_xiaoxi, R.mipmap.pc_dianpu, headImg, nickname));

        //我的订单
        pcMultipleItems.add(new PCMultipleItem(PCMultipleItem.TYPE_1, R.mipmap.pc_wodedingdan, "我的订单", R.mipmap.pc_qianjin, "查看全部订单"));

        //分割线
        pcMultipleItems.add(new PCMultipleItem(PCMultipleItem.TYPE_2));

        //待付款、待发货、已成交、待评价、退款
        JSONObject orderObject = Object.optJSONObject("order_count_info");
        //待付款订单数
        int pay = orderObject.optInt("wait_pay", 0);
        //待发货订单数
        int deliver = orderObject.optInt("wait_deliver", 0);
        //待收货
        int take = orderObject.optInt("wait_take", 0);
        //已成交/待评价
        int evaluate = orderObject.optInt("wait_evaluate", 0);
        //退货退款
        int refund = orderObject.optInt("wait_refund", 0);
        //全部订单数
        int total = pay + deliver + take + evaluate;

        orderMsg = new int[]{total, pay, deliver, take, evaluate};

        pcMultipleItems.add(new PCMultipleItem(PCMultipleItem.TYPE_3, R.mipmap.pc_daifukuan, pay, "待付款", PCMultipleItem.TYPE_SPAN_SIZE_4));
        pcMultipleItems.add(new PCMultipleItem(PCMultipleItem.TYPE_3, R.mipmap.pc_daifahuo, deliver, "待发货", PCMultipleItem.TYPE_SPAN_SIZE_4));
        pcMultipleItems.add(new PCMultipleItem(PCMultipleItem.TYPE_3, R.mipmap.pc_yichengjiao, take, "已成交", PCMultipleItem.TYPE_SPAN_SIZE_4));
        pcMultipleItems.add(new PCMultipleItem(PCMultipleItem.TYPE_3, R.mipmap.pc_daipingjia, evaluate, "待评价", PCMultipleItem.TYPE_SPAN_SIZE_4));
        pcMultipleItems.add(new PCMultipleItem(PCMultipleItem.TYPE_3, R.mipmap.pc_tuikuan, refund, "退款/售后", PCMultipleItem.TYPE_SPAN_SIZE_4));

        //粗分割线
        pcMultipleItems.add(new PCMultipleItem(PCMultipleItem.TYPE_4));

        JSONObject listObject = Object.optJSONObject("nav_list_info");

        //学堂
        String school = listObject.optJSONObject("school_nav").optString("sub_title");
        pcMultipleItems.add(new PCMultipleItem(PCMultipleItem.TYPE_1, R.mipmap.pc_xuetang, "钱来钱往学堂", R.mipmap.pc_qianjin, school));

        //粗分割线
        pcMultipleItems.add(new PCMultipleItem(PCMultipleItem.TYPE_4));


        //代金券
        String voucher = listObject.optJSONObject("voucher_nav").optString("sub_title");
        pcMultipleItems.add(new PCMultipleItem(PCMultipleItem.TYPE_1, R.mipmap.pc_daijinquan, "代金券", R.mipmap.pc_qianjin, voucher));

        //分割线
        pcMultipleItems.add(new PCMultipleItem(PCMultipleItem.TYPE_2));

        //账户余额
        String balance = listObject.optJSONObject("balance_nav").optString("sub_title");
        pcMultipleItems.add(new PCMultipleItem(PCMultipleItem.TYPE_1, R.mipmap.pc_zhanghuyue, "账户余额", R.mipmap.pc_qianjin, balance));

        //分割线
        pcMultipleItems.add(new PCMultipleItem(PCMultipleItem.TYPE_2));

        //当日任务
        String task = listObject.optJSONObject("today_task_nav").optString("sub_title");
        pcMultipleItems.add(new PCMultipleItem(PCMultipleItem.TYPE_1, R.mipmap.pc_dangrirenwu, "当日任务", R.mipmap.pc_qianjin, task));
        //分割线
        pcMultipleItems.add(new PCMultipleItem(PCMultipleItem.TYPE_2));

        //收货地址
        String address = listObject.optJSONObject("shipping_address_nav").optString("sub_title");
        pcMultipleItems.add(new PCMultipleItem(PCMultipleItem.TYPE_1, R.mipmap.pc_shouhuodizhi, "收货地址", R.mipmap.pc_qianjin, address));

        //粗分割线
        pcMultipleItems.add(new PCMultipleItem(PCMultipleItem.TYPE_4));

        //在线客服
        String phone = listObject.optJSONObject("customer_services_nav").optString("sub_title");
        pcMultipleItems.add(new PCMultipleItem(PCMultipleItem.TYPE_5, R.mipmap.pc_kefu, "在线客服", phone));
        //分割线
        pcMultipleItems.add(new PCMultipleItem(PCMultipleItem.TYPE_2));

        //设置
        pcMultipleItems.add(new PCMultipleItem(PCMultipleItem.TYPE_1, R.mipmap.pc_shezhi, "设置", R.mipmap.pc_qianjin, ""));

        //粗分割线
        pcMultipleItems.add(new PCMultipleItem(PCMultipleItem.TYPE_4));
    }

    @Override
    public void handleReqFailed() {
        refreshLat.setRefreshing(false);
    }

    @Override
    public void handle404(String message) {
        refreshLat.setRefreshing(false);
        getDm().buildAlertDialog(message);
    }

    @Override
    public void handleReLogin() {
        refreshLat.setRefreshing(false);
    }

    @Override
    public void handleNoNetWork() {
        refreshLat.setRefreshing(false);
        getDm().buildAlertDialog(getString(R.string.no_network));
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
            } else {
                getMeData();
            }
        }
    }
}
