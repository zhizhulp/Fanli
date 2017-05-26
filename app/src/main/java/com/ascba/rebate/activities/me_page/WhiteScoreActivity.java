package com.ascba.rebate.activities.me_page;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.FastBillActivity;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.activities.me_page.white_score_child.WSExchangeActivity;
import com.ascba.rebate.adapter.WhiteTicketAdapter;
import com.ascba.rebate.beans.WhiteTicket;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.MoneyBar;
import com.yanzhenjie.nohttp.rest.Request;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.ascba.rebate.fragments.main.MoneyFragment.REQUEST_EXCHANGE_TICKET;

/**
 * 财富-快速兑现券
 */
public class WhiteScoreActivity extends BaseNetActivity implements BaseNetActivity.Callback {

    public static final int REQUEST_EXCHANGE = 0;
    private ListView listView;
    private List<WhiteTicket> mList;
    private WhiteTicketAdapter wta;
    private View noView;
    private MoneyBar moneyBar;

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
        moneyBar = (MoneyBar) findViewById(R.id.moneyBar);
        moneyBar.setTailTitle(getString(R.string.inoutcome_record));
        moneyBar.setCallBack(new MoneyBar.CallBack() {
            @Override
            public void clickImage(View im) {
                finish();
            }

            @Override
            public void clickComplete(View tv) {
                Intent intent = new Intent(WhiteScoreActivity.this, FastBillActivity.class);
                startActivity(intent);
            }
        });

        noView = findViewById(R.id.no_ticket_view);
        listView = ((ListView) findViewById(R.id.cash_ticket_list));
        initData();
        wta = new WhiteTicketAdapter(mList, this);
        wta.setCallback(new WhiteTicketAdapter.Callback() {
            @Override
            public void onExchangeClick(int position) {
                WhiteTicket wt = mList.get(position);
                if (wt.getTest() == 1) {
                    getDm().buildAlertDialog("推荐用户为体验账户升级，兑现券暂未激活！");
                    return;
                }
                Intent intent = new Intent(WhiteScoreActivity.this, WSExchangeActivity.class);
                intent.putExtra("cashing_id", wt.getId());
                startActivityForResult(intent, REQUEST_EXCHANGE_TICKET);
            }
        });
        listView.setAdapter(wta);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        switch (requestCode) {
            case REQUEST_EXCHANGE_TICKET:
                if (resultCode == RESULT_OK) {
                    setResult(RESULT_OK,getIntent());
                    finish();
                }
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
                //1 2 0
                String cashing_money = cashObj.optString("cashing_money");
                long create_time = cashObj.optLong("create_time");//获取时间(s)
                long thaw_time = cashObj.optLong("thaw_time");//解冻剩余时间（s）
                int is_thaw = cashObj.optInt("is_thaw");//是否解冻
                int is_test = cashObj.optInt("is_test");//是否体验
                if (is_thaw == 1) {//便于排序
                    is_thaw = 2;
                } else if (is_thaw == 2) {
                    is_thaw = 1;
                }
                WhiteTicket wt = new WhiteTicket(cashing_money, id, is_thaw,
                        formatTime(new SimpleDateFormat("yyyy.MM.dd"), create_time),
                        formatTime(thaw_time));
                wt.setTest(is_test);
                mList.add(wt);
            }
            Collections.sort(mList);
            wta.notifyDataSetChanged();
        }
    }

    @Override
    public void handle404(String message) {
    }

    @Override
    public void handleNoNetWork() {
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
            if (leftM != 0) {
                long min = leftM / 60;
                sb.append(min);
                sb.append("分");
            } else {
                sb.append("0分");
            }
        } else {
            sb.append("0时0分");
        }
        return sb.toString();
    }
}
