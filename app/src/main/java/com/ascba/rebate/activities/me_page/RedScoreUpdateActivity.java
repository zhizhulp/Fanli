package com.ascba.rebate.activities.me_page;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.activities.me_page.red_score_child.RedScSuccActivity;
import com.ascba.rebate.fragments.main.MoneyFragment;
import com.ascba.rebate.utils.DialogHome;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.MoneyBar;
import com.yanzhenjie.nohttp.rest.Request;

import org.json.JSONObject;

/**
 * 财富——红积分
 */
public class RedScoreUpdateActivity extends BaseNetActivity implements BaseNetActivity.Callback {
    private int finalScene;
    private TextView tvMax;
    private TextView tvCash;
    private TextView tvTicket;
    private TextView tvTodayRate;
    private MoneyBar moneyBar;
    private TextView tvTips;
    private TextView tvCashDesc;
    private TextView tvTotalRed;
    private TextView tvHeadTip;
    private Button btnExchange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_red_score_update);
        initViews();
        requestRedScore();
    }

    private void initViews() {
        moneyBar = (MoneyBar) findViewById(R.id.moneyBar);
        moneyBar.setCallBack(new MoneyBar.CallBack() {
            @Override
            public void clickImage(View im) {
                finish();
            }

            @Override
            public void clickComplete(View tv) {

            }
        });

        tvMax = ((TextView) findViewById(R.id.total_red_score));
        tvCash = ((TextView) findViewById(R.id.tv_cash));
        tvTicket = ((TextView) findViewById(R.id.tv_ticket));
        tvTodayRate = ((TextView) findViewById(R.id.red_rate));

        tvTips = ((TextView)findViewById(R.id.tv_tips));
        tvCashDesc = ((TextView) findViewById(R.id.tv_cash_desc));
        tvTotalRed = ((TextView) findViewById(R.id.total_red_score_total));
        tvHeadTip = ((TextView) findViewById(R.id.tv_tips_head));

        btnExchange = ((Button) findViewById(R.id.btn_exchange));
    }

    private void requestRedScore() {
        finalScene = 1;
        Request<JSONObject> request = buildNetRequest(UrlUtils.getRedScore, 0, true);
        executeNetWork(request, "请稍后");
        setCallback(this);
    }


    @Override
    public void handle200Data(JSONObject dataObj, String message) {
        if (finalScene == 1) {
            JSONObject redObj = dataObj.optJSONObject("getRedScore");
            //最大兑换金额
            int convertible_money = redObj.optInt("convertible_money");
            //最大兑换积分
            int convertible_red_score = redObj.optInt("convertible_red_score");
            //最终获得最大金额
            int subscription_ratio_money = redObj.optInt("subscription_ratio_money");
            //最终获得最大积分
            int subscription_ratio_score = redObj.optInt("subscription_ratio_score");
            int rateMoney = redObj.optInt("ratio_money");
            int rateScore = redObj.optInt("ratio_score");
            int notice_tip = redObj.optInt("notice_tip");
            int exchange_unit = redObj.optInt("exchange_unit");
            int red_score = redObj.optInt("red_score");
            tvMax.setText("" + convertible_red_score);
            tvCash.setText("" + subscription_ratio_money );
            tvTicket.setText("" + subscription_ratio_score);
            tvTodayRate.setText("当前兑换比例：" + rateMoney + ":" + rateScore);
            //tvTips.setText("转出赠返现金为预存款，实际到账为"+redObj.optString("money"));
            tvTips.setText("根据以上比例兑换扣除"+redObj.optString("cash_tax_rate"));
            tvHeadTip.setText(notice_tip==0? null: "提醒：满"+exchange_unit+"红积分方可兑换现金与代金券" );
            tvHeadTip.setVisibility(notice_tip==0?View.GONE:View.VISIBLE);
            tvTotalRed.setText(""+red_score);
            btnExchange.setBackgroundDrawable(notice_tip==0?getResources().getDrawable(R.drawable.register_btn_bg)
                    :getResources().getDrawable(R.drawable.ticket_no_shop_bg));
            btnExchange.setEnabled(notice_tip == 0);

        } else if (finalScene == 2) {
            Intent intent = new Intent(this, RedScSuccActivity.class);
            startActivityForResult(intent, MoneyFragment.REQUEST_RED);
        }

    }

    @Override
    public void handle404(String message) {
    }

    @Override
    public void handleNoNetWork() {

    }

    //点击兑换红积分
    public void redChange(View view) {
        if ("0".equals(tvMax.getText().toString())) {
            getDm().buildAlertDialog("可兑换红积分不足");
            return;
        }
        getDm().buildAlertDialogSure("您确定要兑换吗？", new DialogHome.Callback() {
            @Override
            public void handleSure() {
                finalScene = 2;
                Request<JSONObject> request = buildNetRequest(UrlUtils.redIntegration, 0, true);
                request.add("red_integration", tvMax.getText().toString());
                executeNetWork(request, "请稍后");
                setCallback(RedScoreUpdateActivity.this);
            }
        });
    }

    //去账单列表
    public void goAcc(View view) {
        showToast("暂未开放");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data==null){
            return;
        }
        switch (requestCode) {
            case MoneyFragment.REQUEST_RED:
                setResult(RESULT_OK, getIntent());
                finish();
                break;
        }
    }
}
