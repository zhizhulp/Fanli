package com.ascba.rebate.activities.me_page.business_center_child.child;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWork3Activity;
import com.ascba.rebate.activities.base.BaseNetWorkActivity;
import com.ascba.rebate.handlers.DialogManager;
import com.ascba.rebate.utils.UrlUtils;
import com.google.gson.JsonObject;
import com.jaeger.library.StatusBarUtil;
import com.yolanda.nohttp.rest.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EmployeeRateActivity extends BaseNetWork3Activity implements BaseNetWork3Activity.Callback{

    private RadioGroup rgRate;
    private RadioButton rbRate1;
    private RadioButton rbRate2;
    private RadioButton rbRate3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_rate);
        //StatusBarUtil.setColor(this, getResources().getColor(R.color.moneyBarColor));
        initViews();
        requestNet();
    }

    private void requestNet() {
        Request<JSONObject> request = buildNetRequest(UrlUtils.getCharging, 0, true);
        executeNetWork(request,"请稍后");
        setCallback(this);
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if(intent!=null){
            String seller_return_ratio = intent.getStringExtra("seller_return_ratio");
            if(seller_return_ratio!=null){
                if(seller_return_ratio.equals("1")){
                    rbRate1.setChecked(true);
                }else if(seller_return_ratio.equals("2")){
                    rbRate2.setChecked(true);
                }else if(seller_return_ratio.equals("3")){
                    rbRate3.setChecked(true);
                }
            }
        }
    }

    private void initViews() {
        rgRate = ((RadioGroup) findViewById(R.id.rg_rate));
        rbRate1 = ((RadioButton) findViewById(R.id.rate_8));
        rbRate2 = ((RadioButton) findViewById(R.id.rate_16));
        rbRate3 = ((RadioButton) findViewById(R.id.rate_24));

        rbRate1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                intent.putExtra("business_data_rate", rbRate1.getText());
                intent.putExtra("business_data_rate_type", "1");
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        rbRate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                intent.putExtra("business_data_rate", rbRate2.getText());
                intent.putExtra("business_data_rate_type", "2");
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        rbRate3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                intent.putExtra("business_data_rate", rbRate3.getText());
                intent.putExtra("business_data_rate_type", "3");
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    public void handle200Data(JSONObject dataObj, String message) throws JSONException {
        JSONObject system_charging = dataObj.optJSONObject("system_charging");
        handleString(system_charging);
    }

    @Override
    public void handle404(String message) {
        finish();
    }

    private void handleString(JSONObject rates) {
        String s1 = rates.optString("1");
        String s2 = rates.optString("2");
        String s3 = rates.optString("3");
        String[] split1 = s1.split("-");
        String type1 = split1[0];
        String rate1 = split1[1];
        String user1 = split1[2];
        String bus1 = split1[3];

        String[] split2 = s2.split("-");
        String type2 = split2[0];
        String rate2 = split2[1];
        String user2 = split2[2];
        String bus2 = split2[3];

        String[] split3 = s3.split("-");
        String type3 = split3[0];
        String rate3 = split3[1];
        String user3 = split3[2];
        String bus3 = split3[3];

        rbRate1.setText(type1+"类型佣金"+rate1+"%,赠返"+bus1+"%");
        rbRate2.setText(type2+"类型佣金"+rate2+"%,赠返"+bus2+"%");
        rbRate3.setText(type3+"类型佣金"+rate3+"%,赠返"+bus3+"%");

        getDataFromIntent();
    }
}
