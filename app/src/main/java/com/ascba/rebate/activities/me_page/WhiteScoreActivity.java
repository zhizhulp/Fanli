package com.ascba.rebate.activities.me_page;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.me_page.white_score_child.WSExchangeActivity;
import com.ascba.rebate.activities.base.BaseNetWorkActivity;
import com.ascba.rebate.adapter.WhiteTicketAdapter;
import com.ascba.rebate.beans.WhiteTicket;
import com.ascba.rebate.handlers.DialogManager;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.ScrollViewWithListView;
import com.jaeger.library.StatusBarUtil;
import com.yolanda.nohttp.rest.Request;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WhiteScoreActivity extends BaseNetWorkActivity implements BaseNetWorkActivity.Callback {

    private ListView listView;
    private List<WhiteTicket> mList;
    private WhiteTicketAdapter wta;
    private View noView;
    public static final int REQUEST_EXCHANGE=1;
    private DialogManager dm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_white_to);
        StatusBarUtil.setColor(this, 0xffe52020);
        initViews();
        requestNetWork();
    }

    private void requestNetWork() {
        Request<JSONObject> request = buildNetRequest(UrlUtils.getCashingList, 0, true);
        executeNetWork(request, "请稍后");
        setCallback(this);
    }

    private void initViews() {
        dm=new DialogManager(this);
        noView = findViewById(R.id.no_ticket_view);
        listView = ((ListView) findViewById(R.id.cash_ticket_list));
        initData();
        wta = new WhiteTicketAdapter(mList, this);
        wta.setCallback(new WhiteTicketAdapter.Callback() {
            @Override
            public void onExchangeClick(int position) {
                WhiteTicket wt = mList.get(position);
                if(wt.getTest()==1){
                    dm.buildAlertDialog("推荐用户为体验账户升级，兑现券暂未激活！");
                    return;
                }
                Intent intent = new Intent(WhiteScoreActivity.this, WSExchangeActivity.class);
                intent.putExtra("cashing_id", wt.getId());
                startActivityForResult(intent,REQUEST_EXCHANGE);
            }
        });
        listView.setAdapter(wta);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_EXCHANGE:
                setResult(REQUEST_EXCHANGE,getIntent());
                finish();
                break;
        }
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
            mList.clear();
            noView.setVisibility(View.GONE);
            JSONArray list = dataObj.optJSONArray("cashing_list");
            for (int i = 0; i < list.length(); i++) {
                JSONObject cashObj = list.optJSONObject(i);
                int id = cashObj.optInt("id");
                String cashing_money = cashObj.optString("cashing_money");
                long create_time = cashObj.optLong("create_time");//获取时间(s)
                long thaw_time = cashObj.optLong("thaw_time");//解冻剩余时间（s）
                int is_thaw = cashObj.optInt("is_thaw");//是否解冻
                int is_test = cashObj.optInt("is_test");//是否体验
                WhiteTicket wt = new WhiteTicket(cashing_money, id, is_thaw,
                        formatTime(new SimpleDateFormat("yyyy.MM.dd"), create_time),
                        formatTime(thaw_time));
                wt.setTest(is_test);
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
