package com.ascba.rebate.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWorkActivity;
import com.ascba.rebate.adapter.ReceiveAddressAdapter;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.beans.ReceiveAddressBean;
import com.ascba.rebate.handlers.DialogManager;
import com.ascba.rebate.utils.UrlEncodeUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.ShopABar;
import com.ascba.rebate.view.SuperSwipeRefreshLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.yanzhenjie.nohttp.rest.Request;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李鹏 on 2017/03/13 0013.
 * 售货地址管理
 */

public class ReceiveAddressWorkActivity extends BaseNetWorkActivity implements SuperSwipeRefreshLayout.OnPullRefreshListener {

    private ShopABar shopABar;
    private RecyclerView recyclerView;
    private LinearLayout addBtn;
    private List<ReceiveAddressBean> beanList = new ArrayList<>();
    private Context context;
    private ReceiveAddressAdapter myAdapter;
    private SuperSwipeRefreshLayout refreshLayout;
    private DialogManager dm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_address);
        context = this;
        initView();
        getData();
    }

    private void initView() {
        shopABar = (ShopABar) findViewById(R.id.activity_receive_address_bar);
        shopABar.setImageOtherEnable(false);
        shopABar.setMsgEnable(false);
        shopABar.setCallback(new ShopABar.Callback() {
            @Override
            public void back(View v) {
                finish();
            }

            @Override
            public void clkMsg(View v) {

            }

            @Override
            public void clkOther(View v) {

            }
        });

        refreshLayout = (SuperSwipeRefreshLayout) findViewById(R.id.refresh_layout);
        refreshLayout.setOnPullRefreshListener(this);

        /**
         * 新增收货地址
         */
        addBtn = (LinearLayout) findViewById(R.id.activity_receive_address_btn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddAdressWorkActivity.class);
                startActivityForResult(intent, 2);
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.activity_receive_address_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        myAdapter = new ReceiveAddressAdapter(R.layout.item_receive_address, beanList);
        recyclerView.setAdapter(myAdapter);

        View emptyView = LayoutInflater.from(context).inflate(R.layout.empty_address, null);
        myAdapter.setEmptyView(emptyView);

        recyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.item_receive_address_del:
                        //删除
                        deleteAddress(position);
                        break;
                    case R.id.item_receive_address_edit:
                        //编辑
                        Intent intent = new Intent(context, EditAdressWorkActivity.class);
                        intent.putExtra("address", beanList.get(position));
                        startActivityForResult(intent, 2);
                        break;
                    case R.id.item_receive_address_check:
                        //设置默认地址
                        ReceiveAddressBean bean = beanList.get(position);
                        if (bean.getIsDefault().equals("0")) {
                            setDefault(bean);
                        }
                        break;
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        getData();
    }

    @Override
    public void onPullDistance(int distance) {

    }

    @Override
    public void onPullEnable(boolean enable) {

    }


    /**
     * 获取收货地址数据
     */
    private void getData() {
        dm = new DialogManager(context);
        Request<JSONObject> jsonRequest = buildNetRequest(UrlUtils.getMemberAddress, 0, true);
        jsonRequest.add("member_id", AppConfig.getInstance().getInt("uuid", -1000));
        executeNetWork(jsonRequest, "请稍后");
        setCallback(new Callback() {
            @Override
            public void handle200Data(JSONObject dataObj, String message) {
                beanList.clear();
                JSONArray jsonArray = dataObj.optJSONArray("member_address_list");
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject object = jsonArray.getJSONObject(i);
                        ReceiveAddressBean addressBean = new ReceiveAddressBean();
                        addressBean.setId(object.optString("id"));
                        addressBean.setName(object.optString("consignee"));
                        addressBean.setPhone(object.optString("mobile"));
                        addressBean.setAddress(object.optString("address"));
                        addressBean.setProvince(object.optString("province"));
                        addressBean.setCity(object.optString("city"));
                        addressBean.setDistrict(object.optString("district"));
                        addressBean.setTwon(object.optString("twon"));
                        String isSelected = object.optString("default");
                        addressBean.setIsDefault(isSelected);

                        if (isSelected.equals("1")) {
                            beanList.add(0, addressBean);
                        } else {
                            beanList.add(addressBean);
                        }
                        myAdapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void handle404(String message) {
                dm.buildAlertDialog(message);
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void handleNoNetWork() {
                dm.buildAlertDialog("请检查网络！");
                refreshLayout.setRefreshing(false);
            }
        });
    }

    /**
     * 删除收货地址
     *
     * @param postition
     */
    private void deleteAddress(final int postition) {
        dm = new DialogManager(context);
        ReceiveAddressBean bean = beanList.get(postition);
        Request<JSONObject> jsonRequest = buildNetRequest(UrlUtils.memberAddressDel, 0, true);
        jsonRequest.add("member_id", AppConfig.getInstance().getInt("uuid", -1000));
        jsonRequest.add("member_address_id", bean.getId());
        executeNetWork(jsonRequest, "请稍后");
        setCallback(new Callback() {
            @Override
            public void handle200Data(JSONObject dataObj, String message) {
                beanList.remove(postition);
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void handle404(String message) {
                dm.buildAlertDialog(message);
            }

            @Override
            public void handleNoNetWork() {
                dm.buildAlertDialog("请检查网络！");
            }
        });
    }

    /**
     * 设置默认收货地址
     *
     * @param bean
     */
    private void setDefault(final ReceiveAddressBean bean) {
        dm = new DialogManager(context);
        Request<JSONObject> jsonRequest = buildNetRequest(UrlUtils.memberAddressSetDefault, 0, true);
        jsonRequest.add("sign", UrlEncodeUtils.createSign(UrlUtils.memberAddressSetDefault));
        jsonRequest.add("member_id", AppConfig.getInstance().getInt("uuid", -1000));
        jsonRequest.add("member_address_id", bean.getId());
        jsonRequest.add("default", bean.getIsDefault());
        executeNetWork(jsonRequest, "请稍后");
        setCallback(new Callback() {
            @Override
            public void handle200Data(JSONObject dataObj, String message) {
                for (ReceiveAddressBean receiveAddressBean : beanList) {
                    receiveAddressBean.setIsDefault("0");
                }
                bean.setIsDefault("1");
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void handle404(String message) {
                dm.buildAlertDialog(message);
            }

            @Override
            public void handleNoNetWork() {
                dm.buildAlertDialog("请检查网络！");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //增加收货地址||编辑地址，刷新数据
        if (requestCode == 2 && resultCode == 2) {
            getData();
        }
    }
}
