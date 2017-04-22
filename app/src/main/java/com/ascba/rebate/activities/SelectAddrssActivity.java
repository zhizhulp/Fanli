package com.ascba.rebate.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.adapter.SelectAddressAdapter;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.application.MyApplication;
import com.ascba.rebate.beans.ReceiveAddressBean;
import com.ascba.rebate.handlers.DialogManager;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.ShopABarText;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.yanzhenjie.nohttp.rest.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by 李鹏 on 2017/04/05 0005.
 * 选择收货地址
 */

public class SelectAddrssActivity extends BaseNetActivity {

    private ArrayList<ReceiveAddressBean> beanList;//收货地址
    private Context context;
    private ShopABarText shopbar;
    private RecyclerView recyclerview;
    private DialogManager dm;
    private SelectAddressAdapter adapter;
    private LinearLayout btnAddAddress;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_address);
        context = this;
        getAddressList();
    }

    /**
     * 接受收货地址list
     */
    private void getAddressList() {
        Intent intent = getIntent();
        if (intent != null) {
            beanList = intent.getParcelableArrayListExtra("address");
            initUI();
            setSelectState();
        }
    }

    /**
     * 设置选中状态
     */
    private void setSelectState() {
        for (int i = 0; i < beanList.size(); i++) {
            if (beanList.get(i).getId().equals(MyApplication.addressId)) {
                beanList.get(i).setSelect(true);
            } else {
                beanList.get(i).setSelect(false);
            }
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * 初始化UI
     */
    private void initUI() {
        shopbar = (ShopABarText) findViewById(R.id.shopbar);
        shopbar.setBtnEnable(false);
        shopbar.setCallback(new ShopABarText.Callback() {
            @Override
            public void back(View v) {
                finish();
            }

            @Override
            public void clkBtn(View v) {
                Intent intentAddress = new Intent(context, ReceiveAddressActivity.class);
                startActivity(intentAddress);
            }
        });

        //新加收货地址
        btnAddAddress = (LinearLayout) findViewById(R.id.address_btn);
        btnAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddAdressActivity.class);
                startActivityForResult(intent, 2);
            }
        });

        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(context));
        adapter = new SelectAddressAdapter(R.layout.item_select_address, beanList);
        View empty = LayoutInflater.from(context).inflate(R.layout.empty_address, null);
        adapter.setEmptyView(empty);
        recyclerview.setAdapter(adapter);

        /**
         * 选择收货地址回调给确认订单页面
         */
        recyclerview.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.item_select_address_rl:
                        //更新选中状态
                        MyApplication.addressId = beanList.get(position).getId();
                        setSelectState();

                        //选择
                        Intent intent = new Intent();
                        intent.putExtra("address", beanList.get(position));
                        setResult(1, intent);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 300);

                        break;
                    case R.id.item_select_address_edit:
                        //编辑
                        Intent intent2 = new Intent(context, EditAdressActivity.class);
                        intent2.putExtra("address", beanList.get(position));
                        startActivityForResult(intent2, 2);
                        break;
                }
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //新加收货地址||编辑，刷新数据
        if (requestCode == 2 && resultCode == 2) {
            getData();
        }
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

                        //排序：如果有默认收货地址就排到第一位
                        if (isSelected.equals("1")) {
                            beanList.add(0, addressBean);
                        } else {
                            beanList.add(addressBean);
                        }

                        //设置选中状态
                        setSelectState();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
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
}
