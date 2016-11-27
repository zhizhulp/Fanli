package com.ascba.rebate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseActivity;

public class AgeChangeActivity extends BaseActivity {

    private EditText edAge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_age_change);
        initViews();
    }

    private void initViews() {
        edAge = ((EditText) findViewById(R.id.et_age));
    }

    public void saveAge(View view) {
        Intent intent = getIntent();
        intent.putExtra("age",edAge.getText().toString());
        setResult(RESULT_OK,intent);
        finish();
    }
}
