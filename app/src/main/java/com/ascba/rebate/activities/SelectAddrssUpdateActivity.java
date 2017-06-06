package com.ascba.rebate.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.adapter.SelectAddressAdapter;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.application.MyApplication;
import com.ascba.rebate.beans.ReceiveAddressBean;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.ShopABarText;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.yanzhenjie.nohttp.rest.Request;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Created by 李平 2017/06/06 0005.
 * 选择收货地址（最好不要使用之前的）
 */

public class SelectAddrssUpdateActivity extends BaseNetActivity {
    public static final int REQUEST_ADDRESS=0;
    private ArrayList<ReceiveAddressBean> beanList=new ArrayList<>();//收货地址
    private Context context;
    private SelectAddressAdapter adapter;
    private Handler handler = new Handler();
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_address);
        context = this;
        initUI();
        requestNetwork(UrlUtils.getMemberAddress, 0);
    }

    private void requestNetwork(String url, int what) {
        Request<JSONObject> request = buildNetRequest(url, 0, true);
        if(what==1){
            request.add("member_id", AppConfig.getInstance().getInt("uuid", -1000));
            request.add("member_address_id", beanList.get(position).getId());
        }
        executeNetWork(what, request, "请稍后");
    }

    @Override
    protected void mhandle200Data(int what, JSONObject object, JSONObject dataObj, String message) {
        if (what == 0) {
            parseAndSetData(dataObj);
        }else if(what==1){
            beanList.remove(position);
            adapter.notifyItemRemoved(position);
        }
    }

    private void parseAndSetData(JSONObject dataObj) {
        beanList.clear();
        JSONArray jsonArray = dataObj.optJSONArray("member_address_list");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.optJSONObject(i);
            ReceiveAddressBean addressBean = new ReceiveAddressBean();
            addressBean.setId(obj.optString("id"));
            addressBean.setName(obj.optString("consignee"));
            addressBean.setPhone(obj.optString("mobile"));
            addressBean.setAddress(obj.optString("address"));
            addressBean.setAddressDetl(obj.optString("address_detail"));
            addressBean.setProvince(obj.optString("province"));
            addressBean.setCity(obj.optString("city"));
            addressBean.setDistrict(obj.optString("district"));
            addressBean.setTwon(obj.optString("twon"));
            String isSelected = obj.optString("default");
            addressBean.setIsDefault(isSelected);
            //排序：如果有默认收货地址就排到第一位
            if (isSelected.equals("1")) {
                beanList.add(0, addressBean);
            } else {
                beanList.add(addressBean);
            }
            //设置选中状态
            setSelectState();
        }
    }

    //设置选中状态
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

    private void initUI() {
        initShaoABar();
        //新加收货地址
        initApplyBtn();
        initRecyclerView();
    }

    private void initRecyclerView() {
        RecyclerView recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(context));
        adapter = new SelectAddressAdapter(R.layout.item_select_address, beanList);
        View empty = LayoutInflater.from(context).inflate(R.layout.empty_address, null);
        adapter.setEmptyView(empty);
        recyclerview.setAdapter(adapter);
        adapter.setCallback(new SelectAddressAdapter.Callback() {

            @Override
            public void click(int position) {
                SelectAddrssUpdateActivity.this.position=position;
                requestNetwork(UrlUtils.memberAddressDel,1);
            }
        });
        //选择收货地址回调给确认订单页面
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
                        setResult(RESULT_OK, intent);
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

    private void initApplyBtn() {
        View btnAddAddress = findViewById(R.id.address_btn);
        btnAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddAdressActivity.class);
                startActivityForResult(intent, 2);
            }
        });
    }

    private void initShaoABar() {
        ShopABarText shopbar = (ShopABarText) findViewById(R.id.shopbar);
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
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //新加收货地址||编辑，刷新数据
        if (requestCode == 2 && resultCode == 2) {
            requestNetwork(UrlUtils.getMemberAddress, 0);
        }
    }
}
