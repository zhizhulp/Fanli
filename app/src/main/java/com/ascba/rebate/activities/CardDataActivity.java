package com.ascba.rebate.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.Base2Activity;
import com.ascba.rebate.activities.base.BaseActivity;
import com.ascba.rebate.activities.login.LoginActivity;
import com.ascba.rebate.handlers.CheckThread;
import com.ascba.rebate.handlers.DialogManager;
import com.ascba.rebate.handlers.PhoneHandler;
import com.ascba.rebate.utils.LogUtils;
import com.ascba.rebate.utils.NetUtils;
import com.ascba.rebate.utils.UrlEncodeUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;

import org.json.JSONException;
import org.json.JSONObject;

public class CardDataActivity extends Base2Activity implements Base2Activity.Callback {
    private TextView tvCard;
    private TextView tvSex;
    private TextView tvAge;
    private TextView tvLocation;
    private EditText etName;
    private String card;
    private String sex;
    private String age;
    private String location;
    private DialogManager dm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_data);
        initViews();
        getDataFromBefore();
    }

    private void initViews() {
        dm=new DialogManager(this);
        tvCard = ((TextView) findViewById(R.id.card));
        tvSex = ((TextView) findViewById(R.id.sex));
        tvAge = ((TextView) findViewById(R.id.age));
        tvLocation = ((TextView) findViewById(R.id.location));
        etName = ((EditText) findViewById(R.id.name));

    }

    public void getDataFromBefore() {
        Intent intent = getIntent();
        if(intent!=null){
            card = intent.getStringExtra("card");
            sex = intent.getStringExtra("sex");
            age = intent.getStringExtra("age");
            location = intent.getStringExtra("location");
            tvCard.setText(card);
            tvSex.setText(sex);
            tvAge.setText(age);
            tvLocation.setText(location);
        }
    }

    public void goRealNameConfirm(View view) {
        requestVerifyCard(UrlUtils.verifyCard);
    }

    private void requestVerifyCard(String url) {
        String s = etName.getText().toString();
        if("".equals(s)){
            dm.buildAlertDialog("请输入姓名");
            return;
        }
        Request<JSONObject> objRequest = buildNetRequest(url, 0, true);
        objRequest.add("cardid", card);
        objRequest.add("realname", s);
        if(sex.equals("男")){
            objRequest.add("sex", 1);
        }else if(sex.equals("女")){
            objRequest.add("sex", 0);
        }else{
            objRequest.add("sex", 2);
        }
        objRequest.add("age", age);
        objRequest.add("location", location);
        executeNetWork(objRequest,"请稍后");
        setCallback(this);
    }

    @Override
    public void handle200Data(JSONObject dataObj, String message) {
        Intent intent=new Intent(CardDataActivity.this,RealNameSuccessActivity.class);
        intent.putExtra("realname",etName.getText().toString());
        intent.putExtra("cardid",tvCard.getText().toString());
        String sexString = tvSex.getText().toString();
        int sex=2;
        if("女".equals(sexString)){
            sex=0;
        }else if("男".equals(sexString)){
            sex=1;
        }
        intent.putExtra("sex",sex);
        intent.putExtra("age",tvAge.getText().toString());
        intent.putExtra("location",tvLocation.getText().toString());
        startActivity(intent);
        finish();
    }
}
