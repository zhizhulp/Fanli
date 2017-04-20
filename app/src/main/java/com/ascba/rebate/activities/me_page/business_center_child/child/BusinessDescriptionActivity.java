package com.ascba.rebate.activities.me_page.business_center_child.child;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.view.EditTextWithCustomHint;
import com.ascba.rebate.view.MoneyBar;


public class BusinessDescriptionActivity extends BaseNetActivity {

    private EditTextWithCustomHint tvDesc;
    private MoneyBar mbDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_description);
        //StatusBarUtil.setColor(this, 0xffe52020);
        initViews();
        getDataFromIntent();
    }
    private void getDataFromIntent() {
        Intent intent = getIntent();
        if(intent!=null){
            String desc = intent.getStringExtra("desc");
            if(desc!=null){
                tvDesc.setText(desc);
                tvDesc.setSelection(desc.length());
            }

        }
    }

    private void initViews() {
        tvDesc = ((EditTextWithCustomHint) findViewById(R.id.ed_desc));
        mbDesc = ((MoneyBar) findViewById(R.id.mb_desc));
        mbDesc.setCallBack(new MoneyBar.CallBack() {
            @Override
            public void clickImage(View im) {

            }

            @Override
            public void clickComplete(View tv) {
                String s = tvDesc.getText().toString();
                if(!"".equals(s)){
                    Intent intent = getIntent();
                    intent.putExtra("desc",s);
                    setResult(RESULT_OK,intent);
                    finish();
                }
            }
        });
    }
}
