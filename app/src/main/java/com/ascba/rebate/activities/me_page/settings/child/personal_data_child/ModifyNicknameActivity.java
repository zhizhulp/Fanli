package com.ascba.rebate.activities.me_page.settings.child.personal_data_child;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWorkActivity;
import com.ascba.rebate.view.MoneyBar;
import com.jaeger.library.StatusBarUtil;

public class ModifyNicknameActivity extends BaseNetWorkActivity implements MoneyBar.CallBack {

    private EditText edNickName;
    private MoneyBar mb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_nickname);
        StatusBarUtil.setColor(this, 0xffe52020);
        initViews();
    }

    private void initViews() {
        edNickName = ((EditText) findViewById(R.id.et_nickname));
        mb = ((MoneyBar) findViewById(R.id.mb));
        mb.setCallBack(this);
    }


    @Override
    public void clickImage(View im) {

    }

    @Override
    public void clickComplete(View tv) {
        Intent intent = getIntent();
        intent.putExtra("nickname",edNickName.getText().toString());
        setResult(RESULT_OK,intent);
        finish();
    }
}
