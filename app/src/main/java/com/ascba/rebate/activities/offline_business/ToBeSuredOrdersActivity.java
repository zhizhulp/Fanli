package com.ascba.rebate.activities.offline_business;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.adapter.sweep.ToBeSuredOrdersAdapter;
import com.ascba.rebate.beans.sweep.ToBeSuredOrdersEntity;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.utils.ViewUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yanzhenjie.nohttp.rest.Request;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ToBeSuredOrdersActivity extends BaseNetActivity {
    private RecyclerView recycler;
    private int paged=1;
    private TextView tobe_sure_tv1,tobe_sure_tv2,tobe_sure_tv3,seller_sure_order_tv4;
    private ToBeSuredOrdersAdapter adapter;
    List<ToBeSuredOrdersEntity.DataListBean> data_list=new ArrayList<>();
    private int order_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_be_sured_orders);
        initView();
        requestNetwork(UrlUtils.sureOrderList,0);

    }

    private void initView() {



        recycler= (RecyclerView) findViewById(R.id.tobe_sure_recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter=new ToBeSuredOrdersAdapter(R.layout.item_myallorders,data_list);

        recycler.setAdapter(adapter);


        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent=new Intent(ToBeSuredOrdersActivity.this,SellerOrderDetailActivity.class);
              if(data_list!=null ){
                  order_id = data_list.get(position).getOrder_id();
              }
                intent.putExtra("order_id",order_id);
                startActivity(intent);

            }
        });


    }
    public void requestNetwork(String url, int what) {
        Request<JSONObject> request = buildNetRequest(url, 0, true);
        request.add("paged",paged);
        executeNetWork(what,request,"请稍后");
    }
//    "title": "待确认总额",
//            "total_money": "10000元",
//            "total": 10,
//            "tip": "仅
    @Override
    protected void mhandle200Data(int what, JSONObject object, JSONObject dataObj, String message) {
        super.mhandle200Data(what, object, dataObj, message);
        ToBeSuredOrdersEntity toBeSuredOrdersEntity = JSON.parseObject(dataObj.toString(), ToBeSuredOrdersEntity.class);
        ToBeSuredOrdersEntity.IdenInfoBean iden_info = toBeSuredOrdersEntity.getIden_info();
        View head = ViewUtils.getView(this, R.layout.tobesuredorders_head);

        tobe_sure_tv1= (TextView) head.findViewById(R.id.tobe_sure_tv1);
        tobe_sure_tv2= (TextView) head.findViewById(R.id.tobe_sure_tv2);
        tobe_sure_tv3= (TextView) head.findViewById(R.id.tobe_sure_tv3);
        seller_sure_order_tv4= (TextView) head.findViewById(R.id.seller_sure_order_tv4);

        tobe_sure_tv1.setText(iden_info.getTitle());
        tobe_sure_tv2.setText(iden_info.getTotal_money());
        tobe_sure_tv3.setText("共计"+iden_info.getTotal()+"笔");
        seller_sure_order_tv4.setText(iden_info.getTip());
        data_list.addAll(toBeSuredOrdersEntity.getData_list());
        adapter.addHeaderView(head);
        adapter.notifyDataSetChanged();



    }
}
