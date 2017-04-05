package com.ascba.rebate.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWork4Activity;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.beans.ReceiveAddressBean;
import com.ascba.rebate.handlers.DialogManager;
import com.ascba.rebate.utils.UrlEncodeUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.ShopABar;
import com.ascba.rebate.view.SuperSwipeRefreshLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.yolanda.nohttp.rest.Request;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李鹏 on 2017/03/13 0013.
 * 售货地址管理
 */

public class ReceiveAddressActivity extends BaseNetWork4Activity {

    private ShopABar shopABar;
    private RecyclerView recyclerView;
    private LinearLayout addBtn;
    private List<ReceiveAddressBean> beanList = new ArrayList<>();
    private Context context;
    private MyAdapter myAdapter;
    private SuperSwipeRefreshLayout refreshLayout;
    private DialogManager dm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_address);
        context = this;
        initView();
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

        /**
         * 新增收货地址
         */
        addBtn = (LinearLayout) findViewById(R.id.activity_receive_address_btn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddAdressActivity.class);
                startActivity(intent);
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.activity_receive_address_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        getData();
        myAdapter = new MyAdapter(R.layout.item_receive_address, beanList);
        recyclerView.setAdapter(myAdapter);

        recyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.item_receive_address_del:
                        Toast.makeText(context, "删除：" + position, Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.item_receive_address_edit:
                        Toast.makeText(context, "编辑：" + position, Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.item_receive_address_check:
                        Toast.makeText(context, "check：" + position, Toast.LENGTH_SHORT).show();
                        ReceiveAddressBean bean = beanList.get(position);
                        if (!bean.isSelect()) {
                            for (ReceiveAddressBean receiveAddressBean : beanList) {
                                receiveAddressBean.setSelect(false);
                            }
                            bean.setSelect(true);
                            myAdapter.notifyDataSetChanged();
                        }
                        break;
                }
            }
        });
    }

    public class MyAdapter extends BaseQuickAdapter<ReceiveAddressBean, BaseViewHolder> {


        public MyAdapter(int layoutResId, List<ReceiveAddressBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, ReceiveAddressBean item) {
            //姓名
            helper.setText(R.id.item_receive_address_name, item.getName());

            //隐藏手机号中间4位
            String phone = item.getPhone();
            phone = phone.substring(0, 3) + "****" + phone.substring(7, 11);
            helper.setText(R.id.item_receive_address_phone, phone);

            //地址
            helper.setText(R.id.item_receive_address_address, item.getAddress());
            helper.addOnClickListener(R.id.item_receive_address_del);
            helper.addOnClickListener(R.id.item_receive_address_edit);

            CheckBox checkBox = helper.getView(R.id.item_receive_address_check);
            checkBox.setChecked(item.isSelect());
            if (item.isSelect()) {
                checkBox.setEnabled(false);
            } else {
                checkBox.setEnabled(true);
            }
            helper.addOnClickListener(R.id.item_receive_address_check);
        }
    }

    private void getData() {
        ReceiveAddressBean bean1 = new ReceiveAddressBean("纸飞机", "18332145692", "北京市朝阳区三间房街道福盈家园4号院2号楼2单元", true);
        beanList.add(bean1);

        for (int i = 0; i < 4; i++) {
            ReceiveAddressBean bean2 = new ReceiveAddressBean("木子", "15350732091", "北京市朝阳区三间房街道福盈家园1号院1号楼1单元", false);
            beanList.add(bean2);
        }

        dm = new DialogManager(context);
        Request<JSONObject> jsonRequest = buildNetRequest(UrlUtils.getMemberAddress, 0, true);
        jsonRequest.add("sign", UrlEncodeUtils.createSign(UrlUtils.getMemberAddress));
        jsonRequest.add("member_id", AppConfig.getInstance().getInt("uuid", -1000));
        executeNetWork(jsonRequest, "请稍后");
        setCallback(new Callback() {
            @Override
            public void handle200Data(JSONObject dataObj, String message) {
                JSONArray jsonArray = dataObj.optJSONArray("member_address_list");
                for (int i = 0; i < jsonArray.length(); i++) {
                    ReceiveAddressBean addressBean=new ReceiveAddressBean();
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
