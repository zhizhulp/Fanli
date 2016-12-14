package com.ascba.rebate.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.Base2Activity;
import com.ascba.rebate.activities.base.NetworkBaseActivity;
import com.ascba.rebate.activities.login.LoginActivity;
import com.ascba.rebate.adapter.TicketAdapter;
import com.ascba.rebate.adapter.TicketAdapter2;
import com.ascba.rebate.beans.Ticket;
import com.ascba.rebate.handlers.CheckThread;
import com.ascba.rebate.handlers.PhoneHandler;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.ScrollViewWithListView;
import com.yolanda.nohttp.rest.Request;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TicketActivity extends Base2Activity implements Base2Activity.Callback {

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
        JSONArray list = dataObj.optJSONArray("voucher_list");
        if(list!=null && list.length()!=0){
            noIv.setVisibility(View.GONE);
            noTv.setVisibility(View.GONE);
            for (int i = 0; i < list.length(); i++) {
                JSONObject jsonObject = list.optJSONObject(i);
                int id = jsonObject.optInt("id");
                String type = jsonObject.optString("type");
                int virtual_money = jsonObject.optInt("virtual_money");
                String expiring_time = jsonObject.optString("expiring_time");//???????????????
                int state = jsonObject.optInt("state");
                Ticket ticket=new Ticket(id,virtual_money,expiring_time,type,state);
                mList.add(ticket);
            }
            ticketAdapter.notifyDataSetChanged();
            btnTicket.setText("前往商城");
        }else{
            noIv.setVisibility(View.VISIBLE);
            noTv.setVisibility(View.VISIBLE);
            btnTicket.setText("兑换代金券");
        }
    }
}
