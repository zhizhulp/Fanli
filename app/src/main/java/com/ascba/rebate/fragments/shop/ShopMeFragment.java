package com.ascba.rebate.fragments.shop;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.shop.order.MyOrderActivity;
import com.ascba.rebate.activities.ReceiveAddressActivity;
import com.ascba.rebate.activities.RefundOrderActivity;
import com.ascba.rebate.activities.ShopMessageActivity;
import com.ascba.rebate.activities.me_page.TicketActivity;
import com.ascba.rebate.activities.me_page.settings.SettingActivity;
import com.ascba.rebate.adapter.PCMultipleItemAdapter;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.application.MyApplication;
import com.ascba.rebate.beans.PCMultipleItem;
import com.ascba.rebate.fragments.base.BaseNetFragment;
import com.ascba.rebate.utils.UrlUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.yanzhenjie.nohttp.rest.Request;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 商城设置
 */
public class ShopMeFragment extends BaseNetFragment implements SwipeRefreshLayout.OnRefreshListener,
        BaseNetFragment.Callback {
    private Context context;
    private RecyclerView pc_RecyclerView;
    private PCMultipleItemAdapter pcMultipleItemAdapter;
    private List<PCMultipleItem> pcMultipleItems = new ArrayList<>();
    private int[] orderMsg;
    private boolean isFirstResume=true;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();
        initView(view);
        getMeData();
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_shop_me;
    }

    //获取me数据
    private void getMeData() {
        Request<JSONObject> jsonRequest = buildNetRequest(UrlUtils.myPageInfo, 0, true);
        executeNetWork(jsonRequest, "请稍后");
        setCallback(this);
    }

    //初始化UI
    private void initView(View view) {

        pc_RecyclerView = (RecyclerView) view.findViewById(R.id.list_pc);
        final GridLayoutManager manager = new GridLayoutManager(getActivity(), PCMultipleItem.TYPE_SPAN_SIZE_DEFAULT);
        pc_RecyclerView.setLayoutManager(manager);

        pc_RecyclerView.addOnItemTouchListener(new OnItemClickListener() {

            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                Log.d(TAG, "onSimpleItemClick: "+position);
                PCMultipleItem item = pcMultipleItems.get(position);
                switch (position) {
                    case 1:
                        //全部订单
                        MyOrderActivity.startIntent(ShopMeFragment.this, 0, orderMsg);
                        break;
                    case 3:
                        //待付款
                        MyOrderActivity.startIntent(ShopMeFragment.this, 1, orderMsg);
                        break;
                    case 4:
                        //待发货
                        MyOrderActivity.startIntent(ShopMeFragment.this, 2, orderMsg);
                        break;
                    case 5:
                        //已成交
                        MyOrderActivity.startIntent(ShopMeFragment.this, 3, orderMsg);
                        break;
                    case 6:
                        //待评价
                        MyOrderActivity.startIntent(ShopMeFragment.this, 4, orderMsg);
                        break;
                    case 7:
                        //退货
                        RefundOrderActivity.startIntent(getActivity());
                        break;
                    case 9:
                        //学堂
                        showToast("暂未开放");
                        /*Intent college = new Intent(getActivity(), ASKCollegeActivity.class);
                        startActivity(college);*/
                        break;
                    case 11:
                        //代金券
                        Intent ticket = new Intent(getActivity(), TicketActivity.class);
                        startActivity(ticket);
                        break;
                    case 15:
                        //收货地址管理
                        Intent intent = new Intent(getActivity(), ReceiveAddressActivity.class);
                        startActivity(intent);
                        break;
                    case 17:
                        //在线客服
                        Intent phone = new Intent();
                        phone.setAction(Intent.ACTION_DIAL);
                        phone.setData(Uri.parse("tel:" + item.getContenRight()));
                        startActivity(phone);
                        break;
                    case 19:
                        Intent setting = new Intent(getActivity(), SettingActivity.class);
                        startActivity(setting);
                        break;
                }
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemChildClick(adapter, view, position);
                switch (view.getId()){
                    case R.id.activity_pc_item_head_message:
                        ShopMessageActivity.startIntent(getActivity());
                        break;
                }
            }
        });

        initRefreshLayout(view);
        refreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        getMeData();
    }


    @Override
    public void handle200Data(JSONObject dataObj, String message) {
        JSONObject jsonObject = dataObj.optJSONObject("my_page_info");
        initDat(jsonObject);
    }

    private void initDat(JSONObject Object) {
        pcMultipleItems.clear();

        //头信息
        JSONObject meObject = Object.optJSONObject("member_info");
        String headImg = UrlUtils.baseWebsite + meObject.optString("avatar");
        String realname = meObject.optString("realname");
        String nickname = meObject.optString("nickname");
        AppConfig.getInstance().putInt("is_level_pwd",meObject.optInt("is_level_pwd"));//是设置支付密码 还是修改支付密码
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
        pcMultipleItems.add(new PCMultipleItem(PCMultipleItem.TYPE_3, R.mipmap.pc_yichengjiao, take, "待收货", PCMultipleItem.TYPE_SPAN_SIZE_4));
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
        pcMultipleItems.add(new PCMultipleItem(PCMultipleItem.TYPE_1, R.mipmap.pc_daijinquan, "折扣券余额", R.mipmap.pc_qianjin, voucher));

        //分割线
        pcMultipleItems.add(new PCMultipleItem(PCMultipleItem.TYPE_2));

        //账户余额
        String balance = listObject.optJSONObject("balance_nav").optString("sub_title");
        pcMultipleItems.add(new PCMultipleItem(PCMultipleItem.TYPE_5, R.mipmap.pc_zhanghuyue, "代金券余额", balance));

        //分割线
        pcMultipleItems.add(new PCMultipleItem(PCMultipleItem.TYPE_2));

        //当日任务
        /*String task = listObject.optJSONObject("today_task_nav").optString("sub_title");
        pcMultipleItems.add(new PCMultipleItem(PCMultipleItem.TYPE_1, R.mipmap.pc_dangrirenwu, "当日任务", R.mipmap.pc_qianjin, task));
        //分割线
        pcMultipleItems.add(new PCMultipleItem(PCMultipleItem.TYPE_2));*/

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


        pcMultipleItemAdapter = new PCMultipleItemAdapter(pcMultipleItems, context);
        pcMultipleItemAdapter.setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
            @Override
            public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                return pcMultipleItems.get(position).getSpanSize();
            }
        });
        pc_RecyclerView.setAdapter(pcMultipleItemAdapter);
    }

    @Override
    public void handleReqFailed() {
    }

    @Override
    public void handle404(String message, JSONObject dataObj) {
    }

    @Override
    public void handleReLogin() {
    }

    @Override
    public void handleNoNetWork() {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case MyOrderActivity.REQUEST_ORDER:
                if(MyApplication.isRefreshOrderCount){
                    getMeData();
                    MyApplication.isRefreshOrderCount=false;
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!MyApplication.isSignOut && MyApplication.signOutSignInShopMe && !isFirstResume){
            getMeData();
            MyApplication.signOutSignInShopMe=false;
        }
        isFirstResume=false;
    }
}
