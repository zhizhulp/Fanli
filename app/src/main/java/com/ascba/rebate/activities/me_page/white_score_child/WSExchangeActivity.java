package com.ascba.rebate.activities.me_page.white_score_child;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.utils.DialogHome;
import com.ascba.rebate.utils.UrlUtils;
import com.yanzhenjie.nohttp.rest.Request;
import org.json.JSONObject;
import static com.ascba.rebate.fragments.MoneyFragment.REQUEST_EXCHANGE_TICKET;

public class WSExchangeActivity extends BaseNetActivity implements BaseNetActivity.Callback{
    private View noView;
    private TextView tvTips;
    private TextView tvTotal;
    private TextView tvTicketScore;
    private int finalScene;
    private int cashingId;
    private int cashing_score;
    private String cashing_money;
    private Button btnGo;
    private TextView tvMoney;
    private TextView tvMax;
    private TextView tvHandFee;

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
            setCallback(this);
        }else if(finalScene==2){
            Request<JSONObject> request = buildNetRequest(UrlUtils.cashingMoney, 0, true);
            request.add("cashing_id",cashingId);
            request.add("client_money",cashing_money);
            executeNetWork(request,"请稍后");
            setCallback(this);
        }

    }

    private void initViews() {
        noView = findViewById(R.id.when_no_card);
        tvTips = ((TextView) findViewById(R.id.tv_tips));
        tvTotal = ((TextView) findViewById(R.id.tv_total));
        tvTicketScore = (TextView)findViewById(R.id.tv_ticket_score);
        btnGo = ((Button) findViewById(R.id.btn_go));
        tvMoney = ((TextView) findViewById(R.id.tv_money));
        tvMax = ((TextView) findViewById(R.id.tv_max_money));

        tvHandFee = ((TextView) findViewById(R.id.tv_fee_hand));
    }

    public void go(View view) {
        getDm().buildAlertDialogSure("您确定要兑换吗？",new DialogHome.Callback() {
            @Override
            public void handleSure() {
                requestNetwork(2);
            }
        });


    }

    @Override
    public void handle200Data(JSONObject dataObj, String message) {
        if(finalScene==1){
            int tip_status = dataObj.optInt("tip_status");
            JSONObject cashObj = dataObj.optJSONObject("cashing_info");
            int id = cashObj.optInt("id");//索引ID
            int white_score = cashObj.optInt("white_score");//白积分总额
            cashing_money = cashObj.optString("cashing_money");//兑换金额
            cashing_score = cashObj.optInt("cashing_score");//兑换最大积分
            String actual_cash=cashObj.optString("actual_cash");//实际到账
            String cash_tax_rate=cashObj.optString("cash_tax_rate");//兑现手续费
            if(tip_status==1){
                String cashing_info_tip = dataObj.optString("cashing_info_tip");
                noView.setVisibility(View.VISIBLE);
                tvTips.setText(cashing_info_tip);
                btnGo.setEnabled(false);
                btnGo.setBackgroundDrawable(getResources().getDrawable(R.drawable.ticket_no_shop_bg));
                tvMax.setText("转出佣金为预存款，实际到账为"+actual_cash+"元");
            }else{
                btnGo.setEnabled(true);
                noView.setVisibility(View.GONE);
                tvMax.setText("本次兑换最大额度为"+cashing_money+"元");
            }
            tvHandFee.setText("手续费为"+cash_tax_rate);
            tvTotal.setText(white_score+"");
            tvTicketScore.setText(cashing_score+"");
            tvMoney.setText(cashing_money);

        }else if(finalScene==2){
            Intent intent=new Intent(this,WSSuccActivity.class);
            intent.putExtra("money",cashing_money);
            startActivityForResult(intent, REQUEST_EXCHANGE_TICKET);
        }
    }

    @Override
    public void handle404(String message) {
        getDm().buildAlertDialog(message);
        btnGo.setEnabled(false);
        btnGo.setBackgroundDrawable(getResources().getDrawable(R.drawable.ticket_no_shop_bg));
    }

    @Override
    public void handleNoNetWork() {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        btnGo.setEnabled(false);
        btnGo.setBackgroundDrawable(getResources().getDrawable(R.drawable.ticket_no_shop_bg));
        if(data==null){
            return;
        }
        switch (requestCode){
            case REQUEST_EXCHANGE_TICKET:
                if(resultCode==RESULT_OK){
                    setResult(RESULT_OK,getIntent());
                    finish();
                    break;
                }

        }
    }
}
