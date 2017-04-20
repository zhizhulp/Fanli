package com.ascba.rebate.activities.me_page.business_center_child.child;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWorkActivity;
import com.ascba.rebate.view.EditTextWithCustomHint;
import com.ascba.rebate.view.MoneyBar;


public class BusinessLocationWorkActivity extends BaseNetWorkActivity {
    private EditTextWithCustomHint edLocate;
    private EditTextWithCustomHint edCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_location);
        //StatusBarUtil.setColor(this, 0xffe52020);
        initViews();
    }

    private void initViews() {
        edCity = ((EditTextWithCustomHint) findViewById(R.id.ed_city));
        edLocate = ((EditTextWithCustomHint) findViewById(R.id.ed_locate));
        ((MoneyBar) findViewById(R.id.mb_location)).setCallBack(new MoneyBar.CallBack() {
            @Override
            public void clickImage(View im) {

            }

            @Override
            public void clickComplete(View tv) {
                String s = edCity.getText().toString();
                Intent intent=getIntent();
                intent.putExtra("location",s);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }
}
