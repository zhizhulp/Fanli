package com.ascba.rebate.activities.me_page.settings.child.safe_setting_child;

import android.os.Bundle;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWorkActivity;
import com.jaeger.library.StatusBarUtil;

public class PhoneChange2Activity extends BaseNetWorkActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_change2);
        StatusBarUtil.setColor(this, 0xffe52020);
    }
}
