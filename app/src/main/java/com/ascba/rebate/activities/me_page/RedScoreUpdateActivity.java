package com.ascba.rebate.activities.me_page;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.ascba.rebate.R;
import com.ascba.rebate.activities.me_page.red_score_child.RedScSuccActivity;
import com.ascba.rebate.activities.base.BaseNetWorkActivity;
import com.ascba.rebate.fragments.me.FourthFragment;
import com.ascba.rebate.handlers.DialogManager;
import com.ascba.rebate.utils.UrlUtils;
import com.jaeger.library.StatusBarUtil;
import com.yolanda.nohttp.rest.Request;
import org.json.JSONObject;

public class RedScoreUpdateActivity extends BaseNetWorkActivity implements BaseNetWorkActivity.Callback{
    private int finalScene;
    private TextView tvMax;
    private TextView tvCash;
    private TextView tvTicket;
    private DialogManager dm;
    private TextView tvTodayRate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_red_score_update);
        //StatusBarUtil.setColor(this, 0xffe52020);
        initViews();
        requestRedScore();
    }

    private void initViews() {
        dm=new DialogManager(this);
        tvMax = ((TextView) findViewById(R.id.total_red_score));
        tvCash = ((TextView) findViewById(R.id.tv_cash));
        tvTicket=((TextView) findViewById(R.id.tv_ticket));
        tvTodayRate = ((TextView) findViewById(R.id.red_rate));
    }

    private void requestRedScore() {
        finalScene=1;
        Request<JSONObject> request = buildNetRequest(UrlUtils.getRedScore, 0, true);
        executeNetWork(request,"请稍后");
        setCallback(this);
    }


    @Override
    public void handle200Data(JSONObject dataObj, String message) {
        if(finalScene==1){
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
            tvMax.setText(""+ convertible_red_score);
            tvCash.setText(""+subscription_ratio_money);
            tvTicket.setText(""+subscription_ratio_score);
            tvTodayRate.setText("当前兑换比例："+rateMoney+":"+rateScore);
        }else if(finalScene==2){
            Intent intent=new Intent(this,RedScSuccActivity.class);
            startActivityForResult(intent,FourthFragment.REQUEST_RED);
        }

    }
    //点击兑换红积分
    public void redChange(View view) {
        if("0".equals(tvMax.getText().toString())){
            dm.buildAlertDialog("可兑换红积分不足");
            return;
        }
        dm.buildAlertDialog1("您确定要兑换吗？");
        dm.setCallback(new DialogManager.Callback() {
            @Override
            public void handleSure() {
                dm.dismissDialog();
                finalScene=2;
                Request<JSONObject> request = buildNetRequest(UrlUtils.redIntegration, 0, true);
                request.add("red_integration",tvMax.getText().toString());
                executeNetWork(request,"请稍后");
                setCallback(RedScoreUpdateActivity.this);
            }
        });
    }
    //将带小数点专为比例
    public String getMaxDivide_ab(double a,double b){
        if(a==b){
            return "当前兑换比例 1:1";
        }
        int value=1;
        double max;
        double min;
        int intA= (int) (a*10);
        int intB= (int) (b*10);
        if(intA > intB){
            max=intA;
            min=intB;
        }else{
            max=intB;
            min=intA;
        }
        for(int i=2;i<=min;i++){
            if(0==max%i && 0==min%i){
                value=i;
                break;
            }

        }
        StringBuffer sb=new StringBuffer();
        sb.append("今日兑换比例 ").append(intA/value).append(":").append(intB/value);
        return sb.toString();
    }
    //去账单列表
    public void goAcc(View view) {
        Intent intent=new Intent(this,AllAccountActivity.class);
        intent.putExtra("order",2);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case FourthFragment.REQUEST_RED:
                setResult(RESULT_OK,getIntent());
                finish();
                break;
        }
    }
}
