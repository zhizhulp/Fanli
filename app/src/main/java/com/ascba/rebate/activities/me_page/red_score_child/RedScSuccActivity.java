package com.ascba.rebate.activities.me_page.red_score_child;

import android.os.Bundle;
import android.view.View;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.view.MoneyBar;

public class RedScSuccActivity extends BaseNetActivity implements MoneyBar.CallBack2 {
    private static final String TAG = "RedScSuccActivity";
    private MoneyBar mb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_red_sc_succ);
        initViews();
    }
    private void initViews() {
        mb = ((MoneyBar) findViewById(R.id.mb));
        mb.setCallBack2(this);
    }
    //点击完成
    @Override
    public void clickComplete(View tv) {
        finishActivity();
    }
    //actionBar返回
    @Override
    public void clickBack(View back) {
        finishActivity();
    }
    //点击返回键
    @Override
    public void onBackPressed() {
        finishActivity();
        super.onBackPressed();
    }

    //进入查看账单页面
    public void goAcc(View view) {
        showToast("暂未开放");
    }
    @Override
    public void clickImage(View im) {

    }
    private void finishActivity(){
        setResult(RESULT_OK,getIntent());
        finish();
    }

}
