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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
        executeNetWork(request, "请稍后");
        setCallback(this);
    }

    private void initViews() {
        noView = findViewById(R.id.no_ticket_view);
        listView = ((ScrollViewWithListView) findViewById(R.id.cash_ticket_list));
        initData();
        wta = new WhiteTicketAdapter(mList, this);
        wta.setCallback(new WhiteTicketAdapter.Callback() {
            @Override
            public void onExchangeClick(int position) {

                Intent intent = new Intent(WhiteScoreActivity.this, WSExchangeActivity.class);
                intent.putExtra("cashing_id", mList.get(position).getId());
                startActivity(intent);
            }
        });
        listView.setAdapter(wta);
    }

    private void initData() {
        mList = new ArrayList<>();
    }

    @Override
    public void handle200Data(JSONObject dataObj, String message) {
        int is_cashing_list = dataObj.optInt("is_cashing_list");//是否有券
        if (is_cashing_list == 0) {//无券
            noView.setVisibility(View.VISIBLE);
        } else {//有券
            noView.setVisibility(View.GONE);
            JSONArray list = dataObj.optJSONArray("cashing_list");
            for (int i = 0; i < list.length(); i++) {
                JSONObject cashObj = list.optJSONObject(i);
                int id = cashObj.optInt("id");
                String cashing_money = cashObj.optString("cashing_money");
                long create_time = cashObj.optLong("create_time");//获取时间(s)
                long thaw_time = cashObj.optLong("thaw_time");//解冻剩余时间（s）
                int is_thaw = cashObj.optInt("is_thaw");//是否解冻
                WhiteTicket wt = new WhiteTicket(cashing_money, id, is_thaw,
                        formatTime(new SimpleDateFormat("yyyy.MM.dd"), create_time),
                        formatTime(thaw_time));
                mList.add(wt);
            }
            wta.notifyDataSetChanged();
        }
    }

    private String formatTime(SimpleDateFormat sdf, Long time) {
        Date date = new Date(time * 1000);
        return sdf.format(date);
    }

    private String formatTime(Long time) {
        StringBuffer sb = new StringBuffer();
        long day = time / (24 * 60 * 60);
        sb.append(day);
        sb.append("天");
        long leftH = time % (24 * 60 * 60);
        if (leftH != 0) {
            long hour = leftH / (60 * 60);
            sb.append(hour);
            sb.append("时");
            long leftM = leftH % (60 * 60);
            if(leftM!=0){
                long min = leftM / 60;
                sb.append(min);
                sb.append("分");
            }else {
                sb.append("0分");
            }
        } else {
            sb.append("0时0分");
        }
        return sb.toString();

    }
}
