package com.ascba.rebate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWorkActivity;
import com.ascba.rebate.adapter.TicketAdapter2;
import com.ascba.rebate.beans.Ticket;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.ScrollViewWithListView;
import com.yolanda.nohttp.rest.Request;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TicketActivity extends BaseNetWorkActivity implements BaseNetWorkActivity.Callback,View.OnClickListener {

    private ScrollViewWithListView ticketListView;
    private TicketAdapter2 ticketAdapter;
    private List<Ticket> mList;
    private View noIv;
    private View noTv;
    private Button btnTicket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);
        initViews();
        requestServer(UrlUtils.getVoucherList);
    }


    private void requestServer(String url) {
        Request<JSONObject> request = buildNetRequest(url, 0, true);
        executeNetWork(request,"请稍后");
        setCallback(this);
    }

    private void initViews() {
        initListView();
        noIv = findViewById(R.id.no_ticket_icon);
        noTv = findViewById(R.id.no_ticket_text);
        btnTicket = ((Button) findViewById(R.id.ticket_btn));
        btnTicket.setOnClickListener(this);
    }

    private void initListView() {
        ticketListView = ((ScrollViewWithListView) findViewById(R.id.ticket_list));
        initData();
        ticketAdapter = new TicketAdapter2(mList,this);
        ticketListView.setAdapter(ticketAdapter);
    }

    private void initData() {
        mList=new ArrayList<>();
        /*for (int i = 0; i < 7; i++) {
            if(i%3==0){
                Ticket ticket=new Ticket(0,500,"有效期  2016.12.13-2016-12.14","商城代金券",0);
                mList.add(ticket);
            }else if(i%3==1){
                Ticket ticket=new Ticket(1,100,"有效期  2016.12.13-2016-12.14","商城代金券",0);
                mList.add(ticket);
            }else if(i%3==2){
                Ticket ticket=new Ticket(2,50,"有效期  2016.12.13-2016-12.14","商城代金券",0);
                mList.add(ticket);
            }
        }*/
    }


    @Override
    public void handle200Data(JSONObject dataObj, String message) {
        int is_shop = dataObj.optInt("is_shop");
        JSONArray list = dataObj.optJSONArray("voucher_list");
        if(list!=null && list.length()!=0){
            noIv.setVisibility(View.GONE);
            noTv.setVisibility(View.GONE);
            for (int i = 0; i < list.length(); i++) {
                JSONObject jsonObject = list.optJSONObject(i);
                int id = jsonObject.optInt("id");
                String type = jsonObject.optString("type");
                int virtual_money = jsonObject.optInt("virtual_money");
                Long expiring_time = jsonObject.optLong("expiring_time");
                Long create_time = jsonObject.optLong("create_time");
                int state = jsonObject.optInt("expiring_status");
                Ticket ticket=new Ticket(id,virtual_money,type,state,create_time,expiring_time);
                mList.add(ticket);
            }
            Collections.sort(mList);
            ticketAdapter.notifyDataSetChanged();
            btnTicket.setText("前往商城");
            if(is_shop==0){
                //btnTicket.setBackgroundColor(0xffc0c0c0);
                btnTicket.setBackgroundDrawable(getResources().getDrawable(R.drawable.ticket_no_shop_bg));
                btnTicket.setEnabled(false);
            }
        }else{
            noIv.setVisibility(View.VISIBLE);
            noTv.setVisibility(View.VISIBLE);
            btnTicket.setText("兑换代金券");
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent=new Intent(this,RedScoreUpdateActivity.class);
        startActivity(intent);
        finish();
    }
}
