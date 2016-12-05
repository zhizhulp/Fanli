package com.ascba.rebate.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.ListView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseActivity;
import com.ascba.rebate.activities.base.NetworkBaseActivity;
import com.ascba.rebate.adapter.TicketAdapter;
import com.ascba.rebate.beans.Card;
import com.ascba.rebate.beans.Ticket;
import com.ascba.rebate.handlers.CheckThread;
import com.ascba.rebate.handlers.PhoneHandler;
import com.ascba.rebate.view.ScrollViewWithListView;
import com.yolanda.nohttp.rest.Request;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TicketActivity extends NetworkBaseActivity {

    private ScrollViewWithListView ticketListView;
    private TicketAdapter ticketAdapter;
    private List<Ticket> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);
        initViews();
        requestServer();
    }


    private void requestServer() {
        sendMsgToSevr("http://api.qlqwgw.com/v1/getVoucherList",0);
        CheckThread checkThread = getCheckThread();
        PhoneHandler phoneHandler = checkThread.getPhoneHandler();
        final ProgressDialog dialog = new ProgressDialog(phoneHandler.getContext(), R.style.dialog);
        dialog.setMessage("请稍后");
        phoneHandler.setCallback(phoneHandler.new Callback2(){
            @Override
            public void getMessage(Message msg) {
                super.getMessage(msg);
                dialog.dismiss();
                JSONObject jObj = (JSONObject) msg.obj;
                int status = jObj.optInt("status");
                if(status==200){
                    JSONArray list = jObj.optJSONArray("voucher_list");
                    if(list!=null){
                        for (int i = 0; i < list.length(); i++) {
                            JSONObject jsonObject = list.optJSONObject(i);
                            int id = jsonObject.optInt("id");
                            String type = jsonObject.optString("type");
                            int virtual_money = jsonObject.optInt("virtual_money");
                            long expiring_time = jsonObject.optLong("expiring_time");
                            Ticket ticket=new Ticket(id,virtual_money,expiring_time,type);
                            mList.add(ticket);
                        }
                        ticketAdapter.notifyDataSetChanged();
                    }

                }
            }
        });
        checkThread.start();
        dialog.show();
    }

    private void initViews() {
        initListView();
    }

    private void initListView() {
        ticketListView = ((ScrollViewWithListView) findViewById(R.id.ticket_list));
        initData();
        ticketAdapter = new TicketAdapter(mList,this);
        ticketListView.setAdapter(ticketAdapter);
    }

    private void initData() {
        mList=new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            Ticket ticket=new Ticket(0,500,20150629,"商城代金券");
            mList.add(ticket);
        }
    }


}
