package com.ascba.rebate.activities.me_page;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.ascba.rebate.R;
import com.ascba.rebate.activities.TransactionRecordsActivity;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.activities.shop.ShopActivity;
import com.ascba.rebate.adapter.TicketAdapter;
import com.ascba.rebate.beans.Ticket;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.MoneyBar;
import com.ascba.rebate.view.ScrollViewWithListView;
import com.yanzhenjie.nohttp.rest.Request;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TicketActivity extends BaseNetActivity implements BaseNetActivity.Callback,View.OnClickListener {

    private ScrollViewWithListView ticketListView;
    private TicketAdapter ticketAdapter;
    private List<Ticket> mList;
    private View noIv;
    private View noTv;
    private Button btnTicket;
    private MoneyBar moneyBar;
    private View noView;
    private int status;//0 无兑换券 1 有兑换券，可以进入商城 2 有兑换券，不可以进入商城

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

        moneyBar = (MoneyBar) findViewById(R.id.moneyBar);
        //moneyBar.setTailTitle(getString(R.string.inoutcome_record));
        moneyBar.setCallBack(new MoneyBar.CallBack() {
            @Override
            public void clickImage(View im) {
                finish();
            }

            @Override
            public void clickComplete(View tv) {
                //TransactionRecordsActivity.startIntent(TicketActivity.this);
            }
        });

        noView = findViewById(R.id.no_ticket_view);
        btnTicket = ((Button) findViewById(R.id.ticket_btn));
        btnTicket.setOnClickListener(this);
    }

    private void initListView() {
        ticketListView = ((ScrollViewWithListView) findViewById(R.id.ticket_list));
        initData();
        ticketAdapter = new TicketAdapter(mList,this);
        ticketListView.setAdapter(ticketAdapter);
    }

    private void initData() {
        mList=new ArrayList<>();
    }


    @Override
    public void handle200Data(JSONObject dataObj, String message) {
        int is_shop = dataObj.optInt("is_shop");
        JSONArray list = dataObj.optJSONArray("voucher_list");
        if(list!=null && list.length()!=0){
            noView.setVisibility(View.GONE);
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
            if(is_shop==0){//不可以进商城
                status=2;
                btnTicket.setBackgroundDrawable(getResources().getDrawable(R.drawable.ticket_no_shop_bg));
                btnTicket.setEnabled(false);
            }else {//可以进商城
                status=1;
            }
        }else{
            status=0;
            noView.setVisibility(View.VISIBLE);
            btnTicket.setText("兑换代金券");
        }
    }

    @Override
    public void handle404(String message) {
        getDm().buildAlertDialog(message);
    }

    @Override
    public void handleNoNetWork() {

    }

    @Override
    public void onClick(View v) {
        if(status==0){//进入兑换代金券页面
            Intent intent=new Intent(this,RedScoreUpdateActivity.class);
            startActivity(intent);
        }else if(status==1){//进入商城
            Intent intent=new Intent(this,ShopActivity.class);
            startActivity(intent);
        }
    }
}
