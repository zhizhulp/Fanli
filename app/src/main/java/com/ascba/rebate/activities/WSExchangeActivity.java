package com.ascba.rebate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWorkActivity;
import com.ascba.rebate.handlers.DialogManager;
import com.ascba.rebate.utils.UrlUtils;
import com.yolanda.nohttp.rest.Request;

import org.json.JSONObject;

public class WSExchangeActivity extends BaseNetWorkActivity implements BaseNetWorkActivity.Callback{
    private DialogManager dm;
    private View noView;
    private TextView tvTips;
    private TextView tvTotal;
    private TextView tvTicketScore;
    private int finalScene;
    private int cashingId;
    private int cashing_score;
    private String cashing_money;
    private Button btnEx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wsaccount);
        initViews();
        getDataFromIntent();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if(intent!=null){
            cashingId = intent.getIntExtra("cashing_id",-1);
            if(cashingId!=-1){
                requestNetwork(1);
            }
        }
    }

    private void requestNetwork(int scene) {
        finalScene=scene;
        if(finalScene==1){
            Request<JSONObject> request = buildNetRequest(UrlUtils.getCashingInfo, 0, true);
            request.add("cashing_id",cashingId);
            executeNetWork(request,"请稍后");
        }else if(finalScene==2){
            Request<JSONObject> request = buildNetRequest(UrlUtils.getCashingInfo, 0, true);
            request.add("cashing_id",cashingId);
            request.add("cashing_score",cashing_score);
            request.add("money",cashing_money);
            executeNetWork(request,"请稍后");
        }

    }

    private void initViews() {
        dm=new DialogManager(this);
        noView = findViewById(R.id.no_card_view);
        tvTips = ((TextView) findViewById(R.id.tv_tips));
        tvTotal = ((TextView) findViewById(R.id.tv_total));
        tvTicketScore = (TextView)findViewById(R.id.tv_ticket_score);
    }

    public void go(View view) {
        btnEx = ((Button) view);
        dm.buildAlertDialog("暂时不能兑换喔");
    }

    @Override
    public void handle200Data(JSONObject dataObj, String message) {
        if(finalScene==1){
            int tip_status = dataObj.optInt("tip_status");
            JSONObject cashObj = dataObj.optJSONObject("cashing_info");
            int id = cashObj.optInt("id");//索引ID
            cashing_money = cashObj.optString("cashing_money");//兑换金额
            int p_referee = cashObj.optInt("p_referee");
            int pp_referee = cashObj.optInt("pp_referee");
            cashing_score = cashObj.optInt("cashing_score");//兑换最大积分
            if(tip_status==1){
                String cashing_info_tip = dataObj.optString("cashing_info_tip");
                noView.setVisibility(View.VISIBLE);
                tvTips.setText(cashing_info_tip);
                btnEx.setEnabled(false);
                btnEx.setBackgroundDrawable(getResources().getDrawable(R.drawable.ticket_no_shop_bg));
            }else{
                btnEx.setEnabled(true);
                noView.setVisibility(View.GONE);
            }
            tvTotal.setText(cashing_money);
            tvTicketScore.setText(cashing_score+"");
        }else if(finalScene==2){
            dm.buildAlertDialog(message);
        }

    }
}
