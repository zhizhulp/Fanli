package com.ascba.rebate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWorkActivity;
import com.ascba.rebate.adapter.WhiteTicketAdapter;
import com.ascba.rebate.beans.WhiteTicket;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.ScrollViewWithListView;
import com.yolanda.nohttp.rest.Request;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WhiteScoreActivity extends BaseNetWorkActivity implements BaseNetWorkActivity.Callback {

    private ScrollViewWithListView listView;
    private List<WhiteTicket> mList;
    private WhiteTicketAdapter wta;
    private View noView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_white_to);
        initViews();
        requestNetWork();
    }

    private void requestNetWork() {
        Request<JSONObject> request = buildNetRequest(UrlUtils.getCashingList, 0, true);
        executeNetWork(request,"请稍后");
        setCallback(this);
    }

    private void initViews() {
        noView = findViewById(R.id.no_ticket_view);
        listView = ((ScrollViewWithListView) findViewById(R.id.cash_ticket_list));
        initData();
        wta=new WhiteTicketAdapter(mList,this);
        wta.setCallback(new WhiteTicketAdapter.Callback() {
            @Override
            public void onExchangeClick(int position) {

                Intent intent=new Intent(WhiteScoreActivity.this,WSExchangeActivity.class);
                intent.putExtra("cashing_id",mList.get(position).getId());
                startActivity(intent);
            }
        });
        listView.setAdapter(wta);
    }

    private void initData() {
        mList=new ArrayList<>();
    }

    @Override
    public void handle200Data(JSONObject dataObj, String message) {
        int is_cashing_list = dataObj.optInt("is_cashing_list");//是否有券
        if(is_cashing_list==0){//无券
            noView.setVisibility(View.VISIBLE);
        }else {//有券
            noView.setVisibility(View.GONE);
            JSONArray list = dataObj.optJSONArray("cashing_list");
            for (int i = 0; i < list.length(); i++) {
                JSONObject cashObj = list.optJSONObject(i);
                int id = cashObj.optInt("id");
                String cashing_money = cashObj.optString("cashing_money");
                long create_time = cashObj.optLong("create_time");
                WhiteTicket wt=new WhiteTicket(cashing_money,id);
                mList.add(wt);
            }
            wta.notifyDataSetChanged();
        }


    }
}
