package com.ascba.rebate.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;
import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWorkActivity;
import com.ascba.rebate.handlers.DialogManager;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.EditTextWithCustomHint;
import com.yolanda.nohttp.rest.Request;
import org.json.JSONObject;

public class RedScoreActivity extends BaseNetWorkActivity implements BaseNetWorkActivity.Callback,TextWatcher{

    private TextView tvMax;
    private EditTextWithCustomHint edScore;
    private TextView tvCash;
    private TextView tvTicket;
    private int max;//最多可兑换的积分（除以返现单元后的值）
    private final int unit=100*100;//返现单元
    private Double rateMoney;//金额比例
    private Double rateScore;//积分比例
    private int convertible_red_score;//最多可兑换的积分
    private DialogManager dm;
    private TextView tvResults;
    private int finalScene;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_red_score);
        initViews();
        requestRedScore();
    }

    private void initViews() {
        dm=new DialogManager(this);
        tvMax = ((TextView) findViewById(R.id.tv_red_score_available));
        edScore = ((EditTextWithCustomHint) findViewById(R.id.ed_red_score_default));
        edScore.addTextChangedListener(this);
        tvCash = ((TextView) findViewById(R.id.tv_red_score_max_money));
        tvTicket = ((TextView) findViewById(R.id.tv_red_score_max_ticket));
        tvResults = ((TextView) findViewById(R.id.tv_red_score_result));

    }

    private void requestRedScore() {
        finalScene=1;
        Request<JSONObject> request = buildNetRequest(UrlUtils.getRedScore, 0, true);
        executeNetWork(request,"请稍后");
        setCallback(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        String s1 = s.toString();
        if(!"".equals(s1)){
            int i = Integer.parseInt(s1);
            if(i*unit>convertible_red_score){
                dm.buildAlertDialog("您最多可兑换积分为" +convertible_red_score);
                edScore.setText(max+"");
                edScore.setSelection(edScore.getText().length());
                tvResults.setText(max*unit +" =");
                return;
            }
            int cash= (int) (i*unit*rateMoney)/100;
            int ticket= (int) (i*unit*rateScore)/100;
            tvCash.setText(cash+"");
            tvTicket.setText(ticket+"");
            tvResults.setText(i*unit +" =");
        }else {
            tvCash.setText(0+"");
            tvTicket.setText(0+"");
            tvResults.setText(0 +" =");
        }
    }
    @Override
    public void handle200Data(JSONObject dataObj, String message) {
        if(finalScene==1){
            JSONObject redObj = dataObj.optJSONObject("getRedScore");
            //最大兑换金额
            int convertible_money = redObj.optInt("convertible_money");
            //最大兑换积分
            convertible_red_score = redObj.optInt("convertible_red_score");
            //最终获得最大金额
            int subscription_ratio_money = redObj.optInt("subscription_ratio_money");
            //最终获得最大积分
            int subscription_ratio_score = redObj.optInt("subscription_ratio_score");

            rateMoney=redObj.optDouble("ratio_money");
            rateScore=redObj.optDouble("ratio_score");
            tvMax.setText("您可兑换红积分为  "+convertible_red_score);
            max=convertible_red_score/unit;
            edScore.setText("" + max);
            edScore.setSelection(edScore.getText().length());
            tvCash.setText(""+subscription_ratio_money);
            tvTicket.setText(""+subscription_ratio_score);
        }else if(finalScene==2){
            dm.buildAlertDialog(message);
        }

    }
    //点击兑换红积分
    public void redChange(View view) {
        finalScene=2;
        String s = edScore.getText().toString();
        if("".equals(s) || "0".equals(s)){
            dm.buildAlertDialog("请输入积分");
            return;
        }
        Request<JSONObject> request = buildNetRequest(UrlUtils.redIntegration, 0, true);
        request.add("red_integration",(Integer.parseInt(s))*unit);
        executeNetWork(request,"请稍后");
        setCallback(this);
    }
}
