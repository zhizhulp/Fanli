package com.ascba.rebate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWorkActivity;
import com.ascba.rebate.view.MoneyBar;
import com.jaeger.library.StatusBarUtil;

public class AgeChangeActivity extends BaseNetWorkActivity implements MoneyBar.CallBack {

    private EditText edAge;
    private MoneyBar mb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_age_change);
        StatusBarUtil.setColor(this, 0xffe52020);
        initViews();
    }

    private void initViews() {
        edAge = ((EditText) findViewById(R.id.et_age));
        mb = ((MoneyBar) findViewById(R.id.mb));
        mb.setCallBack(this);
    }

    @Override
    public void clickImage(View im) {

    }

    @Override
    public void clickComplete(View tv) {
        Intent intent = getIntent();
        intent.putExtra("age",edAge.getText().toString());
        setResult(RESULT_OK,intent);
        finish();
    }
}
