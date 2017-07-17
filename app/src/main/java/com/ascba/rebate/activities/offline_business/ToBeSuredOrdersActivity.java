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
    private TextView tobe_sure_tv1, tobe_sure_tv2, tobe_sure_tv3, seller_sure_order_tv4, tobe_sure_noorders;
    //private ToBeSuredOrdersAdapter adapter;
    List<ToBeSuredOrdersEntity.DataListBean> data_list = new ArrayList<>();
    private int order_id;
    ToBeSuredOrdersEntity.IdenInfoBean iden_info;
    private View head;
    public static final int CODE_RESULE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_be_sured_orders);
        initRefreshLayout();
        initView();
        requestNetwork(UrlUtils.sureOrderList, 0);
    }

    private void initView() {
        tobe_sure_noorders = (TextView) findViewById(R.id.tobe_sure_noorders);
        recycler = (RecyclerView) findViewById(R.id.tobe_sure_recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        baseAdapter = new ToBeSuredOrdersAdapter(R.layout.item_myallorders, data_list);
        head = ViewUtils.getView(this, R.layout.tobesuredorders_head);
        baseAdapter.addHeaderView(head);
        recycler.setAdapter(baseAdapter);
        baseAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(ToBeSuredOrdersActivity.this, SellerOrderDetailActivity.class);
                order_id = data_list.get(position).getFivepercent_log_id();
                intent.putExtra("type","other");
                intent.putExtra("order_id", order_id);
                intent.putExtra("into_type", 1);
                startActivityForResult(intent, CODE_RESULE);
            }
        });

        Intent intent = getIntent();
        setResult(RESULT_OK, intent);


        loadRequestor = new LoadRequestor() {
            @Override
            public void loadMore() {
                requestNetwork(UrlUtils.sureOrderList, 0);
            }

            @Override
            public void pullToRefresh() {

                requestNetwork(UrlUtils.sureOrderList, 0);
            }
        };
        initLoadMoreRequest();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CODE_RESULE:
                if (resultCode == RESULT_OK) {
                    resetPage();
                    requestNetwork(UrlUtils.sureOrderList, 0);
                }
                break;
        }
    }

    public void requestNetwork(String url, int what) {
        Request<JSONObject> request = buildNetRequest(url, 0, true);
        request.add("now_page", now_page);
        executeNetWork(what, request, "请稍后");
    }

    @Override
    protected void mhandle200Data(int what, JSONObject object, JSONObject dataObj, String message) {
        super.mhandle200Data(what, object, dataObj, message);
        ToBeSuredOrdersEntity toBeSuredOrdersEntity = JSON.parseObject(dataObj.toString(), ToBeSuredOrdersEntity.class);
        if (iden_info != null) {
            iden_info = null;

        }
        iden_info = toBeSuredOrdersEntity.getIden_info();
        tobe_sure_tv1 = (TextView) head.findViewById(R.id.tobe_sure_tv1);
        tobe_sure_tv2 = (TextView) head.findViewById(R.id.tobe_sure_tv22);
        tobe_sure_tv3 = (TextView) head.findViewById(R.id.tobe_sure_tv3);
        seller_sure_order_tv4 = (TextView) head.findViewById(R.id.seller_sure_order_tv4);
        tobe_sure_tv1.setText(iden_info.getTitle());
        tobe_sure_tv2.setText(iden_info.getTotal_money() + "");
        tobe_sure_tv3.setText("共计" + iden_info.getTotal() + "笔");
        seller_sure_order_tv4.setText(iden_info.getTip());
        if (isRefreshing) {//是下拉刷新才清楚数据
            if (data_list.size() != 0) {
                data_list.clear();

            }
        }

        data_list.addAll(toBeSuredOrdersEntity.getData_list());
        baseAdapter.notifyDataSetChanged();
        if (data_list.size() == 0) {
            tobe_sure_noorders.setVisibility(View.VISIBLE);
        }


    }

}
