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
import com.ascba.rebate.activities.base.NetworkBaseActivity;
import com.ascba.rebate.activities.login.LoginActivity;
import com.ascba.rebate.adapter.TicketAdapter;
import com.ascba.rebate.beans.Ticket;
import com.ascba.rebate.handlers.CheckThread;
import com.ascba.rebate.handlers.PhoneHandler;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.ScrollViewWithListView;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TicketActivity extends NetworkBaseActivity {

    private ScrollViewWithListView ticketListView;
    private TicketAdapter ticketAdapter;
    private List<Ticket> mList;
    private SharedPreferences sf;
    private View noIv;
    private View noTv;
    private Button btnTicket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);
        initViews();
        requestServer();
    }


    private void requestServer() {
        sendMsgToSevr(UrlUtils.getVoucherList,0);
        CheckThread checkThread = getCheckThread();
        if(checkThread!=null){
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
                    String message = jObj.optString("msg");
                    if(status==200){
                        JSONObject dataObj = jObj.optJSONObject("data");
                        JSONArray list = dataObj.optJSONArray("voucher_list");
                        if(list!=null && list.length()!=0){
                            noIv.setVisibility(View.GONE);
                            noTv.setVisibility(View.GONE);
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
                            btnTicket.setText("前往商城");
                        }else{
                            noIv.setVisibility(View.VISIBLE);
                            noTv.setVisibility(View.VISIBLE);
                            btnTicket.setText("兑换代金券");
                        }
                    } else if(status==1||status==2||status==3||status == 4||status==5){//缺少sign参数
                        Intent intent = new Intent(TicketActivity.this, LoginActivity.class);
                        sf.edit().putInt("uuid", -1000).apply();
                        startActivity(intent);
                        finish();
                    } else if(status==404){
                        Toast.makeText(TicketActivity.this, message, Toast.LENGTH_SHORT).show();
                    } else if(status==500){
                        Toast.makeText(TicketActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                }
            });
            checkThread.start();
            dialog.show();
        }

    }

    private void initViews() {
        sf=getSharedPreferences("first_login_success_name_password", MODE_PRIVATE);
        initListView();
        noIv = findViewById(R.id.no_ticket_icon);
        noTv = findViewById(R.id.no_ticket_text);
        btnTicket = ((Button) findViewById(R.id.ticket_btn));
    }

    private void initListView() {
        ticketListView = ((ScrollViewWithListView) findViewById(R.id.ticket_list));
        initData();
        ticketAdapter = new TicketAdapter(mList,this);
        ticketListView.setAdapter(ticketAdapter);
    }

    private void initData() {
        mList=new ArrayList<>();
       /* for (int i = 0; i < 7; i++) {
            Ticket ticket=new Ticket(0,500,20150629,"商城代金券");
            mList.add(ticket);
        }*/
    }


}
