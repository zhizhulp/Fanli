package com.ascba.rebate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWorkActivity;

public class ModifyNicknameActivity extends BaseNetWorkActivity {

    private EditText edNickName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_nickname);
        initViews();
    }

    private void initViews() {
        edNickName = ((EditText) findViewById(R.id.et_nickname));
    }

    public void saveNickName(View view) {
        Intent intent = getIntent();
        intent.putExtra("nickname",edNickName.getText().toString());
        setResult(RESULT_OK,intent);
        finish();
    }
}
