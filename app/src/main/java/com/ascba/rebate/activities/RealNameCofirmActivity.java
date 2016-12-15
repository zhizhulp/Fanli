package com.ascba.rebate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWorkActivity;
import com.ascba.rebate.handlers.DialogManager;
import com.ascba.rebate.utils.UrlUtils;
import com.yolanda.nohttp.rest.Request;
import org.json.JSONObject;

public class RealNameCofirmActivity extends BaseNetWorkActivity implements BaseNetWorkActivity.Callback {

    private EditText edId;
    private DialogManager dm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_name_cofirm);
        initView();
    }

    private void initView() {
        dm=new DialogManager(this);
        edId = ((EditText) findViewById(R.id.editTextWithCustomHint2));
    }

    public void goConfirm(View view) {
        requestCardInfo(UrlUtils.findCardInfo);
    }

    private void requestCardInfo(String url) {
        String s = edId.getText().toString();
        if("".equals(s)){
            dm.buildAlertDialog("请输入身份证号码");
            return;
        }
        Request<JSONObject> objRequest = buildNetRequest(url, 0, true);
        objRequest.add("cardid", s);
        executeNetWork(objRequest,"请稍后");
        setCallback(this);
    }

    @Override
    public void handle200Data(JSONObject dataObj, String message) {
        JSONObject cardObj = dataObj.optJSONObject("cardIdInfo");
        String card = cardObj.optString("cardid");
        String sex = cardObj.optString("sex");
        String age = cardObj.optString("age");
        String location = cardObj.optString("location");
        Intent intent=new Intent(RealNameCofirmActivity.this,CardDataActivity.class);
        intent.putExtra("card",card);
        intent.putExtra("sex",sex);
        intent.putExtra("age",age);
        intent.putExtra("location",location);
        startActivity(intent);
        finish();
    }
}
